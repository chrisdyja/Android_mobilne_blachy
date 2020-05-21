package com.zbud.mobilneblachy.Helper;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftKeyboardManager {

    private static InputMethodManager inputMethodManager;

    public static void hideKeyboard(View view) {
        if(view != null) {
            inputMethodManager = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(View view) {
        if(view != null) {
            inputMethodManager = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public static void disableKeyboardShowing(EditText editText) {
        if(editText != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                editText.setShowSoftInputOnFocus(false);
            } else {
                editText.setTextIsSelectable(true);
            }
        }
    }
}
