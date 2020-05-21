package com.zbud.mobilneblachy.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.zbud.mobilneblachy.Contract.PullOutSteelSheetContract;
import com.zbud.mobilneblachy.Helper.CheckNetworkStatus;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.DialogSteelSheetLocationsAdapter;
import com.zbud.mobilneblachy.Model.SteelSheetModel;
import com.zbud.mobilneblachy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PullOutSteelSheetPresenter {
    private PullOutSteelSheetContract.View contractView;
    private Context context;
    private ProgressDialog progressDialog;
    private SteelSheetModel steelSheet;
    private List<SteelSheetModel> steelSheetLocationsList;
    private DialogSteelSheetLocationsAdapter dialogSteelSheetLocationsAdapter;
    private JSONObject jsonObject;
    private boolean isForProcessing;


    /* CONSTRUCTORS */

    public PullOutSteelSheetPresenter(PullOutSteelSheetContract.View contractView, Context context) {
        this.context = context;
        this.steelSheet = new SteelSheetModel();
        this.contractView = contractView;
        this.isForProcessing = false;
    }

    /* SELECT METHODS */
    public void getSteelSheetLocations(String sheetBarcode) {
        if(CheckNetworkStatus.isNetworkAvailable(context)) {
            new PullOutSteelSheetPresenter.getSteelSheetLocationsAsyncTask().execute(sheetBarcode);
        } else {
            Toast.makeText(context, "Unable to connect to internet", Toast.LENGTH_LONG).show();
        }
    }

    /* UPDATE METHODS */
    public void pullOutSteelSheetsFromWarehouses(String sheetBarcode, String steelSheetsQty, String subsectorId, boolean isForProcessing) {
        if(CheckNetworkStatus.isNetworkAvailable(context)) {
            this.isForProcessing = isForProcessing;
            new PullOutSteelSheetPresenter.pullOutSteelSheetsFromWarehousesAsyncTask().execute(sheetBarcode, steelSheetsQty, subsectorId);
        } else {
            Toast.makeText(context, "Unable to connect to internet", Toast.LENGTH_LONG).show();
        }
    }


    /* ASYNC TASKS */
    private class getSteelSheetLocationsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Proszę czekać, trwa wyświetlanie lokalizacji...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonObject = steelSheet.getSteelSheetLocations(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                if(jsonObject.optInt(Constants.KEY_SUCCESS) == 1) {
                    JSONArray jsonResult = jsonObject.getJSONArray(Constants.KEY_DATA);
                    steelSheetLocationsList = new ArrayList<>();
                    JSONObject singleLocation;
                    for (int i=0; i<jsonResult.length(); i++) {
                        singleLocation = jsonResult.getJSONObject(i);
                        if(singleLocation.optInt(Constants.KEY_IS_STORED) == 1) {
                            steelSheetLocationsList.add(new SteelSheetModel(
                                    singleLocation.optInt(Constants.KEY_SUBSECTOR_ID),
                                    singleLocation.optString(Constants.KEY_SUBSECTOR_NAME),
                                    singleLocation.optString(Constants.KEY_SECTOR_NAME),
                                    singleLocation.optString(Constants.KEY_WAREHOUSE_NAME),
                                    singleLocation.optInt(Constants.KEY_QTY),
                                    singleLocation.optInt(Constants.KEY_IS_STORED)
                            ));
                        }
                    }

                    dialogSteelSheetLocationsAdapter = new DialogSteelSheetLocationsAdapter(context,
                            R.layout.list_item_steel_sheet_location, steelSheetLocationsList);

                    contractView.populateOneSteelSheetLocationsList(dialogSteelSheetLocationsAdapter);

                } else {
                    switch(jsonObject.optInt(Constants.KEY_ERROR_CODE)) {
                        case 1 : {
                            Toast.makeText(context, "Nie pobrano lokalizacji dla danej blachy. " +
                                    "Brak wymaganego parametru.", Toast.LENGTH_LONG).show();
                        } break;
                        case 2 : {
                            Toast.makeText(context, "Nie pobrano lokalizacji dla danej blachy. " +
                                    "Błąd zapytania SQL.", Toast.LENGTH_LONG).show();
                        } break;
                        default : {
                            Toast.makeText(context, "Nie pobrano lokalizacji dla danej blachy. " +
                                    "Nierozpoznany błąd.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

    private class pullOutSteelSheetsFromWarehousesAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Proszę czekać...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonObject = steelSheet.pullOutSteelSheetsFromWarehouses(params[0], params[1], params[2], isForProcessing);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("=========", "=====================================================");
            Log.i("Success = ", Integer.toString(jsonObject.optInt(Constants.KEY_SUCCESS)));
            Log.i("ErrCode = ", Integer.toString(jsonObject.optInt(Constants.KEY_ERROR_CODE)));
            Log.i("=========", "=====================================================");
            if(jsonObject.optInt(Constants.KEY_SUCCESS) == 1) {
                Toast.makeText(context, "Pobrano arkusze z magazynu mobilnego.", Toast.LENGTH_LONG).show();
                contractView.clearEdtBarcodeScanField();
            } else {
                switch(jsonObject.optInt(Constants.KEY_ERROR_CODE)) {
                    case 1 : {
                        Toast.makeText(context, "Nie pobrano arkuszy z magazynu mobilnego. " +
                                "Błąd zapytania SQL.", Toast.LENGTH_LONG).show();
                    } break;
                    case 2 : {
                        Toast.makeText(context, "Nie pobrano arkuszy z magazynu mobilnego. " +
                                "Brak rekordu o podanym kodzie w bazie.", Toast.LENGTH_LONG).show();
                    } break;
                    case 3 : {
                        Toast.makeText(context, "Nie pobrano arkuszy z magazynu mobilnego. " +
                                "Zbyt duża ilość.", Toast.LENGTH_LONG).show();
                    } break;
                    case 4 : {
                        Toast.makeText(context, "Nie pobrano arkuszy z magazynu mobilnego. " +
                                "Błąd zapytania SQL (Przywracanie zmian).", Toast.LENGTH_LONG).show();
                    } break;
                    case 5 : {
                        Toast.makeText(context, "Nie pobrano arkuszy z magazynu mobilnego. " +
                                "Brak wymaganych parametrów.", Toast.LENGTH_LONG).show();
                    } break;
                    default : {
                        Toast.makeText(context, "Nie pobrano arkuszy z magazynu mobilnego. " +
                                "Nierozpoznany błąd.", Toast.LENGTH_LONG).show();
                    }
                }
            }
            progressDialog.dismiss();
        }
    }

}
