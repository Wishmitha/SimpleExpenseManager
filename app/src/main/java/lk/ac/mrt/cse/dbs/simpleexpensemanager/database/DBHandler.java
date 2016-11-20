package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    //-------------------------------------------------------variables-----------------------------------------------------------//
    private static final int db_version = 1;
    private static final String db_name = "140392M.db";

    public static final String table_acc = "`account`";
    public static final String col_acc_no = "`accountNo`";
    public static final String col_bank_name = "`bankName`";
    public static final String col_account_holder_name = "`accountHolderName`";
    public static final String col_balance = "`balance`";

    public static final String table_tansaction = "`transaction`";
    public static final String col_date = "`date`";
    public static final String col_expense_type = "`expenseType`";
    public static final String col_amount = "`amount`";

    String acc_table = "CREATE TABLE " + table_acc + "("+ col_acc_no + " VARCHAR(10) PRIMARY KEY," + col_bank_name + " VARCHAR(100)," + col_account_holder_name + " VARCHAR(200)," + col_balance + " DOUBLE "+ ")";

    String trans_table = "CREATE TABLE " + table_tansaction + "(" + col_acc_no + " VARCHAR(10)," + col_date + "VARCHAR(10," + col_expense_type + " VARCHAR(20), " + col_amount  + " DOUBLE " + " , FOREIGN KEY(" + col_acc_no + ") REFERENCES " + table_acc+"("+ col_acc_no +")"+")";

    private static DBHandler instance = null;

    public static DBHandler getInstance(Context context)
    {
        if(instance == null)
            instance = new DBHandler(context);
        return instance;
    }

    private DBHandler(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(acc_table);
        db.execSQL(trans_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_acc);
        db.execSQL("DROP TABLE IF EXISTS " + table_tansaction);
        onCreate(db);

    }

}
