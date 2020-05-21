package com.zbud.mobilneblachy.Model;


import android.util.Log;

import com.zbud.mobilneblachy.Helper.Config;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static java.lang.Math.round;

public class SteelSheetModel {

    /* MODEL FROM DB */
    private int id;                     // (PK) ONLY SET WHEN STEEL_SHEET IS GETTING FROM DB
    private int subsectorId;            // (FK) FK to table store_sectors
    private int isStored;               // Set to 1 if steel sheet is stored
    private int qty;                    // SNDBase.stock
    private double thickness;           // SNDBase.stock
    private double length;              // SNDBase.stock
    private double width;               // SNDBase.stock
    private double area;                // SNDBase.stock
    private String material;            // SNDBase.stock
    private String steelSheetBarcode;
    private String subsectorName;       // Does not exist in db
    private String sectorName;          // Does not exist in db
    private String warehouseName;       // Does not exist in db



    /* CONSTRUCTORS */
    public SteelSheetModel() { }

    public SteelSheetModel(int subsectorId, String subsectorName, String sectorName,
                           String warehouseName, int qty, int isStored) {
        this.subsectorId = subsectorId;
        this.subsectorName = subsectorName;
        this.sectorName = sectorName;
        this.warehouseName = warehouseName;
        this.qty = qty;
        this.isStored = isStored;
    }

    public SteelSheetModel(String steelSheetBarcode, int subsectorId, String subsectorName, String sectorName,
                           String warehouseName, int qty) {
        this.steelSheetBarcode = steelSheetBarcode;
        this.subsectorId = subsectorId;
        this.subsectorName = subsectorName;
        this.sectorName = sectorName;
        this.warehouseName = warehouseName;
        this.qty = qty;
    }

    public SteelSheetModel(int id, String steelSheetBarcode, int subsectorId, String subsectorName,
                           String sectorName, String warehouseName, int isStored, int qty, double thickness,
                           double length, double width, double area, String material) {
        this.id = id;
        this.steelSheetBarcode = steelSheetBarcode;   // BARCODE
        this.subsectorId = subsectorId;
        this.subsectorName = subsectorName;
        this.sectorName = sectorName;
        this.warehouseName = warehouseName;
        this.isStored = isStored;
        this.qty = qty;
        this.thickness = thickness;
        this.length = length;
        this.width = width;
        this.area = area;
        this.material = material;
    }

    /* GETTERS */
    public int getId() { return id; }
    public String getSteelSheetBarcode() { return steelSheetBarcode; }
    public int getSubsectorId() { return subsectorId; }
    public String getSubsectorName() { return subsectorName; }
    public String getSectorName() { return sectorName; }
    public String getWarehouseName() { return warehouseName; }
    public int getQty() { return qty; }
    public double getThickness() { return thickness; }
    public int getLength() { return (int) round(length); }
    public int getWidth() { return (int) round(width); }
    public int getArea() { return (int) round(area); }
    public String getMaterial() { return material; }
    public int getIsStored() { return isStored; }

    /* SETTERS */
    public void setId(int id) { this.id = id; }
    public void setSteelSheetBarcode(String steelSheetBarcode) { this.steelSheetBarcode = steelSheetBarcode; }
    public void setSubsectorId(int subsectorId) { this.subsectorId = subsectorId; }
    public void setSectorName(String sectorName) { this.sectorName = sectorName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public void setQty(int qty) { this.qty = qty; }
    public void setThickness(double thickness) { this.thickness = thickness; }
    public void setLength(double length) { this.length = length; }
    public void setWidth(double width) { this.width = width; }
    public void setArea(double area) { this.area = area; }
    public void setMaterial(String material) { this.material = material; }

    /* INSERT METHODS */
    public JSONObject addSteelSheet(Boolean returnFromProcessing) {
        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonSteelSheet;
        JSONObject jsonSteelSheets = new JSONObject();

        HashMap<String, String> httpParams = new HashMap<>();
        try {
            jsonSteelSheet = new JSONObject();
            jsonSteelSheet.put(Constants.KEY_STEEL_SHEET_CODE, this.steelSheetBarcode);
            jsonSteelSheet.put(Constants.KEY_SUBSECTOR_ID, this.subsectorId);
            jsonSteelSheet.put(Constants.KEY_QTY, this.qty);
            jsonSteelSheet.put(Constants.KEY_RETURN_FROM_PROCESSING, returnFromProcessing);
            jsonArray.put(jsonSteelSheet);
            jsonSteelSheets.put(Constants.KEY_JSON_STEEL_SHEET, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpParams.put(Constants.KEY_JSON_STEEL_SHEET, jsonSteelSheets.toString());

        JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                Config.BASE_URL + Config.ADD_SINGLE_STEEL_SHEET,
                Constants.REQUEST_METHOD_POST, httpParams);

        return jsonObject;
    }

    /* SELECT METHODS */
    public JSONObject getAllSteelSheets() {
        JSONObject jsonObject = new JSONObject();
        HttpJsonParser httpJsonParser = new HttpJsonParser();

        try {
            jsonObject = httpJsonParser.makeHttpRequest(
                    Config.BASE_URL + Config.GET_ALL_STEEL_SHEETS,
                    Constants.REQUEST_METHOD_POST, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
    public JSONObject getSteelSheetLocations(String sheetBarcode) {
        JSONObject jsonObject = new JSONObject();
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put(Constants.KEY_STEEL_SHEET_CODE, sheetBarcode);
        try {
            jsonObject = httpJsonParser.makeHttpRequest(
                    Config.BASE_URL + Config.GET_STEEL_SHEET_LOCATIONS,
                    Constants.REQUEST_METHOD_POST, httpParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /* PULL METHODS */
    public JSONObject pullOutSteelSheetsFromWarehouses(String sheetBarcode, String steelSheetQty, String subsectorId, boolean isForProcessing) {
        JSONObject jsonObject = new JSONObject();
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put(Constants.KEY_STEEL_SHEET_CODE, sheetBarcode);
        httpParams.put(Constants.KEY_QTY, steelSheetQty);
        httpParams.put(Constants.KEY_SUBSECTOR_ID, subsectorId);
        try {

            jsonObject = httpJsonParser.makeHttpRequest(
                    Config.BASE_URL + Config.PULL_OUT_STEEL_SHEETS,
                    Constants.REQUEST_METHOD_POST, httpParams);

            Log.i("===========","==================================================");
            Log.i("isForProcessing  ",Boolean.toString(isForProcessing));
            Log.i("jsonObject ",jsonObject.toString());
            Log.i("==========","===================================================");
            if(isForProcessing && jsonObject.optInt(Constants.KEY_SUCCESS) == 1) {
                jsonObject = httpJsonParser.makeHttpRequest(
                        Config.BASE_URL + Config.ADD_STEEL_SHEET_TO_IN_PROCESS_TABLE,
                        Constants.REQUEST_METHOD_POST, httpParams);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
