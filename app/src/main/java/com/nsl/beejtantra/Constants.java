package com.nsl.beejtantra;

import static com.nsl.beejtantra.DatabaseHandler.TABLE_CHANNEL_PREFERENCE;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMMODITY_PRICE;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPETITOR_CHANNEL;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPETITOR_STRENGTH;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROP_SHIFTS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_MARKET_POTENTIAL;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PRODUCT_PRICING_SURVEY;

/**
 * Created by Praveen on 12/5/2016.
 */
public class Constants {
    //base url for retofit
   /* public static final String IMAGE_URL = "http://suprasoftapp.com/nsl_dev/";
    public static final String BASE_URL = "http://suprasoftapp.com/nsl_dev/Api";
    public static final String URL_NSL_MAIN = "http://suprasoftapp.com/nsl_dev/Api/";
    public static final String BASE_URL_SHECHES_IMAGE = "http://www.suprasoftapp.com/uploads/schemas/";
    public static final String B_URL_PRODUCT_CATALOGUE = "http://suprasoftapp.com/nsl_dev/";
*/

    public static final String IMAGE_URL = "http://devapp.nslsaathi.com/";
    public static final String BASE_URL = "http://devapp.nslsaathi.com/Api";
    public static final String URL_NSL_MAIN = "http://devapp.nslsaathi.com/Api/";
    public static final String BASE_URL_SHECHES_IMAGE = "http://devapp.nslsaathi.com/uploads/schemas/";
    public static final String B_URL_PRODUCT_CATALOGUE = "http://devapp.nslsaathi.com/";

   /* public static final String IMAGE_URL = "http://nslbeejtantra.com/";
    public static final String BASE_URL = "http://nslbeejtantra.com/Api";
    public static final String URL_NSL_MAIN = "http://nslbeejtantra.com/Api/";
    public static final String BASE_URL_SHECHES_IMAGE = "http://nslbeejtantra.com/uploads/schemas/";
    public static final String B_URL_PRODUCT_CATALOGUE = "http://nslbeejtantra.com/";*/

    /* public static final String IMAGE_URL = "http://13.127.129.100/";
   public static final String BASE_URL = "http://13.127.129.100/Api";
    public static final String URL_NSL_MAIN = "http://13.127.129.100/Api/";
    public static final String BASE_URL_SHECHES_IMAGE = "http://13.127.129.100/uploads/schemas/";
     public static final String B_URL_PRODUCT_CATALOGUE = "http://13.127.129.100/";*/

    public static final String INTERNET_UNABLEABLE = "Not connected to the internet. Please check your connection and try again.";
    public static final String URL_TABLE_UPDATE = URL_NSL_MAIN + "updatetable";
    public static final String URL_LOGIN = URL_NSL_MAIN + "login";
    public static final String URL_APP_LOGIN = URL_NSL_MAIN + "app_login";
    public static final String URL_CHANGE_PASSWORD = URL_NSL_MAIN + "update_password";
    public static final String URL_CHECKINOUT = "geo_check_inout";
    public static final String URL_ROUTEPATH_UPDATE_INTERVAL = "geo_updatepath";
    public static final String URL_GEO_POLYLINE = "geo_polyline";
    public static final String URL_INSERT_ADVANCEBOOKING = "advbooking";
    public static final String URL_MASTER_SERVICE_ORDER = URL_NSL_MAIN + "service_orderadv?";
    public static final String URL_MASTER_SEASON_MAPPING = URL_NSL_MAIN + "season_mapping";
    public static final String URL_INSERTING_ORDERINDENT = URL_NSL_MAIN + "orderindent";
    public static final String URL_INSERTING_FEEDBACK = URL_NSL_MAIN + "feedback";
    public static final String URL_INSERTING_COMPLAINTS = URL_NSL_MAIN + "complaint";
    public static final String URL_INSERTING_EMP_VISIT_MANAGEMENT = URL_NSL_MAIN + "events";
    public static final String URL_INSERTING_PAYMENT_COLLECTION = URL_NSL_MAIN + "paymentcollection";
    public static final String URL_INSERTING_PUSHTABLE = URL_NSL_MAIN + "tablepush";
    public static final String URL_INSERTING_GEO_PUSH_FULL_PATH = URL_NSL_MAIN + "geopush";
    public static final String URL_INSERTING_DAILYDAIRY = URL_NSL_MAIN + "dailydairy";
    public static final String URL_UPDATING_DAILYDAIRY = URL_NSL_MAIN + "dailydairyedit";
    public static final String URL_UPDATING_PROFILEPIC = URL_NSL_MAIN + "updateprofilepic";
    public static final String URL_ORDER_APPROVAL = URL_NSL_MAIN + "orderapproval";
    public static final String URL_COMPLAINT_APPROVAL = URL_NSL_MAIN + "complaintApproval";
    public static final String URL_INSERT_STOCKMOVEMENT = URL_NSL_MAIN + "stockmovement";
    public static final String URL_INSERT_STOCKMOVEMENT_RETAILER = URL_NSL_MAIN + "stockmovement_retailer";
    public static final String URL_INSERTING_BANK = URL_NSL_MAIN + "addbankdetails";
    public static final String URL_INSERTING_STOCKRETURNS = URL_NSL_MAIN + "stockreturns";
    public static final String URL_INSERTING_MARKET_POTENTIAL = URL_NSL_MAIN + "mi_marketPotential";
    public static final String URL_INSERTING_COMPETITOR_CHANNEL = URL_NSL_MAIN + "mi_competitorChannel";
    public static final String URL_INSERTING_COMMODITY_PRICE = URL_NSL_MAIN + "mi_commodityPrice";
    public static final String URL_INSERTING_CROP_SHIFTS = URL_NSL_MAIN + "mi_cropShifts";
    public static final String URL_INSERTING_CHANNEL_PREFERENCE = URL_NSL_MAIN + "mi_channelPreference";
    public static final String URL_INSERTING_COMPETITOR_STRENGTH = URL_NSL_MAIN + "mi_competitorStrength";
    public static final String URL_INSERTING_PRODUCT_SURVEY=URL_NSL_MAIN+"mi_priceproductSurvey";
    public static final String URL_INSERTING_YIELD_ESTIMATION = URL_NSL_MAIN + "yeild_estimation";


