package com.nsl.beejtantra;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.nsl.beejtantra.FarmerCoupans.Getcoupans;
import com.nsl.beejtantra.FarmerCoupans.ModelforCoupansList;
import com.nsl.beejtantra.TFA.ActivityPlanner;
import com.nsl.beejtantra.TFA.Approvaldetails;
import com.nsl.beejtantra.TFA.Demandgeneation;
import com.nsl.beejtantra.TFA.Demandgeneration_add;
import com.nsl.beejtantra.TFA.Village_list;
import com.nsl.beejtantra.TFA.support.ActivityPlanner2;
import com.nsl.beejtantra.TFA.support.tfaactivitylist;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.complaints.Complaints;
import com.nsl.beejtantra.dailydairy.DailyDairy;
import com.nsl.beejtantra.feedback.Feedback;
import com.nsl.beejtantra.fieldestimation.YieldReqVo;
import com.nsl.beejtantra.marketintelligence.Commodity_Price;
import com.nsl.beejtantra.marketintelligence.Crop_Shifts;
import com.nsl.beejtantra.marketintelligence.Price_Survey;
import com.nsl.beejtantra.marketintelligence.Product_Survey;
import com.nsl.beejtantra.marketintelligencenew.CompetitorChannel;
import com.nsl.beejtantra.marketintelligencenew.MarketPotential;
import com.nsl.beejtantra.pojo.BankDetailsListPojo;
import com.nsl.beejtantra.pojo.ChannelPreference;
import com.nsl.beejtantra.pojo.CommodityPrice;
import com.nsl.beejtantra.pojo.CompetitorStrength;
import com.nsl.beejtantra.pojo.CropShift;
import com.nsl.beejtantra.pojo.DetailObject;
import com.nsl.beejtantra.pojo.DistributorsRetailerPojo;
import com.nsl.beejtantra.pojo.District;
import com.nsl.beejtantra.pojo.FarmerPojo;
import com.nsl.beejtantra.pojo.Godown;
import com.nsl.beejtantra.pojo.GodownStock;
import com.nsl.beejtantra.pojo.GradePojo;
import com.nsl.beejtantra.pojo.InsertedRetailer;
import com.nsl.beejtantra.pojo.MappedRetailerPojo;
import com.nsl.beejtantra.pojo.MappedRetailerWithDistributorPojo;
import com.nsl.beejtantra.pojo.ProductPricingSurvey;
import com.nsl.beejtantra.pojo.RetailerStockSupply;
import com.nsl.beejtantra.pojo.RetailersNamePojo;
import com.nsl.beejtantra.pojo.Season;
import com.nsl.beejtantra.pojo.SeasonLineItem;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;
import com.nsl.beejtantra.pojo.StockDispatch;
import com.nsl.beejtantra.pojo.StockDispatchLineItem;
import com.nsl.beejtantra.pojo.StockMovementDetailsPojo;
import com.nsl.beejtantra.pojo.StockMovementFirstListPojo;
import com.nsl.beejtantra.pojo.StockMovementPoJo;
import com.nsl.beejtantra.pojo.StockMovementRetailerDetails;
import com.nsl.beejtantra.pojo.StockMovementUnSynedPojo;
import com.nsl.beejtantra.pojo.StockPlacementPopupPojo;
import com.nsl.beejtantra.pojo.StockReturnDetailsPoJo;
import com.nsl.beejtantra.pojo.StockReturnPoJo;
import com.nsl.beejtantra.pojo.StockReturnUnSynedPojo;
import com.nsl.beejtantra.pojo.UserRegions;
import com.nsl.beejtantra.pojo.VersionControlVo;
import com.nsl.beejtantra.product_catalogue.CatalogueCropsPojo;
import com.nsl.beejtantra.product_catalogue.CatalogueCropsProductsPojo;
import com.nsl.beejtantra.retailers.Retailer;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 59;

    // Database Name
    private static final String DATABASE_NAME = "NSL.db";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    // Table Names
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_COMPANIES = "companies";
    public static final String TABLE_COMPANY_DIVISION_CROPS = "company_division_crop";
    public static final String TABLE_USER_COMPANY_CUSTOMER = "user_company_customer";
    public static final String TABLE_SCHEME_PRODUCTS = "scheme_products";
    public static final String TABLE_USER_COMPANY_DIVISION = "user_company_division";
    public static final String TABLE_CROPS = "crops";
    public static final String TABLE_SCHEMES = "schemes";
    public static final String TABLE_COMPLAINT = "complaints";
    public static final String TABLE_COMPLAINT_NEW = "complaints_new";
    public static final String TABLE_DAILYDAIRY = "dailydairy";
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_CUSTOMER_DETAILS = "customer_details";
    public static final String TABLE_DISTRIBUTOR_RETAILER = "distributor_retailer";
    public static final String TABLE_DIVISION = "division";
    public static final String TABLE_ERRORS = "errors";
    public static final String TABLE_FARMERS = "farmers";
    public static final String TABLE_GEO_TRACKING = "geo_tracking";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_USER_REGIONS = "user_regions";
    public static final String TABLE_UCDD = "user_company_division_distributor";
    public static final String TABLE_SMD = "stock_movement_detail";
    public static final String TABLE_SM = "stock_movement";
    public static final String TABLE_STOCK_MOVEMENT_RETAILER_DETAILS = "stock_movement_retailer_details";
    public static final String TABLE_EVENT_MANAGMENT = "event_management";
    public static final String TABLE_EMPLOYEE_VISIT_MANAGEMENT = "employee_visit_management";
    public static final String TABLE_PAYMENT_COLLECTION = "payment_collection";
    public static final String TABLE_PLANTS = "plants";
    public static final String TABLE_REGION = "region";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_PRODUCT_PRICE = "product_price";
    public static final String TABLE_FEEDBACK = "feedback";
    public static final String TABLE_SERVICEORDER = "service_order";
    public static final String TABLE_SERVICEORDERDETAILS = "service_order_details";
    public static final String TABLE_CUSTOMER_BANKDETAILS = "customer_bankdetails";
    //    public static final String TABLE_MI_COMMODITY_PRICE = "mi_commodity_price";
    public static final String TABLE_MI_CROP_SHIFTS = "mi_crop_shifts";
    public static final String TABLE_MI_PRICE_SURVEY = "mi_price_survey";
    public static final String TABLE_MI_PRODUCT_SURVEY = "mi_product_survey";
    public static final String TABLE_RETAILER = "retailers";
    public static final String TABLE_GRADE = "grade";
    public static final String TABLE_VERSION_CONTROL = "table_version_control";
    public static final String TABLE_STOCK_RETURNS = "stock_returns";
    public static final String TABLE_STOCK_RETURNS_DETAILS = "stock_returns_details";
    public static final String TABLE_CATALOGUE_CROPS = "catalogue_crops";
    public static final String TABLE_CATALOGUE_CROPS_PRODUCTS = "catalogue_crops_products";
    public static final String TABLE_SERVICE_ORDER_APPROVAL = "service_order_approval";
    public static final String TABLE_DISTRICT = "districts";
    public static final String TABLE_SERVICE_ORDER_HISTORY = "service_order_history";

    public static final String TABLE_MARKET_POTENTIAL = "mi_market_potential";
    public static final String TABLE_COMPETITOR_CHANNEL = "mi_competitorChannel";
    public static final String TABLE_COMPETITOR_STRENGTH = "mi_competitorStrength";
    public static final String TABLE_CHANNEL_PREFERENCE = "mi_channelPreference";
    public static final String TABLE_COMMODITY_PRICE = "mi_commodity_price";
    public static final String TABLE_PRODUCT_PRICING_SURVEY = "mi_priceproductSurvey";
    public static final String TABLE_CROP_SHIFTS = "mi_cropShifts";

    public static final String TABLE_SEASON = "season_master";
    public static final String TABLE_SEASON_LINE_ITEMS = "season_line_items";
    public static final String TABLE_YE = "yeild_estimation";
    public static final String TABLE_GODOWN = "godown";
    public static final String TABLE_GODOWN_STOCK = "stocks_godown";
    public static final String TABLE_STOCK_DISPATCH = "stock_dispatch";
    public static final String TABLE_STOCK_DISPATCH_ITEM = "stock_dispatch_line_items";
    public static final String TABLE_RETAILER_STOCK_SUPPLY = "retailer_stock_supply";





    public static final String VERSION_TABLE_NAME = "table_name";
    public static final String VERSION_CODE = "version_code";
    public static final String UPDATED_DATE = "updated_date";

    // Contacts Table Columns names
    public static final String GRADE_ID = "grade_id";
    public static final String GRADE_NAME = "grade_name";
    public static final String PRICE_PER_KM = "price_per_km";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PH_NO = "phone_number";


    // Contacts Table Columns names
    public static final String KEY_ORDER_ID = "id";
    public static final String KEY_ORDER_NAME = "name";
    public static final String KEY_ORDER_AMT = "phone_number";


    // table companies column names
    public static final String KEY_TABLE_COMPANIES_ID = "id";
    public static final String KEY_TABLE_COMPANIES_MASTER_ID = "company_id";
    public static final String KEY_TABLE_COMPANIES_NAME = "name";
    public static final String KEY_TABLE_COMPANIES_COMPANY_SAP_ID = "company_sap_id";
    public static final String KEY_TABLE_COMPANIES_COMPANY_CODE = "company_code";
    public static final String KEY_TABLE_COMPANIES_COMPANY_STATUS = "status";
    public static final String KEY_TABLE_COMPANIES_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_COMPANIES_UPDATED_DATETIME = "updated_datetime";

    // table company_division_crops column names
    public static final String KEY_TABLE_COMPANY_DIVISION_CROPS_ID = "id";
    public static final String KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID = "crop_id";

    // table USER COMPANY CUSTOMER column names
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_ID = "id";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID = "user_id";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID = "customer_id";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_MASTER_ID = "junction_id";

    // table SCHEME PRODUCTS column names
    public static final String KEY_TABLE_SCHEME_PRODUCTS_ID = "id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID = "scheme_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID = "product_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_PRICE = "price_per_packet";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_REGION_ID = "region_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_CROP_ID = "crop_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID = "slab_id";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_BOOKING_INACTIVE = "booking_incentive";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM = "valid_from";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_VALID_TO = "valid_to";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_BOOKING_YEAR = "booking_year";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_SEASON_CODE = "season_code";
    public static final String KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE = "extenstion_date";


    // table USER COMPANY DIVISION column names
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_ID = "id";
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_USER_ID = "user_id";
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_USER_COMPANY_DIVISION_MASTER_ID = "junction_id";

    // table complaints column names
    public static final String KEY_TABLE_COMPLAINTS_ID = "complaint_id";
    public static final String KEY_TABLE_COMPLAINT_USER_ID = "user_id";
    public static final String KEY_TABLE_COMPLAINTS_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_COMPLAINTS_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_COMPLAINTS_CROP_ID = "crop_id";
    public static final String KEY_TABLE_COMPLAINTS_VARIETY = "variety_id";
    public static final String KEY_TABLE_COMPLAINTS_PRODUCT_ID = "product_id";
    public static final String KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO = "marketing_lot_number";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE = "complaint_type";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE = "complaint_percentage";
    public static final String KEY_TABLE_COMPLAINTS_FARMER_NAME = "farmer_name";
    //    public static final String KEY_TABLE_COMPLAINTS_CONTACT_NO = "contact_no";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES = "complaint_area_acres";
    public static final String KEY_TABLE_COMPLAINTS_SOIL_TYPE = "soil_type";
    public static final String KEY_OTHERS = "others";
    public static final String KEY_TABLE_COMPLAINTS_PURCHASED_QTY = "purchased_quantity";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_QTY = "complain_quantity";
    public static final String KEY_PURCHASE_DATE = "purchase_date";
    public static final String KEY_TABLE_COMPLAINTS_BILL_NUMBER = "bill_number";
    public static final String KEY_TABLE_COMPLAINTS_RETAILER_NAME = "retailer_name";
    public static final String KEY_TABLE_COMPLAINTS_RETAILER_ID = "retailer_id";
    public static final String KEY_TABLE_COMPLAINTS_DISTRIBUTOR = "distributor";
    public static final String KEY_TABLE_COMPLAINTS_MANDAL = "mandal";
    public static final String KEY_TABLE_COMPLAINTS_IRRIGATION = "zone";
    public static final String KEY_TABLE_COMPLAINTS_AREA_IN_ACERS = "complaint_area_in_acers";
    public static final String KEY_TABLE_COMPLAINTS_FRAMER_NAME = "name";
    public static final String KEY_TABLE_COMPLAINTS_DATE_OF_PURCHASE = "date_of_purchase";
    public static final String KEY_TABLE_COMPLAINTS_NO_OF_PKTS_PURCHASED = "no_of_packets";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_NO_OF_PKTS = "complaint_no_of_packets";
    public static final String KEY_TABLE_COMPLAINTS_BILL_RECEIPET_REFNO = "bill_ref_no";
    public static final String KEY_TABLE_COMPLAINTS_FARMER_CONTACT_NO = "farmer_contact_no";
    public static final String KEY_TABLE_COMPLAINTS_CONTACT_NO = "contact_no";
    public static final String KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS = "performance_of_lot_in_other_fields";
    public static final String KEY_TABLE_COMPLAINTS_REMARKS = "remarks";
    public static final String KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME = "inspected_date";
    public static final String KEY_TABLE_COMPLAINTS_INSPECTED_BY_STAFF = "inspected_by_staff";
    public static final String KEY_TABLE_COMPLAINTS_INSPECTED_BY_DESIGNATION = "inspected_by_designation";
    public static final String KEY_TABLE_COMPLAINTS_INSPECTED_BY_EMAIL = "inspected_by_email";
    public static final String KEY_TABLE_COMPLAINTS_INSPECTED_BY_CONTACT_NO = "inspected_by_contact_no";
    public static final String KEY_TABLE_COMPLAINTS_VILAGE = "village";
    public static final String KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD = "image_upload";
    public static final String KEY_REGULATORY_TYPE = "regulatory_type";
    public static final String KEY_TABLE_COMPLAINTS_SAMPLING_DATE = "sampling_date";
    public static final String KEY_TABLE_COMPLAINTS_PLACE_SAMPLING = "place_sampling";
    public static final String KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME = "sampling_officer_name";
    public static final String KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT = "sampling_officer_contact";
    public static final String KEY_TABLE_COMPLAINTS_COMMENTS = "comments";
    public static final String KEY_TABLE_COMPLAINTS_STATUS = "status";
    public static final String KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS = "complaint_remarks";
    public static final String KEY_TABLE_COMPLAINTS_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_COMPLAINTS_DEALER = "dealer";
    public static final String KEY_TABLE_COMPLAINTS_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_COMPLAINTS_FFMID = "ffmid";
    public static final String KEY_TABLE_COMPLAINTS_ZONE = "zone";
    public static final String KEY_TABLE_COMPLAINTS_REGION = "region";
    public static final String KEY_TABLE_COMPLAINTS_STATE = "state";
    public static final String KEY_TABLE_COMPLAINTS_DATE_OF_SOWING = "date_of_sowing";
    public static final String KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT = "date_of_complaint";
    public static final String KEY_TABLE_COMPLAINTS_DISTRICT = "district";
    public static final String KEY_TABLE_COMPLAINTS_STAGES = "stages";

    //FEEDBACK TABLE COLUMN NAMES

    public static final String KEY_TABLE_FEEDBACK_FEEDBACK_ID = "feedback_id";
    public static final String KEY_TABLE_FEEDBACK_USER_ID = "user_id";
    public static final String KEY_TABLE_FEEDBACK_FARMER_NAME = "farmer_name";
    public static final String KEY_TABLE_FEEDBACK_PLACE = "place";
    public static final String KEY_TABLE_FEEDBACK_CONTACT_NO = "contact_no";
    public static final String KEY_TABLE_FEEDBACK_CROP = "crop";
    public static final String KEY_TABLE_FEEDBACK_HYBRID = "hybrid";
    public static final String KEY_TABLE_FEEDBACK_SOWING_DATE = "sowing_date";
    public static final String KEY_TABLE_FEEDBACK_FEEDBACK_MESSAGE = "feedback_message";
    public static final String KEY_TABLE_FEEDBACK_IMAGE = "image";
    public static final String KEY_TABLE_FEEDBACK_FFMID = "ffmid";
    public static final String KEY_TABLE_FEEDBACK_TRANSPLANT_DATE = "transplanting_date";

    // table crops column names
    public static final String KEY_TABLE_CROPS_CROP_ID = "ids";
    public static final String KEY_TABLE_CROPS_CROP_MASTER_ID = "crop_id";
    public static final String KEY_TABLE_CROPS_CROP_NAME = "crop_name";
    public static final String KEY_TABLE_CROPS_CROP_SAP_ID = "crop_sap_id";
    public static final String KEY_TABLE_CROPS_CROP_CODE = "crop_code";
    public static final String KEY_TABLE_CROPS_DIVISION_ID = "divsion_id";
    public static final String KEY_TABLE_CROPS_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_CROPS_UPDATED_DATETIME = "updated_datetime";


    // table customers column names
    public static final String KEY_TABLE_CUSTOMER_ID = "c_id";
    public static final String KEY_TABLE_CUSTOMER_MASTER_ID = "customer_id";
    public static final String KEY_TABLE_CUSTOMER_NAME = "customer_name";
    public static final String KEY_TABLE_CUSTOMER_CODE = "customer_code";
    public static final String KEY_TABLE_CUSTOMER_ADDRESS = "address";
    public static final String KEY_TABLE_CUSTOMER_STREET = "street";
    public static final String KEY_TABLE_CUSTOMER_CITY = "city";
    public static final String KEY_TABLE_CUSTOMER_COUNTRY = "country";
    public static final String KEY_TABLE_CUSTOMER_REGION_ID = "region_id";
    public static final String KEY_TABLE_CUSTOMER_TELEPHONE = "telephone";

    public static final String KEY_TABLE_CUSTOMER_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_CUSTOMER_STATUS = "status";
    public static final String KEY_TABLE_CUSTOMER_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_CUSTOMER_UPDATED_DATETIME = "updated_datetime";

    public static final String KEY_TABLE_CUSTOMER_PASSWORD = "password";
    public static final String KEY_TABLE_CUSTOMER_EMAIL = "email";
    public static final String KEY_TABLE_CUSTOMER_STATE = "state";
    public static final String KEY_TABLE_CUSTOMER_DISTRICT = "district";
    public static final String KEY_TABLE_CUSTOMER_LAT_LNG = "lat_lon";


    // table servce order column names
    public static final String KEY_TABLE_SERVICEORDER_ID = "service_id";
    public static final String KEY_TABLE_SERVICEORDER_MASTER_ID = "order_id";
    public static final String KEY_TABLE_SERVICEORDER_ORDERTYPE = "order_type";
    public static final String KEY_TABLE_SERVICEORDER_ORDERDATE = "order_date";
    public static final String KEY_TABLE_SERVICEORDER_USER_ID = "user_id";
    public static final String KEY_TABLE_SERVICEORDER_CUSTOMER_ID = "customer_id";
    public static final String KEY_TABLE_SERVICEORDER_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_SERVICEORDER_COMPANY_ID = "company_id";
    public static final String KEY_TABLE_SERVICEORDER_STATUS = "status";
    public static final String KEY_TABLE_SERVICEORDER_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_SERVICEORDER_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_SERVICEORDER_SLAB_ID = "slab_id";
    public static final String KEY_TABLE_SERVICEORDER_FFM_ID = "ffmid";
    public static final String KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT = "advance_amount";
    public static final String KEY_TABLE_SERVICEORDER_CREATED_BY = "createdby";
    public static final String KEY_TABLE_SERVICEORDER_APPROVAL_STATUS = "approval_status";
    public static final String KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS = "approval_comments";
    public static final String KEY_TABLE_SERVICEORDER_OFFLINE_APPROVAL_SET = "offline_approval_status";
    public static final String KEY_TABLE_SERVICEORDER_UPDATED_BY = "updatedby";
    public static final String KEY_TABLE_SERVICEORDER_SAP_ID = "order_sap_id";
    public static final String KEY_TABLE_SERVICEORDER_SEASON = "season_name";

    // table servce order details column names
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ID = "order_detail_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID = "service_order_detail_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID = "service_relation_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID = "crop_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID = "scheme_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID = "product_id";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY = "quantity";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY = "order_quantity";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE = "order_price";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT = "advance_amount";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_STATUS = "status";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME = "updated_datetime";

    // table customer_details column names
    public static final String KEY_TABLE_CUSTOMER_DETAILS_ID = "customer_details_id";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID = "customer_id";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID = "division_id";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT = "credit_limit";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET = "inside_bucket";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET = "outside_bucket";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_STATUS = "status";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_CUSTOMER_DETAILS_CREDIT_BALANCE = "credit_balance";

    // table DISTRIBUTOR_RETAILER column names
    public static final String KEY_TABLE_DISTRIBUTOR_RTAILER_ID = "distributor_retailer_id";
    public static final String KEY_TABLE_DISTRIBUTOR_ID = "distributor_id";
    public static final String KEY_TABLE_RETAILER_ID = "retailer_id";


    // table division column names
    public static final String KEY_TABLE_DIVISION_ID = "div_id";
    public static final String KEY_TABLE_DIVISION_MASTER_ID = "division_id";
    public static final String KEY_TABLE_DIVISION_NAME = "division_name";
    public static final String KEY_TABLE_DIVISION_SAP_ID = "division_sap_id";
    public static final String KEY_TABLE_DIVISION_CODE = "division_code";
    public static final String KEY_TABLE_DIVISION_STATUS = "status";
    public static final String KEY_TABLE_DIVISION_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_DIVISION_UPDATED_DATETIME = "updated_datetime";


    // table errors column names
    private static final String KEY_TABLE_ERRORS_ID = "id";
    private static final String KEY_TABLE_ERRORS_NAME = "name";
    private static final String KEY_TABLE_ERRORS_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_ERRORS_UPDATED_DATETIME = "updated_datetime";

    public static final String KEY_DD_ID = "id";
    public static final String KEY_DD_MASTER_ID = "dailydairy_id";
    public static final String KEY_DD_TITLE = "title";
    public static final String KEY_DD_USER_ID = "user_id";
    public static final String KEY_DD_COMMENTS = "comments";
    public static final String KEY_DD_TIME_SLOT = "time_slot";
    public static final String KEY_DD_DATE = "dairy_date";
    public static final String KEY_DD_CREATED_DATE = "created_date";
    public static final String KEY_DD_FFMID = "ffmid";
    public static final String KEY_DD_TYPE = "type"; // 1-> Normal DD, 2-> Adhoc Task
    public static final String KEY_DD_TENTATIVE_TIME = "tentative_time";
    public static final String KEY_DD_STATUS = "status";// 1-> Pending DD, 2-> Task Complete

    // table EVENT MANAGEMENT column names
    private static final String KEY_TABLE_EVENT_MANAGEMENT_ID = "event_id";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_TITLE = "title";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_PLANED_EVENT_DATE = "planed_event_date";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_PLANED_EVENT_TIME = "planed_event_starttime_endtime";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_CONCER_PERSON = "concern_person";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_VILLAGE = "village";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_LOCATION_ADDR = "location_addr";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_PARTICIPANTS = "participants";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_STATUS = "status";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_EVENT_MANAGEMENT_UPDATED_DATETIME = "updated_datetime";

    // table farmers column names

    public static final String KEY_TABLE_FARMER_PRIMARY_ID = "farmer_primary_id";
    public static final String KEY_TABLE_FARMER_ID = "farmer_id";
    public static final String KEY_TABLE_FARMER_NAME = "farmer_name";
    public static final String KEY_TABLE_FARMER_PHONE = "mobile";
    public static final String KEY_TABLE_FARMER_STATE = "state";
    public static final String KEY_TABLE_FARMER_DISTRICT = "location_district";
    public static final String KEY_TABLE_FARMER_REGION_ID = "region_id";
    public static final String KEY_TABLE_FARMER_VILLAGE = "location_village";
    public static final String KEY_TABLE_FARMER_TALUKA = "location_taluka";
    public static final String KEY_TABLE_FARMER_TOTAL_LAND_HOLDING = "total_land_holding";
    public static final String KEY_TABLE_FARMER_CROP_ID = "crop_id";
    public static final String KEY_TABLE_FARMER_STATUS = "status";
    public static final String KEY_TABLE_FARMER_FFM_ID = "ffm_id";
    public static final String KEY_TABLE_FARMER_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_FARMER_UPDATED_DATETIME = "updated_datetime";


    // table GEO_TRACKING column names
    public static final String KEY_TABLE_GEO_TRACKING_ID = "t_id";
    public static final String KEY_TABLE_GEO_TRACKING_MASTER_ID = "tracking_id";
    public static final String KEY_TABLE_GEO_TRACKING_VISIT_TYPE = "visit_type";
    public static final String KEY_TABLE_GEO_TRACKING_USER_ID = "user_id";
    public static final String KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG = "check_in_lat_lon";
    public static final String KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG = "check_out_lat_lon";
    public static final String KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG = "route_path_lat_lon";
    public static final String KEY_TABLE_GEO_TRACKING_DISTANCE = "distance";
    public static final String KEY_TABLE_GEO_TRACKING_VISIT_DATE = "visit_date";
    public static final String KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME = "check_in_time";
    public static final String KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME = "check_out_time";
    public static final String KEY_TABLE_GEO_TRACKING_STATUS = "status";
    public static final String KEY_TABLE_GEO_TRACKING_FFMID = "ffmid";
    public static final String KEY_TABLE_GEO_TRACKING_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_GEO_TRACKING_UPDATED_STATUS = "updated_status"; //1-> Synced, 0-> Need to Sync
    public static final String KEY_TABLE_GEO_TRACKING_POLYLINE = "polyline";
    public static final String KEY_TABLE_GEO_TRACKING_VERSION = "version";
    public static final String METER_READING_CHECKIN_IMAGE = "meter_reading_checkin_image";
    public static final String METER_READING_CHECKIN_TEXT = "meter_reading_checkin_text";
    public static final String METER_READING_CHECKOUT_IMAGE = "meter_reading_checkout_image";
    public static final String METER_READING_CHECKOUT_TEXT = "meter_reading_checkout_text";
    public static final String VEHICLE_TYPE = "vehicle_type";
    public static final String CHECKIN_COMMENT = "checkin_comment";
    public static final String PERSONAL_USES_KM = "personal_uses_km";
    public static final String PAUSE = "pause";
    public static final String RESUME = "resume";
    public static final String SYNC_STATUS = "sync_status";


    // table companies column names
    public static final String KEY_TABLE_USERS_USER_ID = "id";
    public static final String KEY_TABLE_USERS_MASTER_ID = "user_id";
    public static final String KEY_TABLE_USERS_FIRST_NAME = "first_name";
    public static final String KEY_TABLE_USERS_LAST_NAME = "last_name";
    public static final String KEY_TABLE_USERS_MOBILE_NO = "mobile_no";
    public static final String KEY_TABLE_USERS_EMAIL = "email";
    public static final String KEY_TABLE_USERS_SAP_ID = "sap_id";
    public static final String KEY_TABLE_USERS_PASSWORD = "password";
    public static final String KEY_TABLE_USERS_ROLE_ID = "role_id";
    public static final String KEY_TABLE_USERS_REPORTING_MANAGER_ID = "reporting_manager_id";
    public static final String KEY_TABLE_USERS_STATUS = "status";
    public static final String KEY_TABLE_USERS_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_USERS_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_USERS_DESIGNATION = "designation";
    public static final String KEY_TABLE_USERS_HEADQUARTER = "headquarter";
    public static final String KEY_TABLE_USERS_LOCATION = "location";
    public static final String KEY_TABLE_USERS_REGION_ID = "region_id";
    public static final String KEY_TABLE_USERS_IMAGE = "image";
    public static final String KEY_TABLE_USERS_GRADE = "grade";
    public static final String KEY_TABLE_USERS_COST_CENTER = "cost_center";


    // table ucdd column names
    private static final String KEY_TABLE_UCDD_JUNCTION_ID = "junction_id";
    private static final String KEY_TABLE_UCDD_USER_ID = "user_id";
    private static final String KEY_TABLE_UCDD_COMPANY_ID = "company_id";
    private static final String KEY_TABLE_UCDD_DIVISION_ID = "division_id";
    private static final String KEY_TABLE_UCDD_CUSTOMER_ID = "customer_id";

    //table user_region colum names
    private static final String KEY_TABLE_USER_REGION_PRIMARY_ID = "id";
    private static final String KEY_TABLE_USER_REGION_USER_REGION_ID = "user_regions_id";
    private static final String KEY_TABLE_USER_REGION_USER_ID = "user_id";
    private static final String KEY_TABLE_USER_REGION_REGION_ID = "region_id";
    private static final String KEY_TABLE_USER_REGION_STATUS = "status";


    // table SM column names
    private static final String KEY_TABLE_SM_ID = "stock_movement_id";
    private static final String KEY_TABLE_SM_MOVEMENT_TYPE = "movement_type";
    private static final String KEY_TABLE_SM_USER_ID = "user_id";
    private static final String KEY_TABLE_SM_DIVISION = "division_id";
    private static final String KEY_TABLE_SM_COMPANY_ID = "company_id";
    private static final String KEY_TABLE_SM_CREATED_BY = "created_by";
    private static final String KEY_TABLE_SM_UPDATED_BY = "updated_by";
    private static final String KEY_TABLE_SM_STATUS = "status";
    public static final String FFM_ID = "ffm_id";
    private static final String KEY_TABLE_SM_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_SM_UPDATED_DATETIME = "updated_datetime";

    // table SMD column names

    public static final String KEY_TABLE_SMD_DETAIL_ID = "stock_movement_detail_id";
    private static final String KEY_TABLE_SMD_STOCK_MOVEMENT_ID = "stock_movement_id";
    private static final String KEY_TABLE_SMD_USER_TYPE = "user_type";
    private static final String KEY_TABLE_SMD_CUSTOMER_ID = "customer_id";
    private static final String KEY_TABLE_SMD_CROP_ID = "crop_id";
    private static final String KEY_TABLE_SMD_PRODUCT_ID = "product_id";
    private static final String KEY_TABLE_SMD_STOCK_PLACED = "stock_placed";
    private static final String KEY_TABLE_SMD_CURRENT_STOCK = "current_stock";
    private static final String KEY_TABLE_SMD_PLACED_DATE = "placed_date";
    private static final String KEY_TABLE_SMD_POG = "pog";
    private static final String KEY_TABLE_CREATED_BY = "created_by";
    private static final String KEY_TABLE_UPDATED_BY = "updated_by";
    private static final String KEY_TABLE_SMD_CREATED_DATETIME = "created_datetime";
    private static final String KEY_TABLE_SMD_UPDATED_DATETIME = "updated_datetime";
    private static final String KEY_TABLE_SMD_ORDER_SAP_ID = "order_sap_id";

    private static final String KEY_TABLE_SMD_RETAILER_ID_PRIMARY_KEY = "stock_movement_retailer_id";
    private static final String KEY_TABLE_SMD_RETAILER_USER_ID = "user_id";
    private static final String KEY_TABLE_SMD_RETAILER_ID = "retailer_id";
    private static final String KEY_TABLE_SMD_RETAILER_VERIFIED = "verified";
    private static final String KEY_TABLE_SMD_RETAILER_VERIFIED_BY = "verified_by";


    //table employee_visit_management column names

    public static final String KEY_EMP_VISIT_ID = "emp_v_id";
    public static final String KEY_EMP_VISIT_MASTER_ID = "emp_visit_id";
    public static final String KEY_EMP_VISIT_USER_ID = "user_id";
    public static final String KEY_EMP_VISIT_CUSTOMER_ID = "customer_id";
    public static final String KEY_EMP_VISIT_PLAN_TYPE = "visit_plan_type";
    public static final String KEY_EMP_PURPOSE_VISIT = "purpose_visit";
    public static final String KEY_EMP_PURPOSE_VISIT_IDS = "purpose_visit_ids";
    public static final String KEY_EMP_PLAN_DATE_TIME = "plan_date_time";
    public static final String KEY_EMP_VISIT_CROP_ID = "crop_id";
    public static final String KEY_EMP_CONCERN_PERSON_NAME = "concern_person_name";
    public static final String KEY_EMP_MOBILE = "mobile";
    public static final String KEY_EMP_VILLAGE = "village";
    public static final String KEY_EMP_LOCATION_ADDRESS = "location_address";
    public static final String KEY_EMP_REGION_ID = "region_id";
    public static final String KEY_EMP_RETAILER_ID = "retailer_id";
    public static final String KEY_EMP_FARMER_ID = "farmer_id";
    public static final String KEY_EMP_LOCATION_DISTRICT = "location_district";
    public static final String KEY_EMP_LOCATION_TALUKA = "location_taluka";
    public static final String KEY_EMP_LOCATION_VILLAGE = "location_village";
    public static final String KEY_EMP_GSTIN_NO = "retailer_gstin_no";
    public static final String KEY_EMP_FEILD_AREA = "field_area";
    public static final String KEY_EMP_CHECK_IN_TIME = "check_in_time";
    public static final String KEY_EMP_COMMENTS = "comments";
    public static final String KEY_EMP_CREATED_BY = "created_by";
    public static final String KEY_EMP_UPDATED_BY = "updated_by";
    public static final String KEY_EMP_STATUS = "status";
    public static final String KEY_EMP_FFM_ID = "emp_ffm_id";
    public static final String KEY_EMP_SERVER_FLAG = "server_flag";
    public static final String KEY_EMP_TYPE = "type";
    public static final String KEY_EMP_CREATED_DATETIME = "created_datetime";
    public static final String KEY_EMP_UPDATE_DATETIME = "update_datetime";
    public static final String KEY_EMP_GEO_TRACKING_ID = "geo_tracking_id";
    public static final String KEY_EMP_APPROVAL_STATUS = "approval_status";
    public static final String KEY_EMP_EVENT_NAME = "event_name";
    public static final String KEY_EMP_EVENT_END_DATE = "event_end_date";
    public static final String KEY_EMP_EVENT_PURPOSE = "event_purpose";
    public static final String KEY_EMP_EVENT_VENUE = "event_venue";
    public static final String KEY_EMP_EVENT_PARTICIPANTS = "event_participants";
    public static final String KEY_EMP_TASK_COMPLETED_LATLNG = "task_completed_latlng";


// table plants column names

    private static final String KEY_PLANT_ID = "plant_id";
    private static final String KEY_PLANT_NAME = "plant_name";
    private static final String KEY_PLANT_SAP_CODE = "plant_sap_code";
    private static final String KEY_STATE = "state";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PLANTS_STATUS = "status";
    private static final String KEY_PLANTS_CREATED_DATETIME = "created_datetime";
    private static final String KEY_PLANTS_UPDATEd_DATETIME = "updated_datetime";

// Table region column names

    public static final String KEY_SCHEMES_ID = "id";
    public static final String KEY_SCHEMES_MASTER_ID = "scheme_id";
    public static final String KEY_SCHEMES_TITLE = "scheme_title";
    public static final String KEY_SCHEMES_SAP_CODE = "scheme_sap_code";
    public static final String KEY_SCHEMES_FILE_LOCATION = "file_location";
    public static final String KEY_SCHEMES_STATUS = "status";

    // Table schemes column names

    public static final String KEY_REGION_ID = "id";
    public static final String KEY_REGION__MASTER_ID = "region_id";
    public static final String KEY_REGION_CODE = "region_code";
    public static final String KEY_REGION_NAME = "region_name";
    public static final String KEY_REGION_STATUS = "status";

// Table products column names

    public static final String KEY_PRODUCT_ID = "product_ids";
    public static final String KEY_PRODUCT_MASTER_ID = "product_id";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_DESCRIPTION = "product_description";
    public static final String KEY_PRODUCT_SAP_CODE = "product_sap_code";
    public static final String KEY_PRODUCT_CROP_ID = "product_crop_id";
    public static final String KEY_PRODUCTS_COMPANY_ID = "company_id";
    public static final String KEY_PRODUCTS_DIVISION_ID = "division_id";
    public static final String KEY_PRODUCTS_REGION = "region";
    public static final String KEY_PRODUCTS_PRICE = "price";
    public static final String KEY_PRODUCTS_PACKETS_COUNT = "no_packets";
    public static final String KEY_PRODUCTS_CATALOG_URL = "catalog_url";
    public static final String KEY_PRODUCT_DISCOUNT = "discount";
    public static final String KEY_PRODUCT_FROM_DATE = "from_date";
    public static final String KEY_PRODUCT_TO_DATE = "to_date";
    public static final String KEY_PRODUCT_STATUS = "status";
    public static final String KEY_PRODUCT_IMAGE = "products_images";
    public static final String KEY_PRODUCT_VIDEO = "product_videos";
    public static final String KEY_PRODUCT_CREATED_DATETIME = "created_datetime";
    public static final String KEY_PRODUCT_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_PRODUCT_BRAND_NAME = "brand_name";

    //table payment_collection column names
    public static final String KEY_PAYMENT_COLLECTION_PAYMENT_ID = "p_id";
    private static final String KEY_PAYMENT_COLLECTION_PAYMENT_MASTER_ID = "payment_id";
    private static final String KEY_PAYMENT_COLLECTION_PAYMENT_TYPE = "payment_type";
    private static final String KEY_PAYMENT_COLLECTION_USER_ID = "user_id";
    private static final String KEY_PAYMENT_COLLECTION_COMPANY_ID = "company_id";
    private static final String KEY_PAYMENT_COLLECTION_DIVISION_ID = "division_id";
    private static final String KEY_PAYMENT_COLLECTION_CUSTOMER_ID = "customer_id";
    private static final String KEY_PAYMENT_COLLECTION_TOTAL_AMOUNT = "total_amount";
    private static final String KEY_PAYMENT_COLLECTION_PAYMENT_MODE = "payment_mode";
    private static final String KEY_PAYMENT_COLLECTION_BANK_NAME = "bank_name";
    private static final String KEY_PAYMENT_COLLECTION_RTGS_OR_NEFT_NO = "rtgs_or_neft_no";
    private static final String KEY_PAYMENT_COLLECTION_PAYMENT_DATETIME = "payment_datetime";
    private static final String KEY_PAYMENT_COLLECTION_DATE_ON_CHEQUE_NUMBER = "date_on_cheque_no";
    private static final String KEY_PAYMENT_COLLECTION_CHEQUE_NO_DD_NO = "cheque_no_dd_no";
    private static final String KEY_PAYMENT_COLLECTION_STATUS = "status";
    private static final String KEY_PAYMENT_COLLECTION_CREATED_DATETIME = "created_datetime";
    private static final String KEY_PAYMENT_COLLECTION_UPDATEd_DATETIME = "updated_datetime";
    public static final String KEY_PAYMENT_COLLECTION_FFMID = "ffmid";

    // Table customer_bankdetails column names

    public static final String KEY_BANKDETAIL_ID = "id";
    public static final String KEY_BANKDETAIL_MASTER_ID = "bank_detail_id";
    public static final String KEY_BANKDETAIL_CUSTOMER_ID = "customer_id";
    public static final String KEY_BANKDETAIL_ACCOUNT_NUMBER = "account_no";
    public static final String KEY_BANKDETAIL_ACCOUNT_NAME = "account_name";
    public static final String KEY_BANKDETAIL_BANK_NAME = "bank_name";
    public static final String KEY_BANKDETAIL_BRANCH_NAME = "branch_name";
    public static final String KEY_BANKDETAIL_IFSC_CODE = "ifsc_code";
    public static final String KEY_BANKDETAIL_STATUS = "status";
    public static final String KEY_BANKDETAIL_CREATED_BY = "created_by";
    public static final String KEY_UPDATED_BY = "updated_by";
    public static final String KEY_CREATED_DATE = "created_date";
    public static final String KEY_BANKDETAIL_FFMID = "ffmid";

// Table mi_commodity_price column names

    public static final String KEY_COMMODITY_PRICE_ID = "id";
    public static final String KEY_COMMODITY_PRICE_MASTER_ID = "commodity_price_id";
    public static final String KEY_COMMODITY_PRICE_CROP_NAME = "crop_name";
    public static final String KEY_COMMODITY_PRICE_VARIETY_TYPE = "variety_type";
    public static final String KEY_COMMODITY_PRICE_APMC_MANDI_PRICE = "apmc_mandi_price";
    public static final String KEY_COMMODITY_PRICE_COMMODITY_DEALER_AGENT_PRICE = "commodity_dealer_agent_price";
    public static final String KEY_COMMODITY_PRICE_PURCHASE_PRICE_BY_INDUSTRY = "purchage_price_by_industry";
    public static final String KEY_COMMODITY_PRICE_CREATED_BY = "created_by";
    public static final String KEY_COMMODITY_PRICE_CREATED_ON = "created_on";
    public static final String KEY_COMMODITY_PRICE_FFMID = "ffmid";

    // Table mi_crop_shifts column names

    public static final String KEY_CROP_SHIFTS_ID = "id";
    public static final String KEY_CROP_SHIFTS_MASTER_ID = "crop_shits_id";
    public static final String KEY_CROP_SHIFTS_CROP_NAME = "crop_name";
    public static final String KEY_CROP_SHIFTS_VARIETY_TYPE = "variety_type";
    public static final String KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA = "previous_year_area_in_acres";
    public static final String KEY_CROP_SHIFTS_CURRENT_YEAR_EXPECTED_AREA = "current_year_expected_area";
    public static final String KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE = "percentage_increase_or_decrease";
    public static final String KEY_CROP_SHIFTS_REASON_CROP_SHIFT = "reason_for_crop_shift";
    public static final String KEY_CROP_SHIFTS_CREATED_BY = "created_by";
    public static final String KEY_CROP_SHIFTS_CREATED_ON = "created_on";
    public static final String KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR = "previous_year_srr";
    public static final String KEY_CROP_SHIFTS_CURRENT_YEAR_SRR = "current_year_srr";
    public static final String KEY_CROP_SHIFTS_NEXT_YEAR_SRR = "next_year_srr";
    public static final String KEY_CROP_SHIFTS_CROP_IN_SAVED_SEED = "crop_in_saved_seed";
    public static final String KEY_CROP_SHIFTS_FFMID = "ffmid";


    // Table mi_price_survey column names

    public static final String KEY_PRICE_SURVEY_ID = "id";
    public static final String KEY_PRICE_SURVEY_MASTER_ID = "price_survey_id";
    public static final String KEY_PRICE_SURVEY_COMPANY_NAME = "company_name";
    public static final String KEY_PRICE_SURVEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRICE_SURVEY_SKU_PACK_SIZE = "sku_pack_size";
    public static final String KEY_PRICE_SURVEY_RETAIL_PRICE = "retail_price";
    public static final String KEY_PRICE_SURVEY_INVOICE_PRICE = "invoice_price";
    public static final String KEY_PRICE_SURVEY_NET_DISTRIBUTOR_LANDING_PRICE = "net_distributor_landing_price";
    public static final String KEY_PRICE_SURVEY_CREATED_BY = "created_by";
    public static final String KEY_PRICE_SURVEY_CREATED_ON = "created_on";
    public static final String KEY_PRICE_SURVEY_FFMID = "ffmid";


    // Table mi_product_survey column names

    public static final String KEY_PRODUCT_SURVEY_ID = "id";
    public static final String KEY_PRODUCT_SURVEY_MASTER_ID = "product_survey_id";
    public static final String KEY_PRODUCT_SURVEY_COMPANY_NAME = "company_name";
    public static final String KEY_PRODUCT_SURVEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_SURVEY_NAME_OF_THE_CHECK_SEGMENT = "name_of_the_check_segment";
    public static final String KEY_PRODUCT_SURVEY_LAUNCH_YEAR = "launch_year";
    public static final String KEY_PRODUCT_SURVEY_NO_UNITS_SOLD = "no_units_sold";
    public static final String KEY_PRODUCT_SURVEY_AREA_CROP_SOWN_NEW_PRODUCT = "area_crop_sown_new_product";
    public static final String KEY_PRODUCT_SURVEY_REMARK_UNIQUE_FEATURE = "remarks_unique_feature";
    public static final String KEY_PRODUCT_SURVEY_CREATED_BY = "created_by";
    public static final String KEY_PRODUCT_SURVEY_CREATED_ON = "created_on";
    public static final String KEY_PRODUCT_SURVEY_FFMID = "ffmid";
    private final String userId;


    public static final String KEY_TABLE_RETAILER_PRIMARY_ID = "mobile_id";
    public static final String KEY_TABLE_RETAILER_MASTER_ID = "retailer_id";
    public static final String KEY_TABLE_RETAILER_NAME = "retailer_name";
    public static final String KEY_TABLE_RETAILER_TIN_NO = "retailer_tin_no";
    public static final String KEY_TABLE_RETAILER_GSTIN_NO = "retailer_gstin_no";
    public static final String KEY_TABLE_RETAILER_ADDRESS = "address";
    public static final String KEY_TABLE_RETAILER_DISTRICT = "location_district";
    public static final String KEY_TABLE_RETAILER_TALUKA = "location_taluka";
    public static final String KEY_TABLE_RETAILER_VILLAGE = "location_village";
    public static final String KEY_TABLE_RETAILER_REGION_ID = "region_id";
    public static final String KEY_TABLE_RETAILER_PHONE = "phone";
    public static final String KEY_TABLE_RETAILER_MOBILE = "mobile";
    public static final String KEY_TABLE_RETAILER_EMAIL_ID = "email_id";
    public static final String KEY_TABLE_RETAILER_DISTRIBUTOR_ID = "distributor_id";
    public static final String KEY_TABLE_RETAILER_SAP_CODE = "distributor_sap_code";
    public static final String KEY_TABLE_RETAILER_STATUS = "status";
    public static final String KEY_TABLE_RETAILER_CREATED_DATETIME = "created_datetime";
    public static final String KEY_TABLE_RETAILER_UPDATED_DATETIME = "updated_datetime";
    public static final String KEY_TABLE_RETAILER_FFMID = "ffmid";

    public static final String KEY_STOCK_RETURNS_ID = "stock_returns_id";
    public static final String KEY_COMPANY_ID = "company_id";
    public static final String KEY_DIVISION_ID = "division_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_CREATED_BY = "created_by";
    public static final String KEY_CREATED_DATETIME = "created_datetime";
    public static final String KEY_UPDATED_DATETIME = "updated_datetime";


    public static final String KEY_STOCK_RETURNS_DETAILS_ID = "stock_returns_details_id";
    public static final String KEY_CROP_ID = "crop_id";
    public static final String KEY_STOCK_RETURNS_DETAILS_PRODUCT_ID = "product_id";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_FFMID = "ffmid";

    public static final String ID = "id";
    public static final String CROP_NAME = "crop_name";
    public static final String CROP_IMG_PATH = "crop_img_path";
    public static final String SERVER_PK = "server_pk";
    public static final String STATUS = "status";
    public static final String VERSION = "version";

    public static final String CROP_RELATION_ID = "crop_relation_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_IMG = "product_img";
    public static final String DESCRIPTION = "description";
    public static final String DATE_TIME = "date_time";
    public static final String IMG_URI = "img_uri";


    // table service order approval column names
    public static final String KEY_SOA_PRIMARY_ID = "id";
    public static final String KEY_SOA_SERVICE_ORDER_APPROVAL_ID = "service_order_approval_id";
    public static final String KEY_SOA_ORDER_ID = "order_id";
    public static final String KEY_SOA_USER_ID = "user_id";
    public static final String KEY_SOA_ASSIGNED_USER_ID = "assigned_user_id";
    public static final String KEY_SOA_ORDER_STATUS = "order_status";
    public static final String KEY_SOA_CREATED_BY = "created_by";
    public static final String KEY_SOA_MODIFIED_BY = "modified_by";
    public static final String KEY_SOA_C_DATE_TIME = "created_date_time";
    public static final String KEY_SOA_U_DATE_TIME = "modified_date_time";
    public static final String KEY_SOA_SYNC_STATUS = "sync_status";
    public static final String KEY_SOA_PENDING_BY = "pending_by";

    //table service order history column names
    public static final String KEY_SOH_PRIMARY_ID = "id";
    public static final String KEY_SOH_ORDER_HISTORY_ID = "order_history_id";
    public static final String KEY_SOH_ORDER_ID = "order_id";
    public static final String KEY_SOH_CREATED_BY = "createdby";
    public static final String KEY_SOH_MODIFIED_BY = "modified_by";
    public static final String KEY_SOH_ORDER_APPROVAL_ID = "service_order_approval_id";

    //table planner approval column names
    public static final String KEY_PA_PRIMARY_ID = "id";
    public static final String KEY_PA_PLANNER_APPROVAL_ID = "planner_approval_id";
    public static final String KEY_PA_EMP_VISIT_ID = "emp_visit_id";
    public static final String KEY_PA_ASSIGNED_USER_ID = "assigned_user_id";
    public static final String KEY_PA_USER_ID = "user_id";
    public static final String KEY_PA_PLANNER_STATUS = "planner_status";
    public static final String KEY_PA_SYNC_STATUS = "sync_status";
    public static final String KEY_PA_CREATED_BY = "created_by";
    public static final String KEY_PA_MODIFIED_BY = "modified_by";
    public static final String KEY_PA_C_DATE_TIME = "created_date_time";
    public static final String KEY_PA_U_DATE_TIME = "modified_date_time";

    //table district column names
    public static final String KEY_DISTRICT_PRIMARY_ID = "id";
    public static final String KEY_DISTRICT_DISTRICT_ID = "district_id";
    public static final String KEY_DISTRICT_REGION_ID = "region_id";
    public static final String KEY_DISTRICT_DISTRICT_NAME = "district_name";
    public static final String KEY_DISTRICT_STATUS = "status";
    public static final String KEY_DISTRICT_CREATED_BY = "created_by";
    public static final String KEY_DISTRICT_MODIFIED_BY = "modified_by";
    public static final String KEY_DISTRICT_C_DATE_TIME = "created_date_time";
    public static final String KEY_DISTRICT_U_DATE_TIME = "modified_date_time";


    // Market Potential Table Columns
    public static final String KEY_MARKET_POTENTIAL_PRIMARY_ID = "id";
    public static final String KEY_MARKET_POTENTIAL_FFM_ID = "ffm_id";
    public static final String KEY_MARKET_POTENTIAL_USER_ID = "user_id";
    public static final String KEY_MARKET_POTENTIAL_REGION_ID = "region_id";
    public static final String KEY_MARKET_POTENTIAL_DISTRICT = "district";
    public static final String KEY_MARKET_POTENTIAL_TALUKA = "taluka";
    public static final String KEY_MARKET_POTENTIAL_VILLAGE = "village";
    public static final String KEY_MARKET_POTENTIAL_DIVISION_ID = "division_id";
    public static final String KEY_MARKET_POTENTIAL_CROP_ID = "crop_id";
    public static final String KEY_MARKET_POTENTIAL_KHARIF_CROP_ACREAGE = "kharif_crop_acreage";
    public static final String KEY_MARKET_POTENTIAL_RABI_CROP_ACREAGE = "rabi_crop_acreage";
    public static final String KEY_MARKET_POTENTIAL_SUMMER_CROP_ACREAGE = "summer_crop_acreage";
    public static final String KEY_MARKET_POTENTIAL_TOTAL_POTENTIAL_ACREAGE = "total_potential_acreage";
    public static final String KEY_MARKET_POTENTIAL_SEED_USAGE_QUANTITY_ACRE = "seed_usage_quanity";
    public static final String KEY_MARKET_POTENTIAL_TOTAL_MARKET_POTENTIAL_VOLUME = "total_market_potential_volume";
    public static final String KEY_MARKET_POTENTIAL_NSL_SALE = "nsl_sale";
    public static final String KEY_MARKET_POTENTIAL_TOP_COMPANY_1_NAME = "top_company_name_1";
    public static final String KEY_MARKET_POTENTIAL_COMPANY_1_QUANTITY = "company_1_qty";
    public static final String KEY_MARKET_POTENTIAL_TOP_COMPANY_2_NAME = "top_company_name_2";
    public static final String KEY_MARKET_POTENTIAL_COMPANY_2_QUANTITY = "company_2_qty";
    public static final String KEY_MARKET_POTENTIAL_TOP_COMPANY_3_NAME = "top_company_name_3";
    public static final String KEY_MARKET_POTENTIAL_COMPANY_3_QUANTITY = "company_3_qty";
    public static final String KEY_MARKET_POTENTIAL_TOP_COMPANY_4_NAME = "top_company_name_4";
    public static final String KEY_MARKET_POTENTIAL_COMPANY_4_QUANTITY = "company_4_qty";
    public static final String KEY_MARKET_POTENTIAL_TOP_COMPANY_5_NAME = "top_company_name_5";
    public static final String KEY_MARKET_POTENTIAL_COMPANY_5_QUANTITY = "company_5_qty";
    public static final String KEY_MARKET_POTENTIAL_CROP_NAME = "crop_name";
    public static final String KEY_MARKET_POTENTIAL_DIVISION_NAME = "division_name";

    // Competitor Channel Columns
    public static final String KEY_COMP_CHANNEL_PRIMARY_ID = "id";
    public static final String KEY_COMP_CHANNEL_USER_ID = "user_id";
    public static final String KEY_COMP_CHANNEL_FFM_ID = "ffm_id";
    public static final String KEY_COMP_CHANNEL_REGION_ID = "region_id";
    public static final String KEY_COMP_CHANNEL_DISTRICT = "district";
    public static final String KEY_COMP_CHANNEL_TERRITORY = "territory";
    public static final String KEY_COMP_CHANNEL_TOTAL_NO_OF_VILLAGES = "total_no_of_villages";
    public static final String KEY_COMP_CHANNEL_TOTAL_NO_OF_DISTRIBUTORS = "total_no_of_distributors";
    public static final String KEY_COMP_CHANNEL_TOTAL_NO_OF_RETAILERS = "total_no_of_retailers";
    public static final String KEY_COMP_CHANNEL_NO_OF_NSL_VILLAGES = "no_of_nsl_villages";
    public static final String KEY_COMP_CHANNEL_NO_OF_NSL_DISTRIBUTORS = "no_of_nsl_distributors";
    public static final String KEY_COMP_CHANNEL_NO_OF_NSL_RETAILERS = "no_of_nsl_retailers";
    public static final String KEY_COMP_CHANNEL_COMP_COMPANY_NAME_1 = "competitor_company_name_1";
    public static final String KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_1 = "no_of_distributors_1";
    public static final String KEY_COMP_CHANNEL_NO_OF_RETAILERS_1 = "no_of_retailers_1";
    public static final String KEY_COMP_CHANNEL_MARKET_PENETRATION_1 = "market_penetration_1";
    public static final String KEY_COMP_CHANNEL_COMP_COMPANY_NAME_2 = "competitor_company_name_2";
    public static final String KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_2 = "no_of_distributors_2";
    public static final String KEY_COMP_CHANNEL_NO_OF_RETAILERS_2 = "no_of_retailers_2";
    public static final String KEY_COMP_CHANNEL_MARKET_PENETRATION_2 = "market_penetration_2";
    public static final String KEY_COMP_CHANNEL_COMP_COMPANY_NAME_3 = "competitor_company_name_3";
    public static final String KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_3 = "no_of_distributors_3";
    public static final String KEY_COMP_CHANNEL_NO_OF_RETAILERS_3 = "no_of_retailers_3";
    public static final String KEY_COMP_CHANNEL_MARKET_PENETRATION_3 = "market_penetration_3";
    public static final String KEY_COMP_CHANNEL_COMP_COMPANY_NAME_4 = "competitor_company_name_4";
    public static final String KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_4 = "no_of_distributors_4";
    public static final String KEY_COMP_CHANNEL_NO_OF_RETAILERS_4 = "no_of_retailers_4";
    public static final String KEY_COMP_CHANNEL_MARKET_PENETRATION_4 = "market_penetration_4";
    public static final String KEY_COMP_CHANNEL_COMP_COMPANY_NAME_5 = "competitor_company_name_5";
    public static final String KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_5 = "no_of_distributors_5";
    public static final String KEY_COMP_CHANNEL_NO_OF_RETAILERS_5 = "no_of_retailers_5";
    public static final String KEY_COMP_CHANNEL_MARKET_PENETRATION_5 = "market_penetration_5";

    // Product and Pricing Survey Columns
    public static final String KEY_SURVEY_PRIMARY_ID = "id";
    public static final String KEY_SURVEY_FFM_ID = "ffm_id";
    public static final String KEY_SURVEY_USER_ID = "user_id";
    public static final String KEY_SURVEY_REGION_ID = "region_id";
    public static final String KEY_SURVEY_DISTRICT = "district";
    public static final String KEY_SURVEY_TALUKA = "taluka";
    public static final String KEY_SURVEY_VILLAGE = "village";
    public static final String KEY_SURVEY_DIVISION_ID = "division_id";
    public static final String KEY_SURVEY_DIVISION_NAME = "division_name";
    public static final String KEY_SURVEY_CROP_ID = "crop_id";
    public static final String KEY_SURVEY_CROP_NAME = "crop_name";
    public static final String KEY_SURVEY_COMPETITOR_COMPANY_NAME = "competitor_company_name";
    public static final String KEY_SURVEY_COMPETITOR_PRODUCT_NAME = "competitor_product_name";
    public static final String KEY_SURVEY_SEGMENT = "segment";
    public static final String KEY_SURVEY_SALE_QUANTITY = "sale_quantity";
    public static final String KEY_SURVEY_PACK_SIZE = "pack_size";
    public static final String KEY_SURVEY_DISTRIBUTOR_NET_LANDING_PRICE = "distributor_net_landing_price";
    public static final String KEY_SURVEY_COMPANY_BILLING_PRICE = "company_billing_price";
    public static final String KEY_SURVEY_DISTRIBUTOR_BILL_PRICE_RETAILER = "distributor_billing_price_to_retailer";
    public static final String KEY_SURVEY_FARMER_PRICE = "farmer_price";
    public static final String KEY_SURVEY_MRP = "mrp";
    public static final String KEY_SURVEY_LAST_YEAR_SALE_IN_VILLAGE = "last_year_sale_in_village";
    public static final String KEY_SURVEY_CURRENT_YEAR_SALE_IN_VILLAGE = "current_year_sale_in_village";
    public static final String KEY_SURVEY_NEXT_YEAR_ESTIMATED_SALE_IN_VILLAGE = "next_year_estimated_sale_in_village";

    // TargetActual Columns
   /* public static final String KEY_TARGET_ACTUAL_PRIMARY_ID = "id";
    public static final String KEY_TARGET_ACTUAL_CUSTOMER_ID = "customer_id";
    public static final String KEY_TARGET_ACTUAL_CUSTOMER_TARGET_ID = "customer_target_id";
    public static final String KEY_TARGET_ACTUAL_PRODUCT_NAME = "product_name";
    public static final String KEY_TARGET_ACTUAL_TARGET_QUANTITY = "target_quantity";
    public static final String KEY_TARGET_ACTUAL_ACTUAL_QUANTITY = "actual_quantity";
    public static final String KEY_TARGET_ACTUAL_SALE_PERCENTAGE = "sale_percentage";
    public static final String KEY_TARGET_ACTUAL_MONTH = "month";
    public static final String KEY_TARGET_ACTUAL_YEAR = "year";

    private static final String CREATE_TARGET_ACTUAL = "CREATE TABLE " + TABLE_TARGET_ACTUAL + "(" +
            KEY_TARGET_ACTUAL_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_TARGET_ACTUAL_CUSTOMER_ID + " TEXT," +
            KEY_TARGET_ACTUAL_CUSTOMER_TARGET_ID + " TEXT," +
            KEY_TARGET_ACTUAL_PRODUCT_NAME + " TEXT," +
            KEY_TARGET_ACTUAL_TARGET_QUANTITY + " INTEGER," +
            KEY_TARGET_ACTUAL_ACTUAL_QUANTITY + " INTEGER," +
            KEY_TARGET_ACTUAL_SALE_PERCENTAGE + " INTEGER," +
            KEY_TARGET_ACTUAL_MONTH + " INTEGER," +
            KEY_TARGET_ACTUAL_YEAR + " INTEGER" +
            ")";*/

    // Season Columns
    public static final String KEY_SEASON_PRIMARY_ID = "primary_id";
    public static final String KEY_SEASON_SEASON_ID = "season_master_id";
    public static final String KEY_SEASON_SEASON_NAME = "season_name";
    public static final String KEY_SEASON_STATUS = "status";


    public static final String KEY_SEASON_LINE_ITEM_DIVISION_ID = "division_id";
    public static final String KEY_SEASON_LINE_ITEM_VALID_FROM = "valid_from";
    public static final String KEY_SEASON_LINE_ITEM_VALID_TO = "valid_to";
    public static final String KEY_SEASON_LINE_ITEM_ID = "season_line_item_id";
    public static final String KEY_SEASON_LINE_ITEM_PRIMARY_ID = "primary_id";

    public static final String KEY_YE_PRIMARY_ID = "primary_id";
    public static final String KEY_YE_DIVISION_ID = "division_id";
    public static final String KEY_YE_CROP_ID = "crop_id";
    public static final String KEY_YE_PRODUCT_ID = "product_id";
    public static final String KEY_YE_AVG_NO_OF_BALLS_PLANT = "avg_no_balls_plant";
    public static final String KEY_YE_AVG_BALL_WEIGHT = "avg_ball_weight_in_gm";
    public static final String KEY_YE_ROW_ROW_DISTANCE = "row_to_row_distance_sqm";
    public static final String KEY_YE_PLANT_PLANT_DISTANCE = "plant_to_plant_distance_sqm";
    public static final String KEY_YE_AREA = "area_in_acres";
    public static final String KEY_YE_YIELD = "yeild_in_sqm";
    public static final String KEY_YE_RESULT = "result";
    public static final String KEY_YE_CREATED_BY = "created_by";

    public static final String KEY_GODOWN_PRIMARY_ID = "primary_id";
    public static final String KEY_GODOWN_ID = "godown_id";
    public static final String KEY_GODOWN_NAME = "godown_name";
    public static final String KEY_GODOWN_CODE = "godown_code";
    public static final String KEY_GODOWN_COMPANY_NAME = "company_name";
    public static final String KEY_GODOWN_REGION_NAME = "region_name";

    public static final String KEY_GODOWN_STOCK_PRIMARY_ID = "primary_id";
    public static final String KEY_GODOWN_STOCK_STOCK_GODOWN_ID = "stock_godown_id";
    public static final String KEY_GODOWN_STOCK_GODOWN_ID = "godown_id";
    public static final String KEY_GODOWN_STOCK_DIVISION_NAME = "division_name";
    public static final String KEY_GODOWN_STOCK_CROP_NAME = "crop_name";
    public static final String KEY_GODOWN_STOCK_PRODUCT_NAME = "product_name";
    public static final String KEY_GODOWN_STOCK_QUANTITY = "quantity";


    // stock dispatch columns
    public static final String KEY_STOCK_DISPATCH_PRIMARY_ID = "primary_id";
    public static final String KEY_STOCK_DISPATCH_STOCK_DISPATCH_ID = "stock_dispatch_id";
    public static final String KEY_STOCK_DISPATCH_COMPANY_ID = "company_id";
    public static final String KEY_STOCK_DISPATCH_ORDER_SAP_ID = "order_sap_id";
    public static final String KEY_STOCK_DISPATCH_SO_NO = "so_no";
    public static final String KEY_STOCK_DISPATCH_ORDER_ID = "order_id";
    public static final String KEY_STOCK_DISPATCH_USER_ID = "user_id";
    public static final String KEY_STOCK_DISPATCH_DISTRIBUTOR_SAP_ID = "distributor_sap_id";
    public static final String KEY_STOCK_DISPATCH_DISTRIBUTOR_ID = "distributor_id";
    public static final String KEY_STOCK_DISPATCH_DIVISION_ID = "division_id";
    public static final String KEY_STOCK_DISPATCH_DISPATCH_DATE = "dispatch_date";
    public static final String KEY_STOCK_DISPATCH_ORDER_CREATED_DATE = "order_created_date";
    public static final String KEY_STOCK_DISPATCH_STATUS = "status";
    public static final String KEY_STOCK_DISPATCH_FISCAL_YEAR = "fiscal_year";

    public static final String KEY_TABLE_CUSTOMER_LAT_LNG_ADDRESS = "lat_lng_address";
    // stock dispatch line items columns
    public static final String KEY_STOCK_DISPATCH_ITEM_PRIMARY_ID = "primary_id";
    public static final String KEY_STOCK_DISPATCH_ITEM_LINE_ITEM_ID = "stock_dispatch_line_item_id";
    public static final String KEY_STOCK_DISPATCH_ITEM_STOCK_DISPATCH_ID = "stock_dispatch_id";
    public static final String KEY_STOCK_DISPATCH_ITEM_CROP_ID = "crop_id";
    public static final String KEY_STOCK_DISPATCH_ITEM_PRODUCT_ID = "product_id";
    public static final String KEY_STOCK_DISPATCH_ITEM_QUANTITY = "quantity";
    public static final String KEY_STOCK_DISPATCH_ITEM_DISPATCH_DATE = "dispatch_date";
    public static final String KEY_STOCK_DISPATCH_ITEM_DELIVERED_DATE = "delivered_date";
    public static final String KEY_STOCK_DISPATCH_ITEM_PRICE = "price";
    public static final String KEY_STOCK_DISPATCH_ITEM_STATUS = "status";
    public static final String KEY_STOCK_DISPATCH_ITEM_ORDER_SAP_ID = "order_sap_id";

    // retailer stock supply columns
    public static final String KEY_RETAILER_SS_PRIMARY_ID = "primary_id";
    public static final String KEY_RETAILER_SS_STOCK_SUPPLY_ID = "stock_supply_id";
    public static final String KEY_RETAILER_SS_USER_ID = "user_id";
    public static final String KEY_RETAILER_SS_DISTRIBUTOR_ID = "distributor_id";
    public static final String KEY_RETAILER_SS_RETAILER_ID = "retailer_id";
    public static final String KEY_RETAILER_SS_CROP_ID = "crop_id";
    public static final String KEY_RETAILER_SS_PRODUCT_ID = "product_id";
    public static final String KEY_RETAILER_SS_SUPPLIED_DATE = "supplied_date";
    public static final String KEY_RETAILER_SS_QUANTITY = "quantity";
    public static final String KEY_RETAILER_SS_STATUS = "status";
    public static final String KEY_RETAILER_SS_USER_TYPE = "user_type ";

    public static final String TABLE_DEMANDGENERATION= "demand_generation";
    // demand generation stock supply columns
    public static final String KEY_DEMANDGENERATION_PRIMARY_ID = "dg_id";
    public static final String KEY_DEMANDGENERATION_EVENT = "event";
    public static final String KEY_DEMANDGENERATION_ADDRESS = "address";
    public static final String KEY_DEMANDGENERATION_DATEOFEVENT = "dateofevent";
    public static final String KEY_DEMANDGENERATION_NOOFFARMERS = "formers";
    public static final String KEY_DEMANDGENERATION_STATUS="status";
    public static final String KEY_DEMANDGENERATION_PlANNEDDATE="planneddate";


    public static final String TABLE_COUPANS= "farmer_coupon_code_list";
    // demand generation stock supply columns
    public static final String KEY_FARMER_COUPAN_PRIMARY_ID = "cp_id";
    public static final String KEY_FARMER_COUPAN_ID = "farmer_coupon_id";
    public static final String KEY_FARMER_USER_ID = "user_id";
    public static final String KEY_FARMER_UNIQUE_NO = "unique_no";
    public static final String KEY_FARMER_POINTS = "points";
    public static final String KEY_FARMER_REGION_ID = "region_id";
    public static final String KEY_FARMER_COMPANY_ID="company_id";
    public static final String KEY_FARMER_COUPAN_TYPE="coupon_type";
    public static final String KEY_FARMER_STATUS= "status";
    public static final String KEY_FARMER_NAME = "farmer_name";
    public static final String KEY_FARMER_EMAIL = "farmer_email";
    public static final String KEY_FARMER_MOBILE = "farmer_mobile";
    public static final String KEY_FARMER_FARMER_ADHAR_NO = "farmer_adhar_no";
    public static final String KEY_FARMER_IMAGE="image";
    public static final String KEY_FARMER_CROP="crop";

    public static final String KEY_FARMER_LOCATION = "location";
    public static final String KEY_FARMER_ADDRESS="address";
    public static final String KEY_FARMER_SYNC_STATUS="sync_status";
    public static final String KEY_FARMER_CREATED_DATETIME="created_datetime";
    public static final String KEY_FARMER_UPDATED_DATETIME="updated_datetime";

    public static final String KEY_FARMER_VILLAGE="village";
    public static final String KEY_FARMER_THALUKA="thaluka";
    public static final String KEY_FARMER_SERVERSTATUS="server_status";


    //*Tfa
    public static final String TABLE_TFA_APPROVAL_HISTORY = "tfa_approval_history";
    // table companies column names
    public static final String KEY_tfa_approval_id = "tfa_approval_id";
    public static final String KEY_tfa_approval_status = "approval_status";
    public static final String KEY_tfa_approval_comment = "approval_comment";
    public static final String KEY_tfa_approved_user_id = "approved_user_id";
    public static final String KEY_tfa_approval_name = "name";
    public static final String KEY_tfa_approval_mail = "mail";
    public static final String KEY_tfa_pending_by_name = "pending_by_name";
    public static final String KEY_tfa_pending_by_role = "pending_by_role";
    public static final String KEY_tfa_approval_role = "role";
    public static final String KEY_tfa_approval_mobile = "pnno";



    public static final String TABLE_TFA_ACTIVITYLIST = "tfa_activity_list";
    // table companies column names
    public static final String KEY_tfa_list_id = "tfa_list_id";
    public static final String KEY_district_id = "district_id";
    public static final String KEY_division_id = "division_id";
    public static final String KEY_crop_id = "crop_id";
    public static final String KEY_product_id = "product_id";
    public static final String KEY_tfa_activity_master_id = "tfa_activity_master_id";
    public static final String KEY_activity_date = "activity_date";
    public static final String KEY_taluka= "taluka";
    public static final String KEY_village = "village";
    public static final String KEY_no_of_farmers = "no_of_farmers";
    public static final String KEY_estimation_per_head = "estimation_per_head";
    public static final String KEY_total_expences= "total_expences";
    public static final String KEY_advance_required = "advance_required";
    public static final String KEY_conducting_place = "conducting_place";
    public static final String KEY_user_id = "user_id";
    public static final String KEY_created_user_id = "created_user_id";
    public static final String KEY_user_email= "user_email";
    public static final String KEY_status = "status";

    public static final String KEY_approval_status = "approval_status";
    public static final String KEY_approval_comments= "approval_comments";
    public static final String KEY_approved_by= "approved_by";
    public static final String KEY_approved_date= "approved_date";
    public static final String KEY_used_farmers= "used_farmers";
    public static final String KEY_non_used_farmers= "non_used_farmers";
    public static final String KEY_actual_no_farmers= "actual_no_farmers";
    public static final String KEY_actual__estimation_per_head= "actual__estimation_per_head";
    public static final String KEY_actual__total_expences= "actual_total_expences";
    public static final String KEY_location_lat_lng= "location_lat_lng";
    public static final String KEY_owner_number= "owner_number";
    public static final String KEY_owner_name= "owner_name";

    public static final String KEY_sync_status = "sync_status";
    public static final String KEY_created_datetime= "created_datetime";
    public static final String KEY_updated_datetime= "updated_datetime";


    public static final String TABLE_TFA_VILLAGELIST = "tfa_village_list";
    // table companies column names
    public static final String KEY_tfa_village_id = "tfa_village_id";
    public static final String KEY_village_name = "village_name";
    public static final String KEY_current_sal = "current_sal";
    public static final String KEY_potential = "potential";

    public static final String TABLE_TFA_ACTIVITY_MASTER="tfa_activity_master";
    public static final String KEY_tfa_master_id = "tfa_master_id";
    public static final String KEY_tfa_title = "tfa_title";


    private static final String CREATE_RETAILER_STOCK_SUPPLY = "CREATE TABLE " + TABLE_RETAILER_STOCK_SUPPLY + "(" +
            KEY_RETAILER_SS_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_RETAILER_SS_STOCK_SUPPLY_ID + " TEXT," +
            KEY_RETAILER_SS_USER_ID + " TEXT," +
            KEY_RETAILER_SS_DISTRIBUTOR_ID + " TEXT," +
            KEY_RETAILER_SS_RETAILER_ID + " TEXT," +
            KEY_RETAILER_SS_CROP_ID + " TEXT," +
            KEY_RETAILER_SS_PRODUCT_ID + " TEXT," +
            KEY_RETAILER_SS_SUPPLIED_DATE + " TEXT," +
            KEY_RETAILER_SS_QUANTITY + " TEXT," +
            KEY_RETAILER_SS_STATUS + " TEXT," +
            KEY_RETAILER_SS_USER_TYPE + " TEXT" + ")";

    private static final String CREATE_STOCK_DISPATCH_ITEM = "CREATE TABLE " + TABLE_STOCK_DISPATCH_ITEM + "(" +
            KEY_STOCK_DISPATCH_ITEM_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_STOCK_DISPATCH_ITEM_LINE_ITEM_ID + " TEXT," +
            KEY_STOCK_DISPATCH_ITEM_STOCK_DISPATCH_ID + " TEXT," +
            KEY_STOCK_DISPATCH_ITEM_CROP_ID + " TEXT," +
            KEY_STOCK_DISPATCH_ITEM_PRODUCT_ID + " TEXT," +
            KEY_STOCK_DISPATCH_ITEM_QUANTITY + " TEXT," +
            KEY_STOCK_DISPATCH_ITEM_DISPATCH_DATE + " TEXT," +
            KEY_STOCK_DISPATCH_ITEM_DELIVERED_DATE + " TEXT," +
            KEY_STOCK_DISPATCH_ITEM_PRICE + " TEXT," +
            KEY_STOCK_DISPATCH_ITEM_STATUS + " TEXT,"
            + KEY_STOCK_DISPATCH_ITEM_ORDER_SAP_ID + " TEXT" + ")";

    private static final String CREATE_STOCK_DISPATCH = "CREATE TABLE " + TABLE_STOCK_DISPATCH + "(" +
            KEY_STOCK_DISPATCH_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_STOCK_DISPATCH_STOCK_DISPATCH_ID + " TEXT," +
            KEY_STOCK_DISPATCH_COMPANY_ID + " TEXT," +
            KEY_STOCK_DISPATCH_ORDER_SAP_ID + " TEXT," +
            KEY_STOCK_DISPATCH_SO_NO + " TEXT," +
            KEY_STOCK_DISPATCH_ORDER_ID + " TEXT," +
            KEY_STOCK_DISPATCH_USER_ID + " TEXT," +
            KEY_STOCK_DISPATCH_DISTRIBUTOR_SAP_ID + " TEXT," +
            KEY_STOCK_DISPATCH_DISTRIBUTOR_ID + " TEXT," +
            KEY_STOCK_DISPATCH_DIVISION_ID + " TEXT," +
            KEY_STOCK_DISPATCH_DISPATCH_DATE + " TEXT," +
            KEY_STOCK_DISPATCH_ORDER_CREATED_DATE + " TEXT," +
            KEY_STOCK_DISPATCH_STATUS + " TEXT,"
            + KEY_STOCK_DISPATCH_FISCAL_YEAR + " TEXT" + ")";

    private static final String CREATE_GODOWN_STOCK = "CREATE TABLE " + TABLE_GODOWN_STOCK + "(" +
            KEY_GODOWN_STOCK_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_GODOWN_STOCK_STOCK_GODOWN_ID + " TEXT," +
            KEY_GODOWN_STOCK_GODOWN_ID + " TEXT," +
            KEY_GODOWN_STOCK_DIVISION_NAME + " TEXT," +
            KEY_GODOWN_STOCK_CROP_NAME + " TEXT," +
            KEY_GODOWN_STOCK_PRODUCT_NAME + " TEXT," +
            KEY_GODOWN_STOCK_QUANTITY + " TEXT" + ")";

    private static final String CREATE_GODOWN = "CREATE TABLE " + TABLE_GODOWN + "(" +
            KEY_GODOWN_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_GODOWN_ID + " TEXT," +
            KEY_GODOWN_NAME + " TEXT," +
            KEY_GODOWN_CODE + " TEXT," +
            KEY_GODOWN_COMPANY_NAME + " TEXT," +
            KEY_GODOWN_REGION_NAME + " TEXT" + ")";

    private static final String CREATE_YIELD_ESTIMATION = "CREATE TABLE " + TABLE_YE + "(" +
            KEY_YE_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_YE_DIVISION_ID + " TEXT," +
            KEY_YE_CROP_ID + " TEXT," +
            KEY_YE_PRODUCT_ID + " TEXT," +
            KEY_YE_AVG_NO_OF_BALLS_PLANT + " TEXT," +
            KEY_YE_AVG_BALL_WEIGHT + " TEXT," +
            KEY_YE_ROW_ROW_DISTANCE + " TEXT," +
            KEY_YE_PLANT_PLANT_DISTANCE + " TEXT," +
            KEY_YE_AREA + " TEXT," +
            KEY_YE_YIELD + " TEXT," +
            KEY_YE_RESULT + " TEXT," +
            KEY_YE_CREATED_BY + " TEXT" +
            ")";


    private static final String CREATE_SEASON = "CREATE TABLE " + TABLE_SEASON + "(" +
            KEY_SEASON_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_SEASON_SEASON_ID + " TEXT," +
            KEY_SEASON_SEASON_NAME + " TEXT," +
            KEY_SEASON_STATUS + " TEXT" + ")";

    private static final String CREATE_SEASON_LINE_ITEMS = "CREATE TABLE " + TABLE_SEASON_LINE_ITEMS + "(" +
            KEY_SEASON_LINE_ITEM_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_SEASON_LINE_ITEM_ID + " TEXT," +
            KEY_SEASON_SEASON_ID + " TEXT," +
            KEY_SEASON_LINE_ITEM_DIVISION_ID + " TEXT," +
            KEY_SEASON_LINE_ITEM_VALID_FROM + " TEXT," +
            KEY_SEASON_LINE_ITEM_VALID_TO + " TEXT" + ")";

    private static final String CREATE_PRODUCT_PRICING_SURVEY = "CREATE TABLE " + TABLE_PRODUCT_PRICING_SURVEY + "(" +
            KEY_SURVEY_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_SURVEY_FFM_ID + " TEXT," +
            KEY_SURVEY_USER_ID + " INTEGER," +
            KEY_SURVEY_REGION_ID + " TEXT," +
            KEY_SURVEY_DISTRICT + " TEXT," +
            KEY_SURVEY_TALUKA + " TEXT," +
            KEY_SURVEY_VILLAGE + " TEXT," +
            KEY_SURVEY_DIVISION_ID + " INTEGER," +
            KEY_SURVEY_DIVISION_NAME + " TEXT," +
            KEY_SURVEY_CROP_ID + " INTEGER," +
            KEY_SURVEY_CROP_NAME + " TEXT," +
            KEY_SURVEY_COMPETITOR_COMPANY_NAME + " TEXT," +
            KEY_SURVEY_COMPETITOR_PRODUCT_NAME + " TEXT," +
            KEY_SURVEY_SEGMENT + " TEXT," +
            KEY_SURVEY_SALE_QUANTITY + " TEXT," +
            KEY_SURVEY_PACK_SIZE + " TEXT," +
            KEY_SURVEY_DISTRIBUTOR_NET_LANDING_PRICE + " TEXT," +
            KEY_SURVEY_COMPANY_BILLING_PRICE + " TEXT," +
            KEY_SURVEY_DISTRIBUTOR_BILL_PRICE_RETAILER + " TEXT," +
            KEY_SURVEY_FARMER_PRICE + " TEXT," +
            KEY_SURVEY_MRP + " TEXT," +
            KEY_SURVEY_LAST_YEAR_SALE_IN_VILLAGE + " TEXT," +
            KEY_SURVEY_CURRENT_YEAR_SALE_IN_VILLAGE + " TEXT," +
            KEY_SURVEY_NEXT_YEAR_ESTIMATED_SALE_IN_VILLAGE + " TEXT" +
            ")";

    public static final String KEY_CROP_SHIFTS_PRIMARY_ID = "id";
    public static final String KEY_CROP_SHIFTS_USER_ID = "user_id";
    public static final String KEY_CROP_SHIFTS_DISTRICT = "district";
    public static final String KEY_CROP_SHIFTS_TALUKA = "taluka";
    public static final String KEY_CROP_SHIFTS_VILLAGE = "village";
    public static final String KEY_CROP_SHIFTS_DIVISION_ID = "division_id";
    public static final String KEY_CROP_SHIFTS_DIVISION_NAME = "division_name";
    public static final String KEY_CROP_SHIFTS_CROP_ID = "crop_id";
    public static final String KEY_CROP_SHIFTS_SEGMENT = "segment";
    public static final String KEY_CROP_SHIFTS_CURRENT_YEAR_AREA = "current_year_area_in_acres";
    public static final String KEY_CROP_SHIFTS_NEXT_YEAR_ESTIMATED_SRR = "next_year_estimated_srr";
    public static final String KEY_CROP_SHIFTS_CREATED_DATE_TIME = "created_datetime";
    public static final String KEY_CROP_SHIFTS_UPDATED_DATE_TIME = "updated_datetime";

    private static final String CREATE_CROP_SHIFTS = "CREATE TABLE " + TABLE_CROP_SHIFTS + "(" +
            KEY_CROP_SHIFTS_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_CROP_SHIFTS_FFMID + " TEXT," +
            KEY_CROP_SHIFTS_USER_ID + " TEXT," +
            KEY_CROP_SHIFTS_DISTRICT + " TEXT," +
            KEY_CROP_SHIFTS_TALUKA + " TEXT," +
            KEY_CROP_SHIFTS_VILLAGE + " TEXT," +
            KEY_CROP_SHIFTS_DIVISION_ID + " TEXT," +
            KEY_CROP_SHIFTS_DIVISION_NAME + " TEXT," +
            KEY_CROP_SHIFTS_CROP_ID + " TEXT," +
            KEY_CROP_SHIFTS_CROP_NAME + " TEXT," +
            KEY_CROP_SHIFTS_SEGMENT + " TEXT," +
            KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA + " TEXT," +
            KEY_CROP_SHIFTS_CURRENT_YEAR_AREA + " TEXT," +
            KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE + " TEXT," +
            KEY_CROP_SHIFTS_REASON_CROP_SHIFT + " TEXT," +
            KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR + " TEXT," +
            KEY_CROP_SHIFTS_CURRENT_YEAR_SRR + " TEXT," +
            KEY_CROP_SHIFTS_NEXT_YEAR_ESTIMATED_SRR + " TEXT," +
            KEY_CROP_SHIFTS_CREATED_DATE_TIME + " TEXT," +
            KEY_CROP_SHIFTS_UPDATED_DATE_TIME + " TEXT" + ")";

    public static final String KEY_COMPETITOR_STRENGTH_PRIMARY_ID = "id";
    public static final String KEY_COMPETITOR_STRENGTH_USER_ID = "user_id";
    public static final String KEY_COMPETITOR_STRENGTH_FFM_ID = "ffm_id";
    public static final String KEY_COMPETITOR_STRENGTH_DISTRICT = "district";
    public static final String KEY_COMPETITOR_STRENGTH_TERRITORY = "territory";
    public static final String KEY_COMPETITOR_STRENGTH_COMPANY_NAME = "competitor_company_name";
    public static final String KEY_COMPETITOR_STRENGTH_BUSINESS_COVERING_VILLAGES = "business_covering_villages";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_PRODUCTS_SOLD = "no_of_products_sold";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_FARMER_CLUBS = "no_of_farmer_clubs";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_DEMO_PLOTS = "no_of_demo_plots";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_TEMPORARY_BOYS = "no_of_temporary_fas_counter_boys";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_PERMANENT_FA = "no_of_permanent_fa";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_COMPANY_STAFF = "no_of_company_staff";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_FDS_CONDUCTED = "no_of_fds_conducted";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_MFDS_CONDUCTED = "no_of_mfds_conducted";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_NEW_PRODUCT_MINIKIT = "no_of_new_product_minikit_trail_plots";
    public static final String KEY_COMPETITOR_STRENGTH_NO_OF_WORKSHOPS_CONDUCTED = "no_of_workshops_conducted";
    private static final String CREATE_COMPETITOR_STRENGTH = "CREATE TABLE " + TABLE_COMPETITOR_STRENGTH + "(" +
            KEY_COMPETITOR_STRENGTH_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_COMPETITOR_STRENGTH_USER_ID + " TEXT," +
            KEY_COMPETITOR_STRENGTH_FFM_ID + " TEXT," +
            KEY_COMPETITOR_STRENGTH_DISTRICT + " TEXT," +
            KEY_COMPETITOR_STRENGTH_TERRITORY + " TEXT," +
            KEY_COMPETITOR_STRENGTH_COMPANY_NAME + " TEXT," +
            KEY_COMPETITOR_STRENGTH_BUSINESS_COVERING_VILLAGES + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_PRODUCTS_SOLD + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_FARMER_CLUBS + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_DEMO_PLOTS + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_TEMPORARY_BOYS + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_PERMANENT_FA + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_COMPANY_STAFF + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_FDS_CONDUCTED + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_MFDS_CONDUCTED + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_NEW_PRODUCT_MINIKIT + " TEXT," +
            KEY_COMPETITOR_STRENGTH_NO_OF_WORKSHOPS_CONDUCTED + " TEXT" + ")";

    public static final String KEY_CP_PRIMARY_ID = "id";
    public static final String KEY_CP_FFM_ID = "ffm_id";
    public static final String KEY_CP_USER_ID = "user_id";
    public static final String KEY_CP_REGION_ID = "region_id";
    public static final String KEY_CP_DISTRICT = "district";
    public static final String KEY_CP_TALUKA = "taluka";
    public static final String KEY_CP_VILLAGE = "village";
    public static final String KEY_CP_DIVISION_ID = "division_id";
    public static final String KEY_CP_DIVISION_NAME = "division_name";
    public static final String KEY_CP_CROP_ID = "crop_id";
    public static final String KEY_CP_CROP_NAME = "crop_name";
    public static final String KEY_CP_SEGMENT = "segment";
    public static final String KEY_CP_APMC_MANDI_PRICE = "apmc_mandi_price";
    public static final String KEY_CP_COMMODITY_DEALER_PRICE = "commodity_dealer_commission_agent_price";
    public static final String KEY_CP_PURCHASE_PRICE_BY_INDUSTRY = "purchase_price_by_industry";
    public static final String KEY_CP_CREATED_DATETIME = "created_datetime";
    public static final String KEY_CP_UPDATED_DATETIME = "updated_datetime";
    public static final String CREATE_COMMODITY_PRICE = "CREATE TABLE " + TABLE_COMMODITY_PRICE + "(" +
            KEY_CP_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_CP_FFM_ID + " TEXT," +
            KEY_CP_USER_ID + " TEXT," +
            KEY_CP_REGION_ID + " TEXT," +
            KEY_CP_DISTRICT + " TEXT," +
            KEY_CP_TALUKA + " TEXT," +
            KEY_CP_VILLAGE + " TEXT," +
            KEY_CP_DIVISION_ID + " TEXT," +
            KEY_CP_DIVISION_NAME + " TEXT," +
            KEY_CP_CROP_ID + " TEXT," +
            KEY_CP_CROP_NAME + " TEXT," +
            KEY_CP_SEGMENT + " TEXT," +
            KEY_CP_APMC_MANDI_PRICE + " TEXT," +
            KEY_CP_COMMODITY_DEALER_PRICE + " TEXT," +
            KEY_CP_PURCHASE_PRICE_BY_INDUSTRY + " TEXT," +
            KEY_CP_CREATED_DATETIME + " TEXT," +
            KEY_CP_UPDATED_DATETIME + " TEXT" + ")";

    private static final String CREATE_MARKET_POTENTIAL = "CREATE TABLE " + TABLE_MARKET_POTENTIAL + "(" +
            KEY_MARKET_POTENTIAL_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_MARKET_POTENTIAL_FFM_ID + " TEXT," +
            KEY_MARKET_POTENTIAL_USER_ID + " INTEGER," +
            KEY_MARKET_POTENTIAL_REGION_ID + " TEXT," +
            KEY_MARKET_POTENTIAL_DISTRICT + " TEXT," +
            KEY_MARKET_POTENTIAL_TALUKA + " TEXT," +
            KEY_MARKET_POTENTIAL_VILLAGE + " TEXT," +
            KEY_MARKET_POTENTIAL_DIVISION_ID + " INTEGER," +
            KEY_MARKET_POTENTIAL_CROP_ID + " INTEGER," +
            KEY_MARKET_POTENTIAL_KHARIF_CROP_ACREAGE + " TEXT," +
            KEY_MARKET_POTENTIAL_RABI_CROP_ACREAGE + " TEXT," +
            KEY_MARKET_POTENTIAL_SUMMER_CROP_ACREAGE + " TEXT," +
            KEY_MARKET_POTENTIAL_TOTAL_POTENTIAL_ACREAGE + " TEXT," +
            KEY_MARKET_POTENTIAL_SEED_USAGE_QUANTITY_ACRE + " TEXT," +
            KEY_MARKET_POTENTIAL_TOTAL_MARKET_POTENTIAL_VOLUME + " TEXT," +
            KEY_MARKET_POTENTIAL_NSL_SALE + " TEXT," +
            KEY_MARKET_POTENTIAL_TOP_COMPANY_1_NAME + " TEXT," +
            KEY_MARKET_POTENTIAL_COMPANY_1_QUANTITY + " TEXT," +
            KEY_MARKET_POTENTIAL_TOP_COMPANY_2_NAME + " TEXT," +
            KEY_MARKET_POTENTIAL_COMPANY_2_QUANTITY + " TEXT," +
            KEY_MARKET_POTENTIAL_TOP_COMPANY_3_NAME + " TEXT," +
            KEY_MARKET_POTENTIAL_COMPANY_3_QUANTITY + " TEXT," +
            KEY_MARKET_POTENTIAL_TOP_COMPANY_4_NAME + " TEXT," +
            KEY_MARKET_POTENTIAL_COMPANY_4_QUANTITY + " TEXT," +
            KEY_MARKET_POTENTIAL_TOP_COMPANY_5_NAME + " TEXT," +
            KEY_MARKET_POTENTIAL_COMPANY_5_QUANTITY + " TEXT," +
            KEY_MARKET_POTENTIAL_DIVISION_NAME + " TEXT," +
            KEY_MARKET_POTENTIAL_CROP_NAME + " TEXT" +
            ")";


    public static final String KEY_CHANNEL_PREF_PRIMARY_ID = "id";
    public static final String KEY_CHANNEL_PREF_FFM_ID = "ffm_id";
    public static final String KEY_CHANNEL_PREF_USER_ID = "user_id";
    public static final String KEY_CHANNEL_PREF_DISTRIBUTOR_ID = "distributor_id";
    public static final String KEY_CHANNEL_PREF_CROP_ID = "crop_id";
    public static final String KEY_CHANNEL_PREF_CROP_NAME = "crop_name";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_1 = "company_name_1";
    public static final String KEY_CHANNEL_PREF_COMP1_TURNOVER = "company_1_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_2 = "company_name_2";
    public static final String KEY_CHANNEL_PREF_COMP2_TURNOVER = "company_2_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_3 = "company_name_3";
    public static final String KEY_CHANNEL_PREF_COMP3_TURNOVER = "company_3_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_4 = "company_name_4";
    public static final String KEY_CHANNEL_PREF_COMP4_TURNOVER = "company_4_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_5 = "company_name_5";
    public static final String KEY_CHANNEL_PREF_COMP5_TURNOVER = "company_5_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_6 = "company_name_6";
    public static final String KEY_CHANNEL_PREF_COMP6_TURNOVER = "company_6_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_7 = "company_name_7";
    public static final String KEY_CHANNEL_PREF_COMP7_TURNOVER = "company_7_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_8 = "company_name_8";
    public static final String KEY_CHANNEL_PREF_COMP8_TURNOVER = "company_8_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_9 = "company_name_9";
    public static final String KEY_CHANNEL_PREF_COMP9_TURNOVER = "company_9_turnover";
    public static final String KEY_CHANNEL_PREF_COMP_NAME_10 = "company_name_10";
    public static final String KEY_CHANNEL_PREF_COMP10_TURNOVER = "company_10_turnover";
    private static final String CREATE_CHANNEL_PREFERENCE = "CREATE TABLE " + TABLE_CHANNEL_PREFERENCE + "(" +
            KEY_CHANNEL_PREF_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_CHANNEL_PREF_FFM_ID + " TEXT," +
            KEY_CHANNEL_PREF_USER_ID + " TEXT," +
            KEY_CHANNEL_PREF_DISTRIBUTOR_ID + " TEXT," +
            KEY_CHANNEL_PREF_CROP_ID + " TEXT," +
            KEY_CHANNEL_PREF_CROP_NAME + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_1 + " TEXT," +
            KEY_CHANNEL_PREF_COMP1_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_2 + " TEXT," +
            KEY_CHANNEL_PREF_COMP2_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_3 + " TEXT," +
            KEY_CHANNEL_PREF_COMP3_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_4 + " TEXT," +
            KEY_CHANNEL_PREF_COMP4_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_5 + " TEXT," +
            KEY_CHANNEL_PREF_COMP5_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_6 + " TEXT," +
            KEY_CHANNEL_PREF_COMP6_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_7 + " TEXT," +
            KEY_CHANNEL_PREF_COMP7_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_8 + " TEXT," +
            KEY_CHANNEL_PREF_COMP8_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_9 + " TEXT," +
            KEY_CHANNEL_PREF_COMP9_TURNOVER + " TEXT," +
            KEY_CHANNEL_PREF_COMP_NAME_10 + " TEXT," +
            KEY_CHANNEL_PREF_COMP10_TURNOVER + " TEXT" + ")";

    private static final String CREATE_COMPETITOR_CHANNEL = "CREATE TABLE " + TABLE_COMPETITOR_CHANNEL + "(" +
            KEY_COMP_CHANNEL_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_COMP_CHANNEL_FFM_ID + " TEXT," +
            KEY_COMP_CHANNEL_USER_ID + " INTEGER," +
            KEY_COMP_CHANNEL_REGION_ID + " TEXT," +
            KEY_COMP_CHANNEL_DISTRICT + " TEXT," +
            KEY_COMP_CHANNEL_TERRITORY + " TEXT," +
            KEY_COMP_CHANNEL_TOTAL_NO_OF_VILLAGES + " TEXT," +
            KEY_COMP_CHANNEL_TOTAL_NO_OF_DISTRIBUTORS + " TEXT," +
            KEY_COMP_CHANNEL_TOTAL_NO_OF_RETAILERS + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_NSL_VILLAGES + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_NSL_DISTRIBUTORS + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_NSL_RETAILERS + " TEXT," +
            KEY_COMP_CHANNEL_COMP_COMPANY_NAME_1 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_1 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_RETAILERS_1 + " TEXT," +
            KEY_COMP_CHANNEL_MARKET_PENETRATION_1 + " TEXT," +
            KEY_COMP_CHANNEL_COMP_COMPANY_NAME_2 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_2 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_RETAILERS_2 + " TEXT," +
            KEY_COMP_CHANNEL_MARKET_PENETRATION_2 + " TEXT," +
            KEY_COMP_CHANNEL_COMP_COMPANY_NAME_3 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_3 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_RETAILERS_3 + " TEXT," +
            KEY_COMP_CHANNEL_MARKET_PENETRATION_3 + " TEXT," +
            KEY_COMP_CHANNEL_COMP_COMPANY_NAME_4 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_4 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_RETAILERS_4 + " TEXT," +
            KEY_COMP_CHANNEL_MARKET_PENETRATION_4 + " TEXT," +
            KEY_COMP_CHANNEL_COMP_COMPANY_NAME_5 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_5 + " TEXT," +
            KEY_COMP_CHANNEL_NO_OF_RETAILERS_5 + " TEXT," +
            KEY_COMP_CHANNEL_MARKET_PENETRATION_5 + " TEXT" +
            ")";
    private static final String CREATE_DISTRICT = "CREATE TABLE " + TABLE_DISTRICT + "(" +
            KEY_DISTRICT_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_DISTRICT_DISTRICT_ID + " TEXT," +
            KEY_DISTRICT_REGION_ID + " TEXT," +
            KEY_DISTRICT_DISTRICT_NAME + " TEXT," +
            KEY_DISTRICT_STATUS + " TEXT," +
            KEY_DISTRICT_CREATED_BY + " TEXT," +
            KEY_DISTRICT_MODIFIED_BY + " TEXT," +
            KEY_DISTRICT_C_DATE_TIME + " TEXT," +
            KEY_DISTRICT_U_DATE_TIME + " TEXT" +
            ")";


    private static final String CREATE_SERVICE_ORDER_APPROVAL = "CREATE TABLE " + TABLE_SERVICE_ORDER_APPROVAL + "(" +
            KEY_SOA_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_SOA_SERVICE_ORDER_APPROVAL_ID + " TEXT," +
            KEY_SOA_ORDER_ID + " TEXT," +
            KEY_SOA_USER_ID + " TEXT," +
            KEY_SOA_ASSIGNED_USER_ID + " TEXT," +
            KEY_SOA_ORDER_STATUS + " TEXT," +
            KEY_SOA_CREATED_BY + " TEXT," +
            KEY_SOA_MODIFIED_BY + " TEXT," +
            KEY_SOA_C_DATE_TIME + " TEXT," +
            KEY_SOA_U_DATE_TIME + " TEXT," +
            KEY_SOA_SYNC_STATUS + " TEXT," +
            KEY_SOA_PENDING_BY + " TEXT" +
            ")";

    /*private static final String CREATE_PLANNER_APPROVAL = "CREATE TABLE " + TABLE_PLANNER_APPROVAL + "(" + KEY_PA_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_PA_PLANNER_APPROVAL_ID + " TEXT," +
            KEY_PA_EMP_VISIT_ID + " TEXT," +
            KEY_PA_ASSIGNED_USER_ID + " TEXT," +
            KEY_PA_USER_ID + " TEXT," +
            KEY_PA_PLANNER_STATUS + " TEXT," +
            KEY_PA_SYNC_STATUS + " TEXT," +
            KEY_PA_CREATED_BY + " TEXT," +
            KEY_PA_MODIFIED_BY + " TEXT," +
            KEY_PA_C_DATE_TIME + " TEXT," +
            KEY_PA_U_DATE_TIME + " TEXT" +
            ")";*/

    private static final String CREATE_CATALOGUE_CROP = "CREATE TABLE " + TABLE_CATALOGUE_CROPS + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CROP_NAME + " TEXT," +
            CROP_IMG_PATH + " TEXT," +
            SERVER_PK + " INTEGER," +
            STATUS + " INTEGER," +
            VERSION + " TEXT," +
            IMG_URI + " TEXT," +
            DATE_TIME + " TEXT" +
            ")";

    private static final String CREATE_CATALOGUE_CROP_PRODUCTS = "CREATE TABLE " + TABLE_CATALOGUE_CROPS_PRODUCTS + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CROP_RELATION_ID + " INTEGER," +
            SERVER_PK + " INTEGER," +
            PRODUCT_NAME + " TEXT," +
            PRODUCT_IMG + " TEXT," +
            DESCRIPTION + " BLOB," +
            STATUS + " INTEGER," +
            VERSION + " TEXT," +
            IMG_URI + " TEXT," +
            DATE_TIME + " TEXT" +

            ")";


    private static final String CREATE_TABLE_STOCK_RETURNS = "CREATE TABLE "
            + TABLE_STOCK_RETURNS + "(" + KEY_STOCK_RETURNS_ID + " INTEGER PRIMARY KEY,"
            + KEY_COMPANY_ID + " TEXT,"
            + KEY_DIVISION_ID + " TEXT,"
            + KEY_USER_ID + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_CREATED_DATETIME + " DATETIME,"
            + KEY_UPDATED_DATETIME + " DATETIME,"
            + KEY_FFMID + " TEXT"
            + ")";

    private static final String CREATE_TABLE_STOCK_RETURNS_DETAILS = "CREATE TABLE "
            + TABLE_STOCK_RETURNS_DETAILS + "(" + KEY_STOCK_RETURNS_DETAILS_ID + " INTEGER PRIMARY KEY,"
            + KEY_STOCK_RETURNS_ID + " TEXT,"
            + KEY_CROP_ID + " TEXT,"
            + KEY_STOCK_RETURNS_DETAILS_PRODUCT_ID + " TEXT,"
            + KEY_QUANTITY + " TEXT,"
            + KEY_FFMID + " TEXT"
            + ")";


    // Table Create Statements
    // ROUTE ATH table create statement
    String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_PH_NO + " TEXT" + ")";

    // Table Create Statements
    // ROUTE ATH table create statement
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_PH_NO + " TEXT" + ")";
    // COMPANIES table create statement
    private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE "
            + TABLE_COMPANIES + "(" + KEY_TABLE_COMPANIES_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_COMPANIES_NAME + " TEXT,"
            + KEY_TABLE_COMPANIES_MASTER_ID + " TEXT,"
            + KEY_TABLE_COMPANIES_COMPANY_SAP_ID + " TEXT,"
            + KEY_TABLE_COMPANIES_COMPANY_CODE + " TEXT,"
            + KEY_TABLE_COMPANIES_COMPANY_STATUS + " TEXT,"
            + KEY_TABLE_COMPANIES_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_COMPANIES_UPDATED_DATETIME + " DATETIME" + ")";

    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS = "status";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER__MAP_START_DATE = "map_start_date";
    public static final String KEY_TABLE_USER_COMPANY_CUSTOMER_UNMAP_DATE = "unmap_date";
    // COMPANY_CROP table create statement
    private static final String CREATE_TABLE_USER_COMPANY_CUSTOMER = "CREATE TABLE "
            + TABLE_USER_COMPANY_CUSTOMER + "("
            + KEY_TABLE_USER_COMPANY_CUSTOMER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_MASTER_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " TEXT DEFAULT 1,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER__MAP_START_DATE + " TEXT,"
            + KEY_TABLE_USER_COMPANY_CUSTOMER_UNMAP_DATE + " TEXT"

            + ")";


    // SCHEME_PRODUCTS table create statement
    private static final String CREATE_TABLE_SCHEME_PRODUCTS = "CREATE TABLE "
            + TABLE_SCHEME_PRODUCTS + "("
            + KEY_TABLE_SCHEME_PRODUCTS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_COMPANY_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_CROP_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_BOOKING_INACTIVE + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_VALID_TO + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_BOOKING_YEAR + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_SEASON_CODE + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE + " TEXT,"
            + KEY_TABLE_SCHEME_PRODUCTS_PRICE + " TEXT" + ")";

    // UCD table create statement
    private static final String CREATE_TABLE_USER_COMPANY_DIVISION = "CREATE TABLE "
            + TABLE_USER_COMPANY_DIVISION + "("
            + KEY_TABLE_USER_COMPANY_DIVISION_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " TEXT,"
            + KEY_TABLE_USER_COMPANY_DIVISION_MASTER_ID + " TEXT" + ")";

    // USER COMPANY CUSTOMER table create statement
    private static final String CREATE_TABLE_COMPANY_DIVISION_CROPS = "CREATE TABLE "
            + TABLE_COMPANY_DIVISION_CROPS + "("
            + KEY_TABLE_COMPANY_DIVISION_CROPS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " TEXT,"
            + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " TEXT,"
            + KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " TEXT" + ")";

    // COMPLAINTS table create statement
    private static final String CREATE_TABLE_COMPLAINTS = "CREATE TABLE "
            + TABLE_COMPLAINT + "(" + KEY_TABLE_COMPLAINTS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_COMPLAINT_USER_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_COMPANY_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_DIVISION_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_CROP_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_PRODUCT_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_FARMER_NAME + " TEXT,"
            + KEY_TABLE_COMPLAINTS_CONTACT_NO + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES + " TEXT,"
            + KEY_TABLE_COMPLAINTS_SOIL_TYPE + " TEXT,"
            + KEY_OTHERS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_PURCHASED_QTY + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_QTY + " TEXT,"
            + KEY_PURCHASE_DATE + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_BILL_NUMBER + " TEXT,"
            + KEY_TABLE_COMPLAINTS_RETAILER_NAME + " TEXT,"
            + KEY_TABLE_COMPLAINTS_DISTRIBUTOR + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_MANDAL + " TEXT,"
            + KEY_TABLE_COMPLAINTS_VILAGE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD + " TEXT,"
            + KEY_REGULATORY_TYPE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_SAMPLING_DATE + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_PLACE_SAMPLING + " TEXT,"
            + KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME + " TEXT,"
            + KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMMENTS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_STATUS + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_REMARKS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_DISTRICT + " TEXT,"
            + KEY_TABLE_COMPLAINTS_STATE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_REGION + " TEXT,"
            + KEY_TABLE_COMPLAINTS_ZONE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_DATE_OF_SOWING + " TEXT,"
            + KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_DEALER + " TEXT,"
            + KEY_TABLE_COMPLAINTS_STAGES + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME + " TEXT,"
            + KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT + " TEXT,"
            + KEY_TABLE_COMPLAINTS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_FFMID + " TEXT,"
            + KEY_TABLE_COMPLAINTS_RETAILER_ID + " TEXT" + ")";

    // COMPLAINTS1 table create statement


    //complaints new form insert

    private static final String CREATE_TABLE_COMPLAINTS_NEW = "CREATE TABLE "
            + TABLE_COMPLAINT_NEW + "(" + KEY_TABLE_COMPLAINTS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_COMPLAINT_USER_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_COMPANY_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_CROP_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_VARIETY + "INTEGER,"
            + KEY_TABLE_COMPLAINTS_PRODUCT_ID + " INTEGER,"
            + KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE + "DOUBLE,"//
            + KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS + " TEXT,"
            + KEY_TABLE_COMPLAINTS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_COMPLAINTS_DEALER + "TEXT,"
            + KEY_TABLE_COMPLAINTS_DISTRIBUTOR + "INERGER,"
            + KEY_EMP_VILLAGE + "TEXT,"
            + KEY_TABLE_COMPLAINTS_MANDAL + "TEXT,"
            + KEY_TABLE_FARMER_DISTRICT + "TEXT,"
            + KEY_TABLE_COMPLAINTS_REGION + "TEXT,"
            + KEY_TABLE_FARMER_STATE + "TEXT,"
            + KEY_TABLE_COMPLAINTS_ZONE + "TEXT,"
            + KEY_TABLE_COMPLAINTS_IRRIGATION + "INTEGER,"
            + KEY_TABLE_COMPLAINTS_AREA_IN_ACERS + "DOUBLE,"
            + KEY_TABLE_COMPLAINTS_FRAMER_NAME + "TEXT,"
            + KEY_TABLE_COMPLAINTS_DATE_OF_PURCHASE + "TEXT,"
            + KEY_TABLE_COMPLAINTS_NO_OF_PKTS_PURCHASED + "INTEGER,"
            + KEY_TABLE_COMPLAINTS_COMPLAINT_NO_OF_PKTS + "INTEGER,"
            + KEY_TABLE_COMPLAINTS_BILL_RECEIPET_REFNO + "INTEGER,"
            + KEY_TABLE_COMPLAINTS_FARMER_CONTACT_NO + "TEXT,"
            + KEY_TABLE_COMPLAINTS_DATE_OF_SOWING + "TEXT,"
            + KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS + "INETGER,"
            + KEY_TABLE_COMPLAINTS_REMARKS + "TEXT,"
            + KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME + "TEXT,"
            + KEY_TABLE_COMPLAINTS_INSPECTED_BY_STAFF + "TEXT,"
            + KEY_TABLE_COMPLAINTS_INSPECTED_BY_DESIGNATION + "TEXT,"
            + KEY_TABLE_COMPLAINTS_INSPECTED_BY_EMAIL + "TEXT,"
            + KEY_TABLE_COMPLAINTS_CONTACT_NO + "TEXT,"
            + KEY_TABLE_COMPLAINTS_STATE + " TEXT,"
            + KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME + " TEXT,"
            + KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT + " TEXT,"
            + KEY_TABLE_COMPLAINTS_FFMID + " INTEGER" + ")";


    // CROPS table create statement
    private static final String CREATE_TABLE_CROPS = "CREATE TABLE "
            + TABLE_CROPS + "(" + KEY_TABLE_CROPS_CROP_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_CROPS_CROP_MASTER_ID + " TEXT,"
            + KEY_TABLE_CROPS_CROP_NAME + " TEXT,"
            + KEY_TABLE_CROPS_CROP_CODE + " TEXT,"
            + KEY_TABLE_CROPS_CROP_SAP_ID + " TEXT,"
            + KEY_TABLE_CROPS_DIVISION_ID + " TEXT,"
            + KEY_TABLE_CROPS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_CROPS_UPDATED_DATETIME + " DATETIME" + ")";

    // CUSTOMER table create statement
    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE "
            + TABLE_CUSTOMERS + "(" + KEY_TABLE_CUSTOMER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_CUSTOMER_MASTER_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_NAME + " TEXT,"
            + KEY_TABLE_CUSTOMER_CODE + " TEXT,"
            + KEY_TABLE_CUSTOMER_ADDRESS + " TEXT,"
            + KEY_TABLE_CUSTOMER_STREET + " TEXT,"
            + KEY_TABLE_CUSTOMER_CITY + " TEXT,"
            + KEY_TABLE_CUSTOMER_COUNTRY + " TEXT,"
            + KEY_TABLE_CUSTOMER_REGION_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_TELEPHONE + " TEXT,"
            + KEY_TABLE_CUSTOMER_COMPANY_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_STATUS + " TEXT,"
            + KEY_TABLE_CUSTOMER_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_CUSTOMER_UPDATED_DATETIME + " DATETIME,"


            + KEY_TABLE_CUSTOMER_PASSWORD + " TEXT,"
            + KEY_TABLE_CUSTOMER_EMAIL + " TEXT,"
            + KEY_TABLE_CUSTOMER_STATE + " TEXT,"
            + KEY_TABLE_CUSTOMER_DISTRICT + " TEXT,"
            + KEY_TABLE_CUSTOMER_LAT_LNG + " TEXT, "
            + KEY_TABLE_CUSTOMER_LAT_LNG_ADDRESS + " TEXT" + ")";


    // serviceorder table create statement
    private static final String CREATE_TABLE_SERVICEORDER = "CREATE TABLE "
            + TABLE_SERVICEORDER + "(" + KEY_TABLE_SERVICEORDER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SERVICEORDER_MASTER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_ORDERTYPE + " TEXT,"
            + KEY_TABLE_SERVICEORDER_ORDERDATE + " TEXT,"
            + KEY_TABLE_SERVICEORDER_USER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DIVISION_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_COMPANY_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_STATUS + " TEXT,"
            + KEY_TABLE_SERVICEORDER_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT + " TEXT,"
            + KEY_TABLE_SERVICEORDER_FFM_ID + " TEXT ,"
            + KEY_TABLE_SERVICEORDER_CREATED_BY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_UPDATED_BY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_APPROVAL_STATUS + " TEXT,"
            + KEY_TABLE_SERVICEORDER_OFFLINE_APPROVAL_SET + " INTEGER DEFAULT 0,"
            + KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS + " TEXT,"
            + KEY_TABLE_SERVICEORDER_SAP_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_SEASON + " TEXT" + ")";

    // serviceorderDETAILS table create statement
    private static final String CREATE_TABLE_SERVICEORDERDETAILS = "CREATE TABLE "
            + TABLE_SERVICEORDERDETAILS + "(" + KEY_TABLE_SERVICEORDER_DETAIL_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_STATUS + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_FFM_ID + " INTEGER DEFAULT 0,"
            + KEY_TABLE_SERVICEORDER_SLAB_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY + " TEXT "
            + ")";

    private static final String CREATE_TABLE_SERVICE_ORDER_HISTORY = "CREATE TABLE "
            + TABLE_SERVICE_ORDER_HISTORY + "(" + KEY_SOH_PRIMARY_ID + " INTEGER PRIMARY KEY,"
            + KEY_SOH_ORDER_HISTORY_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID + " TEXT,"
            + KEY_SOH_ORDER_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_STATUS + " TEXT,"
            + KEY_SOH_CREATED_BY + " TEXT,"
            + KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SERVICEORDER_FFM_ID + " INTEGER DEFAULT 0,"
            + KEY_TABLE_SERVICEORDER_SLAB_ID + " TEXT,"
            + KEY_SOH_MODIFIED_BY + " TEXT,"
            + KEY_SOH_ORDER_APPROVAL_ID + " TEXT"
            + ")";

    // CUSTOMER_DETAIL table create statement
    // CUSTOMER_DETAIL table create statement
    private static final String CREATE_TABLE_CUSTOMER_DETAILS = "CREATE TABLE "
            + TABLE_CUSTOMER_DETAILS + "(" + KEY_TABLE_CUSTOMER_DETAILS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_STATUS + " TEXT,"
            + KEY_TABLE_CUSTOMER_DETAILS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_CUSTOMER_DETAILS_UPDATED_DATETIME + " DATETIME, "
            + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_BALANCE + " TEXT"
            + ")";

    // DISTRIBUTOR_RETAILER table create statement
    private static final String CREATE_TABLE_DISTRIBUTOR_RETAILER = "CREATE TABLE "
            + TABLE_DISTRIBUTOR_RETAILER + "("
            + KEY_TABLE_DISTRIBUTOR_RTAILER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_DISTRIBUTOR_ID + " TEXT,"
            + KEY_TABLE_RETAILER_ID + " TEXT" + ")";


    // DIVISION table create statement
    private static final String CREATE_TABLE_DIVISION = "CREATE TABLE "
            + TABLE_DIVISION + "("
            + KEY_TABLE_DIVISION_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_DIVISION_MASTER_ID + " TEXT,"
            + KEY_TABLE_DIVISION_NAME + " TEXT,"
            + KEY_TABLE_DIVISION_SAP_ID + " TEXT,"
            + KEY_TABLE_DIVISION_CODE + " TEXT,"
            + KEY_TABLE_DIVISION_STATUS + " TEXT,"
            + KEY_TABLE_DIVISION_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_DIVISION_UPDATED_DATETIME + " DATETIME" + ")";

    // COMPANIES table create statement
    private static final String CREATE_TABLE_ERRORS = "CREATE TABLE "
            + TABLE_ERRORS + "(" + KEY_TABLE_ERRORS_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_ERRORS_NAME + " TEXT,"
            + KEY_TABLE_ERRORS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_ERRORS_UPDATED_DATETIME + " DATETIME" + ")";

    // COMPLAINTS table create statement
    private static final String CREATE_TABLE_EVENT_MANAGMENT = "CREATE TABLE "
            + TABLE_EVENT_MANAGMENT + "(" + KEY_TABLE_EVENT_MANAGEMENT_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_EVENT_MANAGEMENT_TITLE + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_PLANED_EVENT_DATE + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_PLANED_EVENT_TIME + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_CONCER_PERSON + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_VILLAGE + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_LOCATION_ADDR + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_PARTICIPANTS + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_STATUS + " TEXT,"
            + KEY_TABLE_EVENT_MANAGEMENT_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_EVENT_MANAGEMENT_UPDATED_DATETIME + " DATETIME" + ")";

    // FARMERS table create statement
    private static final String CREATE_TABLE_FARMERS = "CREATE TABLE "
            + TABLE_FARMERS + "(" + KEY_TABLE_FARMER_PRIMARY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_FARMER_ID + " TEXT,"
            + KEY_TABLE_FARMER_NAME + " TEXT,"
            + KEY_TABLE_FARMER_PHONE + " TEXT,"
            + KEY_TABLE_FARMER_REGION_ID + " TEXT,"
            + KEY_TABLE_FARMER_STATE + " TEXT,"
            + KEY_TABLE_FARMER_DISTRICT + " TEXT,"
            + KEY_TABLE_FARMER_TALUKA + " TEXT,"
            + KEY_TABLE_FARMER_VILLAGE + " TEXT,"
            + KEY_TABLE_FARMER_TOTAL_LAND_HOLDING + " TEXT,"
            + KEY_TABLE_FARMER_CROP_ID + " TEXT,"
            + KEY_TABLE_FARMER_STATUS + " TEXT,"
            + KEY_TABLE_FARMER_FFM_ID + " TEXT,"
            + KEY_TABLE_FARMER_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_FARMER_UPDATED_DATETIME + " DATETIME" + ")";


    // CUSTOMER table create statement
    private static final String CREATE_TABLE_GEO_TRACKING = "CREATE TABLE "
            + TABLE_GEO_TRACKING + "(" + KEY_TABLE_GEO_TRACKING_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_GEO_TRACKING_MASTER_ID + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_VISIT_TYPE + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_USER_ID + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + "  TEXT,"
            + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_DISTANCE + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_VISIT_DATE + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_STATUS + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_FFMID + " TEXT,"
            + KEY_TABLE_GEO_TRACKING_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME + " DATETIME, "
            + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " TEXT, "
            + KEY_TABLE_GEO_TRACKING_POLYLINE + " TEXT, "
            + KEY_TABLE_GEO_TRACKING_VERSION + " TEXT, "
            + METER_READING_CHECKIN_IMAGE + " TEXT, "
            + METER_READING_CHECKIN_TEXT + " TEXT, "
            + METER_READING_CHECKOUT_IMAGE + " TEXT, "
            + METER_READING_CHECKOUT_TEXT + " TEXT, "
            + VEHICLE_TYPE + " TEXT, "
            + CHECKIN_COMMENT + " TEXT DEFAULT NULL, "
            + PERSONAL_USES_KM + " TEXT, "
            + PAUSE + " TEXT, "
            + RESUME + " TEXT, "
            + SYNC_STATUS + " INTEGER DEFAULT 1"

            + ")";


    // users table create statement

    private static final String CREATE_TABLE_USER_REGIONS = "CREATE TABLE "
            + TABLE_USER_REGIONS + "(" + KEY_TABLE_USER_REGION_PRIMARY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_USER_REGION_USER_REGION_ID + " TEXT,"
            + KEY_TABLE_USER_REGION_USER_ID + " TEXT,"
            + KEY_TABLE_USER_REGION_REGION_ID + " TEXT,"
            + KEY_TABLE_USER_REGION_STATUS + " TEXT " + ")";
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_TABLE_USERS_USER_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_USERS_MASTER_ID + " TEXT,"
            + KEY_TABLE_USERS_FIRST_NAME + " TEXT,"
            + KEY_TABLE_USERS_LAST_NAME + " TEXT,"
            + KEY_TABLE_USERS_MOBILE_NO + " TEXT,"
            + KEY_TABLE_USERS_EMAIL + " TEXT,"
            + KEY_TABLE_USERS_SAP_ID + " TEXT,"
            + KEY_TABLE_USERS_PASSWORD + " TEXT,"
            + KEY_TABLE_USERS_ROLE_ID + " TEXT,"
            + KEY_TABLE_USERS_REPORTING_MANAGER_ID + " TEXT,"
            + KEY_TABLE_USERS_STATUS + " TEXT,"
            + KEY_TABLE_USERS_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_USERS_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_USERS_DESIGNATION + " TEXT,"
            + KEY_TABLE_USERS_HEADQUARTER + " TEXT,"
            + KEY_TABLE_USERS_LOCATION + " TEXT,"
            + KEY_TABLE_USERS_IMAGE + " TEXT,"
            + KEY_TABLE_USERS_REGION_ID + " TEXT, "
            + KEY_TABLE_USERS_GRADE + " TEXT, "
            + KEY_TABLE_USERS_COST_CENTER + " TEXT "

            + ")";

    // COMPANY_CROP table create statement
    private static final String CREATE_TABLE_UCDD = "CREATE TABLE "
            + TABLE_UCDD + "("
            + KEY_TABLE_UCDD_JUNCTION_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_UCDD_USER_ID + " TEXT,"
            + KEY_TABLE_UCDD_COMPANY_ID + " TEXT,"
            + KEY_TABLE_UCDD_DIVISION_ID + " TEXT,"
            + KEY_TABLE_UCDD_CUSTOMER_ID + " TEXT" + ")";


    // SM table create statement
    private static final String CREATE_TABLE_SM = "CREATE TABLE "
            + TABLE_SM + "(" + KEY_TABLE_SM_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SM_MOVEMENT_TYPE + " INTEGER,"
            + KEY_TABLE_SM_USER_ID + " INTEGER,"
            + KEY_TABLE_SM_COMPANY_ID + " INTEGER,"
            + KEY_TABLE_SM_DIVISION + " INTEGER,"
            + KEY_TABLE_SM_STATUS + " INTEGER,"
            + KEY_TABLE_SM_CREATED_BY + " TEXT,"
            + KEY_TABLE_SM_UPDATED_BY + " TEXT,"
            + FFM_ID + " INTEGER,"
            + KEY_TABLE_SM_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SM_UPDATED_DATETIME + " DATETIME,"
            + KEY_CUSTOMER_ID + " TEXT"
            + ")";


    // SMD table create statement
    private static final String CREATE_TABLE_SMD = "CREATE TABLE "
            + TABLE_SMD + "(" + KEY_TABLE_SMD_DETAIL_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SMD_STOCK_MOVEMENT_ID + " INTEGER,"
            + KEY_TABLE_SMD_USER_TYPE + " INTEGER,"
            + KEY_TABLE_SMD_CUSTOMER_ID + " INTEGER,"
            + KEY_TABLE_SMD_CROP_ID + " INTEGER,"
            + KEY_TABLE_SMD_PRODUCT_ID + " INTEGER,"
            + KEY_TABLE_SMD_STOCK_PLACED + " TEXT,"
            + KEY_TABLE_SMD_CURRENT_STOCK + " TEXT,"
            + KEY_TABLE_SMD_PLACED_DATE + " TEXT,"
            + KEY_TABLE_SMD_POG + " TEXT,"
            + KEY_TABLE_CREATED_BY + " TEXT,"
            + KEY_TABLE_UPDATED_BY + " TEXT,"
            + KEY_TABLE_SMD_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SMD_UPDATED_DATETIME + " DATETIME,"
            + FFM_ID + " INTEGER,"
            + KEY_TABLE_SMD_ORDER_SAP_ID + " TEXT"
            + ")";


    private static final String sqltable_stock_movement_retailer_details = "CREATE TABLE "
            + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + "(" + KEY_TABLE_SMD_RETAILER_ID_PRIMARY_KEY + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_SMD_STOCK_MOVEMENT_ID + " INTEGER,"
            + KEY_TABLE_SMD_USER_TYPE + " INTEGER,"
            + KEY_TABLE_SMD_CROP_ID + " INTEGER,"
            + KEY_TABLE_SMD_PRODUCT_ID + " INTEGER,"
            + KEY_TABLE_SMD_STOCK_PLACED + " TEXT,"
            + KEY_TABLE_SMD_CURRENT_STOCK + " TEXT,"
            + KEY_TABLE_SMD_PLACED_DATE + " TEXT,"
            + KEY_TABLE_SMD_POG + " TEXT,"
            + KEY_TABLE_CREATED_BY + " TEXT,"
            + KEY_TABLE_UPDATED_BY + " TEXT,"
            + KEY_TABLE_SMD_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SMD_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_SMD_RETAILER_USER_ID + " INTEGER,"
            + KEY_TABLE_SMD_RETAILER_ID + " TEXT,"
            + KEY_TABLE_SMD_RETAILER_VERIFIED + " TEXT,"
            + KEY_TABLE_SMD_RETAILER_VERIFIED_BY + " TEXT,"
            + FFM_ID + " INTEGER"
            + ")";


    ////////////////////////////////////////////////////////////////////////////////////////
    //employ visit management create statement


    private static final String CREATE_TABLE_EMPLOYEE_VISIT_MANAGEMENT = "CREATE TABLE "
            + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "(" + KEY_EMP_VISIT_ID + " INTEGER PRIMARY KEY,"
            + KEY_EMP_VISIT_MASTER_ID + " TEXT,"
            + KEY_EMP_TYPE + " INTEGER,"
            + KEY_EMP_GEO_TRACKING_ID + " INTEGER,"
            + KEY_EMP_VISIT_USER_ID + " INTEGER,"
            + KEY_EMP_VISIT_CUSTOMER_ID + " INTEGER,"
            + KEY_EMP_VISIT_PLAN_TYPE + " INTEGER,"
            + KEY_EMP_PURPOSE_VISIT + " TEXT,"
            + KEY_EMP_PLAN_DATE_TIME + " DATETIME,"
            + KEY_EMP_CONCERN_PERSON_NAME + " TEXT,"
            + KEY_EMP_MOBILE + " INTEGER,"
            + KEY_EMP_VILLAGE + " TEXT,"
            + KEY_EMP_LOCATION_ADDRESS + " TEXT,"
            + KEY_EMP_VISIT_CROP_ID + " TEXT,"
            + KEY_EMP_REGION_ID + " TEXT,"
            + KEY_EMP_RETAILER_ID + " TEXT,"
            + KEY_EMP_FARMER_ID + " TEXT,"
            + KEY_EMP_GSTIN_NO + " TEXT,"
            + KEY_EMP_LOCATION_DISTRICT + " TEXT,"
            + KEY_EMP_LOCATION_TALUKA + " TEXT,"
            + KEY_EMP_LOCATION_VILLAGE + " TEXT,"
            + KEY_EMP_FEILD_AREA + " TEXT,"
            + KEY_EMP_CHECK_IN_TIME + " DATETIME,"
            + KEY_EMP_COMMENTS + " TEXT,"
            + KEY_EMP_STATUS + " INTEGER,"
            + KEY_EMP_APPROVAL_STATUS + " INTEGER,"
            + KEY_EMP_EVENT_NAME + " TEXT,"
            + KEY_EMP_EVENT_END_DATE + " TEXT,"
            + KEY_EMP_EVENT_PURPOSE + " TEXT,"
            + KEY_EMP_EVENT_VENUE + " TEXT,"
            + KEY_EMP_EVENT_PARTICIPANTS + " TEXT,"
            + KEY_EMP_CREATED_BY + " INTEGER,"
            + KEY_EMP_UPDATED_BY + " INTEGER,"
            + KEY_EMP_CREATED_DATETIME + " DATETIME,"
            + KEY_EMP_UPDATE_DATETIME + " DATETIME,"
            + KEY_EMP_FFM_ID + " INTEGER ,"
            + KEY_EMP_PURPOSE_VISIT_IDS + " TEXT ,"
            + KEY_EMP_TASK_COMPLETED_LATLNG + " TEXT ,"
            + KEY_EMP_SERVER_FLAG + " INTEGER"

            + ")";

    //payment_collection create statement

    private static final String CREATE_TABLE_PAYMENT_COLLECTION = "CREATE TABLE "
            + TABLE_PAYMENT_COLLECTION + "(" + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " INTEGER PRIMARY KEY,"
            + KEY_PAYMENT_COLLECTION_PAYMENT_MASTER_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_PAYMENT_TYPE + " TEXT,"
            + KEY_PAYMENT_COLLECTION_USER_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_COMPANY_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_DIVISION_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_TOTAL_AMOUNT + " TEXT,"
            + KEY_PAYMENT_COLLECTION_PAYMENT_MODE + " TEXT,"
            + KEY_PAYMENT_COLLECTION_BANK_NAME + " TEXT,"
            + KEY_PAYMENT_COLLECTION_RTGS_OR_NEFT_NO + " TEXT,"
            + KEY_PAYMENT_COLLECTION_PAYMENT_DATETIME + " DATETIME,"
            + KEY_PAYMENT_COLLECTION_DATE_ON_CHEQUE_NUMBER + " DATETIME,"
            + KEY_PAYMENT_COLLECTION_CHEQUE_NO_DD_NO + " TEXT,"
            + KEY_PAYMENT_COLLECTION_STATUS + " INTEGER,"
            + KEY_PAYMENT_COLLECTION_CREATED_DATETIME + " DATETIME,"
            + KEY_PAYMENT_COLLECTION_UPDATEd_DATETIME + " DATETIME,"
            + KEY_PAYMENT_COLLECTION_FFMID + " INTEGER" + ")";


    // plants create statement

    private static final String CREATE_TABLE_PLANTS = "CREATE TABLE "
            + TABLE_PLANTS + "(" + KEY_PLANT_ID + " INTEGER PRIMARY KEY,"
            + KEY_PLANT_NAME + " TEXT,"
            + KEY_PLANT_SAP_CODE + " TEXT,"
            + KEY_STATE + " INTEGER,"
            + KEY_DISTRICT + " INTEGER,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_PLANTS_STATUS + " INTEGER,"
            + KEY_PLANTS_CREATED_DATETIME + " DATETIME,"
            + KEY_PLANTS_UPDATEd_DATETIME + " DATETIME," + ")";


    // region table create statement
    private static final String CREATE_TABLE_REGION = "CREATE TABLE "
            + TABLE_REGION + "(" + KEY_REGION_ID + " INTEGER PRIMARY KEY,"
            + KEY_REGION_NAME + " TEXT,"
            + KEY_REGION__MASTER_ID + " TEXT,"
            + KEY_REGION_CODE + " TEXT,"
            + KEY_REGION_STATUS + " TEXT" + ")";

    // SCHEMES table create statement

    private static final String CREATE_TABLE_SCHEMES = "CREATE TABLE "
            + TABLE_SCHEMES + "(" + KEY_SCHEMES_ID + " INTEGER PRIMARY KEY,"
            + KEY_SCHEMES_MASTER_ID + " TEXT,"
            + KEY_SCHEMES_TITLE + " TEXT,"
            + KEY_SCHEMES_SAP_CODE + " TEXT,"
            + KEY_SCHEMES_FILE_LOCATION + " TEXT,"
            + KEY_SCHEMES_STATUS + " TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
            + TABLE_PRODUCTS + "(" + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRODUCT_MASTER_ID + " TEXT,"
            + KEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRODUCT_DESCRIPTION + " TEXT,"
            + KEY_PRODUCT_SAP_CODE + " TEXT,"
            + KEY_PRODUCT_CROP_ID + " TEXT,"
            + KEY_PRODUCTS_COMPANY_ID + " TEXT,"
            + KEY_PRODUCTS_DIVISION_ID + " TEXT,"
            + KEY_PRODUCTS_REGION + " TEXT,"
            + KEY_PRODUCTS_PACKETS_COUNT + " TEXT,"
            + KEY_PRODUCTS_PRICE + " TEXT,"
            + KEY_PRODUCT_DISCOUNT + " TEXT,"
            + KEY_PRODUCT_FROM_DATE + " DATETIME,"
            + KEY_PRODUCT_TO_DATE + " DATETIME,"
            + KEY_PRODUCTS_CATALOG_URL + " TEXT,"
            + KEY_PRODUCT_CREATED_DATETIME + " DATETIME,"
            + KEY_PRODUCT_UPDATED_DATETIME + " DATETIME,"
            + KEY_PRODUCT_STATUS + " TEXT,"
            + KEY_PRODUCT_IMAGE + " TEXT,"
            + KEY_PRODUCT_VIDEO + " TEXT,"
            + KEY_PRODUCT_BRAND_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCT_PRICE = "CREATE TABLE "
            + TABLE_PRODUCT_PRICE + "(" + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRODUCT_MASTER_ID + " TEXT,"
            + KEY_PRODUCTS_PRICE + " TEXT,"
            + KEY_PRODUCT_DISCOUNT + " TEXT,"
            + KEY_PRODUCT_FROM_DATE + " DATETIME,"
            + KEY_PRODUCT_TO_DATE + " DATETIME,"
            + KEY_PRODUCT_STATUS + " TEXT ,"
            + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + " TEXT" + ")";

//feedback table create statement

    private static final String CREATE_DAILYDAIRY_TABLE = "CREATE TABLE " + TABLE_DAILYDAIRY + "("
            + KEY_DD_ID + " INTEGER PRIMARY KEY,"
            + KEY_DD_MASTER_ID + " TEXT,"
            + KEY_DD_TITLE + " TEXT,"
            + KEY_DD_USER_ID + " INTEGER,"
            + KEY_DD_COMMENTS + " TEXT,"
            + KEY_DD_TIME_SLOT + " TEXT,"
            + KEY_DD_DATE + " TEXT,"
            + KEY_DD_CREATED_DATE + " TEXT,"
            + KEY_DD_FFMID + " INTEGER,"
            + KEY_DD_TENTATIVE_TIME + " TEXT,"
            + KEY_DD_TYPE + " INTEGER,"
            + KEY_DD_STATUS + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_FEEDBACK = " CREATE TABLE "
            + TABLE_FEEDBACK + "(" + KEY_TABLE_FEEDBACK_FEEDBACK_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_FEEDBACK_USER_ID + " INTEGER,"
            + KEY_TABLE_FEEDBACK_FARMER_NAME + " TEXT,"
            + KEY_TABLE_FEEDBACK_PLACE + " TEXT,"
            + KEY_TABLE_FEEDBACK_CONTACT_NO + " TEXT,"
            + KEY_TABLE_FEEDBACK_CROP + " TEXT,"
            + KEY_TABLE_FEEDBACK_HYBRID + " TEXT,"
            + KEY_TABLE_FEEDBACK_SOWING_DATE + " DATETIME,"
            + KEY_TABLE_FEEDBACK_FEEDBACK_MESSAGE + " TEXT,"
            + KEY_TABLE_FEEDBACK_IMAGE + " TEXT,"
            + KEY_TABLE_FEEDBACK_FFMID + " INTEGER,"
            + KEY_TABLE_FEEDBACK_TRANSPLANT_DATE + " TEXT" + ")";

    private static final String CREATE_TABLE_CUSTOMER_BANKDETAILS = "CREATE TABLE "
            + TABLE_CUSTOMER_BANKDETAILS + "(" + KEY_BANKDETAIL_ID + " INTEGER PRIMARY KEY,"
            + KEY_BANKDETAIL_MASTER_ID + " TEXT,"
            + KEY_BANKDETAIL_CUSTOMER_ID + " TEXT,"
            + KEY_BANKDETAIL_ACCOUNT_NUMBER + " TEXT,"
            + KEY_BANKDETAIL_ACCOUNT_NAME + " TEXT,"
            + KEY_BANKDETAIL_BANK_NAME + " TEXT,"
            + KEY_BANKDETAIL_BRANCH_NAME + " TEXT,"
            + KEY_BANKDETAIL_IFSC_CODE + " TEXT,"
            + KEY_BANKDETAIL_STATUS + " TEXT,"
            + KEY_BANKDETAIL_CREATED_BY + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " DATETIME,"
            + KEY_BANKDETAIL_FFMID + " TEXT" + ")";

    /*private static final String CREATE_TABLE_MI_COMMODITY_PRICE = "CREATE TABLE "
            + TABLE_MI_COMMODITY_PRICE + "(" + KEY_COMMODITY_PRICE_ID + " INTEGER PRIMARY KEY,"
            + KEY_COMMODITY_PRICE_MASTER_ID + " TEXT,"
            + KEY_COMMODITY_PRICE_CROP_NAME + " TEXT,"
            + KEY_COMMODITY_PRICE_VARIETY_TYPE + " TEXT,"
            + KEY_COMMODITY_PRICE_APMC_MANDI_PRICE + " TEXT,"
            + KEY_COMMODITY_PRICE_COMMODITY_DEALER_AGENT_PRICE + " TEXT,"
            + KEY_COMMODITY_PRICE_PURCHASE_PRICE_BY_INDUSTRY + " TEXT,"
            + KEY_COMMODITY_PRICE_CREATED_BY + " TEXT,"
            + KEY_COMMODITY_PRICE_CREATED_ON + " TEXT,"
            + KEY_COMMODITY_PRICE_FFMID + " TEXT" + ")";*/

    private static final String CREATE_TABLE_MI_CROP_SHIFTS = "CREATE TABLE "
            + TABLE_MI_CROP_SHIFTS + "(" + KEY_CROP_SHIFTS_ID + " INTEGER PRIMARY KEY,"
            + KEY_CROP_SHIFTS_MASTER_ID + " TEXT,"
            + KEY_CROP_SHIFTS_CROP_NAME + " TEXT,"
            + KEY_CROP_SHIFTS_VARIETY_TYPE + " TEXT,"
            + KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA + " TEXT,"
            + KEY_CROP_SHIFTS_CURRENT_YEAR_EXPECTED_AREA + " TEXT,"
            + KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE + " TEXT,"
            + KEY_CROP_SHIFTS_REASON_CROP_SHIFT + " TEXT,"
            + KEY_CROP_SHIFTS_CREATED_BY + " TEXT,"
            + KEY_CROP_SHIFTS_CREATED_ON + " TEXT,"
            + KEY_CROP_SHIFTS_CROP_IN_SAVED_SEED + " TEXT,"
            + KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR + " TEXT,"
            + KEY_CROP_SHIFTS_CURRENT_YEAR_SRR + " TEXT,"
            + KEY_CROP_SHIFTS_NEXT_YEAR_SRR + " TEXT,"
            + KEY_CROP_SHIFTS_FFMID + " TEXT" + ")";


    private static final String CREATE_TABLE_MI_PRICE_SURVEY = "CREATE TABLE "
            + TABLE_MI_PRICE_SURVEY + "(" + KEY_PRICE_SURVEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRICE_SURVEY_MASTER_ID + " TEXT,"
            + KEY_PRICE_SURVEY_COMPANY_NAME + " TEXT,"
            + KEY_PRICE_SURVEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRICE_SURVEY_SKU_PACK_SIZE + " TEXT,"
            + KEY_PRICE_SURVEY_RETAIL_PRICE + " TEXT,"
            + KEY_PRICE_SURVEY_INVOICE_PRICE + " TEXT,"
            + KEY_PRICE_SURVEY_NET_DISTRIBUTOR_LANDING_PRICE + " TEXT,"
            + KEY_PRICE_SURVEY_CREATED_BY + " TEXT,"
            + KEY_PRICE_SURVEY_CREATED_ON + " TEXT,"
            + KEY_PRICE_SURVEY_FFMID + " TEXT" + ")";

    private static final String CREATE_TABLE_MI_PRODUCT_SURVEY = "CREATE TABLE "
            + TABLE_MI_PRODUCT_SURVEY + "(" + KEY_PRODUCT_SURVEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRODUCT_SURVEY_MASTER_ID + " TEXT,"
            + KEY_PRODUCT_SURVEY_COMPANY_NAME + " TEXT,"
            + KEY_PRODUCT_SURVEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRODUCT_SURVEY_NAME_OF_THE_CHECK_SEGMENT + " TEXT,"
            + KEY_PRODUCT_SURVEY_LAUNCH_YEAR + " TEXT,"
            + KEY_PRODUCT_SURVEY_NO_UNITS_SOLD + " TEXT,"
            + KEY_PRODUCT_SURVEY_AREA_CROP_SOWN_NEW_PRODUCT + " TEXT,"
            + KEY_PRODUCT_SURVEY_REMARK_UNIQUE_FEATURE + " TEXT,"
            + KEY_PRODUCT_SURVEY_CREATED_BY + " TEXT,"
            + KEY_PRODUCT_SURVEY_CREATED_ON + " TEXT,"
            + KEY_PRODUCT_SURVEY_FFMID + " TEXT" + ")";


    private static final String CREATE_TABLE_RETAILER = "CREATE TABLE "
            + TABLE_RETAILER + "(" + KEY_TABLE_RETAILER_PRIMARY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TABLE_RETAILER_MASTER_ID + " TEXT,"
            + KEY_TABLE_RETAILER_NAME + " TEXT,"
            + KEY_TABLE_RETAILER_TIN_NO + " TEXT,"
            + KEY_TABLE_RETAILER_ADDRESS + " TEXT,"
            + KEY_TABLE_RETAILER_DISTRICT + " TEXT,"
            + KEY_TABLE_RETAILER_TALUKA + " TEXT,"
            + KEY_TABLE_RETAILER_VILLAGE + " TEXT,"
            + KEY_TABLE_RETAILER_REGION_ID + " TEXT,"
            + KEY_TABLE_RETAILER_GSTIN_NO + " TEXT,"
            + KEY_TABLE_RETAILER_PHONE + " TEXT,"
            + KEY_TABLE_RETAILER_MOBILE + " TEXT,"
            + KEY_TABLE_RETAILER_EMAIL_ID + " TEXT,"
            + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " TEXT,"
            + KEY_TABLE_RETAILER_SAP_CODE + " TEXT,"
            + KEY_TABLE_RETAILER_STATUS + " TEXT,"
            + KEY_TABLE_RETAILER_CREATED_DATETIME + " DATETIME,"
            + KEY_TABLE_RETAILER_UPDATED_DATETIME + " DATETIME,"
            + KEY_TABLE_RETAILER_FFMID + " TEXT, "
            + KEY_TABLE_CUSTOMER_LAT_LNG + " TEXT" + ")";



    private static final String CREATE_TABLE_DEMANDGENERATION = "CREATE TABLE "
            + TABLE_DEMANDGENERATION + "(" + KEY_DEMANDGENERATION_PRIMARY_ID +" INTEGER PRIMARY KEY,"
            + KEY_USER_ID + " TEXT,"
            + KEY_REGION__MASTER_ID + " TEXT,"
            + KEY_TABLE_DIVISION_ID + " TEXT,"
            + KEY_TABLE_CROPS_CROP_ID + " TEXT,"
            + KEY_DEMANDGENERATION_EVENT + " TEXT,"
            + KEY_DEMANDGENERATION_ADDRESS+ " TEXT,"
            + KEY_DEMANDGENERATION_PlANNEDDATE+ " TEXT,"
            +KEY_DEMANDGENERATION_NOOFFARMERS+ " TEXT,"
            +KEY_DEMANDGENERATION_STATUS+ " TEXT,"
            + KEY_CREATED_DATETIME+ " DATETIME,"
            + KEY_UPDATED_DATETIME+ " DATETIME"
           + ")";

    private static final String CREATE_TABLE_FARMER_COUPANS = "CREATE TABLE "
            + TABLE_COUPANS + "(" + KEY_FARMER_COUPAN_PRIMARY_ID +" INTEGER PRIMARY KEY,"
            + KEY_FARMER_COUPAN_ID + " TEXT,"
            + KEY_FARMER_USER_ID + " TEXT,"
            + KEY_FARMER_UNIQUE_NO + " TEXT,"
            + KEY_FARMER_POINTS + " TEXT,"
            + KEY_FARMER_REGION_ID + " TEXT,"
            + KEY_FARMER_COMPANY_ID+ " TEXT,"
            + KEY_FARMER_COUPAN_TYPE+ " TEXT,"
            +KEY_FARMER_STATUS+ " TEXT,"
            +KEY_FARMER_NAME+ " TEXT,"
            + KEY_FARMER_EMAIL+ " TEXT,"
            +KEY_FARMER_MOBILE+ " TEXT,"
            +KEY_FARMER_FARMER_ADHAR_NO+ " TEXT,"
            + KEY_FARMER_IMAGE+ " TEXT,"
            + KEY_FARMER_CROP+ " TEXT,"


            + KEY_FARMER_LOCATION+ " TEXT,"
            +KEY_FARMER_ADDRESS+ " TEXT,"
            +KEY_FARMER_SYNC_STATUS+ " TEXT,"
            + KEY_FARMER_CREATED_DATETIME+ " TEXT,"
            + KEY_FARMER_UPDATED_DATETIME+ " DATETIME,"
            +KEY_FARMER_VILLAGE+ " TEXT,"
            + KEY_FARMER_THALUKA+ " TEXT,"
            + KEY_FARMER_SERVERSTATUS+ " TEXT"
            + ")";

    public String adddemangeneration(Demandgeneration_add demandgeneration,String type) {
        String status="";
        long l=0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();
        //values_customers.put(KEY_DEMANDGENERATION_PRIMARY_ID, demandgeneration.getCusMasterId());
        values_customers.put(KEY_USER_ID, demandgeneration.getUserId());
        values_customers.put(KEY_REGION__MASTER_ID, demandgeneration.getDistrict_id());
        values_customers.put(KEY_TABLE_DIVISION_ID, demandgeneration.getDivision_id());
        values_customers.put(KEY_TABLE_CROPS_CROP_ID, demandgeneration.getCrop_id());
        values_customers.put(KEY_DEMANDGENERATION_EVENT, demandgeneration.getEvent_name());
        values_customers.put(KEY_DEMANDGENERATION_ADDRESS, demandgeneration.getAddress());
        values_customers.put(KEY_DEMANDGENERATION_PlANNEDDATE, demandgeneration.getPlanneddate());
        values_customers.put(KEY_DEMANDGENERATION_NOOFFARMERS, demandgeneration.getNooffarmers());
        values_customers.put(KEY_DEMANDGENERATION_STATUS, demandgeneration.getStatus());
        values_customers.put(KEY_CREATED_DATETIME, demandgeneration.getCreateddate());
        values_customers.put(KEY_UPDATED_DATETIME, demandgeneration.getUpdated());
        db = getWritableDbIfClosed(db);
        // Inserting Row
        // db.insert(TABLE_CUSTOMERS, null, values_customers);
        if(type.equals("local"))
        {
            if (isAlreadyRecordExist2(db, demandgeneration.getEvent_name(), TABLE_DEMANDGENERATION, KEY_DEMANDGENERATION_EVENT, demandgeneration.getPlanneddate(), "and " + KEY_DEMANDGENERATION_PlANNEDDATE) == 0)
            {

                l = db.insert(TABLE_DEMANDGENERATION, null, values_customers);
                Log.d("Userss", String.valueOf(l));
                status = String.valueOf(l);
            }
            else
            {
                status = String.valueOf(l);

            }
        }
        else  if(type.equals("server")) {



                l = db.insert(TABLE_DEMANDGENERATION, null, values_customers);
                Log.d("Userss", String.valueOf(l));
                status = String.valueOf(l);

        }
        db.close(); // Closing database connection

        return status;
    }
    public String addcoupans(Coupans_add coupans_add,String type) {
        String status="";
        long l=0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();
        //values_customers.put(KEY_DEMANDGENERATION_PRIMARY_ID, demandgeneration.getCusMasterId());
        values_customers.put(KEY_FARMER_COUPAN_ID, coupans_add.getFarmerCouponId());
        values_customers.put(KEY_FARMER_USER_ID, coupans_add.getUserId());
        values_customers.put(KEY_FARMER_UNIQUE_NO, coupans_add.getUniqueNo());
        values_customers.put(KEY_FARMER_POINTS, coupans_add.getPoints());
        values_customers.put(KEY_FARMER_COMPANY_ID, coupans_add.getCompanyId());
        values_customers.put(KEY_FARMER_COUPAN_TYPE, coupans_add.getCouponType());
        values_customers.put(KEY_FARMER_STATUS, coupans_add.getS());
        values_customers.put(KEY_FARMER_NAME, coupans_add.getFarmerName());
        values_customers.put(KEY_FARMER_EMAIL, coupans_add.getFarmerEmail());
        values_customers.put(KEY_FARMER_FARMER_ADHAR_NO, coupans_add.getFarmerAdharNo());
        values_customers.put(KEY_FARMER_IMAGE, coupans_add.getImage());
        values_customers.put(KEY_FARMER_CROP, coupans_add.getCrop());
        values_customers.put(KEY_FARMER_LOCATION, coupans_add.getLocation());
        values_customers.put(KEY_FARMER_ADDRESS, coupans_add.getAddress());
        values_customers.put(KEY_FARMER_SYNC_STATUS, coupans_add.getSyncStatus());
        values_customers.put(KEY_FARMER_CREATED_DATETIME, coupans_add.getCreatedDatetime());
        values_customers.put(KEY_FARMER_UPDATED_DATETIME, coupans_add.getUpdatedDatetime());
        values_customers.put(KEY_FARMER_VILLAGE, coupans_add.getVillage());
        values_customers.put(KEY_FARMER_THALUKA, coupans_add.getThaluka());
        values_customers.put(KEY_FARMER_SERVERSTATUS, coupans_add.getSs());
        db = getWritableDbIfClosed(db);
        // Inserting Row
        // db.insert(TABLE_CUSTOMERS, null, values_customers);
        /*if(type.equals("local"))
        {
            if (isAlreadyRecordExist2(db, demandgeneration.getEvent_name(), TABLE_DEMANDGENERATION, KEY_DEMANDGENERATION_EVENT, demandgeneration.getPlanneddate(), "and " + KEY_DEMANDGENERATION_PlANNEDDATE) == 0)
            {

                l = db.insert(TABLE_DEMANDGENERATION, null, values_customers);
                Log.d("Userss", String.valueOf(l));
                status = String.valueOf(l);
            }
            else
            {
                status = String.valueOf(l);

            }
        }*/
        if(type.equals("server")) {
            if (isAlreadyRecordExist(db,coupans_add.getUniqueNo(), TABLE_COUPANS, KEY_FARMER_UNIQUE_NO) == 0) {
                l = db.insert(TABLE_COUPANS, null, values_customers);
            } else {
                //id = db.update(TABLE_COMPETITOR_CHANNEL, values, KEY_COMP_CHANNEL_FFM_ID + "=?", new String[]{String.valueOf(cc.ffmId)});
                Log.d("Userss", "Already exists");
            }



            Log.d("Userss", String.valueOf(l));
            status = String.valueOf(l);

        }
        db.close(); // Closing database connection

        return status;
    }


    public String update_demandgeneration(String userId, String dg_id) {
        /*SQLiteDatabase db = this.getWritableDatabase();
        //update demand_generation set status =1 where ids =2019-10-17 andplanneddate =42event =Yield estimation and user_id =674
        db.execSQL("update "+TABLE_DEMANDGENERATION+" set "+KEY_DEMANDGENERATION_STATUS+" ="+'1'+" where "+KEY_DEMANDGENERATION_EVENT+" ='"+event+"' and "+KEY_DEMANDGENERATION_PlANNEDDATE+" ='"+date+"' and "+KEY_USER_ID+" ="+userId);
        Log.d("qqqq","update "+TABLE_DEMANDGENERATION+" set "+KEY_DEMANDGENERATION_STATUS+" ="+'1'+" where "+KEY_TABLE_CROPS_CROP_ID+" ="+cropId+" and "+KEY_DEMANDGENERATION_PlANNEDDATE+" ="+date+" and "+KEY_DEMANDGENERATION_EVENT+" ='"+event+"' and "+KEY_USER_ID+" ="+userId);
        db.close(); // Closing database connection
        return  "Sucess";*/
        String status="";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_update = new ContentValues();
        values_update.put(KEY_DEMANDGENERATION_STATUS,"1");
        int i=db.update(TABLE_DEMANDGENERATION,
                values_update,
                KEY_USER_ID + " = ? AND " + KEY_DEMANDGENERATION_PRIMARY_ID + " = ?",
                new String[]{userId, dg_id});
        if(i>0) status="Sucess";
        db.close();

        return  status;
    }
    public String update_credit_limit(String customer_id, String division_id,String FinalCreditAmount) {

        String status="";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_update = new ContentValues();
        values_update.put(KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT,FinalCreditAmount);

        int i=db.update(TABLE_CUSTOMER_DETAILS,
                values_update,
                KEY_CUSTOMER_ID + " = ? AND " + KEY_DIVISION_ID + " = ?",
                new String[]{customer_id, division_id});
        if(i>0) status="Sucess";
        db.close();

        return  status;
    }
    public String update_Coupan_Server_Status(String userId, String coupon_id) {

        String status="";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_update = new ContentValues();
        values_update.put(KEY_FARMER_SERVERSTATUS,"1");
        int i=db.update(TABLE_COUPANS,
                values_update,
                KEY_FARMER_USER_ID + " = ? AND " + KEY_FARMER_COUPAN_ID + " = ?",
                new String[]{userId, coupon_id});
        if(i>0) status="Sucess";
        db.close();

        return  status;
    }
    public String update_COUPANS(String COUPANID,String uid,String name,String mobile,String adhar,String address,String location,String village,String thaluka) {
        /*SQLiteDatabase db = this.getWritableDatabase();
        //update demand_generation set status =1 where ids =2019-10-17 andplanneddate =42event =Yield estimation and user_id =674
        db.execSQL("update "+TABLE_DEMANDGENERATION+" set "+KEY_DEMANDGENERATION_STATUS+" ="+'1'+" where "+KEY_DEMANDGENERATION_EVENT+" ='"+event+"' and "+KEY_DEMANDGENERATION_PlANNEDDATE+" ='"+date+"' and "+KEY_USER_ID+" ="+userId);
        Log.d("qqqq","update "+TABLE_DEMANDGENERATION+" set "+KEY_DEMANDGENERATION_STATUS+" ="+'1'+" where "+KEY_TABLE_CROPS_CROP_ID+" ="+cropId+" and "+KEY_DEMANDGENERATION_PlANNEDDATE+" ="+date+" and "+KEY_DEMANDGENERATION_EVENT+" ='"+event+"' and "+KEY_USER_ID+" ="+userId);
        db.close(); // Closing database connection
        return  "Sucess";*/
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String status="";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_update = new ContentValues();
        values_update.put(KEY_FARMER_USER_ID,uid);
        values_update.put(KEY_FARMER_NAME,name);
        values_update.put(KEY_FARMER_MOBILE,mobile);
        values_update.put(KEY_FARMER_FARMER_ADHAR_NO,adhar);
        values_update.put(KEY_FARMER_ADDRESS,address);
        values_update.put(KEY_FARMER_LOCATION,location);
        values_update.put(KEY_FARMER_VILLAGE,village);
        values_update.put(KEY_FARMER_THALUKA,thaluka);
        values_update.put(KEY_FARMER_SERVERSTATUS,"0");
        values_update.put(KEY_FARMER_UPDATED_DATETIME,sdf.format(c.getTime()));
        int i=db.update(TABLE_COUPANS,
                values_update,
                KEY_FARMER_UNIQUE_NO + " = ? ",
                new String[]{COUPANID});
        if(i>0) status="Sucess";
        db.close();

        return  status;
    }



    private static final String CREATE_TABLE_GRADE = "CREATE TABLE "
            + TABLE_GRADE + "(" + GRADE_ID + " INTEGER ,"
            + GRADE_NAME + " TEXT,"
            + PRICE_PER_KM + " TEXT" + ")";

    private static final String sql_version_control = "CREATE TABLE "
            + TABLE_VERSION_CONTROL + "(" + KEY_ID + " INTEGER ,"
            + VERSION_TABLE_NAME + " TEXT,"
            + UPDATED_DATE + " TEXT,"
            + VERSION_CODE + " TEXT" + ")";

//*tfa

    private static final String CREATE_TFA_APPROVAL_HISTORY = "CREATE TABLE " + TABLE_TFA_APPROVAL_HISTORY+ "(" +
            KEY_tfa_approval_id + " INTEGER PRIMARY KEY," +
            KEY_tfa_list_id + " INTEGER," +
            KEY_tfa_approval_status + " INTEGER," +
            KEY_tfa_approval_comment + " INTEGER," +
            KEY_tfa_approved_user_id + " INTEGER," +
            KEY_status + " INTEGER," +
            KEY_sync_status+ " INTEGER," +

            KEY_tfa_approval_name + " TEXT," +
            KEY_tfa_approval_role + " INTEGER," +
            KEY_tfa_approval_mail+ " TEXT," +
            KEY_tfa_approval_mobile+ " TEXT," +

            KEY_tfa_pending_by_name+ " TEXT," +
            KEY_tfa_pending_by_role+ " TEXT," +

            KEY_created_datetime + " TEXT," +
            KEY_updated_datetime+ " TEXT" + ")";

    private static final String CREATE_TFA_ACTIVITYLIST = "CREATE TABLE " + TABLE_TFA_ACTIVITYLIST+ "(" +
            KEY_tfa_list_id + " INTEGER PRIMARY KEY," +
            KEY_district_id + " INTEGER," +
            KEY_division_id + " INTEGER," +
            KEY_crop_id + " INTEGER," +
            KEY_product_id + " INTEGER," +
            KEY_tfa_activity_master_id + " INTEGER," +
            KEY_activity_date + " TEXT," +
            KEY_taluka + " TEXT," +
            KEY_village + " TEXT," +
            KEY_no_of_farmers + " INTEGER," +
            KEY_estimation_per_head + " INTEGER," +
            KEY_total_expences + " INTEGER," +
            KEY_advance_required + " INTEGER," +
            KEY_conducting_place + " TEXT," +
            KEY_user_id + " INTEGER," +
            KEY_created_user_id + " INTEGER," +
            KEY_user_email + " TEXT," +
            KEY_status + " INTEGER," +
            KEY_approval_status+ " INTEGER," +
            KEY_approval_comments + " TEXT," +
            KEY_approved_by+ " INTEGER," +
            KEY_approved_date+ " TEXT," +
            KEY_sync_status+ " INTEGER," +
            KEY_created_datetime + " TEXT," +
            KEY_updated_datetime+ " TEXT,"+
            KEY_used_farmers + " INTEGER," +
            KEY_non_used_farmers + " INTEGER," +
            KEY_actual_no_farmers + " INTEGER," +
            KEY_actual__estimation_per_head + " INTEGER," +
            KEY_actual__total_expences + " INTEGER," +
            KEY_location_lat_lng + " TEXT," +
            KEY_owner_number + " TEXT," +
            KEY_owner_name + " TEXT"
            + ")";
    private static final String CREATE_TFA_VILLAGE_LIST= "CREATE TABLE " + TABLE_TFA_VILLAGELIST+ "(" +
            KEY_tfa_village_id + " INTEGER PRIMARY KEY," +
            KEY_tfa_list_id + " INTEGER," +
            KEY_village_name + " TEXT," +
            KEY_current_sal + " TEXT," +
            KEY_potential + " TEXT," +
            KEY_sync_status + " INTEGER," +
            KEY_status + " INTEGER," +
            KEY_created_datetime + " TEXT," +
            KEY_updated_datetime+ " TEXT" + ")";


    private static final String CREATE_TFA_ACTIVITY_MASTER= "CREATE TABLE " + TABLE_TFA_ACTIVITY_MASTER+ "(" +
            KEY_tfa_master_id + " INTEGER," +
            KEY_tfa_title + " TEXT," +
            KEY_status + " INTEGER," +
            KEY_created_datetime + " TEXT," +
            KEY_updated_datetime+ " TEXT" + ")";

    private static final String ALTER_USER_TABLE_ADD_STATUS =
            "ALTER TABLE user_company_customer ADD " + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " TEXT DEFAULT 1";
    private static final String ALTER_USER_TABLE_ADD_MAP_DATE =
            "ALTER TABLE user_company_customer ADD " + KEY_TABLE_USER_COMPANY_CUSTOMER__MAP_START_DATE + " TEXT";
    private static final String ALTER_USER_TABLE_ADD_UNMAP_DATE =
            "ALTER TABLE user_company_customer ADD " + KEY_TABLE_USER_COMPANY_CUSTOMER_UNMAP_DATE + " TEXT";


    private static final String ALTER_METER_READING_CHECKIN_IMAGE = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + METER_READING_CHECKIN_IMAGE + " TEXT";
    private static final String ALTER_METER_READING_CHECKIN_TEXT = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + METER_READING_CHECKIN_TEXT + " TEXT";
    private static final String ALTER_METER_READING_CHECKOUT_IMAGE = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + METER_READING_CHECKOUT_IMAGE + " TEXT";
    private static final String ALTER_METER_READING_CHECKOUT_TEXT = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + METER_READING_CHECKOUT_TEXT + " TEXT";
    private static final String ALTER_VEHICLE_TYPE = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + VEHICLE_TYPE + " TEXT";
    private static final String ALTER_CHECKIN_COMMENT = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + CHECKIN_COMMENT + " TEXT DEFAULT null";
    private static final String ALTER_PERSONAL_USES_KM = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + PERSONAL_USES_KM + " TEXT";
    private static final String ALTER_PAUSE = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + PAUSE + " TEXT";
    private static final String ALTER_RESUME = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + RESUME + " TEXT";
    private static final String ALTER_SYNC_STATUS = "ALTER TABLE " + TABLE_GEO_TRACKING + " ADD " + SYNC_STATUS + " INTEGER DEFAULT 1";

    private SQLiteDatabase dbsql;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

     /*   super(context, Environment.getExternalStorageDirectory()
                + File.separator + "NSL.db"
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);

    */
        this.context = context;

        sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString(Constants.SharedPrefrancesKey.USER_ID, null);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        Common.Log.i("onUpgrade..");
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_TABLE_COMPANIES);
        db.execSQL(CREATE_TABLE_COMPANY_DIVISION_CROPS);
        db.execSQL(CREATE_TABLE_USER_COMPANY_CUSTOMER);
        db.execSQL(CREATE_TABLE_USER_COMPANY_DIVISION);
        db.execSQL(CREATE_TABLE_COMPLAINTS);
        db.execSQL(CREATE_TABLE_SCHEME_PRODUCTS);


        db.execSQL(CREATE_TABLE_COMPLAINTS_NEW);
        db.execSQL(CREATE_TABLE_CROPS);
        db.execSQL(CREATE_TABLE_CUSTOMERS);
        db.execSQL(CREATE_TABLE_CUSTOMER_DETAILS);
        db.execSQL(CREATE_TABLE_DISTRIBUTOR_RETAILER);
        db.execSQL(CREATE_TABLE_DIVISION);
        db.execSQL(CREATE_TABLE_ERRORS);
        db.execSQL(CREATE_TABLE_EVENT_MANAGMENT);
        db.execSQL(CREATE_TABLE_FARMERS);
        db.execSQL(CREATE_TABLE_GEO_TRACKING);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_USER_REGIONS);
        db.execSQL(CREATE_TABLE_UCDD);
        db.execSQL(CREATE_TABLE_SM);
        db.execSQL(CREATE_TABLE_SMD);
        db.execSQL(sqltable_stock_movement_retailer_details);
        db.execSQL(CREATE_TABLE_SERVICEORDER);
        db.execSQL(CREATE_TABLE_SERVICEORDERDETAILS);
        db.execSQL(CREATE_TABLE_SERVICE_ORDER_HISTORY);
        db.execSQL(CREATE_TABLE_PAYMENT_COLLECTION);
        db.execSQL(CREATE_TABLE_EMPLOYEE_VISIT_MANAGEMENT);
        db.execSQL(CREATE_TABLE_REGION);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_PRODUCT_PRICE);
        db.execSQL(CREATE_TABLE_SCHEMES);
        db.execSQL(CREATE_TABLE_FEEDBACK);
        db.execSQL(CREATE_DAILYDAIRY_TABLE);
        db.execSQL(CREATE_TABLE_CUSTOMER_BANKDETAILS);
//        db.execSQL(CREATE_TABLE_MI_COMMODITY_PRICE);
        db.execSQL(CREATE_TABLE_MI_CROP_SHIFTS);
        db.execSQL(CREATE_TABLE_MI_PRICE_SURVEY);
        db.execSQL(CREATE_TABLE_MI_PRODUCT_SURVEY);
        db.execSQL(CREATE_TABLE_RETAILER);
        db.execSQL(CREATE_TABLE_GRADE);

        db.execSQL(sql_version_control);
        db.execSQL(CREATE_TABLE_STOCK_RETURNS);
        db.execSQL(CREATE_TABLE_STOCK_RETURNS_DETAILS);
        db.execSQL(CREATE_CATALOGUE_CROP);
        db.execSQL(CREATE_CATALOGUE_CROP_PRODUCTS);
        db.execSQL(CREATE_SERVICE_ORDER_APPROVAL);
//        db.execSQL(CREATE_PLANNER_APPROVAL);
        db.execSQL(CREATE_DISTRICT);
        db.execSQL(CREATE_SEASON);
        db.execSQL(CREATE_SEASON_LINE_ITEMS);
        db.execSQL(CREATE_YIELD_ESTIMATION);
        db.execSQL(CREATE_GODOWN);
        db.execSQL(CREATE_GODOWN_STOCK);
        db.execSQL(CREATE_STOCK_DISPATCH);
        db.execSQL(CREATE_STOCK_DISPATCH_ITEM);
        db.execSQL(CREATE_RETAILER_STOCK_SUPPLY);

        db.execSQL(CREATE_COMPETITOR_CHANNEL);
        db.execSQL(CREATE_PRODUCT_PRICING_SURVEY);
        db.execSQL(CREATE_CROP_SHIFTS);
        db.execSQL(CREATE_COMPETITOR_STRENGTH);
        db.execSQL(CREATE_CHANNEL_PREFERENCE);
        db.execSQL(CREATE_COMMODITY_PRICE);
        db.execSQL(CREATE_MARKET_POTENTIAL);

        db.execSQL(CREATE_TABLE_DEMANDGENERATION);

        db.execSQL(CREATE_TABLE_FARMER_COUPANS);


        db.execSQL(CREATE_TFA_APPROVAL_HISTORY);
        db.execSQL(CREATE_TFA_ACTIVITYLIST);
        db.execSQL(CREATE_TFA_VILLAGE_LIST);
        db.execSQL(CREATE_TFA_ACTIVITY_MASTER);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //  boolean status = isFieldExist(db, TABLE_GEO_TRACKING, SYNC_STATUS);
        Common.Log.i("onUpgrade" + "oldVersion :" + oldVersion + "\n newVersion: " + newVersion);
       if (newVersion==DATABASE_VERSION){

           db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKET_POTENTIAL);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPETITOR_CHANNEL);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPETITOR_STRENGTH);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNEL_PREFERENCE);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMODITY_PRICE);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_PRICING_SURVEY);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_CROP_SHIFTS);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEMANDGENERATION);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPANS);

           db.execSQL("DROP TABLE IF EXISTS " + TABLE_TFA_APPROVAL_HISTORY);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_TFA_ACTIVITYLIST);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_TFA_VILLAGELIST);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_TFA_ACTIVITY_MASTER);

           db.execSQL(CREATE_COMPETITOR_CHANNEL);
           db.execSQL(CREATE_PRODUCT_PRICING_SURVEY);
           db.execSQL(CREATE_CROP_SHIFTS);
           db.execSQL(CREATE_COMPETITOR_STRENGTH);
           db.execSQL(CREATE_CHANNEL_PREFERENCE);
           db.execSQL(CREATE_COMMODITY_PRICE);
           db.execSQL(CREATE_MARKET_POTENTIAL);
           db.execSQL(CREATE_TABLE_DEMANDGENERATION);
           db.execSQL(CREATE_TABLE_FARMER_COUPANS);

           db.execSQL(CREATE_TFA_APPROVAL_HISTORY);
           db.execSQL(CREATE_TFA_ACTIVITYLIST);
           db.execSQL(CREATE_TFA_VILLAGE_LIST);
           db.execSQL(CREATE_TFA_ACTIVITY_MASTER);
       }

        /* if (!status) {
            db.execSQL(ALTER_METER_READING_CHECKIN_IMAGE);
            db.execSQL(ALTER_METER_READING_CHECKIN_TEXT);
            db.execSQL(ALTER_METER_READING_CHECKOUT_IMAGE);
            db.execSQL(ALTER_METER_READING_CHECKOUT_TEXT);
            db.execSQL(ALTER_VEHICLE_TYPE);
            db.execSQL(ALTER_CHECKIN_COMMENT);
            db.execSQL(ALTER_PERSONAL_USES_KM);
            db.execSQL(ALTER_PAUSE);
            db.execSQL(ALTER_RESUME);
            db.execSQL(ALTER_SYNC_STATUS);
        }*/
       /* if (oldVersion < DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GEO_TRACKING);
           // db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATALOGUE_CROPS);
           // db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATALOGUE_CROPS_PRODUCTS);

           // db.execSQL(CREATE_CATALOGUE_CROP);
           // db.execSQL(CREATE_CATALOGUE_CROP_PRODUCTS);
            db.execSQL(CREATE_TABLE_GEO_TRACKING);
            db.execSQL(CREATE_TABLE_COMPLAINTS_NEW);
        }
        }*/


//        if (Common.haveInternet(context)) {
//            new Async_Logout().execute();
//        }
        // Drop older table if existed

      /*  db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY_DIVISION_CROPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_COMPANY_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_COMPANY_DIVISION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEME_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CROPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRIBUTOR_RETAILER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIVISION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ERRORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_MANAGMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GEO_TRACKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_REGIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UCDD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE_VISIT_MANAGEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_COLLECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICEORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICEORDERDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILYDAIRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_BANKDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MI_COMMODITY_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MI_CROP_SHIFTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MI_PRICE_SURVEY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MI_PRODUCT_SURVEY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETAILER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSION_CONTROL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_RETURNS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_RETURNS_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATALOGUE_CROPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATALOGUE_CROPS_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_ORDER_APPROVAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRICT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_ORDER_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKET_POTENTIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASON_LINE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_YE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GODOWN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GODOWN_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_DISPATCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_DISPATCH_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETAILER_STOCK_SUPPLY);

        // Create tables again
        onCreate(db);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Divisions", "false");
        // editor.putString("userId", "");
        editor.putBoolean("sync_master_data_on_new_update", true);
        editor.putInt(Constants.SharedPrefrancesKey.ROLE, 0);
        // editor.putString("fcm_id", "");
        editor.commit();

        //  context.deleteDatabase(DATABASE_NAME);

        Intent login = new Intent(context, SplashScreen.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(login);*/

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new order
    void addOrders(Orders orders) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_NAME, orders.getName()); // Contact Name
        values.put(KEY_ORDER_AMT, orders.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_ORDERS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new division
    public void addDivisions(Divisions divisions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_division = new ContentValues();
        values_division.put(KEY_TABLE_DIVISION_MASTER_ID, divisions.getDivMasterId());
        values_division.put(KEY_TABLE_DIVISION_NAME, divisions.getDivName());
        values_division.put(KEY_TABLE_DIVISION_CODE, divisions.getDivcode());
        values_division.put(KEY_TABLE_DIVISION_SAP_ID, divisions.getDivsapid());
        values_division.put(KEY_TABLE_DIVISION_STATUS, divisions.getDivstatus());
        values_division.put(KEY_TABLE_DIVISION_CREATED_DATETIME, divisions.getDivcdatetime());
        values_division.put(KEY_TABLE_DIVISION_UPDATED_DATETIME, divisions.getDivudatetime());

        if (isAlreadyRecordExist(db, divisions.getDivMasterId(), TABLE_DIVISION, KEY_TABLE_DIVISION_MASTER_ID) == 0) {
            // Inserting Row
            db.insert(TABLE_DIVISION, null, values_division);

            Log.d("User", "DIVISION Inserted");
        } else {
            Log.d("User", "DIVISION Updated");
            db.update(TABLE_DIVISION, values_division, KEY_TABLE_DIVISION_MASTER_ID + "=?", new String[]{divisions.getDivMasterId()});
        }

        db.close(); // Closing database connection
    }


    // Adding new company
    public void addCompanies(Companies companies) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_companies = new ContentValues();
        values_companies.put(KEY_TABLE_COMPANIES_MASTER_ID, companies.getCompanyMasterId());
        values_companies.put(KEY_TABLE_COMPANIES_NAME, companies.getCompanyName());
        values_companies.put(KEY_TABLE_COMPANIES_COMPANY_CODE, companies.getCompanycode()); // Contact Div_code
        values_companies.put(KEY_TABLE_COMPANIES_COMPANY_SAP_ID, companies.getCompanysapid()); // Contact DivName
        values_companies.put(KEY_TABLE_COMPANIES_COMPANY_STATUS, companies.getCompanystatus()); // Contact Div_code
        values_companies.put(KEY_TABLE_COMPANIES_CREATED_DATETIME, companies.getCompanycdatetime()); // Contact DivName
        values_companies.put(KEY_TABLE_COMPANIES_UPDATED_DATETIME, companies.getCompanyudatetime()); // Contact Div_code

        if (isAlreadyRecordExist(db, companies.getCompanyMasterId(), TABLE_COMPANIES, KEY_TABLE_COMPANIES_MASTER_ID) == 0) {
            // Inserting Row
            db.insert(TABLE_COMPANIES, null, values_companies);

            Log.d("User", "TABLE_COMPANIES Inserted");
        } else {
            Log.d("User", "TABLE_COMPANIES Updated");
            db.update(TABLE_COMPANIES, values_companies, KEY_TABLE_COMPANIES_MASTER_ID + "=?", new String[]{companies.getCompanyMasterId()});
        }

        db.close(); // Closing database connection
    }


    // Adding new company
    void addEVM(Employe_visit_management_pojo employe_visit_management_pojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_evm = new ContentValues();
        values_evm.put(KEY_EMP_VISIT_MASTER_ID, employe_visit_management_pojo.getEmp_visitMasterId());
        values_evm.put(KEY_EMP_TYPE, employe_visit_management_pojo.getEmpvisitstype());
        values_evm.put(KEY_EMP_GEO_TRACKING_ID, employe_visit_management_pojo.getEmpvisitgeotrackingid());
        values_evm.put(KEY_EMP_VISIT_USER_ID, employe_visit_management_pojo.getEmp_visit_userid());
        values_evm.put(KEY_EMP_VISIT_CUSTOMER_ID, employe_visit_management_pojo.getEmp_visit_customerid()); // Contact Div_code
        values_evm.put(KEY_EMP_VISIT_PLAN_TYPE, employe_visit_management_pojo.getEmp_visit_plantype()); // Contact DivName
        values_evm.put(KEY_EMP_PURPOSE_VISIT, employe_visit_management_pojo.get_emp_visit_purposevisit()); // Contact Div_code
        values_evm.put(KEY_EMP_PURPOSE_VISIT_IDS, employe_visit_management_pojo.get_emp_visit_purposevisit_ids()); // Contact Div_code
        values_evm.put(KEY_EMP_PLAN_DATE_TIME, employe_visit_management_pojo.getEmp_visit_plandatetime()); // Contact DivName
        values_evm.put(KEY_EMP_CONCERN_PERSON_NAME, employe_visit_management_pojo.getEmp_visit_concernpersonname()); // Contact Div_code
        values_evm.put(KEY_EMP_RETAILER_ID, employe_visit_management_pojo.get_emp_visit_retailer_id());
        values_evm.put(KEY_EMP_FARMER_ID, employe_visit_management_pojo.get_emp_visit_farmer_id());
        values_evm.put(KEY_EMP_MOBILE, employe_visit_management_pojo.getEmp_visit_mobile()); // Contact Div_code
        values_evm.put(KEY_EMP_VILLAGE, employe_visit_management_pojo.getEmp_visit_village());
        values_evm.put(KEY_EMP_LOCATION_ADDRESS, employe_visit_management_pojo.getEmp_visit_locationaddress());
        values_evm.put(KEY_EMP_VISIT_CROP_ID, employe_visit_management_pojo.get_emp_visit_crop_id());
        values_evm.put(KEY_EMP_REGION_ID, employe_visit_management_pojo.get_emp_visit_region_id());
        values_evm.put(KEY_EMP_LOCATION_DISTRICT, employe_visit_management_pojo.get_emp_visit_locationdistrict());
        values_evm.put(KEY_EMP_LOCATION_TALUKA, employe_visit_management_pojo.get_emp_visit_locationtaluka());
        values_evm.put(KEY_EMP_LOCATION_VILLAGE, employe_visit_management_pojo.get_emp_visit_locationvillage());
        values_evm.put(KEY_EMP_GSTIN_NO, employe_visit_management_pojo.get_emp_visit_gstinno());
        values_evm.put(KEY_EMP_FEILD_AREA, employe_visit_management_pojo.getEmp_visit_fieldarea());
        values_evm.put(KEY_EMP_CHECK_IN_TIME, employe_visit_management_pojo.getEmp_visit_checkintime());
        values_evm.put(KEY_EMP_COMMENTS, employe_visit_management_pojo.getEmp_visit_comments());
        values_evm.put(KEY_EMP_STATUS, employe_visit_management_pojo.getEmpvisitstatus());
        values_evm.put(KEY_EMP_APPROVAL_STATUS, employe_visit_management_pojo.getEmpvisitapproval_status());
        values_evm.put(KEY_EMP_EVENT_NAME, employe_visit_management_pojo.getEmpvisitevent_name());
        values_evm.put(KEY_EMP_EVENT_END_DATE, employe_visit_management_pojo.getEmpvisitend_date());
        values_evm.put(KEY_EMP_EVENT_PURPOSE, employe_visit_management_pojo.getEmpvisitevent_purpose());
        values_evm.put(KEY_EMP_EVENT_VENUE, employe_visit_management_pojo.getEmpvisitevent_venue());
        values_evm.put(KEY_EMP_EVENT_PARTICIPANTS, employe_visit_management_pojo.getEmpvisitevent_participants());
        values_evm.put(KEY_EMP_CREATED_BY, employe_visit_management_pojo.getEmp_visit_createdby());
        values_evm.put(KEY_EMP_UPDATED_BY, employe_visit_management_pojo.getEmp_visit_updatedby());
        values_evm.put(KEY_EMP_CREATED_DATETIME, employe_visit_management_pojo.getEmpvisitcdatetime());
        values_evm.put(KEY_EMP_UPDATE_DATETIME, employe_visit_management_pojo.getEmpvisitudatetime());
        values_evm.put(KEY_EMP_FFM_ID, employe_visit_management_pojo.getEmpvisitsffmid());
        values_evm.put(KEY_EMP_SERVER_FLAG, employe_visit_management_pojo.getServerFlag());

        if (isAlreadyRecordExist(db, String.valueOf(employe_visit_management_pojo.getID()), TABLE_EMPLOYEE_VISIT_MANAGEMENT, KEY_EMP_VISIT_ID) == 0) {
            db.insert(TABLE_EMPLOYEE_VISIT_MANAGEMENT, null, values_evm);
        } else {
            db.update(TABLE_EMPLOYEE_VISIT_MANAGEMENT, values_evm, KEY_EMP_VISIT_ID + "=?", new String[]{String.valueOf(employe_visit_management_pojo.getID())});
        }

        // Inserting Row
//        db.insert(TABLE_EMPLOYEE_VISIT_MANAGEMENT, null, values_evm);
        db.close(); // Closing database connection
    }

    public String getLatestDateFromTable(String tableName) {
        String query = null;
        switch (tableName) {
            case TABLE_EMPLOYEE_VISIT_MANAGEMENT:
                query = "SELECT MAX(" + KEY_EMP_CREATED_DATETIME + ") FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " WHERE " + KEY_EMP_CREATED_DATETIME + " <> 'null'";
                break;
            case TABLE_SERVICEORDER:
                query = "SELECT MAX(" + KEY_TABLE_SERVICEORDER_ORDERDATE + ") FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_ORDERDATE + " <> 'null'";
                break;

        }
        if (query == null)
            return null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        Common.closeCursor(cursor);
        return null;
    }


    //public void updateFFMId


    // Adding new REGION
    public void addRegions(Regions regions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_regions = new ContentValues();
        values_regions.put(KEY_REGION_NAME, regions.getRegionName());
        values_regions.put(KEY_REGION__MASTER_ID, regions.getRegionMasterId());
        values_regions.put(KEY_REGION_CODE, regions.getRegioncode());
        values_regions.put(KEY_REGION_STATUS, regions.getRegionstatus());


        if (isAlreadyRecordExist(db, regions.getRegionMasterId(), TABLE_REGION, KEY_REGION__MASTER_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_REGION, null, values_regions);

            Log.d("User", "TABLE_REGION Inserted");
        } else {
            Log.d("User", "TABLE_REGION Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_REGION, values_regions, KEY_REGION__MASTER_ID + "=?", new String[]{regions.getRegionMasterId()});
        }

        db.close(); // Closing database connection
    }


    // Adding new crop
    public void addCrops(Crops crops) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_crops = new ContentValues();
        values_crops.put(KEY_TABLE_CROPS_CROP_MASTER_ID, crops.getCropMasterId());
        values_crops.put(KEY_TABLE_CROPS_CROP_NAME, crops.getCropName());
        values_crops.put(KEY_TABLE_CROPS_CROP_CODE, crops.getCropcode());
        values_crops.put(KEY_TABLE_CROPS_CROP_SAP_ID, crops.getCropsapid());
        values_crops.put(KEY_TABLE_CROPS_DIVISION_ID, crops.getCropdivisionId());
        values_crops.put(KEY_TABLE_CROPS_CREATED_DATETIME, crops.getCropcdatetime());
        values_crops.put(KEY_TABLE_CROPS_UPDATED_DATETIME, crops.getCropudatetime());

        if (isAlreadyRecordExist(db, crops.getCropMasterId(), TABLE_CROPS, KEY_TABLE_CROPS_CROP_MASTER_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_CROPS, null, values_crops);

            Log.d("User", "TABLE_CROPS Inserted");
        } else {
            Log.d("User", "TABLE_CROPS Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_CROPS, values_crops, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?", new String[]{crops.getCropMasterId()});
        }
        //  db.close(); // Closing database connection
    }

    // Adding new user
    public void addusers(Users users) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_users = new ContentValues();
        values_users.put(KEY_TABLE_USERS_MASTER_ID, users.getUserMasterId());
        values_users.put(KEY_TABLE_USERS_FIRST_NAME, users.getUser_first_name());
        values_users.put(KEY_TABLE_USERS_LAST_NAME, users.getUser_last_name());
        values_users.put(KEY_TABLE_USERS_MOBILE_NO, users.getUser_mobile_no());
        values_users.put(KEY_TABLE_USERS_EMAIL, users.getUser_email());
        values_users.put(KEY_TABLE_USERS_SAP_ID, users.getUser_sap_id());
        values_users.put(KEY_TABLE_USERS_PASSWORD, users.getUser_password());
        values_users.put(KEY_TABLE_USERS_ROLE_ID, users.getUser_role_id());
        values_users.put(KEY_TABLE_USERS_REPORTING_MANAGER_ID, users.getUser_reporting_manager_id());
        values_users.put(KEY_TABLE_USERS_STATUS, users.getUserstatus());
        values_users.put(KEY_TABLE_USERS_CREATED_DATETIME, users.getUsercdatetime());
        values_users.put(KEY_TABLE_USERS_UPDATED_DATETIME, users.getUserudatetime());
        values_users.put(KEY_TABLE_USERS_DESIGNATION, users.getUserdesignation());
        values_users.put(KEY_TABLE_USERS_HEADQUARTER, users.getUserheadquarter());
        values_users.put(KEY_TABLE_USERS_LOCATION, users.getUserlocation());
        values_users.put(KEY_TABLE_USERS_REGION_ID, users.getUserregionId());
        values_users.put(KEY_TABLE_USERS_GRADE, users.getGrade());
        values_users.put(KEY_TABLE_USERS_COST_CENTER, users.getCost_center());
        values_users.put(KEY_TABLE_USERS_IMAGE, users.getImage());
        if (isAlreadyRecordExist(db, users.getUser_sap_id(), TABLE_USERS, KEY_TABLE_USERS_SAP_ID) == 0) {
            // Inserting Row
            // if (users.getUserstatus().equalsIgnoreCase("1")) {
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_USERS, null, values_users);
            Log.d("User", "User Inserted");
            //  }
        } else {
            Log.d("User", "User Updated");
           /* if (users.getUserstatus().equalsIgnoreCase("0")) {
                deleteDataById(TABLE_USERS, KEY_TABLE_USERS_MASTER_ID, users.getUserMasterId());
            } else {*/
            db = getWritableDbIfClosed(db);
            db.update(TABLE_USERS, values_users, KEY_TABLE_USERS_SAP_ID + "=?", new String[]{users.getUser_sap_id()});
            //}
        }
        db.close(); // Closing database connection
    }

    public int isAlreadyRecordExist(SQLiteDatabase db, String sap_id, String tableName, String whereCondition) {
        if (sap_id == null || sap_id.length() == 0)
            sap_id = "0";
        String selectQuerys = "SELECT  * FROM " + tableName + " where " + whereCondition + " = '" + sap_id + "'";
        //  SQLiteDatabase db = this.getWritableDatabase();
        //
        db = getWritableDbIfClosed(db);
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        try {
            if (cursor != null && db.isOpen()) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    Common.Log.i("1111SERVICE_ORDER_DETAILS cursor return 1");
                    return 1;
                } else {
                    Common.Log.i("1111SERVICE_ORDER_DETAILS cursor return 0");
                    return 0;
                }
            }

        } catch (Exception e) {
            Common.Log.i("exception " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        //  db.close();
        Common.Log.i("1111SERVICE_ORDER_DETAILS End return 0");
        return 0;

    }
    public int isAlreadyRecordExist2(SQLiteDatabase db, String sap_id, String tableName, String whereCondition,  String sap_id2,String whereCondition2) {
        if (sap_id == null || sap_id.length() == 0)
            sap_id = "0";
        String selectQuerys = "SELECT  event FROM " + tableName + " where " + whereCondition + " = '" + sap_id + "' " + whereCondition2 + "='"+sap_id2+"'   ";
        //  SQLiteDatabase db = this.getWritableDatabase();
        //
        db = getWritableDbIfClosed(db);
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        System.out.println("cursor count "+cursor.getCount());
        try {
            if (cursor != null && db.isOpen()) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    Common.Log.i("1111SERVICE_ORDER_DETAILS cursor return 1");
                    return 1;
                } else {
                    Common.Log.i("1111SERVICE_ORDER_DETAILS cursor return 0");
                    return 0;
                }
            }

        } catch (Exception e) {
            Common.Log.i("exception " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        //  db.close();
        Common.Log.i("1111SERVICE_ORDER_DETAILS End return 0");
        return 0;

    }


    // Updating new user
    void updateusers(String userId, String imageData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_users = new ContentValues();
        values_users.put(KEY_TABLE_USERS_IMAGE, imageData);

        // Inserting Row
        db.update(TABLE_USERS, values_users, KEY_TABLE_USERS_MASTER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close(); // Closing database connection
    }

    // Adding new COMPANY DIVISION crop
    void addCompany_division_crops(Company_division_crops company_division_crops) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_company_division_crops = new ContentValues();

        values_company_division_crops.put(KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID, company_division_crops.getCdcCompanyId());
        values_company_division_crops.put(KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID, company_division_crops.getCdcDivisionId());
        values_company_division_crops.put(KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID, company_division_crops.getCdcCropId());

        // Inserting Row
        db.insert(TABLE_COMPANY_DIVISION_CROPS, null, values_company_division_crops);
        db.close(); // Closing database connection
    }

    // Adding new USER COMPANY CUSTOMER
    public void addUser_Company_Customer(User_Company_Customer user_company_customer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_user_company_customer = new ContentValues();

        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_MASTER_ID, user_company_customer.getID());
        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID, user_company_customer.getuccUserId());
        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID, user_company_customer.getuccCompanyId());
        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID, user_company_customer.getuccCustomerId());
        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS, user_company_customer.getStatus());
        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER__MAP_START_DATE, user_company_customer.getMap_start_date());
        values_user_company_customer.put(KEY_TABLE_USER_COMPANY_CUSTOMER_UNMAP_DATE, user_company_customer.getUnmap_date());

        if (isAlreadyRecordExist(db, String.valueOf(user_company_customer.getID()), TABLE_USER_COMPANY_CUSTOMER, KEY_TABLE_USER_COMPANY_CUSTOMER_MASTER_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_USER_COMPANY_CUSTOMER, null, values_user_company_customer);
            Log.d("User", "TABLE_USER_COMPANY_CUSTOMER Inserted");
        } else {
            Log.d("User", "TABLE_USER_COMPANY_CUSTOMER Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_USER_COMPANY_CUSTOMER, values_user_company_customer, KEY_TABLE_USER_COMPANY_CUSTOMER_MASTER_ID + "=?", new String[]{String.valueOf(user_company_customer.getID())});
        }

        db.close(); // Closing database connection
    }

    // Adding new Scheme Products
    public void addScheme_Products(Scheme_Products scheme_products) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_user_company_customer = new ContentValues();
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID, scheme_products.getspscheme_id());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID, scheme_products.getspProductId());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_PRICE, scheme_products.getspproductprice());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_REGION_ID, scheme_products.getspRegionId());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_COMPANY_ID, scheme_products.getspCompanyId());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_CROP_ID, scheme_products.get_sp_crop_id());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID, scheme_products.get_sp_slab_id());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_BOOKING_INACTIVE, scheme_products.get_sp_booking_incentive());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM, scheme_products.get_sp_valid_from());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_VALID_TO, scheme_products.get_sp_valid_to());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_BOOKING_YEAR, scheme_products.get_sp_booking_year());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_SEASON_CODE, scheme_products.get_sp_season_code());
        values_user_company_customer.put(KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE, scheme_products.get_sp_extenstion_date());

        if (isAlreadyRecordExist(db, scheme_products.getspscheme_id(), TABLE_SCHEME_PRODUCTS, KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_SCHEME_PRODUCTS, null, values_user_company_customer);
            Log.d("User", "TABLE_SCHEME_PRODUCTS Inserted");
        } else {
            Log.d("User", "TABLE_SCHEME_PRODUCTS Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_SCHEME_PRODUCTS, values_user_company_customer, KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + "=?", new String[]{scheme_products.getspProductId()});
        }
        db.close(); // Closing database connection
    }

    // Adding new USER COMPANY Division
    public void addUser_Company_Division(User_Company_Division user_company_division) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_user_company_division = new ContentValues();

        values_user_company_division.put(KEY_TABLE_USER_COMPANY_DIVISION_USER_ID, user_company_division.getucdUserId());
        values_user_company_division.put(KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID, user_company_division.getucdCompanyId());
        values_user_company_division.put(KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID, user_company_division.getucdDivisionId());
        values_user_company_division.put(KEY_TABLE_USER_COMPANY_DIVISION_MASTER_ID, user_company_division.getID());

        if (isAlreadyRecordExist(db, String.valueOf(user_company_division.getID()), TABLE_USER_COMPANY_DIVISION, KEY_TABLE_USER_COMPANY_DIVISION_MASTER_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_USER_COMPANY_DIVISION, null, values_user_company_division);
            Log.d("User", "TABLE_USER_COMPANY_DIVISION Inserted");
        } else {
            Log.d("User", "TABLE_USER_COMPANY_DIVISION Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_USER_COMPANY_DIVISION, values_user_company_division, KEY_TABLE_USER_COMPANY_DIVISION_MASTER_ID + "=?", new String[]{String.valueOf(user_company_division.getID())});
        }
        db.close(); // Closing database connection
    }

    // Adding new product
    public void addProducts(Products_Pojo products_pojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_products = new ContentValues();
        values_products.put(KEY_PRODUCT_MASTER_ID, products_pojo.getProductMasterId());
        values_products.put(KEY_PRODUCT_NAME, products_pojo.getProductName());
        values_products.put(KEY_PRODUCT_DESCRIPTION, products_pojo.getProductDescription());
        values_products.put(KEY_PRODUCT_SAP_CODE, products_pojo.getProductSapCode());
        values_products.put(KEY_PRODUCT_CROP_ID, products_pojo.getProductcropid());
        values_products.put(KEY_PRODUCTS_COMPANY_ID, products_pojo.getProductcompanyid());
        values_products.put(KEY_PRODUCTS_DIVISION_ID, products_pojo.getProductdivisionid());
        values_products.put(KEY_PRODUCTS_REGION, products_pojo.getProductregeion());
        values_products.put(KEY_PRODUCTS_PRICE, products_pojo.getProductprice());
        values_products.put(KEY_PRODUCTS_PACKETS_COUNT, products_pojo.get_product_no_packets());
        values_products.put(KEY_PRODUCT_DISCOUNT, products_pojo.getProductdiscount());
        values_products.put(KEY_PRODUCT_FROM_DATE, products_pojo.getProductfromdate());
        values_products.put(KEY_PRODUCT_TO_DATE, products_pojo.getProducttodate());
        values_products.put(KEY_PRODUCT_STATUS, products_pojo.getProductstatus());
        values_products.put(KEY_PRODUCT_CREATED_DATETIME, products_pojo.getProductcdatetime());
        values_products.put(KEY_PRODUCT_UPDATED_DATETIME, products_pojo.getProductudatetime());
        values_products.put(KEY_PRODUCT_IMAGE, products_pojo.getProductImages());
        values_products.put(KEY_PRODUCT_VIDEO, products_pojo.getProductVideos());
        values_products.put(KEY_PRODUCTS_CATALOG_URL, products_pojo.get_product_catalog_url());
        values_products.put(KEY_PRODUCT_BRAND_NAME, products_pojo.getProduct_brand_name());


        if (isAlreadyRecordExist(db, products_pojo.getProductMasterId(), TABLE_PRODUCTS, KEY_PRODUCT_MASTER_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_PRODUCTS, null, values_products);
            Log.d("User", "TABLE_PRODUCTS Inserted");
        } else {
            Log.d("User", "TABLE_PRODUCTS Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_PRODUCTS, values_products, KEY_PRODUCT_MASTER_ID + "=?", new String[]{products_pojo.getProductMasterId()});
        }
        db.close(); // Closing database connection
    }

    public Regions getRegionById(String regionId) {
        Regions region = null;
        String selectQuery = "SELECT * FROM " + TABLE_REGION + " WHERE " + KEY_REGION__MASTER_ID + " = " + regionId + " AND " + KEY_REGION_STATUS + " = 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                region = new Regions();
                region._region_id = cursor.getInt(cursor.getColumnIndex(KEY_REGION_ID));
                region._region_master_id = cursor.getString(cursor.getColumnIndex(KEY_REGION__MASTER_ID));
                region._region_name = cursor.getString(cursor.getColumnIndex(KEY_REGION_NAME));
                region._region_code = cursor.getString(cursor.getColumnIndex(KEY_REGION_CODE));
                region._region_status = cursor.getString(cursor.getColumnIndex(KEY_REGION_STATUS));
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }


        return region;
    }

    public List<Regions> getRegionsByUserId(String userId) {
        List<Regions> regions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_REGIONS + " tur left join " + TABLE_REGION + " r ON (tur.region_id=r.region_id) where tur." + KEY_TABLE_USER_REGION_USER_ID + " =" + userId + " AND tur.status=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Regions region = new Regions();
                    region._region_id = cursor.getInt(cursor.getColumnIndex(KEY_REGION_ID));
                    region._region_master_id = cursor.getString(cursor.getColumnIndex(KEY_REGION__MASTER_ID));
                    region._region_name = cursor.getString(cursor.getColumnIndex(KEY_REGION_NAME));
                    region._region_code = cursor.getString(cursor.getColumnIndex(KEY_REGION_CODE));
                    region._region_status = cursor.getString(cursor.getColumnIndex(KEY_REGION_STATUS));
                    regions.add(region);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }


        return regions;
    }


    // Adding new product
    public void addProductPrice(Products_Pojo products_pojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_products = new ContentValues();
        values_products.put(KEY_PRODUCT_MASTER_ID, products_pojo.getProductMasterId());
        values_products.put(KEY_PRODUCTS_PRICE, products_pojo.getProductprice());
        values_products.put(KEY_PRODUCT_DISCOUNT, products_pojo.getProductdiscount());
        values_products.put(KEY_PRODUCT_FROM_DATE, products_pojo.getProductfromdate());
        values_products.put(KEY_PRODUCT_TO_DATE, products_pojo.getProducttodate());
        values_products.put(KEY_PRODUCT_STATUS, products_pojo.getProductstatus());
        values_products.put(KEY_TABLE_SCHEME_PRODUCTS_REGION_ID, products_pojo.getProductregeion());


        //  if ( isAlreadyRecordExist(db, products_pojo.getProductMasterId(), TABLE_PRODUCT_PRICE, KEY_TABLE_SCHEME_PRODUCTS_REGION_ID+"='"+products_pojo.getProductregeion()+"' AND "+KEY_PRODUCT_MASTER_ID) == 0 ) {
        // Inserting Row
        //  if (products_pojo.getProductregeion().equalsIgnoreCase("47"))
        db = getWritableDbIfClosed(db);
        db.insert(TABLE_PRODUCT_PRICE, null, values_products);
        Log.d("User", "TABLE_PRODUCT_PRICE Inserted");
        // } else {
        //    Log.d("User", "TABLE_PRODUCT_PRICE Updated");
        //    db.update(TABLE_PRODUCT_PRICE, values_products, KEY_PRODUCT_MASTER_ID + "=?", new String[]{products_pojo.getProductMasterId()});
        // }
        db.close(); // Closing database connection
    }

    // Adding new customers
    public void addCustomers(Customers customers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();
        values_customers.put(KEY_TABLE_CUSTOMER_MASTER_ID, customers.getCusMasterId());
        values_customers.put(KEY_TABLE_CUSTOMER_NAME, customers.getCusName());
        values_customers.put(KEY_TABLE_CUSTOMER_CODE, customers.getCuscode());
        values_customers.put(KEY_TABLE_CUSTOMER_ADDRESS, customers.getCusaddress());
        values_customers.put(KEY_TABLE_CUSTOMER_STREET, customers.getCusstreet());
        values_customers.put(KEY_TABLE_CUSTOMER_CITY, customers.getCus_city());
        values_customers.put(KEY_TABLE_CUSTOMER_COMPANY_ID, customers.getCuscompany_Id());
        values_customers.put(KEY_TABLE_CUSTOMER_REGION_ID, customers.getCusregion_Id());
        values_customers.put(KEY_TABLE_CUSTOMER_TELEPHONE, customers.getCustelephone());
        values_customers.put(KEY_TABLE_CUSTOMER_COUNTRY, customers.getCuscountry());
        values_customers.put(KEY_TABLE_CUSTOMER_STATUS, customers.getCusstatus());
        values_customers.put(KEY_TABLE_CUSTOMER_CREATED_DATETIME, customers.getCuscdatetime());
        values_customers.put(KEY_TABLE_CUSTOMER_UPDATED_DATETIME, customers.getCusudatetime());
        values_customers.put(KEY_TABLE_CUSTOMER_PASSWORD, customers.getCuspassword());
        values_customers.put(KEY_TABLE_CUSTOMER_EMAIL, customers.getCusEmail());
        values_customers.put(KEY_TABLE_CUSTOMER_STATE, customers.getCusState());
        values_customers.put(KEY_TABLE_CUSTOMER_DISTRICT, customers.getCusDistrict());
        values_customers.put(KEY_TABLE_CUSTOMER_LAT_LNG, customers.getCusLatlng());
        // Inserting Row
        // db.insert(TABLE_CUSTOMERS, null, values_customers);
        if (isAlreadyRecordExist(db, customers.getCusMasterId(), TABLE_CUSTOMERS, KEY_TABLE_CUSTOMER_MASTER_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_CUSTOMERS, null, values_customers);
            Log.d("User", "TABLE_CUSTOMERS Inserted");
        } else {
            Log.d("User", "TABLE_CUSTOMERS Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_CUSTOMERS, values_customers, KEY_TABLE_CUSTOMER_MASTER_ID + "=?", new String[]{customers.getCusMasterId()});
        }
        db.close(); // Closing database connection
    }

    public void addCustomer_details(Customer_Details customer_details) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues customerdetails = new ContentValues();
        //values_feedback.put(KEY_TABLE_FEEDBACK_FEEDBACK_ID, feedback.getID());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID, customer_details.get_customer_details_master_id());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID, customer_details.get_customer_details_division_id());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT, customer_details.get_customer_details_credit_limit());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET, customer_details.get_customer_details_inside_bucket());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET, customer_details.get_customer_details_outside_bucket());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_STATUS, customer_details.get_customer_details_status());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_CREATED_DATETIME, customer_details.get_customer_details_created_datetime());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_UPDATED_DATETIME, customer_details.get_customer_details_updated_datetime());
        customerdetails.put(KEY_TABLE_CUSTOMER_DETAILS_CREDIT_BALANCE, customer_details.get_customer_details_credit_balance());

        // Inserting Row
        // db.insert(TABLE_CUSTOMER_DETAILS, null, customerdetails);
        if (isAlreadyRecordExist(db, customer_details.get_customer_details_master_id(), TABLE_CUSTOMER_DETAILS, KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID + "=" + customer_details.get_customer_details_division_id() + " and " + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_CUSTOMER_DETAILS, null, customerdetails);
            Log.d("User", "TABLE_CUSTOMER_DETAILS Inserted");
        } else {
            Log.d("User", "TABLE_CUSTOMER_DETAILS Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_CUSTOMER_DETAILS, customerdetails, KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + "=? and " + KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID + "=?", new String[]{customer_details.get_customer_details_master_id(), customer_details.get_customer_details_division_id()});
        }

        db.close(); // Closing database connection
    }

    // Adding new Scheme
    public void addSchemes(Schemes schemes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_schemes = new ContentValues();
        values_schemes.put(KEY_SCHEMES_MASTER_ID, schemes.getschemeMasterId());
        values_schemes.put(KEY_SCHEMES_TITLE, schemes.getschemeName());
        values_schemes.put(KEY_SCHEMES_SAP_CODE, schemes.getscheme_sap_code());
        values_schemes.put(KEY_SCHEMES_FILE_LOCATION, schemes.getscheme_file_location());
        values_schemes.put(KEY_SCHEMES_STATUS, schemes.getscheme_status());
        if (isAlreadyRecordExist(db, schemes.getschemeMasterId(), TABLE_SCHEMES, KEY_SCHEMES_MASTER_ID) == 0) {
            // Inserting Row
            db = getWritableDbIfClosed(db);
            db.insert(TABLE_SCHEMES, null, values_schemes);
            Log.d("User", "TABLE_SCHEMES Inserted");
        } else {
            Log.d("User", "TABLE_SCHEMES Updated");
            db = getWritableDbIfClosed(db);
            db.update(TABLE_SCHEMES, values_schemes, KEY_SCHEMES_MASTER_ID + "=?", new String[]{schemes.getschemeMasterId()});
        }
        db.close(); // Closing database connection
    }

    // Adding new geo_tracking
    public void addGeotracking(Geo_Tracking_POJO geo_tracking_pojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_geotracking = new ContentValues();
        //values_geotracking.put(KEY_TABLE_GEO_TRACKING_ID, geo_tracking_pojo.getID());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_MASTER_ID, geo_tracking_pojo.getGeoMasterId());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_VISIT_TYPE, geo_tracking_pojo.getGeoVisitType());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_USER_ID, geo_tracking_pojo.get_Geo_user_id());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG, geo_tracking_pojo.get_Geo_check_in_lat_lon());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG, geo_tracking_pojo.getGeo_check_out_lat_lon());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG, geo_tracking_pojo.getGeo_route_path_lat_lon());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_DISTANCE, geo_tracking_pojo.getGeo_distance());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_VISIT_DATE, geo_tracking_pojo.getGeo_visit_date());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME, geo_tracking_pojo.getGeo_check_in_time());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME, geo_tracking_pojo.getGeo_check_out_time());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_STATUS, geo_tracking_pojo.getGeostatus());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_FFMID, geo_tracking_pojo.getGeoffmid());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_CREATED_DATETIME, geo_tracking_pojo.getGeocdatetime());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME, geo_tracking_pojo.getGeoudatetime());
        values_geotracking.put(KEY_TABLE_GEO_TRACKING_POLYLINE, geo_tracking_pojo.getPolyline());
        values_geotracking.put(SYNC_STATUS, 1);
        if (geo_tracking_pojo.getMeter_reading_checkin_image() != null)
            values_geotracking.put(METER_READING_CHECKIN_IMAGE, geo_tracking_pojo.getMeter_reading_checkin_image());
        if (geo_tracking_pojo.getMeter_reading_checkin_text() != null)
            values_geotracking.put(METER_READING_CHECKIN_TEXT, geo_tracking_pojo.getMeter_reading_checkin_text());
        if (geo_tracking_pojo.getMeter_reading_checkout_image() != null)
            values_geotracking.put(METER_READING_CHECKOUT_IMAGE, geo_tracking_pojo.getMeter_reading_checkout_image());
        if (geo_tracking_pojo.getMeter_reading_checkout_text() != null)
            values_geotracking.put(METER_READING_CHECKOUT_TEXT, geo_tracking_pojo.getMeter_reading_checkout_text());
        if (geo_tracking_pojo.getVehicle_type() != null)
            values_geotracking.put(VEHICLE_TYPE, geo_tracking_pojo.getVehicle_type());
        if (geo_tracking_pojo.getCheckin_comment() != null)
            values_geotracking.put(CHECKIN_COMMENT, geo_tracking_pojo.getCheckin_comment());
        if (geo_tracking_pojo.getPersonal_uses_km() != null)
            values_geotracking.put(PERSONAL_USES_KM, geo_tracking_pojo.getPersonal_uses_km());
        // Inserting Row
        db.insert(TABLE_GEO_TRACKING, null, values_geotracking);
//		Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection

    }

    // Adding Serviceorder
    public void addServiceorder(ServiceOrderMaster serviceOrderMaster) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_serviceorder = new ContentValues();
        // values_serviceorder.put(KEY_TABLE_SERVICEORDER_MASTER_ID,         serviceOrderMaster.getserviceorder_masterid());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_ORDERTYPE, serviceOrderMaster.getserviceorder_type());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_ORDERDATE, serviceOrderMaster.get_serviceorder_date()); // Contact Div_code
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_USER_ID, serviceOrderMaster.get_serviceorder_user_id());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_CUSTOMER_ID, serviceOrderMaster.getserviceorder_customer_id());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_DIVISION_ID, serviceOrderMaster.getserviceorder_division_id());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_COMPANY_ID, serviceOrderMaster.getserviceorder_company_id()); // Contact DivName
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_STATUS, serviceOrderMaster.getserviceorderstatus()); // Contact Div_code
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_CREATED_DATETIME, serviceOrderMaster.getserviceordercdatetime()); // Contact DivName
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_UPDATED_DATETIME, serviceOrderMaster.getserviceorderudatetime()); // Contact Div_code

        values_serviceorder.put(KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT, serviceOrderMaster.get_token_amount());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_CREATED_BY, serviceOrderMaster.get_created_by());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_APPROVAL_STATUS, serviceOrderMaster.get_approval_status());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS, serviceOrderMaster.get_approval_comments());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_SAP_ID, serviceOrderMaster.getServiceOrderSapId());
        values_serviceorder.put(KEY_TABLE_SERVICEORDER_SEASON, serviceOrderMaster.getSeason());

        if (serviceOrderMaster.getserviceorderffm_id() != "" && serviceOrderMaster.getserviceorderffm_id() != null && !serviceOrderMaster.getserviceorderffm_id().equalsIgnoreCase("0")) {
            values_serviceorder.put(KEY_TABLE_SERVICEORDER_FFM_ID, serviceOrderMaster.getserviceorderffm_id());

            if (isAlreadyRecordExist(db, String.valueOf(serviceOrderMaster.getserviceorderffm_id()), TABLE_SERVICEORDER, KEY_TABLE_SERVICEORDER_FFM_ID) == 0) {
                db.insert(TABLE_SERVICEORDER, null, values_serviceorder);
            } else {
                db.update(TABLE_SERVICEORDER, values_serviceorder, KEY_TABLE_SERVICEORDER_FFM_ID + "=?", new String[]{String.valueOf(serviceOrderMaster.getserviceorderffm_id())});
            }
        } else {
            db.insert(TABLE_SERVICEORDER, null, values_serviceorder);
        }
        db.close();
    }


    // Adding new customers
    void addCustomersBankDetails(Customer_Bank_Details customerBankDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();
        values_customers.put(KEY_BANKDETAIL_MASTER_ID, customerBankDetails.get_cus_bak_dtls_masterid());
        values_customers.put(KEY_BANKDETAIL_CUSTOMER_ID, customerBankDetails.get_customer_id());
        values_customers.put(KEY_BANKDETAIL_ACCOUNT_NUMBER, customerBankDetails.get_account_no());
        values_customers.put(KEY_BANKDETAIL_ACCOUNT_NAME, customerBankDetails.get_account_name());
        values_customers.put(KEY_BANKDETAIL_BANK_NAME, customerBankDetails.get_bank_name());
        values_customers.put(KEY_BANKDETAIL_BRANCH_NAME, customerBankDetails.get_branch_name());
        values_customers.put(KEY_BANKDETAIL_IFSC_CODE, customerBankDetails.get_ifsc_code());
        values_customers.put(KEY_BANKDETAIL_STATUS, customerBankDetails.get_status());
        values_customers.put(KEY_BANKDETAIL_CREATED_BY, customerBankDetails.get_created_by());
        values_customers.put(KEY_UPDATED_BY, customerBankDetails.get_updated_by());
        values_customers.put(KEY_CREATED_DATE, customerBankDetails.get_created_date());
        values_customers.put(KEY_BANKDETAIL_FFMID, customerBankDetails.get_ffmid());

        // Inserting Row
        db.insert(TABLE_CUSTOMER_BANKDETAILS, null, values_customers);
        db.close(); // Closing database connection
    }

    // Adding new customers
    public void addCommodityPrice(Commodity_Price commodity_price) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();

        values_customers.put(KEY_COMMODITY_PRICE_MASTER_ID, commodity_price.get_commodity_price_master_id());
        values_customers.put(KEY_COMMODITY_PRICE_CROP_NAME, commodity_price.get_commodity_price_crop_name());
        values_customers.put(KEY_COMMODITY_PRICE_VARIETY_TYPE, commodity_price.get_commodity_price_variety_type());
        values_customers.put(KEY_COMMODITY_PRICE_APMC_MANDI_PRICE, commodity_price.get_commodity_price_apmc_mandi_price());
        values_customers.put(KEY_COMMODITY_PRICE_COMMODITY_DEALER_AGENT_PRICE, commodity_price.get_commodity_price_commodity_dealer_agent_price());
        values_customers.put(KEY_COMMODITY_PRICE_PURCHASE_PRICE_BY_INDUSTRY, commodity_price.get_commodity_price_purchage_price_by_industry());
        values_customers.put(KEY_COMMODITY_PRICE_CREATED_BY, commodity_price.get_commodity_price_created_by());
        values_customers.put(KEY_COMMODITY_PRICE_CREATED_ON, commodity_price.get_commodity_price_created_on());
        values_customers.put(KEY_COMMODITY_PRICE_FFMID, commodity_price.get_commodity_price_ffmid());

        // Inserting Row
//        db.insert(TABLE_MI_COMMODITY_PRICE, null, values_customers);
        db.close(); // Closing database connection
    }

    // Adding new crop shifta
    public void addCropShifts(Crop_Shifts crop_shifts) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_customers = new ContentValues();
        values_customers.put(KEY_CROP_SHIFTS_MASTER_ID, crop_shifts.get_crop_shifts_master_id());
        values_customers.put(KEY_CROP_SHIFTS_CROP_NAME, crop_shifts.get_crop_shifts_crop_name());
        values_customers.put(KEY_CROP_SHIFTS_VARIETY_TYPE, crop_shifts.get_crop_shifts_variety_type());
        values_customers.put(KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA, crop_shifts.get_crop_shifts_previous_year_area());
        values_customers.put(KEY_CROP_SHIFTS_CURRENT_YEAR_EXPECTED_AREA, crop_shifts.get_crop_shifts_current_year_expected_area());
        values_customers.put(KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE, crop_shifts.get_crop_shifts_percentage_increase_decrease());
        values_customers.put(KEY_CROP_SHIFTS_REASON_CROP_SHIFT, crop_shifts.get_crop_shifts_reason_crop_shift());
        values_customers.put(KEY_CROP_SHIFTS_CREATED_BY, crop_shifts.get_crop_shifts_created_by());
        values_customers.put(KEY_CROP_SHIFTS_CREATED_ON, crop_shifts.get_crop_shifts_created_on());
        values_customers.put(KEY_CROP_SHIFTS_CROP_IN_SAVED_SEED, crop_shifts.get_crop_shifs_crop_in_saved_seed());
        values_customers.put(KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR, crop_shifts.get_crop_shifs_crop_in_previous_year());
        values_customers.put(KEY_CROP_SHIFTS_CURRENT_YEAR_SRR, crop_shifts.get_crop_shifs_crop_in_current_year());
        values_customers.put(KEY_CROP_SHIFTS_NEXT_YEAR_SRR, crop_shifts.get_crop_shifs_crop_in_next_year());
        values_customers.put(KEY_CROP_SHIFTS_FFMID, crop_shifts.get_crop_shifts_ffmid());

        // Inserting Row
        db.insert(TABLE_MI_CROP_SHIFTS, null, values_customers);
        db.close(); // Closing database connection
    }

    public void addPricingServey(Price_Survey price_survey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues pricing_servey = new ContentValues();

        pricing_servey.put(KEY_PRICE_SURVEY_MASTER_ID, price_survey.get_price_survey_master_id());
        pricing_servey.put(KEY_PRICE_SURVEY_COMPANY_NAME, price_survey.get_price_survey_company_name());
        pricing_servey.put(KEY_PRICE_SURVEY_PRODUCT_NAME, price_survey.get_price_survey_product_name());
        pricing_servey.put(KEY_PRICE_SURVEY_SKU_PACK_SIZE, price_survey.get_price_survey_sku_pack_size());
        pricing_servey.put(KEY_PRICE_SURVEY_RETAIL_PRICE, price_survey.get_price_survey_retail_price());
        pricing_servey.put(KEY_PRICE_SURVEY_INVOICE_PRICE, price_survey.get_price_survey_invoice_price());
        pricing_servey.put(KEY_PRICE_SURVEY_NET_DISTRIBUTOR_LANDING_PRICE, price_survey.get_price_survey_net_distributor_landing_price());
        pricing_servey.put(KEY_PRICE_SURVEY_CREATED_BY, price_survey.get_price_survey_created_by());
        pricing_servey.put(KEY_PRICE_SURVEY_CREATED_ON, price_survey.get_price_survey_created_on());
        pricing_servey.put(KEY_PRICE_SURVEY_FFMID, price_survey.get_price_survey_ffmid());

        // Inserting Row
        db.insert(TABLE_MI_PRICE_SURVEY, null, pricing_servey);
        db.close(); // Closing database connection
    }

    public void addProductServey(Product_Survey product_survey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues product_servey = new ContentValues();

        product_servey.put(KEY_PRODUCT_SURVEY_MASTER_ID, product_survey.get_product_survey_master_id());
        product_servey.put(KEY_PRODUCT_SURVEY_COMPANY_NAME, product_survey.get_product_survey_company_name());
        product_servey.put(KEY_PRODUCT_SURVEY_PRODUCT_NAME, product_survey.get_product_survey_product_name());
        product_servey.put(KEY_PRODUCT_SURVEY_NAME_OF_THE_CHECK_SEGMENT, product_survey.get_product_survey_name_of_the_check_segment());
        product_servey.put(KEY_PRODUCT_SURVEY_LAUNCH_YEAR, product_survey.get_product_survey_launch_year());
        product_servey.put(KEY_PRODUCT_SURVEY_NO_UNITS_SOLD, product_survey.get_product_survey_no_units_sold());
        product_servey.put(KEY_PRODUCT_SURVEY_AREA_CROP_SOWN_NEW_PRODUCT, product_survey.get_product_survey_area_crop_sown_new_product());
        product_servey.put(KEY_PRODUCT_SURVEY_REMARK_UNIQUE_FEATURE, product_survey.get_product_survey_remarks_unique_feature());
        product_servey.put(KEY_PRODUCT_SURVEY_CREATED_BY, product_survey.get_product_survey_created_by());
        product_servey.put(KEY_PRODUCT_SURVEY_CREATED_ON, product_survey.get_product_survey_created_on());
        product_servey.put(KEY_PRODUCT_SURVEY_FFMID, product_survey.get_product_survey_ffmid());

        // Inserting Row
        db.insert(TABLE_MI_PRODUCT_SURVEY, null, product_servey);
        db.close(); // Closing database connection
    }

    // get ServiceorderDetails advance booking
    public List<ServiceOrderMaster> getOfflineAdvanceBookingData() {

        List<ServiceOrderMaster> serviceOrderMasterList = new ArrayList<ServiceOrderMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_ORDERTYPE + ","
                + KEY_TABLE_SERVICEORDER_ORDERDATE + ","
                + KEY_TABLE_SERVICEORDER_USER_ID + ","
                + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + ","
                + KEY_TABLE_SERVICEORDER_DIVISION_ID + ","
                + KEY_TABLE_SERVICEORDER_COMPANY_ID + ","
                + KEY_TABLE_SERVICEORDER_STATUS + ","
                + KEY_TABLE_SERVICEORDER_CREATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_UPDATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_FFM_ID + ","
                + KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT


                + " FROM " + TABLE_SERVICEORDER + " WHERE (" + KEY_TABLE_SERVICEORDER_FFM_ID + " = '0' OR " + KEY_TABLE_SERVICEORDER_FFM_ID + "='' OR " + KEY_TABLE_SERVICEORDER_FFM_ID + " is null) AND " + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=2";
        Log.d("selectQuery1", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);


            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ServiceOrderMaster serviceOrderMasterData = new ServiceOrderMaster();
                    serviceOrderMasterData.setserviceorder_masterid(cursor.getString(0));
                    serviceOrderMasterData.setserviceorder_type(cursor.getString(1));
                    serviceOrderMasterData.set_serviceorder_date(cursor.getString(2));
                    serviceOrderMasterData.set_serviceorder_user_id(cursor.getString(3));
                    serviceOrderMasterData.setserviceorder_customer_id(cursor.getString(4));
                    serviceOrderMasterData.setserviceorder_division_id(cursor.getString(5));
                    serviceOrderMasterData.setserviceorder_company_id(cursor.getString(6));
                    serviceOrderMasterData.setserviceorderstatus(cursor.getString(7));
                    serviceOrderMasterData.setserviceordercdatetime(cursor.getString(8));
                    serviceOrderMasterData.setserviceorderffm_id(cursor.getString(10));
                    serviceOrderMasterData.set_token_amount(cursor.getString(11));

                    serviceOrderMasterData.setServiceOrderDetailMasterList(getServiceorderDetails(serviceOrderMasterData));
                    serviceOrderMasterData.setServiceOrderDetailMasterListGrpByCropID(getServiceorderDetailsGrpByCropID(serviceOrderMasterData));


                    // Adding contact to list
                    serviceOrderMasterList.add(serviceOrderMasterData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }

        // return serviceOrder Master list
        return serviceOrderMasterList;

    }

    // get ServiceorderDetails
    public List<ServiceOrderMaster> getOfflineServiceorders() {

        List<ServiceOrderMaster> serviceOrderMasterList = new ArrayList<ServiceOrderMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_ORDERTYPE + ","
                + KEY_TABLE_SERVICEORDER_ORDERDATE + ","
                + KEY_TABLE_SERVICEORDER_USER_ID + ","
                + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + ","
                + KEY_TABLE_SERVICEORDER_DIVISION_ID + ","
                + KEY_TABLE_SERVICEORDER_COMPANY_ID + ","
                + KEY_TABLE_SERVICEORDER_STATUS + ","
                + KEY_TABLE_SERVICEORDER_CREATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_UPDATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_FFM_ID + ","
                + KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT

                + " FROM " + TABLE_SERVICEORDER + " WHERE (" + KEY_TABLE_SERVICEORDER_FFM_ID + " = '0' OR " + KEY_TABLE_SERVICEORDER_FFM_ID + " IS NULL OR " + KEY_TABLE_SERVICEORDER_FFM_ID + "='') AND " + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=1";
        Log.d("selectQuery1 OrderIn", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);


            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ServiceOrderMaster serviceOrderMasterData = new ServiceOrderMaster();
                    serviceOrderMasterData.setID(cursor.getInt(0));
                    serviceOrderMasterData.setserviceorder_masterid(String.valueOf(cursor.getInt(0)));
                    serviceOrderMasterData.setserviceorder_type(cursor.getString(1));
                    serviceOrderMasterData.set_serviceorder_date(cursor.getString(2));
                    serviceOrderMasterData.set_serviceorder_user_id(cursor.getString(3));
                    serviceOrderMasterData.setserviceorder_customer_id(cursor.getString(4));
                    serviceOrderMasterData.setserviceorder_division_id(cursor.getString(5));
                    serviceOrderMasterData.setserviceorder_company_id(cursor.getString(6));
                    serviceOrderMasterData.setserviceorderstatus(cursor.getString(7));
                    serviceOrderMasterData.setserviceordercdatetime(cursor.getString(8));
                    serviceOrderMasterData.setserviceorderffm_id(cursor.getString(10));
                    serviceOrderMasterData.set_token_amount(cursor.getString(11));

                    serviceOrderMasterData.setServiceOrderDetailMasterList(getServiceorderDetails(serviceOrderMasterData));
                    serviceOrderMasterData.setServiceOrderDetailMasterListGrpByCropID(getServiceorderDetailsGrpByCropID(serviceOrderMasterData));


                    // Adding contact to list
                    serviceOrderMasterList.add(serviceOrderMasterData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }
        // return serviceOrder Master list
        return serviceOrderMasterList;

    }

    // get Serviceordersetails
    public List<ServiceOrderDetailMaster> getServiceorderDetails(ServiceOrderMaster serviceOrderMaster) {

        List<ServiceOrderDetailMaster> serviceOrderMasterList = new ArrayList<ServiceOrderDetailMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_DETAIL_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_STATUS + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_SLAB_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY
                + " FROM " + TABLE_SERVICEORDERDETAILS + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = " + serviceOrderMaster.getserviceorder_masterid();
        Log.d("selectQuery2 ", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderDetailMaster serviceOrderDetailMaster = new ServiceOrderDetailMaster();
                serviceOrderDetailMaster.setserviceorderdetail_masterid(cursor.getString(0));
                serviceOrderDetailMaster.setserviceorderdetailorderid_id(cursor.getString(1));
                serviceOrderDetailMaster.set_serviceorderdetail_crop_id(cursor.getString(2));
                serviceOrderDetailMaster.setserviceorderdetail_scheme_id(cursor.getString(3));
                serviceOrderDetailMaster.setserviceorderdetail_product_id(cursor.getString(4));
                serviceOrderDetailMaster.set_serviceorderdetail_quantity(cursor.getString(5));
                serviceOrderDetailMaster.setserviceorderdetail_order_price(cursor.getString(6));
                serviceOrderDetailMaster.setserviceorderdetail_advance_amount(cursor.getString(7));
                serviceOrderDetailMaster.setserviceorderdetail_status(cursor.getString(8));
                serviceOrderDetailMaster.setserviceorderdetailcdatetime(cursor.getString(9));
                serviceOrderDetailMaster.setSlabId(cursor.getString(11));
                serviceOrderDetailMaster.set_serviceorderdetail_order_quantity(cursor.getString(12));

                // Adding contact to list
                serviceOrderMasterList.add(serviceOrderDetailMaster);
            } while (cursor.moveToNext());
        }

        // return serviceOrder Master list
        return serviceOrderMasterList;

    }

    // get Serviceordersetails
    public List<ServiceOrderDetailMaster> getServiceorderDetailsGrpByCropID(ServiceOrderMaster serviceOrderMaster) {

        List<ServiceOrderDetailMaster> serviceOrderMasterList = new ArrayList<ServiceOrderDetailMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_DETAIL_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_STATUS + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME + ","
                + KEY_TABLE_SERVICEORDER_SLAB_ID + ","
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY
                + " FROM " + TABLE_SERVICEORDERDETAILS + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = " + serviceOrderMaster.getserviceorder_masterid() + " group by crop_id";
        Log.d("selectQuery2 ", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderDetailMaster serviceOrderDetailMaster = new ServiceOrderDetailMaster();
                serviceOrderDetailMaster.setserviceorderdetail_masterid(cursor.getString(0));
                serviceOrderDetailMaster.setserviceorderdetailorderid_id(cursor.getString(1));
                serviceOrderDetailMaster.set_serviceorderdetail_crop_id(cursor.getString(2));
                serviceOrderDetailMaster.setserviceorderdetail_scheme_id(cursor.getString(3));
                serviceOrderDetailMaster.setserviceorderdetail_product_id(cursor.getString(4));
                serviceOrderDetailMaster.set_serviceorderdetail_quantity(cursor.getString(5));
                serviceOrderDetailMaster.setserviceorderdetail_order_price(cursor.getString(6));
                serviceOrderDetailMaster.setserviceorderdetail_advance_amount(cursor.getString(7));
                serviceOrderDetailMaster.setserviceorderdetail_status(cursor.getString(8));
                serviceOrderDetailMaster.setserviceorderdetailcdatetime(cursor.getString(9));
                serviceOrderDetailMaster.setSlabId(cursor.getString(11));
                serviceOrderDetailMaster.set_serviceorderdetail_order_quantity(cursor.getString(12));

                // Adding contact to list
                serviceOrderMasterList.add(serviceOrderDetailMaster);
            } while (cursor.moveToNext());
        }

        // return serviceOrder Master list
        return serviceOrderMasterList;

    }


    // Adding Serviceorderdetails
    public void addServiceorderdetails(ServiceOrderDetailMaster serviceOrderDetailMaster) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_serviceorderdetails = new ContentValues();
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID, serviceOrderDetailMaster.getserviceorderdetail_masterid());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID, serviceOrderDetailMaster.get_serviceorderdetail_crop_id());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID, serviceOrderDetailMaster.getserviceorderdetail_scheme_id()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID, serviceOrderDetailMaster.getserviceorderdetail_product_id());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY, serviceOrderDetailMaster.get_serviceorderdetail_quantity());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE, serviceOrderDetailMaster.getserviceorderdetail_order_price());
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT, serviceOrderDetailMaster.getserviceorderdetail_advance_amount()); // Contact DivName
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_STATUS, serviceOrderDetailMaster.getserviceorderdetail_status()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME, serviceOrderDetailMaster.getserviceorderdetailcdatetime()); // Contact DivName
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_UPDATED_DATETIME, serviceOrderDetailMaster.getserviceorderdetailudatetime()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_FFM_ID, serviceOrderDetailMaster.getFfmID()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_SLAB_ID, serviceOrderDetailMaster.getSlabId()); // Contact Div_code
        values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY, serviceOrderDetailMaster.get_serviceorderdetail_order_quantity());
        // Inserting Row
        if (isAlreadyRecordExist(db, String.valueOf(serviceOrderDetailMaster.getserviceorderdetail_masterid()), TABLE_SERVICEORDERDETAILS, KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID) == 0) {
            values_serviceorderdetails.put(KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID, serviceOrderDetailMaster.getserviceorderdetailorderid_id());
            int id = (int) db.insert(TABLE_SERVICEORDERDETAILS, null, values_serviceorderdetails);
            Common.Log.i("1111SERVICE_ORDER_DETAILS_INSERT");
        } else {
            Common.Log.i("1111SERVICE_ORDER_DETAILS_UPDATE");
            db.update(TABLE_SERVICEORDERDETAILS, values_serviceorderdetails, KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID + "=?", new String[]{String.valueOf(serviceOrderDetailMaster.getserviceorderdetail_masterid())});
        }
        db.close(); // Closing database connection
    }

    public void updateServiceOrderDetails(ServiceOrderDetailMaster serviceOrderDetailMaster) {


    }

    public void addPaymentCollection(Payment_collection payment_collection) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_payment_collection = new ContentValues();
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_PAYMENT_MASTER_ID, payment_collection.getID());
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_PAYMENT_TYPE, payment_collection.get_payment_type());
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_USER_ID, payment_collection.get_user_id()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_COMPANY_ID, payment_collection.get_company_id()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_DIVISION_ID, payment_collection.get_division_id()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_CUSTOMER_ID, payment_collection.get_customer_id()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_TOTAL_AMOUNT, payment_collection.get_total_amount()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_PAYMENT_MODE, payment_collection.get_payment_mode());
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_BANK_NAME, payment_collection.get_bank_name());
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_RTGS_OR_NEFT_NO, payment_collection.get_rtgs_or_neft_no()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_PAYMENT_DATETIME, payment_collection.get_payment_datetime()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_DATE_ON_CHEQUE_NUMBER, payment_collection.get_date_on_cheque_no()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_CHEQUE_NO_DD_NO, payment_collection.get_cheque_no_dd_no()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_STATUS, payment_collection.get_status()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_CREATED_DATETIME, payment_collection.get_created_datetime()); // Contact DivName
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_UPDATEd_DATETIME, payment_collection.get_updated_datetime()); // Contact Div_code
        values_payment_collection.put(KEY_PAYMENT_COLLECTION_FFMID, payment_collection.get_ffmid());
        // Inserting Row
        db.insert(TABLE_PAYMENT_COLLECTION, null, values_payment_collection);
        db.close(); // Closing database connection
    }


    public void addDailyDairy(DailyDairy dailydairy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_dd = new ContentValues();
        //values_feedback.put(KEY_TABLE_FEEDBACK_FEEDBACK_ID, feedback.getID());
        values_dd.put(KEY_DD_MASTER_ID, dailydairy.getID());
        values_dd.put(KEY_DD_TITLE, dailydairy.get_title());
        values_dd.put(KEY_DD_USER_ID, dailydairy.get_userid());
        values_dd.put(KEY_DD_COMMENTS, dailydairy.get_comments());
        values_dd.put(KEY_DD_TIME_SLOT, dailydairy.get_time());
        values_dd.put(KEY_DD_DATE, dailydairy.get_date());
        values_dd.put(KEY_DD_CREATED_DATE, dailydairy.get_createddate());
        values_dd.put(KEY_DD_FFMID, dailydairy.get_ffmid());
        values_dd.put(KEY_DD_TYPE, dailydairy.get_type());
        values_dd.put(KEY_DD_TENTATIVE_TIME, dailydairy.get_tentative_time());
        values_dd.put(KEY_DD_STATUS, dailydairy.get_status());


        // Inserting Row
        db.insert(TABLE_DAILYDAIRY, null, values_dd);
//    Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }


    public List<DailyDairy> getAlldateDailyDairy(String userID, String item) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_DATE + " = '" + item + "'" + " AND " + KEY_DD_FFMID + " IS NOT NULL AND "+KEY_DD_TYPE +"= 1";
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_TYPE + " = 1" + " AND " + KEY_DD_DATE + " = '" + item + "'";

        Log.e("DD query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));


                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<DailyDairy> getAlldateDairyAdhoc(String userID, String item) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_TYPE + " IN(1,2) AND " + KEY_DD_DATE + " = '" + item + "'" + " ORDER BY " + KEY_DD_TYPE;
        Log.e("DD query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DailyDairy dailyDairy = new DailyDairy();
                    dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                    dailyDairy.set_title(cursor.getString(2));
                    dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                    dailyDairy.set_comments(cursor.getString(4));
                    dailyDairy.set_time(cursor.getString(5));
                    dailyDairy.set_date(cursor.getString(6));
                    dailyDairy.set_createddate(cursor.getString(7));
                    dailyDairy.set_ffmid(cursor.getString(8));
                    dailyDairy.set_type(cursor.getInt(10));
                    dailyDairy.set_tentative_time(cursor.getString(9));
                    dailyDairy.set_status(cursor.getInt(11));


                    // Adding contact to list
                    dairy.add(dailyDairy);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
        }
        // return contact list
        return dairy;
    }


    public int getAdhocCompletedPendingCount(String userID, String date, int status) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " IN(" + userID + ") AND " + KEY_DD_DATE + " = '" + date + "'" + " AND " + KEY_DD_STATUS + "= " + status + " AND " + KEY_DD_TYPE + "=2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.getCount();
        Log.e("DD query", selectQuery + "\n count:" + cursor.getCount());

        // return contact list
        return cursor.getCount();
    }

    public int getRetailersCount() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETAILER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.getCount();
        Log.e("DD query", selectQuery + "\n count:" + cursor.getCount());

        // return contact list
        return cursor.getCount();
    }

    public List<DailyDairy> getAllDailyDairy(String userID) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_FFMID + " IS NOT NULL";
        Log.e("DD query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));


                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<DailyDairy> getAdhocDailyDairy(String userID, String date) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userID + " AND " + KEY_DD_TYPE + " = 2" + " AND " + KEY_DD_DATE + " = '" + date + "'";
        Log.e("DD query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<DailyDairy> getAllnullDailyDairy(String userId) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userId + " AND " + KEY_DD_FFMID + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DailyDairy dailyDairy = new DailyDairy();
                    dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                    dailyDairy.set_title(cursor.getString(2));
                    dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                    dailyDairy.set_comments(cursor.getString(4));
                    dailyDairy.set_time(cursor.getString(5));
                    dailyDairy.set_date(cursor.getString(6));
                    dailyDairy.set_createddate(cursor.getString(7));
                    dailyDairy.set_ffmid(cursor.getString(8));
                    dailyDairy.set_type(cursor.getInt(9));
                    dailyDairy.set_tentative_time(cursor.getString(10));
                    dailyDairy.set_status(cursor.getInt(11));

                    // Adding contact to list
                    dairy.add(dailyDairy);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }

        // return contact list
        return dairy;
    }

    public List<DailyDairy> getAllnullAdhocDailyDairy(String userId) {
        List<DailyDairy> dairy = new ArrayList<DailyDairy>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DAILYDAIRY + " WHERE " + KEY_DD_USER_ID + " = " + userId + " AND " + KEY_DD_FFMID + " IS NULL AND " + KEY_DD_TYPE + "=2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DailyDairy dailyDairy = new DailyDairy();
                dailyDairy.setID(Integer.parseInt(cursor.getString(0)));
                dailyDairy.set_title(cursor.getString(2));
                dailyDairy.set_userid(Integer.parseInt(cursor.getString(3)));
                dailyDairy.set_comments(cursor.getString(4));
                dailyDairy.set_time(cursor.getString(5));
                dailyDairy.set_date(cursor.getString(6));
                dailyDairy.set_createddate(cursor.getString(7));
                dailyDairy.set_ffmid(cursor.getString(8));
                dailyDairy.set_type(cursor.getInt(10));
                dailyDairy.set_tentative_time(cursor.getString(9));
                dailyDairy.set_status(cursor.getInt(11));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }


    public List<Payment_collection> getAllPaymentCollectionhistory(String userId, String cust_id) {
        List<Payment_collection> paymentCollectionListList = new ArrayList<Payment_collection>();
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_COLLECTION + " PC LEFT JOIN " + TABLE_CUSTOMERS + " C ON PC." + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID + " WHERE PC." + KEY_PAYMENT_COLLECTION_USER_ID + " = " + userId + " AND PC." + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " = " + cust_id + " ORDER BY " + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("payment select ", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        Log.e("length", String.valueOf(paymentCollectionListList.size()));
        if (cursor.moveToFirst()) {
            do {
                Payment_collection pc = new Payment_collection();
                pc.setID(cursor.getString(0));
                pc.set_payment_type(cursor.getString(2));
                pc.set_user_id(cursor.getString(3));
                pc.set_company_id(cursor.getString(4));
                pc.set_division_id(cursor.getString(5));
                pc.set_customer_id(cursor.getString(6));
                pc.set_total_amount(cursor.getString(7));
                pc.set_payment_mode(cursor.getString(8));
                pc.set_bank_name(cursor.getString(9));
                pc.set_rtgs_or_neft_no(cursor.getString(10));
                pc.set_payment_datetime(cursor.getString(11));
                pc.set_date_on_cheque_no(cursor.getString(12));
                pc.set_cheque_no_dd_no(cursor.getString(13));
                pc.set_status(Integer.parseInt(cursor.getString(14)));
                pc.set_created_datetime(cursor.getString(15));
                pc.set_updated_datetime(cursor.getString(16));
                pc.set_ffmid(cursor.getString(17));
                // Adding contact to list
                paymentCollectionListList.add(pc);
            } while (cursor.moveToNext());
        }

        // return contact list
        return paymentCollectionListList;
    }

    public List<Payment_collection> getAllPaymentCollectionhistoryForABS(String userId, String cust_id, String division_id) {
        List<Payment_collection> paymentCollectionListList = new ArrayList<Payment_collection>();
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_COLLECTION + " PC LEFT JOIN " + TABLE_CUSTOMERS + " C ON PC." + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID + " WHERE PC." + KEY_PAYMENT_COLLECTION_USER_ID + " = " + userId + " AND PC." + KEY_PAYMENT_COLLECTION_CUSTOMER_ID + " = " + cust_id + " AND PC." + KEY_PAYMENT_COLLECTION_DIVISION_ID + " = " + division_id + " AND PC." + KEY_PAYMENT_COLLECTION_PAYMENT_TYPE + " = 1" + " ORDER BY " + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("payment select ", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        Log.e("length", String.valueOf(paymentCollectionListList.size()));
        if (cursor.moveToFirst()) {
            do {
                Payment_collection pc = new Payment_collection();
                pc.setID(cursor.getString(0));
                pc.set_payment_type(cursor.getString(2));
                pc.set_user_id(cursor.getString(3));
                pc.set_company_id(cursor.getString(4));
                pc.set_division_id(cursor.getString(5));
                pc.set_customer_id(cursor.getString(6));
                pc.set_total_amount(cursor.getString(7));
                pc.set_payment_mode(cursor.getString(8));
                pc.set_bank_name(cursor.getString(9));
                pc.set_rtgs_or_neft_no(cursor.getString(10));
                pc.set_payment_datetime(cursor.getString(11));
                pc.set_date_on_cheque_no(cursor.getString(12));
                pc.set_cheque_no_dd_no(cursor.getString(13));
                pc.set_status(Integer.parseInt(cursor.getString(14)));
                pc.set_created_datetime(cursor.getString(15));
                pc.set_updated_datetime(cursor.getString(16));
                pc.set_ffmid(cursor.getString(17));
                // Adding contact to list
                paymentCollectionListList.add(pc);
            } while (cursor.moveToNext());
        }

        // return contact list
        return paymentCollectionListList;
    }


    public List<Payment_collection> getAllPaymentCollection(String userId) {
        List<Payment_collection> paymentCollectionListList = new ArrayList<Payment_collection>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENT_COLLECTION + " WHERE " + KEY_PAYMENT_COLLECTION_USER_ID + " = " + userId + " AND " + KEY_PAYMENT_COLLECTION_FFMID + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Payment_collection pc = new Payment_collection();
                    pc.setID(cursor.getString(0));
                    pc.set_payment_type(cursor.getString(2));
                    pc.set_user_id(cursor.getString(3));
                    pc.set_company_id(cursor.getString(4));
                    pc.set_division_id(cursor.getString(5));
                    pc.set_customer_id(cursor.getString(6));
                    pc.set_total_amount(cursor.getString(7));
                    pc.set_payment_mode(cursor.getString(8));
                    pc.set_bank_name(cursor.getString(9));
                    pc.set_rtgs_or_neft_no(cursor.getString(10));
                    pc.set_payment_datetime(cursor.getString(11));
                    pc.set_date_on_cheque_no(cursor.getString(12));
                    pc.set_cheque_no_dd_no(cursor.getString(13));
                    pc.set_status(Integer.parseInt(cursor.getString(14)));
                    pc.set_created_datetime(cursor.getString(15));
                    pc.set_updated_datetime(cursor.getString(16));
                    pc.set_ffmid(cursor.getString(17));
                    // Adding contact to list
                    paymentCollectionListList.add(pc);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }
        db.close();
        // return contact list
        return paymentCollectionListList;
    }


    public List<Customer_BankDetails> getAllNullCustomerbankdetails(String cust_id) {
        List<Customer_BankDetails> dairy = new ArrayList<Customer_BankDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_BANKDETAILS + " WHERE " + KEY_BANKDETAIL_CUSTOMER_ID + " = '" + cust_id + "' AND " + KEY_BANKDETAIL_FFMID + " IS NULL";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_BankDetails dailyDairy = new Customer_BankDetails();
                dailyDairy.set_cus_bak_dtls_masterid(cursor.getString(1));
                dailyDairy.set_customer_id(cursor.getString(2));
                dailyDairy.set_account_no(cursor.getString(3));
                dailyDairy.set_account_name(cursor.getString(4));
                dailyDairy.set_bank_name(cursor.getString(5));
                dailyDairy.set_branch_name(cursor.getString(6));
                dailyDairy.set_ifsc_code(cursor.getString(7));
                dailyDairy.set_status(cursor.getString(8));
                dailyDairy.set_created_by(cursor.getString(9));
                dailyDairy.set_updated_by(cursor.getString(10));
                dailyDairy.set_created_date(cursor.getString(11));
                dailyDairy.set_ffmid(cursor.getString(12));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<Customer_Bank_Details> getAllCustomerbankdetails(String cust_id) {
        List<Customer_Bank_Details> dairy = new ArrayList<Customer_Bank_Details>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_BANKDETAILS + " WHERE " + KEY_BANKDETAIL_CUSTOMER_ID + " = '" + cust_id + "'";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_Bank_Details dailyDairy = new Customer_Bank_Details();
                dailyDairy.set_cus_bak_dtls_masterid(cursor.getString(1));
                dailyDairy.set_customer_id(cursor.getString(2));
                dailyDairy.set_account_no(cursor.getString(3));
                dailyDairy.set_account_name(cursor.getString(4));
                dailyDairy.set_bank_name(cursor.getString(5));
                dailyDairy.set_branch_name(cursor.getString(6));
                dailyDairy.set_ifsc_code(cursor.getString(7));
                dailyDairy.set_status(cursor.getString(8));
                dailyDairy.set_created_by(cursor.getString(9));
                dailyDairy.set_updated_by(cursor.getString(10));
                dailyDairy.set_created_date(cursor.getString(11));
                dailyDairy.set_ffmid(cursor.getString(12));

                // Adding contact to list
                dairy.add(dailyDairy);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;
    }

    public List<Commodity_Price> getAllnullCommodity_price(String userId) {
        List<Commodity_Price> commodity_prices = new ArrayList<Commodity_Price>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE + " WHERE " + KEY_COMMODITY_PRICE_CREATED_BY + " = " + userId + " AND " + KEY_COMMODITY_PRICE_FFMID + " IS NULL";
//        // String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE +  " WHERE " + KEY_COMMODITY_PRICE_CREATED_BY + " = " + userId +" AND "+KEY_COMMODITY_PRICE_FFMID +".compareTo('0') > 0";
//        Log.e("cmp query", selectQuery);
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Commodity_Price commodity_price = new Commodity_Price();
//                commodity_price.setID(Integer.parseInt(cursor.getString(0)));
//                commodity_price.set_commodity_price_master_id(cursor.getString(1));
//                commodity_price.set_commodity_price_crop_name(cursor.getString(2));
//                commodity_price.set_commodity_price_variety_type(cursor.getString(3));
//                commodity_price.set_commodity_price_apmc_mandi_price(cursor.getString(4));
//                commodity_price.set_commodity_price_commodity_dealer_agent_price(cursor.getString(5));
//                commodity_price.set_commodity_price_purchage_price_by_industry(cursor.getString(6));
//                commodity_price.set_commodity_price_created_by(cursor.getString(7));
//                commodity_price.set_commodity_price_created_on(cursor.getString(8));
//                commodity_price.set_commodity_price_ffmid(cursor.getString(9));
//
//                // Adding contact to list
//                commodity_prices.add(commodity_price);
//            } while (cursor.moveToNext());
//        }

        // return contact list
        return commodity_prices;
    }

    public List<Commodity_Price> getAllCommodity_price(String userId) {
        List<Commodity_Price> commodity_prices = new ArrayList<Commodity_Price>();
        // Select All Query
        //  String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE +  " WHERE " + KEY_COMMODITY_PRICE_CREATED_BY + " = " + userId +" AND "+KEY_COMMODITY_PRICE_FFMID +".compareTo(0) > 0";

//        String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE + " WHERE " + KEY_COMMODITY_PRICE_CREATED_BY + " = " + userId + " AND " + KEY_COMMODITY_PRICE_FFMID + " IS NOT NULL ORDER BY " + KEY_COMMODITY_PRICE_ID + " DESC";
//        //String selectQuery = "SELECT  * FROM " + TABLE_MI_COMMODITY_PRICE ;
//        Log.e("cmp query", selectQuery);
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Commodity_Price commodity_price = new Commodity_Price();
//                commodity_price.setID(Integer.parseInt(cursor.getString(0)));
//                commodity_price.set_commodity_price_master_id(cursor.getString(1));
//                commodity_price.set_commodity_price_crop_name(cursor.getString(2));
//                commodity_price.set_commodity_price_variety_type(cursor.getString(3));
//                commodity_price.set_commodity_price_apmc_mandi_price(cursor.getString(4));
//                commodity_price.set_commodity_price_commodity_dealer_agent_price(cursor.getString(5));
//                commodity_price.set_commodity_price_purchage_price_by_industry(cursor.getString(6));
//                commodity_price.set_commodity_price_created_by(cursor.getString(7));
//                commodity_price.set_commodity_price_created_on(cursor.getString(8));
//                commodity_price.set_commodity_price_ffmid(cursor.getString(9));
//
//                // Adding contact to list
//                commodity_prices.add(commodity_price);
//            } while (cursor.moveToNext());
//        }

        // return contact list
        return commodity_prices;
    }

    public List<Crop_Shifts> getAllnullCrop_Shifts(String userId) {
        List<Crop_Shifts> crop_shifts = new ArrayList<Crop_Shifts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_CROP_SHIFTS + " WHERE " + KEY_CROP_SHIFTS_CREATED_BY + " = " + userId + " AND " + KEY_CROP_SHIFTS_FFMID + " IS NULL";
        Log.e("cs query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Crop_Shifts crop_shifts1 = new Crop_Shifts();
                crop_shifts1.setID(cursor.getString(0));
                crop_shifts1.set_crop_shifts_master_id(cursor.getString(1));
                crop_shifts1.set_crop_shifts_crop_name(cursor.getString(2));
                crop_shifts1.set_crop_shifts_variety_type(cursor.getString(3));
                crop_shifts1.set_crop_shifts_previous_year_area(cursor.getString(4));
                crop_shifts1.set_crop_shifts_current_year_expected_area(cursor.getString(5));
                crop_shifts1.set_crop_shifts_percentage_increase_decrease(cursor.getString(6));
                crop_shifts1.set_crop_shifts_reason_crop_shift(cursor.getString(7));
                crop_shifts1.set_crop_shifts_created_by(cursor.getString(8));
                crop_shifts1.set_crop_shifts_created_on(cursor.getString(9));
                crop_shifts1.set_crop_shifs_crop_in_saved_seed(cursor.getString(10));
                crop_shifts1.set_crop_shifs_crop_in_previous_year(cursor.getString(11));
                crop_shifts1.set_crop_shifs_crop_in_current_year(cursor.getString(12));
                crop_shifts1.set_crop_shifs_crop_in_next_year(cursor.getString(13));
                crop_shifts1.set_crop_shifts_ffmid(cursor.getString(14));

                // Adding contact to list
                crop_shifts.add(crop_shifts1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return crop_shifts;
    }

    public List<Crop_Shifts> getAllCrop_Shifts(String userId) {
        List<Crop_Shifts> crop_shifts = new ArrayList<Crop_Shifts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_CROP_SHIFTS + " WHERE " + KEY_CROP_SHIFTS_CREATED_BY + " = " + userId + " AND " + KEY_CROP_SHIFTS_FFMID + " IS NOT NULL ORDER BY " + KEY_CROP_SHIFTS_ID + " DESC";
        Log.e("cs query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Crop_Shifts crop_shifts1 = new Crop_Shifts();
                crop_shifts1.setID(cursor.getString(0));
                crop_shifts1.set_crop_shifts_master_id(cursor.getString(1));
                crop_shifts1.set_crop_shifts_crop_name(cursor.getString(2));
                crop_shifts1.set_crop_shifts_variety_type(cursor.getString(3));
                crop_shifts1.set_crop_shifts_previous_year_area(cursor.getString(4));
                crop_shifts1.set_crop_shifts_current_year_expected_area(cursor.getString(5));
                crop_shifts1.set_crop_shifts_percentage_increase_decrease(cursor.getString(6));
                crop_shifts1.set_crop_shifts_reason_crop_shift(cursor.getString(7));
                crop_shifts1.set_crop_shifts_created_by(cursor.getString(8));
                crop_shifts1.set_crop_shifts_created_on(cursor.getString(9));
                crop_shifts1.set_crop_shifs_crop_in_saved_seed(cursor.getString(10));
                crop_shifts1.set_crop_shifs_crop_in_previous_year(cursor.getString(11));
                crop_shifts1.set_crop_shifs_crop_in_current_year(cursor.getString(12));
                crop_shifts1.set_crop_shifs_crop_in_next_year(cursor.getString(13));
                crop_shifts1.set_crop_shifts_ffmid(cursor.getString(14));

                // Adding contact to list
                crop_shifts.add(crop_shifts1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return crop_shifts;
    }


    public List<Price_Survey> getAllnullPrice_Survey(String checkuid) {
        List<Price_Survey> price_surveys = new ArrayList<Price_Survey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_PRICE_SURVEY + " WHERE " + KEY_PRICE_SURVEY_CREATED_BY + " = " + checkuid + " AND " + KEY_CROP_SHIFTS_FFMID + " IS NULL";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Price_Survey price_survey1 = new Price_Survey();
                price_survey1.setID(cursor.getInt(0));
                price_survey1.set_price_survey_master_id(cursor.getString(1));
                price_survey1.set_price_survey_company_name(cursor.getString(2));
                price_survey1.set_price_survey_product_name(cursor.getString(3));
                price_survey1.set_price_survey_sku_pack_size(cursor.getString(4));
                price_survey1.set_price_survey_retail_price(cursor.getString(5));
                price_survey1.set_price_survey_invoice_price(cursor.getString(6));
                price_survey1.set_price_survey_net_distributor_landing_price(cursor.getString(7));
                price_survey1.set_price_survey_created_by(cursor.getString(8));
                price_survey1.set_price_survey_created_on(cursor.getString(9));
                price_survey1.set_price_survey_ffmid(cursor.getString(10));

                // Adding contact to list
                price_surveys.add(price_survey1);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return price_surveys;
    }


    public List<Price_Survey> getAllPrice_Survey(String userId) {
        List<Price_Survey> price_surveys = new ArrayList<Price_Survey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_PRICE_SURVEY + " WHERE " + KEY_PRICE_SURVEY_CREATED_BY + " = " + userId + " AND " + KEY_PRICE_SURVEY_FFMID + " IS NOT NULL ORDER BY " + KEY_PRICE_SURVEY_ID + " DESC";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Price_Survey price_survey1 = new Price_Survey();
                price_survey1.setID(cursor.getInt(0));
                price_survey1.set_price_survey_master_id(cursor.getString(1));
                price_survey1.set_price_survey_company_name(cursor.getString(2));
                price_survey1.set_price_survey_product_name(cursor.getString(3));
                price_survey1.set_price_survey_sku_pack_size(cursor.getString(4));
                price_survey1.set_price_survey_retail_price(cursor.getString(5));
                price_survey1.set_price_survey_invoice_price(cursor.getString(6));
                price_survey1.set_price_survey_net_distributor_landing_price(cursor.getString(7));
                price_survey1.set_price_survey_created_by(cursor.getString(8));
                price_survey1.set_price_survey_created_on(cursor.getString(9));
                price_survey1.set_price_survey_ffmid(cursor.getString(10));

                // Adding contact to list
                price_surveys.add(price_survey1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return price_surveys;
    }


    public List<Product_Survey> getAllnullProduct_Survey(String checkuid) {
        List<Product_Survey> product_surveys = new ArrayList<Product_Survey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_PRODUCT_SURVEY + " WHERE " + KEY_PRODUCT_SURVEY_CREATED_BY + " = " + checkuid + " AND " + KEY_PRODUCT_SURVEY_FFMID + " IS NULL";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product_Survey product_survey1 = new Product_Survey();
                product_survey1.setID(cursor.getInt(0));
                product_survey1.set_product_survey_master_id(cursor.getString(1));
                product_survey1.set_product_survey_company_name(cursor.getString(2));
                product_survey1.set_product_survey_product_name(cursor.getString(3));
                product_survey1.set_product_survey_name_of_the_check_segment(cursor.getString(4));
                product_survey1.set_product_survey_launch_year(cursor.getString(5));
                product_survey1.set_product_survey_no_units_sold(cursor.getString(6));
                product_survey1.set_product_survey_area_crop_sown_new_product(cursor.getString(7));
                product_survey1.set_product_survey_remarks_unique_feature(cursor.getString(8));
                product_survey1.set_product_survey_created_by(cursor.getString(9));
                product_survey1.set_product_survey_created_by(cursor.getString(10));
                product_survey1.set_product_survey_ffmid(cursor.getString(11));

                // Adding contact to list
                product_surveys.add(product_survey1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return product_surveys;
    }


    public List<Product_Survey> getAllProduct_Survey(String checkuid) {
        List<Product_Survey> product_surveys = new ArrayList<Product_Survey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MI_PRODUCT_SURVEY + " WHERE " + KEY_PRODUCT_SURVEY_CREATED_BY + " = " + checkuid + " AND " + KEY_PRODUCT_SURVEY_FFMID + " IS NOT NULL ORDER BY " + KEY_PRODUCT_SURVEY_ID + " DESC";
        Log.e("cmp query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product_Survey product_survey1 = new Product_Survey();
                product_survey1.setID(cursor.getInt(0));
                product_survey1.set_product_survey_master_id(cursor.getString(1));
                product_survey1.set_product_survey_company_name(cursor.getString(2));
                product_survey1.set_product_survey_product_name(cursor.getString(3));
                product_survey1.set_product_survey_name_of_the_check_segment(cursor.getString(4));
                product_survey1.set_product_survey_launch_year(cursor.getString(5));
                product_survey1.set_product_survey_no_units_sold(cursor.getString(6));
                product_survey1.set_product_survey_area_crop_sown_new_product(cursor.getString(7));
                product_survey1.set_product_survey_remarks_unique_feature(cursor.getString(8));
                product_survey1.set_product_survey_created_by(cursor.getString(9));
                product_survey1.set_product_survey_created_on(cursor.getString(10));
                product_survey1.set_product_survey_ffmid(cursor.getString(11));

                // Adding contact to list
                product_surveys.add(product_survey1);
            } while (cursor.moveToNext());
        }

        // return contact list
        return product_surveys;
    }


    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PH_NO}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting single division
    Divisions getDivision(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DIVISION, new String[]{KEY_TABLE_DIVISION_ID,
                        KEY_TABLE_DIVISION_MASTER_ID, KEY_TABLE_DIVISION_NAME, KEY_TABLE_DIVISION_CODE, KEY_TABLE_DIVISION_SAP_ID, KEY_TABLE_DIVISION_STATUS, KEY_TABLE_DIVISION_CREATED_DATETIME, KEY_TABLE_DIVISION_UPDATED_DATETIME}, KEY_TABLE_DIVISION_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Divisions divisions = new Divisions(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        return divisions;
    }


    // Getting single COMPANY
    Companies getCompany(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COMPANIES, new String[]{KEY_TABLE_COMPANIES_ID,
                        KEY_TABLE_COMPANIES_MASTER_ID, KEY_TABLE_COMPANIES_NAME, KEY_TABLE_COMPANIES_COMPANY_CODE, KEY_TABLE_COMPANIES_COMPANY_SAP_ID, KEY_TABLE_COMPANIES_COMPANY_STATUS, KEY_TABLE_COMPANIES_CREATED_DATETIME, KEY_TABLE_COMPANIES_UPDATED_DATETIME}, KEY_TABLE_COMPANIES_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Companies companies = new Companies(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        return companies;
    }


    // Getting single Region
    Regions getRegion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REGION, new String[]{KEY_REGION_ID,
                        KEY_REGION__MASTER_ID, KEY_REGION_NAME, KEY_REGION_CODE, KEY_REGION_STATUS}, KEY_REGION_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Regions regions = new Regions(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return contact
        return regions;
    }

    // Getting single crop
    Crops getCrop(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CROPS, new String[]{KEY_TABLE_CROPS_CROP_ID,
                        KEY_TABLE_CROPS_CROP_MASTER_ID, KEY_TABLE_CROPS_CROP_NAME}, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Crops crops = new Crops(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        Log.v("Cropidis xyz", cursor.getString(1) + cursor.getString(2));
        return crops;
    }

    // Getting All Orders
    public List<Orders> getAllOrders() {
        List<Orders> ordersList = new ArrayList<Orders>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Orders orders = new Orders();
                orders.setID(Integer.parseInt(cursor.getString(0)));
                orders.setName(cursor.getString(1));
                orders.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                ordersList.add(orders);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ordersList;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting All Divisions
    public List<Divisions> getAllDivisions() {
        List<Divisions>
                divisionList = new ArrayList<Divisions>();
        try {
            //String selectQuery = "SELECT  * FROM " + TABLE_DIVISION;
            String selectQuery = "SELECT  * FROM " + TABLE_DIVISION + " where " + KEY_TABLE_DIVISION_MASTER_ID + " = 2  " + " or " + KEY_TABLE_DIVISION_MASTER_ID + " = 3  ";
            //	String selectQuery = "SELECT  * FROM " + TABLE_DIVISION + " where " + KEY_TABLE_DIVISION_MASTER_ID + " = " + example + "";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {

                    Divisions divisions = new Divisions();
                    divisions.setID(Integer.parseInt(cursor.getString(0)));
                    divisions.setDivMasterID(cursor.getString(1));
                    divisions.setDivName(cursor.getString(2));
                    divisions.setDivcode(cursor.getString(3));
                    divisions.setDivsapid(cursor.getString(4));
                    divisions.setDivstatus(cursor.getString(5));
                    divisions.setDivcdatetime(cursor.getString(6));
                    divisions.setDivudatetime(cursor.getString(7));
                    // Adding contact to list
                    divisionList.add(divisions);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }

        return divisionList;
    }

    // Getting All Companies
    public List<Companies> getAllCompanies() {
        List<Companies> companiesList = new ArrayList<Companies>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMPANIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Companies companies = new Companies();
                companies.setID(Integer.parseInt(cursor.getString(0)));
                companies.setCompanyMasterId(cursor.getString(2));
                companies.setCompanyName(cursor.getString(1));
                companies.setCompanycode(cursor.getString(3));
                companies.setCompanysapid(cursor.getString(4));
                companies.setCompanystatus(cursor.getString(5));
                companies.setCompanycdatetime(cursor.getString(6));
                companies.setCompanyudatetime(cursor.getString(7));
                // Adding contact to list
                companiesList.add(companies);
            } while (cursor.moveToNext());
        }

        // return contact list
        return companiesList;
    }


    // Getting All EVM
    /*public List<Employe_visit_management_pojo> getAllEVM() {
        List<Employe_visit_management_pojo> evmList = new ArrayList<Employe_visit_management_pojo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Employe_visit_management_pojo employe_visit_management_pojo =  new Employe_visit_management_pojo();
				employe_visit_management_pojo.setEmpvisitffmid(Integer.parseInt(cursor.getString(27)));
				employe_visit_management_pojo.setEmp_visitMasterId(cursor.getString(1));
				employe_visit_management_pojo.setEmp_visit_userid(cursor.getString(2));
				employe_visit_management_pojo.setEmp_visit_customerid(cursor.getString(3));
				employe_visit_management_pojo.setEmp_visit_plantype(cursor.getString(4));
				employe_visit_management_pojo.setEmp_visit_purposevisit(cursor.getString(5));
				employe_visit_management_pojo.setEmp_visit_plandatetime(cursor.getString(6));
				employe_visit_management_pojo.setEmp_visit_concernpersonname(cursor.getString(7));
				employe_visit_management_pojo.setEmp_visit_mobile(cursor.getString(8));
				employe_visit_management_pojo.setEmp_visit_village(cursor.getString(9));
				employe_visit_management_pojo.setEmp_visit_locationaddress(cursor.getString(10));
				employe_visit_management_pojo.setEmp_visit_fieldarea(cursor.getString(11));
				employe_visit_management_pojo.setEmp_visit_checkintime(cursor.getString(12));
				employe_visit_management_pojo.setEmp_visit_comments(cursor.getString(13));
				employe_visit_management_pojo.setEmpvisitstatus(cursor.getString(14));
				employe_visit_management_pojo.setEmp_visit_createdby(cursor.getString(15));
				employe_visit_management_pojo.setEmp_visit_updatedby(cursor.getString(16));
				// Adding contact to list
				evmList.add(employe_visit_management_pojo);
			} while (cursor.moveToNext());
		}

		// return contact list
		return evmList;
	}*/
    // Getting All Geo Tracking
    public List<Geo_Tracking_POJO> getUnSyncAllGeotracking(String userId) {
        List<Geo_Tracking_POJO> geotrackingList = new ArrayList<Geo_Tracking_POJO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GEO_TRACKING + " WHERE " + SYNC_STATUS + " IS NULL OR " + SYNC_STATUS + " =0 AND " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + userId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Geo_Tracking_POJO geo_tracking_pojo = new Geo_Tracking_POJO();
                    geo_tracking_pojo.setID(Integer.parseInt(cursor.getString(0)));
                    geo_tracking_pojo.setGeoMasterID(cursor.getString(2));
                    geo_tracking_pojo.setGeoVisitType(cursor.getString(1));
                    geo_tracking_pojo.set_Geo_user_id(cursor.getString(3));
                    geo_tracking_pojo.set_Geo_check_in_lat_lon(cursor.getString(4));
                    geo_tracking_pojo.setGeo_check_out_lat_lon(cursor.getString(5));
                    geo_tracking_pojo.setGeo_route_path_lat_lon(cursor.getString(6));
                    geo_tracking_pojo.setGeo_distance(cursor.getString(7));
                    geo_tracking_pojo.setGeo_visit_date(cursor.getString(8));
                    geo_tracking_pojo.setGeo_check_in_time(cursor.getString(9));
                    geo_tracking_pojo.setGeo_check_out_time(cursor.getString(10));
                    geo_tracking_pojo.setGeostatus(cursor.getString(11));
                    geo_tracking_pojo.setGeoffmid(cursor.getString(12));
                    geo_tracking_pojo.setGeocdatetime(cursor.getString(13));
                    geo_tracking_pojo.setGeoudatetime(cursor.getString(14));
                    geo_tracking_pojo.setPolyline(cursor.getString(16));
                    geo_tracking_pojo.setPause(cursor.getString(25));
                    geo_tracking_pojo.setResume(cursor.getString(26));
                    // Adding contact to list
                    geotrackingList.add(geo_tracking_pojo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
            Common.closeDataBase(db);
        }
        // return contact list
        return geotrackingList;
    }


    public boolean getAllGeotracking(String traking_id) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_FFMID + " IS NULL ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {

        }

        // return contact list
        return false;
    }


    // Getting Schemes by product Id
    public List<Schemes> getSchemesByProducId(String productId) {
        List<Schemes> schemesList = new ArrayList<Schemes>();
        // Select All QueryKEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String CURDATE = df.format(c.getTime());
        String selectQuery = "SELECT  " + KEY_SCHEMES_TITLE + "," + KEY_TABLE_SCHEME_PRODUCTS_PRICE + ",s." + KEY_SCHEMES_MASTER_ID + ",slab_id" + " FROM "
                + TABLE_SCHEME_PRODUCTS + " sp JOIN " + TABLE_SCHEMES + " s ON s." + KEY_SCHEMES_MASTER_ID + " = sp." + KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID
                + " WHERE sp." + KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + "=" + productId
                + " AND " + KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM + "<= '" + CURDATE + "' AND " + KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE + " >= '" + CURDATE
                + "' AND sp." + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + "=" + sharedpreferences.getString("region_id", "0")
                + " GROUP BY s." + KEY_SCHEMES_MASTER_ID;
        Log.d("schecmes", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schemes schemes = new Schemes();
                schemes.setschemeName(cursor.getString(3));
                schemes.setScheme_value(cursor.getString(1));
                schemes.setschemeMasterID(cursor.getString(2));
                // Adding contact to list
                schemesList.add(schemes);
            } while (cursor.moveToNext());
        }

        // return contact list
        return schemesList;
    }


    public List<Schemes> getSchemesByProducId(String productId, String slabId, String schemeID) {
        List<Schemes> schemesList = new ArrayList<Schemes>();
        // Select All QueryKEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String CURDATE = df.format(c.getTime());
        String selectQuery = "SELECT  id," + KEY_TABLE_SCHEME_PRODUCTS_PRICE + "," + KEY_SCHEMES_MASTER_ID + ",slab_id" + " FROM "
                + TABLE_SCHEME_PRODUCTS
                + " WHERE " + KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + "=" + productId
                // + " AND " + KEY_TABLE_SCHEME_PRODUCTS_VALID_FROM + "<= '" + CURDATE + "' AND " + KEY_TABLE_SCHEME_PRODUCTS_EXTENSTION_DATE + " >= '" + CURDATE
                + " AND " + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + "=" + sharedpreferences.getString("region_id", "0")
                + " AND " + KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID + "='" + slabId + "'"
                + " AND " + KEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID + "='" + schemeID + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("selectQuery", selectQuery);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schemes schemes = new Schemes();
                schemes.setschemeName(cursor.getString(3));
                schemes.setScheme_value(cursor.getString(1));
                schemes.setschemeMasterID(cursor.getString(2));
                // Adding contact to list
                schemesList.add(schemes);
            } while (cursor.moveToNext());
        }

        // return contact list
        return schemesList;
    }

    // Getting amount by product Id
    public Products_Pojo getAmountByProducId(String productId) {
        Products_Pojo products = null;
        // Select All QueryKEY_TABLE_SCHEME_PRODUCTS_SCHEME_ID
        String selectQuery = "SELECT  pp." + KEY_PRODUCTS_PRICE + "," + KEY_PRODUCTS_PACKETS_COUNT + "," + KEY_PRODUCTS_CATALOG_URL + " FROM " + TABLE_PRODUCTS + " p JOIN " + TABLE_PRODUCT_PRICE + " pp ON p." + KEY_PRODUCT_MASTER_ID + "=pp." + KEY_PRODUCT_MASTER_ID + " WHERE p." + KEY_PRODUCT_MASTER_ID + "=" + productId + " AND DATE('now') > DATE(pp." + KEY_PRODUCT_FROM_DATE + ") AND date('now') < DATE(pp." + KEY_PRODUCT_TO_DATE + ")";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                products = new Products_Pojo();
                products.setProductprice(cursor.getString(0));
                products.set_product_no_packets(cursor.getString(1));
                products.set_product_catalog_url(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return products
        return products;
    }

    // Getting All Geo Tracking with checkin
    public List<Geo_Tracking_POJO> getAllGeotrackingwithcheckin(String user_id) {
        List<Geo_Tracking_POJO> geotrackingList = new ArrayList<Geo_Tracking_POJO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + " IS NOT NULL" + " AND " + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + " IS NOT NULL" + " AND " + KEY_TABLE_GEO_TRACKING_FFMID + " IS  NULL" + " AND " + KEY_TABLE_GEO_TRACKING_USER_ID + "=" + user_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Geo_Tracking_POJO geo_tracking_pojo = new Geo_Tracking_POJO();
                    geo_tracking_pojo.setID(Integer.parseInt(cursor.getString(0)));
                    geo_tracking_pojo.setGeoMasterID(cursor.getString(2));
                    geo_tracking_pojo.setGeoVisitType(cursor.getString(1));
                    geo_tracking_pojo.set_Geo_user_id(cursor.getString(3));
                    geo_tracking_pojo.set_Geo_check_in_lat_lon(cursor.getString(4));
                    geo_tracking_pojo.setGeo_check_out_lat_lon(cursor.getString(5));
                    geo_tracking_pojo.setGeo_route_path_lat_lon(cursor.getString(6));
                    geo_tracking_pojo.setGeo_distance(cursor.getString(7));
                    geo_tracking_pojo.setGeo_visit_date(cursor.getString(8));
                    geo_tracking_pojo.setGeo_check_in_time(cursor.getString(9));
                    geo_tracking_pojo.setGeo_check_out_time(cursor.getString(10));
                    geo_tracking_pojo.setGeostatus(cursor.getString(11));
                    geo_tracking_pojo.setGeoffmid(cursor.getString(12));
                    geo_tracking_pojo.setGeocdatetime(cursor.getString(13));
                    geo_tracking_pojo.setGeoudatetime(cursor.getString(14));
                    // Adding contact to list
                    geotrackingList.add(geo_tracking_pojo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
            Common.closeDataBase(db);
        }
        // return contact list
        return geotrackingList;
    }

    // Adding new feedback
    public void addFeedback(Feedback feedback) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_feedback = new ContentValues();
        //values_feedback.put(KEY_TABLE_FEEDBACK_FEEDBACK_ID, feedback.getID());
        values_feedback.put(KEY_TABLE_FEEDBACK_USER_ID, feedback.get_user_id());
        values_feedback.put(KEY_TABLE_FEEDBACK_FARMER_NAME, feedback.getFarmerName());
        values_feedback.put(KEY_TABLE_FEEDBACK_PLACE, feedback.getplace());
        values_feedback.put(KEY_TABLE_FEEDBACK_CONTACT_NO, feedback.getContactNo());
        values_feedback.put(KEY_TABLE_FEEDBACK_CROP, feedback.getCrop());
        values_feedback.put(KEY_TABLE_FEEDBACK_HYBRID, feedback.getHybrid());
        values_feedback.put(KEY_TABLE_FEEDBACK_SOWING_DATE, feedback.getSowingDate());
        values_feedback.put(KEY_TABLE_FEEDBACK_FEEDBACK_MESSAGE, feedback.getfeedbackmessage());
        values_feedback.put(KEY_TABLE_FEEDBACK_IMAGE, feedback.getImage());
        values_feedback.put(KEY_TABLE_FEEDBACK_FFMID, feedback.get_ffmid());
        values_feedback.put(KEY_TABLE_FEEDBACK_TRANSPLANT_DATE, feedback.getTransplantingDate());

        // Inserting Row
        db.insert(TABLE_FEEDBACK, null, values_feedback);
//    Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }


    // Getting All Feedback
    public List<Feedback> getAllFeedback(String userId) {
        List<Feedback> feedbackList = new ArrayList<Feedback>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK + " WHERE " + KEY_TABLE_FEEDBACK_USER_ID + " in (" + userId + ") AND " + KEY_TABLE_FEEDBACK_FFMID + " IS NULL";
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Feedback feedback = new Feedback();
                    feedback.setID(Integer.parseInt(cursor.getString(0)));
                    feedback.set_user_id(Integer.parseInt(cursor.getString(1)));
                    feedback.setFarmerName(cursor.getString(2));
                    feedback.setPlace(cursor.getString(3));
                    feedback.setContactNo(cursor.getString(4));
                    feedback.setCrop(cursor.getString(5));
                    feedback.setHybrid(cursor.getString(6));
                    feedback.setSowingDate(cursor.getString(7));
                    feedback.setFeedbackMessage(cursor.getString(8));
                    feedback.setImage(cursor.getString(9));
                    feedback.set_ffmid(cursor.getString(10));
                    feedback.setTransplantingDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FEEDBACK_TRANSPLANT_DATE)));
                    // Adding contact to list
                    feedbackList.add(feedback);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
        }
        // return contact list
        return feedbackList;
    }

    public List<Feedback> getAllFeedbacks(String userId) {
        List<Feedback> feedbacksList = new ArrayList<Feedback>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK + " WHERE " + KEY_TABLE_FEEDBACK_USER_ID + " in (" + userId + ")  ORDER BY " + KEY_TABLE_FEEDBACK_FEEDBACK_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Feedback feedback = new Feedback();
                feedback.setID(Integer.parseInt(cursor.getString(0)));
                feedback.set_user_id(cursor.getInt(1));
                feedback.setFarmerName(cursor.getString(2));
                feedback.setPlace(cursor.getString(3));
                feedback.setContactNo(cursor.getString(4));
                feedback.setCrop(cursor.getString(5));
                feedback.setHybrid(cursor.getString(6));
                feedback.setSowingDate(cursor.getString(7));
                feedback.setFeedbackMessage(cursor.getString(8));
                feedback.setImage(cursor.getString(9));
                feedback.set_ffmid(cursor.getString(10));
                feedback.setTransplantingDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FEEDBACK_TRANSPLANT_DATE)));
                // Adding contact to list
                feedbacksList.add(feedback);
            } while (cursor.moveToNext());
        }

        // return contact list
        return feedbacksList;
    }

    // Getting All service orders
    public List<ServiceOrderMaster> getAllServiceorders() {
        List<ServiceOrderMaster> companiesList = new ArrayList<ServiceOrderMaster>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SERVICEORDER;
        //String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID  +" FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderMaster serviceOrderMaster = new ServiceOrderMaster();
                serviceOrderMaster.setID(Integer.parseInt(cursor.getString(0)));
                serviceOrderMaster.setserviceorder_masterid(cursor.getString(2));
                serviceOrderMaster.setserviceorder_type(cursor.getString(1));
                serviceOrderMaster.set_serviceorder_date(cursor.getString(3));
                serviceOrderMaster.set_serviceorder_user_id(cursor.getString(4));
                serviceOrderMaster.setserviceorder_customer_id(cursor.getString(5));
                serviceOrderMaster.setserviceorder_division_id(cursor.getString(6));
                serviceOrderMaster.setserviceorder_company_id(cursor.getString(7));
                serviceOrderMaster.setserviceorderstatus(cursor.getString(8));
                serviceOrderMaster.setserviceordercdatetime(cursor.getString(9));
                serviceOrderMaster.setserviceorderudatetime(cursor.getString(10));
                serviceOrderMaster.setserviceorderffm_id(cursor.getString(11));
                // Adding contact to list
                companiesList.add(serviceOrderMaster);
            } while (cursor.moveToNext());
        }

        // return contact list
        return companiesList;
    }

    // Getting All service orders
    public List<ServiceOrderDetailMaster> getAllServiceordersdetails() {
        List<ServiceOrderDetailMaster> companiesList = new ArrayList<ServiceOrderDetailMaster>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SERVICEORDERDETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderDetailMaster serviceOrderDetailMaster = new ServiceOrderDetailMaster();
                serviceOrderDetailMaster.setID(Integer.parseInt(cursor.getString(0)));
                serviceOrderDetailMaster.setserviceorderdetail_masterid(cursor.getString(1));
                serviceOrderDetailMaster.setserviceorderdetailorderid_id(cursor.getString(2));
                serviceOrderDetailMaster.set_serviceorderdetail_crop_id(cursor.getString(3));
                serviceOrderDetailMaster.setserviceorderdetail_scheme_id(cursor.getString(4));
                serviceOrderDetailMaster.setserviceorderdetail_product_id(cursor.getString(5));
                serviceOrderDetailMaster.set_serviceorderdetail_quantity(cursor.getString(6));
                serviceOrderDetailMaster.setserviceorderdetail_order_price(cursor.getString(7));
                serviceOrderDetailMaster.setserviceorderdetail_advance_amount(cursor.getString(8));
                serviceOrderDetailMaster.setserviceorderdetail_status(cursor.getString(9));
                serviceOrderDetailMaster.setserviceorderdetailcdatetime(cursor.getString(10));
                serviceOrderDetailMaster.setserviceorderdetailudatetime(cursor.getString(11));
                // Adding contact to list
                companiesList.add(serviceOrderDetailMaster);
            } while (cursor.moveToNext());
        }

        // return contact list
        return companiesList;
    }


    // Getting All Schemes
    public List<Schemes> getAllSchemes() {
        List<Schemes> schemesList = new ArrayList<Schemes>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCHEMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schemes schemes = new Schemes();
                schemes.setID(Integer.parseInt(cursor.getString(0)));
                schemes.setschemeMasterID(cursor.getString(1));
                schemes.setschemeName(cursor.getString(2));
                //  schemes.setscheme_company_id(cursor.getString(3));
                ////   schemes.setscheme_division_id(cursor.getString(4));
                //   schemes.setscheme_crop_id(cursor.getString(5));
                schemes.setscheme_sap_code(cursor.getString(6));
                //   schemes.setscheme_value(cursor.getString(7));
                // Adding contact to list
                schemesList.add(schemes);
            } while (cursor.moveToNext());
        }

        // return contact list
        return schemesList;
    }

    // Getting All Users
    public List<Users> getAllUsers() {
        List<Users> usersList = new ArrayList<Users>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();
                users.setUserMasterID(cursor.getString(1));
                users.setUser_first_name(cursor.getString(2));
                users.setUser_last_name(cursor.getString(3));
                users.setUser_mobile_no(cursor.getString(4));
                users.setUser_email(cursor.getString(5));
                users.setUser_sap_id(cursor.getString(6));
                users.setUser_password(cursor.getString(7));
                users.setUser_role_id(cursor.getString(8));
                users.setUser_reporting_manager_id(cursor.getString(9));
                users.setUserstatus(cursor.getString(10));
                users.setUsercdatetime(cursor.getString(11));
                users.setUserudatetime(cursor.getString(12));
                // Adding contact to list
                usersList.add(users);
            } while (cursor.moveToNext());
        }

        // return contact list
        return usersList;
    }

    // Getting All REGIONS
    public List<Regions> getAllRegions() {
        List<Regions> regionsList = new ArrayList<Regions>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REGION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Regions regions = new Regions();
                regions.setID(Integer.parseInt(cursor.getString(0)));
                regions.setRegionMasterID(cursor.getString(2));
                regions.setRegionName(cursor.getString(1));
                regions.setRegioncode(cursor.getString(3));
                regions.setRegionstatus(cursor.getString(4));

                // Adding contact to list
                regionsList.add(regions);
            } while (cursor.moveToNext());
        }

        // return contact list
        return regionsList;
    }


    // Getting All Company Division Crop
    public List<Company_division_crops> getAllCompany_division_crops() {
        List<Company_division_crops> cdcList = new ArrayList<Company_division_crops>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = 1  " + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = 3  ";
        // String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = 1" +  " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = 3 " ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Company_division_crops company_division_crops = new Company_division_crops();

                company_division_crops.setCdcCropId(cursor.getString(3));
                Log.d("Selected crop id is", cursor.getString(3));

                /*String selectQuerys = "SELECT  KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME FROM " + TABLE_CROPS +" where " + cursor.getString(3)+ " = 1  " + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = 3  " ;*/
                // Adding contact to list
                // getCrop(cursor.getString(3));
                cdcList.add(company_division_crops);


            } while (cursor.moveToNext());
        }

        // return contact list
        return cdcList;
    }

    // Getting All User_Company_Customer
    public List<User_Company_Customer> getAllUser_Company_Customer() {
        List<User_Company_Customer> uccList = new ArrayList<User_Company_Customer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_COMPANY_CUSTOMER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User_Company_Customer user_company_customer = new User_Company_Customer();
                user_company_customer.setID(Integer.parseInt(cursor.getString(0)));
                user_company_customer.setuccUserId(cursor.getString(1));
                user_company_customer.setuccCompanyId(cursor.getString(2));
                user_company_customer.setuccCustomerId(cursor.getString(3));


                // Adding contact to list
                uccList.add(user_company_customer);
            } while (cursor.moveToNext());
        }

        // return contact list
        return uccList;

    }

    // Getting All Scheme Products
    public List<Scheme_Products> getAllSchemeProducts() {
        List<Scheme_Products> spList = new ArrayList<Scheme_Products>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCHEME_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Scheme_Products scheme_products = new Scheme_Products();
                scheme_products.setspscheme_id(cursor.getString(1));
                scheme_products.setspProductId(cursor.getString(2));
                // Adding contact to list
                spList.add(scheme_products);
            } while (cursor.moveToNext());
        }

        // return contact list
        return spList;

    }

    // Getting All User_Company_Division
    public List<User_Company_Division> getAllUser_Company_Division() {
        List<User_Company_Division> ucdList = new ArrayList<User_Company_Division>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_COMPANY_DIVISION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User_Company_Division user_company_division = new User_Company_Division();
                user_company_division.setID(Integer.parseInt(cursor.getString(0)));
                user_company_division.setucdUserId(cursor.getString(1));
                user_company_division.setucdCompanyId(cursor.getString(2));
                user_company_division.setucdDivisionId(cursor.getString(3));


                // Adding contact to list
                ucdList.add(user_company_division);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ucdList;
    }


    // Getting All Crops
    public List<Crops> getAllCrops() {
        List<Crops> cropsList = new ArrayList<Crops>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CROPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Crops crops = new Crops();
                crops.setCropMasterID(cursor.getString(1));
                crops.setCropName(cursor.getString(2));
                crops.setCropcode(cursor.getString(3));
                crops.setCropsapid(cursor.getString(4));
                crops.setCropdivisionId(cursor.getString(5));
                crops.setCropcdatetime(cursor.getString(6));
                crops.setCropudatetime(cursor.getString(7));
                // Adding contact to list
                cropsList.add(crops);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return cropsList;
    }

    // Getting AllProducts
    public List<Products_Pojo> getAllProducts() {
        List<Products_Pojo> productsList = new ArrayList<Products_Pojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products_Pojo products_pojo = new Products_Pojo();
                products_pojo.setProductMasterId(cursor.getString(1));
                products_pojo.setProductName(cursor.getString(2));
                products_pojo.setProductDescription(cursor.getString(3));
                products_pojo.setProductSapCode(cursor.getString(4));
                products_pojo.setProductcropid(cursor.getString(5));
                products_pojo.setProductcompanyid(cursor.getString(6));
                products_pojo.setProductdivisionid(cursor.getString(7));
                products_pojo.setProductregeion(cursor.getString(8));
                products_pojo.setProductprice(cursor.getString(9));
                products_pojo.setProductdiscount(cursor.getString(10));
                products_pojo.setProductfromdate(cursor.getString(11));
                products_pojo.setProducttodate(cursor.getString(12));
                products_pojo.setProductstatus(cursor.getString(13));
                products_pojo.setProductcdatetime(cursor.getString(14));
                products_pojo.setProductudatetime(cursor.getString(15));
                products_pojo.setProductImages(cursor.getString(16));
                products_pojo.setProductVideos(cursor.getString(17));
                // Adding contact to list
                productsList.add(products_pojo);
            } while (cursor.moveToNext());
        }

        // return Products list
        return productsList;
    }


    // Getting All Customers
    public List<Customers> getAllCustomers() {
        List<Customers> customerList = new ArrayList<Customers>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customers customers = new Customers();
                customers.setCusMasterID(cursor.getString(1));
                customers.setCusName(cursor.getString(2));
                customers.setCuscode(cursor.getString(3));
                customers.setCusaddress(cursor.getString(4));
                customers.setCusstreet(cursor.getString(5));
                customers.setCus_city(cursor.getString(6));
                customers.setCuscountry(cursor.getString(7));
                customers.setCusregion_Id(cursor.getString(8));
                customers.setCustelephone(cursor.getString(9));
                customers.setCuscompany_Id(cursor.getString(10));
                customers.setCusstatus(cursor.getString(11));
                customers.setCuscdatetime(cursor.getString(12));
                customers.setCusudatetime(cursor.getString(13));
                // Adding contact to list
                customerList.add(customers);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return customerList;
    }

    public String getCustomerLatLngById(int id) {
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_CUSTOMER_LAT_LNG + " FROM " + TABLE_CUSTOMERS + " where " + KEY_TABLE_CUSTOMER_MASTER_ID + "=" + id;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // return DIVISIONS list
        return null;

    }

    public String getRetailerLatLngById(int id) {
        String selectQuery = "SELECT  " + KEY_TABLE_CUSTOMER_LAT_LNG + " FROM " + TABLE_RETAILER + " where " + KEY_TABLE_RETAILER_MASTER_ID + "=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // return DIVISIONS list
        return null;
    }

    // Getting All Customer_details
    public List<Customer_Details> getAllCustomer_details() {
        List<Customer_Details> customerdetailsListList = new ArrayList<Customer_Details>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer_Details customer_details = new Customer_Details();
                customer_details.setID(Integer.parseInt(cursor.getString(0)));
                customer_details.set_customer_details_master_id(cursor.getString(1));
                customer_details.set_customer_details_division_id(cursor.getString(2));
                customer_details.set_customer_details_credit_limit(cursor.getString(3));
                customer_details.set_customer_details_inside_bucket(cursor.getString(4));
                customer_details.set_customer_details_outside_bucket(cursor.getString(5));
                customer_details.set_customer_details_status(cursor.getString(6));
                customer_details.set_customer_details_created_datetime(cursor.getString(7));
                customer_details.set_customer_details_updated_datetime(cursor.getString(8));
                // Adding contact to list
                customerdetailsListList.add(customer_details);
            } while (cursor.moveToNext());
        }

        // return contact list
        return customerdetailsListList;
    }

    public List<Complaints> getAllComplaintreg(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        Cursor cursor = null;
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userId + "' ORDER BY " + KEY_TABLE_COMPLAINTS_ID + " DESC";
        Log.e("select reg", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Complaints complaints = new Complaints();
                    complaints.setID(Integer.parseInt(cursor.getString(0)));
                    complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                    complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                    complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                    complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                    complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                    complaints.set_marketing_lot_number(cursor.getString(6));
                    complaints.set_complaint_type(cursor.getString(7));
                    complaints.set_farmer_name(cursor.getString(8));
                    complaints.set_contact_no(cursor.getString(9));
                    complaints.set_complaint_area_acres(cursor.getString(10));
                    complaints.set_soil_type(cursor.getString(11));
                    complaints.set_others(cursor.getString(12));
                    complaints.set_purchased_quantity(cursor.getString(13));
                    complaints.set_complaint_quantity(cursor.getString(14));
                    complaints.set_purchase_date(cursor.getString(15));
                    complaints.set_bill_number(cursor.getString(16));
                    complaints.set_retailer_name(cursor.getString(17));
                    complaints.set_distributor(cursor.getInt(18));
                    complaints.set_mandal(cursor.getString(19));
                    complaints.set_village(cursor.getString(20));
                    complaints.set_image(cursor.getString(21));
                    complaints.set_regulatory_type(cursor.getString(22));
                    complaints.set_sampling_date(cursor.getString(23));
                    complaints.set_place_sampling(cursor.getString(24));
                    complaints.set_sampling_officer_name(cursor.getString(25));
                    complaints.set_sampling_officer_contact(cursor.getString(26));
                    complaints.set_comments(cursor.getString(27));
                    complaints.set_status(cursor.getInt(28));
                    complaints.set_remarks(cursor.getString(29));
                    complaints.set_created_datetime(cursor.getString(30));
                    complaints.set_updated_datetime(cursor.getString(31));
                    complaints.set_ffmid(cursor.getString(32));
                    // Adding contact to list
                    complaintList.add(complaints);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
        }
        // return contact list
        return complaintList;
    }

    public void addComplaint(Complaints complaints) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_complaint = new ContentValues();
        //values_complaint.put(KEY_TABLE_COMPLAINTS_ID, complaints.getID());
        values_complaint.put(KEY_TABLE_COMPLAINT_USER_ID, complaints.get_user_id());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPANY_ID, complaints.getCompanyId());
        values_complaint.put(KEY_TABLE_COMPLAINTS_DIVISION_ID, complaints.get_division_id());
        values_complaint.put(KEY_TABLE_COMPLAINTS_CROP_ID, complaints.getCropid());
        values_complaint.put(KEY_TABLE_COMPLAINTS_PRODUCT_ID, complaints.getProductid());
        values_complaint.put(KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO, complaints.get_marketing_lot_number());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE, complaints.get_complaint_type());
        values_complaint.put(KEY_TABLE_COMPLAINTS_FARMER_NAME, complaints.get_farmer_name());
        values_complaint.put(KEY_TABLE_COMPLAINTS_CONTACT_NO, complaints.get_contact_no());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES, complaints.get_complaint_area_acres());
        values_complaint.put(KEY_TABLE_COMPLAINTS_SOIL_TYPE, complaints.get_soil_type());
        values_complaint.put(KEY_OTHERS, complaints.get_others());
        values_complaint.put(KEY_TABLE_COMPLAINTS_PURCHASED_QTY, complaints.get_purchased_quantity());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPLAINT_QTY, complaints.get_complaint_quantity());
        values_complaint.put(KEY_PURCHASE_DATE, complaints.get_purchase_date());
        values_complaint.put(KEY_TABLE_COMPLAINTS_BILL_NUMBER, complaints.get_bill_number());
        values_complaint.put(KEY_TABLE_COMPLAINTS_RETAILER_NAME, complaints.get_retailer_name());
        values_complaint.put(KEY_TABLE_COMPLAINTS_RETAILER_ID, complaints.getRetailerId());
        values_complaint.put(KEY_TABLE_COMPLAINTS_DISTRIBUTOR, complaints.get_distributor());
        values_complaint.put(KEY_TABLE_COMPLAINTS_MANDAL, complaints.get_mandal());
        values_complaint.put(KEY_TABLE_COMPLAINTS_VILAGE, complaints.get_village());
        values_complaint.put(KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD, complaints.get_image());
        values_complaint.put(KEY_REGULATORY_TYPE, complaints.get_regulatory_type());
        values_complaint.put(KEY_TABLE_COMPLAINTS_SAMPLING_DATE, complaints.get_sampling_date());
        values_complaint.put(KEY_TABLE_COMPLAINTS_PLACE_SAMPLING, complaints.get_place_sampling());
        values_complaint.put(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME, complaints.get_sampling_officer_name());
        values_complaint.put(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT, complaints.get_sampling_officer_contact());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMMENTS, complaints.get_comments());
        values_complaint.put(KEY_TABLE_COMPLAINTS_STATUS, complaints.get_status());
        values_complaint.put(KEY_TABLE_COMPLAINTS_REMARKS, complaints.get_remarks());
        values_complaint.put(KEY_TABLE_COMPLAINTS_CREATED_DATETIME, complaints.get_created_datetime());
        values_complaint.put(KEY_TABLE_COMPLAINTS_UPDATED_DATETIME, complaints.get_updated_datetime());
        values_complaint.put(KEY_TABLE_COMPLAINTS_FFMID, complaints.get_ffmid());
        values_complaint.put(KEY_TABLE_COMPLAINTS_STATE, complaints.getState());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE, complaints.getComplaintPercentage());
        values_complaint.put(KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS, complaints.getComplaintRemarks());
        values_complaint.put(KEY_TABLE_COMPLAINTS_ZONE, complaints.getZone());
        values_complaint.put(KEY_TABLE_COMPLAINTS_REGION, complaints.getRegion());
        values_complaint.put(KEY_TABLE_COMPLAINTS_DEALER, complaints.getDealer());
        values_complaint.put(KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS, complaints.getPerformanceLot());
        values_complaint.put(KEY_TABLE_COMPLAINTS_DATE_OF_SOWING, complaints.getDateOfSowing());
        values_complaint.put(KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME, complaints.getInspectedDate());
        values_complaint.put(KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT, complaints.getDateOfComplaint());
        values_complaint.put(KEY_TABLE_COMPLAINTS_DISTRICT, complaints.getDistrict());
        values_complaint.put(KEY_TABLE_COMPLAINTS_STAGES, complaints.getStages());

        // Inserting Row
        int id = (int) db.insert(TABLE_COMPLAINT, null, values_complaint);
//		Toast.makeText(getApplicationContext(),"inserted successfully...",Toast.LENGTH_SHORT).show();
        db.close(); // Closing database connection
    }


    public List<Complaints> getAllComplaints(String userId) {
        List<Complaints> complaintsList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " in (" + userId + ") AND " + KEY_TABLE_COMPLAINTS_FFMID + " IS NULL";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ID))));
                complaints.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINT_USER_ID))));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPANY_ID))));
                complaints.set_division_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DIVISION_ID))));
                complaints.setCropid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CROP_ID))));
                complaints.setProductid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PRODUCT_ID))));
                complaints.set_marketing_lot_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO)));
                complaints.set_complaint_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE)));
                complaints.set_farmer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FARMER_NAME)));
                complaints.set_contact_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CONTACT_NO)));
                complaints.set_complaint_area_acres(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES)));
                complaints.set_soil_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SOIL_TYPE)));
                complaints.set_others(cursor.getString(cursor.getColumnIndex(KEY_OTHERS)));
                complaints.set_purchased_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PURCHASED_QTY)));
                complaints.set_complaint_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_QTY)));
                complaints.set_purchase_date(cursor.getString(cursor.getColumnIndex(KEY_PURCHASE_DATE)));
                complaints.set_bill_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_BILL_NUMBER)));
                complaints.set_retailer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_NAME)));
                complaints.setRetailerId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_ID)));
                complaints.set_distributor(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRIBUTOR)));
                complaints.set_mandal(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MANDAL)));
                complaints.set_village(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_VILAGE)));
                complaints.set_image(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD)));
                complaints.set_regulatory_type(cursor.getString(cursor.getColumnIndex(KEY_REGULATORY_TYPE)));
                complaints.set_sampling_date(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_DATE)));
                complaints.set_place_sampling(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PLACE_SAMPLING)));
                complaints.set_sampling_officer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME)));
                complaints.set_sampling_officer_contact(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT)));
                complaints.set_comments(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMMENTS)));
                complaints.set_status(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATUS)));
                complaints.set_remarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REMARKS)));
                complaints.set_created_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CREATED_DATETIME)));
                complaints.set_updated_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_UPDATED_DATETIME)));
                complaints.set_ffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FFMID)));
                complaints.setComplaintPercentage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE)));
                complaints.setComplaintRemarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS)));
                complaints.setZone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ZONE)));
                complaints.setDealer(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DEALER)));
                complaints.setRegion(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REGION)));
                complaints.setPerformanceLot(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS)));
                complaints.setState(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATE)));
                complaints.setDateOfSowing(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_SOWING)));
                complaints.setInspectedDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME)));
                complaints.setDateOfComplaint(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT)));
                complaints.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRICT)));
                complaints.setStages(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STAGES)));
                // Adding contact to list
                complaintsList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintsList;
    }

    public List<Complaints> getAllComplaintregnew(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userId + "' AND " + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + " = 'Regulatory'" + " ORDER BY " + KEY_TABLE_COMPLAINTS_ID + " DESC";
        Log.e("reg query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                complaints.setComplaintPercentage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE)));
                complaints.setComplaintRemarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS)));
                complaints.setZone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ZONE)));
                complaints.setDealer(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DEALER)));
                complaints.setRegion(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REGION)));
                complaints.setPerformanceLot(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS)));
                complaints.setState(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATE)));
                complaints.setDateOfSowing(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_SOWING)));
                complaints.setInspectedDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME)));
                complaints.setDateOfComplaint(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT)));
                complaints.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRICT)));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }

    public List<Complaints> getAllComplaintprodnew(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " in (" + userId + ") ORDER BY " + KEY_TABLE_COMPLAINTS_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ID))));
                complaints.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINT_USER_ID))));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPANY_ID))));
                complaints.set_division_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DIVISION_ID))));
                complaints.setCropid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CROP_ID))));
                complaints.setProductid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PRODUCT_ID))));
                complaints.set_marketing_lot_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO)));
                complaints.set_complaint_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE)));
                complaints.set_farmer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FARMER_NAME)));
                complaints.set_contact_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CONTACT_NO)));
                complaints.set_complaint_area_acres(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES)));
                complaints.set_soil_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SOIL_TYPE)));
                complaints.set_others(cursor.getString(cursor.getColumnIndex(KEY_OTHERS)));
                complaints.set_purchased_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PURCHASED_QTY)));
                complaints.set_complaint_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_QTY)));
                complaints.set_purchase_date(cursor.getString(cursor.getColumnIndex(KEY_PURCHASE_DATE)));
                complaints.set_bill_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_BILL_NUMBER)));
                complaints.set_retailer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_NAME)));
                complaints.setRetailerId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_ID)));
                complaints.set_distributor(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRIBUTOR)));
                complaints.set_mandal(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MANDAL)));
                complaints.set_village(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_VILAGE)));
                complaints.set_image(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD)));
                complaints.set_regulatory_type(cursor.getString(cursor.getColumnIndex(KEY_REGULATORY_TYPE)));
                complaints.set_sampling_date(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_DATE)));
                complaints.set_place_sampling(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PLACE_SAMPLING)));
                complaints.set_sampling_officer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME)));
                complaints.set_sampling_officer_contact(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT)));
                complaints.set_comments(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMMENTS)));
                complaints.set_status(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATUS)));
                complaints.set_remarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REMARKS)));
                complaints.set_created_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CREATED_DATETIME)));
                complaints.set_updated_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_UPDATED_DATETIME)));
                complaints.set_ffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FFMID)));
                complaints.setComplaintPercentage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE)));
                complaints.setComplaintRemarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS)));
                complaints.setZone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ZONE)));
                complaints.setDealer(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DEALER)));
                complaints.setRegion(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REGION)));
                complaints.setPerformanceLot(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS)));
                complaints.setState(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATE)));
                complaints.setDateOfSowing(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_SOWING)));
                complaints.setInspectedDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME)));
                complaints.setDateOfComplaint(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT)));
                complaints.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRICT)));
                complaints.setStages(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STAGES)));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }

    public List<Complaints> getAllComplaints(String userId, String companyId, String divisionId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " in (" + userId + ") AND " + KEY_TABLE_COMPLAINTS_COMPANY_ID + " = " + companyId + " AND " + KEY_TABLE_COMPLAINTS_DIVISION_ID + " = " + divisionId + " ORDER BY " + KEY_TABLE_COMPLAINTS_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ID))));
                complaints.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINT_USER_ID))));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPANY_ID))));
                complaints.set_division_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DIVISION_ID))));
                complaints.setCropid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CROP_ID))));
                complaints.setProductid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PRODUCT_ID))));
                complaints.set_marketing_lot_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO)));
                complaints.set_complaint_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE)));
                complaints.set_farmer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FARMER_NAME)));
                complaints.set_contact_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CONTACT_NO)));
                complaints.set_complaint_area_acres(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES)));
                complaints.set_soil_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SOIL_TYPE)));
                complaints.set_others(cursor.getString(cursor.getColumnIndex(KEY_OTHERS)));
                complaints.set_purchased_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PURCHASED_QTY)));
                complaints.set_complaint_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_QTY)));
                complaints.set_purchase_date(cursor.getString(cursor.getColumnIndex(KEY_PURCHASE_DATE)));
                complaints.set_bill_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_BILL_NUMBER)));
                complaints.set_retailer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_NAME)));
                complaints.setRetailerId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_ID)));
                complaints.set_distributor(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRIBUTOR)));
                complaints.set_mandal(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MANDAL)));
                complaints.set_village(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_VILAGE)));
                complaints.set_image(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD)));
                complaints.set_regulatory_type(cursor.getString(cursor.getColumnIndex(KEY_REGULATORY_TYPE)));
                complaints.set_sampling_date(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_DATE)));
                complaints.set_place_sampling(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PLACE_SAMPLING)));
                complaints.set_sampling_officer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME)));
                complaints.set_sampling_officer_contact(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT)));
                complaints.set_comments(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMMENTS)));
                complaints.set_status(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATUS)));
                complaints.set_remarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REMARKS)));
                complaints.set_created_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CREATED_DATETIME)));
                complaints.set_updated_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_UPDATED_DATETIME)));
                complaints.set_ffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FFMID)));
                complaints.setComplaintPercentage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE)));
                complaints.setComplaintRemarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS)));
                complaints.setZone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ZONE)));
                complaints.setDealer(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DEALER)));
                complaints.setRegion(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REGION)));
                complaints.setPerformanceLot(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS)));
                complaints.setState(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATE)));
                complaints.setDateOfSowing(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_SOWING)));
                complaints.setInspectedDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME)));
                complaints.setDateOfComplaint(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT)));
                complaints.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRICT)));
                complaints.setStages(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STAGES)));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }


    public List<Complaints> getAllComplaint(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " in (" + userId + ")";
        Common.Log.i("TABLE QUERY " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ID))));
                complaints.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINT_USER_ID))));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPANY_ID))));
                complaints.set_division_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DIVISION_ID))));
                complaints.setCropid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CROP_ID))));
                complaints.setProductid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PRODUCT_ID))));
                complaints.set_marketing_lot_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO)));
                complaints.set_complaint_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE)));
                complaints.set_farmer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FARMER_NAME)));
                complaints.set_contact_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CONTACT_NO)));
                complaints.set_complaint_area_acres(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES)));
                complaints.set_soil_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SOIL_TYPE)));
                complaints.set_others(cursor.getString(cursor.getColumnIndex(KEY_OTHERS)));
                complaints.set_purchased_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PURCHASED_QTY)));
                complaints.set_complaint_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_QTY)));
                complaints.set_purchase_date(cursor.getString(cursor.getColumnIndex(KEY_PURCHASE_DATE)));
                complaints.set_bill_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_BILL_NUMBER)));
                complaints.set_retailer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_NAME)));
                complaints.setRetailerId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_ID)));
                complaints.set_distributor(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRIBUTOR)));
                complaints.set_mandal(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MANDAL)));
                complaints.set_village(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_VILAGE)));
                complaints.set_image(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD)));
                complaints.set_regulatory_type(cursor.getString(cursor.getColumnIndex(KEY_REGULATORY_TYPE)));
                complaints.set_sampling_date(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_DATE)));
                complaints.set_place_sampling(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PLACE_SAMPLING)));
                complaints.set_sampling_officer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME)));
                complaints.set_sampling_officer_contact(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT)));
                complaints.set_comments(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMMENTS)));
                complaints.set_status(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATUS)));
                complaints.set_remarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REMARKS)));
                complaints.set_created_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CREATED_DATETIME)));
                complaints.set_updated_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_UPDATED_DATETIME)));
                complaints.set_ffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FFMID)));
                complaints.setComplaintPercentage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE)));
                complaints.setComplaintRemarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS)));
                complaints.setZone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ZONE)));
                complaints.setDealer(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DEALER)));
                complaints.setRegion(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REGION)));
                complaints.setPerformanceLot(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS)));
                complaints.setState(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATE)));
                complaints.setDateOfSowing(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_SOWING)));
                complaints.setInspectedDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME)));
                complaints.setDateOfComplaint(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT)));
                complaints.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRICT)));
                complaints.setStages(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STAGES)));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }

    public List<Complaints> getAllComplaintsregulatory(String userID) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        //String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE "+KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + " = regulatory ORDER BY "+ KEY_TABLE_COMPLAINTS_ID + " DESC" ;
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " = '" + userID + "' AND LOWER(" + KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE + ") = 'regulatory' AND " + KEY_TABLE_COMPLAINTS_FFMID + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Complaints complaints = new Complaints();
                complaints.setID(Integer.parseInt(cursor.getString(0)));
                complaints.set_user_id(Integer.parseInt(cursor.getString(1)));
                complaints.setCompanyId(Integer.parseInt(cursor.getString(2)));
                complaints.set_division_id(Integer.parseInt(cursor.getString(3)));
                complaints.setCropid(Integer.parseInt(cursor.getString(4)));
                complaints.setProductid(Integer.parseInt(cursor.getString(5)));
                complaints.set_marketing_lot_number(cursor.getString(6));
                complaints.set_complaint_type(cursor.getString(7));
                complaints.set_farmer_name(cursor.getString(8));
                complaints.set_contact_no(cursor.getString(9));
                complaints.set_complaint_area_acres(cursor.getString(10));
                complaints.set_soil_type(cursor.getString(11));
                complaints.set_others(cursor.getString(12));
                complaints.set_purchased_quantity(cursor.getString(13));
                complaints.set_complaint_quantity(cursor.getString(14));
                complaints.set_purchase_date(cursor.getString(15));
                complaints.set_bill_number(cursor.getString(16));
                complaints.set_retailer_name(cursor.getString(17));
                complaints.set_distributor(cursor.getInt(18));
                complaints.set_mandal(cursor.getString(19));
                complaints.set_village(cursor.getString(20));
                complaints.set_image(cursor.getString(21));
                complaints.set_regulatory_type(cursor.getString(22));
                complaints.set_sampling_date(cursor.getString(23));
                complaints.set_place_sampling(cursor.getString(24));
                complaints.set_sampling_officer_name(cursor.getString(25));
                complaints.set_sampling_officer_contact(cursor.getString(26));
                complaints.set_comments(cursor.getString(27));
                complaints.set_status(cursor.getInt(28));
                complaints.set_remarks(cursor.getString(29));
                complaints.set_created_datetime(cursor.getString(30));
                complaints.set_updated_datetime(cursor.getString(31));
                complaints.set_ffmid(cursor.getString(32));
                complaints.setComplaintPercentage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE)));
                complaints.setComplaintRemarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS)));
                complaints.setZone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ZONE)));
                complaints.setDealer(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DEALER)));
                complaints.setRegion(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REGION)));
                complaints.setPerformanceLot(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS)));
                complaints.setState(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATE)));
                complaints.setDateOfSowing(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_SOWING)));
                complaints.setInspectedDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME)));
                complaints.setDateOfComplaint(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT)));
                complaints.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRICT)));
                // Adding contact to list
                complaintList.add(complaints);
            } while (cursor.moveToNext());
        }

        // return contact list
        return complaintList;
    }

    public List<Complaints> getAllComplaintsproducts(String userId) {
        List<Complaints> complaintList = new ArrayList<Complaints>();
        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINT + " WHERE " + KEY_TABLE_COMPLAINT_USER_ID + " in (" + userId + ") AND " + KEY_TABLE_COMPLAINTS_FFMID + " IS NULL ";
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Complaints complaints = new Complaints();
                    complaints.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ID))));
                    complaints.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINT_USER_ID))));
                    complaints.setCompanyId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPANY_ID))));
                    complaints.set_division_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DIVISION_ID))));
                    complaints.setCropid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CROP_ID))));
                    complaints.setProductid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PRODUCT_ID))));
                    complaints.set_marketing_lot_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MARKETING_LOT_NO)));
                    complaints.set_complaint_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_TYPE)));
                    complaints.set_farmer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FARMER_NAME)));
                    complaints.set_contact_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CONTACT_NO)));
                    complaints.set_complaint_area_acres(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_AREA_ACRES)));
                    complaints.set_soil_type(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SOIL_TYPE)));
                    complaints.set_others(cursor.getString(cursor.getColumnIndex(KEY_OTHERS)));
                    complaints.set_purchased_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PURCHASED_QTY)));
                    complaints.set_complaint_quantity(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_QTY)));
                    complaints.set_purchase_date(cursor.getString(cursor.getColumnIndex(KEY_PURCHASE_DATE)));
                    complaints.set_bill_number(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_BILL_NUMBER)));
                    complaints.set_retailer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_NAME)));
                    complaints.setRetailerId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_RETAILER_ID)));
                    complaints.set_distributor(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRIBUTOR)));
                    complaints.set_mandal(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_MANDAL)));
                    complaints.set_village(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_VILAGE)));
                    complaints.set_image(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD)));
                    complaints.set_regulatory_type(cursor.getString(cursor.getColumnIndex(KEY_REGULATORY_TYPE)));
                    complaints.set_sampling_date(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_DATE)));
                    complaints.set_place_sampling(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PLACE_SAMPLING)));
                    complaints.set_sampling_officer_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_NAME)));
                    complaints.set_sampling_officer_contact(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_SAMPLING_OFFICER_CONTACT)));
                    complaints.set_comments(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMMENTS)));
                    complaints.set_status(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATUS)));
                    complaints.set_remarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REMARKS)));
                    complaints.set_created_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_CREATED_DATETIME)));
                    complaints.set_updated_datetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_UPDATED_DATETIME)));
                    complaints.set_ffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_FFMID)));
                    complaints.setComplaintPercentage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_PERCENTAGE)));
                    complaints.setComplaintRemarks(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_COMPLAINT_REMARKS)));
                    complaints.setZone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_ZONE)));
                    complaints.setDealer(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DEALER)));
                    complaints.setRegion(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_REGION)));
                    complaints.setPerformanceLot(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_PERFORMANCE_OF_LOT_IN_OTHER_FILEDS)));
                    complaints.setState(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STATE)));
                    complaints.setDateOfSowing(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_SOWING)));
                    complaints.setInspectedDate(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_INSPECTED_DATETIME)));
                    complaints.setDateOfComplaint(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DATE_OF_COMPLAINT)));
                    complaints.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_DISTRICT)));
                    complaints.setStages(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_COMPLAINTS_STAGES)));
                    // Adding contact to list
                    complaintList.add(complaints);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
        }
        // return contact list
        return complaintList;
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                Constants.DateFormat.COMMON_DATE_TIME_FORMAT, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void deleteComplaints() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_COMPLAINT);
    }


    public void deleteFeedback() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FEEDBACK);
    }

    public void deletePayment() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PAYMENT_COLLECTION);
    }

    public void deleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CONTACTS);
    }

    public void deleteGeo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_GEO_TRACKING);
    }

    public void deleteseviceorders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SERVICEORDER);
    }

    public void deleteserviceorderdetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SERVICEORDERDETAILS);
    }


    public void deleteDailyDiary() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_DAILYDAIRY);
    }

    public void deleteCustomerbankdetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CUSTOMER_BANKDETAILS);
    }


    public void deleteCommodityPrice() {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from " + TABLE_MI_COMMODITY_PRICE);
    }

    public void deleteCropShifts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MI_CROP_SHIFTS);
    }


    // Updating approval/reject status
    public void updateApprovalOrRejectStatus(String userId, String approval_status, String approval_comments, String orderId, boolean offlineStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TABLE_SERVICEORDER_APPROVAL_STATUS, approval_status);
        contentValues.put(KEY_TABLE_SERVICEORDER_UPDATED_BY, userId);
        contentValues.put(KEY_TABLE_SERVICEORDER_OFFLINE_APPROVAL_SET, offlineStatus ? 1 : 0);
        if (approval_comments != null && approval_comments.length() > 0) {
            contentValues.put(KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS, approval_comments);
        }


        // Inserting Row
        db.update(TABLE_SERVICEORDER, contentValues, KEY_TABLE_SERVICEORDER_FFM_ID + "=?", new String[]{String.valueOf(orderId)});
        db.close(); // Closing database connection
    }


    public ServiceOrderMaster getApproveRejectCommentData(String orderId) {


        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_APPROVAL_STATUS + ","
                + KEY_TABLE_SERVICEORDER_UPDATED_BY + ","
                + KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS
                + " FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " =" + orderId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            ServiceOrderMaster serviceOrderMasterData = new ServiceOrderMaster();
            serviceOrderMasterData.setID(cursor.getInt(0));
            serviceOrderMasterData.set_approval_status(cursor.getString(1));
            serviceOrderMasterData.set_updated_by(cursor.getString(2));
            serviceOrderMasterData.set_approval_comments(cursor.getString(3));

            return serviceOrderMasterData;


        }

        // return serviceOrder Master list
        return null;

    }


    // get Approve Reject Data offline
    public List<ServiceOrderMaster> getOfflineApproveRejectData() {

        List<ServiceOrderMaster> approvalRejectOrderList = new ArrayList<ServiceOrderMaster>();
        // Select All Query
        String selectQuery = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + ","
                + KEY_TABLE_SERVICEORDER_APPROVAL_STATUS + ","
                + KEY_TABLE_SERVICEORDER_UPDATED_BY + ","
                + KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS
                + " FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_OFFLINE_APPROVAL_SET + " =1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ServiceOrderMaster serviceOrderMasterData = new ServiceOrderMaster();
                serviceOrderMasterData.setID(cursor.getInt(0));
                serviceOrderMasterData.set_approval_status(cursor.getString(1));
                serviceOrderMasterData.set_updated_by(cursor.getString(2));
                serviceOrderMasterData.set_approval_comments(cursor.getString(3));

                approvalRejectOrderList.add(serviceOrderMasterData);
            } while (cursor.moveToNext());
        }

        // return serviceOrder Master list
        return approvalRejectOrderList;

    }


    public String getPlannerCompleteStatus(String date) {
        String selectQuerys = "SELECT  * FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " where " + KEY_EMP_PLAN_DATE_TIME + " like '" + date + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cc = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cc != null && cc.moveToFirst()) {

            return String.valueOf(cc.getInt(16)); //The 0 is the column index, we only have 1 column, so the index is 0

        }


        return "0";
    }


    public String[] getCheckinStatus(String date, String userId) {
        String[] aa = new String[5];
        String selectQuerys = "SELECT  * FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_VISIT_DATE + " like '" + date + "%' and user_id ='" + userId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cc = null;
        try {
            cc = db.rawQuery(selectQuerys, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cc != null && cc.moveToFirst()) {
                aa[0] = cc.getString(9);
                aa[1] = cc.getString(10);


                return aa; //The 0 is the column index, we only have 1 column, so the index is 0

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cc);
            Common.closeDataBase(db);
        }
        return null;
    }

    public void updateAprrovalStatus(String sqlitId, String ffmId, String aprrovalStatus) {


        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EMP_APPROVAL_STATUS, aprrovalStatus);
        contentValues.put(KEY_EMP_FFM_ID, ffmId);
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.update(TABLE_EMPLOYEE_VISIT_MANAGEMENT, contentValues, KEY_EMP_VISIT_ID + " = " + sqlitId, null);
            db.close();
        } catch (SQLiteDatabaseLockedException e) {
            e.printStackTrace();
            SQLiteDatabase db = this.getWritableDatabase();
            db.update(TABLE_EMPLOYEE_VISIT_MANAGEMENT, contentValues, KEY_EMP_VISIT_ID + " = " + sqlitId, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // insert version control data
    public void insertVersionControlData(List<VersionControlVo> versionControlVos) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (VersionControlVo versionControlVo : versionControlVos) {
            ContentValues values_regions = new ContentValues();
            values_regions.put(KEY_ID, versionControlVo.id);
            values_regions.put(VERSION_TABLE_NAME, versionControlVo.tableName);
            values_regions.put(VERSION_CODE, versionControlVo.status);
            values_regions.put(UPDATED_DATE, versionControlVo.updatedDate);

            db.insert(TABLE_VERSION_CONTROL, null, values_regions);
        }
        db.close(); // Closing database connection
    }

    public void updateVersionControlData(VersionControlVo versionControlVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_regions = new ContentValues();
        values_regions.put(VERSION_CODE, versionControlVo.status);
        values_regions.put(UPDATED_DATE, versionControlVo.updatedDate);
        try {

            db.update(TABLE_VERSION_CONTROL, values_regions, VERSION_TABLE_NAME + " = '" + versionControlVo.tableName + "'", null);

            db.close(); // Closing database connection
        } catch (SQLiteDatabaseLockedException e) {
            e.printStackTrace();
            SQLiteDatabase db1 = this.getWritableDatabase();
            db1.update(TABLE_VERSION_CONTROL, values_regions, VERSION_TABLE_NAME + " = '" + versionControlVo.tableName + "'", null);
            db1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<VersionControlVo> getVersionControlList() {
        List<VersionControlVo> versionControlVoList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + TABLE_VERSION_CONTROL + "", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                VersionControlVo versionControlVo = new VersionControlVo();
                versionControlVo.id = res.getString(0);
                versionControlVo.tableName = res.getString(1);
                versionControlVo.status = res.getString(3);

                versionControlVoList.add(versionControlVo);
                res.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(res);
        }
        return versionControlVoList;
    }


    public int deleteDataByTableName(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("deleted table: ", tableName);
        return db.delete(tableName, null, null);


    }


    public int[] deleteDataByTableName(String[] tableName) {
        int[] ids = new int[tableName.length];

        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < tableName.length; i++) {
            int id = db.delete(tableName[i], null, null);
            ids[i] = id;
            Log.d("deleted table: ", tableName[i]);
        }

        return ids;
    }

    public int deleteDataById(String tableName, String key, String uniqueId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("row deleted", tableName + " " + uniqueId);
        return db.delete(tableName, key + "=" + uniqueId, null);


    }
    // table SMD column names


    public long insertStackMovement(StockMovementPoJo stockMovementPoJo, StockMovementDetailsPojo stockMovementDetailsPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowid = 0;
        long relationId;
        relationId = getCombinationOf3(stockMovementPoJo.userId, stockMovementPoJo.companyId, stockMovementPoJo.divisionId, stockMovementPoJo.customerId);
        if (relationId == 0) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_TABLE_SM_MOVEMENT_TYPE, stockMovementPoJo.movementType);
            contentValues.put(KEY_TABLE_SM_USER_ID, stockMovementPoJo.userId);
            contentValues.put(KEY_TABLE_SM_DIVISION, stockMovementPoJo.divisionId);
            contentValues.put(KEY_TABLE_SM_COMPANY_ID, stockMovementPoJo.companyId);
            contentValues.put(KEY_CUSTOMER_ID, stockMovementPoJo.customerId);
            contentValues.put(KEY_TABLE_SM_CREATED_BY, stockMovementPoJo.createdBy);
            contentValues.put(KEY_TABLE_SM_UPDATED_BY, stockMovementPoJo.updatedBy);
            contentValues.put(KEY_TABLE_SM_STATUS, stockMovementPoJo.status);
            contentValues.put(FFM_ID, stockMovementPoJo.ffmId);
            contentValues.put(KEY_TABLE_SM_CREATED_DATETIME, stockMovementPoJo.createdDatetime);
            contentValues.put(KEY_TABLE_SM_UPDATED_DATETIME, stockMovementPoJo.updatedDatetime);

            // Inserting Row
            rowid = db.insert(TABLE_SM, null, contentValues);
            relationId = rowid;
            db.close(); // Closing database connection
        }

        insertStackMovementDetails(stockMovementDetailsPojo, relationId);

        return rowid;
    }

    public void insertStackMovementDetails(StockMovementDetailsPojo stockMovementDetailsPojo, long relationId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_TABLE_SMD_STOCK_MOVEMENT_ID, relationId);
        contentValues.put(KEY_TABLE_SMD_USER_TYPE, stockMovementDetailsPojo.userType);
        contentValues.put(KEY_TABLE_SMD_CUSTOMER_ID, stockMovementDetailsPojo.customerId);
        contentValues.put(KEY_TABLE_SMD_CROP_ID, stockMovementDetailsPojo.cropId);
        contentValues.put(KEY_TABLE_SMD_PRODUCT_ID, stockMovementDetailsPojo.productId);
        contentValues.put(KEY_TABLE_SMD_STOCK_PLACED, stockMovementDetailsPojo.stockPlaced);
        if (!stockMovementDetailsPojo.currentStock.equalsIgnoreCase("")) {
            contentValues.put(KEY_TABLE_SMD_CURRENT_STOCK, stockMovementDetailsPojo.currentStock);
        }

        contentValues.put(KEY_TABLE_SMD_PLACED_DATE, stockMovementDetailsPojo.placedDate);
        contentValues.put(KEY_TABLE_SMD_POG, stockMovementDetailsPojo.pog);
        contentValues.put(KEY_TABLE_CREATED_BY, stockMovementDetailsPojo.createdBy);
        contentValues.put(KEY_TABLE_UPDATED_BY, stockMovementDetailsPojo.updatedBy);
        contentValues.put(KEY_TABLE_SMD_CREATED_DATETIME, stockMovementDetailsPojo.createdDatetime);
        contentValues.put(KEY_TABLE_SMD_UPDATED_DATETIME, stockMovementDetailsPojo.updatedDatetime);
        contentValues.put(FFM_ID, stockMovementDetailsPojo.ffmId);
        contentValues.put(KEY_TABLE_SMD_ORDER_SAP_ID, stockMovementDetailsPojo.orderSapId);

        // Inserting Row
        db.insert(TABLE_SMD, null, contentValues);
        db.close(); // Closing database connection
    }

    public long insertStackMovementForRetailerDetails(StockMovementPoJo stockMovementPoJo, StockMovementRetailerDetails stockMovementDetailsPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowid = 0;
        long relationId;
        relationId = getCombinationOf3(stockMovementPoJo.userId, stockMovementPoJo.companyId, stockMovementPoJo.divisionId, stockMovementPoJo.customerId);
        if (relationId == 0) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_TABLE_SM_MOVEMENT_TYPE, stockMovementPoJo.movementType);
            contentValues.put(KEY_TABLE_SM_USER_ID, stockMovementPoJo.userId);
            contentValues.put(KEY_TABLE_SM_DIVISION, stockMovementPoJo.divisionId);
            contentValues.put(KEY_TABLE_SM_COMPANY_ID, stockMovementPoJo.companyId);
            contentValues.put(KEY_CUSTOMER_ID, stockMovementPoJo.companyId);
            contentValues.put(KEY_TABLE_SM_CREATED_BY, stockMovementPoJo.createdBy);
            contentValues.put(KEY_TABLE_SM_UPDATED_BY, stockMovementPoJo.updatedBy);
            contentValues.put(KEY_TABLE_SM_STATUS, stockMovementPoJo.status);
            contentValues.put(FFM_ID, stockMovementPoJo.ffmId);
            contentValues.put(KEY_TABLE_SM_CREATED_DATETIME, stockMovementPoJo.createdDatetime);
            contentValues.put(KEY_TABLE_SM_UPDATED_DATETIME, stockMovementPoJo.updatedDatetime);

            // Inserting Row
            rowid = db.insert(TABLE_SM, null, contentValues);
            relationId = rowid;
            db.close(); // Closing database connection
        }

        insertStackMovementRetailerDetails(stockMovementDetailsPojo, relationId);

        return rowid;
    }

    public void insertStackMovementRetailerDetails(StockMovementRetailerDetails stockMovementDetailsPojo, long relationId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_TABLE_SMD_STOCK_MOVEMENT_ID, relationId);
        contentValues.put(KEY_TABLE_SMD_USER_TYPE, stockMovementDetailsPojo.userType);
        contentValues.put(KEY_TABLE_SMD_CROP_ID, stockMovementDetailsPojo.cropId);
        contentValues.put(KEY_TABLE_SMD_PRODUCT_ID, stockMovementDetailsPojo.productId);
        contentValues.put(KEY_TABLE_SMD_STOCK_PLACED, stockMovementDetailsPojo.stockPlaced);
        if (!stockMovementDetailsPojo.currentStock.equalsIgnoreCase("")) {
            contentValues.put(KEY_TABLE_SMD_CURRENT_STOCK, stockMovementDetailsPojo.currentStock);
        }

        contentValues.put(KEY_TABLE_SMD_PLACED_DATE, stockMovementDetailsPojo.placedDate);
        contentValues.put(KEY_TABLE_SMD_POG, stockMovementDetailsPojo.pog);
        contentValues.put(KEY_TABLE_CREATED_BY, stockMovementDetailsPojo.createdBy);
        contentValues.put(KEY_TABLE_SMD_CREATED_DATETIME, stockMovementDetailsPojo.createdDatetime);
        contentValues.put(KEY_TABLE_SMD_UPDATED_DATETIME, stockMovementDetailsPojo.updatedDatetime);
        contentValues.put(KEY_TABLE_SMD_RETAILER_USER_ID, stockMovementDetailsPojo.userId);
        contentValues.put(KEY_TABLE_SMD_RETAILER_ID, stockMovementDetailsPojo.retailerId);
        contentValues.put(KEY_TABLE_SMD_RETAILER_VERIFIED, stockMovementDetailsPojo.verified);
        contentValues.put(KEY_TABLE_SMD_RETAILER_VERIFIED_BY, stockMovementDetailsPojo.verifiedBy);
        contentValues.put(FFM_ID, stockMovementDetailsPojo.ffmId);


        // Inserting Row
        db.insert(TABLE_STOCK_MOVEMENT_RETAILER_DETAILS, null, contentValues);
        db.close(); // Closing database connection
    }


    public long insertStackReturn(StockReturnPoJo stockReturnPoJo, StockReturnDetailsPoJo stockReturnDetailsPoJo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowid = 0;
        long relationId;
        relationId = getCombinationOf3FromStockReturn(stockReturnPoJo.userId, stockReturnPoJo.companyId, stockReturnPoJo.divisionId);
        if (relationId == 0) {
            ContentValues contentValues = new ContentValues();


            contentValues.put(KEY_CUSTOMER_ID, stockReturnPoJo.customerId);
            contentValues.put(KEY_USER_ID, stockReturnPoJo.userId);
            contentValues.put(KEY_DIVISION_ID, stockReturnPoJo.divisionId);
            contentValues.put(KEY_COMPANY_ID, stockReturnPoJo.companyId);
            contentValues.put(KEY_CREATED_BY, stockReturnPoJo.createdBy);
            contentValues.put(KEY_UPDATED_BY, stockReturnPoJo.updatedBy);
            contentValues.put(KEY_FFMID, stockReturnPoJo.ffmId);
            contentValues.put(KEY_CREATED_DATETIME, stockReturnPoJo.createdDatetime);
            contentValues.put(KEY_UPDATED_DATETIME, stockReturnPoJo.updatedDatetime);

            // Inserting Row
            rowid = db.insert(TABLE_STOCK_RETURNS, null, contentValues);
            relationId = rowid;
            db.close(); // Closing database connection
        }

        insertStackReturnDetails(stockReturnDetailsPoJo, relationId);

        return rowid;
    }

    public void insertStackReturnDetails(StockReturnDetailsPoJo stockReturnDetailsPoJo, long relationId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_STOCK_RETURNS_ID, relationId);
        contentValues.put(KEY_CROP_ID, stockReturnDetailsPoJo.cropId);
        contentValues.put(KEY_STOCK_RETURNS_DETAILS_PRODUCT_ID, stockReturnDetailsPoJo.productId);
        contentValues.put(KEY_QUANTITY, stockReturnDetailsPoJo.quantity);
        contentValues.put(KEY_FFMID, stockReturnDetailsPoJo.ffmId);


        // Inserting Row
        db.insert(TABLE_STOCK_RETURNS_DETAILS, null, contentValues);
        db.close(); // Closing database connection
    }

    public ArrayList<StockMovementPoJo> getOfflineStockPlacementUnSynced(int status) {
        ArrayList<StockMovementPoJo> list = new ArrayList<>();
        String selectQuerys = "SELECT  * FROM " + TABLE_SM + " where " + KEY_TABLE_SM_STATUS + " = " + status;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementPoJo stockMovementPoJo = new StockMovementPoJo();
                stockMovementPoJo.stockMovementId = cursor.getInt(0);
                stockMovementPoJo.movementType = cursor.getInt(1);
                stockMovementPoJo.userId = cursor.getInt(2);
                stockMovementPoJo.companyId = cursor.getInt(3);
                stockMovementPoJo.divisionId = cursor.getInt(4);
                stockMovementPoJo.status = cursor.getInt(5);
                stockMovementPoJo.createdBy = cursor.getString(6);
                stockMovementPoJo.updatedBy = cursor.getString(7);
                stockMovementPoJo.ffmId = cursor.getInt(8);
                stockMovementPoJo.createdDatetime = cursor.getString(9);
                stockMovementPoJo.updatedDatetime = cursor.getString(10);


                list.add(stockMovementPoJo);

            } while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<StockPlacementPopupPojo> getOfflineStockPlacementListById(int relationId, int cropId, int productId, int customerId) {
        ArrayList<StockPlacementPopupPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT  * FROM " + TABLE_SMD + " where " + KEY_TABLE_SMD_STOCK_MOVEMENT_ID + " = " + relationId + " and " + KEY_TABLE_SMD_STOCK_PLACED + " IS NOT 0 and " + KEY_TABLE_SMD_CROP_ID + " = " + cropId + " and " + KEY_TABLE_SMD_PRODUCT_ID + " = " + productId + " and " + KEY_TABLE_SMD_CUSTOMER_ID + " = " + customerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockPlacementPopupPojo stockPlacementPopupPojo = new StockPlacementPopupPojo();
                stockPlacementPopupPojo.stockMovementDetailId = cursor.getInt(0);
                stockPlacementPopupPojo.stockMovementId = cursor.getInt(1);
                stockPlacementPopupPojo.userType = cursor.getString(2);
                stockPlacementPopupPojo.customerId = cursor.getInt(3);
                stockPlacementPopupPojo.cropId = cursor.getInt(4);
                stockPlacementPopupPojo.productId = cursor.getInt(5);
                stockPlacementPopupPojo.stockPlaced = cursor.getString(6);
                stockPlacementPopupPojo.currentStock = cursor.getString(7);
                stockPlacementPopupPojo.placedDate = cursor.getString(8);
                stockPlacementPopupPojo.pog = cursor.getString(9);
                stockPlacementPopupPojo.createdBy = cursor.getString(10);
                stockPlacementPopupPojo.updatedBy = cursor.getString(11);
                stockPlacementPopupPojo.createdDatetime = cursor.getString(12);
                stockPlacementPopupPojo.updatedDatetime = cursor.getString(13);
                stockPlacementPopupPojo.ffmId = cursor.getInt(14);
                stockPlacementPopupPojo.orderSapId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SMD_ORDER_SAP_ID));

                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockPlacementPopupPojo> getOfflineSMRDStockPlacementListById(int relationId, int cropId, int productId, String retailerId) {
        ArrayList<StockPlacementPopupPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT  * FROM " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + " where " + KEY_TABLE_SMD_STOCK_MOVEMENT_ID + " = " + relationId + " and " + KEY_TABLE_SMD_STOCK_PLACED + " IS NOT 0 and " + KEY_TABLE_SMD_CROP_ID + " = " + cropId + " and " + KEY_TABLE_SMD_PRODUCT_ID + " = " + productId + " and " + KEY_TABLE_RETAILER_ID + " = " + retailerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockPlacementPopupPojo stockPlacementPopupPojo = new StockPlacementPopupPojo();
                stockPlacementPopupPojo.stockMovementDetailId = cursor.getInt(0);
                stockPlacementPopupPojo.stockMovementId = cursor.getInt(1);
                stockPlacementPopupPojo.userType = cursor.getString(2);
                stockPlacementPopupPojo.cropId = cursor.getInt(3);
                stockPlacementPopupPojo.productId = cursor.getInt(4);
                stockPlacementPopupPojo.stockPlaced = cursor.getString(5);
                stockPlacementPopupPojo.currentStock = cursor.getString(6);
                stockPlacementPopupPojo.placedDate = cursor.getString(7);
                stockPlacementPopupPojo.pog = cursor.getString(8);
                stockPlacementPopupPojo.createdBy = cursor.getString(9);
                stockPlacementPopupPojo.updatedBy = cursor.getString(10);
                stockPlacementPopupPojo.createdDatetime = cursor.getString(11);
                stockPlacementPopupPojo.updatedDatetime = cursor.getString(12);
                stockPlacementPopupPojo.ffmId = cursor.getInt(17);

                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<StockPlacementPopupPojo> getRetailerStockSupplyItems(String customerId, String cropId, String productId, String retailerId) {
        ArrayList<StockPlacementPopupPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT  * FROM " + TABLE_RETAILER_STOCK_SUPPLY + " where " + KEY_RETAILER_SS_DISTRIBUTOR_ID + " = " + customerId + " and " + KEY_RETAILER_SS_QUANTITY + " IS NOT 0 and " + KEY_RETAILER_SS_CROP_ID + " = " + cropId + " and " + KEY_RETAILER_SS_PRODUCT_ID + " = " + productId + " and " + KEY_RETAILER_SS_RETAILER_ID + " = " + retailerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockPlacementPopupPojo stockPlacementPopupPojo = new StockPlacementPopupPojo();
                stockPlacementPopupPojo.cropId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_CROP_ID)));
                stockPlacementPopupPojo.productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_PRODUCT_ID)));
                stockPlacementPopupPojo.placedDate = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_SUPPLIED_DATE));
                stockPlacementPopupPojo.stockPlaced = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_QUANTITY));

                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockMovementUnSynedPojo> getOfflineStockPlacementListUnSyncData1() {
        ArrayList<StockMovementUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,stock_movement.customer_id,placed_date,stock_movement.stock_movement_id,crop_id,product_id,stock_placed,current_stock,stock_movement_detail_id from stock_movement_detail left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id  where stock_movement_detail.ffm_id=0 group by (stock_movement_detail.stock_movement_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuerys, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    StockMovementUnSynedPojo stockPlacementPopupPojo = new StockMovementUnSynedPojo();
                    stockPlacementPopupPojo.stockMovementId = cursor.getInt(4);


                    list.add(stockPlacementPopupPojo);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }
        return list;
    }

    public ArrayList<StockReturnUnSynedPojo> getOfflineStockReturnListUnSyncData1() {
        ArrayList<StockReturnUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,customer_id,stock_returns.stock_returns_id,crop_id,product_id,stock_returns_details_id from stock_returns_details left join stock_returns on stock_returns.stock_returns_id=stock_returns_details.stock_returns_id  where stock_returns_details.ffmid=0 group by (stock_returns_details.stock_returns_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuerys, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    StockReturnUnSynedPojo stockPlacementPopupPojo = new StockReturnUnSynedPojo();
                    stockPlacementPopupPojo.stockReturnId = cursor.getInt(3);


                    list.add(stockPlacementPopupPojo);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }
        return list;
    }


    public ArrayList<StockReturnUnSynedPojo> getOfflineStockReturnsListUnSyncData(int stockReturnId) {
        ArrayList<StockReturnUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,customer_id,stock_returns.stock_returns_id,crop_id,product_id,stock_returns_details_id,quantity from stock_returns_details left join stock_returns on stock_returns.stock_returns_id=stock_returns_details.stock_returns_id  where stock_returns_details.ffmid=0 and stock_returns_details.stock_returns_id=" + stockReturnId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuerys, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    StockReturnUnSynedPojo stockPlacementPopupPojo = new StockReturnUnSynedPojo();
                    stockPlacementPopupPojo.companyId = cursor.getInt(0);
                    stockPlacementPopupPojo.divisionId = cursor.getInt(1);
                    stockPlacementPopupPojo.customerId = cursor.getInt(2);
                    stockPlacementPopupPojo.stockReturnId = cursor.getInt(3);
                    stockPlacementPopupPojo.cropId = cursor.getInt(4);
                    stockPlacementPopupPojo.productId = cursor.getInt(5);
                    stockPlacementPopupPojo.stockReturnsDetailsId = cursor.getInt(6);
                    stockPlacementPopupPojo.quantity = cursor.getString(7);


                    list.add(stockPlacementPopupPojo);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }
        return list;
    }


    public ArrayList<StockReturnUnSynedPojo> getStockReturnsData(int stockReturnId, String product_id) {
        ArrayList<StockReturnUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,customer_id,stock_returns.stock_returns_id,crop_id,product_id,stock_returns_details_id,quantity from stock_returns_details left join stock_returns on stock_returns.stock_returns_id=stock_returns_details.stock_returns_id  where stock_returns_details.stock_returns_id=" + stockReturnId + " and product_id=" + product_id + " order by stock_returns_details_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockReturnUnSynedPojo stockPlacementPopupPojo = new StockReturnUnSynedPojo();
                stockPlacementPopupPojo.companyId = cursor.getInt(0);
                stockPlacementPopupPojo.divisionId = cursor.getInt(1);
                stockPlacementPopupPojo.customerId = cursor.getInt(2);
                stockPlacementPopupPojo.stockReturnId = cursor.getInt(3);
                stockPlacementPopupPojo.cropId = cursor.getInt(4);
                stockPlacementPopupPojo.productId = cursor.getInt(5);
                stockPlacementPopupPojo.stockReturnsDetailsId = cursor.getInt(6);
                stockPlacementPopupPojo.quantity = cursor.getString(7);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockMovementRetailerDetails> getOfflineStockPlacementSMRDListUnSyncData1() {
        ArrayList<StockMovementRetailerDetails> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,placed_date,stock_movement.stock_movement_id,crop_id,product_id,stock_placed,current_stock,stock_movement_retailer_id from stock_movement_retailer_details left join stock_movement on stock_movement.stock_movement_id=stock_movement_retailer_details.stock_movement_id  where stock_movement_retailer_details.ffm_id=0 group by (stock_movement_retailer_details.stock_movement_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementRetailerDetails stockPlacementPopupPojo = new StockMovementRetailerDetails();
                stockPlacementPopupPojo.stockMovementId = cursor.getString(3);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<StockMovementRetailerDetails> getOfflineStockPlacementSMRDListUnSyncData(int stockMovementId) {
        ArrayList<StockMovementRetailerDetails> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,placed_date,stock_movement.stock_movement_id,crop_id,product_id,stock_placed,current_stock,stock_movement_retailer_id,user_type,retailer_id,customer_id from stock_movement_retailer_details left join stock_movement on stock_movement.stock_movement_id=stock_movement_retailer_details.stock_movement_id  where stock_movement_retailer_details.ffm_id=0 and stock_movement_retailer_details.stock_movement_id=" + stockMovementId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementRetailerDetails stockPlacementPopupPojo = new StockMovementRetailerDetails();
                stockPlacementPopupPojo.companyId = cursor.getInt(0);
                stockPlacementPopupPojo.divisionId = cursor.getInt(1);
                stockPlacementPopupPojo.placedDate = cursor.getString(2);
                stockPlacementPopupPojo.stockMovementId = cursor.getString(3);
                stockPlacementPopupPojo.cropId = cursor.getString(4);
                stockPlacementPopupPojo.productId = cursor.getString(5);
                stockPlacementPopupPojo.stockPlaced = cursor.getString(6);
                stockPlacementPopupPojo.currentStock = cursor.getString(7);
                stockPlacementPopupPojo.stockMovementRetailerId = cursor.getString(8);
                stockPlacementPopupPojo.userType = cursor.getString(9);
                stockPlacementPopupPojo.retailerId = cursor.getString(10);
                stockPlacementPopupPojo.customerId = cursor.getInt(11);


                list.add(stockPlacementPopupPojo);

            } while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<StockMovementUnSynedPojo> getOfflineStockPlacementListUnSyncData(int stockMovementId) {
        ArrayList<StockMovementUnSynedPojo> list = new ArrayList<>();
        String selectQuerys = "SELECT company_id,division_id,stock_movement.customer_id,placed_date,stock_movement.stock_movement_id,crop_id,product_id,stock_placed,current_stock,stock_movement_detail_id,user_type from stock_movement_detail left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id  where stock_movement_detail.ffm_id=0 and stock_movement_detail.stock_movement_id=" + stockMovementId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuerys, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    StockMovementUnSynedPojo stockPlacementPopupPojo = new StockMovementUnSynedPojo();
                    stockPlacementPopupPojo.companyId = cursor.getInt(0);
                    stockPlacementPopupPojo.divisionId = cursor.getInt(1);
                    stockPlacementPopupPojo.customerId = cursor.getInt(2);
                    stockPlacementPopupPojo.placedDate = cursor.getString(3);
                    stockPlacementPopupPojo.stockMovementId = cursor.getInt(4);
                    stockPlacementPopupPojo.cropId = cursor.getInt(5);
                    stockPlacementPopupPojo.productId = cursor.getInt(6);
                    stockPlacementPopupPojo.stockPlaced = cursor.getString(7);
                    stockPlacementPopupPojo.currentStock = cursor.getString(8);
                    stockPlacementPopupPojo.stockMovementDetailId = cursor.getInt(9);
                    stockPlacementPopupPojo.userType = cursor.getString(10);


                    list.add(stockPlacementPopupPojo);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }

        return list;
    }

    public ArrayList<StockMovementFirstListPojo> getOfflineStockPlacementList(String user_id, String company_id, String division_id, String customer_id) {
        ArrayList<StockMovementFirstListPojo> list = new ArrayList<>();
//        String selectQuerys = "SELECT sum(stock_placed)sumstockpalced,stock_movement_detail.product_id,brand_name,crop_id,stock_movement_detail.stock_movement_id,stock_movement_detail.customer_id FROM stock_movement_detail left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id left join products on products.product_id=stock_movement_detail.product_id where user_id=" + user_id + " and stock_movement.company_id=" + company_id + " and stock_movement.customer_id =" + customer_id + " and stock_movement.division_id=" + division_id + " group by (stock_movement_detail.product_id)";
        String selectQuerys = "SELECT sum(quantity)sumquantity,sdl.product_id,pr.brand_name,sdl.crop_id,sdl.stock_dispatch_id,sd.distributor_id FROM stock_dispatch_line_items sdl left join stock_dispatch sd on sd.stock_dispatch_id=sdl.stock_dispatch_id left join products pr on pr.product_id=sdl.product_id where user_id=" + user_id + " and sd.company_id=" + company_id + " and sd.distributor_id =" + customer_id + " and sd.division_id=" + division_id /*+ " group by (sdl.product_id)"*/;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockMovementFirstListPojo stockMovementFirstListPojo = new StockMovementFirstListPojo();
                /*String.valueOf(getSumOfStockPlacement(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),customer_id)-
                        (getstockMovementRetailerDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),userId)+
                                getstockMovementDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5))));
*/
                int pog = 0;
               /* if (getstockMovementRetailerDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),userId)==0){
                    pog="0";
                }else{
                    pog=String.valueOf(getSumOfStockPlacement(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),customer_id) -
                            (getstockMovementRetailerDetails(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) +
                                    getstockMovementDetails(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5))));
                }*/

                List<MappedRetailerPojo> mappedRetailerData = getMappedRetailerWithDistributorAndProduct(cursor.getString(4), String.valueOf(cursor.getInt(1)), String.valueOf(cursor.getInt(3)));
                for (MappedRetailerPojo mappedRetailerPojo : mappedRetailerData) {
                    pog = pog + Integer.parseInt(mappedRetailerPojo.pog);
                }
                stockMovementFirstListPojo.stockPlaced = cursor.getString(0);
                stockMovementFirstListPojo.currentStock = getCurrentStock(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4));
                stockMovementFirstListPojo.productId = cursor.getInt(1);
                stockMovementFirstListPojo.brandName = cursor.getInt(2);
                stockMovementFirstListPojo.cropId = cursor.getInt(3);
                stockMovementFirstListPojo.pog = String.valueOf(pog);
                stockMovementFirstListPojo.stockMovementId = cursor.getString(4);


                list.add(stockMovementFirstListPojo);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public StockMovementFirstListPojo getOfflineStockPlacement(String user_id, String company_id, String division_id, String customer_id, String productId) {
        ArrayList<StockMovementFirstListPojo> list = new ArrayList<>();
//        String selectQuerys = "SELECT sum(stock_placed)sumstockpalced,stock_movement_detail.product_id,brand_name,crop_id,stock_movement_detail.stock_movement_id,stock_movement_detail.customer_id FROM stock_movement_detail left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id left join products on products.product_id=stock_movement_detail.product_id where user_id=" + user_id + " and stock_movement.company_id=" + company_id + " and stock_movement.customer_id =" + customer_id + " and stock_movement.division_id=" + division_id + " group by (stock_movement_detail.product_id)";
        String selectQuerys = "SELECT sum(quantity)sumquantity,sdl.product_id,pr.brand_name,sdl.crop_id,sdl.stock_dispatch_id,sd.distributor_id FROM stock_dispatch_line_items sdl left join stock_dispatch sd on sd.stock_dispatch_id=sdl.stock_dispatch_id left join products pr on pr.product_id=sdl.product_id where user_id=" + user_id + " and sd.company_id=" + company_id + " and sd.distributor_id =" + customer_id + " and sd.division_id=" + division_id + " and sdl.product_id=" + productId/*+ " group by (sdl.product_id)"*/;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        StockMovementFirstListPojo stockMovementFirstListPojo = null;
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
//            do {
            stockMovementFirstListPojo = new StockMovementFirstListPojo();
                /*String.valueOf(getSumOfStockPlacement(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),customer_id)-
                        (getstockMovementRetailerDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),userId)+
                                getstockMovementDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5))));
*/
            int pog = 0;
               /* if (getstockMovementRetailerDetails(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),userId)==0){
                    pog="0";
                }else{
                    pog=String.valueOf(getSumOfStockPlacement(cursor.getInt(1),cursor.getInt(3),cursor.getInt(4),customer_id) -
                            (getstockMovementRetailerDetails(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) +
                                    getstockMovementDetails(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5))));
                }*/

            List<MappedRetailerPojo> mappedRetailerData = getMappedRetailerWithDistributorCropProduct(cursor.getString(5), String.valueOf(cursor.getInt(1)), String.valueOf(cursor.getInt(3)));
            for (MappedRetailerPojo mappedRetailerPojo : mappedRetailerData) {
                pog = pog + Integer.parseInt(mappedRetailerPojo.stockPlaced);
            }
            stockMovementFirstListPojo.stockPlaced = cursor.getString(0);
            stockMovementFirstListPojo.pog = String.valueOf(pog);
//            stockMovementFirstListPojo.currentStock = String.valueOf(Integer.parseInt(stockMovementFirstListPojo.stockPlaced)-pog);
            stockMovementFirstListPojo.productId = cursor.getInt(1);
            stockMovementFirstListPojo.brandName = cursor.getInt(2);
            stockMovementFirstListPojo.cropId = cursor.getInt(3);

            stockMovementFirstListPojo.stockMovementId = cursor.getString(4);


            list.add(stockMovementFirstListPojo);

//            } while (cursor.moveToNext());
        }

        cursor.close();
        return stockMovementFirstListPojo;
    }

    public String getCurrentStock(int product_id, int crop_id, int stock_movement_id) {
        String selectQuerys = "select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " order by stock_movement_detail_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "0";
    }

    public String getCurrentStockSMRD(int product_id, int crop_id, int stock_movement_id, String retailer_id) {
        String selectQuerys = "select  current_stock from stock_movement_retailer_details where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and retailer_id =" + retailer_id + " order by stock_movement_retailer_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "0";
    }


    public int getCombinationOf3(int user_id, int company_id, int division_id, int customerId) {
        String selectQuerys = "SELECT " + KEY_TABLE_SM_ID + " FROM " + TABLE_SM + " where " + KEY_TABLE_SM_USER_ID + " = " + user_id + " AND " + KEY_TABLE_SM_COMPANY_ID + " = " + company_id + " AND " + KEY_TABLE_SM_DIVISION + " = " + division_id + " AND " + KEY_CUSTOMER_ID + " = " + customerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return cursor.getInt(0);

        }


        return 0;
    }

    public int getCombinationOf3FromStockReturn(int user_id, int company_id, int division_id) {
        String selectQuerys = "SELECT " + KEY_STOCK_RETURNS_ID + " FROM " + TABLE_STOCK_RETURNS + " where " + KEY_USER_ID + " = " + user_id + " AND " + KEY_COMPANY_ID + " = " + company_id + " AND " + KEY_DIVISION_ID + " = " + division_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            int stockRetunId = cursor.getInt(0);
            cursor.close();
            return stockRetunId;

        }

        cursor.close();
        return 0;
    }

    public int getCombinationOf3SMRD(int user_id, int company_id, int division_id) {
        String selectQuerys = "SELECT " + KEY_TABLE_SM_ID + " FROM " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + " where " + KEY_TABLE_SM_USER_ID + " = " + user_id + " AND " + KEY_TABLE_SM_COMPANY_ID + " = " + company_id + " AND " + KEY_TABLE_SM_DIVISION + " = " + division_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return cursor.getInt(0);

        }


        return 0;
    }


    public long getSumOfStockPlacement(int product_id, int crop_id, int stock_movement_id, String customer_id) {
        long sum = 0;
        String selectQuerys = "SELECT sum(stock_placed)sumstockpalced FROM stock_movement_detail  where product_id=" + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and customer_id=" + customer_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getString(0) == null) {
                sum = 0;
            } else {
                sum = Long.parseLong(cursor.getString(0));
            }
            return sum;
        }

        cursor.close();
        return sum;
    }

    public long getstockMovementRetailerDetails(int product_id, int crop_id, int stock_movement_id, String user_id) {
        long sum = 0;
        String selectQuerys = "select  current_stock from stock_movement_retailer_details where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and user_id=" + user_id + " order by stock_movement_retailer_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public long getstockMovementDetails(int product_id, int crop_id, int stock_movement_id, int customer_id) {
        long sum = 0;
        String selectQuerys = "select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and customer_id=" + customer_id + " order by stock_movement_detail_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public List<Retailer> getOfflineRetailers() {
        List<Retailer> dairy = new ArrayList<Retailer>();
        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_FFMID + " = 0 ORDER BY " + KEY_TABLE_RETAILER_PRIMARY_ID + " DESC LIMIT 20";
            Log.e("bd query", selectQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Retailer retailer = new Retailer();
                    retailer.setID(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_RETAILER_PRIMARY_ID)));
                    retailer.set_ret_masterid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_MASTER_ID)));
                    retailer.set_ret_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_NAME)));
                    retailer.set_ret_tin_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_TIN_NO)));
                    retailer.set_ret_address(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_ADDRESS)));
                    retailer.set_ret_district(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_DISTRICT)));
                    retailer.set_ret_taluka(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_TALUKA)));
                    retailer.set_ret_village(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_VILLAGE)));
                    retailer.set_ret_region_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_REGION_ID)));
                    retailer.set_ret_gstin_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_GSTIN_NO)));
                    retailer.set_ret_phone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_PHONE)));
                    retailer.set_ret_mobile(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_MOBILE)));
                    retailer.set_email(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_EMAIL_ID)));
                    retailer.set_ret_dist_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_DISTRIBUTOR_ID)));
                    retailer.set_ret_dist_sap_code(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_SAP_CODE)));
                    retailer.set_ret_status(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_STATUS)));
                    retailer.set_ret_cdatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_CREATED_DATETIME)));
                    retailer.set_ret_udatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_UPDATED_DATETIME)));
                    retailer.set_ffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_FFMID)));
                    dairy.add(retailer);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        }
        // return contact list
        return dairy;
    }

    public int addRetailers(Retailer retailer) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
//        if (!isTINnoExist(retailer.get_ret_tin_no())) {
        ContentValues values_retailers = new ContentValues();
        values_retailers.put(KEY_TABLE_RETAILER_MASTER_ID, retailer.get_ret_masterid());
        values_retailers.put(KEY_TABLE_RETAILER_NAME, retailer.get_ret_name());
        values_retailers.put(KEY_TABLE_RETAILER_TIN_NO, retailer.get_ret_tin_no());
        values_retailers.put(KEY_TABLE_RETAILER_ADDRESS, retailer.get_ret_address());
        values_retailers.put(KEY_TABLE_RETAILER_DISTRICT, retailer.get_ret_district());
        values_retailers.put(KEY_TABLE_RETAILER_TALUKA, retailer.get_ret_taluka());
        values_retailers.put(KEY_TABLE_RETAILER_VILLAGE, retailer.get_ret_village());
        values_retailers.put(KEY_TABLE_RETAILER_REGION_ID, retailer.get_ret_region_id());
        values_retailers.put(KEY_TABLE_RETAILER_GSTIN_NO, retailer.get_ret_gstin_no());
        values_retailers.put(KEY_TABLE_RETAILER_PHONE, retailer.get_ret_phone());
        values_retailers.put(KEY_TABLE_RETAILER_MOBILE, retailer.get_ret_mobile());
        values_retailers.put(KEY_TABLE_RETAILER_EMAIL_ID, retailer.get_email());
        values_retailers.put(KEY_TABLE_RETAILER_DISTRIBUTOR_ID, retailer.get_ret_dist_id());
        values_retailers.put(KEY_TABLE_RETAILER_SAP_CODE, retailer.get_ret_dist_sap_code());
        values_retailers.put(KEY_TABLE_RETAILER_STATUS, retailer.get_ret_status());
        values_retailers.put(KEY_TABLE_RETAILER_CREATED_DATETIME, retailer.get_ret_cdatetime());
        values_retailers.put(KEY_TABLE_RETAILER_UPDATED_DATETIME, retailer.get_ret_udatetime());
        values_retailers.put(KEY_TABLE_RETAILER_FFMID, retailer.get_ffmid());

        if (isAlreadyRecordExist(db, String.valueOf(retailer.getID()), TABLE_RETAILER, KEY_TABLE_RETAILER_PRIMARY_ID) == 0) {
            id = (int) db.insert(TABLE_RETAILER, null, values_retailers);
        } else {
            id = db.update(TABLE_RETAILER, values_retailers, KEY_TABLE_RETAILER_PRIMARY_ID + "=?", new String[]{String.valueOf(retailer.getID())});
        }


//        } else {
//
//        }
        db.close(); // Closing database connection
        return id;
    }

    public int insertServiceOrderApproval(ServiceOrderApproval soa) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
//        if (!isTINnoExist(retailer.get_ret_tin_no())) {
        ContentValues values = new ContentValues();
//        values.put(KEY_SOA_PRIMARY_ID, soa.primaryId);
        values.put(KEY_SOA_SERVICE_ORDER_APPROVAL_ID, soa.orderApprovalId);
        values.put(KEY_SOA_ORDER_ID, soa.orderId);
        values.put(KEY_SOA_USER_ID, soa.userId);
        values.put(KEY_SOA_ASSIGNED_USER_ID, soa.assignedUserId);
        values.put(KEY_SOA_ORDER_STATUS, soa.orderStatus);
        values.put(KEY_SOA_CREATED_BY, soa.createdBy);
        values.put(KEY_SOA_MODIFIED_BY, soa.modifiedBy);
        values.put(KEY_SOA_C_DATE_TIME, soa.cDateTime);
        values.put(KEY_SOA_U_DATE_TIME, soa.uDateTime);
        values.put(KEY_SOA_SYNC_STATUS, soa.syncStatus);
        values.put(KEY_SOA_PENDING_BY, soa.pendingBy);

        if (isAlreadyRecordExist(db, String.valueOf(soa.orderApprovalId), TABLE_SERVICE_ORDER_APPROVAL, KEY_SOA_SERVICE_ORDER_APPROVAL_ID) == 0) {
            id = (int) db.insert(TABLE_SERVICE_ORDER_APPROVAL, null, values);
        } else {
            id = db.update(TABLE_SERVICE_ORDER_APPROVAL, values, KEY_SOA_SERVICE_ORDER_APPROVAL_ID + "=?", new String[]{String.valueOf(soa.orderApprovalId)});
        }
//        } else {
//
//        }
        db.close(); // Closing database connection
        return id;
    }

    public int insertSeason(Season season) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SEASON_SEASON_ID, season.seasonMasterId);
        values.put(KEY_SEASON_SEASON_NAME, season.seasonName);
        values.put(KEY_SEASON_STATUS, season.status);
        id = (int) db.insert(TABLE_SEASON, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public int insertGodown(Godown godown) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GODOWN_ID, godown.godownId);
        values.put(KEY_GODOWN_NAME, godown.godownName);
        values.put(KEY_GODOWN_CODE, godown.godownCode);
        values.put(KEY_GODOWN_COMPANY_NAME, godown.companyName);
        values.put(KEY_GODOWN_REGION_NAME, godown.regionName);
        id = (int) db.insert(TABLE_GODOWN, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public int insertGodownStock(GodownStock godownStock) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GODOWN_STOCK_STOCK_GODOWN_ID, godownStock.stockGodownId);
        values.put(KEY_GODOWN_STOCK_GODOWN_ID, godownStock.godownId);
        values.put(KEY_GODOWN_STOCK_DIVISION_NAME, godownStock.divisionName);
        values.put(KEY_GODOWN_STOCK_CROP_NAME, godownStock.cropName);
        values.put(KEY_GODOWN_STOCK_PRODUCT_NAME, godownStock.productName);
        values.put(KEY_GODOWN_STOCK_QUANTITY, godownStock.quantity);
        id = (int) db.insert(TABLE_GODOWN_STOCK, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public List<Godown> getAllGodowns() {
        List<Godown> godowns = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GODOWN;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Godown godown = new Godown();
                godown.godownName = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_NAME));
                godown.companyName = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_COMPANY_NAME));
                godown.godownCode = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_CODE));
                godown.godownId = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_ID));
                godown.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_GODOWN_PRIMARY_ID));
                godown.regionName = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_REGION_NAME));
                godowns.add(godown);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return godowns;
    }

    public List<GodownStock> getGodownStocksByID(String goDownId) {
        List<GodownStock> godownStocks = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GODOWN_STOCK + " WHERE " + KEY_GODOWN_STOCK_GODOWN_ID + " = " + goDownId;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                GodownStock godownStock = new GodownStock();
                godownStock.cropName = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_STOCK_CROP_NAME));
                godownStock.divisionName = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_STOCK_DIVISION_NAME));
                godownStock.godownId = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_STOCK_GODOWN_ID));
                godownStock.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_GODOWN_STOCK_PRIMARY_ID));
                godownStock.productName = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_STOCK_PRODUCT_NAME));
                godownStock.quantity = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_STOCK_QUANTITY));
                godownStock.stockGodownId = cursor.getString(cursor.getColumnIndex(KEY_GODOWN_STOCK_STOCK_GODOWN_ID));
                godownStocks.add(godownStock);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return godownStocks;
    }


    public int insertSeasonLineItem(SeasonLineItem lineItem) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SEASON_LINE_ITEM_ID, lineItem.seasonLineItemId);
        values.put(KEY_SEASON_SEASON_ID, lineItem.seasonMasterId);
        values.put(KEY_SEASON_LINE_ITEM_DIVISION_ID, lineItem.divisionId);
        values.put(KEY_SEASON_LINE_ITEM_VALID_FROM, lineItem.validFrom);
        values.put(KEY_SEASON_LINE_ITEM_VALID_TO, lineItem.validTo);
        id = (int) db.insert(TABLE_SEASON_LINE_ITEMS, null, values);
        db.close(); // Closing database connection
        return id;
    }


    public int insertYE(YieldReqVo yield) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_YE_DIVISION_ID, yield.divisionId);
        values.put(KEY_YE_CROP_ID, yield.cropId);
        values.put(KEY_YE_PRODUCT_ID, yield.productId);
        values.put(KEY_YE_AVG_NO_OF_BALLS_PLANT, yield.avgNoOfBallsPlant);
        values.put(KEY_YE_AVG_BALL_WEIGHT, yield.avgBallWeight);
        values.put(KEY_YE_ROW_ROW_DISTANCE, yield.rowToRowDistance);
        values.put(KEY_YE_PLANT_PLANT_DISTANCE, yield.plantToPlantDistance);
        values.put(KEY_YE_AREA, yield.area);
        values.put(KEY_YE_YIELD, yield.yield);
        values.put(KEY_YE_RESULT, yield.result);
        values.put(KEY_YE_CREATED_BY, yield.createdBy);
        id = (int) db.insert(TABLE_YE, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public List<YieldReqVo> getYERecords() {
        List<YieldReqVo> yields = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_YE;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    YieldReqVo yieldReqVo = new YieldReqVo();
                    yieldReqVo.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_YE_PRIMARY_ID));
                    yieldReqVo.divisionId = cursor.getString(cursor.getColumnIndex(KEY_YE_DIVISION_ID));
                    yieldReqVo.cropId = cursor.getString(cursor.getColumnIndex(KEY_YE_CROP_ID));
                    yieldReqVo.productId = cursor.getString(cursor.getColumnIndex(KEY_YE_PRODUCT_ID));
                    yieldReqVo.avgNoOfBallsPlant = cursor.getString(cursor.getColumnIndex(KEY_YE_AVG_NO_OF_BALLS_PLANT));
                    yieldReqVo.avgBallWeight = cursor.getString(cursor.getColumnIndex(KEY_YE_AVG_BALL_WEIGHT));
                    yieldReqVo.rowToRowDistance = cursor.getString(cursor.getColumnIndex(KEY_YE_ROW_ROW_DISTANCE));
                    yieldReqVo.plantToPlantDistance = cursor.getString(cursor.getColumnIndex(KEY_YE_PLANT_PLANT_DISTANCE));
                    yieldReqVo.area = cursor.getString(cursor.getColumnIndex(KEY_YE_AREA));
                    yieldReqVo.yield = cursor.getString(cursor.getColumnIndex(KEY_YE_YIELD));
                    yieldReqVo.result = cursor.getString(cursor.getColumnIndex(KEY_YE_CREATED_BY));
                    yields.add(yieldReqVo);
                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();
        } catch (Exception e) {

        }
        return yields;
    }

    public void deleteYE(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_YE, KEY_YE_PRIMARY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int insertMarketPotential(MarketPotential mp) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
//        if (!isTINnoExist(retailer.get_ret_tin_no())) {
        ContentValues values = new ContentValues();
//        values.put(KEY_SOA_PRIMARY_ID, soa.primaryId);
        values.put(KEY_MARKET_POTENTIAL_USER_ID, mp.userId);
        values.put(KEY_MARKET_POTENTIAL_REGION_ID, mp.regionId);
        values.put(KEY_MARKET_POTENTIAL_DISTRICT, mp.district);
        values.put(KEY_MARKET_POTENTIAL_TALUKA, mp.taluka);
        values.put(KEY_MARKET_POTENTIAL_VILLAGE, mp.village);
        values.put(KEY_MARKET_POTENTIAL_DIVISION_ID, mp.divisionId);
        values.put(KEY_MARKET_POTENTIAL_CROP_ID, mp.cropId);
        values.put(KEY_MARKET_POTENTIAL_KHARIF_CROP_ACREAGE, mp.kharifCropAcreage);
        values.put(KEY_MARKET_POTENTIAL_RABI_CROP_ACREAGE, mp.rabiCropAcreage);
        values.put(KEY_MARKET_POTENTIAL_SUMMER_CROP_ACREAGE, mp.summerCropAcreage);
        values.put(KEY_MARKET_POTENTIAL_TOTAL_POTENTIAL_ACREAGE, mp.totalPotentialAcreage);
        values.put(KEY_MARKET_POTENTIAL_SEED_USAGE_QUANTITY_ACRE, mp.seedUsageQuanity);
        values.put(KEY_MARKET_POTENTIAL_TOTAL_MARKET_POTENTIAL_VOLUME, mp.totalMarketPotentialVolume);
        values.put(KEY_MARKET_POTENTIAL_NSL_SALE, mp.nslSale);
        values.put(KEY_MARKET_POTENTIAL_TOP_COMPANY_1_NAME, mp.topCompanyName1);
        values.put(KEY_MARKET_POTENTIAL_COMPANY_1_QUANTITY, mp.company1Qty);
        values.put(KEY_MARKET_POTENTIAL_TOP_COMPANY_2_NAME, mp.topCompanyName2);
        values.put(KEY_MARKET_POTENTIAL_COMPANY_2_QUANTITY, mp.company2Qty);
        values.put(KEY_MARKET_POTENTIAL_TOP_COMPANY_3_NAME, mp.topCompanyName3);
        values.put(KEY_MARKET_POTENTIAL_COMPANY_3_QUANTITY, mp.company3Qty);
        values.put(KEY_MARKET_POTENTIAL_TOP_COMPANY_4_NAME, mp.topCompanyName4);
        values.put(KEY_MARKET_POTENTIAL_COMPANY_4_QUANTITY, mp.company4Qty);
        values.put(KEY_MARKET_POTENTIAL_TOP_COMPANY_5_NAME, mp.topCompanyName5);
        values.put(KEY_MARKET_POTENTIAL_COMPANY_5_QUANTITY, mp.company5Qty);
        values.put(KEY_MARKET_POTENTIAL_CROP_NAME, mp.cropName);
        values.put(KEY_MARKET_POTENTIAL_DIVISION_NAME, mp.divisionName);
        values.put(KEY_MARKET_POTENTIAL_FFM_ID, mp.ffmId);


        if (isAlreadyRecordExist(db, String.valueOf(mp.ffmId), TABLE_MARKET_POTENTIAL, KEY_MARKET_POTENTIAL_FFM_ID) == 0) {
            id = (int) db.insert(TABLE_MARKET_POTENTIAL, null, values);
        } else {
            id = db.update(TABLE_MARKET_POTENTIAL, values, KEY_MARKET_POTENTIAL_FFM_ID + "=?", new String[]{String.valueOf(mp.ffmId)});
        }
//        } else {
//
//        }
        db.close(); // Closing database connection
        return id;
    }

    public List<MarketPotential> getAllMarketPotentials(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_MARKET_POTENTIAL + " WHERE " + KEY_MARKET_POTENTIAL_USER_ID + " in (" + userId + ") ORDER BY " + KEY_MARKET_POTENTIAL_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getMarketPotentialData(cursor);
    }

    public List<MarketPotential> getofflineMarketPotentials(String userId) {

        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_MARKET_POTENTIAL + " WHERE " + KEY_MARKET_POTENTIAL_USER_ID + " in (" + userId + ") AND " + KEY_MARKET_POTENTIAL_FFM_ID + " IS NULL " + " ORDER BY " + KEY_MARKET_POTENTIAL_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getMarketPotentialData(cursor);
    }


    public int insertCompetitorChannel(CompetitorChannel cc) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COMP_CHANNEL_USER_ID, cc.userId);
        values.put(KEY_COMP_CHANNEL_REGION_ID, cc.regionId);
        values.put(KEY_COMP_CHANNEL_DISTRICT, cc.district);
        values.put(KEY_COMP_CHANNEL_FFM_ID, cc.ffmId);
        values.put(KEY_COMP_CHANNEL_TERRITORY, cc.territory);
        values.put(KEY_COMP_CHANNEL_TOTAL_NO_OF_VILLAGES, cc.totalNoOfVillages);
        values.put(KEY_COMP_CHANNEL_TOTAL_NO_OF_DISTRIBUTORS, cc.totalNoOfDistributors);
        values.put(KEY_COMP_CHANNEL_TOTAL_NO_OF_RETAILERS, cc.totalNoOfRetailers);
        values.put(KEY_COMP_CHANNEL_NO_OF_NSL_VILLAGES, cc.noOfNslVillages);
        values.put(KEY_COMP_CHANNEL_NO_OF_NSL_DISTRIBUTORS, cc.noOfNslDistributors);
        values.put(KEY_COMP_CHANNEL_NO_OF_NSL_RETAILERS, cc.noOfNslRetailers);
        values.put(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_1, cc.competitorCompanyName1);
        values.put(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_1, cc.noOfDistributors1);
        values.put(KEY_COMP_CHANNEL_NO_OF_RETAILERS_1, cc.noOfRetailers1);
        values.put(KEY_COMP_CHANNEL_MARKET_PENETRATION_1, cc.marketPenetration1);
        values.put(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_2, cc.competitorCompanyName2);
        values.put(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_2, cc.noOfDistributors2);
        values.put(KEY_COMP_CHANNEL_NO_OF_RETAILERS_2, cc.noOfRetailers2);
        values.put(KEY_COMP_CHANNEL_MARKET_PENETRATION_2, cc.marketPenetration2);
        values.put(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_3, cc.competitorCompanyName3);
        values.put(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_3, cc.noOfDistributors3);
        values.put(KEY_COMP_CHANNEL_NO_OF_RETAILERS_3, cc.noOfRetailers3);
        values.put(KEY_COMP_CHANNEL_MARKET_PENETRATION_3, cc.marketPenetration3);
        values.put(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_4, cc.competitorCompanyName4);
        values.put(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_4, cc.noOfDistributors4);
        values.put(KEY_COMP_CHANNEL_NO_OF_RETAILERS_4, cc.noOfRetailers4);
        values.put(KEY_COMP_CHANNEL_MARKET_PENETRATION_4, cc.marketPenetration4);
        values.put(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_5, cc.competitorCompanyName5);
        values.put(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_5, cc.noOfDistributors5);
        values.put(KEY_COMP_CHANNEL_NO_OF_RETAILERS_5, cc.noOfRetailers5);
        values.put(KEY_COMP_CHANNEL_MARKET_PENETRATION_5, cc.marketPenetration5);
        if (isAlreadyRecordExist(db, String.valueOf(cc.ffmId), TABLE_COMPETITOR_CHANNEL, KEY_COMP_CHANNEL_FFM_ID) == 0) {
            id = (int) db.insert(TABLE_COMPETITOR_CHANNEL, null, values);
        } else {
            id = db.update(TABLE_COMPETITOR_CHANNEL, values, KEY_COMP_CHANNEL_FFM_ID + "=?", new String[]{String.valueOf(cc.ffmId)});
        }
        db.close(); // Closing database connection
        return id;


    }

    public List<CompetitorChannel> getAllCompetitorChannels(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_COMPETITOR_CHANNEL + " WHERE " + KEY_COMP_CHANNEL_USER_ID + " in (" + userId + ") ORDER BY " + KEY_COMP_CHANNEL_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getCompChannelData(cursor);
    }

    public List<CompetitorChannel> getOfflineCompetitorChannels(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_COMPETITOR_CHANNEL + " WHERE " + KEY_COMP_CHANNEL_USER_ID + " in (" + userId + ") AND " + KEY_COMP_CHANNEL_FFM_ID + " IS NULL " + " ORDER BY " + KEY_MARKET_POTENTIAL_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getCompChannelData(cursor);
    }

    private List<CompetitorChannel> getCompChannelData(Cursor cursor) {
        List<CompetitorChannel> ccList = new ArrayList<CompetitorChannel>();
        if (cursor.moveToFirst()) {
            do {
                CompetitorChannel cc = new CompetitorChannel();
                cc.id = cursor.getInt(cursor.getColumnIndex(KEY_COMP_CHANNEL_PRIMARY_ID));
                cc.ffmId = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_FFM_ID));
                cc.userId = cursor.getInt(cursor.getColumnIndex(KEY_COMP_CHANNEL_USER_ID));
                cc.regionId = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_REGION_ID));
                cc.district = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_DISTRICT));
                cc.territory = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_TERRITORY));
                cc.totalNoOfVillages = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_TOTAL_NO_OF_VILLAGES));
                cc.totalNoOfDistributors = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_TOTAL_NO_OF_DISTRIBUTORS));
                cc.totalNoOfRetailers = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_TOTAL_NO_OF_RETAILERS));
                cc.noOfNslVillages = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_NSL_VILLAGES));
                cc.noOfNslDistributors = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_NSL_DISTRIBUTORS));
                cc.noOfNslRetailers = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_NSL_RETAILERS));
                cc.competitorCompanyName1 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_1));
                cc.noOfDistributors1 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_1));
                cc.noOfRetailers1 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_RETAILERS_1));
                cc.marketPenetration1 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_MARKET_PENETRATION_1));
                cc.competitorCompanyName2 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_2));
                cc.noOfDistributors2 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_2));
                cc.noOfRetailers2 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_RETAILERS_2));
                cc.marketPenetration2 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_MARKET_PENETRATION_2));
                cc.competitorCompanyName3 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_3));
                cc.noOfDistributors3 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_3));
                cc.noOfRetailers3 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_RETAILERS_3));
                cc.marketPenetration3 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_MARKET_PENETRATION_3));
                cc.competitorCompanyName4 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_4));
                cc.noOfDistributors4 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_4));
                cc.noOfRetailers4 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_RETAILERS_4));
                cc.marketPenetration4 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_MARKET_PENETRATION_4));
                cc.competitorCompanyName5 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_5));
                cc.noOfDistributors5 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_5));
                cc.noOfRetailers5 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_NO_OF_RETAILERS_5));
                cc.marketPenetration5 = cursor.getString(cursor.getColumnIndex(KEY_COMP_CHANNEL_MARKET_PENETRATION_5));

                // Adding contact to list
                ccList.add(cc);
            } while (cursor.moveToNext());
        }
        return ccList;
    }

    public int insertProductPricingSurvey(ProductPricingSurvey pps) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SURVEY_USER_ID, pps.userId);
        values.put(KEY_SURVEY_REGION_ID, pps.regionId);
        values.put(KEY_SURVEY_COMPANY_BILLING_PRICE, pps.companyBillingPrice);
        values.put(KEY_SURVEY_COMPETITOR_COMPANY_NAME, pps.competitorCompanyName);
        values.put(KEY_SURVEY_COMPETITOR_PRODUCT_NAME, pps.competitorProductName);
        values.put(KEY_SURVEY_CROP_ID, pps.cropId);
        values.put(KEY_SURVEY_CROP_NAME, pps.cropName);
        values.put(KEY_SURVEY_CURRENT_YEAR_SALE_IN_VILLAGE, pps.currentYearSaleInVillage);
        values.put(KEY_SURVEY_DISTRIBUTOR_BILL_PRICE_RETAILER, pps.distributorBillingPriceToRetailer);
        values.put(KEY_SURVEY_DISTRIBUTOR_NET_LANDING_PRICE, pps.distributorNetLandingPrice);
        values.put(KEY_SURVEY_DISTRICT, pps.district);
        values.put(KEY_SURVEY_DIVISION_ID, pps.divisionId);
        values.put(KEY_SURVEY_DIVISION_NAME, pps.divisionName);
        values.put(KEY_SURVEY_FARMER_PRICE, pps.farmerPrice);
        values.put(KEY_SURVEY_FFM_ID, pps.ffmId);
        values.put(KEY_SURVEY_LAST_YEAR_SALE_IN_VILLAGE, pps.lastYearSaleInVillage);
        values.put(KEY_SURVEY_MRP, pps.mrp);
        values.put(KEY_SURVEY_NEXT_YEAR_ESTIMATED_SALE_IN_VILLAGE, pps.nextYearEstimatedSaleInVillage);
        values.put(KEY_SURVEY_PACK_SIZE, pps.packSize);
        values.put(KEY_SURVEY_SALE_QUANTITY, pps.saleQuantity);
        values.put(KEY_SURVEY_SEGMENT, pps.segment);
        values.put(KEY_SURVEY_TALUKA, pps.taluka);
        values.put(KEY_SURVEY_VILLAGE, pps.village);


        if (isAlreadyRecordExist(db, String.valueOf(pps.ffmId), TABLE_PRODUCT_PRICING_SURVEY, KEY_SURVEY_FFM_ID) == 0) {
            id = (int) db.insert(TABLE_PRODUCT_PRICING_SURVEY, null, values);
        } else {
            id = db.update(TABLE_PRODUCT_PRICING_SURVEY, values, KEY_SURVEY_FFM_ID + "=?", new String[]{String.valueOf(pps.ffmId)});
        }
//        } else {
//
//        }
        db.close(); // Closing database connection
        return id;
    }

    public List<ProductPricingSurvey> getAllProductPricingSurvey(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_PRICING_SURVEY + " WHERE " + KEY_SURVEY_USER_ID + " in (" + userId + ") ORDER BY " + KEY_SURVEY_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getProductPricingData(cursor);
    }

    public List<ProductPricingSurvey> getofflineProductPricingSurvey(String userId) {

        // Select All Quer
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_PRICING_SURVEY + " WHERE " + KEY_SURVEY_USER_ID + " in (" + userId + ") AND " + KEY_SURVEY_FFM_ID + " IS NULL " + " ORDER BY " + KEY_SURVEY_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getProductPricingData(cursor);
    }

    private List<ProductPricingSurvey> getProductPricingData(Cursor cursor) {
        List<ProductPricingSurvey> ppsList = new ArrayList<ProductPricingSurvey>();
        if (cursor.moveToFirst()) {
            do {
                ProductPricingSurvey pps = new ProductPricingSurvey();
                pps.userId = cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_USER_ID));
                pps.nextYearEstimatedSaleInVillage = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_NEXT_YEAR_ESTIMATED_SALE_IN_VILLAGE));
                pps.currentYearSaleInVillage = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_CURRENT_YEAR_SALE_IN_VILLAGE));
                pps.lastYearSaleInVillage = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_LAST_YEAR_SALE_IN_VILLAGE));
                pps.mrp = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_MRP));
                pps.farmerPrice = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_FARMER_PRICE));
                pps.distributorBillingPriceToRetailer = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_DISTRIBUTOR_BILL_PRICE_RETAILER));
                pps.companyBillingPrice = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_COMPANY_BILLING_PRICE));
                pps.distributorNetLandingPrice = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_DISTRIBUTOR_NET_LANDING_PRICE));
                pps.packSize = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_PACK_SIZE));
                pps.saleQuantity = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_SALE_QUANTITY));
                pps.segment = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_SEGMENT));
                pps.competitorProductName = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_COMPETITOR_PRODUCT_NAME));
                pps.competitorCompanyName = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_COMPETITOR_COMPANY_NAME));
                pps.cropName = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_CROP_NAME));
                pps.cropId = cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_CROP_ID));
                pps.divisionName = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_DIVISION_NAME));
                pps.divisionId = cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_DIVISION_ID));
                pps.village = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_VILLAGE));
                pps.regionId = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_REGION_ID));
                pps.taluka = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_TALUKA));
                pps.district = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_DISTRICT));
                pps.ffmId = cursor.getString(cursor.getColumnIndex(KEY_SURVEY_FFM_ID));
                pps.id = cursor.getInt(cursor.getColumnIndex(KEY_SURVEY_PRIMARY_ID));
                ppsList.add(pps);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ppsList;
    }

    public int insertCompetitorStrength(CompetitorStrength cs) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COMPETITOR_STRENGTH_USER_ID, cs.userId);
        values.put(KEY_COMPETITOR_STRENGTH_DISTRICT, cs.district);
        values.put(KEY_COMPETITOR_STRENGTH_FFM_ID, cs.ffmId);
        values.put(KEY_COMPETITOR_STRENGTH_TERRITORY, cs.territory);
        values.put(KEY_COMPETITOR_STRENGTH_COMPANY_NAME, cs.competitorCompanyName);
        values.put(KEY_COMPETITOR_STRENGTH_BUSINESS_COVERING_VILLAGES, cs.businessCoveringVillages);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_PRODUCTS_SOLD, cs.noOfProductsSold);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_FARMER_CLUBS, cs.noOfFarmerClubs);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_DEMO_PLOTS, cs.noOfDemoPlots);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_TEMPORARY_BOYS, cs.noOfTemporaryFasCounterBoys);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_PERMANENT_FA, cs.noOfPermanentFa);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_COMPANY_STAFF, cs.noOfCompanyStaff);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_FDS_CONDUCTED, cs.noOfFdsConducted);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_MFDS_CONDUCTED, cs.noOfMfdsConducted);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_NEW_PRODUCT_MINIKIT, cs.noOfNewProductMinikitTrailPlots);
        values.put(KEY_COMPETITOR_STRENGTH_NO_OF_WORKSHOPS_CONDUCTED, cs.noOfWorkshopsConducted);
        if (isAlreadyRecordExist(db, String.valueOf(cs.ffmId), TABLE_COMPETITOR_STRENGTH, KEY_COMP_CHANNEL_FFM_ID) == 0) {
            id = (int) db.insert(TABLE_COMPETITOR_STRENGTH, null, values);
        } else {
            id = db.update(TABLE_COMPETITOR_STRENGTH, values, KEY_COMPETITOR_STRENGTH_FFM_ID + "=?", new String[]{String.valueOf(cs.ffmId)});
        }
        db.close(); // Closing database connection
        return id;


    }

    public List<CompetitorStrength> getAllCompetitorStrengths(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_COMPETITOR_STRENGTH + " WHERE " + KEY_COMPETITOR_STRENGTH_USER_ID + " in (" + userId + ") ORDER BY " + KEY_COMPETITOR_STRENGTH_USER_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getCompStrengthData(cursor);
    }

    public List<CompetitorStrength> getOfflineCompetitorStrengths(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_COMPETITOR_STRENGTH + " WHERE " + KEY_COMPETITOR_STRENGTH_USER_ID + " in (" + userId + ") AND " + KEY_COMPETITOR_STRENGTH_FFM_ID + " IS NULL " + " ORDER BY " + KEY_COMPETITOR_STRENGTH_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getCompStrengthData(cursor);
    }

    private List<CompetitorStrength> getCompStrengthData(Cursor cursor) {
        List<CompetitorStrength> csList = new ArrayList<CompetitorStrength>();
        if (cursor.moveToFirst()) {
            do {
                CompetitorStrength cs = new CompetitorStrength();
                cs.id = cursor.getInt(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_PRIMARY_ID));
                cs.userId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_USER_ID)));
                cs.district = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_DISTRICT));
                cs.territory = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_TERRITORY));
                cs.competitorCompanyName = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_COMPANY_NAME));
                cs.businessCoveringVillages = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_BUSINESS_COVERING_VILLAGES));
                cs.noOfProductsSold = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_PRODUCTS_SOLD));
                cs.noOfFarmerClubs = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_FARMER_CLUBS));
                cs.noOfDemoPlots = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_DEMO_PLOTS));
                cs.noOfTemporaryFasCounterBoys = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_TEMPORARY_BOYS));
                cs.noOfPermanentFa = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_PERMANENT_FA));
                cs.noOfCompanyStaff = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_COMPANY_STAFF));
                cs.noOfFdsConducted = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_FDS_CONDUCTED));
                cs.noOfMfdsConducted = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_MFDS_CONDUCTED));
                cs.noOfNewProductMinikitTrailPlots = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_NEW_PRODUCT_MINIKIT));
                cs.noOfWorkshopsConducted = cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_STRENGTH_NO_OF_WORKSHOPS_CONDUCTED));
                // Adding contact to list
                csList.add(cs);
            } while (cursor.moveToNext());
        }
        return csList;
    }


    public int insertChannelPreference(ChannelPreference cp) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHANNEL_PREF_USER_ID, cp.userId);
        values.put(KEY_CHANNEL_PREF_DISTRIBUTOR_ID, cp.distributorId);
        values.put(KEY_CHANNEL_PREF_CROP_ID, cp.cropId);
        values.put(KEY_CHANNEL_PREF_CROP_NAME, cp.cropName);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_1, cp.companyName1);
        values.put(KEY_CHANNEL_PREF_COMP1_TURNOVER, cp.company1Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_2, cp.companyName2);
        values.put(KEY_CHANNEL_PREF_COMP2_TURNOVER, cp.company2Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_3, cp.companyName3);
        values.put(KEY_CHANNEL_PREF_COMP3_TURNOVER, cp.company3Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_4, cp.companyName4);
        values.put(KEY_CHANNEL_PREF_COMP4_TURNOVER, cp.company4Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_5, cp.companyName5);
        values.put(KEY_CHANNEL_PREF_COMP5_TURNOVER, cp.company5Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_6, cp.companyName6);
        values.put(KEY_CHANNEL_PREF_COMP6_TURNOVER, cp.company6Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_7, cp.companyName7);
        values.put(KEY_CHANNEL_PREF_COMP7_TURNOVER, cp.company7Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_8, cp.companyName8);
        values.put(KEY_CHANNEL_PREF_COMP8_TURNOVER, cp.company8Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_9, cp.companyName9);
        values.put(KEY_CHANNEL_PREF_COMP9_TURNOVER, cp.company9Turnover);
        values.put(KEY_CHANNEL_PREF_COMP_NAME_10, cp.companyName10);
        values.put(KEY_CHANNEL_PREF_COMP10_TURNOVER, cp.company10Turnover);

        if (isAlreadyRecordExist(db, String.valueOf(cp.ffmId), TABLE_CHANNEL_PREFERENCE, KEY_CHANNEL_PREF_FFM_ID) == 0) {
            id = (int) db.insert(TABLE_CHANNEL_PREFERENCE, null, values);
        } else {
            id = db.update(TABLE_CHANNEL_PREFERENCE, values, KEY_CHANNEL_PREF_FFM_ID + "=?", new String[]{String.valueOf(cp.ffmId)});
        }
        db.close(); // Closing database connection
        return id;


    }

    public List<ChannelPreference> getAllChannelPreferences(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_CHANNEL_PREFERENCE + " WHERE " + KEY_CHANNEL_PREF_USER_ID + " in (" + userId + ") ORDER BY " + KEY_CHANNEL_PREF_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getChannelPreferenceData(cursor);
    }

    public List<ChannelPreference> getOfflineChannelPreferences(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_CHANNEL_PREFERENCE + " WHERE " + KEY_CHANNEL_PREF_USER_ID + " in (" + userId + ") AND " + KEY_CHANNEL_PREF_FFM_ID + " IS NULL " + " ORDER BY " + KEY_CHANNEL_PREF_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getChannelPreferenceData(cursor);
    }

    private List<ChannelPreference> getChannelPreferenceData(Cursor cursor) {
        List<ChannelPreference> cpList = new ArrayList<ChannelPreference>();
        if (cursor.moveToFirst()) {
            do {
                ChannelPreference cp = new ChannelPreference();
                cp.id = cursor.getInt(cursor.getColumnIndex(KEY_CHANNEL_PREF_PRIMARY_ID));
                cp.userId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_USER_ID)));
                cp.distributorId = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_DISTRIBUTOR_ID));
                cp.cropId = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_CROP_ID));
                cp.cropName = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_CROP_NAME));
                cp.companyName1 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_1));
                cp.company1Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP1_TURNOVER));
                cp.companyName2 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_2));
                cp.company2Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP2_TURNOVER));
                cp.companyName3 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_3));
                cp.company3Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP3_TURNOVER));
                cp.companyName4 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_4));
                cp.company4Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP4_TURNOVER));
                cp.companyName5 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_5));
                cp.company5Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP5_TURNOVER));
                cp.companyName6 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_6));
                cp.company6Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP6_TURNOVER));
                cp.companyName7 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_7));
                cp.company7Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP7_TURNOVER));
                cp.companyName8 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_8));
                cp.company8Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP8_TURNOVER));
                cp.companyName9 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_9));
                cp.company9Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP9_TURNOVER));
                cp.companyName10 = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP_NAME_10));
                cp.company10Turnover = cursor.getString(cursor.getColumnIndex(KEY_CHANNEL_PREF_COMP10_TURNOVER));
                // Adding contact to list
                cpList.add(cp);
            } while (cursor.moveToNext());
        }
        return cpList;
    }


    public int insertCommodityPrice(CommodityPrice cp) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CP_USER_ID, cp.userId);
        values.put(KEY_CP_REGION_ID, cp.regionId);
        values.put(KEY_CP_FFM_ID, cp.ffmId);
        values.put(KEY_CP_DISTRICT, cp.district);
        values.put(KEY_CP_TALUKA, cp.taluka);
        values.put(KEY_CP_VILLAGE, cp.village);
        values.put(KEY_CP_DIVISION_ID, cp.divisionId);
        values.put(KEY_CP_DIVISION_NAME, cp.divisionName);
        values.put(KEY_CP_CROP_ID, cp.cropId);
        values.put(KEY_CP_CROP_NAME, cp.cropName);
        values.put(KEY_CP_SEGMENT, cp.segment);
        values.put(KEY_CP_APMC_MANDI_PRICE, cp.apmcMandiPrice);
        values.put(KEY_CP_COMMODITY_DEALER_PRICE, cp.commodityDealerCommissionAgentPrice);
        values.put(KEY_CP_PURCHASE_PRICE_BY_INDUSTRY, cp.purchasePriceByIndustry);
        values.put(KEY_CP_CREATED_DATETIME, cp.createdDatetime);
        values.put(KEY_CP_UPDATED_DATETIME, cp.updatedDatetime);
        if (isAlreadyRecordExist(db, String.valueOf(cp.ffmId), TABLE_COMMODITY_PRICE, KEY_CP_FFM_ID) == 0) {
            id = (int) db.insert(TABLE_COMMODITY_PRICE, null, values);
        } else {
            id = db.update(TABLE_COMMODITY_PRICE, values, KEY_CP_FFM_ID + "=?", new String[]{String.valueOf(cp.ffmId)});
        }
        db.close(); // Closing database connection
        return id;


    }

    public List<CommodityPrice> getAllCommodityPrices(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_COMMODITY_PRICE + " WHERE " + KEY_CP_USER_ID + " in (" + userId + ") ORDER BY " + KEY_CP_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getCommodityPriceData(cursor);
    }

    public List<CommodityPrice> getOfflineCommodityPrices(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_COMMODITY_PRICE + " WHERE " + KEY_CP_USER_ID + " in (" + userId + ") AND " + KEY_CP_FFM_ID + " IS NULL " + " ORDER BY " + KEY_CP_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getCommodityPriceData(cursor);
    }

    private List<CommodityPrice> getCommodityPriceData(Cursor cursor) {
        List<CommodityPrice> csList = new ArrayList<CommodityPrice>();
        if (cursor.moveToFirst()) {
            do {
                CommodityPrice cp = new CommodityPrice();
                cp.id = cursor.getInt(cursor.getColumnIndex(KEY_CP_PRIMARY_ID));
                cp.userId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CP_USER_ID)));
                cp.regionId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CP_REGION_ID)));
                cp.district = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CP_DISTRICT)));
                cp.taluka = cursor.getString(cursor.getColumnIndex(KEY_CP_TALUKA));
                cp.village = cursor.getString(cursor.getColumnIndex(KEY_CP_VILLAGE));
                cp.divisionId = cursor.getString(cursor.getColumnIndex(KEY_CP_DIVISION_ID));
                cp.divisionName = cursor.getString(cursor.getColumnIndex(KEY_CP_DIVISION_NAME));
                cp.cropId = cursor.getString(cursor.getColumnIndex(KEY_CP_CROP_ID));
                cp.cropName = cursor.getString(cursor.getColumnIndex(KEY_CP_CROP_NAME));
                cp.segment = cursor.getString(cursor.getColumnIndex(KEY_CP_SEGMENT));
                cp.apmcMandiPrice = cursor.getString(cursor.getColumnIndex(KEY_CP_APMC_MANDI_PRICE));
                cp.commodityDealerCommissionAgentPrice = cursor.getString(cursor.getColumnIndex(KEY_CP_COMMODITY_DEALER_PRICE));
                cp.purchasePriceByIndustry = cursor.getString(cursor.getColumnIndex(KEY_CP_PURCHASE_PRICE_BY_INDUSTRY));
                cp.createdDatetime = cursor.getString(cursor.getColumnIndex(KEY_CP_CREATED_DATETIME));
                cp.updatedDatetime = cursor.getString(cursor.getColumnIndex(KEY_CP_UPDATED_DATETIME));
                // Adding contact to list
                csList.add(cp);
            } while (cursor.moveToNext());
        }
        return csList;
    }

    public int insertCropShift(CropShift cs) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CROP_SHIFTS_USER_ID, cs.userId);
        values.put(KEY_CROP_SHIFTS_FFMID, cs.ffmId);
        values.put(KEY_CROP_SHIFTS_DISTRICT, cs.district);
        values.put(KEY_CROP_SHIFTS_TALUKA, cs.taluka);
        values.put(KEY_CROP_SHIFTS_VILLAGE, cs.village);
        values.put(KEY_CROP_SHIFTS_DIVISION_ID, cs.divisionId);
        values.put(KEY_CROP_SHIFTS_DIVISION_NAME, cs.divisionName);
        values.put(KEY_CROP_SHIFTS_CROP_ID, cs.cropId);
        values.put(KEY_CROP_SHIFTS_CROP_NAME, cs.cropName);
        values.put(KEY_CROP_SHIFTS_SEGMENT, cs.segment);
        values.put(KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA, cs.previousYearAreaInAcres);
        values.put(KEY_CROP_SHIFTS_CURRENT_YEAR_AREA, cs.currentYearAreaInAcres);
        values.put(KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE, cs.percentageIncreaseOrDecrease);
        values.put(KEY_CROP_SHIFTS_REASON_CROP_SHIFT, cs.reasonForCropShift);
        values.put(KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR, cs.previousYearSrr);
        values.put(KEY_CROP_SHIFTS_CURRENT_YEAR_SRR, cs.currentYearSrr);
        values.put(KEY_CROP_SHIFTS_NEXT_YEAR_ESTIMATED_SRR, cs.nextYearEstimatedSrr);
        values.put(KEY_CROP_SHIFTS_CREATED_DATE_TIME, cs.createdDatetime);
        values.put(KEY_CROP_SHIFTS_UPDATED_DATE_TIME, cs.updatedDatetime);
        if (isAlreadyRecordExist(db, String.valueOf(cs.ffmId), TABLE_CROP_SHIFTS, KEY_CROP_SHIFTS_FFMID) == 0) {
            id = (int) db.insert(TABLE_CROP_SHIFTS, null, values);
        } else {
            id = db.update(TABLE_CROP_SHIFTS, values, KEY_CROP_SHIFTS_FFMID + "=?", new String[]{String.valueOf(cs.ffmId)});
        }
        db.close(); // Closing database connection
        return id;


    }

    public List<CropShift> getAllCropShifts(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_CROP_SHIFTS + " WHERE " + KEY_CROP_SHIFTS_USER_ID + " in (" + userId + ") ORDER BY " + KEY_CROP_SHIFTS_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getCropShiftData(cursor);
    }

    public List<CropShift> getOfflineCropShifts(String userId) {
        String selectQuery = "SELECT  * FROM " + TABLE_CROP_SHIFTS + " WHERE " + KEY_CROP_SHIFTS_USER_ID + " in (" + userId + ") AND " + KEY_CROP_SHIFTS_FFMID + " IS NULL " + " ORDER BY " + KEY_CROP_SHIFTS_PRIMARY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getCropShiftData(cursor);
    }


    public List<Demandgeneation> getOfflineDemandgeneration(String userId) {
        String selectQuery = "SELECT DISTINCT *  FROM " + TABLE_DEMANDGENERATION + "  where "+KEY_DEMANDGENERATION_STATUS + "=0"+" and "+KEY_USER_ID+"="+userId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return getDemandgenrationdata(cursor);
    }
    private List<Demandgeneation> getDemandgenrationdata(Cursor cursor) {
        List<Demandgeneation> csList = new ArrayList<Demandgeneation>();
        if (cursor.moveToFirst()) {
            do {
                Demandgeneation cp = new Demandgeneation();
               cp.user_id= cursor.getString(1);
               cp.district_id=cursor.getString(2);
               cp.division_id=cursor.getString(3);
               cp.crop_id=cursor.getString(4);
               cp.activity_event=cursor.getString(5);
               cp.no_of_farmers=cursor.getString(8);
               cp.date_of_event=cursor.getString(7);
               cp.address=cursor.getString(6);

                cp.dg_id=cursor.getString(0);
                Log.d("hiiiiiiiiiiiii",cursor.getString(0));

                // Adding contact to list
                csList.add(cp);
            } while (cursor.moveToNext());
        }
        return csList;
    }
    public List<Getcoupans> getcoupans(String uid) {
        String selectQuery = "SELECT DISTINCT *  FROM " + TABLE_COUPANS +" WHERE " + KEY_FARMER_SERVERSTATUS+" = ? "+" AND "+KEY_FARMER_USER_ID +" = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] {"0",uid});
        return getCoupans(cursor);
    }

    private List<Getcoupans> getCoupans(Cursor cursor) {
        List<Getcoupans> csList = new ArrayList<Getcoupans>();
        if (cursor.moveToFirst()) {
            do {
                Getcoupans cp = new Getcoupans();
                cp.farmer_coupon_id= cursor.getString(1);
                cp.user_id=cursor.getString(2);


                cp.farmer_name= cursor.getString(9);

                cp.farmer_mobile=cursor.getString(11);
                cp.farmer_adhar_no=cursor.getString(12);

                cp.location=cursor.getString(15);
                cp.address=cursor.getString(16);

                cp.village=cursor.getString(20);
                cp.thaluka=cursor.getString(21);

                // Adding contact to list
                csList.add(cp);
            } while (cursor.moveToNext());
        }
        return csList;
    }

    public List<ModelforCoupansList> getcoupans2(String uid) {
        ArrayList<ModelforCoupansList> arrayList = new ArrayList<ModelforCoupansList>();

        String selectQuery = "SELECT * FROM " + TABLE_COUPANS + " WHERE " + KEY_FARMER_USER_ID +" = ?" +" ORDER BY "+KEY_FARMER_UPDATED_DATETIME+" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,  new String[] {  uid});
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(new ModelforCoupansList(cursor.getString(3),cursor.getString(9),cursor.getString(11),cursor.getString(16),cursor.getString(19),cursor.getString(20),cursor.getString(21),cursor.getString(7)));
            } while (cursor.moveToNext());
        }
        return  arrayList;
    }
    public ArrayList<String> getcoupanschk(String coupan) {
        String t="";
        ArrayList<String> check = new ArrayList<String>();
        check.add("");

       /* String query = "SELECT " + KEY_FARMER_UNIQUE_NO + " FROM " + TABLE_COUPANS + " WHERE " + KEY_FARMER_UNIQUE_NO + "   =   " + coupan;
        Cursor cursor = getWritableDatabase().rawQuery(query, null);*/


        String query = "SELECT "+KEY_FARMER_COUPAN_ID+","+KEY_FARMER_USER_ID+","+KEY_FARMER_COUPAN_TYPE+ " FROM " + TABLE_COUPANS + " WHERE " + KEY_FARMER_UNIQUE_NO +" = ? AND " + KEY_FARMER_SYNC_STATUS + " = ?";
        Log.e(DatabaseHandler.class.getName(), query);
        Cursor cursor = getWritableDatabase().rawQuery(query, new String[] { coupan ,"0"});



        if (cursor != null && cursor.moveToFirst()) {

            do {

                if(cursor.getString(1).equals("0")) {
                    t = cursor.getString(0);
                    check.clear();
                    check.add(t);
                    check.add(cursor.getString(2));
                }
                else {
                    t="Scanned";
                    check.clear();
                    check.add("Scanned");

                }
            } while (cursor.moveToNext());

        }
        else {
            check.clear();
            t="Copan not exists in database";
            check.add("Copan not exists in database");
        }
       /* return null;
        String selectQuery = "SELECT  *  FROM " + TABLE_COUPANS+" where "+KEY_FARMER_UNIQUE_NO+ " = " + coupan;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount()>0) {
              t=true;
        }
        else {
            t=false;
        }*/
       Log.d("scanner",check.toString());
        return  check;
    }




    private List<CropShift> getCropShiftData(Cursor cursor) {
        List<CropShift> csList = new ArrayList<CropShift>();
        if (cursor.moveToFirst()) {
            do {
                CropShift cp = new CropShift();
                cp.id = cursor.getInt(cursor.getColumnIndex(KEY_CROP_SHIFTS_PRIMARY_ID));
                cp.userId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_USER_ID)));
                cp.district = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_DISTRICT)));
                cp.taluka = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_TALUKA));
                cp.village = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_VILLAGE));
                cp.divisionId = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_DIVISION_ID));
                cp.divisionName = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_DIVISION_NAME));
                cp.cropId = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_CROP_ID));
                cp.cropName = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_CROP_NAME));
                cp.segment = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_SEGMENT));
                cp.previousYearAreaInAcres = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA));
                cp.currentYearAreaInAcres = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_CURRENT_YEAR_AREA));
                cp.percentageIncreaseOrDecrease = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE));
                cp.reasonForCropShift = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_REASON_CROP_SHIFT));
                cp.previousYearSrr = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR));
                cp.currentYearSrr = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_CURRENT_YEAR_SRR));
                cp.nextYearEstimatedSrr = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_NEXT_YEAR_ESTIMATED_SRR));
                cp.createdDatetime = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_CREATED_DATE_TIME));
                cp.updatedDatetime = cursor.getString(cursor.getColumnIndex(KEY_CROP_SHIFTS_UPDATED_DATE_TIME));
                // Adding contact to list
                csList.add(cp);
            } while (cursor.moveToNext());
        }
        return csList;
    }


    public List<DetailObject> getMpItemDetailsById(int id) {
        List<DetailObject> detailObjects = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MARKET_POTENTIAL + " WHERE " + KEY_MARKET_POTENTIAL_PRIMARY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_REGION_ID, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_DISTRICT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_TALUKA, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_VILLAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_KHARIF_CROP_ACREAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_RABI_CROP_ACREAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_SUMMER_CROP_ACREAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_TOTAL_POTENTIAL_ACREAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_SEED_USAGE_QUANTITY_ACRE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_TOTAL_MARKET_POTENTIAL_VOLUME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_NSL_SALE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_TOP_COMPANY_1_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_COMPANY_1_QUANTITY, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_TOP_COMPANY_2_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_COMPANY_2_QUANTITY, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_TOP_COMPANY_3_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_COMPANY_3_QUANTITY, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_TOP_COMPANY_4_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_COMPANY_4_QUANTITY, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_TOP_COMPANY_5_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_MARKET_POTENTIAL_COMPANY_5_QUANTITY, cursor));
        }
        return detailObjects;
    }

    public List<DetailObject> getCcItemDetailsById(int id) {
        List<DetailObject> detailObjects = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPETITOR_CHANNEL + " WHERE " + KEY_COMP_CHANNEL_PRIMARY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_REGION_ID, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_DISTRICT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_TERRITORY, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_TOTAL_NO_OF_VILLAGES, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_TOTAL_NO_OF_DISTRIBUTORS, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_TOTAL_NO_OF_RETAILERS, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_NSL_VILLAGES, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_NSL_DISTRIBUTORS, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_NSL_RETAILERS, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_1, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_1, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_RETAILERS_1, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_MARKET_PENETRATION_1, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_2, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_2, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_RETAILERS_2, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_MARKET_PENETRATION_2, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_3, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_3, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_RETAILERS_3, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_MARKET_PENETRATION_3, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_4, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_4, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_RETAILERS_4, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_MARKET_PENETRATION_4, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_COMP_COMPANY_NAME_5, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_DISTRIBUTORS_5, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_NO_OF_RETAILERS_5, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMP_CHANNEL_MARKET_PENETRATION_5, cursor));
        }
        return detailObjects;
    }

    public List<DetailObject> getPpsItemDetailsById(int id) {
        List<DetailObject> detailObjects = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_PRICING_SURVEY + " WHERE " + KEY_SURVEY_PRIMARY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_REGION_ID, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_DISTRICT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_TALUKA, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_VILLAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_DIVISION_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_CROP_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_COMPETITOR_COMPANY_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_COMPETITOR_PRODUCT_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_SEGMENT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_SALE_QUANTITY, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_PACK_SIZE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_DISTRIBUTOR_NET_LANDING_PRICE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_COMPANY_BILLING_PRICE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_DISTRIBUTOR_BILL_PRICE_RETAILER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_FARMER_PRICE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_MRP, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_LAST_YEAR_SALE_IN_VILLAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_CURRENT_YEAR_SALE_IN_VILLAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_SURVEY_NEXT_YEAR_ESTIMATED_SALE_IN_VILLAGE, cursor));
        }
        return detailObjects;
    }

    public List<DetailObject> getCsItemDetailsById(int id) {
        List<DetailObject> detailObjects = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPETITOR_STRENGTH + " WHERE " + KEY_COMPETITOR_STRENGTH_PRIMARY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_DISTRICT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_TERRITORY, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_COMPANY_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_BUSINESS_COVERING_VILLAGES, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_PRODUCTS_SOLD, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_FARMER_CLUBS, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_DEMO_PLOTS, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_TEMPORARY_BOYS, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_PERMANENT_FA, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_COMPANY_STAFF, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_FDS_CONDUCTED, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_MFDS_CONDUCTED, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_NEW_PRODUCT_MINIKIT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_COMPETITOR_STRENGTH_NO_OF_WORKSHOPS_CONDUCTED, cursor));
        }
        return detailObjects;
    }

    public List<DetailObject> getChannelPreferenceItemDetailsById(int id) {
        List<DetailObject> detailObjects = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CHANNEL_PREFERENCE + " WHERE " + KEY_CHANNEL_PREF_PRIMARY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_CROP_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_1, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP1_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_2, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP2_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_3, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP3_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_4, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP4_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_5, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP5_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_6, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP6_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_7, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP7_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_8, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP8_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_9, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP9_TURNOVER, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP_NAME_10, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CHANNEL_PREF_COMP10_TURNOVER, cursor));
        }
        return detailObjects;
    }

    public List<DetailObject> getCpItemDetailsById(int id) {
        List<DetailObject> detailObjects = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COMMODITY_PRICE + " WHERE " + KEY_CP_PRIMARY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            detailObjects.add(getDetailObjectByColumn(KEY_CP_REGION_ID, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_DISTRICT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_TALUKA, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_VILLAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_DIVISION_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_CROP_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_SEGMENT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_APMC_MANDI_PRICE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_COMMODITY_DEALER_PRICE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CP_PURCHASE_PRICE_BY_INDUSTRY, cursor));
        }
        return detailObjects;
    }

    public List<DetailObject> getCropShiftItemDetailsById(int id) {
        List<DetailObject> detailObjects = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CROP_SHIFTS + " WHERE " + KEY_CROP_SHIFTS_PRIMARY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_DISTRICT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_TALUKA, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_VILLAGE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_DIVISION_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_CROP_NAME, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_SEGMENT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_PREVIOUS_YEAR_AREA, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_CURRENT_YEAR_AREA, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_PERCENTAGE_INCREASE_DECREASE, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_REASON_CROP_SHIFT, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_PREVIOUS_YEAR_SRR, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_CURRENT_YEAR_SRR, cursor));
            detailObjects.add(getDetailObjectByColumn(KEY_CROP_SHIFTS_NEXT_YEAR_ESTIMATED_SRR, cursor));
        }
        return detailObjects;
    }

    private DetailObject getDetailObjectByColumn(String columnName, Cursor cursor) {
        DetailObject detailObject = new DetailObject();
        String value = cursor.getString(cursor.getColumnIndex(columnName));
        if (columnName.equalsIgnoreCase(KEY_MARKET_POTENTIAL_REGION_ID)) {
            value = getRegionName(value);
        } else if (columnName.equalsIgnoreCase(KEY_MARKET_POTENTIAL_DISTRICT)) {
            value = getDistrictName(value);
        }

        detailObject.label = columnName;
        detailObject.value = value;
        return detailObject;
    }

    private String getDistrictName(String value) {
        String query = "SELECT " + KEY_DISTRICT_DISTRICT_NAME + " FROM " + TABLE_DISTRICT + " WHERE " + KEY_DISTRICT_DISTRICT_ID + " = " + value;
        Cursor cursor = getWritableDatabase().rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

    private String getRegionName(String value) {
        String query = "SELECT " + KEY_REGION_NAME + " FROM " + TABLE_REGION + " WHERE " + KEY_REGION_ID + " = " + value;
        Cursor cursor = getWritableDatabase().rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

    private List<MarketPotential> getMarketPotentialData(Cursor cursor) {
        List<MarketPotential> mpList = new ArrayList<MarketPotential>();
        if (cursor.moveToFirst()) {
            do {
                MarketPotential mp = new MarketPotential();
                mp.id = cursor.getInt(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_PRIMARY_ID));
                mp.ffmId = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_FFM_ID));
                mp.userId = cursor.getInt(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_USER_ID));
                mp.regionId = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_REGION_ID));
                mp.district = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_DISTRICT));
                mp.taluka = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_TALUKA));
                mp.village = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_VILLAGE));
                mp.divisionId = cursor.getInt(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_DIVISION_ID));
                mp.cropId = cursor.getInt(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_CROP_ID));
                mp.kharifCropAcreage = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_KHARIF_CROP_ACREAGE));
                mp.rabiCropAcreage = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_RABI_CROP_ACREAGE));
                mp.summerCropAcreage = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_SUMMER_CROP_ACREAGE));
                mp.totalPotentialAcreage = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_TOTAL_POTENTIAL_ACREAGE));
                mp.seedUsageQuanity = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_SEED_USAGE_QUANTITY_ACRE));
                mp.totalMarketPotentialVolume = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_TOTAL_MARKET_POTENTIAL_VOLUME));
                mp.nslSale = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_NSL_SALE));
                mp.topCompanyName1 = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_TOP_COMPANY_1_NAME));
                mp.company1Qty = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_COMPANY_1_QUANTITY));
                mp.topCompanyName2 = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_TOP_COMPANY_2_NAME));
                mp.company2Qty = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_COMPANY_2_QUANTITY));
                mp.topCompanyName3 = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_TOP_COMPANY_3_NAME));
                mp.company3Qty = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_COMPANY_3_QUANTITY));
                mp.topCompanyName4 = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_TOP_COMPANY_4_NAME));
                mp.company4Qty = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_COMPANY_4_QUANTITY));
                mp.topCompanyName5 = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_TOP_COMPANY_5_NAME));
                mp.company5Qty = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_COMPANY_5_QUANTITY));
                mp.cropName = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_CROP_NAME));
                mp.divisionName = cursor.getString(cursor.getColumnIndex(KEY_MARKET_POTENTIAL_DIVISION_NAME));
                // Adding contact to list
                mpList.add(mp);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return mpList;
    }

    public void updateTable(String ffmKey, String tableName, String primaryKey, int primaryId, String ffmId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ffmKey, ffmId);
        // Inserting Row
        db.update(tableName, contentValues, primaryKey + "=?", new String[]{String.valueOf(primaryId)});
        db.close(); // Closing database connection
    }

    /*public void updateCc(int primaryId, String ffmId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMP_CHANNEL_FFM_ID, ffmId);
        // Inserting Row
        db.update(TABLE_COMPETITOR_CHANNEL, contentValues, KEY_COMP_CHANNEL_PRIMARY_ID + "=?", new String[]{String.valueOf(primaryId)});
        db.close(); // Closing database connection
    }*/


    public int insertDistrict(District district) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
//        if (!isTINnoExist(retailer.get_ret_tin_no())) {
        ContentValues values = new ContentValues();
//        values.put(KEY_SOA_PRIMARY_ID, soa.primaryId);
        values.put(KEY_DISTRICT_DISTRICT_ID, district.districtId);
        values.put(KEY_DISTRICT_REGION_ID, district.regionId);
        values.put(KEY_DISTRICT_DISTRICT_NAME, district.districtName);
        values.put(KEY_DISTRICT_STATUS, district.status);
        values.put(KEY_DISTRICT_CREATED_BY, district.createdBy);
        values.put(KEY_DISTRICT_MODIFIED_BY, district.modifiedBy);
        values.put(KEY_DISTRICT_C_DATE_TIME, district.createdDateTime);
        values.put(KEY_DISTRICT_U_DATE_TIME, district.modifieDateTime);

        if (isAlreadyRecordExist(db, String.valueOf(district.districtId), TABLE_DISTRICT, KEY_DISTRICT_DISTRICT_ID) == 0) {
            id = (int) db.insert(TABLE_DISTRICT, null, values);
        } else {
            id = db.update(TABLE_DISTRICT, values, KEY_DISTRICT_DISTRICT_ID + "=?", new String[]{String.valueOf(district.districtId)});
        }
//        } else {
//
//        }
        db.close(); // Closing database connection
        return id;
    }

    public List<District> getDistrictsByRegionID(String key, String regionId) {
        List<District> districts = new ArrayList<>();
        District district = null;
        String selectQuery = "SELECT * FROM " + TABLE_DISTRICT + " WHERE " + KEY_DISTRICT_DISTRICT_NAME + " " + KEY_DISTRICT_REGION_ID + " = " + regionId + " AND " + KEY_DISTRICT_STATUS + " = 1 ORDER BY " + KEY_DISTRICT_DISTRICT_ID + " DESC ";
        if (key != null && key.length() > 0) {
            selectQuery = "SELECT * FROM " + TABLE_DISTRICT + " WHERE " + KEY_DISTRICT_DISTRICT_NAME + " " + "like " + key + " AND " + KEY_DISTRICT_REGION_ID + " = " + regionId + " AND " + KEY_DISTRICT_STATUS + " = 1 ORDER BY " + KEY_DISTRICT_DISTRICT_ID + " DESC ";
        }


        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                district = new District();
                district.districtId = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_DISTRICT_ID));
                district.regionId = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_REGION_ID));
                district.districtName = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_DISTRICT_NAME));
                district.status = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_STATUS));
                district.createdBy = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_CREATED_BY));
                district.modifiedBy = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_MODIFIED_BY));
                district.createdDateTime = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_C_DATE_TIME));
                district.modifieDateTime = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_U_DATE_TIME));
                districts.add(district);
            } while (cursor.moveToNext());
        }

        return districts;
    }

    /*public int insertPlannerApproval(PlannerApproval pa) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
//        if (!isTINnoExist(retailer.get_ret_tin_no())) {
        ContentValues values = new ContentValues();
//        values.put(KEY_SOA_PRIMARY_ID, soa.primaryId);
        values.put(KEY_PA_PLANNER_APPROVAL_ID, pa.plannerApprovalId);
        values.put(KEY_PA_EMP_VISIT_ID, pa.empVisitId);
        values.put(KEY_PA_USER_ID, pa.userId);
        values.put(KEY_PA_ASSIGNED_USER_ID, pa.assignedUserId);
        values.put(KEY_PA_PLANNER_STATUS, pa.plannerStatus);
        values.put(KEY_PA_CREATED_BY, pa.createdBy);
        values.put(KEY_PA_MODIFIED_BY, pa.modifiedBy);
        values.put(KEY_PA_C_DATE_TIME, pa.cDateTime);
        values.put(KEY_PA_U_DATE_TIME, pa.mDateTime);
        values.put(KEY_PA_SYNC_STATUS, pa.syncStatus);

        if (isAlreadyRecordExist(db, String.valueOf(pa.plannerApprovalId), TABLE_PLANNER_APPROVAL, KEY_PA_PLANNER_APPROVAL_ID) == 0) {
            id = (int) db.insert(TABLE_PLANNER_APPROVAL, null, values);
        } else {
            id = db.update(TABLE_PLANNER_APPROVAL, values, KEY_PA_PLANNER_APPROVAL_ID + "=?", new String[]{String.valueOf(pa.plannerApprovalId)});
        }
//        } else {
//
//        }
        db.close(); // Closing database connection
        return id;
    }*/

    public int insertServiceOrderHistory(ServiceOrderHistory soh) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
//        if (!isTINnoExist(retailer.get_ret_tin_no())) {
        ContentValues values = new ContentValues();
//        values.put(KEY_SOH_PRIMARY_ID, soh.primaryId);
        values.put(KEY_SOH_ORDER_HISTORY_ID, soh.orderHistoryId);
        values.put(KEY_SOH_ORDER_ID, soh.orderId);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID, soh.orderDetailsId);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID, soh.cropId);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID, soh.schemeId);
        values.put(KEY_TABLE_SERVICEORDER_SLAB_ID, soh.slabId);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID, soh.productId);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY, soh.orderQuantity);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY, soh.quantity);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE, soh.orderPrice);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT, soh.advanceAmount);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_STATUS, soh.status);
        values.put(KEY_TABLE_SERVICEORDER_DETAIL_CREATED_DATETIME, soh.cDateTime);
        values.put(KEY_SOH_MODIFIED_BY, soh.modifiedBy);
        values.put(KEY_SOH_CREATED_BY, soh.createdBy);
        values.put(KEY_SOH_ORDER_APPROVAL_ID, soh.orderApprovalId);

        if (isAlreadyRecordExist(db, String.valueOf(soh.orderHistoryId), TABLE_SERVICE_ORDER_HISTORY, KEY_SOH_ORDER_HISTORY_ID) == 0) {
            id = (int) db.insert(TABLE_SERVICE_ORDER_HISTORY, null, values);
        } else {
            id = db.update(TABLE_SERVICE_ORDER_HISTORY, values, KEY_SOH_ORDER_HISTORY_ID + "=?", new String[]{String.valueOf(soh.orderHistoryId)});
        }
//        } else {
//
//        }
        db.close(); // Closing database connection
        return id;
    }

    public List<ServiceOrderHistory> getSOHByOrderDetailId(String orderDetailId) {
        List<ServiceOrderHistory> sohList = new ArrayList<ServiceOrderHistory>();
        ServiceOrderHistory soh = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SERVICE_ORDER_HISTORY + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID + " = " + orderDetailId + " ORDER BY " + KEY_SOH_PRIMARY_ID + " DESC ";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                soh = new ServiceOrderHistory();
                soh.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_SOH_PRIMARY_ID));
                soh.orderHistoryId = cursor.getString(cursor.getColumnIndex(KEY_SOH_ORDER_HISTORY_ID));
                soh.orderId = cursor.getString(cursor.getColumnIndex(KEY_SOH_ORDER_ID));
                soh.orderDetailsId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID));
                soh.cropId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID));
                soh.schemeId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID));
                soh.slabId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_SLAB_ID));
                soh.productId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID));
                soh.orderQuantity = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY));
                soh.quantity = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY));
                soh.orderPrice = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE));
                soh.advanceAmount = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT));
                soh.status = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_STATUS));
                soh.createdBy = cursor.getString(cursor.getColumnIndex(KEY_SOH_CREATED_BY));
                soh.cDateTime = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_CREATED_DATETIME));
                soh.modifiedBy = cursor.getString(cursor.getColumnIndex(KEY_SOH_MODIFIED_BY));
                soh.orderApprovalId = cursor.getString(cursor.getColumnIndex(KEY_SOH_ORDER_APPROVAL_ID));
                sohList.add(soh);
            } while (cursor.moveToNext());
        }

        // return contact list
        return sohList;
    }

    public ServiceOrderApproval getSOAByUserId(String userId, String orderId) {
//        List<ServiceOrderApproval> soaList = new ArrayList<ServiceOrderApproval>();
        ServiceOrderApproval soa = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SERVICE_ORDER_APPROVAL + " WHERE " + KEY_SOA_USER_ID + " = " + userId + " AND " + KEY_SOA_ORDER_ID + " = " + orderId + " ORDER BY " + KEY_SOA_SERVICE_ORDER_APPROVAL_ID + " DESC ";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                soa = new ServiceOrderApproval();
                soa.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_SOA_PRIMARY_ID));
                soa.orderApprovalId = cursor.getString(cursor.getColumnIndex(KEY_SOA_SERVICE_ORDER_APPROVAL_ID));
                soa.orderId = cursor.getString(cursor.getColumnIndex(KEY_SOA_ORDER_ID));
                soa.userId = cursor.getString(cursor.getColumnIndex(KEY_SOA_USER_ID));
                soa.assignedUserId = cursor.getString(cursor.getColumnIndex(KEY_SOA_ASSIGNED_USER_ID));
                soa.orderStatus = cursor.getString(cursor.getColumnIndex(KEY_SOA_ORDER_STATUS));
                soa.createdBy = cursor.getString(cursor.getColumnIndex(KEY_SOA_CREATED_BY));
                soa.modifiedBy = cursor.getString(cursor.getColumnIndex(KEY_SOA_MODIFIED_BY));
                soa.cDateTime = cursor.getString(cursor.getColumnIndex(KEY_SOA_C_DATE_TIME));
                soa.uDateTime = cursor.getString(cursor.getColumnIndex(KEY_SOA_U_DATE_TIME));
                soa.syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SOA_SYNC_STATUS));
                soa.pendingBy = cursor.getString(cursor.getColumnIndex(KEY_SOA_PENDING_BY));
            } while (cursor.moveToNext());
        }

        // return contact list
        return soa;

    }

    public List<ServiceOrderApproval> getOrderApprovalsByOrderId(String orderId) {
        List<ServiceOrderApproval> soaList = new ArrayList<ServiceOrderApproval>();
        ServiceOrderApproval soa = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SERVICE_ORDER_APPROVAL + " WHERE " + KEY_SOA_ORDER_ID + " = " + orderId + " ORDER BY " + KEY_SOA_SERVICE_ORDER_APPROVAL_ID + " ASC ";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                soa = new ServiceOrderApproval();
                soa.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_SOA_PRIMARY_ID));
                soa.orderApprovalId = cursor.getString(cursor.getColumnIndex(KEY_SOA_SERVICE_ORDER_APPROVAL_ID));
                soa.orderId = cursor.getString(cursor.getColumnIndex(KEY_SOA_ORDER_ID));
                soa.userId = cursor.getString(cursor.getColumnIndex(KEY_SOA_USER_ID));
                soa.assignedUserId = cursor.getString(cursor.getColumnIndex(KEY_SOA_ASSIGNED_USER_ID));
                soa.orderStatus = cursor.getString(cursor.getColumnIndex(KEY_SOA_ORDER_STATUS));
                soa.createdBy = cursor.getString(cursor.getColumnIndex(KEY_SOA_CREATED_BY));
                soa.modifiedBy = cursor.getString(cursor.getColumnIndex(KEY_SOA_MODIFIED_BY));
                soa.cDateTime = cursor.getString(cursor.getColumnIndex(KEY_SOA_C_DATE_TIME));
                soa.uDateTime = cursor.getString(cursor.getColumnIndex(KEY_SOA_U_DATE_TIME));
                soa.syncStatus = cursor.getString(cursor.getColumnIndex(KEY_SOA_SYNC_STATUS));
                soa.pendingBy = cursor.getString(cursor.getColumnIndex(KEY_SOA_PENDING_BY));
                soaList.add(soa);
            } while (cursor.moveToNext());
        }

        // return contact list
        return soaList;

    }

    /*public PlannerApproval getPAByUserId(String userId, String empVisitId) {
        PlannerApproval soa = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PLANNER_APPROVAL + " WHERE " + KEY_PA_USER_ID + " = " + userId + " AND " + KEY_PA_EMP_VISIT_ID + " = " + empVisitId + " ORDER BY " + KEY_PA_PLANNER_APPROVAL_ID + " DESC ";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                soa = new PlannerApproval();
                soa.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_PA_PRIMARY_ID));
                soa.plannerApprovalId = cursor.getString(cursor.getColumnIndex(KEY_PA_PLANNER_APPROVAL_ID));
                soa.empVisitId = cursor.getString(cursor.getColumnIndex(KEY_PA_EMP_VISIT_ID));
                soa.userId = cursor.getString(cursor.getColumnIndex(KEY_PA_USER_ID));
                soa.assignedUserId = cursor.getString(cursor.getColumnIndex(KEY_PA_ASSIGNED_USER_ID));
                soa.plannerStatus = cursor.getString(cursor.getColumnIndex(KEY_PA_PLANNER_STATUS));
                soa.createdBy = cursor.getString(cursor.getColumnIndex(KEY_PA_CREATED_BY));
                soa.modifiedBy = cursor.getString(cursor.getColumnIndex(KEY_PA_MODIFIED_BY));
                soa.cDateTime = cursor.getString(cursor.getColumnIndex(KEY_PA_C_DATE_TIME));
                soa.mDateTime = cursor.getString(cursor.getColumnIndex(KEY_PA_U_DATE_TIME));
                soa.syncStatus = cursor.getString(cursor.getColumnIndex(KEY_PA_SYNC_STATUS));
            } while (cursor.moveToNext());
        }

        // return contact list
        return soa;
    }*/


    public int addFarmer(FarmerPojo farmerPojo) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
//        if (!isTINnoExist(retailer.get_ret_tin_no())) {
        ContentValues values = new ContentValues();
        values.put(KEY_TABLE_FARMER_ID, farmerPojo.getFarmerId());
        values.put(KEY_TABLE_FARMER_NAME, farmerPojo.getFarmerName());
        values.put(KEY_TABLE_FARMER_PHONE, farmerPojo.getMobile());
        values.put(KEY_TABLE_FARMER_STATE, farmerPojo.getState());
        values.put(KEY_TABLE_FARMER_DISTRICT, farmerPojo.getLocationDistrict());
        values.put(KEY_TABLE_FARMER_TALUKA, farmerPojo.getLocationTaluka());
        values.put(KEY_TABLE_FARMER_VILLAGE, farmerPojo.getLocationVillage());
        values.put(KEY_TABLE_FARMER_REGION_ID, farmerPojo.getRegionId());
        values.put(KEY_TABLE_FARMER_TOTAL_LAND_HOLDING, farmerPojo.getTotalLandHolding());
        values.put(KEY_TABLE_FARMER_STATUS, farmerPojo.getStatus());
        values.put(KEY_TABLE_FARMER_FFM_ID, farmerPojo.getFfmId());

        if (isAlreadyRecordExist(db, String.valueOf(farmerPojo.getPrimaryId()), TABLE_FARMERS, KEY_TABLE_FARMER_PRIMARY_ID) == 0) {
            id = (int) db.insert(TABLE_FARMERS, null, values);
        } else {
            id = db.update(TABLE_FARMERS, values, KEY_TABLE_FARMER_PRIMARY_ID + "=?", new String[]{String.valueOf(farmerPojo.getPrimaryId())});
        }


//        } else {
//
//        }
        db.close(); // Closing database connection
        return id;
    }

    public List<Retailer> getAllnullRetailer(String custid) {
        List<Retailer> customerList = new ArrayList<Retailer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '" + custid + "' AND " + KEY_TABLE_RETAILER_FFMID + " IS NULL";
        Log.e("query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Retailer customers = new Retailer();
                customers.setID(Integer.parseInt(cursor.getString(0)));
                customers.set_ret_masterid(cursor.getString(1));
                customers.set_ret_name(cursor.getString(2));
                customers.set_ret_tin_no(cursor.getString(3));
                customers.set_ret_address(cursor.getString(4));
                customers.set_ret_phone(cursor.getString(5));
                customers.set_ret_mobile(cursor.getString(6));
                customers.set_email(cursor.getString(7));
                customers.set_ret_dist_id(cursor.getString(8));
                customers.set_ret_dist_sap_code(cursor.getString(9));
                customers.set_ret_status(cursor.getString(10));
                customers.set_ret_cdatetime(cursor.getString(11));
                customers.set_ret_udatetime(cursor.getString(12));
                customers.set_ffmid(cursor.getString(13));
                // Adding contact to list
                customerList.add(customers);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return customerList;
    }

    public List<Retailer> getAllRetailers() {
        List<Retailer> dairy = new ArrayList<Retailer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETAILER;
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Retailer retailer = new Retailer();
                retailer.setID(cursor.getInt(0));
                retailer.set_ret_masterid(cursor.getString(1));
                retailer.set_ret_name(cursor.getString(2));
                retailer.set_ret_tin_no(cursor.getString(3));
                retailer.set_ret_address(cursor.getString(4));
                retailer.set_ret_district(cursor.getString(5));
                retailer.set_ret_taluka(cursor.getString(6));
                retailer.set_ret_village(cursor.getString(7));
                retailer.set_ret_region_id(cursor.getString(8));
                retailer.set_ret_gstin_no(cursor.getString(9));
                retailer.set_ret_phone(cursor.getString(10));
                retailer.set_ret_mobile(cursor.getString(11));
                retailer.set_email(cursor.getString(12));
                retailer.set_ret_dist_id(cursor.getString(13));
                retailer.set_ret_dist_sap_code(cursor.getString(14));
                retailer.set_ret_status(cursor.getString(15));
                retailer.set_ret_cdatetime(cursor.getString(16));
                retailer.set_ret_udatetime(cursor.getString(17));
                retailer.set_ffmid(cursor.getString(18));
                dairy.add(retailer);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;

    }

    public List<Retailer> getAllRetailer(String id) {
        List<Retailer> customerList = new ArrayList<Retailer>();
        // Select All Query
        /// String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '"+id+"'" ;
        String selectQuery = " SELECT retailer_name,retailer_tin_no,address,phone,mobile,email_id,ffmid FROM " + TABLE_RETAILER + " left join distributor_retailer ds on ds.retailer_id = retailers.retailer_id where ds.distributor_id=" + id;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Retailer customers = new Retailer();
                // customers.setID(Integer.parseInt(cursor.getString(0)));
                //   customers.set_ret_masterid(cursor.getString(1));
                customers.set_ret_name(cursor.getString(0));
                customers.set_ret_address(cursor.getString(2));
                customers.set_ret_tin_no(cursor.getString(1));
                customers.set_ret_phone(cursor.getString(3));
                customers.set_ret_mobile(cursor.getString(4));
                customers.set_email(cursor.getString(5));
                //  customers.set_ret_dist_id(cursor.getString(8));
                //  customers.set_ret_dist_sap_code(cursor.getString(9));
                //  customers.set_ret_status(cursor.getString(10));
                //  customers.set_ret_cdatetime(cursor.getString(11));
                //  customers.set_ret_udatetime(cursor.getString(12));
                customers.set_ffmid(cursor.getString(6));
                // Adding contact to list
               /* Log.e("Result","ID"+cursor.getString(0)+"ret_master_id"+cursor.getString(1)+"retname"+cursor.getString(2)
                        +"rettinno"+cursor.getString(3)+"retaddress"+cursor.getString(4)


                        +"retphone"+cursor.getString(5)
                        +"retmobile"+cursor.getString(6)
                        +"retemail"+cursor.getString(7)
                        +"retdistid"+cursor.getString(8)
                        +"retsapcode"+cursor.getString(9)
                        +"retstatus"+cursor.getString(10)
                        +"retcdate"+cursor.getString(11)
                        +"retudate"+cursor.getString(12));*/
                customerList.add(customers);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return customerList;
    }

    public boolean isTINnoExist(String tin_no) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_TIN_NO + " = '" + tin_no + "'";
        Log.e("query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        }

        // return DIVISIONS list
        return false;
    }

    public int getSqlPrimaryKeyByFFMID(String ffm_id) {

        // Select All Query
        String selectQuery = "SELECT  mobile_id FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_FFMID + " = '" + ffm_id + "'";
        Log.e("query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        // return DIVISIONS list
        return 0;
    }


    public List<RetailersNamePojo> getAllRetailerByDistributorId(String customer_id, String userId, String company_id, String distributorId) {
        List<RetailersNamePojo> customerList = new ArrayList<RetailersNamePojo>();
        // Select All Query
        /// String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '"+id+"'" ;
        //  String selectQuery  = " SELECT retailer_name,retailer_tin_no,address,phone,mobile,email_id,ffmid FROM "+TABLE_RETAILER  +" left join distributor_retailer ds on ds.retailer_id = retailers.retailer_id where ds.distributor_id="+id;
        String selectQuery = "SELECT retailers.retailer_id,retailer_name,retailer_tin_no FROM distributor_retailer left  join retailers on retailers.retailer_id=distributor_retailer.retailer_id  where  distributor_retailer.distributor_id=" + distributorId;


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RetailersNamePojo retailersNamePojo = new RetailersNamePojo();

                retailersNamePojo.retailerId = cursor.getString(0);
                retailersNamePojo.retailerName = cursor.getString(1);
                retailersNamePojo.retailerTinNo = cursor.getString(2);
                retailersNamePojo.stockMovementFirstListPojo = getOfflineStockPlacementRetailerList(userId, company_id, customer_id);


                customerList.add(retailersNamePojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return customerList;
    }

    public List<MappedRetailerPojo> getMappedRetailerWithDistributorAndProduct(String stock_movement_id, String product_id, String crop_id) {
        List<MappedRetailerPojo> mappedRetailerPojoList = new ArrayList<MappedRetailerPojo>();
        // Select All Query
        String selectQuery = "SELECT retailer_name, retailer_tin_no, sum(stock_placed)stock_placed, sum(current_stock)current_stock, stock_movement_retailer_id,stock_movement_retailer_details.retailer_id FROM stock_movement_retailer_details LEFT JOIN retailers ON retailers.retailer_id=stock_movement_retailer_details.retailer_id WHERE stock_movement_id = " + stock_movement_id + " and product_id = " + product_id + " GROUP BY stock_movement_retailer_details.retailer_id";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MappedRetailerPojo mappedRetailerPojo = new MappedRetailerPojo();

                String currentStock = getCurrentStockSMRD(Integer.valueOf(product_id), Integer.parseInt(crop_id), Integer.valueOf(stock_movement_id), cursor.getString(5));
                mappedRetailerPojo.retailerName = cursor.getString(0);
                mappedRetailerPojo.retailerTinNo = cursor.getString(1);
                mappedRetailerPojo.stockPlaced = cursor.getString(2);
                mappedRetailerPojo.currentStock = currentStock;
                mappedRetailerPojo.stockMovementRetailerId = cursor.getString(4);
                mappedRetailerPojo.retailerId = cursor.getString(5);
                mappedRetailerPojo.pog = String.valueOf(Integer.parseInt(cursor.getString(2)) - Integer.parseInt(currentStock));


                mappedRetailerPojoList.add(mappedRetailerPojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return mappedRetailerPojoList;
    }

    public List<MappedRetailerPojo> getMappedRetailerWithDistributorCropProduct(String customer_id, String product_id, String crop_id) {
        List<MappedRetailerPojo> mappedRetailerPojoList = new ArrayList<MappedRetailerPojo>();
        // Select All Query
//        String selectQuery = "SELECT retailer_name, retailer_tin_no, sum(stock_placed)stock_placed, sum(current_stock)current_stock, stock_movement_retailer_id,stock_movement_retailer_details.retailer_id FROM stock_movement_retailer_details LEFT JOIN retailers ON retailers.retailer_id=stock_movement_retailer_details.retailer_id WHERE stock_movement_id = " + stock_movement_id + " and product_id = " + product_id + " GROUP BY stock_movement_retailer_details.retailer_id";

        String selectQuery = "SELECT r.retailer_name, r.retailer_tin_no, sum(rss.quantity)sumquantity, rss.stock_supply_id,rss.retailer_id FROM retailer_stock_supply rss LEFT JOIN retailers r ON rss.retailer_id=r.retailer_id WHERE rss.distributor_id = " + customer_id + " and rss.product_id = " + product_id + " and rss.crop_id = " + crop_id + " GROUP BY rss.retailer_id";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MappedRetailerPojo mappedRetailerPojo = new MappedRetailerPojo();

//                String currentStock = getCurrentStockSMRD(Integer.valueOf(product_id), Integer.parseInt(crop_id), Integer.valueOf(stock_movement_id), cursor.getString(5));
                mappedRetailerPojo.retailerName = cursor.getString(0);
                mappedRetailerPojo.retailerTinNo = cursor.getString(1);
                mappedRetailerPojo.stockPlaced = cursor.getString(2);
//                mappedRetailerPojo.currentStock = currentStock;
                mappedRetailerPojo.stockMovementRetailerId = cursor.getString(3);
                mappedRetailerPojo.retailerId = cursor.getString(4);
//                mappedRetailerPojo.pog = String.valueOf(Integer.parseInt(cursor.getString(2)) - Integer.parseInt(currentStock));


                mappedRetailerPojoList.add(mappedRetailerPojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return mappedRetailerPojoList;
    }


    public String getPOGMappedRetailerWithDistributorAndProduct(String stock_movement_id, String product_id) {
        // List<MappedRetailerPojo> mappedRetailerPojoList = new ArrayList<MappedRetailerPojo>();
        // Select All Query
        String selectQuery = "SELECT (sum(stock_placed)- sum(current_stock)) as pog, sum(stock_placed)stock_placed, sum(current_stock)current_stock, stock_movement_retailer_id,stock_movement_retailer_details.retailer_id FROM stock_movement_retailer_details LEFT JOIN retailers ON retailers.retailer_id=stock_movement_retailer_details.retailer_id WHERE stock_movement_id = " + stock_movement_id + " and product_id = " + product_id;


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {


            return cursor.getString(0);


        }

        // return DIVISIONS list
        cursor.close();
        return "0";
    }

    public List<MappedRetailerWithDistributorPojo> getMappedRetailerWithDistributor(String customer_id, String retailerIds) {
        List<MappedRetailerWithDistributorPojo> mappedRetailerWithDistributorPojo = new ArrayList<MappedRetailerWithDistributorPojo>();
        // Select All Query
        String selectQuery = "select retailers.retailer_id,retailers.retailer_name,distributor_retailer.distributor_id from distributor_retailer left join  retailers on retailers.mobile_id=distributor_retailer.retailer_id where distributor_retailer.distributor_id=" + customer_id + " and retailers.retailer_id is not NULL and retailers.retailer_id not in (" + retailerIds + ")";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MappedRetailerWithDistributorPojo mappedRetailerPojo = new MappedRetailerWithDistributorPojo();

                mappedRetailerPojo.retailerId = cursor.getString(0);
                mappedRetailerPojo.retailerName = cursor.getString(1);
                mappedRetailerPojo.distributorId = cursor.getString(2);
                System.out.println("ret id" + cursor.getString(0) + "ret name" + cursor.getString(1) + "dist id" + cursor.getString(2));

                mappedRetailerWithDistributorPojo.add(mappedRetailerPojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return mappedRetailerWithDistributorPojo;
    }


    public List<RetailersNamePojo> searchTinNoIsAvailable(String tin_no) {
        List<RetailersNamePojo> customerList = new ArrayList<RetailersNamePojo>();
        // Select All Query
        /// String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '"+id+"'" ;
        //  String selectQuery  = " SELECT retailer_name,retailer_tin_no,address,phone,mobile,email_id,ffmid FROM "+TABLE_RETAILER  +" left join distributor_retailer ds on ds.retailer_id = retailers.retailer_id where ds.distributor_id="+id;
        String selectQuery = "SELECT * FROM retailers where retailer_tin_no = '" + tin_no + "'";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RetailersNamePojo retailersNamePojo = new RetailersNamePojo();

                retailersNamePojo.sqliteId = cursor.getString(0);
                retailersNamePojo.retailerId = cursor.getString(1);
                retailersNamePojo.retailerName = cursor.getString(2);
                retailersNamePojo.retailerTinNo = cursor.getString(3);
                retailersNamePojo.address = cursor.getString(4);
                retailersNamePojo.phoneNo = cursor.getString(5);
                retailersNamePojo.mobileNo = cursor.getString(6);
                retailersNamePojo.email = cursor.getString(7);
                retailersNamePojo.distributorId = cursor.getString(8);


                customerList.add(retailersNamePojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return customerList;
    }

    public boolean isDistributorRetailerMapped(String retailer_id, String distributor_id) {

        String selectQuery = "SELECT * FROM distributor_retailer where retailer_id = '" + retailer_id + "' and distributor_id='" + distributor_id + "'";
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            cursor.close();
            return true;
        }

        // return DIVISIONS list
        cursor.close();
        return false;
    }


    public void insertDistributorRetailers(SQLiteDatabase db, DistributorsRetailerPojo distributorsRetailerPojo) {
        //SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_retailers = new ContentValues();
        values_retailers.put(KEY_TABLE_DISTRIBUTOR_ID, distributorsRetailerPojo.distributorId);
        values_retailers.put(KEY_TABLE_RETAILER_ID, distributorsRetailerPojo.retailerId);

        // Inserting Row
        db.insert(TABLE_DISTRIBUTOR_RETAILER, null, values_retailers);

        //  db.close(); // Closing database connection
    }

    public void insertDistributorRetailers(DistributorsRetailerPojo distributorsRetailerPojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_retailers = new ContentValues();
        values_retailers.put(KEY_TABLE_DISTRIBUTOR_ID, distributorsRetailerPojo.distributorId);
        values_retailers.put(KEY_TABLE_RETAILER_ID, distributorsRetailerPojo.retailerId);

        // Inserting Row
        db.insert(TABLE_DISTRIBUTOR_RETAILER, null, values_retailers);

        db.close(); // Closing database connection
    }


    public StockMovementFirstListPojo getOfflineStockPlacementRetailerList(String user_id, String company_id, String division_id) {
        StockMovementFirstListPojo stockMovementFirstListPojo = null;
        // String selectQuerys = "SELECT sum(stock_placed)sumstockpalced,stock_movement_detail.product_id,brand_name,crop_id,stock_movement_detail.stock_movement_id,stock_movement_detail.customer_id FROM stock_movement_detail_retailer left join stock_movement on stock_movement.stock_movement_id=stock_movement_detail.stock_movement_id left join products on products.product_id=stock_movement_detail.product_id where user_id="+ user_id+" and stock_movement.company_id="+company_id+" and stock_movement.division_id="+division_id+" group by (stock_movement_detail.product_id)";
        String selectQuerys = "SELECT stock_movement_retailer_details.retailer_id, retailer_tin_no,sum(stock_placed)sumstockpalced,stock_movement_retailer_details.product_id,brand_name,products.product_crop_id,stock_movement_retailer_details.stock_movement_id FROM stock_movement_retailer_details left join stock_movement on stock_movement.stock_movement_id=stock_movement_retailer_details.stock_movement_id left join products on products.product_id=stock_movement_retailer_details.product_id left join retailers on retailers.retailer_id=stock_movement_retailer_details.retailer_id where stock_movement_retailer_details.user_id=" + user_id + " and stock_movement.company_id=" + company_id + " and stock_movement.division_id= " + division_id + " group by (stock_movement_retailer_details.product_id)";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            stockMovementFirstListPojo = new StockMovementFirstListPojo();

            String.valueOf(getSumOfStockPlacementRetailer(cursor.getInt(1)) -
                    (getstockMovementRetailerDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) +
                            getstockMovementDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5))));

            String pog;
            if (getstockMovementRetailerDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) == 0) {
                pog = "0";
            } else {
                pog = String.valueOf(getSumOfStockPlacementRetailer(cursor.getInt(1)) -
                        (getstockMovementRetailerDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), userId) +
                                getstockMovementDetailsRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5))));
            }

            stockMovementFirstListPojo.stockPlaced = cursor.getString(0);
            stockMovementFirstListPojo.currentStock = getCurrentStockRetailer(cursor.getInt(1), cursor.getInt(3), cursor.getInt(4));
            stockMovementFirstListPojo.productId = cursor.getInt(1);
            stockMovementFirstListPojo.brandName = cursor.getInt(2);
            stockMovementFirstListPojo.cropId = cursor.getInt(3);
            stockMovementFirstListPojo.pog = pog;


        }

        cursor.close();
        return stockMovementFirstListPojo;
    }


    public long getSumOfStockPlacementRetailer(int product_id) {
        long sum = 0;
        String selectQuerys = "SELECT sum(stock_placed)sumstockpalced FROM stock_movement_detail  where product_id=" + product_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public long getstockMovementRetailerDetailsRetailer(int product_id, int crop_id, int stock_movement_id, String user_id) {
        long sum = 0;
        String selectQuerys = "select  current_stock from stock_movement_retailer_details where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and user_id=" + user_id + " order by stock_movement_retailer_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public long getstockMovementDetailsRetailer(int product_id, int crop_id, int stock_movement_id, int customer_id) {
        long sum = 0;
        String selectQuerys = "select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and customer_id=" + customer_id + " order by stock_movement_detail_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return Long.parseLong(cursor.getString(0));
        }

        cursor.close();
        return sum;
    }

    public String getCurrentStockRetailer(int product_id, int crop_id, int stock_movement_id) {
        //String selectQuerys="select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= "+product_id+" and crop_id="+crop_id+" and stock_movement_id="+stock_movement_id+" order by stock_movement_detail_id  desc limit 1";
        String selectQuerys = "select  current_stock from stock_movement_retailer_details where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= " + product_id + " and crop_id=" + crop_id + " and stock_movement_id=" + stock_movement_id + " and user_id=" + userId + " order by stock_movement_detail_id  desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "0";
    }


    public String getTokenAmount(String customer_id, String division_id, int year, String payment_type) {
        //String selectQuerys="select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= "+product_id+" and crop_id="+crop_id+" and stock_movement_id="+stock_movement_id+" order by stock_movement_detail_id  desc limit 1";
        String selectQuerys = "select sum(total_amount)total_amount from payment_collection  where  customer_id= " + customer_id + " and division_id=" + division_id + " and payment_datetime like '" + year + "%' and payment_type like '" + payment_type + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return cursor.getString(0);
        }
        return "0";
    }

    public String getSumOfOrderAmount(String customer_id, String division_id) {
        String selectQuerys = "SELECT sum(SD.order_price) as sum_order FROM service_order AS SO LEFT JOIN service_order_details AS SD ON SD.service_relation_id = SO.service_id where SO.user_id = " + userId + " and division_id=" + division_id + " AND SO.customer_id = " + customer_id + " AND SO.order_type=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuerys);
        Cursor cursor = db.rawQuery(selectQuerys, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            return String.valueOf(cursor.getDouble(0));
        }
        return "0";
    }

    public String getSumOfOrderInAmount(String userId, String customer_id, String division_id, String serviceID) {
        double sum = 0;
        String selectQuery = " SELECT "
                + "SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE
                + " FROM " + TABLE_SERVICEORDER
                + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID
                + " LEFT JOIN " + TABLE_SERVICEORDERDETAILS + " AS SD ON SD."
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO." + KEY_TABLE_SERVICEORDER_ID
                + " LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID
                + " LEFT JOIN " + TABLE_SCHEMES + " AS SCH ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " = SCH." + KEY_SCHEMES_MASTER_ID
                + " LEFT JOIN " + TABLE_PRODUCTS + " AS PRO ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " = PRO." + KEY_PRODUCT_MASTER_ID
                + " LEFT JOIN division AS dv ON dv.division_id = SO.division_id"
                + " LEFT JOIN scheme_products AS sp ON sp.scheme_id = SCH.scheme_id" +
                "  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId
                + " AND  SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + " = " + customer_id + " AND SO."
                + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=1 AND SO.division_id=" + division_id + " AND SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = " + serviceID + " group by " + KEY_TABLE_SERVICEORDER_ORDERDATE + "," + KEY_PRODUCT_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            do {
                sum = sum + cursor.getDouble(0);


            } while (cursor.moveToNext());


            return String.valueOf(sum);
        }
        return "0";
    }


    public String getSumOfABAmount(String userId, String customer_id, String division_id, String serviceID) {
        double sum = 0;
        String selectQuery = " SELECT "
                + "SD." + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT
                + " FROM " + TABLE_SERVICEORDER
                + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID
                + " LEFT JOIN " + TABLE_SERVICEORDERDETAILS + " AS SD ON SD."
                + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO." + KEY_TABLE_SERVICEORDER_ID
                + " LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID
                + " LEFT JOIN " + TABLE_SCHEMES + " AS SCH ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " = SCH." + KEY_SCHEMES_MASTER_ID
                + " LEFT JOIN " + TABLE_PRODUCTS + " AS PRO ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " = PRO." + KEY_PRODUCT_MASTER_ID
                + " LEFT JOIN division AS dv ON dv.division_id = SO.division_id"
                + " LEFT JOIN scheme_products AS sp ON sp.scheme_id = SCH.scheme_id" +
                "  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId
                + " AND  SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + " = " + customer_id + " AND SO."
                + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=2 AND SO.division_id=" + division_id + " AND SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = " + serviceID + " group by " + KEY_TABLE_SERVICEORDER_ORDERDATE + "," + KEY_PRODUCT_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            do {
                sum = sum + cursor.getDouble(0);


            } while (cursor.moveToNext());


            return String.valueOf(sum);
        }
        return "0";
    }

    public void insertGrade(SQLiteDatabase db, GradePojo gradePojo) {
        SQLiteDatabase db1 = this.getWritableDatabase();

        ContentValues values_retailers = new ContentValues();
        values_retailers.put(GRADE_ID, gradePojo.gradeId);
        values_retailers.put(GRADE_NAME, gradePojo.gradeName);
        values_retailers.put(PRICE_PER_KM, gradePojo.pricePerKm);

        if (isAlreadyRecordExist(db1, gradePojo.gradeId, TABLE_GRADE, GRADE_ID) == 0) {
            // Inserting Row
            db1.insert(TABLE_GRADE, null, values_retailers);
            Log.d("User", "TABLE_GRADE Inserted");
        } else {
            Log.d("User", "TABLE_GRADE Updated");
            db1.update(TABLE_GRADE, values_retailers, GRADE_ID + "=?", new String[]{gradePojo.gradeId});
        }
        db1.close(); // Closing database connection
    }


    public String getTravelAllowance(String user_id) {
        //String selectQuerys="select  current_stock from stock_movement_detail where current_stock <>0 and (stock_placed is null or stock_placed=0) and product_id= "+product_id+" and crop_id="+crop_id+" and stock_movement_id="+stock_movement_id+" order by stock_movement_detail_id  desc limit 1";
        String selectQuerys = "SELECT grade.grade_id,grade.price_per_km from users join grade on grade.grade_id = users.grade where users.user_id= " + user_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("selectQuery1 Token", selectQuerys);
        Cursor cursor = null;
        try {

            cursor = db.rawQuery(selectQuerys, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {

                return cursor.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
        }
        return "0";
    }

    public List<BankDetailsListPojo> getBankDetails(String customer_id) {
        List<BankDetailsListPojo> bankDetailsListPojos = new ArrayList<BankDetailsListPojo>();
        // Select All Query
        /// String selectQuery = "SELECT  * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = '"+id+"'" ;
        //  String selectQuery  = " SELECT retailer_name,retailer_tin_no,address,phone,mobile,email_id,ffmid FROM "+TABLE_RETAILER  +" left join distributor_retailer ds on ds.retailer_id = retailers.retailer_id where ds.distributor_id="+id;
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER_BANKDETAILS + " where customer_id = '" + customer_id + "'";


        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BankDetailsListPojo bankDetailsListPojo = new BankDetailsListPojo();

                bankDetailsListPojo.bankName = cursor.getString(5);
                bankDetailsListPojo.accountNo = cursor.getString(3);
                bankDetailsListPojo.ifscCode = cursor.getString(7);


                bankDetailsListPojos.add(bankDetailsListPojo);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        cursor.close();
        return bankDetailsListPojos;
    }


    public String getPricePerUnitByProductId(String productId) {
        String price = "0";
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " where product_id = '" + productId + "'";
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            float value = (Float.parseFloat(cursor.getString(2)) * Float.parseFloat(cursor.getString(3))) / 100;
            float value1 = Float.parseFloat(cursor.getString(2)) - value;
            price = String.valueOf(value1);
        }

        // return DIVISIONS list
        cursor.close();

        return price;
    }

    public String getProductNametByProductId(String productId) {
        String product = "0";
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " where " + KEY_PRODUCT_MASTER_ID + " = " + productId;
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            product = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_BRAND_NAME));
        }

        // return DIVISIONS list
        cursor.close();

        return product;
    }

    public String getPricePerUnitByProductId(String productId, String regionId) {
        String price = "0";
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " where product_id = '" + productId + "' and region_id = '" + regionId + "' AND date(from_date)<= date('" + Common.getCurrentDateYYYYMMDD() + "')" + " AND date(to_date)>= date('" + Common.getCurrentDateYYYYMMDD() + "') AND status=1 order by product_ids desc limit 1";
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            float value1 = Float.parseFloat(cursor.getString(2));
//            price = String.valueOf(value1);
            float disc = Float.parseFloat(cursor.getString(3));
            float Totaldiscount = value1 * disc / 100;
            float discountedprice = value1 - Totaldiscount;
            price = String.valueOf(discountedprice);
        } else {
           /* String selectQuery5 = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " where product_id = '" + productId + "'";
            Cursor cursor5 = db.rawQuery(selectQuery5, null);
            if (cursor5.moveToFirst()) {
                float productPrice = Float.parseFloat(cursor5.getString(2));
//                price = String.valueOf(productPrice);
                float disc = Float.parseFloat(cursor5.getString(3));
                float Totaldiscount = productPrice *  disc / 100;
                float discountedprice = productPrice - Totaldiscount;
                price=String.valueOf(discountedprice);
            }*/

            String allindiaRegionID = "47";
            String selectQuery5 = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " WHERE " + KEY_TABLE_SCHEME_PRODUCTS_PRODUCT_ID + " = " + productId + " AND " + KEY_TABLE_SCHEME_PRODUCTS_REGION_ID + " = " + allindiaRegionID + " AND date(from_date)<= date('" + Common.getCurrentDateYYYYMMDD() + "')" + " AND date(to_date)>= date('" + Common.getCurrentDateYYYYMMDD() + "') AND status=1 order by product_ids desc limit 1";
            Log.d("selectQuery5", selectQuery5);
            Cursor cursor5 = db.rawQuery(selectQuery5, null);
            if (cursor5.moveToFirst()) {
                float value1 = Float.parseFloat(cursor5.getString(2));
                float disc = Float.parseFloat(cursor5.getString(3));
                float Totaldiscount = value1 * disc / 100;
                float discountedprice = value1 - Totaldiscount;
                price = String.valueOf(discountedprice);
            }
        }

        cursor.close();

        return price;
    }

    public void addUserRegions(UserRegions userRegions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TABLE_USER_REGION_USER_REGION_ID, userRegions.getUserRegionsId());
        values.put(KEY_TABLE_USER_REGION_USER_ID, userRegions.getUserId());
        values.put(KEY_TABLE_USER_REGION_REGION_ID, userRegions.getRegionId());
        values.put(KEY_TABLE_USER_REGION_STATUS, userRegions.getStatus());
        if (isAlreadyRecordExist(db, userRegions.getUserRegionsId(), TABLE_USER_REGIONS, KEY_TABLE_USER_REGION_USER_REGION_ID) == 0) {
            db.insert(TABLE_USER_REGIONS, null, values);
        } else {
            db.update(TABLE_USER_REGIONS, values, KEY_TABLE_USER_REGION_USER_REGION_ID + "=?", new String[]{userRegions.getUserRegionsId()});
        }
        db.close(); // Closing database connection
    }

    public Retailer getRetailer(String retailerId) {
        Retailer retailer = null;
        String selectQuery = "SELECT * FROM " + TABLE_RETAILER + " where " + KEY_TABLE_RETAILER_PRIMARY_ID + " =" + retailerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    retailer = new Retailer();
                    retailer.setID(cursor.getInt(0));
                    retailer.set_ret_masterid(cursor.getString(1));
                    retailer.set_ret_name(cursor.getString(2));
                    retailer.set_ret_tin_no(cursor.getString(3));
                    retailer.set_ret_address(cursor.getString(4));
                    retailer.set_ret_district(cursor.getString(5));
                    retailer.set_ret_taluka(cursor.getString(6));
                    retailer.set_ret_village(cursor.getString(7));
                    retailer.set_ret_region_id(cursor.getString(8));
                    retailer.set_ret_gstin_no(cursor.getString(9));
                    retailer.set_ret_phone(cursor.getString(10));
                    retailer.set_ret_mobile(cursor.getString(11));
                    retailer.set_email(cursor.getString(12));
                    retailer.set_ret_dist_id(cursor.getString(13));
                    retailer.set_ret_dist_sap_code(cursor.getString(14));
                    retailer.set_ret_status(cursor.getString(15));
                    retailer.set_ret_cdatetime(cursor.getString(16));
                    retailer.set_ret_udatetime(cursor.getString(17));
                    retailer.set_ffmid(cursor.getString(18));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }


        return retailer;
    }

    public FarmerPojo getFarmer(String farmerId) {
        FarmerPojo farmer = null;
        String selectQuery = "SELECT * FROM " + TABLE_FARMERS + " where " + KEY_TABLE_FARMER_ID + " =" + farmerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    farmer = new FarmerPojo();
                    farmer.setPrimaryId(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_FARMER_PRIMARY_ID)));
                    farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_ID)));
                    farmer.setFarmerName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_NAME)));
                    farmer.setRegionId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_REGION_ID)));
                    farmer.setLocationDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_DISTRICT)));
                    farmer.setLocationTaluka(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_TALUKA)));
                    farmer.setLocationVillage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_VILLAGE)));
                    farmer.setMobile(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_PHONE)));
                    farmer.setFfmId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_FFM_ID)));
                    farmer.setStatus(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_STATUS)));
                    farmer.setTotalLandHolding(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_TOTAL_LAND_HOLDING)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }


        return farmer;
    }

    public List<Retailer> getAllRetailers(String key, String customerID) {
        List<Retailer> dairy = new ArrayList<Retailer>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RETAILER + " r LEFT JOIN " + TABLE_DISTRIBUTOR_RETAILER + " dr ON (r." + KEY_TABLE_RETAILER_PRIMARY_ID + " = dr." + KEY_TABLE_RETAILER_ID + ") WHERE " + "dr." + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = " + customerID + " ORDER BY r." + KEY_TABLE_RETAILER_MASTER_ID + " DESC LIMIT 20";
        if (key != null && key.length() > 0)
            selectQuery = "SELECT * FROM " + TABLE_RETAILER + " r LEFT JOIN " + TABLE_DISTRIBUTOR_RETAILER + " dr ON (r." + KEY_TABLE_RETAILER_PRIMARY_ID + " = dr." + KEY_TABLE_RETAILER_ID + ") WHERE r." + KEY_TABLE_RETAILER_NAME + " " + "like " + key + " AND dr." + KEY_TABLE_RETAILER_DISTRIBUTOR_ID + " = " + customerID + " ORDER BY r." + KEY_TABLE_RETAILER_MASTER_ID + " DESC LIMIT 20";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Retailer retailer = new Retailer();
                retailer.setID(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_RETAILER_PRIMARY_ID)));
                retailer.set_ret_masterid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_MASTER_ID)));
                retailer.set_ret_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_NAME)));
                retailer.set_ret_tin_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_TIN_NO)));
                retailer.set_ret_address(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_ADDRESS)));
                retailer.set_ret_district(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_DISTRICT)));
                retailer.set_ret_taluka(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_TALUKA)));
                retailer.set_ret_village(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_VILLAGE)));
                retailer.set_ret_region_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_REGION_ID)));
                retailer.set_ret_gstin_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_GSTIN_NO)));
                retailer.set_ret_phone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_PHONE)));
                retailer.set_ret_mobile(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_MOBILE)));
                retailer.set_email(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_EMAIL_ID)));
                retailer.set_ret_dist_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_DISTRIBUTOR_ID)));
                retailer.set_ret_dist_sap_code(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_SAP_CODE)));
                retailer.set_ret_status(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_STATUS)));
                retailer.set_ret_cdatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_CREATED_DATETIME)));
                retailer.set_ret_udatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_UPDATED_DATETIME)));
                retailer.set_ffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_FFMID)));
                dairy.add(retailer);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;

    }


    public List<Retailer> getAllRetailers(String key) {
        List<Retailer> dairy = new ArrayList<Retailer>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_NAME + " " + "like " + key + " ORDER BY " + KEY_TABLE_RETAILER_MASTER_ID + " DESC LIMIT 20";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Retailer retailer = new Retailer();
                retailer.setID(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_RETAILER_PRIMARY_ID)));
                retailer.set_ret_masterid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_MASTER_ID)));
                retailer.set_ret_name(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_NAME)));
                retailer.set_ret_tin_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_TIN_NO)));
                retailer.set_ret_address(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_ADDRESS)));
                retailer.set_ret_district(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_DISTRICT)));
                retailer.set_ret_taluka(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_TALUKA)));
                retailer.set_ret_village(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_VILLAGE)));
                retailer.set_ret_region_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_REGION_ID)));
                retailer.set_ret_gstin_no(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_GSTIN_NO)));
                retailer.set_ret_phone(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_PHONE)));
                retailer.set_ret_mobile(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_MOBILE)));
                retailer.set_email(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_EMAIL_ID)));
                retailer.set_ret_dist_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_DISTRIBUTOR_ID)));
                retailer.set_ret_dist_sap_code(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_SAP_CODE)));
                retailer.set_ret_status(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_STATUS)));
                retailer.set_ret_cdatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_CREATED_DATETIME)));
                retailer.set_ret_udatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_UPDATED_DATETIME)));
                retailer.set_ffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_FFMID)));
                dairy.add(retailer);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;

    }


    public List<FarmerPojo> getOfflineFarmers() {
        List<FarmerPojo> dairy = new ArrayList<FarmerPojo>();
        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_FARMERS + " WHERE " + KEY_TABLE_FARMER_FFM_ID + " = 0 ORDER BY " + KEY_TABLE_FARMER_PRIMARY_ID + " DESC LIMIT 20";
            Log.e("bd query", selectQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    FarmerPojo farmer = new FarmerPojo();
                    farmer.setPrimaryId(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_FARMER_PRIMARY_ID)));
                    farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_ID)));
                    farmer.setFarmerName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_NAME)));
                    farmer.setRegionId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_REGION_ID)));
                    farmer.setLocationDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_DISTRICT)));
                    farmer.setLocationTaluka(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_TALUKA)));
                    farmer.setLocationVillage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_VILLAGE)));
                    farmer.setMobile(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_PHONE)));
                    farmer.setFfmId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_FFM_ID)));
                    farmer.setStatus(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_STATUS)));
                    farmer.setTotalLandHolding(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_TOTAL_LAND_HOLDING)));
                    dairy.add(farmer);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        }
        // return contact list
        return dairy;

    }

    public List<FarmerPojo> getAllFarmers(String key) {
        List<FarmerPojo> dairy = new ArrayList<FarmerPojo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FARMERS + " ORDER BY " + KEY_TABLE_FARMER_PRIMARY_ID + " DESC LIMIT 20";
        if (key != null && key.length() > 0)
            selectQuery = "SELECT * FROM " + TABLE_FARMERS + " WHERE " + KEY_TABLE_FARMER_NAME + " " + "like " + key + " ORDER BY " + KEY_TABLE_FARMER_PRIMARY_ID + " DESC LIMIT 20";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FarmerPojo farmer = new FarmerPojo();
                farmer.setPrimaryId(cursor.getInt(cursor.getColumnIndex(KEY_TABLE_FARMER_PRIMARY_ID)));
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_ID)));
                farmer.setFarmerName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_NAME)));
                farmer.setRegionId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_REGION_ID)));
                farmer.setLocationDistrict(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_DISTRICT)));
                farmer.setLocationTaluka(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_TALUKA)));
                farmer.setLocationVillage(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_VILLAGE)));
                farmer.setMobile(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_PHONE)));
                farmer.setFfmId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_FFM_ID)));
                farmer.setStatus(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_STATUS)));
                farmer.setTotalLandHolding(cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_TOTAL_LAND_HOLDING)));
                dairy.add(farmer);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dairy;

    }

    public void updateSOA(String userId, String status, String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_SOA_ORDER_STATUS, status);
        // Inserting Row
        db.update(TABLE_SERVICE_ORDER_APPROVAL, contentValues, KEY_SOA_USER_ID + " =? AND " + KEY_SOA_ORDER_ID + " =? ", new String[]{String.valueOf(userId), orderId});
        db.close(); // Closing database connection
    }

   /* public void updatePA(String userId, String status, String empVisitId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PA_PLANNER_STATUS, status);
        // Inserting Row
        db.update(TABLE_PLANNER_APPROVAL, contentValues, KEY_PA_USER_ID + " =? AND " + KEY_PA_EMP_VISIT_ID + " =? ", new String[]{String.valueOf(userId), empVisitId});
        db.close(); // Closing database connection
    }*/


    public String getTeam(String sel_userId) {

        List<String> usersList = new ArrayList<>();
        usersList.add(sel_userId);
        usersList = getTeam(sel_userId, usersList);
        LinkedHashSet<String> set = new LinkedHashSet<>(usersList);
        String teamUsers = set.toString();
        teamUsers = teamUsers.substring(1, teamUsers.lastIndexOf("]"));

        Common.Log.i("Team " + teamUsers);
        return teamUsers;

    }

    private List<String> getTeam(String sel_userId, List<String> usersList) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + " WHERE " + KEY_TABLE_USERS_REPORTING_MANAGER_ID + " = " + sel_userId;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_USERS_MASTER_ID));
                usersList.add(userId);
                getTeam(userId, usersList);
            } while (cursor.moveToNext());
        }
        return usersList;
    }

    public void updateSOD(String ffm_id, String order_detail_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID, ffm_id);
        contentValues.put(KEY_TABLE_SERVICEORDER_FFM_ID, ffm_id);
        // Inserting Row
        db.update(TABLE_SERVICEORDERDETAILS, contentValues, KEY_TABLE_SERVICEORDER_DETAIL_ID + " =? ", new String[]{String.valueOf(order_detail_id)});
        db.close();
    }

    public List<Products_Pojo> getProductsByDivisionId(int divisionId) {
        List<Products_Pojo> productsList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCTS_DIVISION_ID + " = " + divisionId + " ORDER BY " + KEY_PRODUCT_MASTER_ID + " DESC ";
        ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products_Pojo products_pojo = new Products_Pojo();
                products_pojo.setProductMasterId(cursor.getString(1));
                products_pojo.setProductName(cursor.getString(2));
                products_pojo.setProductDescription(cursor.getString(3));
                products_pojo.setProductSapCode(cursor.getString(4));
                products_pojo.setProductcropid(cursor.getString(5));
                products_pojo.setProductcompanyid(cursor.getString(6));
                products_pojo.setProductdivisionid(cursor.getString(7));
                products_pojo.setProductregeion(cursor.getString(8));
                products_pojo.setProductprice(cursor.getString(9));
                products_pojo.setProductdiscount(cursor.getString(10));
                products_pojo.setProductfromdate(cursor.getString(11));
                products_pojo.setProducttodate(cursor.getString(12));
                products_pojo.setProductstatus(cursor.getString(13));
                products_pojo.setProductcdatetime(cursor.getString(14));
                products_pojo.setProductudatetime(cursor.getString(15));
                products_pojo.setProductImages(cursor.getString(16));
                products_pojo.setProductVideos(cursor.getString(17));
                // Adding contact to list
                productsList.add(products_pojo);
            } while (cursor.moveToNext());
        }

        // return Products list
        return productsList;

    }

    public List<Crops> getCropsByDivisionId(int divisionId) {
        List<Crops> cropsList = new ArrayList<Crops>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CROPS + " WHERE " + KEY_TABLE_CROPS_DIVISION_ID + " = " + divisionId + " ORDER BY " + KEY_TABLE_CROPS_CROP_MASTER_ID + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Crops crops = new Crops();
                crops.setCropMasterID(cursor.getString(1));
                crops.setCropName(cursor.getString(2));
                crops.setCropcode(cursor.getString(3));
                crops.setCropsapid(cursor.getString(4));
                crops.setCropdivisionId(cursor.getString(5));
                crops.setCropcdatetime(cursor.getString(6));
                crops.setCropudatetime(cursor.getString(7));
                // Adding contact to list
                cropsList.add(crops);
            } while (cursor.moveToNext());
        }

        // return DIVISIONS list
        return cropsList;
    }

    public String getRetailerFFMID(String retailerId) {
        String ffmId = "0";
        String query = "SELECT " + KEY_TABLE_RETAILER_FFMID + " FROM " + TABLE_RETAILER + " WHERE " + KEY_TABLE_RETAILER_PRIMARY_ID + " = " + retailerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            ffmId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_RETAILER_FFMID));
        }
        return ffmId;
    }

    public String getFarmerFFMID(String farmerId) {
        String ffmId = "0";
        String query = "SELECT " + KEY_TABLE_FARMER_FFM_ID + " FROM " + TABLE_FARMERS + " WHERE " + KEY_TABLE_FARMER_PRIMARY_ID + " = " + farmerId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            ffmId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_FARMER_FFM_ID));
        }
        return ffmId;
    }

    public List<ServiceOrderHistory> getOrderHistoryByApprovalId(String orderApprovalId) {
        List<ServiceOrderHistory> sohList = new ArrayList<ServiceOrderHistory>();
        ServiceOrderHistory soh = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SERVICE_ORDER_HISTORY + " WHERE " + KEY_SOH_ORDER_APPROVAL_ID + " = " + orderApprovalId + " ORDER BY " + KEY_SOH_PRIMARY_ID + " DESC ";
        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                soh = new ServiceOrderHistory();
                soh.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_SOH_PRIMARY_ID));
                soh.orderHistoryId = cursor.getString(cursor.getColumnIndex(KEY_SOH_ORDER_HISTORY_ID));
                soh.orderId = cursor.getString(cursor.getColumnIndex(KEY_SOH_ORDER_ID));
                soh.orderDetailsId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID));
                soh.cropId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID));
                soh.schemeId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID));
                soh.slabId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_SLAB_ID));
                soh.productId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID));
                soh.orderQuantity = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY));
                soh.quantity = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY));
                soh.orderPrice = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE));
                soh.advanceAmount = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT));
                soh.status = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_STATUS));
                soh.createdBy = cursor.getString(cursor.getColumnIndex(KEY_SOH_CREATED_BY));
                soh.cDateTime = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_CREATED_DATETIME));
                soh.modifiedBy = cursor.getString(cursor.getColumnIndex(KEY_SOH_MODIFIED_BY));
                soh.orderApprovalId = cursor.getString(cursor.getColumnIndex(KEY_SOH_ORDER_APPROVAL_ID));
                sohList.add(soh);
            } while (cursor.moveToNext());
        }

        // return contact list
        return sohList;
    }

    public int insertRetailerStockSupply(RetailerStockSupply rss) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RETAILER_SS_STOCK_SUPPLY_ID, rss.stockSupplyId);
        values.put(KEY_RETAILER_SS_USER_ID, rss.userId);
        values.put(KEY_RETAILER_SS_DISTRIBUTOR_ID, rss.distributerId);
        values.put(KEY_RETAILER_SS_RETAILER_ID, rss.retailerId);
        values.put(KEY_RETAILER_SS_CROP_ID, rss.cropId);
        values.put(KEY_RETAILER_SS_PRODUCT_ID, rss.productId);
        values.put(KEY_RETAILER_SS_SUPPLIED_DATE, rss.suppliedDate);
        values.put(KEY_RETAILER_SS_QUANTITY, rss.quantity);
        values.put(KEY_RETAILER_SS_USER_TYPE, rss.userType);
        if (rss.stockSupplyId.equalsIgnoreCase("0"))
            id = (int) db.insert(TABLE_RETAILER_STOCK_SUPPLY, null, values);
        else if (isAlreadyRecordExist(db, String.valueOf(rss.stockSupplyId), TABLE_RETAILER_STOCK_SUPPLY, KEY_RETAILER_SS_STOCK_SUPPLY_ID) == 0) {
            id = (int) db.insert(TABLE_RETAILER_STOCK_SUPPLY, null, values);
        } else {
            id = db.update(TABLE_RETAILER_STOCK_SUPPLY, values, KEY_RETAILER_SS_STOCK_SUPPLY_ID + "=?", new String[]{String.valueOf(rss.stockSupplyId)});
        }
        db.close(); // Closing database connection
        return id;
    }

    public int insertStockDispatchItem(StockDispatchLineItem sdl) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STOCK_DISPATCH_ITEM_LINE_ITEM_ID, sdl.stockDispatchLineItemId);
        values.put(KEY_STOCK_DISPATCH_ITEM_STOCK_DISPATCH_ID, sdl.stockDispatchId);
        values.put(KEY_STOCK_DISPATCH_ITEM_CROP_ID, sdl.cropId);
        values.put(KEY_STOCK_DISPATCH_ITEM_PRODUCT_ID, sdl.productId);
        values.put(KEY_STOCK_DISPATCH_ITEM_QUANTITY, sdl.quantity);
        values.put(KEY_STOCK_DISPATCH_ITEM_DISPATCH_DATE, sdl.dispatchDate);
        values.put(KEY_STOCK_DISPATCH_ITEM_PRICE, sdl.price);
        values.put(KEY_STOCK_DISPATCH_ITEM_ORDER_SAP_ID, sdl.orderSapId);
        if (isAlreadyRecordExist(db, String.valueOf(sdl.stockDispatchLineItemId), TABLE_STOCK_DISPATCH_ITEM, KEY_STOCK_DISPATCH_ITEM_LINE_ITEM_ID) == 0) {
            id = (int) db.insert(TABLE_STOCK_DISPATCH_ITEM, null, values);
        } else {
            id = db.update(TABLE_STOCK_DISPATCH_ITEM, values, KEY_STOCK_DISPATCH_ITEM_LINE_ITEM_ID + "=?", new String[]{String.valueOf(sdl.stockDispatchLineItemId)});
        }
        db.close(); // Closing database connection
        return id;
    }

    public int insertStockDispatch(StockDispatch sd) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STOCK_DISPATCH_STOCK_DISPATCH_ID, sd.stockDispatchId);
        values.put(KEY_STOCK_DISPATCH_COMPANY_ID, sd.companyId);
        values.put(KEY_STOCK_DISPATCH_ORDER_SAP_ID, sd.orderSapId);
        values.put(KEY_STOCK_DISPATCH_SO_NO, sd.soNo);
        values.put(KEY_STOCK_DISPATCH_ORDER_ID, sd.orderId);
        values.put(KEY_STOCK_DISPATCH_DISTRIBUTOR_SAP_ID, sd.distributorSapId);
        values.put(KEY_STOCK_DISPATCH_DISTRIBUTOR_ID, sd.distributorId);
        values.put(KEY_STOCK_DISPATCH_DIVISION_ID, sd.divisionId);
        values.put(KEY_STOCK_DISPATCH_DISPATCH_DATE, sd.dispatchDate);
        values.put(KEY_STOCK_DISPATCH_ORDER_CREATED_DATE, sd.orderCreatedDate);
        values.put(KEY_STOCK_DISPATCH_FISCAL_YEAR, sd.fiscalYear);
        values.put(KEY_STOCK_DISPATCH_USER_ID, sd.userId);
        if (isAlreadyRecordExist(db, String.valueOf(sd.stockDispatchId), TABLE_STOCK_DISPATCH, KEY_STOCK_DISPATCH_STOCK_DISPATCH_ID) == 0) {
            id = (int) db.insert(TABLE_STOCK_DISPATCH, null, values);
        } else {
            id = db.update(TABLE_STOCK_DISPATCH, values, KEY_STOCK_DISPATCH_STOCK_DISPATCH_ID + "=?", new String[]{String.valueOf(sd.stockDispatchId)});
        }
        db.close(); // Closing database connection
        return id;
    }

    public void deleteRetailer(int mobile_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RETAILER, KEY_TABLE_RETAILER_PRIMARY_ID + "=?", new String[]{String.valueOf(mobile_id)});
        db.close();
    }

    public void deleteFarmer(int mobile_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FARMERS, KEY_TABLE_FARMER_PRIMARY_ID + "=?", new String[]{String.valueOf(mobile_id)});
        db.close();
    }
    public void DeleteTfaListDataRow(int list_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TFA_ACTIVITYLIST, KEY_tfa_list_id+ "=?", new String[]{String.valueOf(list_id)});
        db.close();
    }
    public void DeleteTfaVillageListDataRow(int list_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TFA_VILLAGELIST, KEY_tfa_list_id+ "=?", new String[]{String.valueOf(list_id)});
        db.close();
    }
    public void DeleteTfaApprovalListDataRow(int list_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TFA_APPROVAL_HISTORY, KEY_tfa_list_id+ "=?", new String[]{String.valueOf(list_id)});
        db.close();
    }
    public List<StockDispatchLineItem> getStockDispatchLineItems(int cropId, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<StockDispatchLineItem> sdls = new ArrayList<>();
        String query = "SELECT * FROM `stock_dispatch_line_items` sdl inner join stock_dispatch sd on (sdl.stock_dispatch_id=sd.stock_dispatch_id) where crop_id=" + cropId + " and product_id=" + productId;
        Log.e("Stock Query ", query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StockDispatchLineItem sdl = new StockDispatchLineItem();
                sdl.cropId = cursor.getString(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_CROP_ID));
                sdl.dispatchDate = cursor.getString(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_DISPATCH_DATE));
                sdl.orderSapId = cursor.getString(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_ORDER_SAP_ID));
                sdl.price = cursor.getString(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_PRICE));
                sdl.primaryId = cursor.getInt(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_PRIMARY_ID));
                sdl.productId = cursor.getString(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_PRODUCT_ID));
                sdl.quantity = cursor.getString(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_QUANTITY));
                sdl.stockDispatchId = cursor.getString(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_STOCK_DISPATCH_ID));
                sdl.stockDispatchLineItemId = cursor.getString(cursor.getColumnIndex(KEY_STOCK_DISPATCH_ITEM_LINE_ITEM_ID));
                sdls.add(sdl);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return sdls;
    }

    public List<RetailerStockSupply> getOfflineRetailerStockSupply() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<RetailerStockSupply> rss = new ArrayList<>();
        String stockQuery = "SELECT * FROM " + TABLE_RETAILER_STOCK_SUPPLY + " WHERE " + KEY_RETAILER_SS_STOCK_SUPPLY_ID + " = 0";
        Log.e("Retailer Stock Query ", stockQuery);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(stockQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    RetailerStockSupply supply = new RetailerStockSupply();
                    supply.mobileId = cursor.getInt(cursor.getColumnIndex(KEY_RETAILER_SS_PRIMARY_ID));
                    supply.stockSupplyId = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_STOCK_SUPPLY_ID));
                    supply.suppliedDate = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_SUPPLIED_DATE));
                    supply.retailerId = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_RETAILER_ID));
                    supply.quantity = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_QUANTITY));
                    supply.productId = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_PRODUCT_ID));
                    supply.distributerId = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_DISTRIBUTOR_ID));
                    supply.cropId = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_CROP_ID));
                    supply.userId = cursor.getString(cursor.getColumnIndex(KEY_RETAILER_SS_USER_ID));
                    rss.add(supply);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            Common.closeCursor(cursor);
        }

        return rss;
    }

    public void updateRetailerStockSupply(InsertedRetailer insertedRetailer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RETAILER_SS_STOCK_SUPPLY_ID, insertedRetailer.ffmId);
        db.update(TABLE_RETAILER_STOCK_SUPPLY, values, KEY_RETAILER_SS_PRIMARY_ID + "=?", new String[]{String.valueOf(insertedRetailer.mobileId)});
        db.close();
    }

    public Geo_Tracking_POJO getGeoTrackingDataByDate(String date, String userIdFromSP) {
        String date1 = Common.getCurrentDateYYYYMMDD();
        if (date != null && date.length() > 10) {
            date1 = date.split(" ")[0];
        }
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_VISIT_DATE + " LIKE '%" + date1 + "%' and " + KEY_TABLE_GEO_TRACKING_USER_ID + "='" + userIdFromSP + "'";
        Cursor cursor = db.rawQuery(query, null);
        Geo_Tracking_POJO geo_tracking_pojo = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    geo_tracking_pojo = new Geo_Tracking_POJO();
                    geo_tracking_pojo.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_ID))));
                    geo_tracking_pojo.setGeoMasterID(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_MASTER_ID)));
                    geo_tracking_pojo.setGeoVisitType(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_VISIT_TYPE)));
                    geo_tracking_pojo.set_Geo_user_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_USER_ID)));
                    geo_tracking_pojo.set_Geo_check_in_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG)));
                    geo_tracking_pojo.setGeo_check_out_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG)));
                    geo_tracking_pojo.setGeo_route_path_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG)));
                    geo_tracking_pojo.setGeo_distance(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_DISTANCE)));
                    geo_tracking_pojo.setGeo_visit_date(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_VISIT_DATE)));
                    geo_tracking_pojo.setGeo_check_in_time(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME)));
                    geo_tracking_pojo.setGeo_check_out_time(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME)));
                    geo_tracking_pojo.setGeostatus(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_STATUS)));
                    geo_tracking_pojo.setGeoffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_FFMID)));
                    geo_tracking_pojo.setGeocdatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CREATED_DATETIME)));
                    geo_tracking_pojo.setGeoudatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME)));
                    geo_tracking_pojo.setPolyline(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_POLYLINE)));
                    geo_tracking_pojo.setMeter_reading_checkin_image(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKIN_IMAGE)));
                    geo_tracking_pojo.setMeter_reading_checkin_text(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKIN_TEXT)));
                    geo_tracking_pojo.setMeter_reading_checkout_image(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKOUT_IMAGE)));
                    geo_tracking_pojo.setMeter_reading_checkout_text(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKOUT_TEXT)));
                    geo_tracking_pojo.setVehicle_type(cursor.getString(cursor.getColumnIndex(VEHICLE_TYPE)));
                    geo_tracking_pojo.setCheckin_comment(cursor.getString(cursor.getColumnIndex(CHECKIN_COMMENT)));
                    geo_tracking_pojo.setPersonal_uses_km(cursor.getString(cursor.getColumnIndex(PERSONAL_USES_KM)));
                    geo_tracking_pojo.setPause(cursor.getString(cursor.getColumnIndex(PAUSE)));
                    geo_tracking_pojo.setResume(cursor.getString(cursor.getColumnIndex(RESUME)));
                    // Adding contact to list
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {

        }
        return geo_tracking_pojo;
    }

    public Geo_Tracking_POJO getLastGeoRecord(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_USER_ID + "=" + userId + " ORDER BY " + KEY_TABLE_GEO_TRACKING_CREATED_DATETIME + " DESC LIMIT 0,1";
        Cursor cursor = db.rawQuery(query, null);
        Geo_Tracking_POJO geo_tracking_pojo = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    geo_tracking_pojo = new Geo_Tracking_POJO();
                    geo_tracking_pojo.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_ID))));
                    geo_tracking_pojo.setGeoMasterID(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_MASTER_ID)));
                    geo_tracking_pojo.setGeoVisitType(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_VISIT_TYPE)));
                    geo_tracking_pojo.set_Geo_user_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_USER_ID)));
                    geo_tracking_pojo.set_Geo_check_in_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG)));
                    geo_tracking_pojo.setGeo_check_out_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG)));
                    geo_tracking_pojo.setGeo_route_path_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG)));
                    geo_tracking_pojo.setGeo_distance(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_DISTANCE)));
                    geo_tracking_pojo.setGeo_visit_date(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_VISIT_DATE)));
                    geo_tracking_pojo.setGeo_check_in_time(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME)));
                    geo_tracking_pojo.setGeo_check_out_time(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME)));
                    geo_tracking_pojo.setGeostatus(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_STATUS)));
                    geo_tracking_pojo.setGeoffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_FFMID)));
                    geo_tracking_pojo.setGeocdatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CREATED_DATETIME)));
                    geo_tracking_pojo.setGeoudatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME)));
                    geo_tracking_pojo.setPolyline(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_POLYLINE)));
                    geo_tracking_pojo.setMeter_reading_checkin_image(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKIN_IMAGE)));
                    geo_tracking_pojo.setMeter_reading_checkin_text(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKIN_TEXT)));
                    geo_tracking_pojo.setMeter_reading_checkout_image(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKOUT_IMAGE)));
                    geo_tracking_pojo.setMeter_reading_checkout_text(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKOUT_TEXT)));
                    geo_tracking_pojo.setVehicle_type(cursor.getString(cursor.getColumnIndex(VEHICLE_TYPE)));
                    geo_tracking_pojo.setCheckin_comment(cursor.getString(cursor.getColumnIndex(CHECKIN_COMMENT)));
                    geo_tracking_pojo.setPersonal_uses_km(cursor.getString(cursor.getColumnIndex(PERSONAL_USES_KM)));
                    geo_tracking_pojo.setPause(cursor.getString(cursor.getColumnIndex(PAUSE)));
                    geo_tracking_pojo.setResume(cursor.getString(cursor.getColumnIndex(RESUME)));
                    // Adding contact to list
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {

        }
        return geo_tracking_pojo;
    }

    public List<Geo_Tracking_POJO> getGeoTrackingDataForDays(String recordDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_VISIT_DATE + " >= '" + recordDate + "'";
        Cursor cursor = db.rawQuery(query, null);
        Geo_Tracking_POJO geo_tracking_pojo = null;
        List<Geo_Tracking_POJO> geoRecords = new ArrayList<>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    geo_tracking_pojo = new Geo_Tracking_POJO();
                    geo_tracking_pojo.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_ID))));
                    geo_tracking_pojo.setGeoMasterID(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_MASTER_ID)));
                    geo_tracking_pojo.setGeoVisitType(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_VISIT_TYPE)));
                    geo_tracking_pojo.set_Geo_user_id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_USER_ID)));
                    geo_tracking_pojo.set_Geo_check_in_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG)));
                    geo_tracking_pojo.setGeo_check_out_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG)));
                    geo_tracking_pojo.setGeo_route_path_lat_lon(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG)));
                    geo_tracking_pojo.setGeo_distance(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_DISTANCE)));
                    geo_tracking_pojo.setGeo_visit_date(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_VISIT_DATE)));
                    geo_tracking_pojo.setGeo_check_in_time(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME)));
                    geo_tracking_pojo.setGeo_check_out_time(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME)));
                    geo_tracking_pojo.setGeostatus(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_STATUS)));
                    geo_tracking_pojo.setGeoffmid(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_FFMID)));
                    geo_tracking_pojo.setGeocdatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_CREATED_DATETIME)));
                    geo_tracking_pojo.setGeoudatetime(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_UPDATED_DATETIME)));
                    geo_tracking_pojo.setPolyline(cursor.getString(cursor.getColumnIndex(KEY_TABLE_GEO_TRACKING_POLYLINE)));
                    geo_tracking_pojo.setMeter_reading_checkin_image(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKIN_IMAGE)));
                    geo_tracking_pojo.setMeter_reading_checkin_text(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKIN_TEXT)));
                    geo_tracking_pojo.setMeter_reading_checkout_image(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKOUT_IMAGE)));
                    geo_tracking_pojo.setMeter_reading_checkout_text(cursor.getString(cursor.getColumnIndex(METER_READING_CHECKOUT_TEXT)));
                    geo_tracking_pojo.setVehicle_type(cursor.getString(cursor.getColumnIndex(VEHICLE_TYPE)));
                    geo_tracking_pojo.setCheckin_comment(cursor.getString(cursor.getColumnIndex(CHECKIN_COMMENT)));
                    geo_tracking_pojo.setPersonal_uses_km(cursor.getString(cursor.getColumnIndex(PERSONAL_USES_KM)));
                    geo_tracking_pojo.setPause(cursor.getString(cursor.getColumnIndex(PAUSE)));
                    geo_tracking_pojo.setResume(cursor.getString(cursor.getColumnIndex(RESUME)));
                    geoRecords.add(geo_tracking_pojo);
                    // Adding contact to list
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {

        }
        return geoRecords;


    }

    public String getDistrictNameById(String district) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_DISTRICT_DISTRICT_NAME + " FROM " + TABLE_DISTRICT + " WHERE " + KEY_DISTRICT_DISTRICT_ID + " = " + district;
        Cursor cursor = db.rawQuery(query, null);
        String districtName = null;
        if (cursor != null && cursor.moveToFirst()) {
            districtName = cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_DISTRICT_NAME));
        }
        if (cursor != null)
            cursor.close();
        return districtName;
    }
    public String getDistributorNameById(String customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_TABLE_CUSTOMER_NAME + " FROM " + TABLE_CUSTOMERS + " WHERE " + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + customer;
        Cursor cursor = db.rawQuery(query, null);
        String districtName = null;
        if (cursor != null && cursor.moveToFirst()) {
            districtName = cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_NAME));
        }
        if (cursor != null)
            cursor.close();
        return districtName;
    }
    public String getorderintentstatus(String order_id) {
        String selectQuery;
        Cursor cursor = null;
        //   selectQuery = "SELECT meter_reading_checkin_text,meter_reading_checkout_text FROM `geo_tracking` where meter_reading_checkin_text is NOT NULL  and meter_reading_checkin_text !='' and meter_reading_checkin_text !='null' AND vehicle_type='" + vehicleType + "' ORDER by tracking_id DESC limit 1";
        selectQuery = "SELECT " +KEY_TABLE_SERVICEORDER_APPROVAL_COMMENTS+" FROM "+TABLE_SERVICEORDER+
                " where "+ KEY_TABLE_SERVICEORDER_ID+"="+order_id;
        String cmt = null;
        Common.Log.i(selectQuery);
        Log.d("anilmek",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                 cmt = cursor.getString(0);
               
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return cmt;
    }
    private class Async_Logout extends AsyncTask<Void, Void, String> {
        String jsonData;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "user_id=" + Common.getUserIdFromSP(context));
                Request request = new Request.Builder()
                        .url(Constants.URL_LOGOUT)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1login" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        Log.e("Logout", "Logged out success..");
                    } else {
                        Log.e("Logout", "Logged out error..");
                        //  Toast.makeText(getApplicationContext(), jsonobject.getString("msg"), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }


        }
    }

    // This method will return if your table exist a field or not
    public boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        boolean isExist = false;

        Cursor res = null;

        try {

            res = db.rawQuery("Select * from " + tableName + " limit 1", null);

            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex != -1) {
                isExist = true;
            }

        } catch (Exception e) {
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception e1) {
            }
        }

        return isExist;
    }

    public SQLiteDatabase getWritableDbIfClosed(SQLiteDatabase db) {
        return !db.isOpen() ? this.getWritableDatabase() : db;
    }

    public void addCatalogues(List<CatalogueCropsPojo> list) {
        if (list == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < list.size(); i++) {
            CatalogueCropsPojo catalogueCropsPojo = list.get(i);

            ContentValues values_companies = new ContentValues();
            values_companies.put(CROP_NAME, catalogueCropsPojo.cropName);
            values_companies.put(CROP_IMG_PATH, catalogueCropsPojo.cropImgPath.startsWith("/") ? catalogueCropsPojo.cropImgPath.substring(1) : catalogueCropsPojo.cropImgPath);
            values_companies.put(SERVER_PK, catalogueCropsPojo.serverPk);
            values_companies.put(STATUS, catalogueCropsPojo.status);
            values_companies.put(VERSION, catalogueCropsPojo.version);
            values_companies.put(DATE_TIME, catalogueCropsPojo.dateTime);

            if (isAlreadyRecordExist(getWritableDbIfClosed(db), String.valueOf(catalogueCropsPojo.serverPk), TABLE_CATALOGUE_CROPS, SERVER_PK) == 0) {
                // Inserting Row
                db.insert(TABLE_CATALOGUE_CROPS, null, values_companies);

                Log.d("User", "TABLE_CATALOGUE_CROPS Inserted");
            } else {
                String NULL1 = null;
                Log.d("User", "TABLE_CATALOGUE_CROPS Updated");
                if (!isUpToDate(getWritableDbIfClosed(db), TABLE_CATALOGUE_CROPS, catalogueCropsPojo.serverPk, catalogueCropsPojo.version))
                    values_companies.put(IMG_URI, NULL1);

                db.update(TABLE_CATALOGUE_CROPS, values_companies, SERVER_PK + "=?", new String[]{String.valueOf(catalogueCropsPojo.serverPk)});
            }
            addCatalogueCropProduct(catalogueCropsPojo.catalogueCropsProductsList, db);
        }
        db.close(); // Closing database connection


    }

    private void addCatalogueCropProduct(List<CatalogueCropsProductsPojo> list, SQLiteDatabase db) {
        if (list == null || list.size() == 0) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {

            CatalogueCropsProductsPojo catalogueCropsProductsPojo = list.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(CROP_RELATION_ID, catalogueCropsProductsPojo.cropRelationId);
            contentValues.put(SERVER_PK, catalogueCropsProductsPojo.serverPk); // Contact Div_code
            contentValues.put(PRODUCT_NAME, catalogueCropsProductsPojo.productName);
            contentValues.put(DESCRIPTION, catalogueCropsProductsPojo.description);
            contentValues.put(STATUS, catalogueCropsProductsPojo.status); // Contact DivName
            contentValues.put(VERSION, catalogueCropsProductsPojo.version); // Contact Div_code
            contentValues.put(PRODUCT_IMG, catalogueCropsProductsPojo.productImg.startsWith("/") ? catalogueCropsProductsPojo.productImg.substring(1) : catalogueCropsProductsPojo.productImg); // Contact Div_code
            contentValues.put(DATE_TIME, catalogueCropsProductsPojo.dateTime); // Contact Div_code
            if (isAlreadyRecordExist(getWritableDbIfClosed(db), String.valueOf(catalogueCropsProductsPojo.serverPk), TABLE_CATALOGUE_CROPS_PRODUCTS, SERVER_PK) == 0) {

                db.insert(TABLE_CATALOGUE_CROPS_PRODUCTS, null, contentValues);
            } else {
                String NULL1 = null;
                Log.d("User", "TABLE_CATALOGUE_CROPS Updated");
                if (!isUpToDate(getWritableDbIfClosed(db), TABLE_CATALOGUE_CROPS_PRODUCTS, catalogueCropsProductsPojo.serverPk, catalogueCropsProductsPojo.version))
                    contentValues.put(IMG_URI, NULL1);

                db.update(TABLE_CATALOGUE_CROPS_PRODUCTS, contentValues, SERVER_PK + "=?", new String[]{String.valueOf(catalogueCropsProductsPojo.serverPk)});
            }
        }
    }

    public List<CatalogueCropsPojo> getCatalogueCrops() {
        return getCatalogueCrops(null);
    }

    public List<CatalogueCropsPojo> getCatalogueCrops(String whereClause) {
        List<CatalogueCropsPojo> list = new ArrayList<>();
        String selectQuery;
        if (whereClause != null) {
            selectQuery = "SELECT * FROM " + TABLE_CATALOGUE_CROPS + " where " + whereClause + " and " + STATUS + " =1";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_CATALOGUE_CROPS + " where " + STATUS + " =1";
        }
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    CatalogueCropsPojo catalogueCropsPojo = new CatalogueCropsPojo();
                    catalogueCropsPojo.sqlPrimaryKey = cursor.getInt(0);
                    catalogueCropsPojo.cropName = cursor.getString(1);
                    catalogueCropsPojo.cropImgPath = cursor.getString(2);
                    catalogueCropsPojo.serverPk = cursor.getInt(3);
                    catalogueCropsPojo.status = cursor.getInt(4);
                    catalogueCropsPojo.version = cursor.getString(5);
                    catalogueCropsPojo.imgURI = cursor.getString(6);
                    catalogueCropsPojo.dateTime = cursor.getString(7);
                    list.add(catalogueCropsPojo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }


        return list;
    }

    public List<CatalogueCropsProductsPojo> getCatalogueCropProducts() {
        return getCatalogueCropProducts(null);
    }

    public List<CatalogueCropsProductsPojo> getCatalogueCropProducts(String cropId) {
        List<CatalogueCropsProductsPojo> list = new ArrayList<>();
        String selectQuery;
        if (cropId != null) {
            selectQuery = "SELECT * FROM " + TABLE_CATALOGUE_CROPS_PRODUCTS + " where " + CROP_RELATION_ID + " = '" + cropId + "' and " + STATUS + "=1";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_CATALOGUE_CROPS_PRODUCTS + " where " + STATUS + " =1";
        }
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    CatalogueCropsProductsPojo catalogueCropsProductsPojo = new CatalogueCropsProductsPojo();
                    catalogueCropsProductsPojo.sqlPrimaryKey = cursor.getInt(0);
                    catalogueCropsProductsPojo.cropRelationId = cursor.getInt(1);
                    catalogueCropsProductsPojo.serverPk = cursor.getInt(2);
                    catalogueCropsProductsPojo.productName = cursor.getString(3);
                    catalogueCropsProductsPojo.productImg = cursor.getString(4);
                    catalogueCropsProductsPojo.description = cursor.getString(5);
                    catalogueCropsProductsPojo.status = cursor.getInt(6);
                    catalogueCropsProductsPojo.version = cursor.getString(7);
                    catalogueCropsProductsPojo.imgURI = cursor.getString(8);
                    catalogueCropsProductsPojo.dateTime = cursor.getString(9);
                    list.add(catalogueCropsProductsPojo);


                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return list;
    }


    public List<CatalogueCropsProductsPojo> getCatalogueCropProductsImgNull() {
        List<CatalogueCropsProductsPojo> list = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT * FROM " + TABLE_CATALOGUE_CROPS_PRODUCTS + " where " + PRODUCT_IMG + " is not " + null + " and " + IMG_URI + " is " + null + " and " + STATUS + "=1";

        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    CatalogueCropsProductsPojo catalogueCropsProductsPojo = new CatalogueCropsProductsPojo();
                    catalogueCropsProductsPojo.sqlPrimaryKey = cursor.getInt(0);
                    catalogueCropsProductsPojo.cropRelationId = cursor.getInt(1);
                    catalogueCropsProductsPojo.serverPk = cursor.getInt(2);
                    catalogueCropsProductsPojo.productName = cursor.getString(3);
                    catalogueCropsProductsPojo.productImg = cursor.getString(4);
                    catalogueCropsProductsPojo.description = cursor.getString(5);
                    catalogueCropsProductsPojo.status = cursor.getInt(6);
                    catalogueCropsProductsPojo.version = cursor.getString(7);
                    catalogueCropsProductsPojo.imgURI = cursor.getString(8);
                    catalogueCropsProductsPojo.dateTime = cursor.getString(9);
                    list.add(catalogueCropsProductsPojo);


                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return list;
    }

    public int updateTable(String tableName, ContentValues contentValues, String pColumnName, String pValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = 0;
        try {

            result = db.update(tableName, contentValues, pColumnName + "=?", new String[]{pValue});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeDataBase(db);

        }
        return result;
    }

    public boolean isUpToDate(SQLiteDatabase db, String tableName, int primaryKey, String version) {

        String selectQuery = "SELECT version,img_uri FROM " + tableName + " where " + SERVER_PK + "='" + primaryKey + "'";

        Common.Log.i(selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {

                if (version.equals(cursor.getString(0))) {
                    return true;
                } else if (cursor.getString(1) != null) {
                    File file = new File(cursor.getString(1));
                    if (file.exists()) {
                        boolean deleted = file.delete();
                        if (deleted)
                            Log.d("file", "file deleted....");
                    }

                    return false;
                }

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }


    public double getPreviousMeterReading(String vehicleType, String userIdFromSP) {
        String selectQuery;
        Cursor cursor = null;
        //   selectQuery = "SELECT meter_reading_checkin_text,meter_reading_checkout_text FROM `geo_tracking` where meter_reading_checkin_text is NOT NULL  and meter_reading_checkin_text !='' and meter_reading_checkin_text !='null' AND vehicle_type='" + vehicleType + "' ORDER by tracking_id DESC limit 1";
        selectQuery = "SELECT meter_reading_checkin_text,meter_reading_checkout_text FROM `geo_tracking` where meter_reading_checkin_text is NOT NULL  and meter_reading_checkin_text !='' and meter_reading_checkin_text !='null' AND vehicle_type='" + vehicleType + "' AND user_id='" + userIdFromSP + "' ORDER by date(visit_date) DESC limit 1";

        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                String meter_reading_checkin_text = cursor.getString(0);
                String meter_reading_checkout_text = cursor.getString(1);
                if (Common.isNumeric(meter_reading_checkout_text)) {
                    return Double.parseDouble(meter_reading_checkout_text);
                } else if (Common.isNumeric(meter_reading_checkin_text)) {
                    return Double.parseDouble(meter_reading_checkin_text);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return 0;
    }


    public String[] getMeterReadingDetails(String userId, String visitDate) {
        String[] details = new String[4];
        String selectQuery;
        Cursor cursor = null;
        selectQuery = "SELECT meter_reading_checkin_text,meter_reading_checkin_image,meter_reading_checkout_text,meter_reading_checkout_image FROM `geo_tracking` where meter_reading_checkin_text is NOT NULL  and meter_reading_checkin_text !='' and meter_reading_checkin_text !='null' AND user_id='" + userId + "' and  visit_date like '" + visitDate + "' ORDER by tracking_id DESC limit 1";
        Common.Log.i(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                String meter_reading_checkin_text = cursor.getString(0);
                String meter_reading_checkin_image = cursor.getString(1);
                String meter_reading_checkout_text = cursor.getString(2);
                String meter_reading_checkout_image = cursor.getString(3);
                if (Common.isNumeric(meter_reading_checkin_text)) {
                    details[0] = meter_reading_checkin_text;
                    details[1] = meter_reading_checkin_image;
                    details[2] = meter_reading_checkout_text;
                    details[3] = meter_reading_checkout_image;
                    return details;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return null;
    }
    //*Tfa
    public List<Approvaldetails> getOrderApprovalsByTfaListId2(String listId) {
        List<Approvaldetails> soaList = new ArrayList<Approvaldetails>();
        Approvaldetails soa = null;
        // Select All Query
        String selectQuery = "SELECT "+KEY_tfa_pending_by_name+","+KEY_tfa_pending_by_role+","+
                KEY_tfa_approval_status+","+KEY_tfa_approval_name+","+
                KEY_tfa_approval_role+" FROM " + TABLE_TFA_APPROVAL_HISTORY +
                " WHERE (" + KEY_tfa_approval_status + " <=4 " + " or " +KEY_tfa_approval_status + " =9 )"+ " and "+ KEY_tfa_list_id+"="+listId+" ORDER BY " + KEY_tfa_approval_id +" ASC ";
        Log.d("bdquery", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

               /* soa.pending_approval_name= cursor.getString(0);
                soa.pending_approval_role = cursor.getString(1);
                soa.approval_status2 = cursor.getInt(2);
                soa.approval_name2 = cursor.getString(3);
                soa.approval_role2 = cursor.getString(4);*/
                Approvaldetails approvaldetails=new Approvaldetails(cursor.getString(0),
                        cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getString(4));
                soaList.add(approvaldetails);
            } while (cursor.moveToNext());
        }

        // return contact list
        return soaList;

    }
    public long addPlannerActivity(ActivityPlanner activityPlanner, String type)
    {
        long i=0;
        SQLiteDatabase db = this.getWritableDatabase();
        int k = 0;
        ContentValues values_companies = new ContentValues();
        if (type.equals("server")) {
            values_companies.put(KEY_tfa_list_id, activityPlanner.getTfa_list_id());
            values_companies.put(KEY_actual__estimation_per_head, activityPlanner.getActual_estimation_per_head());
            values_companies.put(KEY_sync_status, 1);
            values_companies.put(KEY_approved_date, activityPlanner.getApproved_date());
            values_companies.put(KEY_location_lat_lng, activityPlanner.getLocation_lat_lang());
            values_companies.put(KEY_owner_name, activityPlanner.getOwner_name());
            values_companies.put(KEY_owner_number, activityPlanner.getOwner_number());
            values_companies.put(KEY_used_farmers, activityPlanner.getUsed_farmers());
            values_companies.put(KEY_non_used_farmers, activityPlanner.getNon_used_farmers());
            values_companies.put(KEY_actual_no_farmers, activityPlanner.getActual_no_farmers());
            values_companies.put(KEY_actual__total_expences, activityPlanner.getActual_total_expences());

        }
        values_companies.put(KEY_district_id, activityPlanner.getDistrict_id());
        values_companies.put(KEY_division_id, activityPlanner.getDivision_id()); // Contact Div_code
        values_companies.put(KEY_crop_id, activityPlanner.getCrop_id()); // Contact DivName
        values_companies.put(KEY_product_id, activityPlanner.getProduct_id()); // Contact Div_code
        values_companies.put(KEY_tfa_activity_master_id, activityPlanner.getTfa_activity_master_id()); // Contact DivName

        values_companies.put(KEY_activity_date, activityPlanner.getActivity_date());
        values_companies.put(KEY_taluka, activityPlanner.getTaluka());
        values_companies.put(KEY_village, activityPlanner.getVillage()); // Contact Div_code
        values_companies.put(KEY_no_of_farmers, activityPlanner.getNo_of_farmers()); // Contact DivName
        values_companies.put(KEY_estimation_per_head, activityPlanner.getEstimation_per_head()); // Contact Div_code
        values_companies.put(KEY_total_expences, activityPlanner.getTotal_expences()); // Contact DivName
        values_companies.put(KEY_advance_required, activityPlanner.getAdvance_required()); // Contact Div_code
        values_companies.put(KEY_conducting_place, activityPlanner.getConducting_place()); // Contact Div_code
        values_companies.put(KEY_user_id, activityPlanner.getUser_id()); // Contact DivName
        values_companies.put(KEY_created_user_id, activityPlanner.getCreated_user_id()); // Contact Div_code
        values_companies.put(KEY_user_email, activityPlanner.getUser_email()); // Contact DivName
        values_companies.put(KEY_status, activityPlanner.getStatus()); // Contact Div_code
        if (type.equals("local"))
        {
            values_companies.put(KEY_sync_status, 0); // Contact Div_code
        }
        values_companies.put(KEY_created_datetime, activityPlanner.getCreated_datetime()); // Contact Div_code
        values_companies.put(KEY_updated_datetime, activityPlanner.getUpdated_datetime()); // Contact Div_code
        values_companies.put(KEY_approval_status, activityPlanner.getApproval_status()); // Contact Div_code
        values_companies.put(KEY_approval_comments, activityPlanner.getApproval_comments()); // Contact Div_code
        values_companies.put(KEY_approved_by, activityPlanner.getApproved_by()); // Contact Div_code
        values_companies.put(KEY_approved_date, activityPlanner.getUpdated_datetime()); // Con

        i = db.insert(TABLE_TFA_ACTIVITYLIST, null, values_companies);
        Log.d("hi",String.valueOf(activityPlanner.getApproval_status()+"  "+activityPlanner.getCreated_user_id()));

        ContentValues values_approvalhistory = new ContentValues();
        values_approvalhistory.put(KEY_tfa_list_id, i);
        values_approvalhistory.put(KEY_tfa_approval_status, activityPlanner.getApproval_status());
        values_approvalhistory.put(KEY_tfa_approval_comment, activityPlanner.getApproval_comments());
        values_approvalhistory.put(KEY_tfa_approved_user_id, activityPlanner.getUser_id());
        values_approvalhistory.put(KEY_status, activityPlanner.getStatus()); // Contact Div_code
        values_approvalhistory.put(KEY_sync_status, 0); // Contact Div_code
        values_approvalhistory.put(KEY_created_datetime, activityPlanner.getCreated_datetime()); // Contact Div_code
        values_approvalhistory.put(KEY_updated_datetime, activityPlanner.getUpdated_datetime()); // Contact Div_code


        long ijk = db.insert(TABLE_TFA_APPROVAL_HISTORY, null, values_approvalhistory);



        if (i > 0) {
            k = 1;
            Log.d("anil_TABLE_TFA_ACTIVITYLIST", String.valueOf(i));
        }
        if (i < 0) {
            k = 0;
            Log.d("anil_TABLE_TFA_ACTIVITYLIST", String.valueOf(i));
        }

        db.close(); // Closing database connection
        return  i;
    }
    public long addVillageList(Village_list village_list, String type) {//server to local api mainact ,actindent
        int k=0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_companies = new ContentValues();
        if(type.equals("server"))
        {
            values_companies.put(KEY_tfa_village_id, village_list.getTfa_village_id());
            values_companies.put(KEY_updated_datetime, village_list.getUpdated_datetime());
        }

        values_companies.put(KEY_tfa_list_id, village_list.getTfa_list_id()); // Contact Div_code
        values_companies.put(KEY_village_name,village_list.getVillage_name()); // Contact Div_code
        values_companies.put(KEY_current_sal, village_list.getCurrent_sal());
        values_companies.put(KEY_potential, village_list.getPotential());

        values_companies.put(KEY_status, village_list.getStatus()); // Contact Div_code
        values_companies.put(KEY_sync_status,0); // Contact Div_code
        values_companies.put(KEY_created_datetime, village_list.getCreated_datetime()); // Contact Div_code



        long i=db.insert(TABLE_TFA_VILLAGELIST, null, values_companies);

        if(i>0)
        {
            k=1;
            Log.d("anil_TABLE_TFA_ACTIVITYLIST",String.valueOf(i));
        }
        if(i<0)
        {
            k=0;
            Log.d("anil_TABLE_TFA_ACTIVITYLIST",String.valueOf(i));
        }

        db.close(); // Closing database connection
        return  i;
    }
    public String updatetfa_approval(Approvaldetails approvaldetails,String type)
    {                                                                          //server to local api mainact ,actindent
        String sts="";
        SQLiteDatabase db = this.getWritableDatabase();

    /*    ContentValues values_activitylist = new ContentValues();
        if(type.equals("server")) {
        values_activitylist.put(KEY_tfa_list_id, approvaldetails.getTfa_approval_id());}
        values_activitylist.put(KEY_approval_status, approvaldetails.getApproval_status());
        values_activitylist.put(KEY_approval_comments, approvaldetails.getApproval_comments());
        values_activitylist.put(KEY_approved_by, approvaldetails.getApproved_by());
        values_activitylist.put(KEY_approved_date, approvaldetails.getApproved_date());*/

        ContentValues values_approvaltable = new ContentValues();
        values_approvaltable.put(KEY_tfa_approval_id, approvaldetails.getTfa_approval_id());
        values_approvaltable.put(KEY_tfa_list_id, approvaldetails.getList_id());
        values_approvaltable.put(KEY_tfa_approval_status, approvaldetails.getApproval_status());
        values_approvaltable.put(KEY_tfa_approval_comment, approvaldetails.getApproval_comments());
        values_approvaltable.put(KEY_tfa_approved_user_id, approvaldetails.getTfa_approved_user_id());
        values_approvaltable.put(KEY_status, approvaldetails.getStatus());
        values_approvaltable.put(KEY_created_datetime, approvaldetails.getCreated_datetime());
        values_approvaltable.put(KEY_updated_datetime, approvaldetails.getUpdated_datetime());
        values_approvaltable.put(KEY_tfa_approval_name, approvaldetails.getApproval_name());
        values_approvaltable.put(KEY_tfa_approval_role, approvaldetails.getApproval_role());
        values_approvaltable.put(KEY_tfa_approval_mail, approvaldetails.getApproval_mail());
        values_approvaltable.put(KEY_tfa_approval_mobile, approvaldetails.getApproval_pnno());
        values_approvaltable.put(KEY_tfa_pending_by_role, approvaldetails.getPending_by_role()); // Contact Div_code
        values_approvaltable.put(KEY_tfa_pending_by_name, approvaldetails.getPending_by_name()); // Contact Div_code

        long l=0;

       /* if(type.equals("local")) {


             l = db.update(TABLE_TFA_ACTIVITYLIST, values_activitylist, KEY_tfa_list_id + "=?", new String[]{String.valueOf(approvaldetails.getList_id())});
        }
        if(l>0) {
            values_approvaltable.put(KEY_sync_status, "0");
            Log.d("Updte", "TABLE_TFA_ACTIVITYLIST");
            db = getWritableDbIfClosed(db);
           long m= db.update(TABLE_TFA_APPROVAL_HISTORY, values_approvaltable, KEY_tfa_list_id + "=?", new String[]{String.valueOf(approvaldetails.getList_id())});
           if(m>0)
           {
               Log.d("Updte", "TABLE_TFA_ACTIVITYLIST");
               sts="sucess";
           }
        }*/

        if(type.equals("server")) {
            Log.d("Inserted",String.valueOf(approvaldetails.getApproval_status()));
            values_approvaltable.put(KEY_sync_status, "1");
            //Log.d("Updte", "TABLE_SCHEME_PRODUCTS Updated");
            db = getWritableDbIfClosed(db);
            long m = db.insert(TABLE_TFA_APPROVAL_HISTORY, null, values_approvaltable);
            if(m>0)
            {
                sts="sucess";
            }
        }



        db.close(); // Closing database connection
        return sts;
    }








    public String updatetfa_approvalbyamrmmo(Approvaldetails approvaldetails,String type)
    {
        String sts="";
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_approvaltable = new ContentValues();
        values_approvaltable.put(KEY_tfa_list_id, approvaldetails.getList_id());
        //values_approvaltable.put(KEY_tfa_approval_id, approvaldetails.getTfa_approval_id());// anil change
        values_approvaltable.put(KEY_tfa_approval_status, approvaldetails.getApproval_status());
        values_approvaltable.put(KEY_tfa_approval_comment, approvaldetails.getApproval_comments());
        values_approvaltable.put(KEY_tfa_approved_user_id, approvaldetails.getTfa_approved_user_id());
        values_approvaltable.put(KEY_status, approvaldetails.getApproval_status());
        values_approvaltable.put(KEY_sync_status, approvaldetails.getApproval_status());
        values_approvaltable.put(KEY_created_datetime, approvaldetails.getCreated_datetime());
        values_approvaltable.put(KEY_updated_datetime, approvaldetails.getUpdated_datetime());

        values_approvaltable.put(KEY_tfa_approval_name,approvaldetails.getApproval_name());
        values_approvaltable.put(KEY_tfa_approval_role,approvaldetails.getApproval_role());
        values_approvaltable.put(KEY_tfa_approval_mail,approvaldetails.getApproval_mail());
        values_approvaltable.put(KEY_tfa_approval_mobile,approvaldetails.getApproval_pnno());
        values_approvaltable.put(KEY_tfa_pending_by_name,approvaldetails.getApproval_name());
        values_approvaltable.put(KEY_tfa_pending_by_role,approvaldetails.getApproval_role());





        long m = db.insert(TABLE_TFA_APPROVAL_HISTORY, null, values_approvaltable);
        if (m > 0) {
            sts = "sucess";
        }


        db.close(); // Closing database connection
        return sts;
    }

    public String  addtfaactivitylistmaster(tfaactivitylist list)
    {
        String s;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_companies = new ContentValues();
        values_companies.put(KEY_tfa_master_id, list.getTfa_master_id());
        values_companies.put(KEY_tfa_title, list.getTfa_title());
        values_companies.put(KEY_status, list.getStatus()); // Contact Div_code
        values_companies.put(KEY_created_datetime, list.getCreated_datetime()); // Contact DivName
        values_companies.put(KEY_updated_datetime, list.getUpdated_datetime()); // Contact Div_code


        long i=db.insert(TABLE_TFA_ACTIVITY_MASTER, null, values_companies);
        Log.d("hiii",String.valueOf(i));
        if(i>0)
        {
            Log.d("anil_ACTIVITYLIST",String.valueOf(i));
            Log.d("User", "TABLE_TFA_ACTIVITYLIST Inserted");
            s="1";
        }
        else {
            Log.d("anil_TABLE_TFA_ACTIVITYLIST",String.valueOf(i));
            Log.d("User", "TABLE_TFA_ACTIVITYLIST prblm");
            s="-1";
        }
        db.close();
        return s;
        // Closing database connection
    }
    public HashMap<ArrayList,ArrayList> getactvitiestfa() {
        ArrayList<String> arraytitle = new ArrayList<String>();
        ArrayList<String> arrayid = new ArrayList<String>();
        HashMap<ArrayList,ArrayList> HashMap=new HashMap<ArrayList, ArrayList>();
        String selectQuery = "SELECT "+KEY_tfa_title +" , "+KEY_tfa_master_id+" FROM " + TABLE_TFA_ACTIVITY_MASTER ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                arraytitle.add(cursor.getString(0));
                arrayid.add(cursor.getString(1));

            } while (cursor.moveToNext());
        }
        HashMap.put(arraytitle,arrayid);
        db.close();
        return  HashMap;
    }














    public List<Products> getProductByDevisionIdandCropId(String key, String division_id,String sel_cropid) {
        List<Products> productsList = new ArrayList<>();
        District district = null;
        // String selectQuery = "SELECT "+ KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME +  " FROM " + TABLE_PRODUCTS  ;
        String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME + "," + KEY_PRODUCTS_PACKETS_COUNT + "," + KEY_PRODUCT_DISCOUNT + "," + KEY_PRODUCTS_CATALOG_URL + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_DIVISION_ID + " = " + division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_cropid + " and status=1";
        // String selectQuery = "SELECT * FROM " + TABLE_DISTRICT + " WHERE " + KEY_DISTRICT_DISTRICT_NAME + " " + KEY_DISTRICT_REGION_ID + " = " + regionId + " AND " + KEY_DISTRICT_STATUS + " = 1 ORDER BY " + KEY_DISTRICT_DISTRICT_ID + " DESC ";
        if (key != null && key.length() > 0) {
            //selectQuery = "SELECT "+ KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME +  " FROM " + TABLE_PRODUCTS  ;
            selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_BRAND_NAME + "," + KEY_PRODUCTS_PACKETS_COUNT + "," + KEY_PRODUCT_DISCOUNT + "," + KEY_PRODUCTS_CATALOG_URL + " FROM " + TABLE_PRODUCTS + "  where "+ KEY_PRODUCT_BRAND_NAME + " " + "like " + key + " and " +KEY_PRODUCTS_DIVISION_ID + " = " + division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_cropid + " and status=1";
        }


        Log.e("bd query", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products products_pojo = new Products();
                products_pojo.setProductMasterID(cursor.getString(0));
                products_pojo.setProductName(cursor.getString(1));
                // Adding contact to list
                productsList.add(products_pojo);
            } while (cursor.moveToNext());
        }

        // return Products list
        return productsList;
    }
    public ArrayList<Long> addPlannerActivity_Form(ActivityPlanner activityPlanner,String type)
    {
        ArrayList<Long> list=new ArrayList<Long>();
        long i=0;
        SQLiteDatabase db = this.getWritableDatabase();
        int k = 0;
        ContentValues values_companies = new ContentValues();
        if (type.equals("server")) {
            values_companies.put(KEY_tfa_list_id, activityPlanner.getTfa_list_id());
            values_companies.put(KEY_actual__estimation_per_head, activityPlanner.getActual_estimation_per_head());
            values_companies.put(KEY_sync_status, 1);
            values_companies.put(KEY_approved_date, activityPlanner.getApproved_date());
            values_companies.put(KEY_location_lat_lng, activityPlanner.getLocation_lat_lang());
            values_companies.put(KEY_owner_name, activityPlanner.getOwner_name());
            values_companies.put(KEY_owner_number, activityPlanner.getOwner_number());
            values_companies.put(KEY_used_farmers, activityPlanner.getUsed_farmers());
            values_companies.put(KEY_non_used_farmers, activityPlanner.getNon_used_farmers());
            values_companies.put(KEY_actual_no_farmers, activityPlanner.getActual_no_farmers());
            values_companies.put(KEY_actual__total_expences, activityPlanner.getActual_total_expences());

        }
        values_companies.put(KEY_district_id, activityPlanner.getDistrict_id());
        values_companies.put(KEY_division_id, activityPlanner.getDivision_id()); // Contact Div_code
        values_companies.put(KEY_crop_id, activityPlanner.getCrop_id()); // Contact DivName
        values_companies.put(KEY_product_id, activityPlanner.getProduct_id()); // Contact Div_code
        values_companies.put(KEY_tfa_activity_master_id, activityPlanner.getTfa_activity_master_id()); // Contact DivName

        values_companies.put(KEY_activity_date, activityPlanner.getActivity_date());
        values_companies.put(KEY_taluka, activityPlanner.getTaluka());
        values_companies.put(KEY_village, activityPlanner.getVillage()); // Contact Div_code
        values_companies.put(KEY_no_of_farmers, activityPlanner.getNo_of_farmers()); // Contact DivName
        values_companies.put(KEY_estimation_per_head, activityPlanner.getEstimation_per_head()); // Contact Div_code
        values_companies.put(KEY_total_expences, activityPlanner.getTotal_expences()); // Contact DivName
        values_companies.put(KEY_advance_required, activityPlanner.getAdvance_required()); // Contact Div_code
        values_companies.put(KEY_conducting_place, activityPlanner.getConducting_place()); // Contact Div_code
        values_companies.put(KEY_user_id, activityPlanner.getUser_id()); // Contact DivName
        values_companies.put(KEY_created_user_id, activityPlanner.getCreated_user_id()); // Contact Div_code
        values_companies.put(KEY_user_email, activityPlanner.getUser_email()); // Contact DivName
        values_companies.put(KEY_status, activityPlanner.getStatus()); // Contact Div_code
        if (type.equals("local"))
        {
            values_companies.put(KEY_sync_status, 0); // Contact Div_code
        }
        values_companies.put(KEY_created_datetime, activityPlanner.getCreated_datetime()); // Contact Div_code
        values_companies.put(KEY_updated_datetime, activityPlanner.getUpdated_datetime()); // Contact Div_code
        values_companies.put(KEY_approval_status, activityPlanner.getApproval_status()); // Contact Div_code
        values_companies.put(KEY_approval_comments, activityPlanner.getApproval_comments()); // Contact Div_code
        values_companies.put(KEY_approved_by, activityPlanner.getApproved_by()); // Contact Div_code
        values_companies.put(KEY_approved_date, activityPlanner.getUpdated_datetime()); // Con



        i = db.insert(TABLE_TFA_ACTIVITYLIST, null, values_companies);
        list.add(i);
        Log.d("hi",String.valueOf(activityPlanner.getApproval_status()+"  "+activityPlanner.getCreated_user_id()));

        ContentValues values_approvalhistory = new ContentValues();
        values_approvalhistory.put(KEY_tfa_list_id, i);
        values_approvalhistory.put(KEY_tfa_approval_status, activityPlanner.getApproval_status());
        values_approvalhistory.put(KEY_tfa_approval_comment, activityPlanner.getApproval_comments());
        values_approvalhistory.put(KEY_tfa_approved_user_id, activityPlanner.getUser_id());
        values_approvalhistory.put(KEY_status, activityPlanner.getStatus()); // Contact Div_code
        values_approvalhistory.put(KEY_sync_status, 0); // Contact Div_code
        values_approvalhistory.put(KEY_created_datetime, activityPlanner.getCreated_datetime()); // Contact Div_code
        values_approvalhistory.put(KEY_updated_datetime, activityPlanner.getUpdated_datetime()); // Contact Div_code

  /*      values_approvalhistory.put(KEY_tfa_approval_name, activityPlanner.getApproval_name()); // Contact Div_code
        values_approvalhistory.put(KEY_tfa_approval_role, activityPlanner.getApproval_role()); // Contact Div_code*/  //anil
        values_approvalhistory.put(KEY_tfa_approval_mail, activityPlanner.getApproval_mail()); // Contact Div_code
        values_approvalhistory.put(KEY_tfa_approval_mobile, activityPlanner.getApproval_pnno()); // Con
        long ijk = db.insert(TABLE_TFA_APPROVAL_HISTORY, null, values_approvalhistory);
        list.add(ijk);


        if (i > 0) {
            k = 1;
            Log.d("anil_TABLE_TFA_ACTIVITYLIST", String.valueOf(i));
        }
        if (i < 0) {
            k = 0;
            Log.d("anil_TABLE_TFA_ACTIVITYLIST", String.valueOf(i));
        }

        db.close(); // Closing database connection
        return  list;
    }
    public String update_ids_tfaformactivity(String Id, JSONArray ids, String loc_Id, ArrayList<String> loc_ids
            , String apprvloc, String apprvserver, String name, String role) {

        String status="";
        String query="";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_update = new ContentValues();
        values_update.put(KEY_tfa_list_id,Id);

        int i=db.update(TABLE_TFA_ACTIVITYLIST,
                values_update,
                KEY_tfa_list_id + " = ?  " ,
                new String[]{loc_Id});

        Log.d("chk",String.valueOf(i));
        int j;
        for(j=0;j<ids.length();j++)
        {
            try
            {
                String object=ids.getString(j);
                ContentValues values_update2 = new ContentValues();
                values_update2.put(KEY_tfa_village_id,object);
                int i2=db.update(TABLE_TFA_VILLAGELIST,
                        values_update2,
                        KEY_tfa_village_id + " = ?  " ,
                        new String[]{loc_ids.get(j)});
                Log.d("chk",String.valueOf(i2));

            }catch (Exception e)
            {

            }

        }
        //public boolean updateData(String id,String name,String surname,String marks) {

      /*  ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_tfa_approval_name,"");
        contentValues.put(KEY_tfa_approval_role,"");
        contentValues.put(KEY_tfa_pending_by_name,name);
        contentValues.put(KEY_tfa_pending_by_role,role);
        db.update(TABLE_TFA_APPROVAL_HISTORY, contentValues, KEY_tfa_approval_id+" = ?",new String[] { apprvloc });
        //  return true;
        //  }
*/
       ContentValues values_update3 = new ContentValues();
        values_update3.put(KEY_tfa_approval_id,12);    //anil unique constrain failled
       /* values_update3.put(KEY_tfa_list_id,Id);                  //tfa_list_id
        values_update3.put(KEY_tfa_approval_name,"");
        values_update3.put(KEY_tfa_approval_role,"");
        values_update3.put(KEY_tfa_pending_by_name,name);
        values_update3.put(KEY_tfa_pending_by_role,role);*/
//
        int k=db.update(TABLE_TFA_APPROVAL_HISTORY,
                values_update3,
                KEY_tfa_approval_id + " = ?  " ,
                new String[]{apprvloc});

        Log.d("chk",String.valueOf(k));
        if(i>0&&j==loc_ids.size()&&k>0)
        {
            status="sucess";
        }
        db.close();

        return  status;
    }
    public List<Approvaldetails> getOrderApprovalsByTfaListId(String listId) {
        List<Approvaldetails> soaList = new ArrayList<Approvaldetails>();
        Approvaldetails soa = null;
        // Select All Query
        String selectQuery = "SELECT "+KEY_tfa_pending_by_name+","+KEY_tfa_pending_by_role+","+
                KEY_tfa_approval_status+","+KEY_tfa_approval_name+","+
                KEY_tfa_approval_role+" FROM " + TABLE_TFA_APPROVAL_HISTORY +
                " WHERE (" + KEY_tfa_approval_status + " >4 " + " or " +KEY_tfa_approval_status + " =9 )"+ " and "+ KEY_tfa_list_id+"="+listId+" ORDER BY " + KEY_tfa_approval_id +" ASC ";
        Log.d("bdquery", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Approvaldetails approvaldetails=new Approvaldetails(cursor.getString(0),
                        cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getString(4));
                soaList.add(approvaldetails);


            } while (cursor.moveToNext());
        }

        // return contact list
        return soaList;

    }
    public long addPlannerActivity2(ActivityPlanner2 activityPlanner2, String type) {
        int k=0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_used_farmers, activityPlanner2.getUsed_farmers());
            contentValues.put(KEY_non_used_farmers, activityPlanner2.getNon_used_farmers());
            contentValues.put(KEY_actual_no_farmers, activityPlanner2.getActual_no_farmers());
            contentValues.put(KEY_actual__estimation_per_head, activityPlanner2.getActual_estimation_per_head());
            contentValues.put(KEY_actual__total_expences, activityPlanner2.getActual_total_expences());
            contentValues.put(KEY_location_lat_lng, activityPlanner2.getLocation_lat_lang());
            contentValues.put(KEY_owner_name, activityPlanner2.getOwner_name());
            contentValues.put(KEY_owner_number, activityPlanner2.getOwner_number());
            long i=db.update(TABLE_TFA_ACTIVITYLIST ,contentValues, KEY_tfa_list_id + "=?", new String[]{String.valueOf(activityPlanner2.getTfa_list_id())});


            if(i>0)
            {
                k=1;
                Log.d("anil_TABLE_TFA_ACTIVITYLISTsecond",String.valueOf(i));
            }
            if(i<0)
            {
                k=0;
                Log.d("anil_TABLE_TFA_ACTIVITYLISTsecond",String.valueOf(i));
            }

            db.close(); // Closing database connection
        }catch (NullPointerException ex)
        {
            Log.e("error",ex.getMessage());
        }

        // Inserting Row

        return  k;
    }
}
