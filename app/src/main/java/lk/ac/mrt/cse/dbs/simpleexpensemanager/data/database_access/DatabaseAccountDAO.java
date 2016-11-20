package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database_access;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHandler;

public class DatabaseAccountDAO implements AccountDAO {

    DBHandler dbHandler = null;

    public DatabaseAccountDAO(DBHandler dbHandler){
        this.dbHandler = dbHandler;
    }

    @Override
    public List<String> getAccountNumbersList() {

        String query = "Select"+ DBHandler.col_acc_no +"FROM " + DBHandler.table_acc ;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<String> accountsList =  new LinkedList<>();

        if (cursor.moveToFirst()) {
            do{
                //add the account numbers
                accountsList.add(cursor.getString(0));
            }while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return accountsList;
    }

    @Override
    public List<Account> getAccountsList() {

        String query = "Select * FROM " + DBHandler.table_acc;
        SQLiteDatabase database = dbHandler.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        List<Account> accList =  new LinkedList<>();

        if (cursor.moveToFirst()) {
            do{
                Account account = null;
                account= new Account(cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
                accList.add(account);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return accList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {


        String exicuteQuery = "Select * FROM " + DBHandler.table_acc + " WHERE " + DBHandler.col_acc_no + " = " + "'" + accountNo + "'"  ;
        SQLiteDatabase database = dbHandler.getWritableDatabase();

        Cursor cursor = database.rawQuery(exicuteQuery, null);

        Account acc = null;

        if (cursor.moveToFirst()) {
            acc= new Account(cursor.getString(0), cursor.getString(1) , cursor.getString(2), cursor.getDouble(3));
        }

        cursor.close();
        database.close();
        return acc;
    }

    @Override
    public void addAccount(Account account) {

        //add contents ro Content Values
        ContentValues val = new ContentValues();
        val.put(DBHandler.col_acc_no, account.getAccountNo());
        val.put(DBHandler.col_bank_name , account.getBankName());
        val.put(DBHandler.col_account_holder_name , account.getAccountHolderName());
        val.put(DBHandler.col_balance, account.getBalance());


        SQLiteDatabase database = dbHandler.getWritableDatabase();
        database.insert(DBHandler.table_acc, null, val);
        database.close();
    }

    @Override
    public void removeAccount(String accNo) throws InvalidAccountException {

        String query = "Select * FROM " + DBHandler.table_acc + " WHERE " + DBHandler.col_acc_no + " = " + "'" + accNo + "'";

        SQLiteDatabase database = dbHandler.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        //if the account is available
        if (cursor.moveToFirst()) {
            String accNumber = (cursor.getString(0));
            database.delete(DBHandler.table_acc, DBHandler.col_acc_no + " = ?", new String[] { accNumber });
            cursor.close();
        }
        database.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account acc = getAccount(accountNo);
        
        ContentValues conValues = new ContentValues();
        
        conValues.put(DBHandler.col_acc_no , acc.getAccountNo());
        conValues.put(DBHandler.col_bank_name , acc.getBankName());
        conValues.put(DBHandler.col_account_holder_name , acc.getBankName());

        //select expense type
        if (expenseType == ExpenseType.INCOME){

            conValues.put(DBHandler.col_balance , acc.getBalance()+ amount);

        }else if (expenseType == ExpenseType.EXPENSE){
            conValues.put(DBHandler.col_balance , acc.getBalance()- amount);
        }

        //run the update on database
        SQLiteDatabase database = dbHandler.getWritableDatabase();
        database.update(DBHandler.table_acc , conValues , DBHandler.col_acc_no + " = " + "'" + accountNo + "'" , null);

        //the balance is allowed to be negative, because this could be a credit card or current account or similar kind os account

    }
}