    public static final String URL_MASTER_DIVISIONS = "details?name=division";
    public static final String URL_MASTER_ORDERINTENT_APPROVE_REJECT_CMTS = "details?name=service_order_reject_comments";
    public static final String URL_MASTER_REGIONS = "details?name=region";
    public static final String URL_MASTER_DISTRICTS = "details?name=districts";
    public static final String URL_MASTER_COMPANIES = "details?name=companies";
    public static final String URL_MASTER_CROPS = "details?name=crops";
    public static final String URL_MASTER_PRODUCTS = "details?name=products";
    public static final String URL_MASTER_USER_REGIONS = "details?name=user_regions";
    public static final String URL_MASTER_PRODUCT_PRICE = "details?name=product_price";
    public static final String URL_MASTER_USERS = "user_details?name=users";
    public static final String URL_MASTER_GRADE = "details?name=grade";
    public static final String URL_MASTER_CUSTOMER_BANKDETAILS = "details?name=customer_bankdetails";

    public static final String URL_MASTER_COMPANY_DIVISION_CROP = "details?name=company_division_crop";
    public static final String URL_MASTER_USER_COMPANY_CUSTOMER = "details?name=user_company_customer";
    public static final String URL_MASTER_USER_COMPANY_DIVISION = "user_company_divisions?team=";

    public static final String URL_EMPLOYEE_VISIT_MANAGEMENT = "details?name=employee_visit_management";

    public static final String URL_MASTER_GEO_TRACKING = URL_NSL_MAIN + "details?name=geo_tracking";
    public static final String URL_GEO_TRACKING_ADMIN = URL_NSL_MAIN + "check_out_by_admin?team=";

    public static final String URL_MASTER_COMPLAINTS = "details?name=complaints";
    public static final String URL_MASTER_FEEDBACK = "details?name=feedback";
    public static final String URL_MASTER_DAILY_DAIRY = "details?name=dailydairy";
//    public static final String URL_MASTER_COMMODITY_PRICE = "details?name=mi_commodity_price";
    public static final String URL_MASTER_CROP_SHIFTS = "details?name=mi_crop_shifts";
    public static final String URL_MASTER_PRICE_SURVEY = "details?name=mi_price_survey";
    public static final String URL_MASTER_PRODUCT_SURVEY = "details?name=mi_product_survey";
    public static final String URL_MASTER_SERVICE_ORDER_APPROVAL = "details?name=service_order_approval";
    public static final String URL_MASTER_PLANNER_APPROVAL = "details?name=planner_approval";
    public static final String URL_MASTER_MARKET_POTENTIAL = "details?name=" + TABLE_MARKET_POTENTIAL;
    public static final String URL_MASTER_COMPETITOR_CHANNEL = "details?name=" + TABLE_COMPETITOR_CHANNEL;
    public static final String URL_MASTER_COMPETITOR_STRENGTH = "details?name=" + TABLE_COMPETITOR_STRENGTH;
    public static final String URL_MASTER_CHANNEL_PREFERENCE = "details?name=" + TABLE_CHANNEL_PREFERENCE;
    public static final String URL_MASTER_COMMODITY_PRICE="details?name=" + TABLE_COMMODITY_PRICE;
    public static final String URL_MASTER_CROP_SHIFTS1="details?name=" + TABLE_CROP_SHIFTS;
    public static final String URL_MASTER_PRODUCT_PRICING_SURVEY="details?name=" + TABLE_PRODUCT_PRICING_SURVEY;
    public static final String URL_MASTER_RETAILERS = "details?name=retailers";
    public static final String URL_MASTER_FARMERS = "details?name=farmers";
    public static final String URL_INSERTING_RETAILER = URL_NSL_MAIN + "addretailer";
    public static final String URL_INSERTING_FARMER = URL_NSL_MAIN + "addformer";
    public static final String URL_RETAILER_EXIST = URL_NSL_MAIN + "retailer_exist";

