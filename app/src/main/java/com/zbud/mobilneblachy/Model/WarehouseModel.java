package com.zbud.mobilneblachy.Model;

import com.zbud.mobilneblachy.Helper.Config;
import com.zbud.mobilneblachy.Helper.HttpJsonParser;

import org.json.JSONObject;

import java.util.HashMap;

public class WarehouseModel {

    /* MODEL FROM DB */
    private int id;
    private String warehouseName;

    /* CONSTRUCTORS */
    public WarehouseModel() {}

    public WarehouseModel(int id, String warehouseName) {
        this.id = id;
        this.warehouseName = warehouseName;
    }

    /* GETTERS */
    public int getId() { return id; }
    public String getWarehouseName() { return warehouseName; }

    /* SETTERS */
    public void setId(int id) { this.id = id; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    /* ADD METHODS */


    /* GET METHODS */
    public JSONObject getWarehouses() {
        JSONObject jsonObject = new JSONObject();
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        HashMap<String, String> httpParams = new HashMap<>();
        try {
            jsonObject = httpJsonParser.makeHttpRequest(
                    Config.BASE_URL + Config.GET_WAREHOUSES,
                    "POST", httpParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
