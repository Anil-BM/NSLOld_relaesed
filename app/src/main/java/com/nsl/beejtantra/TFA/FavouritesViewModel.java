package com.nsl.beejtantra.TFA;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_APPROVAL_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CHECK_IN_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_COMMENTS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CONCERN_PERSON_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CREATED_BY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CREATED_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_END_DATE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_PARTICIPANTS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_PURPOSE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_VENUE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FEILD_AREA;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_ADDRESS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_DISTRICT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_TALUKA;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_VILLAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_MOBILE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PLAN_DATE_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PURPOSE_VISIT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_TYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_UPDATED_BY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_UPDATE_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VILLAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_PLAN_TYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_ADDRESS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;

public class FavouritesViewModel extends AndroidViewModel
{
    String datefromcalander, dateString, eventdate, datefromrecords, cname, caddress;
    private DatabaseHandler db;
    private ArrayList<ActivityIndentModel> mFavs;
    SQLiteDatabase sdbw, sdbr;
    SimpleDateFormat sdf;
    DateFormat timeFormat, currenttimeFormat;
    DateFormat dateFormat;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    public FavouritesViewModel(Application application) {
        super(application);
        db = new DatabaseHandler(application);
        long date = System.currentTimeMillis();
        sdf = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        dateString = sdf.format(date);
        SimpleDateFormat sdfs = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        eventdate = sdfs.format(date);
        dateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        currenttimeFormat = new SimpleDateFormat("h:mm:ss aa");
        datefromcalander = "2019-09-23";

    }

    public List<ActivityIndentModel> getFavs() {
        if (mFavs == null) {
            mFavs = new ArrayList<>();

          //  createDummyList();
            loadFavs();
        }
        ArrayList<ActivityIndentModel> clonedFavs = new ArrayList<>(mFavs.size());
        for (int i = 0; i < mFavs.size(); i++) {
            clonedFavs.add(new ActivityIndentModel(mFavs.get(i)));
        }
        return clonedFavs;
    }

    /*public void createDummyList() {

        addFav("https://www.journaldev.com", (new Date()).getTime());
        addFav("https://www.medium.com", (new Date()).getTime());
        addFav("https://www.reddit.com", (new Date()).getTime());
        addFav("https://www.github.com", (new Date()).getTime());
        addFav("https://www.hackerrank.com", (new Date()).getTime());
        addFav("https://www.developers.android.com", (new Date()).getTime());
    }*/

