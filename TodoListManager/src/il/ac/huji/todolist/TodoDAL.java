package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
import android.util.Log;


public class TodoDAL extends SQLiteOpenHelper {
	
	// Database Name
    private static final String DATABASE_NAME = "todo_db";
 
    // Contacts table name
    public static final String TABLE_TODO = "todo";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DUE = "due";
    public static final String KEY_THUMBNAIL_LOC = "thumbnailLoc";
    
    // Database Version
    private static final int DATABASE_VERSION = 4;
    
    // for debugging purposes.
    private static final String TAG = "TODO_DAL";
    
    
    
	public TodoDAL(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
        // Parse
        Parse.initialize(context,
        		context.getResources().getString(R.string.parseApplication),
        		context.getResources().getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();
	}
	
	public Cursor getCursor() {
		String [] columns = new String[] { KEY_ID, KEY_TITLE, KEY_DUE, KEY_THUMBNAIL_LOC };
		return this.getReadableDatabase().query("todo", columns, null, null, null, null, null);
	}
	
	public boolean insert(ITodoItem todoItem) {
		
		if (todoItem == null) {
			return false;
		}
		
		if (todoItem.getTitle() == null) {
			return false;
		}
				
		try {
			// DB
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			
			//title
			values.put(KEY_TITLE, todoItem.getTitle());
			
			//dueDate
			if (todoItem.getDueDate() != null) {
				values.put(KEY_DUE, todoItem.getDueDate().getTime());
			} else {
				values.putNull(KEY_DUE);
			}
			
			values.putNull(KEY_THUMBNAIL_LOC);
			
			long r = db.insert(TABLE_TODO, null, values);
			db.close();

			if (r == -1) {
				return false;
			}

			// Parse
			ParseObject obj = new ParseObject(TABLE_TODO);
			obj.put(KEY_TITLE, todoItem.getTitle());

			if (todoItem.getDueDate() != null) {
				obj.put(KEY_DUE, todoItem.getDueDate().getTime());
			} else {
				obj.put(KEY_DUE, JSONObject.NULL);
			}

			obj.saveInBackground();
			
		} catch (Exception e) {
			return false;
		}
				
		return true;
	}
	
	public boolean update(ITodoItem todoItem) {
		
		if (todoItem == null) {
			return false;
		}
		
		if (todoItem.getTitle() == null) {
			return false;
		}
		
		try {
			// DB
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			
			//title
			values.put(KEY_TITLE, todoItem.getTitle());
			
			//dueDate
			if (todoItem.getDueDate() != null) {
				values.put(KEY_DUE, todoItem.getDueDate().getTime());
			} else {
				values.putNull(KEY_DUE);
			}
			
			//this deletes the thumbnail image associated with the item.
			//don't really want to do this, but the app doesn't use this function anyway.
			values.putNull(KEY_THUMBNAIL_LOC);
			
			String where = KEY_TITLE + " = ?";
			String[] whereArgs = {todoItem.getTitle()};

			long r = db.update(TABLE_TODO, values, where, whereArgs);
			db.close();

			if (r == -1) {
				return false;
			}
			
			// Parse
			final Date dueDate = todoItem.getDueDate();
			
			ParseQuery query = new ParseQuery(TABLE_TODO);
			query.whereEqualTo(KEY_TITLE, todoItem.getTitle());
			query.findInBackground(new FindCallback() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			        if (e == null) {
			        	if (scoreList.size() == 1) {
			        		ParseObject objToUpdate = scoreList.get(0);
			        		if (dueDate != null) {
			        			objToUpdate.put(KEY_DUE, dueDate.getTime());
			        		} else {
			        			objToUpdate.put(KEY_DUE, JSONObject.NULL);
			        		}
			        		objToUpdate.saveInBackground();
			        	}
			        } else {
//			            Log.d("TodoDAL", "Error: " + e.getMessage());
			        }
			    }
			});
			
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	//added in hw5
	//TODO add thumbnail to parse too.
	public boolean updateThumbnail(String title, String thumbnailId) {
		try {
			// DB
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			
			if (thumbnailId != null) {
				values.put(KEY_THUMBNAIL_LOC, thumbnailId);
			} else {
				values.putNull(KEY_THUMBNAIL_LOC);
			}
			
			String where = KEY_TITLE + " = ?";
			String[] whereArgs = {title};
	
			long r = db.update(TABLE_TODO, values, where, whereArgs);
			db.close();
			
			if (r == -1) {
				return false;
			}
			
			// TODO Parse
		
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public boolean delete(ITodoItem todoItem) {
		
		if (todoItem == null) {
			return false;
		}
		
		if (todoItem.getTitle() == null) {
			return false;
		}
		
		try {
			// DB
			SQLiteDatabase db = this.getWritableDatabase();

			String where = KEY_TITLE + " = ?";
			String[] whereArgs = {todoItem.getTitle()};

			long r = db.delete(TABLE_TODO, where, whereArgs);
			db.close();
			
			if (r == -1) {
				return false;
			}
			
			// Parse			
			ParseQuery query = new ParseQuery(TABLE_TODO);
			query.whereEqualTo(KEY_TITLE, todoItem.getTitle());
			query.findInBackground(new FindCallback() {
			    public void done(List<ParseObject> scoreList, ParseException e) {
			        if (e == null) {
			        	if (scoreList.size() == 1) {
			        		ParseObject objToDelete = scoreList.get(0);
			        		objToDelete.deleteInBackground();
			        	}
			        } else {
//			            Log.d("TodoDAL", "Error: " + e.getMessage());
			        }
			    }
			});
			
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public List<ITodoItem> all() {		
		List<ITodoItem> todoItemList = new ArrayList<ITodoItem>();
		
	    String queryStr = "SELECT * FROM " + TABLE_TODO;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(queryStr, null);
	 
	    if (cursor.moveToFirst()) {
	        do {
	            Task task = new Task(cursor.getString(1), new Date(cursor.getLong(2)));
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
				+ KEY_DUE + " long, "
				+ KEY_THUMBNAIL_LOC + " text);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		onCreate(db);
		
		//TODO drop parse table
	}
}
