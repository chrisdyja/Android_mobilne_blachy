package com.zbud.mobilneblachy.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;


import com.zbud.mobilneblachy.Contract.AddSteelSheetsContract;
import com.zbud.mobilneblachy.Helper.CheckNetworkStatus;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.SoftKeyboardManager;
import com.zbud.mobilneblachy.Presenter.AddSteelSheetsPresenter;
import com.zbud.mobilneblachy.R;

import static com.zbud.mobilneblachy.Helper.FragmentsManager.changeFragments;


public class AddSteelSheetsFragment extends Fragment implements AddSteelSheetsContract.View {

    private AddSteelSheetsPresenter addSteelSheetsPresenter;
    private EditText edtBarcodeScanField, edtSteelSheetsQty;
    private Spinner spnWarehouseList, spnSectorsList, spnSubsectorsList;
    private Button btnBack, btnSendToDataBase, btnShowKeyboard, btnAddToQty, btnSubstractFromQty;
    private View view;
    private String warehouseName, sectorName;
    private Boolean warehousesListLoaded, returnFromProcessing;



    public AddSteelSheetsFragment() {
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

        view = inflater.inflate(R.layout.fragment_add_steel_sheets, container, false);
        edtBarcodeScanField = view.findViewById(R.id.edtBarcodeScanField_fragment_add);
        edtSteelSheetsQty = view.findViewById(R.id.edtSteelSheetsQty_fragment_add);
        edtSteelSheetsQty.setText("1");
        btnAddToQty = view.findViewById(R.id.btnAddToQty_fragment_add);
        btnSubstractFromQty = view.findViewById(R.id.btnSubtractFromQty_fragment_add);
        btnSendToDataBase = view.findViewById(R.id.btnSendToDataBase_fragment_add);
        spnWarehouseList =  view.findViewById(R.id.spnWarehouseList_fragment_add);
        spnSectorsList = view.findViewById(R.id.spnSectorsList_fragment_add);
        spnSubsectorsList = view.findViewById(R.id.spnSubsectorsList_fragment_add);
        btnBack = view.findViewById(R.id.btnBack_fragment_add);
        btnShowKeyboard = view.findViewById(R.id.btnShowKeyboard_fragment_add);

        returnFromProcessing = false;
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            edtBarcodeScanField.setText(bundle.get("sheetBarcode").toString());
            edtSteelSheetsQty.setText(bundle.get("qty").toString());
            returnFromProcessing = true;
        }
        btnSendToDataBase.setEnabled(false);

        addSteelSheetsPresenter = new AddSteelSheetsPresenter(this, getActivity().getWindow().getContext());
        warehousesListLoaded = addSteelSheetsPresenter.populateSpnWarehouseList(false);

