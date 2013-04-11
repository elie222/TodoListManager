package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDAL extends SQLiteOpenHelper {
	
	// Database Name
    private static final String DATABASE_NAME = "todo_db";
 
    // Contacts table name
    private static final String TABLE_TODO = "todo";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DUE = "due";
	
	public TodoDAL(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}
	
	public boolean insert(ITodoItem todoItem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, todoItem.getTitle());
		values.put(KEY_DUE, todoItem.getDueDate().getTime());

		long r = db.insert(TABLE_TODO, null, values);
		db.close();
		
		return r != -1;
	}
	
	public boolean update(ITodoItem todoItem) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, todoItem.getTitle());
		values.put(KEY_DUE, todoItem.getDueDate().getTime());

		String where = KEY_TITLE + " = ?";
		String[] whereArgs = {todoItem.getTitle()};
		
		long r = db.update(TABLE_TODO, values, where, whereArgs);
		db.close();
		
		return r != -1;
	}
	
	public boolean delete(ITodoItem todoItem) {
		SQLiteDatabase db = this.getWritableDatabase();

		String where = KEY_TITLE + " = ?";
		String[] whereArgs = {todoItem.getTitle()};
		
		long r = db.delete(TABLE_TODO, where, whereArgs);
		db.close();
		
		return r != -1;
	}
	
	@SuppressWarnings("deprecation")
	public List<ITodoItem> all() {		
		List<ITodoItem> todoItemList = new ArrayList<ITodoItem>();
		
	    // Select All Query
	    String selectQuery = "SELECT * FROM " + TABLE_TODO;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Task task = new Task(cursor.getString(1), new Date(cursor.getString(2)));
	            // Adding contact to list
	            todoItemList.add(task);
	        } while (cursor.moveToNext());
	    }
		
		return todoItemList;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_TODO +
				" ( " + KEY_ID + " integer primary key autoincrement, "
				+ KEY_TITLE + " text, "
				+ KEY_DUE + " long);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
