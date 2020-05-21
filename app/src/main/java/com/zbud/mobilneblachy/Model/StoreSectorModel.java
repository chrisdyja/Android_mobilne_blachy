package com.zbud.mobilneblachy.Model;

import com.zbud.mobilneblachy.Helper.Config;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.HttpJsonParser;

import org.json.JSONObject;

import java.util.HashMap;

public class StoreSectorModel {

    /* MODEL FROM DB */
    private int id;
    private String sectorName;
    private int warehouseId;

    /* CONSTRUCTORS */
    public StoreSectorModel() {};

    public StoreSectorModel(int id, String sectorName, int warehouseId) {
        this.id = id;
        this.sectorName = sectorName;
        this.warehouseId = warehouseId;
    }

    /* GETTERS */
    public int getId() { return id; }
    public String getSectorName() { return sectorName; }
    public int getWarehouseId() { return warehouseId; }

    /* SETTERS */
    public void setId(int id) { this.id = id; }
    public void setSectorName(String sectorName) { this.sectorName = sectorName; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }

    /* ADD METHODS */


    /* GET METHODS */
    public JSONObject getSectorsFromWarehouse(String warehouseName) {
        JSONObject jsonObject = new JSONObject();
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put(Constants.KEY_WAREHOUSE_NAME, warehouseName);

        try {
            jsonObject = httpJsonParser.makeHttpRequest(
                    Config.BASE_URL + Config.GET_SECTORS_FROM_WAREHOUSE,
                    "POST", httpParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
