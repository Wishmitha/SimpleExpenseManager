package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database_access;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHandler;

public class DatabaseTransactionDAO implements TransactionDAO {

    DBHandler dbHandler = null;

    public DatabaseTransactionDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        Transaction trans = new Transaction(date, accountNo, expenseType, amount);
 
        ContentValues values = new ContentValues();
        values.put(DBHandler.col_acc_no, accountNo);

        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dt.format(date);
        values.put(DBHandler.col_date , formattedDate);
        values.put(DBHandler.col_expense_type , expenseType.toString());
        values.put(DBHandler.col_amount, amount);

        SQLiteDatabase database = dbHandler.getWritableDatabase();
        database.insert(DBHandler.table_tansaction, null, values);
        database.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        String query = "Select * FROM " + DBHandler.table_tansaction ;
        SQLiteDatabase database = dbHandler.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        List<Transaction> transList =  new LinkedList<>();

        if (cursor.moveToFirst()) {
            do{
                Transaction trans = null;
                Date date = null;
                String strDate = cursor.getString(0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = dateFormat.parse(strDate );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                trans = new Transaction(date, cursor.getString(1) , ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transList.add(trans);
            }while (cursor.moveToNext());

        }
        cursor.close();
        database.close();
        return transList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        String query = "Select * FROM " + DBHandler.table_tansaction + " ORDER BY " + DBHandler.col_date + " LIMIT " + limit ;
        SQLiteDatabase database = dbHandler.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        List<Transaction> transactionsList =  new LinkedList<>();

        if (cursor.moveToFirst()) {
            do{
                Transaction trans = null;
                Date date = null;
                String strDate = cursor.getString(1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = dateFormat.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                trans= new Transaction(date, cursor.getString(0) , ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionsList.add(trans);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return transactionsList;
    }
}
