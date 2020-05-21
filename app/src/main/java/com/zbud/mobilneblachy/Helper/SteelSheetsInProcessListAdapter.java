package com.zbud.mobilneblachy.Helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zbud.mobilneblachy.Model.SteelSheetInProcessModel;
import com.zbud.mobilneblachy.R;

import java.util.List;

public class SteelSheetsInProcessListAdapter extends ArrayAdapter<SteelSheetInProcessModel> {

    Context context;
    int resource;
    List<SteelSheetInProcessModel> steelSheetsInProcessList;

    public SteelSheetsInProcessListAdapter(Context context, int resource, List<SteelSheetInProcessModel> steelSheetsInProcessList) {
        super(context, resource, steelSheetsInProcessList);
        this.context = context;
        this.resource = resource;
        this.steelSheetsInProcessList = steelSheetsInProcessList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);


        TextView tvSteelSheetBarcode = view.findViewById(R.id.tvSteelSheetBarcode_in_process_list_item);
        TextView tvSteelSheetQty = view.findViewById(R.id.tvSteelSheetQty_in_process_list_item);

        SteelSheetInProcessModel steelSheetInProcess = steelSheetsInProcessList.get(position);

        tvSteelSheetBarcode.setText(steelSheetInProcess.getSteelSheetBarcode());
        tvSteelSheetQty.setText(Integer.toString(steelSheetInProcess.getQty()));
        return view;
    }
}
