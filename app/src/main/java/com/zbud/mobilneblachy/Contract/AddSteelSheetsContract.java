package com.zbud.mobilneblachy.Contract;

import android.widget.ArrayAdapter;

public interface AddSteelSheetsContract {

    interface Presenter {

    }

    interface View {
        void populateSpnWarehouseList(ArrayAdapter<String> arrayAdapter);
        void populateSpnSectorsList(ArrayAdapter<String> arrayAdapter);
        void populateSpnSubsectorsList(ArrayAdapter<String> arrayAdapter);
        void clearEdtBarcodeScanField();
    }
}
