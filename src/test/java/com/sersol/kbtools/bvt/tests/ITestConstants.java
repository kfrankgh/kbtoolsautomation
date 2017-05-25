package com.sersol.kbtools.bvt.tests;

import com.sersol.kbtools.bvt.pages.MainMenuForm;

/**
 * Common Test Constants used between various Tests
 */
public interface ITestConstants {

    String USERNAME = "ken.franklin@serialssolutions.com";
    String PASSWORD = "Welcome!";

    enum TitleType {Serial,Monograph,Unknown};

    String DB_CODE = "FLF";
    String PROVIDER_CODE = "PRVEBS";
    String TITLE_CODE = "1BDLF";
    String ISSN = "0769-0959";
    String TITLE_NAME = "1895";
    String TITLE_URL = "http://search.ebscohost.com/direct.asp?db=f3h&jid=1GYR&scope=site";
    String DATABASE_CODE = "FLF";
    String DATABASE_NAME = "Film & Television Literature Index with Full Text";
    String ADVANCED_MARC_DISPLAY_FIELD = "022";
    String ADVANCED_MARC_TITLE_NAME = "Knitting times";
    String MARC_DISPLAY_FIELD = "020";
    String MARC_TITLE_NAME = "The jungle effect : a doctor discovers the healthiest diets from around the world--why they work and how to bring them home";
    String MONOGRAPH_TITLE_CODE = "TC0000276572";
    String MONOGRAPH_TITLE_NAME = "The 1985 Pacific Salmon Treaty : sharing conservation burdens and benefits";
    String MEDIA_TYPE_REPORT_HEADER = "Titles Needing Review";
    String MEDIA_TYPE_REPORT_FORM_1 = "Rows to View:";
    String MEDIA_TYPE_REPORT_FORM_2 = "Hide Missing Records:";
    String AUTHORITY_TEXT = "Authority";
    String TITLE_TEXT = "Title";
    String ID_TEXT = "ID";
    String MARC_RECORD = "MARC Record";
    String ACTION_TEXT = "Action";
    String HTTP_NOT_FOUND_ERROR = "404";
    String RESOURCE_FILES_PATH = "src/test/resources";
    String PUNCTUATION_TXT_FILE_PATH = "/punctuation.txt";
    String LCCN_EXAMPLE_CSV_FILE_PATH = "/lccnExample.csv";
    String LCCN_MONOGRAPH_EXAMPLE_CSV_FILE_PATH = "/lccnMonographExample.csv";
    String AUTH_PROV_CODES_TXT_FILE_PATH = "/AuthProviderCodesSample.txt";
    String DB_CODES_TXT_FILE_PATH = "/DBCodesSample.txt";
    String FAST_HOLDING_EDIT_SERIALS_FILE_PATH = "/FastHoldingEdit_SerialsExamples.csv";
    String ISBN_SAMPLE_FILE_PATH = "/ISBNsSample.txt";
    String ISNN_SAMPLE_FILE_PATH = "/ISSNsSample.txt";
    String MONOGRAPHS_TITLES_CODES_FILE_PATH = "/MonographTitleCodesSample.txt";
    String SERIALS_TITLES_CODES_FILE_PATH = "/SerialTitleCodesSample.txt";
    String ADD_MONOGRAPHS_CSV = "/AddHolding_MonographExamples.csv";

    //Holding Search Page
    String HSF_PAGE_TITLE = "Holding Search View - KBTools";
    String TITLE_CRITICAL_PSYCHOLOGY = "Annual review of critical psychology ";
    String ISSN_CRITICAL_PSYCHOLOGY = "1464-0538";
    String DB_CODE_MARC_SEARCH_RESULTS = "OK1";
    String DB_NAME_MARC_SEARCH_RESULTS = "Elektronische Zeitschriftenbibliothek - Frei zugängliche E-Journals";
    String HSF_BUTTON_NORMALIZE = "Normalize";
    String HSF_LINK_AUTHORITY = "Authority";

