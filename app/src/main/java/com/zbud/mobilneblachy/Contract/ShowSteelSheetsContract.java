package com.zbud.mobilneblachy.Contract;

import com.zbud.mobilneblachy.Helper.SteelSheetsListAdapter;

public interface ShowSteelSheetsContract {

    interface Presenter {

    }

    interface View {
        void populateSteelSheetsList(SteelSheetsListAdapter steelSheetsListAdapter);
    }
}
