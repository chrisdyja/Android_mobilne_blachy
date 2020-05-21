package com.zbud.mobilneblachy.Model;

import com.zbud.mobilneblachy.Helper.Config;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.HttpJsonParser;

import org.json.JSONObject;

import java.util.HashMap;

public class StoreSubsectorModel {

    /* MODEL FROM DB */
    private int id;
    private String subsectorName;
    private int sectorId;

    /* CONSTRUCTORS */
    public StoreSubsectorModel() {};

    public StoreSubsectorModel(int id, String subsectorName, int sectorId) {
        this.id = id;
        this.subsectorName = subsectorName;
        this.sectorId = sectorId;
    }

    /* GETTERS */
    public int getId() { return id; }
    public String getSubsectorName() { return subsectorName; }
    public int getSectorId() { return sectorId; }

    /* SETTERS */
    public void setId(int id) { this.id = id; }
    public void setSubsectorName(String subsectorName) { this.subsectorName = subsectorName; }
    public void setSectorId(int sectorId) { this.sectorId = sectorId; }

    /* ADD METHODS */

    /* GET METHODS */
    public JSONObject getSubsectorsFromSector(String sectorName, String warehouseName) {
        JSONObject jsonObject = new JSONObject();
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put(Constants.KEY_SECTOR_NAME, sectorName);
        httpParams.put(Constants.KEY_WAREHOUSE_NAME, warehouseName);

        try {
            jsonObject = httpJsonParser.makeHttpRequest(
                    Config.BASE_URL + Config.GET_SUBSECTORS_FROM_SECTOR,
                    "POST", httpParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
