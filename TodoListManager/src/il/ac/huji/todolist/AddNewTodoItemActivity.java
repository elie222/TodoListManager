package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;


public class AddNewTodoItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_todo_item_layout);
        
        setTitle("Add New Item");

        findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String title = ((EditText) findViewById(R.id.edtNewItem)).getText().toString();
				
				DatePicker dpDate = (DatePicker) findViewById(R.id.datePicker);
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(dpDate.getYear(), dpDate.getMonth(), dpDate.getDayOfMonth());
				Date dueDate = calendar.getTime();
				
				if (title == null || "".equals(title)) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					Intent resultIntent = new Intent();
					resultIntent.putExtra("title", title);
					resultIntent.putExtra("dueDate", dueDate);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
			}
		});
        
        findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
        
    }
}
