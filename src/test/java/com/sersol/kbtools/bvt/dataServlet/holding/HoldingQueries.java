package com.sersol.kbtools.bvt.dataServlet.holding;

import com.sersol.kbtools.bvt.configuration.KBToolsEnvironment;
import com.sersol.kbtools.bvt.dataServlet.commons.DataServlet;
import com.sersol.kbtools.bvt.dataServlet.holding.hodlingImportFile.HoldingImportFile;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportRequest.HoldingImportRequest;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportRequest.HoldingImportRequestResult;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportRequest.HoldingImportRequestResultParser;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState.HoldingImportState;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState.HoldingImportStateResult;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingImportState.HoldingImportStateResultParser;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingsSearch.HoldingSearchResultParser;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingsSearch.HoldingsSearch;
import com.sersol.kbtools.bvt.dataServlet.holding.holdingsSearch.HoldingsSearchResult;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.List;

/**
 * Container for query methods related to holdings
 */
public class HoldingQueries {
    private DataServlet servlet;

    public HoldingQueries(DataServlet servlet){
        this.servlet = servlet;
    }

    /**
     * Searches holdings by the search criteria passed as parameters.
     * Available parameters {@link HoldingsSearch}
     * @param data
     */
    public List<HoldingsSearchResult> searchHoldings(HoldingsSearch data){
        String parameters = "";

        if(data.getDatabaseCode() != null){
           parameters = servlet.addStringParameter(data.getDatabaseCode(), "DatabaseCode");
        }

        if(data.getTitleBegins() != null){
           parameters += servlet.addStringParameter(data.getTitleBegins(), "TitleBegins");
        }

        Document doc = servlet.execute(parameters, data.getStoredProcedure(), data.getDatabase());
        return servlet.parseResult(doc, new HoldingSearchResultParser(), HoldingsSearchResult.class);
    }

    /**
     * Retrieves a list of imports. It's similar to navigating to Holding Importer | Import Queue in UI
     * @param data
     * @return
     */
    public List<HoldingImportStateResult> getImportQueue(HoldingImportState data){
        Document doc = servlet.execute(new String(), data.getStoredProcedure(), data.getDatabase());
        return servlet.parseResult(doc, new HoldingImportStateResultParser(), HoldingImportStateResult.class);
    }

    /**
     * Cancels the specified import. Get the ids by calling
     * {@link com.sersol.kbtools.bvt.dataServlet.holding.HoldingQueries#getImportQueue(HoldingImportState)}
     * @param holdingImportId
     * @param accountId
     * @return
     */
    public boolean cancelImport(int holdingImportId, int accountId){
        Document doc = executeHoldingImportRequest(holdingImportId, accountId, "Cancel");
        String info = servlet.getErrorMessage(doc);

        if(servlet.errorIndicatesNonQuery(info)){
            return true;
        }else{
            // NOTE: this is what's expected if the already cancelled import was requested to be cancelled again.
            List<HoldingImportRequestResult> resultList = servlet.parseResult(doc, new HoldingImportRequestResultParser(), HoldingImportRequestResult.class);
            if(info.contains("This import was canceled") && resultList.get(0).getErrorMessage().startsWith("This import was canceled ")){
                return true;
            }
        }

        return false;
    }

    /**
     * Accepts the specified import. Get the ids by calling
     * {@link com.sersol.kbtools.bvt.dataServlet.holding.HoldingQueries#getImportQueue(HoldingImportState)}.
     * This action is the same as clicking "Yes" to "Accept Changes?" in Holdings Importer - Review Changes
     * @param holdingImportId
     * @param accountId
     * @return
     */
    public boolean acceptImport(int holdingImportId, int accountId){

        Document doc = executeHoldingImportRequest(holdingImportId, accountId, "Import");
        String info = servlet.getErrorMessage(doc);

        return servlet.errorIndicatesNonQuery(info);
    }

    private Document executeHoldingImportRequest(int holdingImportId, int accountId, String action){
        String parameters = "";

        HoldingImportRequest data = new HoldingImportRequest();
        data.setHoldingImportDatabaseId(holdingImportId);
        data.setAccountId(accountId);
        data.setAction(action);

        parameters = servlet.addNumericParameter(data.getHoldingImportDatabaseId(), "HoldingImportDatabaseId");
        parameters += servlet.addStringParameter(data.getAction(), "Action");
        parameters += servlet.addNumericParameter(data.getAccountId(), "AccountId");

        return servlet.execute(parameters, data.getStoredProcedure(), data.getDatabase());
    }

    /**
     * Copies the provided input file to the samba share and loads the data from the file
     * so that the SQL agent can pick it up from the import queue.
     * Note: the stored procedure has access to this samba share.
     * @param sourceFile You have to provide the absolute path to the local source file
     * @param accountId
     * @return
     */
    public boolean loadFile(String sourceFile, int accountId){
        copyImportFile(sourceFile);
        String fileOnShare = "//" + KBToolsEnvironment.getSmbShare() + "/" + Paths.get(sourceFile).getFileName().toString();

        // NOTE: need to change the directory separator for Windows - otherwise the file would fail to be deleted from the share.
        fileOnShare = fileOnShare.replaceAll("/", "\\\\");

        HoldingImportFile data = new HoldingImportFile();
        data.setSource(fileOnShare);
        data.setAccountId(accountId);

        String parameters = servlet.addStringParameter(data.getSource(), "Source");
        parameters += servlet.addNumericParameter(data.getAccountId(), "AccountId");

        Document doc = servlet.execute(parameters, data.getStoredProcedure(), data.getDatabase());
        String info = servlet.getErrorMessage(doc);

        return servlet.errorIndicatesNonQuery(info);
    }

    /**
     * Copies the input file to the samba share, so that the stored procedure
     * can pick it up from the share.
     * @param sourceFile
     */
    private void copyImportFile(String sourceFile){

        String fileName = Paths.get(sourceFile).getFileName().toString();
        File fileSource = new File(sourceFile);
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",
                                        KBToolsEnvironment.getSmbUsername(),
                                        KBToolsEnvironment.getSmbPassword());

        FileInputStream input = null;
        SmbFileOutputStream output = null;

        try {
            SmbFile smbFileContext = new SmbFile("smb://" + KBToolsEnvironment.getSmbShare() + "/", auth);
            SmbFile smbFileTarget = new SmbFile(smbFileContext, fileName);

            input = new FileInputStream(fileSource);
            output = new SmbFileOutputStream(smbFileTarget);


            final byte[] b  = new byte[16*1024];
            int read = 0;
            while ((read=input.read(b, 0, b.length)) > 0) {
                output.write(b, 0, read);
            }

            input.close();
            output.close();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
