package com.example.pc24.cbohelp.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by pc24 on 15/12/2016.
 */


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CBOhelp.db";
    public static final String MENU_TABLE = "menu";
    public static final String CHAT_TABLE = "chat";
    public static final String COMPLAINT_TABLE = "complaint_table";
    public static final String ORDER_ITEM_TABLE = "order_item";
    public static final String PARTY_TABLE = "party_table";
    public static final String TEAM_TABLE = "team_item";
    private static final int DATABASE_VERSION = 1;
    private HashMap hp;


    String Create_menu="create table " +MENU_TABLE+ " (id integer primary key, MAIN_MENU text,SRNO text,MENU_NAME text, MENU_CODE text,MENU_TYPE text,MAIN_MENU_SRNO text)";
    String Create_chat="create table " +CHAT_TABLE+ " (id integer primary key, MSG_ID text,MSG text,FILE_PATH text, STATUS text,DATE text,MSG_TO text,MSG_FROM text)";
    String Create_order_item="create table " +ORDER_ITEM_TABLE+ " (id integer primary key, ITEM_ID text,ITEM_NAME text,RATE text,MRP_RATE text,PACK text)";
    String Create_complaint="create table " +COMPLAINT_TABLE+ " (id integer primary key, COMPLAINT_TYPE text, DOC_NO text,PA_ID text,PA_NAME text, FROM_USER text,TO_USER text,DONE_BY text, STATUS text,COMMENT text,REMARK text, TIME text,DOC_DATE text,USER1 text,USER_ID1 text,USER2 text,USER_ID2 text, P_DAYS text,MOBILE text,ATTACHMENT text,PRIORITY text,IS_READ text)";
    String Create_party="create table " +PARTY_TABLE+ " (id integer primary key, PA_ID text, PA_NAME text,USER_NAME1 text,ID1 text, USER_NAME2 text, ID2 text)";
    String Create_team="create table " +TEAM_TABLE+ " (id integer primary key, PA_ID text, PA_NAME text, MANAGER_ID text, MANAGER_NAME text)";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TODO Auto-generated method stub

        db.execSQL(Create_team);
        db.execSQL(Create_party);
        db.execSQL(Create_complaint);

       /* db.execSQL(Create_menu);
        db.execSQL(Create_chat);
        db.execSQL(Create_order_item);
       */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        switch(oldVersion) {

        }
    }

    //menu table querry

    public boolean insertMenu (String MAIN_MENU, String SRNO, String MENU_NAME, String MENU_CODE,String MENU_TYPE,String MAIN_MENU_SRNO) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MAIN_MENU", MAIN_MENU);
        contentValues.put("SRNO", SRNO);
        contentValues.put("MENU_NAME", MENU_NAME);
        contentValues.put("MENU_CODE", MENU_CODE);
        contentValues.put("MENU_TYPE", MENU_TYPE);
        contentValues.put("MAIN_MENU_SRNO", MAIN_MENU_SRNO);
        db.insert(MENU_TABLE, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+MENU_TABLE+" where id="+id+"", null );
        return res;
    }

    public int numberOfRows(String table){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, table);
    }

    public boolean updateMenu (Integer id,String MAIN_MENU, String SRNO, String MENU_NAME, String MENU_CODE,String MENU_TYPE,String MAIN_MENU_SRNO) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MAIN_MENU", MAIN_MENU);
        contentValues.put("SRNO", SRNO);
        contentValues.put("MENU_NAME", MENU_NAME);
        contentValues.put("MENU_CODE", MENU_CODE);
        contentValues.put("MENU_TYPE", MENU_TYPE);
        contentValues.put("MAIN_MENU_SRNO", MAIN_MENU_SRNO);
        db.update(MENU_TABLE, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        db.close();
        return true;
    }

    public Integer deleteMenu1 (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MENU_TABLE,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public void deleteMenu() {
        SQLiteDatabase sd = this.getWritableDatabase();
        sd.delete(MENU_TABLE, null, null);
        sd.close();
    }

    public HashMap<String, ArrayList<String>> getMainMenu() {
        HashMap<String, ArrayList<String>> Main_Menu = new HashMap<String, ArrayList<String>>();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> code = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+MENU_TABLE+" where MENU_TYPE='P'", null );
        res.moveToFirst();

       /* name.add("CBO BI");
        code.add("b");
        name.add("New Order");
        code.add("n");*/
        while(!res.isAfterLast()){
            name.add(res.getString(res.getColumnIndex("MENU_NAME")));
            code.add(res.getString(res.getColumnIndex("MENU_CODE")));
            //Log.v("Javed", res.getString(res.getColumnIndex("MENU_NAME")));
            res.moveToNext();
        }
        Main_Menu.put("name",name);
        Main_Menu.put("code",code);
        return Main_Menu;
    }
    public HashMap<String, ArrayList<String>> getSubMenu(String menu_name) {
        HashMap<String, ArrayList<String>> Main_Menu = new HashMap<String, ArrayList<String>>();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> code = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+MENU_TABLE+" where MENU_TYPE='C' and MAIN_MENU='"+menu_name+"'", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            name.add(res.getString(res.getColumnIndex("MENU_NAME")));
            code.add(res.getString(res.getColumnIndex("MENU_CODE")));
            //Log.v("Javed", res.getString(res.getColumnIndex("MENU_NAME")));
            res.moveToNext();
        }
        Main_Menu.put("name",name);
        Main_Menu.put("code",code);
        return Main_Menu;
    }


    ///MENU TABLE END

    ///CHAT TABLE STARTS HERE

    public boolean insertChat (String MSG_ID, String MSG, String FILE_PATH, String STATUS,String DATE,String MSG_TO,String MSG_FROM) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MSG_ID", MSG_ID);
        contentValues.put("MSG", MSG);
        contentValues.put("FILE_PATH", FILE_PATH);
        contentValues.put("STATUS", STATUS);
        contentValues.put("DATE", DATE);
        contentValues.put("MSG_TO", MSG_TO);
        contentValues.put("MSG_FROM", MSG_FROM);
        db.insert(CHAT_TABLE, null, contentValues);
        db.close();
        return true;
    }

    public HashMap<String, ArrayList<String>> getChat(String PA_ID) {
        HashMap<String, ArrayList<String>> Chat = new HashMap<String, ArrayList<String>>();
        ArrayList<String> msg = new ArrayList<String>();
        ArrayList<String> who = new ArrayList<String>();
        ArrayList<String> file = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CHAT_TABLE+" where MSG_TO='"+PA_ID+"' or MSG_FROM='"+PA_ID+"'", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            msg.add(res.getString(res.getColumnIndex("MSG")));
            who.add(res.getString(res.getColumnIndex("MSG_FROM")));
            file.add(res.getString(res.getColumnIndex("FILE_PATH")));
            //Log.v("Javed", res.getString(res.getColumnIndex("MENU_NAME")));
            res.moveToNext();
        }
        Chat.put("msg",msg);
        Chat.put("who",who);
        Chat.put("file",file);
        return Chat;
    }

    /// CHAT TABLE ENDS HERE

    /// Order Item table starts here
    public boolean insertOrderItem (String ITEM_ID, String ITEM_NAME, String RATE, String MRP_RATE, String PACK) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_ID", ITEM_ID);
        contentValues.put("ITEM_NAME", ITEM_NAME);
        contentValues.put("RATE", RATE);
        contentValues.put("MRP_RATE", MRP_RATE);
        contentValues.put("PACK", PACK);
        db.insert(ORDER_ITEM_TABLE, null, contentValues);
        db.close();
        return true;
    }

    public HashMap<String, ArrayList> getOrderItem(String filter_text) {
        HashMap<String, ArrayList> OrderItem = new HashMap<String, ArrayList>();
        ArrayList<String> ITEM_ID = new ArrayList<String>();
        ArrayList<String> ITEM_NAME = new ArrayList<String>();
        ArrayList<String> RATE = new ArrayList<String>();
        ArrayList<String> MRP_RATE = new ArrayList<String>();
        ArrayList<String> PACK = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        if(filter_text.equals("")){
             res =  db.rawQuery( "select * from "+ORDER_ITEM_TABLE, null );
        }else {
             res = db.rawQuery("select * from " + ORDER_ITEM_TABLE + " where ITEM_NAME LIKE '%" + filter_text + "%'", null);
        }
        res.moveToFirst();

        while(!res.isAfterLast()){
            ITEM_ID.add(res.getString(res.getColumnIndex("ITEM_ID")));
            ITEM_NAME.add(res.getString(res.getColumnIndex("ITEM_NAME")));
            RATE.add(res.getString(res.getColumnIndex("RATE")));
            MRP_RATE.add(res.getString(res.getColumnIndex("MRP_RATE")));
            PACK.add(res.getString(res.getColumnIndex("PACK")));
            //Log.v("Javed", res.getString(res.getColumnIndex("MENU_NAME")));
            res.moveToNext();
        }
        OrderItem.put("ITEM_ID",ITEM_ID);
        OrderItem.put("ITEM_NAME",ITEM_NAME);
        OrderItem.put("RATE",RATE);
        OrderItem.put("MRP_RATE",MRP_RATE);
        OrderItem.put("PACK",PACK);
        return OrderItem;
    }

    public void deleteOrderItem() {
        SQLiteDatabase sd = this.getWritableDatabase();
        sd.delete(ORDER_ITEM_TABLE, null, null);
        sd.close();
    }
    /// Order Item table ends here


    /// Order table starts here


    public boolean insertComplaint (String COMPLAINT_TYPE,String DOC_NO,String PA_ID, String PA_NAME,
                                String FROM_USER,String TO_USER, String DONE_BY, String STATUS,String COMMENT,
                                String REMARK, String TIME,String DOC_DATE, String USER1, String USER_ID1, String USER2, String USER_ID2,
                                String P_DAYS, String MOBILE,String ATTACHMENT, String PRIORITY) {

        SQLiteDatabase db = this.getWritableDatabase();

        long l = DatabaseUtils.longForQuery(db, "SELECT count(*) FROM "+COMPLAINT_TABLE+" where DOC_NO='"+DOC_NO+"'", null);

        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPLAINT_TYPE", COMPLAINT_TYPE);
        contentValues.put("DOC_NO", DOC_NO);
        contentValues.put("PA_ID", PA_ID);
        contentValues.put("PA_NAME", PA_NAME);
        contentValues.put("FROM_USER", FROM_USER);
        contentValues.put("TO_USER", TO_USER);
        contentValues.put("DONE_BY", DONE_BY);
        contentValues.put("STATUS", STATUS);
        contentValues.put("COMMENT", COMMENT);
        contentValues.put("REMARK", REMARK);
        contentValues.put("TIME", TIME);
        contentValues.put("DOC_DATE", DOC_DATE);
        contentValues.put("USER1", USER1);
        contentValues.put("USER_ID1", USER_ID1);
        contentValues.put("USER2", USER2);
        contentValues.put("USER_ID2", USER_ID2);
        contentValues.put("P_DAYS", P_DAYS);
        contentValues.put("MOBILE", MOBILE);
        contentValues.put("ATTACHMENT", ATTACHMENT);
        contentValues.put("PRIORITY", PRIORITY);
        if(l==0) {
            contentValues.put("IS_READ", USER2);
            db.insert(COMPLAINT_TABLE, null, contentValues);
        }else{
            db.update(COMPLAINT_TABLE,  contentValues,"DOC_NO='"+DOC_NO+"'",null);
        }
        db.close();
        return true;
    }

    public ArrayList< HashMap<String,String>> getComplaint(String STATUS1,String USER_ID2,String company_id) {
        ArrayList< HashMap<String,String>> adaptor_data = new ArrayList< HashMap<String,String>> ();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        switch (STATUS1){
            case "P":
                STATUS1="PENDING";
                break;
            case "C":
                STATUS1="COMPLETE";
                break;
            default:
                STATUS1="";
        }
        if(STATUS1.equals("") && USER_ID2.equals("")  && company_id.equals("")){
            res =  db.rawQuery( "select * from "+COMPLAINT_TABLE , null );
        }else if(STATUS1.equals("") && !USER_ID2.equals("") && company_id.equals("")){
            res =  db.rawQuery( "select * from "+COMPLAINT_TABLE  + " where USER_ID2='" + USER_ID2 + "'", null );
        }else if(!STATUS1.equals("") && USER_ID2.equals("") && company_id.equals("")){
            res =  db.rawQuery( "select * from "+COMPLAINT_TABLE  + " where STATUS = '" + STATUS1  + "'", null );
        }else  if(!STATUS1.equals("") && !USER_ID2.equals("") && company_id.equals("")){
            res = db.rawQuery("select * from " + COMPLAINT_TABLE + " where STATUS = '" + STATUS1 + "' and USER_ID2='" + USER_ID2 + "'", null);
        }else if(STATUS1.equals("") && USER_ID2.equals("")  && !company_id.equals("")){
            res =  db.rawQuery( "select * from "+COMPLAINT_TABLE  + " where PA_ID='" + company_id + "'", null );
        }else if(STATUS1.equals("") && !USER_ID2.equals("") && !company_id.equals("")){
            res =  db.rawQuery( "select * from "+COMPLAINT_TABLE  + " where USER_ID2='" + USER_ID2 + "'" + " and PA_ID='" + company_id + "'", null );
        }else if(!STATUS1.equals("") && USER_ID2.equals("") && !company_id.equals("")){
            res =  db.rawQuery( "select * from "+COMPLAINT_TABLE  + " where STATUS = '" + STATUS1  + "'" + " and PA_ID='" + company_id + "'", null );
        }else  {
            res = db.rawQuery("select * from " + COMPLAINT_TABLE + " where STATUS = '" + STATUS1 + "' and USER_ID2='" + USER_ID2 + "'" + " and PA_ID='" + company_id + "'" , null);
        }
        res.moveToFirst();

        while(!res.isAfterLast()){
            HashMap<String,String> data=new HashMap<>();
            data.put("COMPLAINT_TYPE",res.getString(res.getColumnIndex("COMPLAINT_TYPE")));
            data.put("DOC_NO",res.getString(res.getColumnIndex("DOC_NO")));
            data.put("PA_NAME",res.getString(res.getColumnIndex("PA_NAME")));
            data.put("FROM_USER",res.getString(res.getColumnIndex("FROM_USER")));
            data.put("TO_USER",res.getString(res.getColumnIndex("TO_USER")));
            data.put("DONE_BY",res.getString(res.getColumnIndex("DONE_BY")));
            data.put("STATUS",res.getString(res.getColumnIndex("STATUS")));
            data.put("COMMENT",res.getString(res.getColumnIndex("COMMENT")));
            data.put("REMARK",res.getString(res.getColumnIndex("REMARK")));
            data.put("TIME",res.getString(res.getColumnIndex("TIME")));
            data.put("DOC_DATE",res.getString(res.getColumnIndex("DOC_DATE")));
            data.put("USER1",res.getString(res.getColumnIndex("USER1")));
            data.put("USER2",res.getString(res.getColumnIndex("USER2")));
            data.put("P_DAYS",res.getString(res.getColumnIndex("P_DAYS")));
            data.put("MOBILE",res.getString(res.getColumnIndex("MOBILE")));
            data.put("ATTACHMENT",res.getString(res.getColumnIndex("ATTACHMENT")));
            data.put("PRIORITY",res.getString(res.getColumnIndex("PRIORITY")));
            data.put("IS_READ",res.getString(res.getColumnIndex("IS_READ")));

            adaptor_data.add(data);

            res.moveToNext();
        }

        return adaptor_data;
    };


    public void deleteComplaint() {
        SQLiteDatabase sd = this.getWritableDatabase();
        sd.delete(COMPLAINT_TABLE, null, null);
        sd.close();
    }

    /// Order table ends here


    /// Party table starts here

    public boolean insertParty (String PA_ID,String PA_NAME, String USER_NAME, String ID,String USER_NAME2, String ID2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PA_ID", PA_ID);
        contentValues.put("PA_NAME", PA_NAME);
        contentValues.put("USER_NAME1", USER_NAME);
        contentValues.put("ID1", ID);
        contentValues.put("USER_NAME2", USER_NAME2);
        contentValues.put("ID2", ID2);
        db.insert(PARTY_TABLE, null, contentValues);
        db.close();
        return true;
    }

    public ArrayList< HashMap<String,String>>  getParty(String STATUS1,String ID) {

        ArrayList< HashMap<String,String>> adaptor_data = new ArrayList< HashMap<String,String>> ();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;

        switch (STATUS1){
            case "P":
                STATUS1="PENDING";
                break;
            case "C":
                STATUS1="COMPLETE";
                break;
            default:
                STATUS1="";
        }



        /*SELECT PHPARTY.PA_NAME, COUNT (PHCOMPLAINT.ID) COUNT ,COM1.REMARK  ,COM1.AUSER_ID
        FROM PHPARTY INNER JOIN PHCOMPLAINT ON PHPARTY.PA_ID=PHCOMPLAINT.PA_ID
        INNER JOIN (SELECT MAX(ID) AS COMP_ID, PA_ID FROM PHCOMPLAINT   GROUP BY PA_ID )  COM ON      PHPARTY.PA_ID=COM.PA_ID
        INNER JOIN PHCOMPLAINT COM1 ON    COM.COMP_ID= COM1.ID
        WHERE (@STATUS='' OR PHCOMPLAINT.STATUS=@STATUS)
        GROUP BY PHPARTY.PA_NAME,COM1.REMARK ,COM1.AUSER_ID  ORDER BY COUNT (PHCOMPLAINT.ID)   DESC*/

        res =  db.rawQuery( "select "+PARTY_TABLE+".PA_NAME,"+PARTY_TABLE+".PA_ID ,"+PARTY_TABLE+".USER_NAME1,"+PARTY_TABLE+".ID1,"+PARTY_TABLE+".USER_NAME2,"+PARTY_TABLE+".ID2, COUNT ("+COMPLAINT_TABLE+".ID) COUNT " +
                "from "+PARTY_TABLE +" INNER JOIN "+COMPLAINT_TABLE+ " ON "+PARTY_TABLE+".PA_ID="+COMPLAINT_TABLE+".PA_ID " +
                "WHERE  ( '"+STATUS1+"'='' OR STATUS='"+STATUS1+"') and  ( '"+ID+"'='' OR ID1='"+ID+"') GROUP BY  "+PARTY_TABLE+".PA_NAME,"+PARTY_TABLE+".PA_ID ,"+PARTY_TABLE+".USER_NAME1,"+PARTY_TABLE+".ID1,"+PARTY_TABLE+".USER_NAME2,"+PARTY_TABLE+".ID2 ORDER BY COUNT ("+COMPLAINT_TABLE+".ID) DESC", null );

        res.moveToFirst();

        while(!res.isAfterLast()){

            HashMap<String,String> data=new HashMap<>();
            data.put("PA_ID",res.getString(res.getColumnIndex("PA_ID")));
            data.put("COUNT",res.getString(res.getColumnIndex("COUNT")));
            data.put("PA_NAME",res.getString(res.getColumnIndex("PA_NAME")));
            data.put("USER_NAME",res.getString(res.getColumnIndex("USER_NAME1")));
            data.put("ID",res.getString(res.getColumnIndex("ID1")));
            data.put("USER_NAME2",res.getString(res.getColumnIndex("USER_NAME2")));
            data.put("ID2",res.getString(res.getColumnIndex("ID2")));
            adaptor_data.add(data);
            res.moveToNext();

        }
        return adaptor_data;
    };

    public HashMap<String, ArrayList<String>> getPartyFilter(String filter_text) {
        HashMap<String, ArrayList<String>> Party = new HashMap<String, ArrayList<String>>();
        ArrayList<String> PA_ID = new ArrayList<String>();
        ArrayList<String> PA_NAME = new ArrayList<String>();
        ArrayList<String> BALANCE = new ArrayList<String>();
        ArrayList<String> HEAD_QTR = new ArrayList<String>();
        ArrayList<String> DIVISION_NAME = new ArrayList<String>();
        ArrayList<String> MOBILE = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        if(filter_text.equals("")){
            res =  db.rawQuery( "select * from "+PARTY_TABLE, null );
        }else {
            res = db.rawQuery("select * from " + PARTY_TABLE + " where PA_NAME LIKE '%" + filter_text + "%'", null);
        }
        res.moveToFirst();
        while(!res.isAfterLast()){
            PA_ID.add(res.getString(res.getColumnIndex("PA_ID")));
            PA_NAME.add(res.getString(res.getColumnIndex("PA_NAME")));
            BALANCE.add(res.getString(res.getColumnIndex("BALANCE")));
            HEAD_QTR.add(res.getString(res.getColumnIndex("HEAD_QTR")));
            DIVISION_NAME.add(res.getString(res.getColumnIndex("DIVISION_NAME")));
            MOBILE.add(res.getString(res.getColumnIndex("MOBILE")));
            res.moveToNext();
        }
        Party.put("PA_ID",PA_ID);
        Party.put("PA_NAME",PA_NAME);
        Party.put("BALANCE",BALANCE);
        Party.put("HEAD_QTR",HEAD_QTR);
        Party.put("DIVISION_NAME",DIVISION_NAME);
        Party.put("MOBILE",MOBILE);
        return Party;
    }


    public void deleteParty() {
        SQLiteDatabase sd = this.getWritableDatabase();
        sd.delete(PARTY_TABLE, null, null);
        sd.close();
    }

    /// Order table ends here


    /// team table starts here

    public boolean insertTeam (String PA_ID, String PA_NAME,String MANAGER_ID, String MANAGER_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PA_ID", PA_ID);
        contentValues.put("PA_NAME", PA_NAME);
        contentValues.put("MANAGER_ID", MANAGER_ID);
        contentValues.put("MANAGER_NAME", MANAGER_NAME);
        db.insert(TEAM_TABLE, null, contentValues);
        db.close();
        return true;
    }

    public void deleteTeam() {
        SQLiteDatabase sd = this.getWritableDatabase();
        sd.delete(TEAM_TABLE, null, null);
        sd.close();
    }

    public HashMap<String, ArrayList<String>> getTeam(String ID) {
        HashMap<String, ArrayList<String>> Party = new HashMap<String, ArrayList<String>>();
        ArrayList<String> PA_ID = new ArrayList<String>();
        ArrayList<String> PA_NAME = new ArrayList<String>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        if(ID.equals("")){
            res =  db.rawQuery( "select * from "+TEAM_TABLE , null );
        }else {
            res = db.rawQuery("select * from " + TEAM_TABLE + " where PA_ID='" + ID + "'", null);
        }
        res.moveToFirst();

        while(!res.isAfterLast()){
            PA_ID.add(res.getString(res.getColumnIndex("PA_ID")));
            PA_NAME.add(res.getString(res.getColumnIndex("PA_NAME")));
            res.moveToNext();
        }
        Party.put("PA_ID",PA_ID);
        Party.put("PA_NAME",PA_NAME);
        return Party;
    };

    /// team table ends here
}