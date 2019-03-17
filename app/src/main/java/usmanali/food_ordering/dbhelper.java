package usmanali.food_ordering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class dbhelper extends SQLiteOpenHelper {
    Context c;
    String Table_Name="shoppingcart";
    String[] pricecolumn={"Sum(price)"};
    String[] columns={"id","productname","price","image","quantity","email","productid"};
    String creat_table="create table shoppingcart (id integer primary key autoincrement,productname text,price integer,image text,quantity integer,email text,productid integer);";
    public dbhelper(Context context) {
        super(context,"shoppingcartdb", null, 1);
        this.c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
sqLiteDatabase.execSQL(creat_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shoppingcart");
        onCreate(sqLiteDatabase);
    }
    public Boolean insert_product_toshoppingcart(String productname,int price,String imageurl,int quantity,String Username,int productid){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("productname ",productname);
        cv.put("price ",price);
        cv.put("image",imageurl);
        cv.put("quantity",quantity);
        cv.put("email",Username);
        cv.put("productid",productid);
       long i= db.insert("shoppingcart",null,cv);
        if(i==-1){
            return false;
        }else{
            return true;
        }

    }
    public  Cursor get_products_in_cart(String Username)throws SQLException {
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("shoppingcart",columns,"email = ?",new String[]{Username},null,null,null,null);
        return products;
    }
    public int get_num_of_rows(String Username){
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("shoppingcart",columns,"email = ?",new String[]{Username},null,null,null,null);
        return products.getCount();
    }

    public int getTotalOfAmount(String Username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query("shoppingcart",pricecolumn,"email = ?",new String[] {Username},null,null,null,null);
        cur.moveToFirst();
        int i = cur.getInt(0);
        cur.close();
        return i;
    }
    public Integer delete(String id){
        SQLiteDatabase sdb = this.getReadableDatabase();
        return sdb.delete("shoppingcart", "id = ?", new String[] {id});
    }
    public void delete_all(String Username){
        SQLiteDatabase sdb = this.getReadableDatabase();
         sdb.delete("shoppingcart", "email = ?", new String[] {Username});
    }

}
