package il.ac.huji.todolist;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
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
		return false;
	}
	
	public boolean delete(ITodoItem todoItem) {
		return false;
	}
	
	public List<ITodoItem> all() {
		return null;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_TODO +
				" ( " + KEY_ID + " integer primary key autoincrement, "
				+ KEY_TITLE + " text, "
				+ KEY_DUE + " long);");//TODO should due be long or int?

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
