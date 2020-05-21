package com.zbud.mobilneblachy.Contract;

import com.zbud.mobilneblachy.Helper.DialogSteelSheetLocationsAdapter;

public interface PullOutSteelSheetContract {


    interface Presenter {

    }

    interface View {
        void clearEdtBarcodeScanField();
        void populateOneSteelSheetLocationsList (DialogSteelSheetLocationsAdapter dialogSteelSheetLocationsAdapter);
    }

}
