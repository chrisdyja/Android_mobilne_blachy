package com.zbud.mobilneblachy.Helper;

/* Config file */
public class Config {

    /* URLS */
    public static final String BASE_URL = "http://192.168.0.202/slb_api/";            // SERVER TESTOWY/slb_api
//    public static final String BASE_URL = "http://192.168.0.204/slb_api/";            // SERVER TESTOWY/slb_api
//    public static final String BASE_URL = "http://192.168.0.174/sigma/";              // JANWAL
//    private static final String BASE_URL = "http://192.168.0.156/php_sigma/";         // DYJCHR


    public static final String ADD_STEEL_SHEET_IN_PROCESS = "add_steel_sheet_in_process.php";
    public static final String ADD_SINGLE_STEEL_SHEET = "add_single_steel_sheet.php";
    public static final String GET_ALL_STEEL_SHEETS = "get_all_steel_sheets.php";
    public static final String GET_ALL_STEEL_SHEETS_IN_PROCESS = "get_all_steel_sheets_in_process.php";
    public static final String GET_STEEL_SHEET_LOCATIONS = "get_steel_sheet_locations.php";
    public static final String GET_SECTORS_FROM_WAREHOUSE = "get_sectors_of_warehouse.php";
    public static final String GET_SUBSECTORS_FROM_SECTOR = "get_subsectors_of_sector.php";
    public static final String GET_WAREHOUSES = "get_warehouses.php";
    public static final String PULL_OUT_STEEL_SHEETS = "pull_out_steel_sheets.php";
    public static final String ADD_STEEL_SHEET_TO_IN_PROCESS_TABLE = "add_steel_sheet_to_in_process_table.php";
    public static final String SET_STEEL_SHEET_AS_PROCESSED = "set_steel_sheet_as_processed.php";


    /* NETWORK */
    /* Array of names of safe networks */
    public static final String NETWORK_NAMES[] = {"APZBUD"};
}