    // Page Titles
    String PT_HOLDING_SEARCH_VIEW = "Holding Search View - KBTools";
    String PT_AUTH_TITLE_SEARCH = "Authority Title Search - KBTools";
    String PT_VIEW_TITLE = "View Title - KBTools";
    String PT_MARC_SEARCH = "MARC Search - KBTools";
    String PT_ADV_MARC_SEARCH = "Advanced MARC Search - KBTools";
    String PT_DB_SEARCH = "Database Search - KBTools";
    String PT_MEDIA_TYPE_REPORT = "KBTools - MediaType Report";
    String PT_TITLE_NORMALIZATION = "Title Normalization";
    String PT_TITLE_NORMALIZATION_HOLDING_VIEW = "Holding View - Title Normalization - KBTools";
    String PT_DATA_QUERY_SERVLET = "Data Query Servlet";

    //Classic-2078
    String DB_CODE_MONOGRAPHS_AND_SERIALS = "ITC";
    String DB_CODE_MONOGRAPHS = "-5Q";
    String DB_CODE_SERIALS = "LBL";
    String FIRST_10000_HOLDINGS = "* Only the first 10000 holdings are displayed *";

    //Rules
    String RULE_SEARCH_DB_CODE = "~7-";
    String RULE_MATCH_TITLE_SHIPS = "How to Avoid Huge Ships";
    String RULE_CHANGE_TITLE_BANANAS = "Be Bold with Bananas";
    String RULE_DESCRIPTION1 = "Out of";
    String RULE_CHANGE_NOTE1 = "Nowhere";

    String RULE_SEARCH_DB_CODE2 = "~-~";
    String RULE_MATCH_TITLE2 = "How High";
    String RULE_CHANGE_TITLE2 = "the Moon";
    String RULE_DESCRIPTION2 = "Stella by";
    String RULE_CHANGE_NOTE2 = "Starlight";
    String RULE_MATCH_TITLE3 = "All the Things";
    String RULE_CHANGE_TITLE3 = "You Are";
    String RULE_DESCRIPTION3 = "You Stepped Out";
    String RULE_CHANGE_NOTE3 = "Of a Dream";

    String RULE_TYPE_CHANGE = "Change";

    String RULE_SEARCH_DB_CODE3 = "~-~";
    String RULE_MATCH_TITLE4 = "Scrapple From";
    String RULE_CHANGE_TITLE4 = "The Apple";
    String RULE_DESCRIPTION4 = "These Foolish Things";
    String RULE_CHANGE_NOTE4 = "Remind Me of You";
    String RULE_MATCH_TITLE5 = "Autumn in";
    String RULE_CHANGE_TITLE5 = "New York";
    String RULE_DESCRIPTION5 = "Straight";
    String RULE_CHANGE_NOTE5 = "No Chaser";

    String RULE_SEARCH_DB_CODE_7XB = "7XB";
    String RULE_SEARCH_ISSN1 = "1527-6546";
    String RULE_SEARCH_ISBN1 = "9781607090953";
    String RULE_SEARCH_PROVIDER1 = "PRVCDH";
    String RULE_SEARCH_SERIAL_TITLE1 = "Africa Report";
    String RULE_SEARCH_MONOGRAPH_TITLE1 = "Encyclopedia of the Novel";
    String RULE_SEARCH_SERIAL_TITLECODE1 = "ARHN2";
    String RULE_SEARCH_MONOGRAPH_TITLECODE1 = "TC0000244653";
    String RULE_SEARCH_TITLE1 = "jazz";
    String RULE_SEARCH_URL1 = "journal";
    String RULE_SEARCH_URL2 = "book";
    String RULE_SEARCH_RULE_ID1 = "14438";
    String RULE_SEARCH_ISSN2 = "0269-3321";
    String RULE_SEARCH_TITLE2 = "a";
    String RULE_SEARCH_URL3 = "gateway";


    //Subject Search Page
    Integer HIERARCHY_DROPDOWN_SIZE = 4;
    Integer SEARCHTYPE_DROPDOWN_SIZE = 9;
    Integer FILTER_RESULTS_DROPDOWN_SIZE = 5;