    public static final String NEW_OR_UPDATED_RECORDS_EMPLOYEE_VISIT_MANAGEMENT = Constants.URL_NSL_MAIN + "neworupdated?table=employee_visit_management";
    public static final String NEW_OR_UPDATED_RECORDS_SERVICE_ORDER = Constants.URL_NSL_MAIN + "neworupdated_sales?table=service_order";
    public static final String ACKNOWLEDGE_TO_SERVER = BASE_URL + "/mo_rmsync";
    public static final String GET_CUSTOMERS_AND_CUSTOMER_DETAILS = URL_NSL_MAIN + "customer_details?";
    public static final String GET_PAYMENT_COLLECTION_DETAILS = URL_NSL_MAIN + "payment_collection?team=";
    public static final String ALL_IN_ONE_MASTERS = BASE_URL + "/masterdata";
    public static final String GET_DISTRIBUTOR_RETAILERS = "/distributor_retailers?team=";
    public static final String GET_STOCKMOVEMENT_LIST = "/stockmovement_list?team=";
    public static final String GET_STOCKRETURNS_LIST = "/stockreturns_list?team=";
    public static final String URL_MASTER_SCHEMES = "scheme_regions?user_id=";
    public static final String CHANGE_PASSWORD = URL_NSL_MAIN + "change_password";
    public static final String POST_DEMANDGENERATION ="/get_dg_monitoring";
    public static final String POST_DEMANDGENERATION_lISt="/dg_monitoring_requested_details";
    public static final String POST_FARMERCOUPONS_lISt="/farmer_coupon_data_multiple";
    public static final String POST_FARMERCOUPAN_DATA="/farmer_coupon_data";
    public static final String GET_customer_division_creditlimt="/customer_division_creditlimt";

    public static final boolean isShowNetworkToast = true;
    public static final String MYPREFERENCE = "mypref";
    public static final String USER_ID = "userId";
    public static final String CUSTOM_FONT_PATH_NORMAL = "fonts/SEGOEWP.TTF";
    public static final String CUSTOM_FONT_PATH_BOLD = "fonts/SEGOEWP-SEMIBOLD.TTF";
    public static final String CUSTOM_FONT_PATH_LIGHT = "fonts/SEGOEWP-LIGHT.TTF";
    public static final String CUSTOMER_ID_GEO = "customer_id_geo";
    public static final String URL_LOGOUT = URL_NSL_MAIN + "logout";
    public static final String URL_APP_LOGGED_IN = URL_NSL_MAIN + "app_logged_in";
    public static final String URL_UPDATE_LAST_LOGIN = URL_NSL_MAIN + "update_last_login";
    public static final String URL_GET_TEAM = "getTeam";
    public static final String TEAM = "team";
    public static final String URL_DISTRIBUTOR_TAGGING = URL_NSL_MAIN + "distributor_tagging";
    public static final int MONTHLY = 2;
    public static final int WEEKLY = 1;

    public static String APP_CURRENT_VERSION = "app_current_version";
    public static String API_CURRENT_KEY = "api_current_key";

    public static final String CHECK_IN = "Check-in";
    public static final String CHECKOUT = "Checkout";
    public static final int URL_SIZE_RESTRICTION = 8192;//characters

    //*Tfa
    public static final String URL_TfA_SERVER_TO_LOCAl = "tfa_server_to_local";
    public static final String URL_TFA_ORDER_APPROVAL = URL_NSL_MAIN + "tfa_user_approval";


    public interface Stages {
        int PENDING = 1;
        int PIR_AUTHORIZATION = 2;
        int ANALYSIS = 3;
        int DISPATCH_LOCATIONS = 4;
        int PRODUCTION_DETAILS = 5;
        int PACKING = 6;
        int GAURD_SAMPLE_ANALYSIS = 7;
        int MARKET_SAMPLE_ANALYSIS = 8;
        int QUANTITY = 9;
        int QA_REPORT = 10;
        int COMPLETED = 11;
        int REJECTED = 12;
        String PENDING_STAGE = "Pending";
        String PIR_AUTH_STAGE = "PirAuthorization";
        String ANALYSIS_STAGE = "Analysis";
        String DISPATCH_STAGE = "Dispatch Locations";
        String PRODUCTION_STAGE = "Production Details";
        String PACKING_STAGE = "Packing";
        String GAURD_SAMPLE_STAGE = "Gaurd Sample Analysis";
        String MARKET_SAMPLE_STAGE = "Market Sample Analysis";
        String QUANTITY_STAGE = "Quantity";
        String QA_REPORT_STAGE = "QA Report";
        String COMPLETED_STAGE = "Completed";
        String REJECTED_STAGE = "Rejected";
    }

