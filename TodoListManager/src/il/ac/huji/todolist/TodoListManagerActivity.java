package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {
	
	private static final int ADD_ITEM_REQUEST_CODE = 1337;
	private static final String CALL = "Call ";
	private ArrayAdapter<Task> adapter;
	private ListView listTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        
        List<Task> tasks = new ArrayList<Task>();
        
        listTasks = (ListView) findViewById(R.id.lstTodoItems);
        
        adapter = new TodoDisplayAdapter(this, tasks);
        
        listTasks.setAdapter(adapter);
        
        registerForContextMenu(listTasks);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, 
    		ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
		Task t = (adapter.getItem(info.position));
		menu.setHeaderTitle(t.getTitle());
		if (t.getTitle().contains(CALL)) {
			menu.getItem(1).setTitle(t.getTitle());
		} else {
			menu.removeItem(R.id.menuItemCall);
		}  
    }
    
	@Override
	public boolean onContextItemSelected(MenuItem item) {
        
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int selectedItemIndex = info.position;
		Task selectedItem = adapter.getItem(selectedItemIndex);
		switch (item.getItemId())
		{
			case R.id.menuItemDelete:
				adapter.remove(selectedItem);
				return true;
			case R.id.menuItemCall:
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				String tel = selectedItem.getTitle().replace(CALL, "tel:");
			    callIntent.setData(Uri.parse(tel));
			    startActivity(callIntent);
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
    		//display other activity
    		Intent intent = new Intent(this, AddNewTodoItemActivity.class);
    		startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
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
    		
    		adapter.add(new Task(title, dueDate));
    	}
    }
    
}
