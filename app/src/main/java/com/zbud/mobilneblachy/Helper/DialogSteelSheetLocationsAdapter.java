package com.zbud.mobilneblachy.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zbud.mobilneblachy.Model.SteelSheetModel;
import com.zbud.mobilneblachy.R;

import java.util.List;


public class DialogSteelSheetLocationsAdapter extends ArrayAdapter<SteelSheetModel>{

    Context context;
    int resource;
    List<SteelSheetModel> steelSheetLocationList;

    public DialogSteelSheetLocationsAdapter(Context context, int resource, List<SteelSheetModel> steelSheetLocationList) {
        super(context, resource, steelSheetLocationList);
        this.context = context;
        this.resource = resource;
        this.steelSheetLocationList = steelSheetLocationList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);

        TextView tvSteelSheetLocation = view.findViewById(R.id.tvSteelSheetLocation_dialog_locations);
        TextView tvSteelSheetQty = view.findViewById(R.id.tvSteelSheetQty_dialog_locations);

        SteelSheetModel steelSheetModel = steelSheetLocationList.get(position);

        String tmpString;

        tmpString = steelSheetModel.getWarehouseName()+" / "+steelSheetModel.getSectorName()+
                " / "+steelSheetModel.getSubsectorName();


        tvSteelSheetLocation.setText(tmpString);
        tvSteelSheetQty.setText(Integer.toString(steelSheetModel.getQty()));

        return view;
    }
}



