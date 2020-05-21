package com.zbud.mobilneblachy.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.zbud.mobilneblachy.Helper.SoftKeyboardManager;
import com.zbud.mobilneblachy.R;

import static com.zbud.mobilneblachy.Helper.FragmentsManager.changeFragments;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFragments(new MainFragment(), false, "main", getSupportFragmentManager());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoftKeyboardManager.hideKeyboard(getWindow().getCurrentFocus());
    }

    @Override
    protected void onStop() {
        super.onStop();
        SoftKeyboardManager.hideKeyboard(getWindow().getCurrentFocus());
    }
}
