package com.zbud.mobilneblachy.View;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.zbud.mobilneblachy.Contract.ManageSteelSheetContract;
import com.zbud.mobilneblachy.R;

import static com.zbud.mobilneblachy.Helper.FragmentsManager.changeFragments;


public class ManageSteelSheetFragment extends Fragment implements ManageSteelSheetContract.View {

    private View view;
    private Button btnPullOut, btnReturn, btnBack;


    public ManageSteelSheetFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        view = inflater.inflate(R.layout.fragment_manage_steel_sheet, container, false);
        btnPullOut = view.findViewById(R.id.btnPullOut_fragment_manage_steel_sheet);
        btnReturn = view.findViewById(R.id.btnReturn_fragment_manage_steel_sheet);
        btnBack = view.findViewById(R.id.btnBack_fragment_manage_steel_sheet);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new MainFragment(), false, "main", getFragmentManager());
            }
        });

        btnPullOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new PullOutSteelSheetFragment(), true, "pullOutSteelSheet", getFragmentManager());
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new ReturnSteelSheetFragment(), true, "returnSteelSheet", getFragmentManager());
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}