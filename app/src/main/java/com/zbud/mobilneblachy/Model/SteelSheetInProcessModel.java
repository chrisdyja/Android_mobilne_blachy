package com.zbud.mobilneblachy.Model;

import com.zbud.mobilneblachy.Helper.Config;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SteelSheetInProcessModel {

    /* MODEL FROM DB */
    private int id;
    private int qty;
    private String steelSheetBarcode;

    /* CONSTRUCTORS */
    public SteelSheetInProcessModel(){};

    public SteelSheetInProcessModel(int id, String steelSheetBarcode, int qty) {
        this.id = id;
        this.qty = qty;
        this.steelSheetBarcode = steelSheetBarcode;
    }

    /* GETTERS */
    public int getId() { return id; }
    public int getQty() { return qty; }
    public String getSteelSheetBarcode() { return steelSheetBarcode; }

    /* SETTERS */
    public void setId(int id) { this.id = id; }
    public void setQty(int qty) { this.qty = qty; }
    public void setSteelSheetBarcode(String steelSheetBarcode) { this.steelSheetBarcode = steelSheetBarcode; }

    /* INSERT METHODS */
    public JSONObject addSteelSheetInProcess() {
        HttpJsonParser httpJsonParser = new HttpJsonParser();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonSteelSheetInProcess = new JSONObject();

        HashMap<String, String> httpParams = new HashMap<>();
        try {
            jsonSteelSheetInProcess = new JSONObject();
            jsonSteelSheetInProcess.put(Constants.KEY_STEEL_SHEET_CODE, this.steelSheetBarcode);
            jsonSteelSheetInProcess.put(Constants.KEY_QTY, this.qty);
            jsonArray.put(jsonSteelSheetInProcess);
            jsonSteelSheetInProcess.put(Constants.KEY_JSON_STEEL_SHEET, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpParams.put(Constants.KEY_JSON_STEEL_SHEET, jsonSteelSheetInProcess.toString());

        JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                Config.BASE_URL + Config.ADD_STEEL_SHEET_IN_PROCESS,
                Constants.REQUEST_METHOD_POST, httpParams);

        return jsonObject;
    }

    /* SELECT METHODS */
    public JSONObject getAllSteelSheetsInProcess() {
        JSONObject jsonObject = new JSONObject();
        HttpJsonParser httpJsonParser = new HttpJsonParser();

        try {
            jsonObject = httpJsonParser.makeHttpRequest(
                    Config.BASE_URL + Config.GET_ALL_STEEL_SHEETS_IN_PROCESS,
                    Constants.REQUEST_METHOD_POST, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /* DELETE METHODS */
    public JSONObject setSteelSheetAsProcessed(String sheetBarcode, String qty) {
        JSONObject jsonObject = new JSONObject();
        HttpJsonParser httpJsonParser = new HttpJsonParser();

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put(Constants.KEY_STEEL_SHEET_CODE, sheetBarcode);
        httpParams.put(Constants.KEY_QTY, qty);

        try {
            jsonObject = httpJsonParser.makeHttpRequest(
                    Config.BASE_URL + Config.SET_STEEL_SHEET_AS_PROCESSED,
                    Constants.REQUEST_METHOD_POST, httpParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
