package com.maxwell.caploc.DatabaseHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.maxwell.caploc.CheckListLeveModel;

import java.util.ArrayList;
import java.util.List;


public class ChecklistControler extends MyDatabaseHelper {
    public ChecklistControler(Context context) {
        super(context);
    }

    public boolean create(ObjectChecklist objectTableSetting) {

        ContentValues values = new ContentValues();

        values.put("checklistid", objectTableSetting.checklistid);
        values.put("checklist", objectTableSetting.checklist);
        values.put("availableqty", objectTableSetting.availableqty);
        values.put("status", objectTableSetting.status);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("Checklist_tbl  ", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public ObjectChecklist readSingleRecord() {

        ObjectChecklist objectTableSetting = null;

        String sql = "SELECT * FROM Checklist_tbl ORDER BY id DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            @SuppressLint("Range") String checklistid = cursor.getString(cursor.getColumnIndex("checklistid"));
            @SuppressLint("Range") String checklist = cursor.getString(cursor.getColumnIndex("checklist"));
            @SuppressLint("Range") String availableqty = cursor.getString(cursor.getColumnIndex("availableqty"));
            @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("status"));

            objectTableSetting = new ObjectChecklist();
            objectTableSetting.id = id;
            objectTableSetting.checklistid = checklistid;
            objectTableSetting.checklist = checklist;
            objectTableSetting.availableqty = availableqty;
            objectTableSetting.status = status;
        }
        cursor.close();
        db.close();
        return objectTableSetting;
    }

    public boolean update(ObjectChecklist objectTableSetting) {

        ContentValues values = new ContentValues();

        values.put("checklistid", objectTableSetting.checklistid);
        values.put("checklist", objectTableSetting.checklist);
        values.put("availableqty", objectTableSetting.availableqty);
        values.put("status", objectTableSetting.status);


        String where = "checklistid = ?";

        String[] whereArgs = { (objectTableSetting.checklistid) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("Checklist_tbl", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public List<ObjectChecklist> read() {

        List<ObjectChecklist> recordsList = new ArrayList<ObjectChecklist>();

        String sql = "SELECT * FROM Checklist_tbl ORDER BY id ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String checklistid = cursor.getString(cursor.getColumnIndex("checklistid"));
                @SuppressLint("Range") String checklist = cursor.getString(cursor.getColumnIndex("checklist"));
                @SuppressLint("Range") String availableqty = cursor.getString(cursor.getColumnIndex("availableqty"));
                @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("status"));


                ObjectChecklist objectShop = new ObjectChecklist();
                objectShop.id = id;

                objectShop.checklistid = checklistid;
                objectShop.checklist = checklist;
                objectShop.availableqty = availableqty;
                objectShop.status = status;
                recordsList.add(objectShop);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public boolean columnExists(String value) {
        String sql = "SELECT EXISTS (SELECT * FROM Checklist_tbl WHERE checklistid='"+value+"' LIMIT 1)";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean delete1(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("Checklist_tbl", "checklistid ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }

    public boolean deleteallrecords() {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("Checklist_tbl", null, null) > 0;
        db.close();

        return deleteSuccessful;

    }
    public List<CheckListLeveModel> getitem(){
        List<CheckListLeveModel> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();


        try {

            //String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            Cursor cursor = db.rawQuery("select * from Checklist_tbl order by id",null);
            StringBuffer stringBuffer = new StringBuffer();
            CheckListLeveModel dataModel = null;
            while (cursor.moveToNext()) {
                dataModel= new CheckListLeveModel();
                String id = cursor.getString(cursor.getColumnIndexOrThrow("checklistid"));
                String seno = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String checklist = cursor.getString(cursor.getColumnIndexOrThrow("checklist"));
                String avaiableqty = cursor.getString(cursor.getColumnIndexOrThrow("availableqty"));


                dataModel.setId(id);
                dataModel.setSno(seno);
                dataModel.setCheckList(checklist);
                dataModel.setAvailableQuantity(avaiableqty);
                stringBuffer.append(dataModel);
                // stringBuffer.append(dataModel);
                data.add(dataModel);
            }

            for (CheckListLeveModel mo:data ) {

                Log.i("Hellomo",""+mo.getId());
            }

        }
        catch (Exception ex)
        {

        }


        //

        return data;
    }

}
