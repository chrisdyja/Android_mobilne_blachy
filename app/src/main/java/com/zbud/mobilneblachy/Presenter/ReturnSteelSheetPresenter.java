package com.zbud.mobilneblachy.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.zbud.mobilneblachy.Contract.ReturnSteelSheetContract;
import com.zbud.mobilneblachy.Helper.CheckNetworkStatus;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.SteelSheetsInProcessListAdapter;
import com.zbud.mobilneblachy.Model.SteelSheetInProcessModel;
import com.zbud.mobilneblachy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReturnSteelSheetPresenter implements ReturnSteelSheetContract.Presenter {

    private ProgressDialog progressDialog;
    private Context context;
    private ReturnSteelSheetContract.View contractView;
    private JSONObject jsonObject;

    private SteelSheetInProcessModel steelSheetInProcess;
    private List<SteelSheetInProcessModel> steelSheetsInProcessList;
    private SteelSheetsInProcessListAdapter steelSheetsInProcessListAdapter;

    /* CONSTRUCTORS */
    public ReturnSteelSheetPresenter(ReturnSteelSheetContract.View contractView, Context context) {
        this.context = context;
        this.contractView = contractView;
        this.steelSheetInProcess = new SteelSheetInProcessModel();
    }

    /* GET METHODS */
    public void getSteelSheetsInProcess() {
        if(CheckNetworkStatus.isNetworkAvailable(context)) {
            new ReturnSteelSheetPresenter.getSteelSheetsInProcessAsyncTask().execute();
        } else {
            Toast.makeText(context,
                    "Unable to connect to internet",
                    Toast.LENGTH_LONG).show();
        }
    }


    public void searchForSteelSheets(String steelSheetName) {
        int success;
        try {
            success = jsonObject.optInt(Constants.KEY_SUCCESS);
            if(success == 1) {
                JSONArray jsonResult = jsonObject.getJSONArray(Constants.KEY_DATA);
                steelSheetsInProcessList = new ArrayList<>();
                int steelSheetsQty = jsonResult.length();
                if(steelSheetsQty >= 1) {
                    for (int i = 0; i < steelSheetsQty; i++) {
                        JSONObject singleSteelSheet = jsonResult.getJSONObject(i);
                        if(singleSteelSheet.optString(Constants.KEY_STEEL_SHEET_CODE)
                                .toLowerCase().contains(steelSheetName.toLowerCase())) {
                            if(singleSteelSheet.optInt(Constants.KEY_IS_STORED) == 1){

                            }
                            steelSheetsInProcessList.add(new SteelSheetInProcessModel(
                                    singleSteelSheet.optInt(Constants.KEY_ID),
                                    singleSteelSheet.optString(Constants.KEY_STEEL_SHEET_CODE),
                                    singleSteelSheet.optInt(Constants.KEY_QTY)
                            ));
                        }
                    }

                    steelSheetsInProcessListAdapter = new SteelSheetsInProcessListAdapter(context,
                            R.layout.list_item_steel_sheets_in_process, steelSheetsInProcessList);
                    contractView.populateSteelSheetsInProcessList(steelSheetsInProcessListAdapter);
                }else {

                    Toast.makeText(context, "Brak danych do wyświetlenia: " + jsonObject.getString(Constants.KEY_MESSAGE),
                            Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(context, "Błąd pobierania danych: " + jsonObject.getString(Constants.KEY_MESSAGE),
                        Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean setSteelSheetAsProcessed(int qty, int position) {
        String sheetBarcode;
        sheetBarcode = steelSheetsInProcessListAdapter.getItem(position).getSteelSheetBarcode();
        if(qty <= steelSheetsInProcessListAdapter.getItem(position).getQty()){
            // Jeśli wprowadzona ilość mniejsza lub równa od ilości w obróbce to wrongQty == false
            if(CheckNetworkStatus.isNetworkAvailable(context)) {
                new ReturnSteelSheetPresenter.setSteelSheetAsProcessedAsyncTask()
                        .execute(sheetBarcode, Integer.toString(qty));
            } else {
                Toast.makeText(context,
                        "Unable to connect to internet",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        }
        // Jeśli wprowadzona ilość większa od ilości w obróbce to wrongQty == true
        return true;
    }

    public boolean getSteelSheetInProcessInfo(int position, int qty) {
        String sheetBarcode;
        sheetBarcode = steelSheetsInProcessListAdapter.getItem(position).getSteelSheetBarcode();
        if(qty <= steelSheetsInProcessListAdapter.getItem(position).getQty()) {
        // Jeśli wprowadzona ilość mniejsza lub równa od ilości w obróbce to wrongQty == false
            contractView.sendDataToAddSteelSheetFragment(sheetBarcode, qty);
            return false;
        }
        // Jeśli wprowadzona ilość większa od ilości w obróbce to wrongQty == true
        return true;
    }

    /* ASYNC TASKS */
    private class setSteelSheetAsProcessedAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Wczytywanie blach. Proszę czekać...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonObject = steelSheetInProcess.setSteelSheetAsProcessed(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(jsonObject.optInt(Constants.KEY_SUCCESS) == 1) {
                Toast.makeText(context, "Usunięto blachę z bazy.", Toast.LENGTH_LONG).show();
            } else {
                switch(jsonObject.optInt(Constants.KEY_ERROR_CODE)) {
                    case 1 : {
                        Toast.makeText(context, "Nie usunięto blachy z bazy. " +
                                "Błąd zapytania SQL.", Toast.LENGTH_LONG).show();
                    } break;
                    case 2 : {
                        Toast.makeText(context, "Nie usunięto blachy z bazy." +
                                "Brak rekordu o podanym kodzie w bazie.", Toast.LENGTH_LONG).show();
                    } break;
                    case 3 : {
                        Toast.makeText(context, "Nie usunięto blachy z bazy." +
                                "Zbyt duża ilość.", Toast.LENGTH_LONG).show();
                    } break;
                    case 4 : {
                        Toast.makeText(context, "Nie usunięto blachy z bazy." +
                                "Błąd zapytania SQL (Przywracanie zmian).", Toast.LENGTH_LONG).show();
                    } break;
                    case 5 : {
                        Toast.makeText(context, "Nie usunięto blachy z bazy. " +
                                "Brak wymaganych parametrów.", Toast.LENGTH_LONG).show();
                    } break;
                    default : {
                        Toast.makeText(context, "Nie usunięto blachy z bazy. " +
                                "Nierozpoznany błąd.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            progressDialog.dismiss();
            getSteelSheetsInProcess();
        }
    }

    private class getSteelSheetsInProcessAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Wczytywanie blach. Proszę czekać...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonObject = steelSheetInProcess.getAllSteelSheetsInProcess();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);  int success;
            try {
                success = jsonObject.optInt(Constants.KEY_SUCCESS);
                if(success == 1) {
                    JSONArray jsonResult = jsonObject.getJSONArray(Constants.KEY_DATA);
                    steelSheetsInProcessList = new ArrayList<>();
                    int steelSheetsQty = jsonResult.length();
                    if(steelSheetsQty >= 1) {
                        for (int i = 0; i < steelSheetsQty; i++) {
                            JSONObject singleSteelSheetInProcess = jsonResult.getJSONObject(i);
                            steelSheetsInProcessList.add(new SteelSheetInProcessModel(
                                    singleSteelSheetInProcess.optInt(Constants.KEY_ID),
                                    singleSteelSheetInProcess.optString(Constants.KEY_STEEL_SHEET_CODE),
                                    singleSteelSheetInProcess.optInt(Constants.KEY_QTY)
                            ));
                        }

//                        steelSheetsInProcessListAdapter = new SteelSheetsInProcessListAdapter(
//                                context, R.layout.list_item_steel_sheets_in_process, steelSheetsInProcessList);
//                        contractView.populateSteelSheetsInProcessList(steelSheetsInProcessListAdapter);
                    } else {

                        Toast.makeText(context, "Brak danych do wyświetlenia: " + jsonObject.getString(Constants.KEY_MESSAGE),
                                Toast.LENGTH_LONG).show();
                    }

                    steelSheetsInProcessListAdapter = new SteelSheetsInProcessListAdapter(
                            context, R.layout.list_item_steel_sheets_in_process, steelSheetsInProcessList);
                    contractView.populateSteelSheetsInProcessList(steelSheetsInProcessListAdapter);
                } else {
                    Toast.makeText(context, "Błąd pobierania danych: " + jsonObject.getString(Constants.KEY_MESSAGE),
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }
}
