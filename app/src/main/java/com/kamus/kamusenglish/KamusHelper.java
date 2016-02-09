package com.kamus.kamusenglish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kamus.kamusenglish.model.KamusModel;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by koba on 11/23/15.
 */
public class KamusHelper  {
    public static String DATABASE_TABLE= DatabaseHelper.TABLE_NAME;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public KamusHelper(Context context){
        this.context=context;

    }
    public KamusHelper open() throws android.database.SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;

    }
    public void close(){
        databaseHelper.close();

    }
    public Cursor terjemahkan(String kata){
        return database.rawQuery("SELECT * FROM "+DATABASE_TABLE+" WHERE kata='"+kata+"'",null);

    }

    public String getHasilTerjemahan(String keyword){
       String arrayList = null;
        Cursor cursor= terjemahkan(keyword);
        cursor.moveToFirst();
        KamusModel kamusModel;

        if (cursor.getCount()>0){
            do {

                    kamusModel = new KamusModel();
                    kamusModel.setId(cursor.getInt(0));
                    kamusModel.setKata(cursor.getString(1));
                    kamusModel.setArti(cursor.getString(2));


                    cursor.moveToNext();

                }while (!cursor.isAfterLast());



            }
        cursor.close();
        return arrayList;
    }
    public Cursor queryAllData(){

        return database.rawQuery("SELECT * FROM "+DATABASE_TABLE+" ORDER BY kata ASC", null);
    }
    public ArrayList<KamusModel> getAllData(){
        ArrayList<KamusModel> arrayList = new ArrayList<KamusModel>();
        Cursor cursor= queryAllData();
        cursor.moveToFirst();
        KamusModel kamusModel;
        if (cursor.getCount()>0){
            do {
                kamusModel =new KamusModel();
                kamusModel.setId(cursor.getInt(0));
                kamusModel.setKata(cursor.getString(1));
                kamusModel.setArti(cursor.getString(2));
               arrayList.add(kamusModel);
                cursor.moveToNext();

            }while (!cursor.isAfterLast());

        }
        cursor.close();
        return arrayList;
    }

   public long insert(KamusModel kamus){
       ContentValues intialValues=new ContentValues();
       intialValues.put(DatabaseHelper.FIELD_KATA,kamus.getKata());
       intialValues.put(DatabaseHelper.FIELD_ARTI, kamus.getArti());
       return database.insert(DATABASE_TABLE,null,intialValues);


   }
    public void update(KamusModel kamus){
        ContentValues ubah= new ContentValues();
        ubah.put(DatabaseHelper.FIELD_KATA,kamus.getKata());
        ubah.put(DatabaseHelper.FIELD_ARTI,kamus.getArti());
        database.update(DATABASE_TABLE, ubah, DatabaseHelper.FIELD_ID + "=" + kamus.getId(), null);

    }
    public void delete(int id){
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.FIELD_ID + "= '"+id+"'",null );
    }
}
