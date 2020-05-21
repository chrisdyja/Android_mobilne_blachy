package com.zbud.mobilneblachy.View;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zbud.mobilneblachy.Contract.PullOutSteelSheetContract;
import com.zbud.mobilneblachy.Helper.CheckNetworkStatus;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.DialogSteelSheetLocationsAdapter;
import com.zbud.mobilneblachy.Helper.SoftKeyboardManager;
import com.zbud.mobilneblachy.Presenter.PullOutSteelSheetPresenter;
import com.zbud.mobilneblachy.R;

import static com.zbud.mobilneblachy.Helper.FragmentsManager.changeFragments;


public class PullOutSteelSheetFragment extends Fragment implements PullOutSteelSheetContract.View {

    private View view;
    private EditText edtBarcodeScanField, edtSteelSheetsQty;
    private Button btnPullOut, btnBack, btnShowKeyboard, btnAddToQty, btnSubstractFromQty;
    private CheckBox chbIsForProcessing;
    private PullOutSteelSheetPresenter pullOutSteelSheetPresenter;



    public PullOutSteelSheetFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(wifiStateReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pullOutSteelSheetPresenter = new PullOutSteelSheetPresenter(this, getActivity().getWindow().getContext());
        view = inflater.inflate(R.layout.fragment_pull_out_steel_sheet, container, false);
        edtBarcodeScanField = view.findViewById(R.id.edtBarcodeScanField_fragment_pull_out_steel_sheet);
        edtSteelSheetsQty = view.findViewById(R.id.edtSteelSheetsQty_fragment_pull_out_steel_sheet);
        btnAddToQty = view.findViewById(R.id.btnAddToQty_fragment_pull_out_steel_sheet);
        btnSubstractFromQty = view.findViewById(R.id.btnSubtractFromQty_fragment_pull_out_steel_sheet);
        btnPullOut = view.findViewById(R.id.btnPullOut_fragment_pull_out_steel_sheet);
        btnBack = view.findViewById(R.id.btnBack_fragment_pull_out_steel_sheet);
        btnShowKeyboard = view.findViewById(R.id.btnShowKeyboard_fragment_pull_out_steel_sheet);
        chbIsForProcessing = view.findViewById(R.id.chbIsForProcessing_fragment_pull_out_steel_sheet);

        edtBarcodeScanField.requestFocus();
        SoftKeyboardManager.disableKeyboardShowing(edtBarcodeScanField);
        SoftKeyboardManager.disableKeyboardShowing(edtSteelSheetsQty);

        edtSteelSheetsQty.setText("1");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new ManageSteelSheetFragment(), false, "main", getFragmentManager());
            }
        });

        btnPullOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String steelSheetBarcode = edtBarcodeScanField.getText().toString().trim();
                String steelSheetQty = edtSteelSheetsQty.getText().toString().trim();
                if(steelSheetBarcode.equals(Constants.STRING_EMPTY) ||
                    steelSheetQty.equals(Constants.STRING_EMPTY) ||
                    steelSheetQty.equals("0")
                ) {
                    Toast.makeText(view.getContext(), "Nie wprowadzono kodu blachy lub ilości.", Toast.LENGTH_LONG).show();
                }else {
                    pullOutSteelSheetPresenter.getSteelSheetLocations(steelSheetBarcode);
                }
            }
        });


        btnShowKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftKeyboardManager.showKeyboard(getView());
            }
        });


        edtSteelSheetsQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(edtSteelSheetsQty != null)
                    {
                        String steelSheetsQty = edtSteelSheetsQty.getText().toString().trim();
                        if(steelSheetsQty.equals(Constants.STRING_EMPTY) || steelSheetsQty.equals("0"))  {
                            edtSteelSheetsQty.setText("1");
                        }
                    }
                }
            }
        });

        btnAddToQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQty;
                if(!edtSteelSheetsQty.getText().toString().equals(Constants.STRING_EMPTY)){
                    currentQty = Integer.parseInt(edtSteelSheetsQty.getText().toString());
                    currentQty += 1;
                } else {
                    currentQty = 1;
                }
                edtSteelSheetsQty.setText("" + currentQty);

            }
        });

        btnSubstractFromQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQty;
                if(!edtSteelSheetsQty.getText().toString().equals(Constants.STRING_EMPTY)) {
                    currentQty = Integer.parseInt(edtSteelSheetsQty.getText().toString());
                    if (currentQty > 1) {
                        currentQty -= 1;
                    } else {
                        currentQty = 1;
                    }
                } else {
                    currentQty = 1;
                }
                edtSteelSheetsQty.setText("" + currentQty);
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
        getContext().unregisterReceiver(wifiStateReceiver);
        this.pullOutSteelSheetPresenter = null;
        this.view = null;
        this.edtBarcodeScanField = null;
        this.edtSteelSheetsQty = null;
        this.btnPullOut = null;
        this.btnBack = null;
        this.btnShowKeyboard = null;
        this.btnAddToQty = null;
        this.btnSubstractFromQty = null;
    }

    @Override
    public void clearEdtBarcodeScanField() {
        edtBarcodeScanField.setText(Constants.STRING_EMPTY);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                if (CheckNetworkStatus.isNetworkAvailable(context)) {
                    edtBarcodeScanField.setEnabled(true);
                    btnPullOut.setEnabled(true);
                    btnShowKeyboard.setEnabled(true);

                } else {
                    edtBarcodeScanField.setEnabled(false);
                    btnPullOut.setEnabled(false);
                    btnShowKeyboard.setEnabled(false);
                }
            }
        }
    };

    public void populateOneSteelSheetLocationsList (final DialogSteelSheetLocationsAdapter steelSheetLocationsAdapter) {

        ListView lvSteelSheetLocation;
        Button btnCancel;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_steel_sheet_locations, null);
        final Dialog attributesDialog = new Dialog(getContext());
        attributesDialog.setContentView(view);
        attributesDialog.setTitle("Lokalizacje");
        attributesDialog.setCancelable(true);
        attributesDialog.show();

        btnCancel = view.findViewById(R.id.btnCancel_dialog_locations);
        lvSteelSheetLocation = view.findViewById(R.id.lvSteelSheetLocation_dialog_locations);
        lvSteelSheetLocation.setAdapter(steelSheetLocationsAdapter);
        lvSteelSheetLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pullOutSteelSheetPresenter.pullOutSteelSheetsFromWarehouses(
                        edtBarcodeScanField.getText().toString().trim(),
                        edtSteelSheetsQty.getText().toString().trim(),
                        Integer.toString(steelSheetLocationsAdapter.getItem(position).getSubsectorId()),
                        chbIsForProcessing.isChecked());
                attributesDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attributesDialog.dismiss();
            }
        });
    }
}

