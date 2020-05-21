package com.zbud.mobilneblachy.Contract;

import com.zbud.mobilneblachy.Helper.SteelSheetsInProcessListAdapter;

public interface ReturnSteelSheetContract {


    interface Presenter {

    }

    interface View {
        void populateSteelSheetsInProcessList(SteelSheetsInProcessListAdapter steelSheetsInProcessListAdapter);
        void sendDataToAddSteelSheetFragment(String sheetBarcode, int qty);
    }
}
