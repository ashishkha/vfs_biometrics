package com.vfs.fingerprint.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


open class DBHandler : SQLiteOpenHelper {

    private val DATABASE_VERSION = 1

    private val TABLE_BIOMETRICS = "biometric"
    private val KEY_TOKEN = "token"

    //For Fingers and Thumb
    private var FINGER_CODE_7 = "FC7"
    private var FINGER_CODE_8 = "FC8"
    private var FINGER_CODE_9 = "FC9"
    private var FINGER_CODE_10 = "FC10"
    private var FINGER_CODE_2 = "FC2"
    private var FINGER_CODE_3 = "FC3"
    private var FINGER_CODE_4 = "FC4"
    private var FINGER_CODE_5 = "FC5"
    private var FINGER_CODE_6 = "FC6"
    private var FINGER_CODE_1 = "FC1"

    private var SIGNATURE = "SIGN"

    private var FACIAL = "FACE"

    private var UNIQUE_ID = "UNIQUE_ID"

    //For Mrz Fields
    private var MRZ_NAME = "mrz_name"

    private var context: Context? = null
    private var uniqueId: String? = null

    var FC1: String? = null
    var FC2: String? = null
    var FC3: String? = null
    var FC4: String? = null
    var FC5: String? = null
    var FC6: String? = null
    var FC7: String? = null
    var FC8: String? = null
    var FC9: String? = null
    var FC10: String? = null
    var SIGN: String? = null
    var FACE: String? = null

    companion object {
        var mInstance: DBHandler? = null
        val DATABASE_NAME = "BiometricDB"

        private val TAG = DBHandler::class.qualifiedName

        fun getInstance(ctx: Context): DBHandler {
            /**
             * use the application context as suggested by CommonsWare.
             * this will ensure that you dont accidentally leak an Activitys
             * context (see this article for more information:
             * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
             */
            if (mInstance == null) {
//                mInstance = DBHandler(ctx.applicationContext, DATABASE_NAME, null, 1)
            }
            return mInstance as DBHandler
        }
    }

    constructor(
            context: Context?,
            name: String?,
            factory: SQLiteDatabase.CursorFactory?,
            version: Int
    ) : super(context, name, factory, version) {
        this.context = context
    }


    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(TAG, "called chekc new row create:")
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_BIOMETRICS + "("
                + KEY_TOKEN + " INTEGER PRIMARY KEY," + FINGER_CODE_7 + " TEXT,"
                + FINGER_CODE_8 + " TEXT,"
                + FINGER_CODE_9 + " TEXT,"
                + FINGER_CODE_10 + " TEXT,"
                + FINGER_CODE_2 + " TEXT,"
                + FINGER_CODE_3 + " TEXT,"
                + FINGER_CODE_4 + " TEXT,"
                + FINGER_CODE_5 + " TEXT,"
                + FINGER_CODE_6 + " TEXT,"
                + FINGER_CODE_1 + " TEXT,"
                + SIGNATURE + " TEXT,"
                + FACIAL + " TEXT,"
                + UNIQUE_ID + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)

        /*   uniqueId = Settings.Secure.getString(
               AppClass.getInstance().getContentResolver(),
               Settings.Secure.ANDROID_ID
           )*/
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_BIOMETRICS);

        // Create tables again
        onCreate(db);
    }

    fun addFingerData(fingerModel: FingerModel, id: Int): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(
                FINGER_CODE_7,
                fingerModel.getSCALE100()?.getFingerprints()?.get(0)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )
        values.put(
                FINGER_CODE_8,
                fingerModel.getSCALE100()?.getFingerprints()?.get(1)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )
        values.put(
                FINGER_CODE_9,
                fingerModel.getSCALE100()?.getFingerprints()?.get(2)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )
        values.put(
                FINGER_CODE_10,
                fingerModel.getSCALE100()?.getFingerprints()?.get(3)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )

        values.put(
                FINGER_CODE_2,
                fingerModel.getSCALE100()?.getFingerprints()?.get(4)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )
        values.put(
                FINGER_CODE_3,
                fingerModel.getSCALE100()?.getFingerprints()?.get(5)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )
        values.put(
                FINGER_CODE_4,
                fingerModel.getSCALE100()?.getFingerprints()?.get(6)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )
        values.put(
                FINGER_CODE_5,
                fingerModel.getSCALE100()?.getFingerprints()?.get(7)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )

        values.put(
                UNIQUE_ID,
                uniqueId
        )

//        var id = db.insert(TABLE_BIOMETRICS, null, values)
//        db.close() // Closing database connection
        var id = db.update(TABLE_BIOMETRICS, values, "$KEY_TOKEN=" + id, null)
        db.close()
        return id
    }

    fun addThumbData(fingerModel: FingerModel, id: Long) {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(
                FINGER_CODE_6,
                fingerModel.getSCALE100()?.getFingerprints()?.get(0)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )

        values.put(
                FINGER_CODE_1,
                fingerModel.getSCALE100()?.getFingerprints()?.get(1)?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )

        Log.d(
                TAG,
                "called ak check what is happeing:" + KEY_TOKEN + "___" + id + "___" + fingerModel.getSCALE100()?.getFingerprints()?.get(
                        0
                )?.getFingerImpressionImage()?.getBinaryBase64ObjectBMP()
        )

        // Inserting Row
        db.update(TABLE_BIOMETRICS, values, "$KEY_TOKEN=" + id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    fun getData(id: Int) {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_BIOMETRICS WHERE $KEY_TOKEN = 1"
        var cursor = db.rawQuery(query, null)

        var fingerModel: FingerModel = FingerModel()

        if (cursor.moveToFirst()) {
            FC1 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_1))
            FC2 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_2))
            FC3 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_3))
            FC4 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_4))
            FC5 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_5))
            FC6 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_6))
            FC7 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_7))
            FC8 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_8))
            FC9 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_9))
            FC10 = cursor.getString(cursor.getColumnIndex(FINGER_CODE_10))
            SIGN = cursor.getString(cursor.getColumnIndex(SIGNATURE))
            FACE = cursor.getString(cursor.getColumnIndex(FACIAL))
        }
    }

    fun getId(): Int {

        val db = this.writableDatabase

        val resultSet = db.rawQuery("SELECT $KEY_TOKEN from $TABLE_BIOMETRICS order by $KEY_TOKEN DESC limit 1", null)
        resultSet.moveToFirst()
        val token = resultSet.getInt(0)
        Log.d(TAG, "called ak token ak:" + token)
        return token;
    }

    fun addSignatureInBase64(base64: String, id: Int) {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(
                SIGNATURE, base64
        )

        db.update(TABLE_BIOMETRICS, values, "$KEY_TOKEN=" + id, null)
        db.close()
    }

    fun addFacialInBase64(base64: String) {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(
                FACIAL, base64
        )

//        db.update(TABLE_BIOMETRICS, values, "$KEY_TOKEN=" + id, null)
//        db.close()
        var id = db.insert(TABLE_BIOMETRICS, null, values)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection

    }
}