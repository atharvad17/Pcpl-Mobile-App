package com.techinvest.pcpl.parserhelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.techinvest.pcpl.model.OfflineSynStatus;
import com.techinvest.pcpl.model.Offlinedatamodel;
import com.techinvest.pcpl.model.SetOfflinedata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

    /**
     * @author Sandip
     */

    // The Android's default system path of your application database.
    @SuppressLint("SdCardPath")

    private static String DB_PATH = null;

    private static String DB_NAME = "pcpldemo4.sqlite";
    //private static String DB_NAME = "pcplpunetest.sqlite";
    private static int DB_VERSION = 5;


    private SQLiteDatabase sqDatabase;

    private final Context myContext;


    //private static final String TABLE_FAVORITE ="locationdetail";

    private static final String TABLE_LOCATION = "locationdetailtab";
    private static final String TABLE_REQUEST = "requestdetailtab";
    private static final String TABLE_PROJECT = "projectdetailtab";
    private static final String TABLE_BUILDING = "buildingtab";
    private static final String TABLE_FLAT = "flatdetailtab";
    private static final String TABLE_ROOM = "roomdetailtab";
    private static final String TABLE_CARPET = "carpetdetailtab";
    private static final String TABLE_FLOWERBED = "flowerbedtab";
    private static final String TABLE_IMAGE = "imagetab";//09
    private static final String TABLE_SYSOFFLINE = "synofflinestatus";

    // requestdetail Table Columns
    private static final String REQUEST_ID = "requestid";
    private static final String PRIM_ID = "_id";
    private static final String REQUEST_DETAIL = "requestdetail";
    private static final String REQUEST_UPDATEDATA = "requestupdate";
    // Location table
    private static final String LOCATION_REQUESTID = "requestid";
    private static final String LOCATION_DETAIL = "locationdetaildata";
    private static final String LOCATION_UPDATEDATA = "locationupdate";

    //projectdetail
    private static final String PROJECT_DETAIL = "projectdetail";
    private static final String PROJECT_REQUESTID = "projectrequestid";
    private static final String PROJECT_UPDATEDATA = "projectupdate";

    // building detail
    private static final String BUILDING_DETAIL = "buildingdata";
    private static final String BUILDING_REQUESTID = "buildingrequestid";
    private static final String BUILDING_UPDATEDATA = "buildingupdate";

    // flatdetail
    private static final String FLAT_DETAIL = "flatdata";
    private static final String FLAT_REQUESTID = "flatrequestid";
    private static final String FLAT_UPDATEDATA = "flatupdate";

    // roomdetail
    private static final String ROOM_DETAIL = "roomdata";
    private static final String ROOM_REQUESTID = "roomrequestid";
    private static final String ROOM_UPDATEDATA = "roomdataupdate";


    // carpet detail
    private static final String CARPET_DETAIL = "carpetdata";
    private static final String CARPET_REQUESTID = "carpetrequestid";
    private static final String CARPET_UPDATEDATA = "carpetupdate";

    //flowerbed detail
    private static final String FLOWERBED_DETAIL = "flowerbeddata";
    private static final String FLOWERBED_REQUESTID = "flowerrequestid";
    private static final String FLOWERBED_UPDATEDATA = "flowerupdate";

    // imagedetail table
    private static final String IMAGE_DETAIL = "imageurlpath";
    private static final String IMAGE_REQUESTID = "imagerequestid";
    private static final String IMAGE_USERID = "imageuserid";
    private static final String IMAGE_UPDATEDATA = "imageupdatelink";
    private static final String IMAGE_ISUPLOAD = "isupload";

    //private static final String COL_DATE = "currentdate";
    //private static final String UPDATE_DATATOSERVER = "export";
    private static final String ROOMDETAIL_ID = "roomdetailid";
    private static final String CARPETDETAIL_ID = "carpetdetailid";
    private static final String FLOWERBED_ID = "flowerdetailid";

    // imagedetail
    private static final String pri_id = "id";
    private static final String SYSOFFLINE_REQUESTID = "requestid";
    private static final String SYSOFFLINE_STATUS = "offlinestatus";


    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     */

    public DataBaseHelper(Activity activity) {
        //super(activity, DB_NAME, null, DB_VERSION);
        //this.myContext = activity;
        //DB_PATH = "/data/data/" + activity.getPackageName() + "/"+ "databases/";

        /*if (android.os.Build.VERSION.SDK_INT >= 4.2) {
            DB_PATH = myContext.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + activity.getPackageName() + "/" + "databases/";
        }*/
        //DB_PATH = "/data/data/"+activity.getPackageName()+"/databases/";

        super(activity, DB_NAME, null, DB_VERSION);// 1? its Database Version
        if(android.os.Build.VERSION.SDK_INT >= 4.2){
            DB_PATH = activity.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + activity.getPackageName() + "/databases/";
        }
        this.myContext = activity;

    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Toast.makeText(myContext, "Local db Found", Toast.LENGTH_LONG).show();
        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();
            // this.getWritableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {

                // throw new Error("Error copying database");
                e.printStackTrace();

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
             File dbfile = new File(myPath);
            checkDB = SQLiteDatabase.openDatabase(dbfile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);

            Log.e(DataBaseHelper.class.getName(), myPath+"\n"+checkDB);

        } catch (SQLiteException e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[4096];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()) {
            sqDatabase = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            sqDatabase.disableWriteAheadLogging();
        }

        Log.e(DataBaseHelper.class.getName(), myPath+"\n"+sqDatabase.isOpen());

    }

	/*
     * public DatabaseHelper open() throws SQLException { sqliteDatabase =
	 * getWritableDatabase(); return this; }
	 */

    @Override
    public synchronized void close() {

        if (sqDatabase != null)
            sqDatabase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // database.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (newVersion == 1) {
//
//        }
        if (newVersion>oldVersion){
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Add your public helper methods to access and get content from the
    // database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd
    // be easy
    // to you to create adapters for your views.

    // insert into product
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //Insert 'now' as the date

    public Long insertSysOfflineStatus(String requestid, String status) {
        // sqDatabase = this.getReadableDatabase();
        if (!checkOfflineStatus(requestid, status)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SYSOFFLINE_REQUESTID, requestid);
            contentValues.put(SYSOFFLINE_STATUS, status);

            return sqDatabase.insert(TABLE_SYSOFFLINE, null, contentValues);
        }
        return null;
    }

    // Check for availability of Building
    public List<OfflineSynStatus> getSysOfflineStatus() {
        // Log.d("Looking for product", productId);
        List<OfflineSynStatus> statusData = new ArrayList<OfflineSynStatus>();
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = " SELECT * FROM " + TABLE_SYSOFFLINE + " WHERE "+ CARPET_REQUESTID + "='" + productId + "'";
        String selectQuery = "SELECT  * FROM " + TABLE_SYSOFFLINE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OfflineSynStatus data = new OfflineSynStatus();
                data.setRequestId(cursor.getString(cursor.getColumnIndex(SYSOFFLINE_REQUESTID)));
                data.setSynOfflineStatus(cursor.getString(cursor.getColumnIndex(SYSOFFLINE_STATUS)));
                statusData.add(data);
            } while (cursor.moveToNext());
        }
        return statusData;
    }

    public Long insertdata(String result, String requestid) {
        // sqDatabase = this.getReadableDatabase();
        if (!checkDatalist(result, requestid)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(REQUEST_ID, requestid);
            contentValues.put(REQUEST_DETAIL, result);
            contentValues.put(REQUEST_UPDATEDATA, result);
            //contentValues.put(COL_DATE, dateFormat.format(new Date()));
            //contentValues.put(UPDATE_DATATOSERVER, "NO");

            return sqDatabase.insert(TABLE_REQUEST, null, contentValues);
        }
        return null;
    }

    public Long insertLocationdata(String result, String requestid) {
        // sqDatabase = this.getReadableDatabase();
        if (!checkDatalistLocation(result, requestid)) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(LOCATION_REQUESTID, requestid);
            contentValues.put(LOCATION_DETAIL, result);
            contentValues.put(LOCATION_UPDATEDATA, result);
            //contentValues.put(COL_DATE, dateFormat.format(new Date()));
            //contentValues.put(UPDATE_DATATOSERVER, "NO");

            return sqDatabase.insert(TABLE_LOCATION, null, contentValues);
        } else {
            /*ContentValues contentValues = new ContentValues();

			//contentValues.put(LOCATION_REQUESTID, requestid);
			contentValues.put(LOCATION_DETAIL, result);
			//contentValues.put(LOCATION_UPDATEDATA, result);
			 // return  sqDatabase.update(TABLE_FAVORITE, contentValues, LOCATION_DETAIL + " = '" + result + "'", null);
			  return (long) sqDatabase.update(TABLE_LOCATION, contentValues, LOCATION_DETAIL + " = '" + result + "'", null);*/
        }

        return null;
    }

    public Long insertProjectdata(String result, String requestid) {

        if (!checkProjectDatalist(result, requestid)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PROJECT_REQUESTID, requestid);
            contentValues.put(PROJECT_DETAIL, result);
            contentValues.put(PROJECT_UPDATEDATA, result);
            //contentValues.put(COL_DATE, dateFormat.format(new Date()));
            //contentValues.put(UPDATE_DATATOSERVER, "NO");

            return sqDatabase.insert(TABLE_PROJECT, null, contentValues);
        }
        return null;
    }

    private boolean checkProjectDatalist(String result, String requestid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_PROJECT + " WHERE " + PROJECT_REQUESTID + "='" + requestid + "'";
        Cursor cursor = db.rawQuery(Query, null);

        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();

            return true;
        }
        return false;
    }

    // Check for availability of product
    public List<Offlinedatamodel> getProjectDetail(String productId) {
        // Log.d("Looking for product", productId);
        List<Offlinedatamodel> projectData = new ArrayList<Offlinedatamodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_PROJECT + " WHERE "
                + PROJECT_REQUESTID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                projectData.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(PROJECT_REQUESTID)), cursor.getString(cursor.getColumnIndex(PROJECT_DETAIL))));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();
        return projectData;


    }

    private boolean checkDatalistLocation(String result, String requestid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATION, new String[]{LOCATION_REQUESTID,
                        LOCATION_DETAIL}, LOCATION_REQUESTID +
                        "='" + requestid + "'" + " AND " + LOCATION_DETAIL + "='" + result + "'", null,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();
            return true;
        }
        return false;
    }

    // Check Wish List for duplicate
    public boolean checkOfflineStatus(String requestid, String status) {
        SQLiteDatabase db = this.getReadableDatabase();


        String Query = "SELECT * FROM " + TABLE_SYSOFFLINE + " WHERE " + SYSOFFLINE_REQUESTID + "='" + requestid + "'";
        Cursor cursor = db.rawQuery(Query, null);

        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();

            return true;
        }
        return false;
    }

    // Check Wish List for duplicate
    public boolean checkDatalist(String result, String requestid) {
        SQLiteDatabase db = this.getReadableDatabase();

		/*  Cursor cursor = db.query(TABLE_FAVORITE, new String[] { REQUEST_ID,
          REQUEST_DETAIL }, REQUEST_ID+
		  "='"+requestid+"'"+" AND "+REQUEST_DETAIL+"="+result+"" , null,
		  null, null, null);*/
        String Query = "SELECT * FROM " + TABLE_REQUEST + " WHERE " + REQUEST_ID + "='" + requestid + "'";
        Cursor cursor = db.rawQuery(Query, null);

        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();

            return true;
        }
        return false;
    }

    // Check for availability of product
    public List<Offlinedatamodel> getRequestDetail(String productId) {

        // Log.d("Looking for product", productId);
        List<Offlinedatamodel> requestData = new ArrayList<Offlinedatamodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_REQUEST + " WHERE "
                + REQUEST_ID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                requestData.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(REQUEST_ID)), cursor.getString(cursor.getColumnIndex(REQUEST_DETAIL))));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();
        return requestData;


    }

    public List<Offlinedatamodel> getLocationdata(String requestid) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Offlinedatamodel> locationdataList = new ArrayList<Offlinedatamodel>();

        // Cursor cursor = db.query(TABLE_FAVORITE, new String[] { REQUEST_ID }, REQUEST_ID+ "=?", new String[] { requestid }, null, null, null);
        String query = " SELECT * FROM " + TABLE_LOCATION + " WHERE "
                + LOCATION_REQUESTID + "='" + requestid + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                locationdataList.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(LOCATION_REQUESTID)), cursor.getString(cursor.getColumnIndex(LOCATION_DETAIL))));
                //locationdataList.add( new Offlinedatamodel (cursor.getString(1),cursor.getString(2)));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();

        return locationdataList;

    }

    // Bulding Request method
    public Long insertBuildingdata(String result, String requestid) {

        if (!checkBuildingDatalist(result, requestid)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BUILDING_REQUESTID, requestid);
            contentValues.put(BUILDING_DETAIL, result);
            contentValues.put(BUILDING_UPDATEDATA, result);
            //contentValues.put(COL_DATE, dateFormat.format(new Date()));
            //contentValues.put(UPDATE_DATATOSERVER, "NO");

            return sqDatabase.insert(TABLE_BUILDING, null, contentValues);
        }
        return null;
    }

    private boolean checkBuildingDatalist(String result, String requestid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_BUILDING + " WHERE " + BUILDING_REQUESTID + "='" + requestid + "'";
        Cursor cursor = db.rawQuery(Query, null);

        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();

            return true;
        }
        return false;
    }

    // Check for availability of Building
    public List<Offlinedatamodel> getBuildingDetail(String productId) {
        // Log.d("Looking for product", productId);
        List<Offlinedatamodel> buildData = new ArrayList<Offlinedatamodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_BUILDING + " WHERE "
                + BUILDING_REQUESTID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                buildData.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(BUILDING_REQUESTID)), cursor.getString(cursor.getColumnIndex(BUILDING_DETAIL))));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();
        return buildData;
    }

    // flat detail request
    public Long insertFlatdata(String result, String requestid) {

        if (!checkFlatDatalist(result, requestid)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FLAT_REQUESTID, requestid);
            contentValues.put(FLAT_DETAIL, result);
            contentValues.put(FLAT_UPDATEDATA, result);
            //contentValues.put(COL_DATE, dateFormat.format(new Date()));
            //contentValues.put(UPDATE_DATATOSERVER, "NO");

            return sqDatabase.insert(TABLE_FLAT, null, contentValues);
        }
        return null;
    }

    private boolean checkFlatDatalist(String result, String requestid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_FLAT + " WHERE " + FLAT_REQUESTID + "='" + requestid + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();
            return true;
        }
        return false;
    }

    // Check for availability of Building
    public List<Offlinedatamodel> getFlatDetail(String productId) {
        // Log.d("Looking for product", productId);
        List<Offlinedatamodel> flatData = new ArrayList<Offlinedatamodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_FLAT + " WHERE "
                + FLAT_REQUESTID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                flatData.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(FLAT_REQUESTID)), cursor.getString(cursor.getColumnIndex(FLAT_DETAIL))));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();
        return flatData;
    }

    // Room detail request
    public Long insertRoomdata(String result, String requestid, String roomdetailid) {

        if (!checkRoomDatalist(result, requestid, roomdetailid)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ROOM_REQUESTID, requestid);
            contentValues.put(ROOM_DETAIL, result);
            contentValues.put(ROOM_UPDATEDATA, result);
            //contentValues.put(COL_DATE, dateFormat.format(new Date()));
            //contentValues.put(UPDATE_DATATOSERVER, "NO");
            contentValues.put(ROOMDETAIL_ID, roomdetailid);

            return sqDatabase.insert(TABLE_ROOM, null, contentValues);
        }
        return null;
    }

    private boolean checkRoomDatalist(String result, String requestid, String roomdetailid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_ROOM + " WHERE " + ROOMDETAIL_ID + "='" + roomdetailid + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();
            return true;
        }
        return false;
    }

    // Check for availability of Building
    public List<Offlinedatamodel> getRoomDetail(String productId) {
        // Log.d("Looking for product", productId);
        List<Offlinedatamodel> roomData = new ArrayList<Offlinedatamodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_ROOM + " WHERE "
                + ROOM_REQUESTID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                roomData.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(ROOM_REQUESTID)), cursor.getString(cursor.getColumnIndex(ROOM_DETAIL))));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();
        return roomData;
    }

    // carpet data
    public Long insertCarpetdata(String result, String requestid, String roomCalDetailID) {

        if (!checkCarpetDatalist(result, requestid, roomCalDetailID)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CARPET_REQUESTID, requestid);
            contentValues.put(CARPET_DETAIL, result);
            contentValues.put(CARPET_UPDATEDATA, result);
            contentValues.put(CARPETDETAIL_ID, roomCalDetailID);

            return sqDatabase.insert(TABLE_CARPET, null, contentValues);
        }
        return null;
    }

    private boolean checkCarpetDatalist(String result, String requestid, String roomCalDetailID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_CARPET + " WHERE " + CARPETDETAIL_ID + "='" + roomCalDetailID + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();
            return true;
        }
        return false;
    }

    // Check for availability of Building
    public List<Offlinedatamodel> getCarpetDetail(String productId) {
        // Log.d("Looking for product", productId);
        List<Offlinedatamodel> carpetData = new ArrayList<Offlinedatamodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_CARPET + " WHERE "
                + CARPET_REQUESTID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                carpetData.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(CARPET_REQUESTID)), cursor.getString(cursor.getColumnIndex(CARPET_DETAIL))));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();
        return carpetData;
    }

    // flowerbed data
    public Long insertFlowerbeddata(String result, String requestid, String roomCalDetailID) {

        if (!checkFlowerbedDatalist(result, requestid, roomCalDetailID)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FLOWERBED_REQUESTID, requestid);
            contentValues.put(FLOWERBED_DETAIL, result);
            contentValues.put(FLOWERBED_UPDATEDATA, result);
            contentValues.put(FLOWERBED_ID, roomCalDetailID);
            return sqDatabase.insert(TABLE_FLOWERBED, null, contentValues);
        }
        return null;
    }

    private boolean checkFlowerbedDatalist(String result, String requestid, String roomCalDetailID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_FLOWERBED + " WHERE " + FLOWERBED_ID + "='" + roomCalDetailID + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();
            return true;
        }
        return false;
    }

    // Check for availability of Building
    public List<Offlinedatamodel> getFlowerbedDetail(String productId) {
        // Log.d("Looking for product", productId);
        List<Offlinedatamodel> flowerbedData = new ArrayList<Offlinedatamodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_FLOWERBED + " WHERE "
                + FLOWERBED_REQUESTID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                flowerbedData.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(FLOWERBED_REQUESTID)), cursor.getString(cursor.getColumnIndex(FLOWERBED_DETAIL))));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();
        return flowerbedData;
    }

    // update requestdata
    public boolean updateRequestdata(String requestId, String getstrdata, String savedata) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_ID, requestId);
        //contentValues.put(REQUEST_DETAIL, getstrdata);
        contentValues.put(REQUEST_UPDATEDATA, savedata);

        return db.update(TABLE_REQUEST, contentValues, REQUEST_ID + "='" + requestId + "'", null) > 0;
    }

    // update locationdata
    public boolean updateLocationdata(String requestId, String getstrdata, String savedata) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_REQUESTID, requestId);
        //contentValues.put(REQUEST_DETAIL, getstrdata);
        contentValues.put(LOCATION_UPDATEDATA, savedata);

        return db.update(TABLE_LOCATION, contentValues, LOCATION_REQUESTID + "='" + requestId + "'", null) > 0;
    }

    // update locationdata
    public boolean updateFlowerbeddata(String requestId, String roomCalDetailID, String savedata) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLOWERBED_REQUESTID, requestId);
        contentValues.put(FLOWERBED_DETAIL, savedata);
        contentValues.put(FLOWERBED_UPDATEDATA, savedata);
        contentValues.put(FLOWERBED_ID, roomCalDetailID);

        if (db.update(TABLE_FLOWERBED, contentValues, FLOWERBED_ID + "='" + roomCalDetailID + "'", null) != 0)
            return true;
        else
            return false;
    }

    // update projectdata
    public boolean updateProjectdata(String requestId, String getstrdata, String savedata) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROJECT_REQUESTID, requestId);
        //contentValues.put(REQUEST_DETAIL, getstrdata);
        contentValues.put(PROJECT_UPDATEDATA, savedata);

        return db.update(TABLE_PROJECT, contentValues, PROJECT_REQUESTID + "='" + requestId + "'", null) > 0;
    }

    // update locationdata
    public boolean updateBuildingdata(String requestId, String getstrdata, String savedata) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUILDING_REQUESTID, requestId);
        //contentValues.put(REQUEST_DETAIL, getstrdata);
        contentValues.put(BUILDING_UPDATEDATA, savedata);

        return db.update(TABLE_BUILDING, contentValues, BUILDING_REQUESTID + "='" + requestId + "'", null) > 0;
    }

    // update flatdata
    public boolean updateFlatdata(String requestId, String getstrdata, String savedata) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLAT_REQUESTID, requestId);
        //contentValues.put(REQUEST_DETAIL, getstrdata);
        contentValues.put(FLAT_UPDATEDATA, savedata);

        return db.update(TABLE_FLAT, contentValues, FLAT_REQUESTID + "='" + requestId + "'", null) > 0;

    }

    // update locationdata
    public boolean updateRoomdata(String requestId, String roomdetailid, String savedata) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROOM_REQUESTID, requestId);
        contentValues.put(ROOMDETAIL_ID, roomdetailid);
        contentValues.put(ROOM_UPDATEDATA, savedata);

        if (db.update(TABLE_ROOM, contentValues, ROOMDETAIL_ID + "='" + roomdetailid + "'", null) != 0)
            return true;
        else

            return false;
    }

    // update carpetdata
    public boolean updateCarpetdata(String requestId, String roomCalDetailID, String savedata) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CARPET_REQUESTID, requestId);
        contentValues.put(CARPET_DETAIL, savedata);
        contentValues.put(CARPET_UPDATEDATA, savedata);
        contentValues.put(CARPETDETAIL_ID, roomCalDetailID);

        if (db.update(TABLE_CARPET, contentValues, CARPETDETAIL_ID + "='" + roomCalDetailID + "'", null) != 0)
            return true;
            //	}
        else
            return false;
    }

    public List<SetOfflinedata> getRequestAllvalues(String RequestID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query

        String selectQuery = "";
        if (RequestID == "") {
            selectQuery = "SELECT  * FROM " + TABLE_REQUEST;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_REQUEST + " Where " + REQUEST_ID + " = '" + RequestID + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(REQUEST_ID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(REQUEST_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getLocationAllvalues(String LocationID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        String selectQuery = "";
        if (LocationID == "") {

            selectQuery = "SELECT  * FROM " + TABLE_LOCATION;

        } else {
            selectQuery = "SELECT  * FROM " + TABLE_LOCATION + " Where " + LOCATION_REQUESTID + "= '" + LocationID + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(LOCATION_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(LOCATION_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getProjectAllvalues(String ProjectID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_PROJECT;
        String selectQuery = "";
        if (ProjectID == "") {

            selectQuery = "SELECT  * FROM " + TABLE_PROJECT;

        } else {
            selectQuery = "SELECT  * FROM " + TABLE_PROJECT + " Where " + PROJECT_REQUESTID + "= '" + ProjectID + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(PROJECT_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(PROJECT_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getBuildingAllvalues(String BuildingID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_BUILDING;

        String selectQuery = "";
        if (BuildingID == "") {

            selectQuery = "SELECT  * FROM " + TABLE_BUILDING;

        } else {
            selectQuery = "SELECT  * FROM " + TABLE_BUILDING + " Where " + BUILDING_REQUESTID + " = '" + BuildingID + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(BUILDING_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(BUILDING_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getFlatAllvalues(String FlatID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_FLAT;
        String selectQuery = "";
        if (FlatID == "") {

            selectQuery = "SELECT  * FROM " + TABLE_FLAT;

        } else {
            selectQuery = "SELECT  * FROM " + TABLE_FLAT + " Where " + FLAT_REQUESTID + " = '" + FlatID + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(FLAT_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(FLAT_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getRoomAllvalues(String RoomID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_ROOM;
        String selectQuery = "";
        if (RoomID == "") {

            selectQuery = "SELECT  * FROM " + TABLE_ROOM;

        } else {
            selectQuery = "SELECT  * FROM " + TABLE_ROOM + " Where " + ROOM_REQUESTID + "= '" + RoomID + "'";
        }


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(ROOM_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(ROOM_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getRoomRequestIdvalues(String requestId) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_ROOM + " WHERE "
                + ROOM_REQUESTID + "='" + requestId + "'";
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(ROOM_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(ROOM_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getCarpetAllvalues(String CarpetID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        //
        String selectQuery = "";
        if (CarpetID == "") {

            selectQuery = "SELECT  * FROM " + TABLE_CARPET;

        } else {
            selectQuery = "SELECT  * FROM " + TABLE_CARPET + " Where " + CARPET_REQUESTID + "='" + CarpetID + "'";
        }


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(CARPET_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(CARPET_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getCarpetRequestIdvalues(String RequestID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        // String selectQuery = "SELECT  * FROM " + TABLE_CARPET;


        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_CARPET + " WHERE "
                + CARPET_REQUESTID + "='" + RequestID + "'";
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(CARPET_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(CARPET_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getFlowerbedAllvalues(String FlowerbedID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        //

        String selectQuery = "";
        if (FlowerbedID == "") {

            selectQuery = "SELECT  * FROM " + TABLE_FLOWERBED;

        } else {
            selectQuery = "SELECT  * FROM " + TABLE_FLOWERBED + " Where " + FLOWERBED_REQUESTID + "='" + FlowerbedID + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(FLOWERBED_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(FLOWERBED_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getFlowerbedRequestIdvalues(String RequestID) {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        //  String selectQuery = "SELECT  * FROM " + TABLE_FLOWERBED;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_FLOWERBED + " WHERE "
                + FLOWERBED_REQUESTID + "='" + RequestID + "'";
        Cursor cursor = db.rawQuery(query, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(FLOWERBED_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(FLOWERBED_UPDATEDATA)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    // for date query
    //  String sql = "DELETE FROM TABLE_ROOM WHERE Save_Date <= date('now','-1 day')";
    //  db.execSQL(sql);


    public boolean DeleteAllRecord() {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
                                 /* db.delete(TABLE_REQUEST, null, null);
							      db.delete(TABLE_LOCATION, null, null);
							      db.delete(TABLE_PROJECT, null, null);
							      db.delete(TABLE_BUILDING, null, null);
							      db.delete(TABLE_FLAT, null, null);
							      db.delete(TABLE_ROOM, null, null);
							      db.delete(TABLE_CARPET, null, null);
							      db.delete(TABLE_FLOWERBED, null, null);
							      db.close();*/

        // SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(Database.DATABASE_NAME, Context.MODE_PRIVATE, null, null);
        db.execSQL("DELETE FROM " + TABLE_REQUEST);
        db.execSQL("DELETE FROM " + TABLE_LOCATION);
        db.execSQL("DELETE FROM " + TABLE_PROJECT);
        db.execSQL("DELETE FROM " + TABLE_BUILDING);
        db.execSQL("DELETE FROM " + TABLE_FLAT);
        db.execSQL("DELETE FROM " + TABLE_ROOM);
        db.execSQL("DELETE FROM " + TABLE_CARPET);
        db.execSQL("DELETE FROM " + TABLE_FLOWERBED);
        db.execSQL("DELETE FROM " + TABLE_SYSOFFLINE);
        //db.execSQL("DELETE FROM " + TABLE_IMAGE);
        return true;
    }

    public boolean DeleteRecord(String RequestID) {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper


        // SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(Database.DATABASE_NAME, Context.MODE_PRIVATE, null, null);
        db.execSQL("DELETE FROM " + TABLE_REQUEST + " WHERE " + REQUEST_ID + " = '" + RequestID + "'");
        db.execSQL("DELETE FROM " + TABLE_LOCATION + " WHERE " + LOCATION_REQUESTID + " = '" + RequestID + "'");
        db.execSQL("DELETE FROM " + TABLE_PROJECT + " WHERE " + PROJECT_REQUESTID + " = '" + RequestID + "'");
        db.execSQL("DELETE FROM " + TABLE_BUILDING + " WHERE " + BUILDING_REQUESTID + " = '" + RequestID + "'");
        db.execSQL("DELETE FROM " + TABLE_FLAT + " WHERE " + FLAT_REQUESTID + " = '" + RequestID + "'");
        db.execSQL("DELETE FROM " + TABLE_ROOM + " WHERE " + ROOM_REQUESTID + " = '" + RequestID + "'");
        db.execSQL("DELETE FROM " + TABLE_CARPET + " WHERE " + CARPET_REQUESTID + " = '" + RequestID + "'");
        db.execSQL("DELETE FROM " + TABLE_FLOWERBED + " WHERE " + FLOWERBED_REQUESTID + " = '" + RequestID + "'");
        db.execSQL("DELETE FROM " + TABLE_SYSOFFLINE + " WHERE " + SYSOFFLINE_REQUESTID + " = '" + RequestID + "'");
       // db.execSQL("DELETE FROM " + TABLE_IMAGE + " WHERE " + IMAGE_REQUESTID + " = '" + RequestID + "'");
        return true;
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //image data //009
    public Long insertImagedata(String result, String requestid, String userid) {

        /*if (!checkImageDatalist(result, requestid)) {*/
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_REQUESTID, requestid);
            contentValues.put(IMAGE_USERID, userid);
            contentValues.put(IMAGE_DETAIL, result);
            contentValues.put(IMAGE_UPDATEDATA, result);

            Log.e(DataBaseHelper.class.getName(),"insertImagedata OK !!");
            return sqDatabase.insert(TABLE_IMAGE, null, contentValues);
        /*}
        return null;*/
    }


    private boolean checkImageDatalist(String result, String requestid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_IMAGE + " WHERE " + IMAGE_REQUESTID + "='" + requestid + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.moveToFirst()) {
            // return contact
            cursor.close();
            // db.close();
            return true;
        }
        return false;
    }

    // Check for availability of Building
    public List<Offlinedatamodel> getImageDetail(String productId) {
        // Log.d("Looking for product", productId);
        List<Offlinedatamodel> flowerbedData = new ArrayList<Offlinedatamodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_IMAGE + " WHERE "
                + IMAGE_REQUESTID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                flowerbedData.add(new Offlinedatamodel(cursor.getString(cursor.getColumnIndex(IMAGE_REQUESTID)), cursor.getString(cursor.getColumnIndex(IMAGE_DETAIL))));
            }
            while (cursor.moveToNext()); // return contact
        }
        cursor.close(); // db.close();
        return flowerbedData;
    }

    // update Imagedata
    public boolean updateImagedata(String requestId, String roomdetailid, String savedata, String userid, int isUpload) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_REQUESTID, requestId);
        contentValues.put(IMAGE_USERID, userid);
        contentValues.put(IMAGE_DETAIL, roomdetailid);
        contentValues.put(IMAGE_UPDATEDATA, savedata);
        contentValues.put(IMAGE_ISUPLOAD, isUpload);

        if (db.update(TABLE_IMAGE, contentValues, IMAGE_REQUESTID + "='" + requestId + "'", null) != 0)
            return true;
        else
            return false;
    }

    public boolean deleteImageRecordByUploadStatus(String RequestID,int isUpload) {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.execSQL("DELETE FROM " + TABLE_IMAGE + " WHERE " + IMAGE_REQUESTID + " = '" + RequestID + "' AND "+IMAGE_ISUPLOAD+" = '"+isUpload+"'");
        Log.e(DataBaseHelper.class.getName(),"Delete "+isUpload+" Image OK !!");
        return true;
    }

    public boolean deleteImageRecordByPath(String RequestID,String path) {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.execSQL("DELETE FROM " + TABLE_IMAGE + " WHERE " + IMAGE_REQUESTID + " = '" + RequestID + "' AND "+IMAGE_DETAIL+" = '"+path+"'");
        Log.e(DataBaseHelper.class.getName(),"Delete "+path+" Image OK !!");
        return true;
    }

    public List<SetOfflinedata> getImagesAllvaluesByUser(String ImageUserID,String ImageReqID)
    {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        //
        String selectQuery = "";
        if (ImageUserID == "") {
            selectQuery = "SELECT  * FROM " + TABLE_IMAGE;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_IMAGE + " Where " + IMAGE_USERID + "='" + ImageUserID + "' AND " + IMAGE_REQUESTID + "='"+ImageReqID+"'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(IMAGE_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(IMAGE_UPDATEDATA)));
                data.setUploadStatus(cursor.getInt(cursor.getColumnIndex(IMAGE_ISUPLOAD)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }

    public List<SetOfflinedata> getPendingUploadImages(String ImageUserID,String ImageReqID)
    {
        List<SetOfflinedata> requestDataList = new ArrayList<SetOfflinedata>();
        // Select All Query
        //
        String selectQuery = "";
        if (ImageUserID == "") {
            selectQuery = "SELECT  * FROM " + TABLE_IMAGE;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_IMAGE + " Where " + IMAGE_USERID + "='" + ImageUserID + "' AND " + IMAGE_REQUESTID + "='"+ImageReqID+"'' AND " + IMAGE_ISUPLOAD + "='0' ";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SetOfflinedata data = new SetOfflinedata();
                data.setRequestid(cursor.getString(cursor.getColumnIndex(IMAGE_REQUESTID)));
                data.setSaveuserdata(cursor.getString(cursor.getColumnIndex(IMAGE_UPDATEDATA)));
                data.setUploadStatus(cursor.getInt(cursor.getColumnIndex(IMAGE_ISUPLOAD)));
                requestDataList.add(data);
            } while (cursor.moveToNext());
        }
        return requestDataList;
    }
}