    private void loadFavs() {

        mFavs.clear();


        String selectQuery = "SELECT DISTINCT "
                + KEY_EMP_CONCERN_PERSON_NAME + ","
                + KEY_EMP_VISIT_PLAN_TYPE + ","
                + KEY_EMP_STATUS + ","
                + KEY_EMP_PLAN_DATE_TIME + ","
                + KEY_EMP_PURPOSE_VISIT + ","
                + KEY_EMP_TYPE + ","
                + KEY_EMP_GEO_TRACKING_ID + ","
                + KEY_EMP_VISIT_USER_ID + ","
                + KEY_EMP_VISIT_CUSTOMER_ID + ","
                + KEY_EMP_MOBILE + ","
                + KEY_EMP_VILLAGE + ","
                + KEY_EMP_LOCATION_ADDRESS + ","
                + KEY_EMP_LOCATION_DISTRICT + ","
                + KEY_EMP_LOCATION_TALUKA + ","
                + KEY_EMP_LOCATION_VILLAGE + ","
                + KEY_EMP_FEILD_AREA + ","
                + KEY_EMP_CHECK_IN_TIME + ","
                + KEY_EMP_COMMENTS + ","
                + KEY_EMP_APPROVAL_STATUS + ","
                + KEY_EMP_EVENT_END_DATE + ","
                + KEY_EMP_EVENT_PURPOSE + ","
                + KEY_EMP_VISIT_MASTER_ID + ","
                + KEY_EMP_EVENT_VENUE + ","
                + KEY_EMP_EVENT_PARTICIPANTS + ","
                + KEY_EMP_FFM_ID + ","
                + KEY_EMP_CREATED_BY + ","
                + KEY_EMP_UPDATED_BY + ","
                + KEY_EMP_CREATED_DATETIME + ","
                + KEY_EMP_UPDATE_DATETIME + ","
                + KEY_EMP_EVENT_NAME + ","
                + KEY_EMP_VISIT_ID
                + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " = " + 861;

        sdbw = db.getWritableDatabase();

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        System.out.println("selectQuery  " + selectQuery);

        if (cursor.moveToFirst()) {
            do {
                datefromrecords = cursor.getString(3).substring(0, 10);

                //Toast.makeText(getApplicationContext(), datefromrecords, Toast.LENGTH_SHORT).show();

                HashMap<String, String> map = new HashMap<String, String>();
                try {
                    if (sdf.parse(datefromcalander).equals(sdf.parse(datefromrecords))) {
                        // If two dates are equal.

                        Log.e("++Dates++", datefromcalander + "&" + datefromrecords);
                        // System.out.println("+++++++++++" + cursor.getString(0)+ cursor.getString(1)+ cursor.getString(2)+ cursor.getString(3));
                        String planvisittype = cursor.getString(1);
                        String emptype = cursor.getString(5);
                        if (planvisittype.equalsIgnoreCase("1")) {
                            String cid = cursor.getString(8);


                            String selectcQuery = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_ADDRESS + " FROM " + TABLE_CUSTOMERS + "  where " + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + cid;
                            // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                            sdbw = db.getWritableDatabase();

                            Cursor ccursor = sdbw.rawQuery(selectcQuery, null);
                            System.out.println("ccursor count " + ccursor.getCount());

                            if (ccursor.moveToFirst()) {
                                do {
                                    cname = ccursor.getString(0);
                                    caddress = ccursor.getString(1);

                                } while (ccursor.moveToNext());
                            } else {
                                Log.e("no plans", "no plans");
                            }

                            map.put("event_name", cname);
                            map.put("address", caddress);
                            map.put("event_status", cursor.getString(2));
                            map.put("location_district", cursor.getString(12));
                            map.put("location_taluka", cursor.getString(13));
                            map.put("location_village", cursor.getString(14));
                            map.put("event_approval_status", cursor.getString(18));
                            map.put("event_customer_id", cursor.getString(8));
                            map.put("event_datetime", cursor.getString(3));
                            map.put("event_purpose", cursor.getString(4));
                            map.put("event_id", cursor.getString(30));
                            map.put("emp_visit_id", cursor.getString(24));

                        } else if (emptype.equalsIgnoreCase("2")) {
                            Log.e("Type : ", "Event");

                            map.put("event_name", cursor.getString(29));
                            map.put("address", cursor.getString(22));
                            map.put("event_status", cursor.getString(2));
                            map.put("event_approval_status", cursor.getString(18));
                            map.put("event_customer_id", cursor.getString(8));
                            map.put("event_datetime", cursor.getString(3));
                            map.put("event_purpose", cursor.getString(20));
                            map.put("event_id", cursor.getString(30));
                            map.put("emp_visit_id", cursor.getString(24));
                        } else {
                            // adding each child node to HashMap key => value
                            map.put("event_name", cursor.getString(0));
                            map.put("address", cursor.getString(11));
                            map.put("location_district", cursor.getString(12));
                            map.put("location_taluka", cursor.getString(13));
                            map.put("location_village", cursor.getString(14));
                            map.put("event_status", cursor.getString(2));
                            map.put("event_customer_id", cursor.getString(8));
                            map.put("event_datetime", cursor.getString(3));
                            map.put("event_purpose", cursor.getString(4));
                            map.put("event_id", cursor.getString(30));
                            map.put("event_approval_status", cursor.getString(18));
                            map.put("emp_visit_id", cursor.getString(24));
                        }

                       // favouriteItem.add(map);
                        mFavs.add(new ActivityIndentModel(cursor.getString(29), cursor.getString(29), cursor.getString(29)));

                               /* Log.e("---inserted values --", "event_name :" + cursor.getString(0)
                                        + "\n visit_plaanetype" + cursor.getString(1)
                                        + "\n event_status" + cursor.getString(2)
                                        + "\n event_datetime" + cursor.getString(3)
                                        + "\n event_purpose" + cursor.getString(4)
                                        + "\n type :" + cursor.getString(5)
                                        + "\n geoid :" + cursor.getString(6)
                                        + "\n userid :" + cursor.getString(7)
                                        + "\n event_customer_id" + cursor.getString(8)
                                        + "\n mobile :" + cursor.getString(9)
                                        + "\n village :" + cursor.getString(10)
                                        + "\n location address :" + cursor.getString(11)
                                        + "\n field area :" + cursor.getString(12)
                                        + "\n checkin :" + cursor.getString(13)
                                        + "\n comments" + cursor.getString(14)
                                        + "\n approval status" + cursor.getString(15)
                                        + "\n Event date" + cursor.getString(16)
                                        + "\n Event purpose" + cursor.getString(17)
                                        + "\n masterid :" + cursor.getString(18)
                                        + "\n event venue :" + cursor.getString(19)
                                        + "\n event participents :" + cursor.getString(20)
                                        + "\n ffmid :" + cursor.getString(21)
                                        + "\n createdby :" + cursor.getString(22)
                                        + "\n updatedby :" + cursor.getString(23)

                                        + "\n cdatetime :" + cursor.getString(24)
                                        + "\n udatetime :" + cursor.getString(25)
                                        + "\n eventname :" + cursor.getString(26)
                                        + "\n event id :" + cursor.getString(27));
*/
                    } else {

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());


        } else {
            Log.e("no plans", "no plans");
        }
    }

  /*  public Favourites addFav(String url, long date) {

        Log.d("API123", "addFav " + url);

        SQLiteDatabase db = mFavHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbSettings.DBEntry.COL_FAV_URL, url);
        values.put(DbSettings.DBEntry.COL_FAV_DATE, date);
        long id = db.insertWithOnConflict(DbSettings.DBEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        Favourites fav = new Favourites(id, url, date);
        mFavs.add(fav);
        return new Favourites(fav);
    }

    public void removeFav(long id) {
        SQLiteDatabase db = mFavHelper.getWritableDatabase();
        db.delete(
                DbSettings.DBEntry.TABLE,
                DbSettings.DBEntry._ID + " = ?",
                new String[]{Long.toString(id)}
        );
        db.close();

        int index = -1;
        for (int i = 0; i < mFavs.size(); i++) {
            Favourites favourites = mFavs.get(i);
            if (favourites.mId == id) {
                index = i;
            }
        }
        if (index != -1) {
            mFavs.remove(index);
        }
    }*/

}