    public interface ViewTypes {
        int MARKET_POTENTIAL = 0;
        int PRODUCT_PRICING = 1;
        int COMPETITOR_CHANNEL = 2;
        int COMPETITOR_STRENGTH = 3;
        int CHANNEL_PREFERENCE = 4;
        int COMMODITY_PRICE = 5;
        int CROP_SHIFTS = 6;
    }

    public interface ComplaintStatus {
        int OPEN = 0;
        int RESOLVED = 1;
        int CANCELLED = 2;
        int IN_PROGRESS = 3;
        String OPEN_STATUS = "Open";
        String RESOLVED_STATUS = "Resolved";
        String CANCELLED_STATUS = "Cancelled";
        String IN_PROGRESS_STATUS = "In Progress";
    }

    public interface Roles {
        int ROLE_5 = 5;   // RM
        int ROLE_6 = 6;   // AM
        int ROLE_7 = 7;   // MO

        int ROLE_12 = 12; //
        int ROLE_4 = 4;

        int ROLE_17 = 19;
    }

    public interface PaymentTypes {
        int PAYMENT_TYPES_ABS = 1;
        int PAYMENT_TYPES_NORMAL = 2;

    }

    public interface Names {
        String PLANNER = "Planner";
        String DISTRIBUTOR = "Distributors";
        String PRODUCTS = "Products";
        String SCHEMES = "Schemes";
        String ADVANCE_BOOKING = "Advance Booking";
        String ORDER_INDENT = "Order Indent";
        String PAYMENT_COLLECTIONS = "Payment Collections";
        String MARKET_INTELLIGENCE = "Market Intelligence";
        String STOCK_SUPPLY = "Stock Supply";
        String STOCK_RETURNS = "Stock Returns";
        String ROUTE_MAP = "Route Map";
        String FEEDBACK = "Feedback";
        String COMPLAINTS = "Seeds Clinic";
        String DAILY_DIARY = "Daily Diary";
        String YIELD_ESTIMATION = "Yield Estimation";
        String GODOWN = "Stock Godown";
        String GEOTAGGING = "Geotagging";
        String GLOBAL_AGRIGENETICS = "GLOBAL AGRIGENETICS";
        String GLOBAL_REWARDS = "GLOBAL REWARDS";
        String PRODUCT_CATALOGUE = "PRODUCT CATALOGUE";


        String Demand_generate="Demand Generation";
        String Farmer_Coupans="Farmer Coupons";

        String Activity_PLANNER="Activity Planner";
        String Field_Activity="Field Activity";
    }

    public interface SharedPrefrancesKey {
        String ROLE = "role";
        String USER_ID = "userId";
        String NETWORK_CHANGED_LAST_TIME = "network_changed_last_time";
        String CURRENT_DATE = "current_date";
        String IS_DEFAULT_PASSWORD = "is_default_password";
    }

    public interface DateFormat {
        String COMMON_DATE_FORMAT = "yyyy-MM-dd";
        String COMMON_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    public interface MasterTableNames {
        String TABLE_DIVISION = "division";
        String TABLE_REGION = "region";
        String TABLE_COMPANIES = "companies";
        String TABLE_CROPS = "crops";
        String TABLE_PRODUCTS = "products";
        String TABLE_PRODUCT_PRICE = "product_price";
        String TABLE_SCHEMES = "schemes";
        String TABLE_SCHEME_PRODUCTS = "scheme_products";
        String TABLE_USERS = "users";
        String TABLE_GRADE = "grade";
        String TABLE_CUSTOMERS = "customers";
        String TABLE_CUSTOMER_DETAILS = "customer_details";
        String TABLE_CATALOGUE_CROPS = "catalogue_crops";
        String TABLE_CATALOGUE_CROPS_PRODUCTS = "catalogue_crops_products";
    }

    public static final String ANDROID = "Android";
    public static final int SUCCESS = 200;
    public static final String KEY_1 = "key1";
    public static final long CONNECTION_TIME_OUT = 60 * 5;
    public static final long READ_TIME_OUT = 60 * 5;
    public static final long WRITE_TIME_OUT = 60 * 5;
    public static final int ZOOM_LEVEL = 20;

}
