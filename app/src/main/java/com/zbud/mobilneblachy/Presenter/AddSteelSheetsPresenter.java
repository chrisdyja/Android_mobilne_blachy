package com.zbud.mobilneblachy.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zbud.mobilneblachy.Contract.AddSteelSheetsContract;
import com.zbud.mobilneblachy.Helper.CheckNetworkStatus;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Model.SteelSheetModel;
import com.zbud.mobilneblachy.Model.StoreSectorModel;
import com.zbud.mobilneblachy.Model.StoreSubsectorModel;
import com.zbud.mobilneblachy.Model.WarehouseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddSteelSheetsPresenter implements AddSteelSheetsContract.Presenter {

    private AddSteelSheetsContract.View contractView;
    private Context context;
    private SteelSheetModel steelSheet;
    private WarehouseModel warehouse;
    private StoreSectorModel storeSector;
    private StoreSubsectorModel storeSubsector;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private List<StoreSubsectorModel> subsectorsList;
    private List<StoreSectorModel> sectorsList;


    public AddSteelSheetsPresenter(AddSteelSheetsContract.View contractView, Context context) {
        this.contractView = contractView;
        this.context = context;
        this.steelSheet = new SteelSheetModel();
        this.warehouse = new WarehouseModel();
        this.storeSubsector = new StoreSubsectorModel();
        this.storeSector = new StoreSectorModel();
    }


    public void addSteelSheetsToDatabase(String steelSheetBarcode, String subsectorName, String sectorName,
                                         String warehouseName, int qty, boolean returnFromProcessing) {
        int subsectorId = -1;

        warehouseName = warehouseName.replaceAll(Constants.STRING_WAREHOUSE,Constants.STRING_EMPTY);
        sectorName = sectorName.replaceAll(Constants.STRING_SECTOR,Constants.STRING_EMPTY);
        subsectorName = subsectorName.replaceAll(Constants.STRING_SUBSECTOR,Constants.STRING_EMPTY);

        /* Przeszukiwanie listy podsektorów po nazwie podsektora ze spinnera
        *  w celu znalezienia id podsektora odpowiadającemu tej nazwie */
        for(int i=0; i<subsectorsList.size(); i++) {
            if(subsectorsList.get(i).getSubsectorName().equals(subsectorName)) {
                subsectorId = subsectorsList.get(i).getId();
            }
        }
        if(subsectorId >= 1){
            steelSheet = null;
            steelSheet = new SteelSheetModel(steelSheetBarcode, subsectorId,
                    subsectorName, sectorName, warehouseName, qty);
            if(CheckNetworkStatus.isNetworkAvailable(context)) {
                new AddSteelSheetsPresenter.addSteelSheetsToDatabaseAsyncTask().execute(returnFromProcessing);
            } else {
                Toast.makeText(context, "Unable to connect to internet", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(context, "Nie ma takiego sektora.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean populateSpnWarehouseList(boolean readEmptyList) {
        if(readEmptyList) {
            ArrayList<String> warehouseName = new ArrayList<String> ();
            List<WarehouseModel> warehouseList;
            warehouseList = new ArrayList<>();
            warehouseList.add(new WarehouseModel(0,Constants.STRING_SELECT_WAREHOUSE));     // indeks nie brany pod uwage, falszywy model
            warehouseName.add(warehouseList.get(0).getWarehouseName());
            contractView.populateSpnWarehouseList(setArrayAdapterForSpinners(warehouseName));
        } else if(CheckNetworkStatus.isNetworkAvailable(context)) {
            new AddSteelSheetsPresenter.getWarehousesAsyncTask().execute();
            return true;
        } else {
            ArrayList<String> warehouseName = new ArrayList<String> ();
            List<WarehouseModel> warehouseList;
            warehouseList = new ArrayList<>();
            warehouseList.add(new WarehouseModel(0,Constants.STRING_SELECT_WAREHOUSE));     // indeks nie brany pod uwage, falszywy model
            warehouseName.add(warehouseList.get(0).getWarehouseName());
            contractView.populateSpnWarehouseList(setArrayAdapterForSpinners(warehouseName));
            Toast.makeText(context, "Unable to connect to internet", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void populateSpnSectorsList(String warehouseName, boolean readEmptyList) {
        if(readEmptyList) {
            ArrayList<String> sectorName = new ArrayList<String> ();
            List<StoreSectorModel> sectorList;
            sectorList = new ArrayList<>();
            sectorList.add(new StoreSectorModel(0,Constants.STRING_SELECT_SECTOR,0));     // indeks nie brany pod uwage, falszywy model
            sectorName.add(sectorList.get(0).getSectorName());
            contractView.populateSpnSectorsList(setArrayAdapterForSpinners(sectorName));
        }else if(CheckNetworkStatus.isNetworkAvailable(context)) {
            new AddSteelSheetsPresenter.getSectorsAsyncTask().execute(warehouseName.replaceFirst(Constants.STRING_WAREHOUSE, Constants.STRING_EMPTY));
        } else {

            Toast.makeText(context, "Unable to connect to internet", Toast.LENGTH_LONG).show();
        }
    }

    public void populateSpnSubsectorsList(String sectorName, String warehouseName, boolean readEmptyList) {
        if(readEmptyList) {
            ArrayList<String> subsectorName = new ArrayList<String> ();
            List<StoreSubsectorModel> subsectorList;
            subsectorList = new ArrayList<>();
            subsectorList.add(new StoreSubsectorModel(0,Constants.STRING_SELECT_SUBSECTOR,0));     // indeks nie brany pod uwage, falszywy model
            subsectorName.add(subsectorList.get(0).getSubsectorName());
            contractView.populateSpnSubsectorsList(setArrayAdapterForSpinners(subsectorName));
        } else if(CheckNetworkStatus.isNetworkAvailable(context)) {
            new AddSteelSheetsPresenter.getSubsectorsAsyncTask().execute(sectorName.replaceFirst(Constants.STRING_SECTOR, Constants.STRING_EMPTY),
                    warehouseName.replaceFirst(Constants.STRING_WAREHOUSE, Constants.STRING_EMPTY));
        } else {
            Toast.makeText(context, "Unable to connect to internet", Toast.LENGTH_LONG).show();
        }
    }

    /* PRIVATE METHODS SECTION */
    private ArrayAdapter setArrayAdapterForSpinners(ArrayList<String> arrayList) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, arrayList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    /* ASYNC TASKS */
    private class addSteelSheetsToDatabaseAsyncTask extends AsyncTask<Boolean, String, String> {
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Przypisywanie blach do sektorów. Proszę czekać...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Boolean... params) {
            jsonObject = steelSheet.addSteelSheet(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (jsonObject.optInt(Constants.KEY_SUCCESS) == 1) {
                Toast.makeText(context, "Blacha została dodana do magazynu.", Toast.LENGTH_LONG).show();
                contractView.clearEdtBarcodeScanField();
            }else {
                switch(jsonObject.optInt(Constants.KEY_ERROR_CODE)) {
                    case 1: {
                        Toast.makeText(context, "Blacha nie została dodana do magazynu. " +
                                "Błąd zapytania SQL.", Toast.LENGTH_LONG).show();
                    } break;
                    case 2: {
                        Toast.makeText(context, "Blacha nie została dodana do magazynu. " +
                                "Brak wymaganego rekordu w bazie lub zbyt duża ilość arkuszy.",
                                Toast.LENGTH_LONG).show();
                    } break;
                    case 3: {
                        Toast.makeText(context, "Blacha nie została dodana do magazynu. " +
                                "Błąd zapytania SQL (DELETE).", Toast.LENGTH_LONG).show();
                    } break;
                    case 4: {
                        Toast.makeText(context, "Blacha nie została dodana do magazynu. " +
                                "Niepoprawny wynik zapytania UPDATE.", Toast.LENGTH_LONG).show();
                    } break;
                    case 5: {
                        Toast.makeText(context, "Blacha nie została dodana do magazynu. " +
                                "Brak wymaganych parametrów w tablicy $_POST.", Toast.LENGTH_LONG).show();
                    } break;
                    default : {
                        Toast.makeText(context, "Blacha nie została dodana do magazynu. " +
                                "Nie rozpoznany błąd.", Toast.LENGTH_LONG).show();
                    }
                }
            }
            progressDialog.dismiss();
        }
    }

    private class getSubsectorsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Wczytywanie podsektorów. Proszę czekać...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonObject = storeSubsector.getSubsectorsFromSector(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int success;
            ArrayList<String> subsectorName = new ArrayList<String>();
            try {
                success = jsonObject.getInt(Constants.KEY_SUCCESS);
                if (success == 1) {
                    JSONArray jsonResult = jsonObject.getJSONArray(Constants.KEY_DATA);
                    subsectorsList = new ArrayList<>();
                    subsectorsList.add(new StoreSubsectorModel(0,Constants.STRING_SELECT_SUBSECTOR, 0));     // indeks nie brany pod uwage, falszywy model
                    subsectorName.add(subsectorsList.get(0).getSubsectorName());
                    int sectorsQty = jsonResult.length();
                    if(sectorsQty >= 1) {
                        for (int i = 1; i <= sectorsQty; i++) {
                            JSONObject sectorObj = jsonResult.getJSONObject(i-1);
                            subsectorsList.add(new StoreSubsectorModel(
                                    sectorObj.optInt(Constants.KEY_ID),
                                    sectorObj.optString(Constants.KEY_SUBSECTOR_NAME),
                                    sectorObj.optInt(Constants.KEY_SECTOR_ID)
                            ));
                            subsectorName.add(Constants.STRING_SUBSECTOR + subsectorsList.get(i).getSubsectorName());

                        }
                        contractView.populateSpnSubsectorsList(setArrayAdapterForSpinners(subsectorName));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    private class getSectorsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Wczytywanie sektorów. Proszę czekać...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonObject = storeSector.getSectorsFromWarehouse(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int success;
            ArrayList<String> sectorName = new ArrayList<String>();
            try {
                success = jsonObject.getInt(Constants.KEY_SUCCESS);
                if (success == 1) {
                    JSONArray jsonResult = jsonObject.getJSONArray(Constants.KEY_DATA);
                    sectorsList = new ArrayList<>();
                    sectorsList.add(new StoreSectorModel(0,Constants.STRING_SELECT_SECTOR,0));     // indeks nie brany pod uwage, falszywy model
                    sectorName.add(sectorsList.get(0).getSectorName());
                    int sectorsQty = jsonResult.length();
                    if(sectorsQty >= 1) {
                        for (int i = 1; i <= sectorsQty; i++) {
                            JSONObject sectorObj = jsonResult.getJSONObject(i-1);
                            sectorsList.add(new StoreSectorModel(
                                    sectorObj.optInt(Constants.KEY_ID),
                                    sectorObj.optString(Constants.KEY_SECTOR_NAME),
                                    sectorObj.optInt(Constants.KEY_WAREHOUSE_ID)
                            ));
                            sectorName.add(Constants.STRING_SECTOR + sectorsList.get(i).getSectorName());

                        }
                        contractView.populateSpnSectorsList(setArrayAdapterForSpinners(sectorName));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    private class getWarehousesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Wczytywanie magazynów. Proszę czekać...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonObject = warehouse.getWarehouses();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int success;
            ArrayList<String> warehouseName = new ArrayList<String> ();
            List<WarehouseModel> warehouseList;
            try {
                success = jsonObject.getInt(Constants.KEY_SUCCESS);
                if(success == 1) {
                    JSONArray jsonResult = jsonObject.getJSONArray(Constants.KEY_DATA);
                    warehouseList = new ArrayList<>();
                    warehouseList.add(new WarehouseModel(0,Constants.STRING_SELECT_WAREHOUSE));     // indeks nie brany pod uwage, falszywy model
                    warehouseName.add(warehouseList.get(0).getWarehouseName());
                    int warehousesQty = jsonResult.length();
                    if(warehousesQty >= 1) {
                        for (int i = 1; i <= warehousesQty; i++) {
                            JSONObject singleWarehouse = jsonResult.getJSONObject(i-1);
                            warehouseList.add(new WarehouseModel(
                                    singleWarehouse.optInt(Constants.KEY_ID),
                                    singleWarehouse.optString(Constants.KEY_WAREHOUSE_NAME)
                            ));
                            warehouseName.add(Constants.STRING_WAREHOUSE + warehouseList.get(i).getWarehouseName());
                        }
                        contractView.populateSpnWarehouseList(setArrayAdapterForSpinners(warehouseName));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }
}
