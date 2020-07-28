//package com.suprasoft.fragment;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.suprasoft.adapter.CustomListAdapter;
//import com.suprasoft.adapter.MedicinePrescriptionAdapter;
//import com.suprasoft.commonutils.Common;
//import com.suprasoft.commonutils.Constants;
//import com.suprasoft.customviews.KeyboardView;
//import com.suprasoft.network.RetrofitRequestController;
//import com.suprasoft.network.RetrofitResponseListener;
//import com.suprasoft.object.ApiRequestController;
//import com.suprasoft.object.ApiResponseController;
//import com.suprasoft.object.FragmentData;
//import com.suprasoft.database.DatabaseHandler;
//import com.suprasoft.object.Medicine;
//import com.suprasoft.object.MedicinePrescriptionReqVo;
//import com.suprasoft.object.PatientList;
//import com.suprasoft.object.Prescription;
//import com.suprasoft.object.PrescriptionSubmitReqVo;
//import com.suprasoft.umdaa.R;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by SupraSoft on 1/4/2019.
// */
//
//public class PrescriptionFragment extends android.support.v4.app.Fragment implements RetrofitResponseListener {
//    @BindView(R.id.auto_tv)
//    AutoCompleteTextView auto_tv;
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerview;
//    @BindView(R.id.tv_ok)
//    TextView tv_ok;
//    @BindView(R.id.submit)
//    Button submit;
//    //    @BindView(R.id.signature)
////    ImageView signatureText;
////    Dialog dialog;
////    @BindView(R.id.linearLayout)
////    LinearLayout mContent;
////    @BindView(R.id.getsign)
////    Button mGetSign;
////    View view;
//    /*  signature mSignature;*/
//
//    private String afterOrAfter;
//    private String intakeQty;
//    private String numberOfDays;
//    private String hours;
//    private String remarks;
//    private int morning;
//    private int afternoon;
//    private int night;
//    List<MedicinePrescriptionReqVo> medicinePrescriptionReqVoList = new ArrayList<>();
//    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/UMDAA/";
//    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//    String StoredPath = DIRECTORY + pic_name + ".png";
//    Prescription prescription;
//    PatientList patientList;
//    private Handler handler;
//    private MyRunnable myRunnable;
//    private Context context;
//    private Activity activity;
//    private boolean onMedicineItemClick = false;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = getLayoutInflater().inflate(R.layout.prescreption_fragment, null);
//        ButterKnife.bind(this, rootView);
//        if (activity != null) {
//            autoCompleteAdapter(new ArrayList<>());
//            patientList = (PatientList) getArguments().getSerializable("object");
//            prescription = FragmentData.getFragmentData().getPrescription();
//            if (prescription != null) {
//                medicinePrescriptionReqVoList = prescription.prescriptionItems;
//                if (medicinePrescriptionReqVoList == null)
//                    medicinePrescriptionReqVoList = new ArrayList<>();
//                if (prescription != null) {
//                    auto_tv.setText(prescription.autoTextData);
//                    setListOnAdapter(activity, medicinePrescriptionReqVoList, recyclerview);
//                }
//            }
//            autoCompleteAdapter(new ArrayList<>());
//            myRunnable = new MyRunnable();
//        }
//        return rootView;
//    }
//
//    public void autoCompleteAdapter(List<Medicine> list) {
//        final CustomListAdapter adapter = new CustomListAdapter(activity,
//                R.layout.autocomplete_custom_layout, list);
//
//        auto_tv.setAdapter(adapter);
//        auto_tv.showDropDown();
//        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, Medicine item) {
//                try {
//                    onMedicineItemClick = true;
//                    String title = item.formulation.toUpperCase() + " " + item.tradeName.toUpperCase();
//                    auto_tv.setText(title);
//                    auto_tv.setSelection(title.length());
//                    auto_tv.dismissDropDown();
//                    Common.hideSoftKeyboard(activity);
//                    timeTablePopup(activity, item, null, false, -1);
//                    //    Toast.makeText(getApplicationContext(),item.getCityName()+"\n"+item.getCityId(),Toast.LENGTH_SHORT).show();
//                    Log.d("onItemClick", item.tradeName);
//                } catch (Exception e) {
//                    Common.displayException(e, null);
//                }
///*
//                sel_customer_id = item.getCityId();
//                tv_customer.setTextColor(getResources().getColor(R.color.colorPrimary));
//                customername = item.getCityName();*/
//
//
//            }
//        });
//        auto_tv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.d("afterTextChanged", "afterTextChanged" + s.length());
//                if (s.length() > 0) {
//                    if (handler != null) {
//                        handler.removeCallbacks(myRunnable);
//                    }
//                    handler = new Handler();
//                    myRunnable.setData(s.toString());
//                    handler.postDelayed(myRunnable, 300);
//                    // auto_tv.setAdapter(adapter);
//
//                } else {
//                    auto_tv.dismissDropDown();
//                }
//
//            }
//        });
//
//        auto_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (auto_tv.getText().length() == 0) {
//                    //   auto_tv.setAdapter(adapter);
////                    auto_tv.showDropDown();
//                }
//            }
//        });
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (medicinePrescriptionReqVoList != null && medicinePrescriptionReqVoList.size() > 0) {
//                    PrescriptionSubmitReqVo submitReqVo = new PrescriptionSubmitReqVo();
//                    submitReqVo.prescription = medicinePrescriptionReqVoList;
//                    submitReqVo.patientId = patientList.patientId;
//                    submitReqVo.appointmentId = patientList.appointmentId;
//                    submitReqVo.clinicId = Common.getClinicId(getActivity());
//                    new RetrofitRequestController(PrescriptionFragment.this).sendRequest(Constants.RequestNames.PRESCRIPTION_SUBMIT, submitReqVo, true);
//                } else
//                    Toast.makeText(activity, "No Prescriptions to Submit", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @OnClick(R.id.tv_ok)
//    void ok_popup() {
////        Intent intent = new Intent(activity, PictureEditActivity.class);
////        startActivity(intent);
//
//        if (auto_tv.getText() == null || auto_tv.getText().toString() == null || auto_tv.getText().toString().trim().length() < 2) {
//            Toast.makeText(activity, "Please enter medicine name.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        try {
//            Medicine phrmacyInventoryPojo = new Medicine();
//            phrmacyInventoryPojo.drugId = "-1";
//            phrmacyInventoryPojo.tradeName = auto_tv.getText().toString().trim();
//            timeTablePopup(activity, phrmacyInventoryPojo, null, false, -1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setListOnAdapter(Context context, final List<MedicinePrescriptionReqVo> medicinePrescriptionReqVos, RecyclerView recyclerView) {
//        try {
//
//            Log.d("viewModal: ", medicinePrescriptionReqVos.toString());
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
//            recyclerView.setLayoutManager(gridLayoutManager);
//            MedicinePrescriptionAdapter medicinePrescriptionAdapter = null;
//
//            if (medicinePrescriptionReqVos.size() != 0) {
//                recyclerView.setVisibility(View.VISIBLE);
//                //recordnotfnd.setVisibility(View.GONE);
//                medicinePrescriptionAdapter = new MedicinePrescriptionAdapter(activity, medicinePrescriptionReqVos);
//                recyclerView.setAdapter(medicinePrescriptionAdapter);
//                MedicinePrescriptionAdapter finalMedicinePrescriptionAdapter = medicinePrescriptionAdapter;
//                medicinePrescriptionAdapter.setOnItemClickListener(new MedicinePrescriptionAdapter.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(View view, View itemView, int position) {
//                        if (view != null) {
//                            Log.d("ittem id: ", String.valueOf(view.getId()));
//                            if (view.getId() == R.id.iv_delete) {
//                                Log.d("ittem id: ", "delete  " + String.valueOf(view.getId()));
//
//                                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity).create();
//                                View diaglogView = PrescriptionFragment.this.getLayoutInflater().inflate(R.layout.warning_popup, null);
//                                alertDialog.setView(diaglogView);
//                                alertDialog.show();
//                                diaglogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        medicinePrescriptionReqVoList.remove(position);
//                                        finalMedicinePrescriptionAdapter.notifyDataSetChanged();
//                                        alertDialog.dismiss();
//                                    }
//                                });
//
//                                diaglogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        alertDialog.dismiss();
//                                    }
//                                });
//
//
//                            } else if (view.getId() == R.id.iv_edit) {
//                                Log.d("ittem id: ", "edit  " + String.valueOf(view.getId()));
//                                timeTablePopup(activity, null, medicinePrescriptionReqVos.get(position), true, position);
//                            }
//                        }
//                        finalMedicinePrescriptionAdapter.notifyDataSetChanged();
//                    }
//                });
//                medicinePrescriptionAdapter.notifyDataSetChanged();
//
//            } else if (medicinePrescriptionReqVos == null || medicinePrescriptionReqVos.size() == 0) {
//                recyclerView.setVisibility(View.GONE);
//            }
//        } catch (Exception e) {
//            Common.displayException(e, null);
//        }
//    }
//
//    /* private List<PhrmacyInventoryPojo> addItem() {
//         List<PhrmacyInventoryPojo> list = new ArrayList<>();
//         for (int i = 0; i < 50; i++) {
//             PhrmacyInventoryPojo phrmacyInventoryPojo = new PhrmacyInventoryPojo();
//             if (i < 10) {
//                 phrmacyInventoryPojo.medicineId = i;
//                 phrmacyInventoryPojo.medicineName = "DOLO " + i + "Mg";
//             } else if (i > 9 && i < 20) {
//                 phrmacyInventoryPojo.medicineId = i;
//                 phrmacyInventoryPojo.medicineName = "Calcol " + i + "Mg";
//             } else if (i > 19) {
//                 phrmacyInventoryPojo.medicineId = i;
//                 phrmacyInventoryPojo.medicineName = "Sea-Cod " + i + "Mg";
//             }
//             list.add(phrmacyInventoryPojo);
//         }
//         return list;
//     }*/
//    public void timeTablePopup(Activity activity, Medicine item, MedicinePrescriptionReqVo medicinePrescriptionReqVo, boolean isEdit, int position) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
//        LayoutInflater inflater = LayoutInflater.from(activity);
//        View dialogView = inflater.inflate(R.layout.time_table_popup, null);
//        KeyboardView keyboardView = dialogView.findViewById(R.id.keyboard_view);
//        EditText hours_field = dialogView.findViewById(R.id.hours_field);
//        EditText days_field = dialogView.findViewById(R.id.password_field);
//        TextView afterFood = dialogView.findViewById(R.id.tv_af);
//        TextView beforeFood = dialogView.findViewById(R.id.tv_bf);
//        TextView oneFour = dialogView.findViewById(R.id.one_four);
//        TextView oneTwo = dialogView.findViewById(R.id.one_two);
//        TextView one = dialogView.findViewById(R.id.one);
//        TextView remarks_field = dialogView.findViewById(R.id.remarks_field);
//
//        TextView ok = dialogView.findViewById(R.id.tv_ok);
//        TextView tv_medicine_name = dialogView.findViewById(R.id.tv_medicine_name);
//        CheckBox checkbox_morning = dialogView.findViewById(R.id.checkbox_morning);
//        CheckBox checkbox_afternoon = dialogView.findViewById(R.id.checkbox_afternoon);
//        CheckBox checkbox_night = dialogView.findViewById(R.id.checkbox_night);
////        dialogView.findViewById(R.id.caddress);
//        //  TextView exit = dialogView.findViewById(R.id.exit);
//        dialogBuilder.setView(dialogView);
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//
//        if (item != null) {
//            tv_medicine_name.setText(item.tradeName);
//        }
//        if (medicinePrescriptionReqVo != null) {
//            tv_medicine_name.setText(medicinePrescriptionReqVo.medicineName);
//            remarks_field.setText(medicinePrescriptionReqVo.remarks);
//            if (medicinePrescriptionReqVo.prefferedIntake != null && medicinePrescriptionReqVo.prefferedIntake.equalsIgnoreCase("AF")) {
//                afterFood.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                beforeFood.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                afterOrAfter = "AF";
//            } else {
//                beforeFood.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                afterFood.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                afterOrAfter = "BF";
//            }
//
//            if (medicinePrescriptionReqVo.drugDose != null && medicinePrescriptionReqVo.drugDose.equalsIgnoreCase("1/4")) {
//                oneFour.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                oneTwo.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                one.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                intakeQty = "1/4";
//            } else if (medicinePrescriptionReqVo.drugDose != null && medicinePrescriptionReqVo.drugDose.equalsIgnoreCase("1/2")) {
//                oneFour.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                oneTwo.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                one.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                intakeQty = "1/2";
//            } else if (medicinePrescriptionReqVo.drugDose != null && medicinePrescriptionReqVo.drugDose.equalsIgnoreCase("1")) {
//                oneFour.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                oneTwo.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                one.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                intakeQty = "1";
//            }
//            if (Common.isNumeric(String.valueOf(medicinePrescriptionReqVo.doseCourse))) {
//                days_field.setText(String.valueOf(medicinePrescriptionReqVo.doseCourse));
//            }
//            try {
//
//                if (Common.isNumeric(medicinePrescriptionReqVo.dayDosage)) {
//                    hours_field.setText(medicinePrescriptionReqVo.dayDosage);
//                } else {
////                        String[] doseArray = medicinePrescriptionReqVo.daySchedule.split(",");
////                        Log.d("dose: ", medicinePrescriptionReqVo.daySchedule);
////                        Log.d("dose: ", doseArray.toString());
//                    String daySchedule = medicinePrescriptionReqVo.daySchedule;
//
//                    if (daySchedule != null) {
//                        String[] schedule = daySchedule.split(",");
//                        for (int i = 0; i < schedule.length; i++) {
//                            if (schedule[i].contains("M")) {
//                                checkbox_morning.setChecked(true);
//                            }
//                            if (schedule[i].contains("A")) {
//                                checkbox_afternoon.setChecked(true);
//                            }
//                            if (schedule[i].contains("N")) {
//                                checkbox_night.setChecked(true);
//                            }
//                        }
//
//                    }
//
//                }
//            } catch (Exception e) {
//                Common.displayException(e, null);
//            }
//
//        } else {
//            afterFood.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//            beforeFood.setBackgroundColor(activity.getResources().getColor(R.color.white));
//            afterOrAfter = "AF";
//        }
//        alertDialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Common.hideSoftKeyboardFromDialog(alertDialog, activity);
//            }
//        }, 200);
//
//        hours_field.requestFocus();
//        hours_field.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d("Touch", "hours_field..");
//                hours_field.requestFocus();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    hours_field.setShowSoftInputOnFocus(false);
//                } else {
//                    Common.hideSoftKeyboardFromDialog(alertDialog, activity);
//                }
//                return true;
//            }
//        });
//        days_field.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d("Touch", "days_field..");
//                days_field.requestFocus();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    days_field.setShowSoftInputOnFocus(false);
//                } else {
//                    Common.hideSoftKeyboardFromDialog(alertDialog, activity);
//                }
//                return true;
//            }
//        });
//        checkbox_morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                morning = b ? 1 : 0;
//                enableEditField(hours_field);
//            }
//        });
//        checkbox_afternoon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                afternoon = b ? 1 : 0;
//                enableEditField(hours_field);
//
//            }
//        });
//        checkbox_night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                night = b ? 1 : 0;
//                enableEditField(hours_field);
//            }
//        });
//        ok.setOnClickListener(new View.OnClickListener() {
//            public ArrayList listStr = null;
//
//            @Override
//            public void onClick(View view) {
//                try {
//                    if (afterOrAfter == null) {
//                        Toast.makeText(activity, "Please select after/before food", Toast.LENGTH_SHORT).show();
//                        return;
//                    } else if (days_field == null || days_field.length() < 0) {
//                        Toast.makeText(activity, "Please enter number of days", Toast.LENGTH_SHORT).show();
//                        return;
//                    } else if (hours_field == null || hours_field.length() < 0) {
//                        Toast.makeText(activity, "Please enter number of hours", Toast.LENGTH_SHORT).show();
//                        return;
//                    } else if (intakeQty == null) {
//                        Toast.makeText(activity, "Please Select Intake Quantity", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    numberOfDays = days_field.getText().toString().trim();
//                    hours = hours_field.getText().toString().trim();
//                    morning = checkbox_morning.isChecked() ? 1 : 0;
//                    afternoon = checkbox_afternoon.isChecked() ? 1 : 0;
//                    night = checkbox_night.isChecked() ? 1 : 0;
//                    remarks = remarks_field.getText().toString().trim();
//
//
//                    String dozeInADay = new String();
//                    int qty = 0;
//                    MedicinePrescriptionReqVo medicinePrescriptionReqVo1 = new MedicinePrescriptionReqVo();
//                    if (item != null) {
//                        medicinePrescriptionReqVo1.drugId = item.drugId;
//                        medicinePrescriptionReqVo1.medicineName = item.tradeName;
//                        medicinePrescriptionReqVo1.composition = item.composition;
//                    }
//                    medicinePrescriptionReqVo1.prefferedIntake = afterOrAfter;
//                    medicinePrescriptionReqVo1.drugDose = intakeQty;
//                    medicinePrescriptionReqVo1.remarks = remarks;
//                    if (morning == 1 || afternoon == 1 || night == 1) {
//                        // dozeInADay = morning + "  -  " + afternoon + "  -  " + night;
//                        listStr = new ArrayList<>();
//                        if (morning == 1) {
//                            //  dozeInADay = dozeInADay + "M";
//                            listStr.add("M");
//                            qty = qty + 1;
//                        }
//                        if (afternoon == 1) {
//                            //  dozeInADay = dozeInADay + "A";
//                            listStr.add("A");
//                            qty = qty + 1;
//                        }
//                        if (night == 1) {
//                            // dozeInADay = dozeInADay + "N";
//                            listStr.add("N");
//                            qty = qty + 1;
//                        }
//                    } else {
////                        dozeInADay = hours;
//                        qty = Common.isNumeric(hours) ? Integer.parseInt(hours) : 0;
//                    }
//                    if (listStr != null && listStr.size() > 0) {
//                        StringBuilder sb = new StringBuilder();
//                        for (int i = 0; i < listStr.size(); i++) {
//                            if (i > 0) {
//                                sb.append(",");
//                            }
//                            sb.append(listStr.get(i));
//                        }
//                        medicinePrescriptionReqVo1.daySchedule = sb.toString();
//                    } else {
//                        medicinePrescriptionReqVo1.dayDosage = hours;
//                    }
//                    medicinePrescriptionReqVo1.doseCourse = numberOfDays;
//                    medicinePrescriptionReqVo1.quantity = qty * Integer.parseInt(numberOfDays);
//                    if (!isEdit)
//                        medicinePrescriptionReqVoList.add(medicinePrescriptionReqVo1);
//                    else if (position != -1) {
//                        medicinePrescriptionReqVo1.drugId = medicinePrescriptionReqVo.drugId;
//                        medicinePrescriptionReqVo1.medicineName = medicinePrescriptionReqVo.medicineName;
//
//                        medicinePrescriptionReqVoList.set(position, medicinePrescriptionReqVo1);
//                    }
//                    Common.dismissAlertDialog(alertDialog);
//                    auto_tv.setText("");
//                    saveDataForInstance();
//                    setListOnAdapter(activity, medicinePrescriptionReqVoList, recyclerview);
//                    auto_tv.dismissDropDown();
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//            }
//
//
//        });
//        afterFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                afterFood.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                beforeFood.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                afterOrAfter = "AF";
//            }
//        });
//        oneFour.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                oneFour.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                oneTwo.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                one.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                intakeQty = "1/4";
//            }
//        });
//        oneTwo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                oneFour.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                oneTwo.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                one.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                intakeQty = "1/2";
//            }
//        });
//        one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                oneFour.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                oneTwo.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                one.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                intakeQty = "1";
//            }
//        });
//        beforeFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                beforeFood.setBackgroundColor(activity.getResources().getColor(R.color.orange));
//                afterFood.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                afterOrAfter = "BF";
//            }
//        });
//        keyboardView.setOnItemClickListener(new KeyboardView.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, View viewItem, int position) {
//                Log.d("keyboard: ", v.toString());
//                if (days_field.isFocused()) {
//                    Log.d("keyboard: ", " " + days_field.isFocused() + "\n" + hours_field.isFocused());
//                    // handle number button click
//                    if (v.getTag() != null && "number_button".equals(v.getTag())) {
//                        days_field.append(((TextView) v).getText());
//                        return;
//                    }
//                    switch (v.getId()) {
//                        case R.id.t9_key_clear: { // handle clear button
//                            days_field.setText(null);
//                        }
//                        break;
//                        case R.id.t9_key_backspace: { // handle backspace button
//                            // delete one character
//                            Editable editable = days_field.getText();
//                            int charCount = editable.length();
//                            if (charCount > 0) {
//                                editable.delete(charCount - 1, charCount);
//                            }
//                        }
//                        break;
//                    }
//                } else {
//                    Log.d("keyboard: ", " " + days_field.isFocused() + "\n" + hours_field.isFocused());
//
//                    // handle number button click
//                    if (v.getTag() != null && "number_button".equals(v.getTag())) {
//                        hours_field.append(((TextView) v).getText());
//                        return;
//                    }
//                    switch (v.getId()) {
//                        case R.id.t9_key_clear: { // handle clear button
//                            hours_field.setText(null);
//                        }
//                        break;
//                        case R.id.t9_key_backspace: { // handle backspace button
//                            // delete one character
//                            Editable editable = hours_field.getText();
//                            int charCount = editable.length();
//                            if (charCount > 0) {
//                                editable.delete(charCount - 1, charCount);
//                            }
//                        }
//                        break;
//                    }
//                }
//            }
//        });
//
//
//    }
//
//    private void enableEditField(EditText hours_field) {
//        if (morning == 1 || afternoon == 1 || night == 1) {
//            hours_field.setText("");
//            hours_field.setEnabled(false);
//        } else
//            hours_field.setEnabled(true);
//    }
//
//    private void saveDataForInstance() {
//        if (prescription == null)
//            prescription = new Prescription();
//        prescription.prescriptionItems = medicinePrescriptionReqVoList;
//        prescription.autoTextData = auto_tv.getText().toString().trim();
//        FragmentData.getFragmentData().setPrescription(prescription);
//    }
//   /* private void signaturePopup() {
//        // Dialog Function
//        dialog = new Dialog(getContext());
//        // Removing the features of Normal Dialogs
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_signature);
//        dialog.setCancelable(false);
//        dialog_action();
//
//    }*/
//
//    /*private void dialog_action() {
//        //mContent = dialog.findViewById(R.id.linearLayout);
//        mSignature = new PictureEditActivity.signature(getContext(), null);
//        mSignature.setBackground(activity.getResources().getDrawable(R.drawable.human));
//        // Dynamically generating Layout through java code
//        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        //mGetSign = dialog.findViewById(R.id.getsign);
//        //TextView titleText = dialog.findViewById(R.id.title_text);
//        // ImageView backButton = dialog.findViewById(R.id.back_button);
//        mGetSign.setEnabled(false);
////        mCancel = (Button) dialog.findViewById(R.id.cancel);
//        view = mContent;
//*//*
//
//        titleText.setText("Signature");
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               dialog.dismiss();
//            }
//        });
//*//*
//
//
//        mGetSign.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                Log.v("log_tag", "Panel Saved");
//                view.setDrawingCacheEnabled(true);
//                mSignature.save(view, StoredPath);
////                relativeLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                //  takeScreenshot();
//                //   dialog.dismiss();
//
//                // displayGoogleLocationSettingPage(NewTripFormActivity.this, REQUEST_CODE_END_TRIP);
//
//                Toast.makeText(activity, "Successfully Saved", Toast.LENGTH_SHORT).show();
//                // Calling the same class
//                // recreate();
//
//            }
//        });
//*//*
//        mCancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.v("log_tag", "Panel Canceled");
//                dialog.dismiss();
//                // Calling the same class
//                // recreate();
//            }
//        });*//*
////        dialog.show();
//
//    }*/
//
//    private void takeScreenshot() {
//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
//
//        try {
//            // image naming and path  to include sd card  appending name you choose for file
//            String mPath = DIRECTORY + "/" + now + ".jpg";
//
//            // create bitmap screen capture
//            View v1 = activity.getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);
//
//            File imageFile = new File(mPath);
//
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            openScreenshot(imageFile);
//        } catch (Throwable e) {
//            // Several error may come out with file handling or DOM
//            e.printStackTrace();
//        }
//    }
//
//    private void openScreenshot(File imageFile) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(imageFile);
//        intent.setDataAndType(uri, "image/*");
//        startActivity(intent);
//    }
//
//    @Override
//    public void onResponseSuccess(ApiResponseController objectResponse, ApiRequestController objectRequest, ProgressDialog progressDialog) {
//        try {
//            if (objectResponse.result != null)
//                Toast.makeText(activity, objectResponse.message, Toast.LENGTH_SHORT).show();
//            Common.dismissProgressDialog(progressDialog);
//        } catch (Exception e) {
//            Common.disPlayExpection(e, progressDialog);
//        }
//    }
//
//    class MyRunnable implements Runnable {
//        private String text;
//
//        public void setData(String text) {
//            this.text = text;
//        }
//
//        @Override
//        public void run() {
//            if (activity != null) {
//                String key = text + "%";
//                List<Medicine> lista = DatabaseHandler.getDatabase(activity).commonDao().getMedicinesonSearch(key);
//                Log.d("afterTextChanged", "list: " + lista.size());
//                saveDataForInstance();
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!onMedicineItemClick)
//                            autoCompleteAdapter(lista);
//                        else onMedicineItemClick = false;
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.context = context;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        this.activity = activity;
//    }
//}
///*
//public class signature extends View {
//
//    private static final float STROKE_WIDTH = 8f;
//    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
//    private Paint paint = new Paint();
//    private Path path = new Path();
//
//    private float lastTouchX;
//    private float lastTouchY;
//    private final RectF dirtyRect = new RectF();
//    Bitmap bitmap;
//    Button mClear, mCancel;
//    private File outputFileSinnature;
//
//    public signature(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        paint.setAntiAlias(true);
//        paint.setColor(Color.BLACK);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeJoin(Paint.Join.ROUND);
//        paint.setStrokeWidth(STROKE_WIDTH);
//    }
//
//    public void save(View v, String StoredPath) {
//        Log.v("log_tag", "Width: " + v.getWidth());
//        Log.v("log_tag", "Height: " + v.getHeight());
//        if (bitmap == null) {
//            bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.ARGB_8888);
//
//        }
//        Canvas canvas = new Canvas(bitmap);
//        try {
//            outputFileSinnature = new File(StoredPath);
//            // Output the file
//            FileOutputStream mFileOutStream = new FileOutputStream(outputFileSinnature);
//            v.draw(canvas);
//
//            // Convert the output file to Image such as .png
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
//            signatureText.setImageBitmap(bitmap);
//
//            mFileOutStream.flush();
//            mFileOutStream.close();
//
//            signatureText.setImageBitmap(bitmap);
//
//        } catch (Exception e) {
//            Log.v("log_tag", e.toString());
//        }
//
//    }
//
//    public void clear() {
//        path.reset();
//        invalidate();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        canvas.drawPath(path, paint);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float eventX = event.getX();
//        float eventY = event.getY();
//        mGetSign.setEnabled(true);
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                path.moveTo(eventX, eventY);
//                lastTouchX = eventX;
//                lastTouchY = eventY;
//                return true;
//
//            case MotionEvent.ACTION_MOVE:
//
//            case MotionEvent.ACTION_UP:
//
//                resetDirtyRect(eventX, eventY);
//                int historySize = event.getHistorySize();
//                for (int i = 0; i < historySize; i++) {
//                    float historicalX = event.getHistoricalX(i);
//                    float historicalY = event.getHistoricalY(i);
//                    expandDirtyRect(historicalX, historicalY);
//                    path.lineTo(historicalX, historicalY);
//                }
//                path.lineTo(eventX, eventY);
//                break;
//
//            default:
//                debug("Ignored touch event: " + event.toString());
//                return false;
//        }
//
//        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
//                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
//                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
//                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
//
//        lastTouchX = eventX;
//        lastTouchY = eventY;
//
//        return true;
//    }
//
//    private void debug(String string) {
//
//        Log.v("log_tag", string);
//
//    }
//
//    private void expandDirtyRect(float historicalX, float historicalY) {
//        if (historicalX < dirtyRect.left) {
//            dirtyRect.left = historicalX;
//        } else if (historicalX > dirtyRect.right) {
//            dirtyRect.right = historicalX;
//        }
//
//        if (historicalY < dirtyRect.top) {
//            dirtyRect.top = historicalY;
//        } else if (historicalY > dirtyRect.bottom) {
//            dirtyRect.bottom = historicalY;
//        }
//    }
//
//    private void resetDirtyRect(float eventX, float eventY) {
//        dirtyRect.left = Math.min(lastTouchX, eventX);
//        dirtyRect.right = Math.max(lastTouchX, eventX);
//        dirtyRect.top = Math.min(lastTouchY, eventY);
//        dirtyRect.bottom = Math.max(lastTouchY, eventY);
//    }
//}*/
