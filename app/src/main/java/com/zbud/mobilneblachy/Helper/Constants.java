package com.zbud.mobilneblachy.Helper;

public class Constants {

    /* REQUEST METHODS */
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_GET = "GET";

    /* JSON REQUEST KEYS */
    public static final String KEY_JSON_STEEL_SHEETS = "json_steel_sheets";
    public static final String KEY_JSON_STEEL_SHEET = "json_steel_sheet";

    /* JSON RESPONSE KEYS */
    public static final String KEY_DATA = "responseData";

    /* JSON DEBUG KEYS */
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_ERROR_CODE = "errCode";

    /* TABLES, COLUMNS and KEYS */
    // common
    public static final String KEY_ID = "id";
    // warehouses
    public static final String KEY_WAREHOUSE_NAME = "warehouseName";
    // store_sectors
    public static final String KEY_SECTOR_NAME = "sectorName";
    public static final String KEY_WAREHOUSE_ID = "warehouseId";
    // store_subsectors
    public static final String KEY_SUBSECTOR_NAME = "subsectorName";
    public static final String KEY_SECTOR_ID = "sectorId";
    // steel_sheets && steel_sheets_in_process
    public static final String KEY_STEEL_SHEET_CODE = "sheetBarcode";
    public static final String KEY_SUBSECTOR_ID = "subsectorId";
    public static final String KEY_IS_STORED = "isStored";
    public static final String KEY_QTY = "qty";
    public static final String KEY_IS_FOR_PROCESSING = "isForProcessing";
    public static final String KEY_RETURN_FROM_PROCESSING = "returnFromProcessing";
    // steel_sheets_SNDBase.stock Attributes
    public static final String KEY_STEEL_SHEET_QTY = "Qty";
    public static final String KEY_STEEL_SHEET_THICKNESS = "Thickness";
    public static final String KEY_STEEL_SHEET_LENGTH = "Length";
    public static final String KEY_STEEL_SHEET_WIDTH = "Width";
    public static final String KEY_STEEL_SHEET_AREA = "Area";
    public static final String KEY_STEEL_SHEET_MATERIAL = "Material";
    public static final String KEY_STEEL_SHEET_LOCATION = "Location";

    /* OTHERS */
    public static final String STRING_EMPTY = "";

    /* GUI STRINGS */
    public static final String STRING_OUTSIDE_WAREHOUSE = "BRAK W MAGAZYNIE";
    public static final String STRING_SELECT_WAREHOUSE = "Wybierz magazyn";
    public static final String STRING_SELECT_SECTOR = "Wybierz sektor";
    public static final String STRING_SELECT_SUBSECTOR = "Wybierz podsektor";
    public static final String STRING_WAREHOUSE = "Magazyn ";
    public static final String STRING_SECTOR = "Sektor ";
    public static final String STRING_SUBSECTOR = "Podsektor ";


}