    String SELECT_VALUE_HILCC = "HILCC";
    String SELECT_VALUE_MESH = "MeSH";
    String SELECT_VALUE_ULRICH = "Ulrich";
    String SELECT_VALUE_ALL = "all";

    String SELECT_VALUE_SUBJECT_ID_EQUALS = "SubjectID Equals";
    String SELECT_VALUE_NAME_EQUALS = "Name Equals";
    String SELECT_VALUE_NAME_CONTAINS = "Name Contains";
    String SELECT_VALUE_NAME_BEGINS_WITH = "Name Begins With";
    String SELECT_VALUE_NAME_CONTAINS_CALL_NUMBER = "Contains Call Number";
    String SELECT_VALUE_MESH_UNIQUE_ID = "MeSH Unique Id";
    String SELECT_VALUE_MESH_PATH_ID = "MeSH Path Id";
    String SELECT_VALUE_TITLECODE_EQUALS = "TitleCode Equals";
    String SELECT_VALUE_PARENT_ID_EQUALS = "ParentID Equals";

    String SELECT_VALUE_SUBJECT_NOT_SET_TO_DISPLAY = "only subjects not set to display.";
    String SELECT_VALUE_SUBJECT_SET_TO_DISPLAY = "only subjects set to display.";
    String SELECT_VALUE_SUBJECT_ASSOCIATED_WITH_TITLES = "only subjects associated with titles.";
    String SELECT_VALUE_ALL_SUBJECTS = "all subjects.";
    String SELECT_VALUE_SUBJECT_NOT_ASSOCIATED_WITH_TITLES = "only subjects not associated with titles.";

    String SELECT_ACCESSOR_HILCC = "1";
    String SELECT_ACCESSOR_MESH = "2";
    String SELECT_ACCESSOR_ULRICH = "3";
    String SELECT_ACCESSOR_ALL = "ALL";

    String SELECT_ACCESSOR_SUBJECT_ID_EQUALS = "SubjectId";
    String SELECT_ACCESSOR_NAME_EQUALS = "NameEquals";
    String SELECT_ACCESSOR_NAME_CONTAINS = "NameContains";
    String SELECT_ACCESSOR_NAME_BEGINS_WITH = "NameBeginsWith";
    String SELECT_ACCESSOR_CONTAINS_CALL_NUMBER = "ContainsCallNumber";
    String SELECT_ACCESSOR_MESH_UNIQUE_ID = "MeSHId";
    String SELECT_ACCESSOR_MESH_PATH_ID = "SubjectCode";
    String SELECT_ACCESSOR_TITLECODE_EQUALS = "TitleCode";
    String SELECT_ACCESSOR_PARENT_ID_EQUALS = "ParentSubjectId";



    String SELECT_ACCESSOR_SUBJECT_NOT_SET_TO_DISPLAY = "excludeDisplay";
    String SELECT_ACCESSOR_SUBJECT_SET_TO_DISPLAY = "includeDisplay";
    String SELECT_ACCESSOR_SUBJECT_ASSOCIATED_WITH_TITLES = "includeAssigned";
    String SELECT_ACCESSOR_ALL_SUBJECTS = "ALL";
    String SELECT_ACCESSOR_SUBJECT_NOT_ASSOCIATED_WITH_TITLES = "excludeAssigned";

    String SUBJECT_SEX_DEVELOPMENT_DISORDERS = "Disorders of Sex Development";

    //View Subject Page
    String VIEW_SUBJECT_PAGE_TITLE = "KBTools - View Subject";
    String VIEW_SUBJECT_LINK_NLM = "NLM";

