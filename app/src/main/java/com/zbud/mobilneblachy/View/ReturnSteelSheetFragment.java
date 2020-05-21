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
import android.widget.Toast;

import com.zbud.mobilneblachy.Contract.ReturnSteelSheetContract;
import com.zbud.mobilneblachy.Helper.Constants;
import com.zbud.mobilneblachy.Helper.SoftKeyboardManager;
import com.zbud.mobilneblachy.Helper.SteelSheetsInProcessListAdapter;
import com.zbud.mobilneblachy.Presenter.ReturnSteelSheetPresenter;
import com.zbud.mobilneblachy.R;


import static com.zbud.mobilneblachy.Helper.FragmentsManager.changeFragments;
import static com.zbud.mobilneblachy.Helper.SoftKeyboardManager.disableKeyboardShowing;
import static com.zbud.mobilneblachy.Helper.SoftKeyboardManager.hideKeyboard;


public class ReturnSteelSheetFragment extends Fragment implements ReturnSteelSheetContract.View {

    ReturnSteelSheetPresenter returnSteelSheetPresenter;
    private View view;

    private Button btnBack;
    private EditText edtSteelSheetBarcode;
    private ListView lvSteelSheetsInProcess;


    public ReturnSteelSheetFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        view = inflater.inflate(R.layout.fragment_return_steel_sheet, container, false);
        returnSteelSheetPresenter = new ReturnSteelSheetPresenter(this, getActivity().getWindow().getContext());
        lvSteelSheetsInProcess = view.findViewById(R.id.lvSteelSheetsInProcess_fragment_return);
        edtSteelSheetBarcode = view.findViewById(R.id.edtSteelSheetBarcode_fragment_return);
        btnBack = view.findViewById(R.id.btnBack_fragment_return);

        returnSteelSheetPresenter.getSteelSheetsInProcess();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(view);
                changeFragments(new ManageSteelSheetFragment(), false, "main", getFragmentManager());
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
                returnSteelSheetPresenter.searchForSteelSheets(barcodeFragment);
            }
        });

        lvSteelSheetsInProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

                Button btnCancel,  btnAddToQty, btnSubstractFromQty,
                        btnReturnToWarehouse, btnSetSteelSheetAsProcessed;
                final EditText edtSteelSheetsQty;

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view = inflater.inflate(R.layout.dialog_steel_sheet_in_process_action, null);
                final Dialog steelSheetInProcessActionsDialog = new Dialog(getContext());
                steelSheetInProcessActionsDialog.setContentView(view);
                steelSheetInProcessActionsDialog.setTitle("Zarządzaj"); // nie potrzebne chyba, bo i tak nie widać tego tytułu
                steelSheetInProcessActionsDialog.setCancelable(true);

                edtSteelSheetsQty = view.findViewById(R.id.edtSteelSheetsQty_dialog_process_actions);
                btnAddToQty = view.findViewById(R.id.btnAddToQty_dialog_process_actions);
                btnSubstractFromQty = view.findViewById(R.id.btnSubtractFromQty_dialog_process_actions);
                btnCancel = view.findViewById(R.id.btnCancel_dialog_locations);
                btnReturnToWarehouse = view.findViewById(R.id.btnReturnToWarehouse_dialog_process_actions);
                btnSetSteelSheetAsProcessed = view.findViewById(R.id.btnSetSteelSheetAsProcessed_dialog_process_actions);

                steelSheetInProcessActionsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                SoftKeyboardManager.disableKeyboardShowing(edtSteelSheetsQty);

                edtSteelSheetsQty.setText("1");

                disableKeyboardShowing(edtSteelSheetsQty);
                steelSheetInProcessActionsDialog.show();
                btnSetSteelSheetAsProcessed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int qty;
                        boolean wrongQty = false;
                        qty = Integer.parseInt(edtSteelSheetsQty.getText().toString());
                        wrongQty = returnSteelSheetPresenter.setSteelSheetAsProcessed(qty, position);
                        if(!edtSteelSheetsQty.getText().toString().equals("0")) {
                            if (!wrongQty) {
                                steelSheetInProcessActionsDialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Wprowadzona ilość arkuszy jest zbyt duża.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Wprowadzona ilość arkuszy nie może wynosić zero.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                btnReturnToWarehouse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int qty;
                        boolean wrongQty = false;
                        qty = Integer.parseInt(edtSteelSheetsQty.getText().toString());
                        wrongQty = returnSteelSheetPresenter.getSteelSheetInProcessInfo(position, qty);
                        if(!edtSteelSheetsQty.getText().toString().equals("0")) {
                            if (!wrongQty) {
                                steelSheetInProcessActionsDialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Wprowadzona ilość arkuszy jest zbyt duża.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Wprowadzona ilość arkuszy nie może wynosić zero.", Toast.LENGTH_LONG).show();
                        }
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

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        steelSheetInProcessActionsDialog.dismiss();
                    }
                });

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
    public void populateSteelSheetsInProcessList(SteelSheetsInProcessListAdapter steelSheetsInProcessListAdapter) {
        lvSteelSheetsInProcess.setAdapter(steelSheetsInProcessListAdapter);
    }

    @Override
    public void sendDataToAddSteelSheetFragment(String sheetBarcode, int qty){
        Bundle bundle = new Bundle();
        bundle.putString("sheetBarcode", sheetBarcode);
        bundle.putInt("qty", qty);
        AddSteelSheetsFragment addSteelSheetsFragment = new AddSteelSheetsFragment();
        addSteelSheetsFragment.setArguments(bundle);
        changeFragments(addSteelSheetsFragment, false, "main", getFragmentManager());
    };
}
