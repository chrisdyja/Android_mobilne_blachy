package com.zbud.mobilneblachy.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.zbud.mobilneblachy.R;

import static com.zbud.mobilneblachy.Helper.FragmentsManager.changeFragments;

public class MainFragment extends Fragment {

    private View view;
    private Button btnAdd;
    private Button btnShow;
    private Button btnManage;
    private ImageView ivCompanyLogo;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        this.view = inflater.inflate(R.layout.fragment_main, container, false);
        btnAdd = view.findViewById(R.id.btnAdd_main_fragment);
        btnShow = view.findViewById(R.id.btnShow_main_fragment);
        btnManage = view.findViewById(R.id.btnManage_main_fragment);

        ivCompanyLogo = (ImageView) view.findViewById(R.id.ivCompanyLogo_main_fragment);
        int companyLogoResource = getResources().getIdentifier("@drawable/zbud_logo",null, getContext().getPackageName());
        ivCompanyLogo.setImageResource(companyLogoResource);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new AddSteelSheetsFragment(), true, "addSteelSheets", getFragmentManager());
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new ShowSteelSheetsFragment(), true, "showSteelSheets", getFragmentManager());
            }
        });

        btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new ManageSteelSheetFragment(), true, "manageSteelSheets", getFragmentManager());
            }
        });

        return this.view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.view = null;
        this.btnAdd = null;
        this.btnShow = null;
        this.btnManage = null;
        this.ivCompanyLogo = null;
    }
}