    //Authority Title Search Page
    String SELECT_VALUE_TITLE_BEGINS = "TitleBegins";
    String SELECT_VALUE_TITLECODE = "TitleCode";
    String SELECT_VALUE_TITLE_ID = "TitleId";
    String SELECT_VALUE_IDENTIFIER = "Identifier";
    String SELECT_VALUE_MARCRECORDCODE = "MARCRecordCodeEquals";
    String SELECT_VALUE_SSID = "SSId";
    String SELECT_VALUE_TITLECONTAINS = "TitleContainsPhrase";
    String SELECT_VALUE_TITLECONTAINS_AND = "TitleContainsAnd";
    String SELECT_VALUE_TITLE_EQUALS = "TitleEquals";

    String SELECT_VALUE_MONOGRAPH = "monograph";
    String SELECT_VALUE_SERIAL = "serial";
    String SELECT_VALUE_UNKNOWN = "unknown";
    String SELECT_VALUE_NO_TYPE = "";

    String TITLE_TYPE_MONOGRAPH = "monograph";
    String TITLE_TYPE_SERIAL = "serial";
    String TITLE_TYPE_UNKNOWN = "unknown";

    String TITLEBEGINS_BIKEBOYS = "Bike Boys, Drag Queens";
    String TITLECODE_BIKEBOYS = "TC0000111439";
    String AUTH_TITLE_BIKEBOYS = "Bike Boys, Drag Queens, and Superstars: Avant-Garde, Mass Culture, and Gay Identities in the 1960s Underground Cinema";
    String ID_BIKEBOYS = "9780253329714";

    String TITLECODE_DIDDLE = "TC0001425854";
    String AUTH_TITLE_DIDDLE = "Hey Diddle Diddle";
    String ID_DIDDLE = "9781283974851";

    String TITLE_ID_ACNE = "513413";
    String TITLECODE_ACNE = "TC0000513413";
    String AUTH_TITLE_ACNE = "Acne";
    String ID_ACNE = "9781420502152";

    String TITLECODE_WEATHER = "TC0000547822";
    String AUTH_TITLE_WEATHER = "Weather, climate, and society";
    String ID_WEATHER = "1948-8327";

    String MARCRECORDCODE_DARKMATTER = "lccn2010256028";
    String TITLECODE_DARKMATTER = "TC0000392468";
    String AUTH_TITLE_DARKMATTER = "Darkmatter";
    String ID_DARKMATTER = "2041-3254";

    String SSID_LIGHT = "ssj0001365063";
    String TITLECODE_lIGHT = "TC0001365063";
    String AUTH_TITLE_LIGHT = "Light and Dark";
    String ID_LIGHT = "9781873631430";

    String TITLECODE_HEY = "TC0001117414";
    String AUTH_TITLE_HEY = "Hey there, stink bug";
    String ID_HEY = "9781580893046";

    String TITLECODE_TWIST = "TC0001359947";
    String AUTH_TITLE_TWIST = "Danny Danger and the Space Twister";
    String ID_TWIST = "9780857630292";

    String TITLECODE_RUNNING = "TC0000390449";
    String AUTH_TITLE_RUNNING = "Running times";
    String ID_RUNNING = "0147-2968";

    String TITLECODE_RUGBY = "TC0001448923";
    String AUTH_TITLE_RUGBY = "Rugby's strangest matches : extraordinary but true stories from over a century of rugby";
    String ID_RUGBY = "9781905798162";

    String TITLE_ID_AXEMAN = "1531778";
    String TITLECODE_AXEMAN = "TC0001531778";
    String AUTH_TITLE_AXEMAN = "The axeman";
    String ID_AXEMAN = "9781492609162";

    String SSID_JUICE = "60781";
    String TITLECODE_JUICE = "LIBJU";
    String AUTH_TITLE_JUICE = "Library juice";
    String ID_JUICE = "1544-9378";

    String REGEX_TITLECODE = "TC000\\d{7}|\\w{5,10}";
    String REGEX_HOLDINGTYPE = "monograph|serial";
    String REGEX_ID = "978\\d{10}|\\d{4}-\\d{3,4}X?";
    String REGEX_TITLE = ".+";

    //Database Search page
    String XPATH_TABLE_SEARCH_RESULTS = "//div[2]/table";
    String DS_DATABASE_CODE = "N48";
    String DS_DATABASE_NAME = "Gale MARC Test Database (DO NOT ADD)";
    String DS_PROVIDER_CODE = "PRVXSS";

