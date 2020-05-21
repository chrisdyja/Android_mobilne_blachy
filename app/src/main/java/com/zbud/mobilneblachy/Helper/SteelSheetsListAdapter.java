package com.zbud.mobilneblachy.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.zbud.mobilneblachy.Model.SteelSheetModel;
import com.zbud.mobilneblachy.R;

import java.util.List;

public class SteelSheetsListAdapter extends ArrayAdapter<SteelSheetModel> {

    Context context;
    int resource;
    List<SteelSheetModel> steelSheetsList;

    public SteelSheetsListAdapter(Context context, int resource, List<SteelSheetModel> steelSheetsList) {
        super(context, resource, steelSheetsList);
        this.context = context;
        this.resource = resource;
        this.steelSheetsList = steelSheetsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);


        TextView tvSteelSheetId = view.findViewById(R.id.tvSteelSheetId_list_item);
        TextView tvSteelSheetBarcode = view.findViewById(R.id.tvSteelSheetBarcode_list_item);
        TextView tvSteelSheetLocation = view.findViewById(R.id.tvSteelSheetLocation_list_item);

        SteelSheetModel steelSheetModel = steelSheetsList.get(position);

        String tmpString;

        if(steelSheetModel.getIsStored() == 0) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIsNotStored));
            tmpString = Constants.STRING_OUTSIDE_WAREHOUSE;
        }else {
            tmpString = steelSheetModel.getWarehouseName()+" / "+steelSheetModel.getSectorName()+
                    " / "+steelSheetModel.getSubsectorName();
        }

        tvSteelSheetId.setText(Integer.toString(steelSheetModel.getId()));
        tvSteelSheetBarcode.setText(steelSheetModel.getSteelSheetBarcode());
        tvSteelSheetLocation.setText(tmpString);

        return view;
    }
}
