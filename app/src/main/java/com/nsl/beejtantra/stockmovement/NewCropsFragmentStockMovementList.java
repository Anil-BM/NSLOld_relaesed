package com.nsl.beejtantra.stockmovement;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.CustomGridtAdapterTenImg;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.OnAmountChangeListener;
import com.nsl.beejtantra.Products;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.adapters.StockPlacementPopupListAdapter;
import com.nsl.beejtantra.adapters.StockPlacementPopupListAdapter1;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.commonutils.DatabaseUtil;
import com.nsl.beejtantra.enums.RoleEnum;
import com.nsl.beejtantra.pojo.RetailerStockSupply;
import com.nsl.beejtantra.pojo.StockDispatch;
import com.nsl.beejtantra.pojo.StockDispatchLineItem;
import com.nsl.beejtantra.pojo.StockDispatchResVo;
import com.nsl.beejtantra.pojo.StockDispatchResp;
import com.nsl.beejtantra.pojo.StockMovementFirstListPojo;
import com.nsl.beejtantra.pojo.StockMovementRetailerDetails;
import com.nsl.beejtantra.pojo.StockMovementUnSynedPojo;
import com.nsl.beejtantra.pojo.StockPlacementPopupPojo;
import com.nsl.beejtantra.pojo.StockMovementDetailsPojo;
import com.nsl.beejtantra.pojo.StockMovementPoJo;
import com.nsl.beejtantra.retailers.AllRetailersActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SMD_DETAIL_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDERDETAILS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SMD;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCropsFragmentStockMovementList extends Fragment {

    private String userId;
    private String crop_id;
    int dd, mm, yy;
    String date;
    private int role;
    private String customer_id;
    private JSONArray mainArray;
    private ProgressDialog progressDialog;
    private String mo_id;

    public NewCropsFragmentStockMovementList() {
        // Required empty public constructor
    }

    ArrayList<CropsFragmentStockMovementActivity.Child> cList = new ArrayList<>();
    AlertDialog dialog;
    // Array of strings storing country names
    LocationManager locationManager;
    //private CustomListAdapter mWorkerListAdapter;
    private CustomGridtAdapterTenImg mGridadapter;
    String latitude, longitude, sel_cropid;
    AlertDialog alert;
    ProgressDialog pDialog;
    StringBuffer responseText, responseText1, responseText2, responseText3;
    Fragment fragment = null;
    String division_id, company_id, response, workerId, wname, subcategoryId;
    ProductsRecyclerAdapter adapter;
//    ArrayList<Boolean> checkedStatesType = new ArrayList<>();

    //ArrayList<States> stateList                          = new ArrayList<States>();
    ArrayList<String> selectedrtype = new ArrayList<String>();
    ArrayList<HashMap<String, String>> type = new ArrayList<HashMap<String, String>>();

    private static final String URL = "http://m3infotech.com/muhurtham/api/search";
    public static final String TAG = NewCropsFragmentStockMovementList.class.getSimpleName();
    int sucess, i = 0;
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    Button getquote, btn_submitproducts;
    FragmentManager fm;
    FragmentTransaction ft;

    ViewPager mViewPager;
    RecyclerView recyclerView;
    TextView empty_view;
    int groupPosition;
    int mPosition = 0;
    String team;
    OnAmountChangeListener onAmountChangeListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onAmountChangeListener = (OnAmountChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            onAmountChangeListener = (OnAmountChangeListener) a;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_d, container, false);
        View v = inflater.inflate(R.layout.fragment_newrequestlist, container, false);
        //  mCustomPagerAdapter = new CustomPagerAdapter(getActivity(), ImagesArray);

        // mViewPager = (ViewPager) v.findViewById(R.id.pager);
        //  mViewPager.setAdapter(mCustomPagerAdapter);
        db = new DatabaseHandler(getActivity());

        recyclerView = (RecyclerView) v.findViewById(R.id.listTypes);
        empty_view = (TextView) v.findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sel_cropid = getArguments().getString("id");
        company_id = getArguments().getString("company_id");
        division_id = getArguments().getString("division_id");
        crop_id = getArguments().getString("crop_id");
        groupPosition = getArguments().getInt("position");
        customer_id = getArguments().getString("customer_id");
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString(Constants.SharedPrefrancesKey.USER_ID, null);
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team = sharedpreferences.getString("team", "");
        mo_id = userId;
        if (role != Constants.Roles.ROLE_7) {
            mo_id = getArguments().getString("mo_id", "");
        }

        DatabaseUtil.copyDatabaseToExtStg(getContext());


        return v;
    }

    private void loadData() {
        progressDialog = Common.showProgressDialog(getActivity());
        if (Common.haveInternet(getActivity())) {
//            new Async_getAllStockMovement().execute();
            MyApplication.getInstance().getRetrofitAPI().getStockDispatch(team).enqueue(new Callback<StockDispatchResp>() {
                @Override
                public void onResponse(Call<StockDispatchResp> call, retrofit2.Response<StockDispatchResp> response) {
                    try {
                        StockDispatchResp resp = response.body();
                        if (resp != null && resp.status.equalsIgnoreCase("success")) {
                            StockDispatchResVo stockDispatchResVo = resp.result;
                            if (stockDispatchResVo != null) {
                                List<StockDispatch> stockDispatch = stockDispatchResVo.stockDispatch;
                                if (stockDispatch != null && stockDispatch.size() > 0) {
                                    for (int i = 0; i < stockDispatch.size(); i++) {
                                        db.insertStockDispatch(stockDispatch.get(i));
                                        List<StockDispatchLineItem> stockDispatchLineItems = stockDispatch.get(i).stockDispatchLineItems;
                                        List<RetailerStockSupply> retailerStockSupply = stockDispatch.get(i).retailerStockSupply;
                                        Common.insertDispatchLineItems(db, stockDispatchLineItems);
                                        Common.insertRetailerStockSupply(db, retailerStockSupply);
                                    }

                                }
                            }
                        }
                        new GetProducts().execute();
                    } catch (Exception e) {
                        Common.dismissProgressDialog(progressDialog);
                    }

                }

                @Override
                public void onFailure(Call<StockDispatchResp> call, Throwable t) {

                }
            });
        } else {
            new GetProducts().execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }

    public class GetProducts extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading  ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... arg0) {

            try {

                type.clear();
                List<Crops> cdcList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + selected_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + company_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_cropid;

                String selectQuery = "SELECT products.product_id,brand_name FROM " + TABLE_SERVICEORDERDETAILS + " left join " + TABLE_SERVICEORDER + " on service_order_details.service_relation_id=service_order.service_id left join products on products.product_id=service_order_details.product_id where user_id=" + mo_id + " and customer_id=" + customer_id + " and products.product_crop_id=" + crop_id + " and service_order.approval_status=1 group by (products.product_id)";

                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                Log.e("Products query ", selectQuery);
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Products products = new Products();
                        products.setProductMasterID(cursor.getString(0));
                        products.setProductName(cursor.getString(1));

                        HashMap<String, String> first = new HashMap<String, String>();
                        first.put("productId", cursor.getString(0));
                        first.put("productName", cursor.getString(1));
                        type.add(first);
                        System.out.println("*****@@@@@" + type.size() + ":" + cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Common.dismissProgressDialog(progressDialog);
//            ArrayList<StockMovementFirstListPojo> offlineStockPlacementList = db.getOfflineStockPlacementList(mo_id, company_id, division_id, customer_id);
            pDialog.dismiss();
            adapter = new ProductsRecyclerAdapter(type);
            if (type.size() == 0) {
                Log.e("No products", "no products");
                empty_view.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setAdapter(adapter);
            }
        }
    }

    public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {
        private ArrayList<SelectedCities> arlist_schemes;
        //ArrayList<String> adapter_schemes;

        ArrayList<StockMovementFirstListPojo> offlineStockPlacementList;
        ArrayList<HashMap<String, String>> type;

        private ArrayList filmArray;
        Products products = new Products();
        Context context;
        private int selctedSchemeId;
        private StockMovementFirstListPojo stockPlacement;

        // HashSet<String> selection;

        public ProductsRecyclerAdapter(/*ArrayList<StockMovementFirstListPojo> offlineStockPlacementList,*/ ArrayList<HashMap<String, String>> type) {
//            this.offlineStockPlacementList = offlineStockPlacementList;
            this.type = type;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_cottton_new, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {


            mPosition = position;

            // Log.d("result", stockMovementFirstListPojo.toString());
            selctedSchemeId = 0;
            final String productId = type.get(position).get("productId");

            holder.tvDealerNameCotton.setText(type.get(position).get("productName"));
            holder.stockPlaced.setText("0");
            holder.currentStock.setText("0");
            holder.pog.setText("0");
            stockPlacement = db.getOfflineStockPlacement(mo_id, company_id, division_id, customer_id, productId);

            if (role != Constants.Roles.ROLE_7) {
                mo_id = getArguments().getString("mo_id", "");

                if (!userId.equalsIgnoreCase(mo_id)) {
                    holder.stockPlaced.setEnabled(false);
                    holder.currentStock.setEnabled(false);

                }


            }


            if (stockPlacement != null) {
                int currentS = 0, stockP = 0, pogI = 0;
                if (stockPlacement.stockPlaced != null)
                    stockP = Integer.parseInt(stockPlacement.stockPlaced);
                if (stockPlacement.pog != null)
                    pogI = Integer.parseInt(stockPlacement.pog);
                currentS = stockP - pogI;
                holder.stockPlaced.setText(stockPlacement.stockPlaced);
                holder.currentStock.setText(String.valueOf(currentS));
                holder.pog.setText(stockPlacement.pog);


            } else {
                holder.stockPlaced.setText("0");
                holder.currentStock.setText("0");
                holder.pog.setText("0");
            }

            holder.stockPlaced.setOnClickListener(new View.OnClickListener() {
                public ArrayList<StockPlacementPopupPojo> stockPlacementPopupPojoArrayList;

                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.stock_placed_popup);

                    final TextView txt_date = (TextView) dialog.findViewById(R.id.datepicker);
                    final TextView crop_name = (TextView) dialog.findViewById(R.id.crop_name);
                    final EditText qnty_places = (EditText) dialog.findViewById(R.id.edittext_quantity_placed);
                    final ImageView ok = (ImageView) dialog.findViewById(R.id.buttton_stock_placed_ok);
                    Button close = (Button) dialog.findViewById(R.id.button_stock_placed_close);
                    final RecyclerView recyclerViewStockPlacement = (RecyclerView) dialog.findViewById(R.id.recycler_view);
//                    stockPlacementPopupPojoArrayList = db.getOfflineStockPlacementListById(db.getCombinationOf3(Integer.parseInt(mo_id), Integer.parseInt(company_id), Integer.parseInt(division_id), Integer.parseInt(customer_id)), Integer.parseInt(crop_id), Integer.parseInt(productId), Integer.parseInt(customer_id));
                    stockPlacement = db.getOfflineStockPlacement(mo_id, company_id, division_id, customer_id, productId);
                    List<StockDispatchLineItem> stockDispatchLineItems = db.getStockDispatchLineItems(stockPlacement.cropId, stockPlacement.productId);
                    final StockPlacementPopupListAdapter1 stockPlacementPopupListAdapter = setListOnAdapter(stockDispatchLineItems, recyclerViewStockPlacement);
                    crop_name.setText(holder.tvDealerNameCotton.getText().toString());
                    txt_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar mcurrentDate = Calendar.getInstance();
                            final int mYear = mcurrentDate.get(Calendar.YEAR);
                            final int mMonth = mcurrentDate.get(Calendar.MONTH);
                            final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                    // TODO Auto-generated method stub
                                    int selmon = selectedmonth + 1;
                                    txt_date.setText(selectedday + "-" + selmon + "-" + selectedyear);
                                }
                            }, mYear, mMonth, mDay);
                            mDatePicker.setTitle("Select date");
                            mDatePicker.show();

                        }
                    });

                    recyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.dismissDialog(dialog);

                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Handler handler = Common.disableClickEvent(ok, true);
                            boolean isInserted = saveStockMovementInDb(txt_date.getText().toString().trim(), qnty_places.getText().toString().trim(), "0", Integer.parseInt(productId));
                            if (isInserted) {
                                txt_date.setText("");
                                qnty_places.setText("");
                            }
                            ArrayList<StockMovementFirstListPojo> offlineStockPlacementList = db.getOfflineStockPlacementList(mo_id, company_id, division_id, customer_id);
                            adapter = new ProductsRecyclerAdapter(type);
                            recyclerView.setAdapter(adapter);


                            stockPlacementPopupPojoArrayList = db.getOfflineStockPlacementListById(db.getCombinationOf3(Integer.parseInt(mo_id), Integer.parseInt(company_id), Integer.parseInt(division_id), Integer.parseInt(customer_id)), Integer.parseInt(crop_id), Integer.parseInt(productId), Integer.parseInt(customer_id));
                            Log.d("stockPlacement:", stockPlacementPopupPojoArrayList.toString());
                            //  if (stockPlacementPopupListAdapter!=null){
                            // stockPlacementPopupListAdapter.notifyDataSetChanged();