    //Title Normalization - Holding View
    String TNHV_PAGE_TITLE = "Holding View - Title Normalization - KBTools";
    String XPATH_MARC_TABLE_TEXT_DISPLAY_50 = "//table[2]//tr[1]//td[2]//i";
    String XPATH_MARC_COUNT_TEXT = "//table[2]/tbody//tr[1]//td[3]";
    String CSS_PATH_AUTH_TITLE_TABLE_ELEMENTS = "form>blockquote>table>tbody>tr";
    int AUTH_TITLE_TABLE_COLUMNS_PER_ROW = 5;

    //TitleCreatePage
    String CSSPATH_TITLE_DETAILS = "form>h3";
    String CSSPATH_MARC_RECORD_INPUT_FIELD = "table>tbody>tr>td>input[name=\"mRecordCode0\"]";
    String SERIAL_MARC_RECORD = "lccn2006216171";
    String SERIAL_MARC_RECORD_INVALID_ERROR = "Invalid title type (serial)";
    String CSSPATH_MARC_RECORD_CODE_IN_TABLE = "table>tbody>tr>td>a[href='../MARC/viewMARC.jsp?MARCRecordCode=lccn2006216171']";
    String CSSPATH_TITLECREATE_TABLES = "table>tbody";
    String BUTTON_PREVIEW_CHANGES = "Preview Changes";
    String TABLE_TITLE_TITLE_DETAILS = "Title Details";
    String ROW_COLOR_RED = "#F7918B";

    //RuleSearchPage
    String LINK_PLUS = "+";
    String PAGETITLE_EDIT_RULE = "Edit Rule - Holdings Import - KBTools";    //Title in the browser tab;
    String TABLE_LABEL_SERIALS = "Serials";
    String TABLE_LABEL_MONOGRAPHS = "Monographs";
    String CSSPATH_TABLE_LABELS = ".tableLabel";
    String CSSPATH_RULES = "tr[bgcolor]";
    int INDEX_SERIALS_TABLE = 0;
    int INDEX_MONOGRAPHS_TABLE = 0;
    int INDEX_MONOGRAPHS_TABLE_AFTER_SERIALS = 1;
    int INDEX_RULE_TITLE = 3;
    int INDEX_RULE_TYPE = 1;
    int INDEX_RULE_ID = 0;


    String CSSPATH_RULE_HEADERS = "th";
    String CSSPATH_RULE_VALUES = "tr[bgcolor]>td";
    String CSSPATH_RULE_ID_VALUES = "tr[bgcolor]>td[rowspan]";

    //ViewRulesPage
    String CSSPATH_DB_HAS_X_RULES = "h3>i>a";

    //EditTitleDetailsPage
    String ETDP_BUTTON_SAVECHANGES = "Save Changes";

    //EditNormalizersPage
    String ENP_BUTTON_REMOVE = "Remove";

    //MarcImporterPage
    String PAGETITLE_MARCIMPORTER = "MARC Importer - KBTools";
    String MARCIMPORT_FILE = "ASP_AGB_Black_Short_Fiction_Updated.mrc";
    String CLEAN_MARCIMPORT_FILE = "ASP_AGB_Black_Short_Fiction.mrc";

    //MarcSearchpage
    enum marcSearchType {TITLE_EQUALS ("TitleEquals"), MARCRECORDCODE_EQUALS ("MARCRecordCodeEquals"),
        ISBN_EQUALS ("ISBNEquals"), ISSN_EQUALS ("ISSNEquals"), TITLE_BEGINS ("TitleBegins"), LCCN_EQUALS ("LCCNEquals"),
        TITLE_PHRASE ("TitlePhrase"),TITLE_AND_WORD ("TitleWordAnd");
        String marcSearchType;
        marcSearchType(String searchType){
            this.marcSearchType = searchType;
        }
        public String value(){
            return this.marcSearchType;
        }
    };
}
