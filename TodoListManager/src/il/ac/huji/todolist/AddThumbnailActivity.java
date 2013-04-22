package il.ac.huji.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddThumbnailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_thumbnail_layout);
        
        setTitle("Add Thumbnail");

        findViewById(R.id.btnOkThumbnailDialog).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent resultIntent = new Intent();
//				resultIntent.putExtra("title", title);
//				resultIntent.putExtra("dueDate", dueDate);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
        
        findViewById(R.id.btnCancelThumbnailDialog).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
        
    }
}
