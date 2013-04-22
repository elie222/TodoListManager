package il.ac.huji.todolist;

import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TodoListManagerActivity extends FragmentActivity {
	
	private static final int ADD_ITEM_REQUEST_CODE = 1337;
	private static final int SETTINGS_REQUEST_CODE = 1338;
	private static final int ADD_THUMBNAIL_REQUEST_CODE = 1339;
	
	private static final String CALL = "Call ";
	private SimpleCursorAdapter adapter;
	private ListView listTasks;
	private TodoDAL todoDal;
	private Cursor cursor;
	
	// for debugging purposes. TAG is used for printing to console.
	private static final String TAG = "TLM_ACTIVITY";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        todoDal = new TodoDAL(this);
        
        listTasks = (ListView) findViewById(R.id.lstTodoItems);
                        
		cursor = todoDal.getCursor();
        String [] from = {"title", "due"};
        int [] to = {R.id.txtTodoTitle, R.id.txtTodoDueDate};
        adapter = new TodoListCursorAdapter(this, R.layout.row, cursor, from, to);
                
        listTasks.setAdapter(adapter);
                
        registerForContextMenu(listTasks);
        
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // TODO change 1 to the most recent id already checked.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tagPref = prefs.getString(SettingsActivity.PREF_KEY_TAG, "todoapp");
        
        new TwitterAsyncTask(TodoListManagerActivity.this, tagPref, todoDal, adapter).execute();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, 
    		ContextMenuInfo menuInfo) {
    	
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
		
		cursor = adapter.getCursor();
		cursor.moveToPosition(info.position);
		
		// set the header title to be the title of the task.
		String title = cursor.getString(1);
		menu.setHeaderTitle(title);
		
		if (title.contains(CALL)) {
			menu.getItem(1).setTitle(title);
		} else {
			menu.removeItem(R.id.menuItemCall);
		}
    }
    
	@Override
	public boolean onContextItemSelected(MenuItem item) {
        
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int selectedItemIndex = info.position;
		
		cursor = adapter.getCursor();
		cursor.moveToPosition(selectedItemIndex);
		
		Task selectedItem = null;
		
		if (cursor.getString(2) == null) {
			selectedItem = new Task(cursor.getString(1), null);
		} else {
			selectedItem = new Task(cursor.getString(1), new Date(cursor.getLong(2)));
		}
		
		switch (item.getItemId())
		{
			case R.id.menuItemDelete:
				todoDal.delete(selectedItem);
				refresh();		    	
				return true;
				
			case R.id.menuItemCall:
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				String tel = selectedItem.getTitle().replace(CALL, "tel:");
			    callIntent.setData(Uri.parse(tel));
			    startActivity(callIntent);
				
			    return true;
			    
			case R.id.menuItemAddThumbnail:
				//TODO
				// show a new activity that allows the user to search for an image on flickr
				//and select it as the thumbnail for this todoitem
				
	    		Intent thumbnailIntent = new Intent(this, AddNewTodoItemActivity.class);
	    		startActivityForResult(thumbnailIntent, ADD_THUMBNAIL_REQUEST_CODE);
				
				return true;
			    
            default:
                return super.onContextItemSelected(item);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_list_manager, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {    	
    	case R.id.menuItemAdd:
    		Intent intent = new Intent(this, AddNewTodoItemActivity.class);
    		startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
    		
    		return true;
    		
    	case R.id.menuItemSettings:
    		intent = new Intent(this, SettingsActivity.class);
    		startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    		
    		return true;
    		
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
       		String title = data.getStringExtra("title");
    		Date dueDate = (Date)data.getSerializableExtra("dueDate");
    		
    		todoDal.insert(new Task(title, dueDate));
    		refresh();
    	} else if (requestCode == ADD_THUMBNAIL_REQUEST_CODE && resultCode == RESULT_OK) {
    		
    	}
    }
    
    private void refresh() {
    	cursor = todoDal.getCursor();
    	adapter.changeCursor(cursor);
    	adapter.notifyDataSetChanged();
    }
}