//                            setListOnAdapter(stockPlacementPopupPojoArrayList, recyclerViewStockPlacement);
                            //   }
                            //  Common.dismissDialog(dialog);
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Common.dismissDialog(dialog);
                        }
                    });


                    dialog.setCancelable(false);
                    dialog.show();
                }
            });

            holder.currentStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog1 = new Dialog(getContext());
                    dialog1.setContentView(R.layout.current_stock_popup);
                    final TextView txt_date = (TextView) dialog1.findViewById(R.id.datepicker);
                    final TextView currentStock = (TextView) dialog1.findViewById(R.id.textview_currentstock_left_value);
                    final EditText qnty_places = (EditText) dialog1.findViewById(R.id.edittext_quantity_placed);
                    final ImageView ok = (ImageView) dialog1.findViewById(R.id.buttton_stock_placed_ok);
                    Button close = (Button) dialog1.findViewById(R.id.button_current_stock_close);
                    final TextView crop_name = (TextView) dialog1.findViewById(R.id.crop_name);

                    currentStock.setText(holder.currentStock.getText().toString());
                    crop_name.setText(holder.tvDealerNameCotton.getText().toString());
                    txt_date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar mcurrentDate = Calendar.getInstance();
                            final int mYear = mcurrentDate.get(Calendar.YEAR);
                            final int mMonth = mcurrentDate.get(Calendar.MONTH);
                            final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                    // TODO Auto-generated method stub
                                    int selmon = selectedmonth + 1;
                                    txt_date.setText(selectedday + "-" + selmon + "-" + selectedyear);
                                }
                            }, mYear, mMonth, mDay);
                            mDatePicker.setTitle("Select date");
                            mDatePicker.show();

                        }
                    });

                    qnty_places.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (editable.toString().length() > 0) {

                            }
                        }
                    });

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Handler handler = Common.disableClickEvent(ok, true);
                            if (qnty_places.getText().toString().trim().length() == 0) {
                                qnty_places.requestFocus();
                                qnty_places.setError("Please Enter Quantity");
                                return;
                            }
                            int qty = Integer.parseInt(qnty_places.getText().toString().trim());
                            if (qty > Integer.parseInt(holder.stockPlaced.getText().toString().trim())) {
                                qnty_places.requestFocus();
                                qnty_places.setError("Current Stock must be lower than or equal to Stock Placed");
                                return;
                            }

                            boolean isInserted = saveStockMovementInDb(txt_date.getText().toString().trim(), "0", qnty_places.getText().toString().trim(), Integer.parseInt(productId));
                            if (isInserted) {
                                currentStock.setText(qnty_places.getText().toString().trim());
                                txt_date.setText("");
                                qnty_places.setText("");
                            }
                            ArrayList<StockMovementFirstListPojo> offlineStockPlacementList = db.getOfflineStockPlacementList(mo_id, company_id, division_id, customer_id);
                            adapter = new ProductsRecyclerAdapter(type);
                            recyclerView.setAdapter(adapter);


                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Common.dismissDialog(dialog1);
                        }
                    });


                    dialog1.setCancelable(false);