        edtBarcodeScanField.requestFocus();
        SoftKeyboardManager.disableKeyboardShowing(edtBarcodeScanField);
        SoftKeyboardManager.disableKeyboardShowing(edtSteelSheetsQty);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new MainFragment(), false, "main", getFragmentManager());
            }
        });

        btnShowKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                edtBarcodeScanField.requestFocus();
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
                String steelSheetsQty = edtSteelSheetsQty.getText().toString().trim();
                int currentQty;
                if(!steelSheetsQty.equals(Constants.STRING_EMPTY)){
                    currentQty = Integer.parseInt(steelSheetsQty);
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
                String steelSheetsQty = edtSteelSheetsQty.getText().toString().trim();
                int currentQty;
                if(!steelSheetsQty.equals(Constants.STRING_EMPTY)) {
                    currentQty = Integer.parseInt(steelSheetsQty.toString());
                    if (currentQty > 1) {
                        currentQty -= 1;
                    } else {
                        currentQty = 1;
                    }
                } else {
                    currentQty = 1;
                }
                String tmpString = Integer.toString(currentQty);
                edtSteelSheetsQty.setText(tmpString);
            }
        });

        btnSendToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcodeScanField = edtBarcodeScanField.getText().toString().trim();
                String subsectorName = spnSubsectorsList.getSelectedItem().toString().trim();
                String sectorName = spnSectorsList.getSelectedItem().toString();
                String warehouseName = spnWarehouseList.getSelectedItem().toString().trim();
                String sheetsQty = edtSteelSheetsQty.getText().toString().trim();
                if(!barcodeScanField.equals(Constants.STRING_EMPTY) &&
                    spnWarehouseList.getSelectedItemPosition() >= 1 &&
                    spnWarehouseList.getSelectedItem() != null &&
                    spnSectorsList.getSelectedItemPosition() >= 1 &&
                    spnSectorsList.getSelectedItem() != null &&
                    spnSubsectorsList.getSelectedItemPosition() >= 1 &&
                    spnSubsectorsList.getSelectedItem() != null &&
                        (!edtSteelSheetsQty.getText().toString().equals("")) &&
                        (!edtSteelSheetsQty.getText().toString().equals("0"))
                ) {
                    addSteelSheetsPresenter.addSteelSheetsToDatabase(
                        barcodeScanField,
                        subsectorName,
                        sectorName,
                        warehouseName,
                        Integer.parseInt(sheetsQty),
                        returnFromProcessing);
                }else {
                    Toast.makeText(getContext(),"Nie uzupełniono wszystkich pól!",Toast.LENGTH_LONG).show();
                }
            }
        });

        spnWarehouseList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!warehousesListLoaded && CheckNetworkStatus.isNetworkAvailable(getContext())) {
                    warehousesListLoaded = addSteelSheetsPresenter.populateSpnWarehouseList(false);
                }
                return false;
            }
        });

        spnWarehouseList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    addSteelSheetsPresenter.populateSpnSectorsList(warehouseName, true);
                }else {
                    warehouseName = String.valueOf(spnWarehouseList.getSelectedItem());
                    addSteelSheetsPresenter.populateSpnSectorsList(warehouseName, false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnSectorsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    addSteelSheetsPresenter.populateSpnSubsectorsList(sectorName, warehouseName,true);
                }else {
                    sectorName = String.valueOf(spnSectorsList.getSelectedItem());
                    warehouseName = String.valueOf(spnWarehouseList.getSelectedItem());
                    addSteelSheetsPresenter.populateSpnSubsectorsList(sectorName, warehouseName, false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnSubsectorsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(wifiStateReceiver);
        this.addSteelSheetsPresenter = null;
        this.view = null;
        this.addSteelSheetsPresenter = null;
        this.edtBarcodeScanField = null;
        this.edtSteelSheetsQty = null;
        this.spnWarehouseList = null;
        this.spnSectorsList = null;
        this.spnSubsectorsList = null;
        this.btnBack = null;
        this.btnSendToDataBase = null;
        this.btnShowKeyboard = null;
        this.btnAddToQty = null;
        this.btnSubstractFromQty = null;
        this.view = null;
        this.warehouseName = null;
        this.sectorName = null;
        this.warehousesListLoaded = null;
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
    public void populateSpnWarehouseList(ArrayAdapter<String> arrayAdapter) {
        spnWarehouseList.setAdapter(arrayAdapter);
    }

    @Override
    public void populateSpnSectorsList(ArrayAdapter<String> arrayAdapter) {
        spnSectorsList.setAdapter(arrayAdapter);
    }

    @Override
    public void populateSpnSubsectorsList(ArrayAdapter<String> arrayAdapter) {
        spnSubsectorsList.setAdapter(arrayAdapter);
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

                    btnSendToDataBase.setEnabled(true);
                    spnSubsectorsList.setEnabled(true);
                    spnSectorsList.setEnabled(true);
                    spnWarehouseList.setEnabled(true);
                    edtBarcodeScanField.setEnabled(true);
                    btnShowKeyboard.setEnabled(true);

                } else {
                    btnSendToDataBase.setEnabled(false);
                    spnSubsectorsList.setEnabled(false);
                    spnSectorsList.setEnabled(false);
                    spnWarehouseList.setEnabled(false);
                    edtBarcodeScanField.setEnabled(false);
                    btnShowKeyboard.setEnabled(false);
                }
            }
        }
    };
}
