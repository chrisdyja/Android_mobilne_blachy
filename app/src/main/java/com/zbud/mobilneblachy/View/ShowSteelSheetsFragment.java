package com.zbud.mobilneblachy.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zbud.mobilneblachy.Contract.ShowSteelSheetsContract;
import com.zbud.mobilneblachy.Helper.SteelSheetsListAdapter;
import com.zbud.mobilneblachy.Presenter.ShowSteelSheetsPresenter;
import com.zbud.mobilneblachy.R;

import static com.zbud.mobilneblachy.Helper.FragmentsManager.changeFragments;
import static com.zbud.mobilneblachy.Helper.SoftKeyboardManager.hideKeyboard;


public class ShowSteelSheetsFragment extends Fragment implements ShowSteelSheetsContract.View {

    private ShowSteelSheetsPresenter showSteelSheetsPresenter;
    private View view;

    private Button btnBack;
    private EditText edtSteelSheetBarcode;
    private ListView lvSteelSheets;

    public ShowSteelSheetsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        view = inflater.inflate(R.layout.fragment_show_steel_sheets, container, false);
        showSteelSheetsPresenter = new ShowSteelSheetsPresenter(this, getActivity().getWindow().getContext());
        btnBack = view.findViewById(R.id.btnBack_fragment_show);
        edtSteelSheetBarcode = view.findViewById(R.id.edtSteelSheetBarcode_fragment_show);
        lvSteelSheets = view.findViewById(R.id.lvSteelSheets_fragment_show);

        showSteelSheetsPresenter.getSteelSheets();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(view);
                changeFragments(new MainFragment(), false, "main", getFragmentManager());
            }
        });

        edtSteelSheetBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String barcodeFragment = s.toString().trim();
                showSteelSheetsPresenter.searchForSteelSheets(barcodeFragment);
            }
        });

        lvSteelSheets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                TextView tvBarcode, tvMaterial, tvThickness,
                        tvLength, tvWidth, tvArea, tvQty;

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_steel_sheet_attributes, null);
                Dialog attributesDialog = new Dialog(getContext());
                attributesDialog.setContentView(view);
                attributesDialog.setTitle("Atrybuty");
                attributesDialog.setCancelable(true);

                tvBarcode = view.findViewById(R.id.tvBarcode_dialog_attributes);
                tvMaterial = view.findViewById(R.id.tvMaterial_dialog_attributes);
                tvThickness = view.findViewById(R.id.tvThickness_dialog_attributes);
                tvLength = view.findViewById(R.id.tvLength_dialog_attributes);
                tvWidth = view.findViewById(R.id.tvWidth_dialog_attributes);
                tvArea = view.findViewById(R.id.tvArea_dialog_attributes);
                tvQty = view.findViewById(R.id.tvQty_dialog_attributes);


                showSteelSheetsPresenter.populateAttributesDialog(position, tvBarcode, tvMaterial,
                        tvThickness, tvLength, tvWidth, tvArea, tvQty);
                attributesDialog.show();
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
        this.showSteelSheetsPresenter = null;
        this.btnBack = null;
        this.edtSteelSheetBarcode = null;
        this.view = null;
        this.lvSteelSheets = null;
    }

    public void populateSteelSheetsList(SteelSheetsListAdapter steelSheetsListAdapter) {
//        this.steelSheetsListAdapter = steelSheetsListAdapter;
        lvSteelSheets.setAdapter(steelSheetsListAdapter);
    }

}