//                    dialog1.show();
                }
            });

            holder.pog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*android.support.v7.app.AlertDialog.Builder builder_pog = new android.support.v7.app.AlertDialog.Builder(getContext());
                    LayoutInflater inflater2 = LayoutInflater.from(getContext());
                    View view_pog = inflater2.inflate(R.layout.pog_popup, null);

                    builder_pog.setView(view_pog);
                    builder_pog.create();
                    builder_pog.show();*/
                }
            });
            holder.retailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String productId = type.get(position).get("productId");

                    if (Common.stringToInt(holder.stockPlaced.getText().toString()) > 0) {
                        String stockMovementId = null;
                        String stockPlaced = null;

//                        for (int i = 0; i < offlineStockPlacementList.size(); i++) {
//                            if (productId.equalsIgnoreCase(String.valueOf(offlineStockPlacementList.get(i).productId))) {
//                                stockMovementId = offlineStockPlacementList.get(i).stockMovementId;
//                                stockPlaced = offlineStockPlacementList.get(i).stockPlaced;
//                            }
//
//                        }

                        //if (db.getRetailersCount()>0)
                        Intent feedback = new Intent(getContext(), AllRetailersActivity.class);
                        StockMovementFirstListPojo sp = db.getOfflineStockPlacement(mo_id, company_id, division_id, customer_id, productId);
                        feedback.putExtra("company_id", company_id);
                        feedback.putExtra("division_id", division_id);
                        feedback.putExtra("crop_id", crop_id);
                        feedback.putExtra("customer_id", customer_id);
                        feedback.putExtra("product_id", productId);
                        feedback.putExtra("product_name", holder.tvDealerNameCotton.getText().toString().trim());
                        feedback.putExtra("stock_placed", sp.stockPlaced);
                        feedback.putExtra("stock_movement_id", sp.stockMovementId);
                        feedback.putExtra("mo_id", mo_id);
                        startActivity(feedback);


                    } else {
                        Toast.makeText(getActivity(), "Please place stock", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return type.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvDealerNameCotton;
            public TextView stockPlaced;
            TextView currentStock;
            TextView pog;
            Button retailer;
            ImageView search;


            public ViewHolder(View itemView) {

                super(itemView);

                tvDealerNameCotton = (TextView) itemView.findViewById(R.id.tv_dealer_name_cotton);
                stockPlaced = (TextView) itemView.findViewById(R.id.TextView_stockplaced);
                currentStock = (TextView) itemView.findViewById(R.id.TextView_current_stock);
                pog = (TextView) itemView.findViewById(R.id.TextView_pog);
                retailer = (Button) itemView.findViewById(R.id.button_r);
                search = (ImageView) itemView.findViewById(R.id.iv_search);


            }
        }
        ////////////////////////////////////////////////////
        ///////////////////////////////////////////////////

        public ArrayList<String> getSelectedTests() {
            ArrayList<String> selected = new ArrayList<>();

            for (int i = 0; i < type.size(); i++) {
//                if (checkedStatesType.get(i)) {
//                    //checked at Ith position
//                    //add it to array
//                    selected.add(results.get(i).get("productId"));
//                }
            }
            return selected;
        }


    }

    private boolean saveStockMovementInDb(String date, String stockPlacedQuantity, String currentStockQuantity, int productId) {
        if (!date.equalsIgnoreCase("") && (!stockPlacedQuantity.equalsIgnoreCase("") || !currentStockQuantity.equalsIgnoreCase(""))) {

            progressDialog = Common.showProgressDialog(getActivity());


            StockMovementPoJo stockMovementPoJo = new StockMovementPoJo();
            stockMovementPoJo.ffmId = 0;
            stockMovementPoJo.movementType = 0;
            stockMovementPoJo.status = 0;
            stockMovementPoJo.divisionId = Integer.parseInt(division_id);
            stockMovementPoJo.companyId = Integer.parseInt(company_id);
            stockMovementPoJo.customerId = Integer.parseInt(customer_id);
            stockMovementPoJo.updatedDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementPoJo.createdDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementPoJo.createdBy = mo_id;
            stockMovementPoJo.createdBy = mo_id;
            stockMovementPoJo.userId = Integer.parseInt(mo_id);


            StockMovementDetailsPojo stockMovementDetailsPojo = new StockMovementDetailsPojo();
            stockMovementDetailsPojo.ffmId = 0;
            stockMovementDetailsPojo.cropId = Integer.parseInt(crop_id);
            stockMovementDetailsPojo.customerId = Integer.parseInt(customer_id);
            stockMovementDetailsPojo.stockPlaced = stockPlacedQuantity;
            stockMovementDetailsPojo.currentStock = currentStockQuantity;
            stockMovementDetailsPojo.updatedDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementDetailsPojo.createdDatetime = String.valueOf(System.currentTimeMillis());
            stockMovementDetailsPojo.createdBy = mo_id;
            stockMovementDetailsPojo.createdBy = mo_id;
            stockMovementDetailsPojo.userType = RoleEnum.getRoleTitle(role).getTitle();
            stockMovementDetailsPojo.placedDate = date;
            stockMovementDetailsPojo.productId = productId;

            Log.d("Customer_id", customer_id + "\n" + stockMovementPoJo.customerId + "\n" + stockMovementDetailsPojo.customerId);
            db.insertStackMovement(stockMovementPoJo, stockMovementDetailsPojo);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (Common.haveInternet(getContext())) {
                prepareStockMovementDataAndPush();
            } else {
                Common.dismissProgressDialog(progressDialog);
            }
            return true;

            // DatabaseUtil.copyDatabaseToExtStg(getContext());
        } else {
            Toast.makeText(getActivity(), "Please date and quntity", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    private void prepareStockMovementDataAndPush() {
        mainArray = new JSONArray();
        JSONArray adbBookArray = new JSONArray();
        JSONObject naimObject = null;

        ArrayList<StockMovementUnSynedPojo> stockMovementUnSynedPojoList = db.getOfflineStockPlacementListUnSyncData1();
        for (StockMovementUnSynedPojo stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

            ArrayList<StockMovementUnSynedPojo> stockMovementUnSynedPojos = db.getOfflineStockPlacementListUnSyncData(stockMovementUnSynedPojo.stockMovementId);
            for (StockMovementUnSynedPojo stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                JSONObject advBookObj = new JSONObject();

                try {
                    advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                    advBookObj.put("customer_id", stockMovementUnSynedPojo1.customerId);
                    advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                    advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                    advBookObj.put("id", stockMovementUnSynedPojo1.stockMovementId);
                    advBookObj.put("user_id", mo_id);
                    advBookObj.put("placed_date", stockMovementUnSynedPojo1.placedDate);
                    advBookObj.put("movement_type", "0");


                    JSONArray cropArray = new JSONArray();

                    JSONArray productArray = new JSONArray();


                    JSONObject object_one = new JSONObject();
                    object_one.put("stock_placed", stockMovementUnSynedPojo1.stockPlaced);
                    object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                    object_one.put("current_stock", stockMovementUnSynedPojo1.currentStock);
                    object_one.put("pog", stockMovementUnSynedPojo1.pog);
                    object_one.put("sqlite_id_detail", stockMovementUnSynedPojo1.stockMovementDetailId);
                    productArray.put(object_one);


                    JSONObject cropObj = new JSONObject();
                    cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                    cropArray.put(cropObj);
                    advBookObj.put("Crops", cropArray);
                    cropObj.put("Products", productArray);
                    //}
                    naimObject = new JSONObject();
                    adbBookArray.put(advBookObj);
                    naimObject.put("stockmovement", adbBookArray);


                } catch (JSONException e) {
                }

            }
        }
        mainArray.put(naimObject);
        Log.i("  -json array -", "" + mainArray.toString());
        if (mainArray.length() > 0) {
            new callAPIToPushStockmovementData().execute();
        }
    }
/*
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, mainArray.toString());
        Request request = new Request.Builder()
                .url(Constants.URL_INSERT_STOCKMOVEMENT)
                .post(body)
                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();


            String jsonData = response.body().string();
            System.out.println("!!!!!!!1Sales Oreder" + jsonData);

            if (jsonData != null) {
                JSONArray jsonArray;

                jsonArray = new JSONArray(jsonData);

                for (int i = 0; i < jsonArray.length(); i++)

                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String status = jsonObject.getString("status");*/
/*
                    if (status.equalsIgnoreCase("success")) {
                        //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                        String service_id = jsonObject.getString("service_id");
                        String ffmid = jsonObject.getString("ffm_id");

                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite id", service_id);
                        Log.e("ffmid", ffmid);
                        sdbw = db.getWritableDatabase();
                        // updateFeedback(Feedback feedback);
                        String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                        sdbw.execSQL(updatequery);

                        JSONArray detailsArray = jsonObject.getJSONArray("details");
                        for (int k = 0; k < detailsArray.length(); k++) {
                            String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                            String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");

                            String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " = " + order_detail_id;
                            sdbw.execSQL(updatequery1);
                        }


                    }
*/


//                }
//
//
//            }

/*

        try {
            ArrayList<StockMovementPoJo> serviceOrderMasterList = db.getOfflineStockPlacementUnSynced(0);

            for (int i = 0; i < serviceOrderMasterList.size(); i++) {
                ServiceOrderMaster serviceOrderMaster = serviceOrderMasterList.get(i);
                JSONObject advBookObj = new JSONObject();

                advBookObj.put("CompanyID", serviceOrderMasterList.get(i)._serviceorder_company_id);
                advBookObj.put("customer_id", serviceOrderMasterList.get(i)._serviceorder_customer_id);
                advBookObj.put("Tokenamount", serviceOrderMasterList.get(i)._token_amount);

                advBookObj.put("DivisionID", serviceOrderMasterList.get(i)._serviceorder_division_id);


                advBookObj.put("id", serviceOrderMasterList.get(i)._serviceorder_masterid);
                advBookObj.put("user_id", sharedpreferences.getString("userId", null));

                JSONArray cropArray = new JSONArray();

                JSONArray productArray = new JSONArray();
                for (int l = 0; l < serviceOrderMaster.getServiceOrderDetailMasterList().size(); l++) {
                    ServiceOrderDetailMaster serviceOrderDetailMaster = serviceOrderMaster.getServiceOrderDetailMasterList().get(l);
                    JSONObject object_one = new JSONObject();
                    object_one.put("advance_amount", serviceOrderDetailMaster._serviceorderdetail_advance_amount);
                    object_one.put("ProductID", serviceOrderDetailMaster._serviceorderdetail_product_id);
                    object_one.put("Qunatity", serviceOrderDetailMaster._serviceorderdetail_quantity);
                    object_one.put("scheme_id", serviceOrderDetailMaster._serviceorderdetail_scheme_id);
                    object_one.put("mobile_serivce_details_id", serviceOrderDetailMaster._serviceorderdetail_masterid);
                    productArray.put(object_one);

                }
                JSONObject cropObj = new JSONObject();
                cropObj.put("CropId", serviceOrderMaster.getServiceOrderDetailMasterList().get(0)._serviceorderdetail_crop_id);
                cropObj.put("scheme_id", "");

                cropArray.put(cropObj);
                advBookObj.put("Crops", cropArray);
                cropObj.put("Products", productArray);
                //}
                naimObject = new JSONObject();
                adbBookArray.put(advBookObj);
                naimObject.put("AdvanceBookings", adbBookArray);

            }
            mainArray.put(naimObject);
            Log.i("  -json array -", "" + mainArray.toString());

*/

    // if (serviceOrderMasterList.size() > 0) {

/*
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_INSERT_ADVANCEBOOKING)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);

                if (jsonData != null) {
                    JSONArray jsonArray;

                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                            String service_id = jsonObject.getString("service_id");
                            String ffmid = jsonObject.getString("ffm_id");

                            //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                            Log.e("sqlite id", service_id);
                            Log.e("ffmid", ffmid);
                            sdbw = db.getWritableDatabase();
                            // updateFeedback(Feedback feedback);
                            String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                            sdbw.execSQL(updatequery);

                            JSONArray detailsArray = jsonObject.getJSONArray("details");
                            for (int k = 0; k < detailsArray.length(); k++) {
                                String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                                String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");

                                String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ID + " = " + order_detail_id;
                                sdbw.execSQL(updatequery1);
                            }


                        }


                    }


                }*/

      /*      }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }*/


       /* } catch (Exception e) {
            e.printStackTrace();
        }*/
    // }

    public StockPlacementPopupListAdapter1 setListOnAdapter(final List<StockDispatchLineItem> stockDispatchLineItems, RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        StockPlacementPopupListAdapter1 stockPlacementPopupListAdapter = null;

        if (stockDispatchLineItems.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //recordnotfnd.setVisibility(View.GONE);
            stockPlacementPopupListAdapter = new StockPlacementPopupListAdapter1(getContext(), stockDispatchLineItems);
            recyclerView.setAdapter(stockPlacementPopupListAdapter);

            stockPlacementPopupListAdapter.notifyDataSetChanged();

        } else if (stockDispatchLineItems == null || stockDispatchLineItems.size() == 0) {

            // recordnotfnd.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        return stockPlacementPopupListAdapter;
    }

    private class callAPIToPushStockmovementData extends AsyncTask<Void, Void, String> {
        private String jsonData;

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, mainArray.toString());
            Request request = new Request.Builder()
                    .url(Constants.URL_INSERT_STOCKMOVEMENT)
                    .post(body)
                    .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("sqlite_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_SMD + " SET " + db.FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SMD_DETAIL_ID + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            Common.dismissProgressDialog(progressDialog);

        }
    }


    public class Async_getAllStockMovement extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.GET_STOCKMOVEMENT_LIST + team)
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (jsonData != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonData);
                        if (!jsonObject.has("error")) {
                            db.deleteDataByTableName(new String[]{"stock_movement", "stock_movement_detail", "stock_movement_retailer_details"});

                            JSONArray retailer_detailsArray = jsonObject.getJSONArray("stock_movement_data");

                            for (int j = 0; j < retailer_detailsArray.length(); j++) {
                                JSONObject retailer_detailsObject = retailer_detailsArray.getJSONObject(j);
                                JSONObject stock_movement_object = retailer_detailsObject.getJSONObject("stock_movement");

                                StockMovementPoJo stockMovementPoJo = new StockMovementPoJo();
                                stockMovementPoJo.ffmId = stock_movement_object.isNull("stock_movement_id") ? 0 : stock_movement_object.getInt("stock_movement_id");
                                stockMovementPoJo.movementType = stock_movement_object.isNull("movement_type") ? 0 : stock_movement_object.getInt("movement_type");
                                stockMovementPoJo.status = stock_movement_object.isNull("status") ? 0 : stock_movement_object.getInt("status");
                                stockMovementPoJo.divisionId = stock_movement_object.isNull("division_id") ? 0 : stock_movement_object.getInt("division_id");
                                stockMovementPoJo.companyId = stock_movement_object.isNull("company_id") ? 0 : stock_movement_object.getInt("company_id");
                                stockMovementPoJo.customerId = stock_movement_object.isNull("customer_id") ? 0 : stock_movement_object.getInt("customer_id");
                                stockMovementPoJo.updatedDatetime = stock_movement_object.getString("updated_datetime");
                                stockMovementPoJo.createdDatetime = stock_movement_object.getString("created_datetime");
                                stockMovementPoJo.createdBy = stock_movement_object.getString("created_by");
                                stockMovementPoJo.updatedBy = stock_movement_object.getString("updated_by");
                                stockMovementPoJo.userId = stock_movement_object.isNull("user_id") ? 0 : stock_movement_object.getInt("user_id");

                                if (retailer_detailsObject.has("stock_movement_details")) {
                                    JSONArray stock_movementdetails_array = retailer_detailsObject.getJSONArray("stock_movement_details");

                                    for (int k = 0; k < stock_movementdetails_array.length(); k++) {
                                        JSONObject jsonObjectDeatils = stock_movementdetails_array.getJSONObject(k);


                                        StockMovementDetailsPojo stockMovementDetailsPojo = new StockMovementDetailsPojo();
                                        stockMovementDetailsPojo.ffmId = jsonObjectDeatils.getInt("stock_movement_detail_id");
                                        stockMovementDetailsPojo.cropId = jsonObjectDeatils.getInt("crop_id");
                                        stockMovementDetailsPojo.customerId = jsonObjectDeatils.getInt("customer_id");
                                        stockMovementDetailsPojo.stockPlaced = jsonObjectDeatils.getString("stock_placed");
                                        stockMovementDetailsPojo.currentStock = jsonObjectDeatils.getString("current_stock");
                                        stockMovementDetailsPojo.updatedDatetime = jsonObjectDeatils.getString("updated_datetime");
                                        stockMovementDetailsPojo.createdDatetime = jsonObjectDeatils.getString("created_datetime");
                                        stockMovementDetailsPojo.createdBy = jsonObjectDeatils.getString("created_by");
                                        stockMovementDetailsPojo.updatedBy = jsonObjectDeatils.getString("updated_by");
                                        stockMovementDetailsPojo.userType = jsonObjectDeatils.getString("user_type");
                                        stockMovementDetailsPojo.placedDate = jsonObjectDeatils.getString("placed_date");
                                        stockMovementDetailsPojo.productId = jsonObjectDeatils.getInt("product_id");
                                        stockMovementDetailsPojo.stockMovementId = jsonObjectDeatils.getInt("stock_movement_id");
                                        stockMovementDetailsPojo.orderSapId = jsonObjectDeatils.getString("order_sap_id");


                                        db.insertStackMovement(stockMovementPoJo, stockMovementDetailsPojo);

                                    }
                                }
                                if (retailer_detailsObject.has("stock_movement_retailer_details")) {
                                    JSONArray stock_movementRetailerdetails_array = retailer_detailsObject.getJSONArray("stock_movement_retailer_details");
                                    for (int l = 0; l < stock_movementRetailerdetails_array.length(); l++) {
                                        JSONObject jsonObjectDeatils = stock_movementRetailerdetails_array.getJSONObject(l);


                                        StockMovementRetailerDetails stockMovementDetailsPojo = new StockMovementRetailerDetails();
                                        stockMovementDetailsPojo.cropId = jsonObjectDeatils.getString("crop_id");
                                        stockMovementDetailsPojo.ffmId = jsonObjectDeatils.getInt("stock_movement_retailer_id");
                                        stockMovementDetailsPojo.stockPlaced = jsonObjectDeatils.getString("stock_placed");
                                        stockMovementDetailsPojo.currentStock = jsonObjectDeatils.getString("current_stock");
                                        stockMovementDetailsPojo.updatedDatetime = jsonObjectDeatils.getString("updated_datetime");
                                        stockMovementDetailsPojo.createdDatetime = jsonObjectDeatils.getString("created_datetime");
                                        stockMovementDetailsPojo.createdBy = jsonObjectDeatils.getString("created_by");
                                        stockMovementDetailsPojo.userType = jsonObjectDeatils.getString("user_type");
                                        stockMovementDetailsPojo.placedDate = jsonObjectDeatils.getString("placed_date");
                                        stockMovementDetailsPojo.productId = jsonObjectDeatils.getString("product_id");
                                        stockMovementDetailsPojo.stockMovementId = jsonObjectDeatils.getString("stock_movement_id");
                                        stockMovementDetailsPojo.retailerId = jsonObjectDeatils.getString("retailer_id");
                                        stockMovementDetailsPojo.verified = jsonObjectDeatils.getString("verified");
                                        stockMovementDetailsPojo.verifiedBy = jsonObjectDeatils.getString("verified_by");

                                        db.insertStackMovementForRetailerDetails(stockMovementPoJo, stockMovementDetailsPojo);


                                    }

                                }

                            }


                        }


                        // dbWritable.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    // db.deleteComplaints();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new GetProducts().execute();

        }

    }
}










