package com.zbud.mobilneblachy.Helper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zbud.mobilneblachy.R;

public class FragmentsManager {

    public static void changeFragments(Fragment fragment, boolean addToBackStack, String tag, FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) { ft.addToBackStack(tag); }

        ft.replace(R.id.flFragmentHolder, fragment, tag);
        ft.commitAllowingStateLoss();
    }

}
