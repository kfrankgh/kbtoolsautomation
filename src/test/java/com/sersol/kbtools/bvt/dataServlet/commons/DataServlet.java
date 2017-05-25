package com.sersol.kbtools.bvt.dataServlet.commons;

import com.sersol.kbtools.bvt.configuration.KBToolsEnvironment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the endpoint for QueryServlet, which lets the end user execute
 * any SQL queries or stored procedures
 *
 * DataServlet format:
 * http://kbtools-server/jtools/Data/<FILE>?mime=<MIME>&pool=<BROKER>&query=<QUERY>
 *
 * example query:
 * http://kbtools-server/jtools/Data/out.html?mime=text/html&pool=LibraryDB&query=EXEC prcHoldingsSearch @DatabaseCode="3AL"
 */
public class DataServlet {
    private static final String SERVLET_BASE_URL = "/Data/out.html";
    private static final String MIME = "text/html";

    private String baseUrl;
    private static DataServlet servlet;

    private DataServlet(){
        KBToolsEnvironment environment = new KBToolsEnvironment();
        String url =  environment.getBaseUrl();
        baseUrl = url + SERVLET_BASE_URL;
    }

    /**
     * Gets the instance of DataServlet
     * @return
     */
    public static DataServlet getInstance(){
        if(servlet == null){
            servlet = new DataServlet();
        }
        return servlet;
    }

    /**
     * Executes the DataServlet query
     * @param parameters Parameter parts of stored procedure statement.
     *                   ex) @PARAM="Test", @ANOTHER="TEST2"
     *                   Use double quotes to surround a string value instead of single quotes.
     *                   Use add methods like {@link #addStringParameter(String, String)}
     * @param storedProc
     * @param database
     * @return DOM representing the "out.html". The html may contain the table, error string or both.
     */
    public Document execute(String parameters, String storedProc, String database){
        if(parameters.endsWith(",")){
            parameters = parameters.substring(0, parameters.length()-1);
        }

        String query = String.format("EXEC %s %s", storedProc, parameters);
        String encoded = encodeQueryUrl(query);
        String queryUrl = String.format("%s?mime=%s&pool=%s&query=%s", baseUrl, MIME, database, encoded);
        String readableUrl = String.format("%s?mime=%s&pool=%s&query=%s", baseUrl, MIME, database, query);

        Document doc = null;
        try{
            doc = Jsoup.connect(queryUrl).get();
            System.out.println(String.format("EXECUTED: %s", readableUrl));
            //TODO: check for the Help or exception in the response html
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        return doc;
    }
    /**
     * Used to convert the "query" parts of the DataServlet endpoint, so that the string is a legal
     * SQL statement when sent over HTTP
     * @param text
     * @return
     */
    private String encodeQueryUrl(String text) {
        String result;

        try {
            result = URLEncoder.encode(text, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%40", "@")
                    .replaceAll("\\%2F", "/")
                    .replaceAll("\\%3D", "=")
                    .replaceAll("\\%E2\\%80\\%9C", "\\%22")
                    .replaceAll("\\%E2\\%80\\%9D", "\\%22")
                    .replaceAll("\\%2C", ",")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = text;
        }

        return result;
    }

    /**
     * Constructs the string in this format: @Parameter="parameter value", (including ',' at the end)
     * @param parameter
     * @return
     */
    public String addStringParameter(String parameter, String paramName){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("@%s=", paramName));
        builder.append(String.format("\"%s\"", parameter));
        builder.append(",");
        return builder.toString();
    }

    /**
     * Constructs the string in this format: @Parameter=###, (including ',' at the end)
     * @param parameter
     * @param paramName
     * @return
     */
    public String addNumericParameter(Number parameter, String paramName){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("@%s=", paramName));
        builder.append(String.format("%s", parameter));
        builder.append(",");
        return builder.toString();
    }

    /**
     * Parses the result table returned by executing any SQL query via DataServlet.
     * @param doc Call {@link #execute(String, String, String)} to retrieve the result html page
     * @param resultParser Custom parser for the specific query result table implementing the interface
     *                     {@link com.sersol.kbtools.bvt.dataServlet.commons.ResultParser}
     * @param clazz The class representing the result table row data
     * @return
     */
    public <T> List<T> parseResult(Document doc, ResultParser resultParser, Class<T> clazz){
        List<Element> headers = servlet.getResultTableHeaders(doc);
        List<Element> rows = servlet.getResultTableRows(doc);
        List<T> resultList = new ArrayList<>();
        for(int i = 0; i < rows.size(); i++){
            Element row = rows.get(i);
            List<Element> cells = row.getElementsByTag("td");
            try{
                T result = clazz.newInstance();
                for(int j = 0; j < cells.size(); j++){
                    Element cell = cells.get(j);
                    Element header = headers.get(j);
                    String headerName = header.childNodeSize() > 0 ? header.childNode(0).toString() : "";
                    String value = cell.childNodeSize() > 0 ? cell.childNode(0).toString() : "";
                    resultParser.parse(headerName, value, result);
                }
                resultList.add(result);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        return resultList;
    }

    /**
     * Retrieves the error string in red starting "Could not execute query: ".
     * Note calling some stored procedure may produce this error string even though the message itself is
     * informational or when the called stored procedure doesn't return a result set in case of "insert", "update", or "delete".
     * @param doc
     * @return
     */
    public String getErrorMessage(Document doc){
        List<Element> elements = doc.getElementsByTag("font");
        return elements.get(0).childNode(0).toString();
    }

    /**
     * Returns a list of table header elements represented by the html tag "th"
     * @param doc
     * @return
     */
    private List<Element> getResultTableHeaders(Document doc){
        List<Element> rows =  getAllRows(doc);
        return rows.get(0).getElementsByTag("th");
    }

    private List<Element> getResultTableRows(Document doc){
        List<Element> rows =  getAllRows(doc);
        return rows.subList(1, rows.size());
    }

    private List<Element> getAllRows(Document doc){
        List<Element> tables = doc.getElementsByTag("table");
        return tables.get(0).getElementsByTag("tr");
    }

    /**
     * Checks to see if the error in red ends in the string "The executeQuery method must return a result set.".
     * This indicates the passed query has resulted in executing some statement which didn't return a result set.
     * The statement was probably either "insert", "update, or "delete".
     * @return
     */
    public boolean errorIndicatesNonQuery(String error){
        return error.endsWith("The executeQuery method must return a result set.");
    }

}
