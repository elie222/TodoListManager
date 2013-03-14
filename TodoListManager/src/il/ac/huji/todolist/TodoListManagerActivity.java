package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {
	
	private ArrayAdapter<Task> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        
        List<Task> tasks = new ArrayList<Task>();
        
        ListView listTasks = (ListView) findViewById(R.id.lstTodoItems);
        
        adapter = new TodoDisplayAdapter(this, tasks);
        
        listTasks.setAdapter(adapter);
        
        registerForContextMenu(listTasks);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_list_manager, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {    	
    	case R.id.menuItemAdd:
    		EditText edtTxtTask = (EditText) findViewById(R.id.edtNewItem);  		
    		adapter.add(new Task(edtTxtTask.getText().toString()));
    		edtTxtTask.setText("");
    		break;
    		
    	case R.id.menuItemDelete:
    		
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        	
        	int selectedItemIndex = info.position;
    		
    		
    		if (selectedItemIndex < 0) {
    			return true;
    		}
    		adapter.remove(adapter.getItem(selectedItemIndex));
    		break;
    	
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    	
    	return true;
  
    }
    
}
