/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database_access.DatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database_access.DatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHandler;

public class PersistentExpenseManager extends ExpenseManager {

    private Context con = null;
    private DBHandler dbHandler = null;

    public PersistentExpenseManager(Context context) {
        this.con = context;
        dbHandler = DBHandler.getInstance(con);
        setup();
    }

    @Override
    public void setup() {

        TransactionDAO memoryTransactionDAO = new DatabaseTransactionDAO(dbHandler);
        setTransactionsDAO(memoryTransactionDAO);

        AccountDAO memoryAccountDAO = new DatabaseAccountDAO(dbHandler);
        setAccountsDAO(memoryAccountDAO);

    }
}