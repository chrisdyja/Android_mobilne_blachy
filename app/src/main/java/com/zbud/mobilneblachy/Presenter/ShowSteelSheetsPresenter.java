package com.zbud.mobilneblachy.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.zbud.mobilneblachy.Contract.ShowSteelSheetsContract;
import com.zbud.mobilneblachy.Helper.CheckNetworkStatus;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.SteelSheetsListAdapter;
import com.zbud.mobilneblachy.Model.SteelSheetModel;
import com.zbud.mobilneblachy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowSteelSheetsPresenter implements ShowSteelSheetsContract {

    private ShowSteelSheetsContract.View contractView;
    private SteelSheetModel steelSheet;
    private JSONObject jsonObject;
    private ProgressDialog progressDialog;
    private List<SteelSheetModel> steelSheetsList;
    private Context context;
    private SteelSheetsListAdapter steelSheetsListAdapter;


    /* CONSTRUCTORS */
    public ShowSteelSheetsPresenter(ShowSteelSheetsContract.View contractView, Context context) {
        this.contractView = contractView;
        this.steelSheet = new SteelSheetModel();
        this.context = context;
    }

    /* GET METHODS */
    public void getSteelSheets() {
        if(CheckNetworkStatus.isNetworkAvailable(context)) {
            new ShowSteelSheetsPresenter.getSteelSheetsAsyncTask().execute();
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
                steelSheetsList = new ArrayList<>();
                int steelSheetsQty = jsonResult.length();
                if(steelSheetsQty >= 1) {
                    for (int i = 0; i < steelSheetsQty; i++) {
                        JSONObject singleSteelSheet = jsonResult.getJSONObject(i);
                        if(singleSteelSheet.optString(Constants.KEY_STEEL_SHEET_CODE)
                                .toLowerCase().contains(steelSheetName.toLowerCase())) {
                            if(singleSteelSheet.optInt(Constants.KEY_IS_STORED) == 1){

                            }
                            steelSheetsList.add(new SteelSheetModel(
                                    singleSteelSheet.optInt(Constants.KEY_ID),
                                    singleSteelSheet.optString(Constants.KEY_STEEL_SHEET_CODE),
                                    singleSteelSheet.optInt(Constants.KEY_SECTOR_ID),
                                    singleSteelSheet.optString(Constants.KEY_SUBSECTOR_NAME),
                                    singleSteelSheet.optString(Constants.KEY_SECTOR_NAME),
                                    singleSteelSheet.optString(Constants.KEY_WAREHOUSE_NAME),
                                    singleSteelSheet.optInt(Constants.KEY_IS_STORED),
                                    singleSteelSheet.optInt(Constants.KEY_QTY),
                                    singleSteelSheet.optDouble(Constants.KEY_STEEL_SHEET_THICKNESS),
                                    singleSteelSheet.optDouble(Constants.KEY_STEEL_SHEET_LENGTH),
                                    singleSteelSheet.optDouble(Constants.KEY_STEEL_SHEET_WIDTH),
                                    singleSteelSheet.optDouble(Constants.KEY_STEEL_SHEET_AREA),
                                    singleSteelSheet.optString(Constants.KEY_STEEL_SHEET_MATERIAL)
                            ));
                        }
                    }

                    steelSheetsListAdapter = new SteelSheetsListAdapter(context,
                            R.layout.list_item_steel_sheets, steelSheetsList);
                    contractView.populateSteelSheetsList(steelSheetsListAdapter);
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

    public void populateAttributesDialog(int position, TextView tvBarcode, TextView tvMaterial,
                                         TextView tvThickness, TextView tvLength, TextView tvWidth,
                                         TextView tvArea, TextView tvQty) {

        String tmpString = "";

        tvBarcode.setText(steelSheetsListAdapter.getItem(position).getSteelSheetBarcode());
        if(!(steelSheetsListAdapter.getItem(position).getMaterial() != null)) {
            tmpString = context.getResources().getString(R.string.tvMaterialText_dialog_attributes) +
                    " " + steelSheetsListAdapter.getItem(position).getMaterial();
        }else {
            tmpString = context.getResources().getString(R.string.tvMaterialText_dialog_attributes) + " - ";
        }
        tvMaterial.setText(tmpString);

        if(!Double.isNaN(steelSheetsListAdapter.getItem(position).getThickness())){
            tmpString = context.getResources().getString(R.string.tvThicknessText_dialog_attributes) +
                    " " + steelSheetsListAdapter.getItem(position).getThickness();
        }else {
            tmpString = context.getResources().getString(R.string.tvThicknessText_dialog_attributes) + " - ";
        }
        tvThickness.setText(tmpString);

        if(!Double.isNaN(steelSheetsListAdapter.getItem(position).getLength())){
            tmpString = context.getResources().getString(R.string.tvLengthText_dialog_attributes) +
                    " " + steelSheetsListAdapter.getItem(position).getLength();
        }else {
            tmpString = context.getResources().getString(R.string.tvLengthText_dialog_attributes) + " - ";
        }
        tvLength.setText(tmpString);

        if(!Double.isNaN(steelSheetsListAdapter.getItem(position).getWidth())){
            tmpString = context.getResources().getString(R.string.tvWidthText_dialog_attributes) +
                    " " + steelSheetsListAdapter.getItem(position).getWidth();
        }else {
            tmpString = context.getResources().getString(R.string.tvWidthText_dialog_attributes) +" - ";
        }
        tvWidth.setText(tmpString);

        if(!Double.isNaN(steelSheetsListAdapter.getItem(position).getArea())){
            tmpString = context.getResources().getString(R.string.tvAreaText_dialog_attributes) +
                    " " + steelSheetsListAdapter.getItem(position).getArea();
        }else {
            tmpString = context.getResources().getString(R.string.tvAreaText_dialog_attributes) + " - ";
        }
        tvArea.setText(tmpString);
        tmpString = context.getResources().getString(R.string.tvQtyText_dialog_attributes) +
                " " + steelSheetsListAdapter.getItem(position).getQty();
        tvQty.setText(tmpString);

    }


    /* ASYNC TASKS */
    private class getSteelSheetsAsyncTask extends AsyncTask<String, String, String> {

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

            jsonObject = steelSheet.getAllSteelSheets();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int success;
            try {
                success = jsonObject.optInt(Constants.KEY_SUCCESS);
                if(success == 1) {
                    JSONArray jsonResult = jsonObject.getJSONArray(Constants.KEY_DATA);
                    steelSheetsList = new ArrayList<>();
                    int steelSheetsQty = jsonResult.length();
                    if(steelSheetsQty >= 1) {
                        for (int i = 0; i < steelSheetsQty; i++) {
                            JSONObject singleSteelSheet = jsonResult.getJSONObject(i);
                            steelSheetsList.add(new SteelSheetModel(
                                    singleSteelSheet.optInt(Constants.KEY_ID),
                                    singleSteelSheet.optString(Constants.KEY_STEEL_SHEET_CODE),
                                    singleSteelSheet.optInt(Constants.KEY_SECTOR_ID),
                                    singleSteelSheet.optString(Constants.KEY_SUBSECTOR_NAME),
                                    singleSteelSheet.optString(Constants.KEY_SECTOR_NAME),
                                    singleSteelSheet.optString(Constants.KEY_WAREHOUSE_NAME),
                                    singleSteelSheet.optInt(Constants.KEY_IS_STORED),
                                    singleSteelSheet.optInt(Constants.KEY_QTY),
                                    singleSteelSheet.optDouble(Constants.KEY_STEEL_SHEET_THICKNESS),
                                    singleSteelSheet.optDouble(Constants.KEY_STEEL_SHEET_LENGTH),
                                    singleSteelSheet.optDouble(Constants.KEY_STEEL_SHEET_WIDTH),
                                    singleSteelSheet.optDouble(Constants.KEY_STEEL_SHEET_AREA),
                                    singleSteelSheet.optString(Constants.KEY_STEEL_SHEET_MATERIAL)
                            ));
                        }
                        steelSheetsListAdapter = new SteelSheetsListAdapter(
                                context, R.layout.list_item_steel_sheets, steelSheetsList);
                        contractView.populateSteelSheetsList(steelSheetsListAdapter);
                    } else {

                        Toast.makeText(context, "Brak danych do wyświetlenia: " + jsonObject.getString(Constants.KEY_MESSAGE),
                                Toast.LENGTH_LONG).show();
                    }
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
