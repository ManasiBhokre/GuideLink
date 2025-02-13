package com.example.guidelink;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Guidelink.db";
    private static final int DATABASE_VERSION = 14;

    // Table Names
    public static final String TABLE_USERS = "users";
    private static final String TABLE_GUIDES = "guides";
    public static final String TABLE_HOTSPOT = "hotspot";
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String TABLE_BOOKING_REQUEST = "bookingrequest";


    public static final String COLUMN_UID = "uid"; // User ID
    public static final String COLUMN_NAME = "uname"; // Username
    public static final String COLUMN_PASSWORD = "upassword"; // User Password
    public static final String COLUMN_EMAIL = "email"; // User Email
    public static final String COLUMN_CONTACT = "contact"; // User Contact
    public static final String COLUMN_REG_DATE = "reg_date";


    public static final String COLUMN_G_ID = "g_id";
    public static final String COLUMN_G_NAME = "g_name";
    public static final String COLUMN_G_EMAIL = "g_email";
    public static final String COLUMN_G_PASSWORD = "g_password";
    public static final String COLUMN_G_CONTACT = "g_contact";
    public static final String COLUMN_EXPERIENCE = "experience";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_AVAILABILITY = "availability";
    public static final String COLUMN_PROFILE_PIC = "profile_pic";
    public static final String COLUMN_LANGUAGES = "languages";
    public static final String COLUMN_HOURLY_RATE = "hourly_rate";
    public static final String COLUMN_G_GENDER = "gender";


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLACENAME = "place_name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_URL = "image_url";


    private static final String COLUMN_B_ID = "b_id";
    private static final String COLUMN_U_ID = "u_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TIMESTAMP = "timestamp";


    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_GUIDE_ID = "guide_id";
    public static final String COLUMN_TOUR_DATE = "tour_date";
    public static final String COLUMN_TOUR_DURATION = "tour_duration";
    public static final String COLUMN_TRAVELER_RANGE = "traveler_range";
    public static final String COLUMN_LANGUAGE_PREFERENCE = "language_preference";
    public static final String COLUMN_SPECIAL_REQUESTS = "special_requests";
    public static final String COLUMN_BUDGET_RANGE = "budget_range";
    private static final String COLUMN_INTERESTS = "interests";
    public static final String COLUMN_BOOKING_AGREEMENT = "booking_agreement";
    public static final String COLUMN_AGE_RANGE = "age_range";
    public static final String COLUMN_BOOKING_STATUS = "status";


    Context context;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_PASSWORD + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_CONTACT + " TEXT, " +
            COLUMN_REG_DATE + " TEXT)";

    String CREATE_GUIDES_TABLE = "CREATE TABLE " + TABLE_GUIDES + " (" +
            COLUMN_G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_G_NAME + " TEXT, " +
            COLUMN_G_EMAIL + " TEXT UNIQUE, " +
            COLUMN_G_PASSWORD + " TEXT, " +
            COLUMN_G_CONTACT + " TEXT, " +
            COLUMN_EXPERIENCE + " TEXT, " +
            COLUMN_RATING + " DECIMAL(3,2), " +
            COLUMN_CITY + " TEXT, " +
            COLUMN_AVAILABILITY + " TEXT, " +
            COLUMN_PROFILE_PIC + " TEXT, " +
            COLUMN_LANGUAGES + " TEXT, " +
            COLUMN_HOURLY_RATE + " DECIMAL(10,2), " +
            COLUMN_G_GENDER + " TEXT);";


    String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
            + COLUMN_B_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_U_ID + " INTEGER, "
            + COLUMN_G_ID + " INTEGER, "
            + COLUMN_CITY + " TEXT, "
            + COLUMN_DATE + " TEXT, "
            + COLUMN_STATUS + " TEXT, "
            + COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(" + COLUMN_G_ID + ") REFERENCES " + TABLE_GUIDES + "(" + COLUMN_G_ID + "))";


    String CREATE_TABLE_BOOKING_REQUEST =
            "CREATE TABLE " + TABLE_BOOKING_REQUEST + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_GUIDE_ID + " INTEGER, " +
                    COLUMN_TOUR_DATE + " TEXT, " +
                    COLUMN_TOUR_DURATION + " TEXT, " +
                    COLUMN_TRAVELER_RANGE + " TEXT, " +
                    COLUMN_LANGUAGE_PREFERENCE + " TEXT, " +
                    COLUMN_SPECIAL_REQUESTS + " TEXT, " +
                    COLUMN_AGE_RANGE + " TEXT, " +
                    COLUMN_INTERESTS + " TEXT, " +
                    COLUMN_BUDGET_RANGE + " TEXT, " +
                    COLUMN_BOOKING_AGREEMENT + " TEXT, " +
                    COLUMN_BOOKING_STATUS + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_UID + "), " +
                    "FOREIGN KEY(" + COLUMN_GUIDE_ID + ") REFERENCES " + TABLE_GUIDES + "(" + COLUMN_G_ID + "))";


    String CREATE_HOTSPOT_TABLE = "CREATE TABLE " + TABLE_HOTSPOT + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PLACENAME + " TEXT NOT NULL, " +
            COLUMN_CITY + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            COLUMN_RATING + " REAL NOT NULL, " +
            COLUMN_IMAGE_URL + " TEXT NOT NULL" +
            ")";


    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GUIDES_TABLE);
        db.execSQL(CREATE_BOOKINGS_TABLE);
        db.execSQL(CREATE_TABLE_BOOKING_REQUEST);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_HOTSPOT_TABLE);

        db.execSQL("PRAGMA foreign_keys=ON;");


    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUIDES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING_REQUEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTSPOT);
        onCreate(db);
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean checkUserExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Method to get user details
    public Cursor getUserDetails(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS,
                new String[]{COLUMN_UID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_CONTACT, COLUMN_REG_DATE},
                COLUMN_UID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);
    }

    // Method to update user profile
    public boolean updateUserProfile(int userId, String name, String email, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_CONTACT, contact);

        db.update(TABLE_USERS, values, COLUMN_UID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        return true;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_USERS + " WHERE " +
                COLUMN_EMAIL + "=?", new String[]{email});

        if (cursor.moveToFirst()) {
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            cursor.close();
            return userName;
        }
        cursor.close();
        return null;
    }

    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"uid"}, "email=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
            cursor.close();
            return userId;
        }
        return -1; // Return -1 if user not found
    }

    public int getGuideId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GUIDES, new String[]{COLUMN_G_ID}, "g_email=?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int guideId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_G_ID));
            cursor.close();
            return guideId;
        }
        return -1; // Return -1 if user not found
    }

    // Method to insert a new user into the database
    public long insertUser(String name, String password, String email, String contact, String regDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_CONTACT, contact);
        values.put(COLUMN_REG_DATE, regDate);

        // Insert the user data into the users table
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    // Method to check if a user with a given email already exists


    public List<Hotspot> searchHotspotsByName(String query) {
        List<Hotspot> hotspotList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM hotspot WHERE place_name LIKE ?", new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                Hotspot hotspot = new Hotspot(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLACENAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)), // Note comma here
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                );
                hotspotList.add(hotspot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hotspotList;
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }


    public boolean insertRequest(String tourDate, String specialRequests,
                                 String tourDuration, String travelerRange, String languagePreference,
                                 String ageRange, String interests, String budgetRange, String agreement) {

        SessionManager sessionManager = new SessionManager(context);
        int userId = sessionManager.getUserId();
        int guideId = sessionManager.getGuideId();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("guide_id", guideId);
        values.put("tour_date", tourDate);
        values.put("special_requests", specialRequests);
        values.put("tour_duration", tourDuration);
        values.put("traveler_range", travelerRange);
        values.put("language_preference", languagePreference);
        values.put("age_range", ageRange);
        values.put("interests", interests);
        values.put("budget_range", budgetRange);
        values.put("booking_agreement", agreement);
        values.put("status", "pending");

        long result = db.insert(TABLE_BOOKING_REQUEST, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateStatus(int guideId) {
        // Fetch the current status of the guide from the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT availability FROM guides WHERE g_id = ?", new String[]{String.valueOf(guideId)});

        if (cursor != null && cursor.moveToFirst()) {
            String currentStatus = cursor.getString(cursor.getColumnIndexOrThrow("availability"));
            cursor.close();

            // Determine the new status
            String newStatus = currentStatus.equalsIgnoreCase("available") ? "unavailable" : "available";

            // Update the status in the database
            ContentValues values = new ContentValues();
            values.put("availability", newStatus);

            int rowsAffected = db.update("guides", values, "g_id = ?", new String[]{String.valueOf(guideId)});

            db.close();

            // Return true if the update was successful, false otherwise
            return rowsAffected > 0;
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return false;
    }

    public boolean insertGuide(String gName, String gContact, String gEmail, String gPassword,
                               String experience, double rating, String city, String availability,
                               String profilePic, String languages, double hourlyRate, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_G_NAME, gName);
        values.put(COLUMN_G_CONTACT, gContact);
        values.put(COLUMN_G_EMAIL, gEmail);
        values.put(COLUMN_G_PASSWORD, gPassword);
        values.put(COLUMN_EXPERIENCE, experience);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_AVAILABILITY, availability);
        values.put(COLUMN_PROFILE_PIC, profilePic);
        values.put(COLUMN_LANGUAGES, languages);
        values.put(COLUMN_HOURLY_RATE, hourlyRate);
        values.put(COLUMN_G_GENDER, gender);

        long result = db.insert(TABLE_GUIDES, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GUIDES, new String[]{COLUMN_G_ID},
                COLUMN_G_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean authenticateGuide(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GUIDES, new String[]{COLUMN_G_ID},
                COLUMN_G_EMAIL + "=? AND " + COLUMN_G_PASSWORD + "=?", new String[]{email, password},
                null, null, null);
        boolean isAuthenticated = cursor.getCount() > 0;
        cursor.close();
        return isAuthenticated;
    }

    public Cursor getGuideRequests(int guideId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKINGS + " WHERE guideId = ?";
        return db.rawQuery(query, new String[]{String.valueOf(guideId)});
    }

    public List<Guide> searchGuidesByName(String name) {
        List<Guide> guides = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Using try-finally to ensure cursor is always closed
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_GUIDES + " WHERE " + COLUMN_G_NAME + " LIKE ?",
                    new String[]{"%" + name + "%"}
            );

            if (cursor.moveToFirst()) {
                do {
                    String guideName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_G_NAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_G_EMAIL));
                    String contact = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_G_CONTACT));
                    String city = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY));
                    float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)); // Simplified parsing
                    String experience = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCE));
                    String languages = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGES));
                    String gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_G_GENDER));

                    guides.add(new Guide(guideName, email, contact, city, rating, experience, languages, gender));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return guides;
    }

    //recommended guides display



    public List<Guide> getGuidesByCity(String city) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Guide> guides = new ArrayList<>();

        // Use rawQuery to select guides by city
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_GUIDES + " WHERE " + COLUMN_CITY + " = ?",
                new String[]{city});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Create a new Guide object and populate it with data from the cursor
                Guide guide = new Guide(); // Use the default constructor
                guide.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_G_NAME)));
                guide.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_G_EMAIL)));
                guide.setContact(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_G_CONTACT)));
                guide.setCity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)));
                guide.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING))); // No need for parseFloat
                guide.setExperience(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCE)));
                guide.setLanguages(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGES)));
                guide.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_G_GENDER)));

                // Add the guide to the list
                guides.add(guide);
            } while (cursor.moveToNext());
            cursor.close(); // Close the cursor after use
        }

        db.close(); // Always close the database to prevent memory leaks
        return guides; // Return the list of guides
    }

    public boolean deleteGuideById(int g_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("guides", "g_id=?", new String[]{String.valueOf(g_id)});
        db.close();
        return rowsDeleted > 0;
    }

    // Method to get guide details from the database
    public Cursor getGuideDetails(int guideId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GUIDES + " WHERE " + COLUMN_G_ID + " = ?", new String[]{String.valueOf(guideId)});
    }

    // Method to update guide profile in the database
    public void updateGuideProfile(int guideId, String guideName, String guideEmail, String guideContact, String experience, String city, String hourlyRate, String profilePicPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_GUIDES + " SET " +
                        COLUMN_G_NAME + " = ?, " +
                        COLUMN_G_EMAIL + " = ?, " +
                        COLUMN_G_CONTACT + " = ?, " +
                        COLUMN_EXPERIENCE + " = ?, " +
                        COLUMN_CITY + " = ?, " +
                        COLUMN_HOURLY_RATE + " = ?, " +
                        COLUMN_PROFILE_PIC + " = ? WHERE " + COLUMN_G_ID + " = ?",
                new Object[]{guideName, guideEmail, guideContact, experience, city, hourlyRate, profilePicPath, guideId});
    }

    public void updateBookingStatus(String newStatus, int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("status", newStatus);

        // Replace `booking_id` with the actual column name for identifying the booking
        int rowsAffected = db.update(
                "bookingrequest",
                contentValues,
                "user_id = ?",
                new String[]{String.valueOf(user_id)} // Replace `bookingId` with the current booking's ID
        );

        if(rowsAffected != -1){
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

}
