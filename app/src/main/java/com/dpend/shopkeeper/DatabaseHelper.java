package com.dpend.shopkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "shopdb";

    private static final String TABLENAME = "products";
    private static final String P_COL2 = "barcode";
    private static final String P_COL3 = "p_name";
    private static final String P_COL4 = "price";
    private static final String P_COL5 = "stock";
    private static final String P_COL6 = "unit";
    private static final String P_COL7 = "pprice";
//    private static final String P_COL8 = "unit";
//    private static final String P_COL9 = "unit";



    private static final String TABLE2NAME = "sales";
    private static final String S_COL2 = "date";
    private static final String S_COL3 = "time";
    private static final String S_COL4 = "amount";

    private static final String TABLE3NAME = "sold_products";
    private static final String SP_COL2 = "name";
    private static final String SP_COL3 = "qty";
    private static final String SP_COL4 = "price";
    private static final String SP_COL5 = "total_amount";
    private static final String SP_COL6 = "bill_no";

    private static final String TABLE4NAME = "daily_sales";
    private static final String DS_COL2 = "sales";
    private static final String DS_COL3 = "date";
    private static final String DS_COL4 = "amount";
    private static final String DS_COL5 = "profit";

    private static final String TABLE5NAME = "returns";
    private static final String R_COL2 = "bill_no";
    private static final String R_COL3 = "product_id";
    private static final String R_COL4 = "qty";
    private static final String R_COL5 ="reason";
    private static final String R_COL6 ="name";
    private static final String R_COL7 ="date";

    private static final String TABLE6NAME = "pro_batches";
    private static final String PB_COL2 = "exp_date";
    private static final String PB_COL3 = "batch_no";
    private static final String PB_COL4 = "product";
    private static final String PB_COL5 = "p_price";
    private static final String PB_COL6 = "r_price";
    private static final String PB_COL7 = "description";
    private static final String PB_COL8 = "stock";

    private static final String TABLE7NAME = "categories";
    private static final String C_COL2 = "cat_name";

    private static final String TABLE8NAME = "account";
    private static final String A_COL2 = "shopname";
    private static final String A_COL3 = "address";
    private static final String A_COL4 = "branch";
    private static final String A_COL5 = "dis_pic";




    public DatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table = "CREATE TABLE "+ TABLENAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+P_COL2+" TEXT, "+P_COL3+" TEXT, "+P_COL4+" FLOAT, "+P_COL5+" INTEGER, "+P_COL7+" FLOAT, "+P_COL6+" TEXT)";
        String create_table2 = "CREATE TABLE "+ TABLE2NAME +"(BILL_NO INTEGER PRIMARY KEY AUTOINCREMENT, "+S_COL2+" TEXT, "+S_COL3+" TEXT, "+S_COL4+" FLOAT)";
        String create_table3 = "CREATE TABLE "+ TABLE3NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+SP_COL2+" TEXT, "+SP_COL3+" INTEGER, "+SP_COL4+" FLOAT, "+SP_COL5+" FLOAT, "+SP_COL6+" INTEGER, FOREIGN KEY("+SP_COL6+") REFERENCES "+TABLE2NAME+"(BILL_NO))";
        String create_table4 = "CREATE TABLE "+ TABLE4NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+DS_COL2+" INTEGER, "+DS_COL3+" TEXT, "+DS_COL4+" FLOAT)";
        String create_table5 = "CREATE TABLE "+ TABLE5NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+R_COL2+" INTEGER, "+R_COL3+" INTEGER, "+R_COL4+" INTEGER, "+R_COL5+" TEXT, "+R_COL6+" TEXT, "+R_COL7+" TEXT)";
        String create_table6 = "CREATE TABLE "+TABLE6NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+PB_COL2+" DATE, "+PB_COL3+" TEXT, "+PB_COL4+" INTEGER, "+PB_COL5+" FLOAT, "+PB_COL8+" INTEGER, "+PB_COL6+" FLOAT, "+PB_COL7+" TEXT, "+"FOREIGN KEY(product) REFERENCES products(ID) ON DELETE CASCADE)";
        sqLiteDatabase.execSQL(create_table);
        sqLiteDatabase.execSQL(create_table2);
        sqLiteDatabase.execSQL(create_table3);
        sqLiteDatabase.execSQL(create_table4);
        sqLiteDatabase.execSQL(create_table5);
        sqLiteDatabase.execSQL(create_table6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE2NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE3NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE4NAME);
        onCreate(sqLiteDatabase);
    }
    public boolean AddData(String barcode,String name,String unit,int stock,float price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(P_COL2,barcode);
        values.put(P_COL3,name);
        values.put(P_COL4,price);
        values.put(P_COL5,stock);
        values.put(P_COL6,unit);
        long result = db.insert(TABLENAME,null,values);
        return result != -1;
        }

    public long AddSaleData(String date,String time,float amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_COL2,date);
        values.put(S_COL3,time);
        values.put(S_COL4,amount);
        long result = db.insert(TABLE2NAME,null,values);
        return result;
    }
    public long AddReturndata(int bill_no,int product_id,int qty,String reason,String name,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(R_COL2,bill_no);
        values.put(R_COL3,product_id);
        values.put(R_COL4,qty);
        values.put(R_COL5,reason);
        values.put(R_COL6,name);
        values.put(R_COL7,date);
        long result = db.insert(TABLE5NAME,null,values);
        return result;
    }

    public long AddSaleproData(String name,int qty,float price,float amount,int bill_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SP_COL2,name);
        values.put(SP_COL3,qty);
        values.put(SP_COL4,price);
        values.put(SP_COL5,amount);
        values.put(SP_COL6,bill_no);
        long result = db.insert(TABLE3NAME,null,values);
        return result;
    }
    public long AddDSaleData(String date,Integer sales,float amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DS_COL2,sales);
        values.put(DS_COL3,date);
        values.put(DS_COL4,amount);
        long result = db.insert(TABLE4NAME,null,values);
        return result;
    }
    public long addBatchdetails(String bNo,int stocks,float pPrice,float rPrice,String expDate,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PB_COL2,expDate);
        values.put(PB_COL3,bNo);
        values.put(PB_COL5,pPrice);
        values.put(PB_COL6,rPrice);
        values.put(PB_COL4,id);
        values.put(PB_COL8,stocks);
        long result = db.insert(TABLE6NAME,null,values);
        return result;
    }

    Cursor ReadAlldata(){
        String query = "SELECT * FROM " +TABLENAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
    Cursor ReadSalesData(){
        String query = "SELECT * FROM " +TABLE2NAME+" ORDER BY BILL_NO DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
    Cursor GetProduct(String code){
        String query = "SELECT * FROM "+TABLENAME +" WHERE "+P_COL2+ " = '"+code+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
    Cursor GetReturns(){
        String query = "SELECT * FROM "+TABLE5NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
//    Cursor GetExpPros(String date){
//        String query = "SELECT * FROM "+ TABLE6NAME+" WHERE "+;
//        Cursor cursor = null;
//        return cursor;
//    }
    Cursor Getsalesno(String date){
        String query = "SELECT COUNT(*) FROM "+TABLE2NAME +" WHERE "+S_COL2+ " = '"+date+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
    Cursor GetReciept(int Billno){
        String query = "SELECT * FROM "+TABLE3NAME+" WHERE "+SP_COL6+" = "+Billno;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
    Cursor GetDailysales(String date){
        String query = "SELECT * FROM "+TABLE4NAME+" WHERE "+DS_COL3+" = '"+date+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }

    Cursor GetWeeklysales(String date){
        String query = "SELECT " +DS_COL2 +" FROM "+TABLE4NAME+" WHERE "+DS_COL3+" = "+"'"+date+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
    Cursor GetBatches(int id){
        String query = "SELECT * FROM "+TABLE6NAME+" WHERE "+PB_COL4+" = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }

    Cursor Exp_pros(){
        String query = "SELECT * FROM "+TABLE6NAME+" WHERE "+PB_COL2+" < DATE('NOW')";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
    int DeleteProduct(int ID){
        String query = "DELETE FROM "+TABLENAME+" WHERE ID = "+ID;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            if(db!=null){
                db.execSQL(query);
            }
            return 1;
        }catch (Exception e){
            return 0;
        }

    }
    void Deletedata(){
        String query = "DELETE FROM daily_sales WHERE ID > 7";
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            if(db!=null){
                db.execSQL(query);
            }
            System.out.println("Success");
        }catch (Exception e){
            System.out.println(""+e);
        }
    }
    int UpdateDsdata(String date,Float amount,int sales){
        String query = "UPDATE "+TABLE4NAME+" SET "+DS_COL4+" = "+amount+", "+DS_COL2+" = "+sales+" WHERE "+DS_COL3+" = '"+date+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            if(db!=null){
                db.execSQL(query);
            }
            return 1;
        }catch (Exception e){
            return 0;
        }

    }

    int UpdateProData(String name,String unit,String code,int id){
        String query = "UPDATE "+TABLENAME+" SET "+P_COL2+" = '"+code+"', "+P_COL6+" = '"+unit+"', "+P_COL3+" = '"+name+"' "+ "WHERE ID = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            if(db!=null){
                db.execSQL(query);
            }
            return 1;
        }catch (Exception e){
            return 0;
        }

    }
    int UpdateBatchData(String Bno,String expDate,float pprice,float rprice,int id,int stock){
        String query = "UPDATE "+TABLE6NAME+" SET "+PB_COL3+" = '"+Bno+"', "+PB_COL2+" = '"+expDate+"', "+PB_COL5+" = "+pprice+", "+PB_COL6+" = "+rprice+", "+PB_COL8+" = "+stock+" WHERE ID = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            if(db!=null){
                db.execSQL(query);
            }
            return 1;
        }catch (Exception e){
            return 0;
        }

    }



    int DeleteReciept(int Billno){
        String query = "DELETE FROM "+TABLE2NAME+" WHERE BILL_NO = "+Billno;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            if(db!=null){
                db.execSQL(query);
            }
            return 1;
        }catch (Exception e){
            return 0;
        }

    }
    void Decreasestock(int qty,int id){
        String query = "UPDATE "+TABLENAME+" SET "+P_COL5+" = "+qty+" WHERE ID = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            if(db!=null){
                db.execSQL(query);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void Create_table(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String create_table2 = "CREATE TABLE "+ TABLE2NAME +"(BILL_NO INTEGER PRIMARY KEY AUTOINCREMENT, "+S_COL2+" TEXT, "+S_COL3+" TEXT, "+S_COL4+" FLOAT)";
        String create_table3 = "CREATE TABLE "+ TABLE3NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+SP_COL2+" TEXT, "+SP_COL3+" INTEGER, "+SP_COL4+" FLOAT, "+SP_COL5+" FLOAT, "+SP_COL6+" INTEGER, "+"FOREIGN KEY("+SP_COL6+") REFERENCES "+TABLE2NAME+"(BILL_NO))";
        sqLiteDatabase.execSQL(create_table2);
        sqLiteDatabase.execSQL(create_table3);
    }
    public  void Create_dstable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String create_table4 = "CREATE TABLE "+ TABLE4NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+DS_COL2+" INTEGER, "+DS_COL3+" TEXT, "+DS_COL4+" FLOAT)";
        sqLiteDatabase.execSQL(create_table4);
    }
    public  void Create_btable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String create_table6 = "CREATE TABLE "+TABLE6NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+PB_COL2+" DATE, "+PB_COL3+" TEXT, "+PB_COL4+" INTEGER, "+PB_COL5+" FLOAT, "+PB_COL8+" INTEGER, "+PB_COL6+" FLOAT, "+PB_COL7+" TEXT, "+"FOREIGN KEY(product) REFERENCES products(ID) ON DELETE CASCADE)";
        sqLiteDatabase.execSQL(create_table6);
    }
//    public  void delete_column(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        String create_table3 = "DROP TABLE pro_batches";
//        sqLiteDatabase.execSQL(create_table3);
//    }
//    public  void edit_table(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        String edit_table = "ALTER TABLE pro_batches MODIFY (exp_date DATE)";
//        sqLiteDatabase.execSQL(edit_table);
//    }
}
