package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TodoListCursorAdapter extends SimpleCursorAdapter {
	
	private Context _context;

	@SuppressWarnings("deprecation")
	public TodoListCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		_context = context;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.row, null);
		
		Cursor cursor = (Cursor) getItem(position);
		cursor.moveToPosition(position);
		
		Task task = null;
		
		if (cursor.getString(2) == null) {
			task = new Task(cursor.getString(1), null);
		} else {
			task = new Task(cursor.getString(1), new Date(cursor.getLong(2)));
		}
		
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTodoTitle);
		TextView txtDueDate = (TextView) view.findViewById(R.id.txtTodoDueDate);
		
		txtTitle.setText(task.getTitle());
		
		if (task.getDueDate() == null) {
			txtDueDate.setText("No due date");
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			Date currentDate = new Date(System.currentTimeMillis());
			
			String formattedDueDate = dateFormat.format(task.getDueDate());
			String formattedCurrentDate = dateFormat.format(currentDate);
			txtDueDate.setText(formattedDueDate);
			
			if (currentDate.after(task.getDueDate())
					&& !(formattedDueDate.equals(formattedCurrentDate)))
			{
				txtTitle.setTextColor(Color.RED);
				txtDueDate.setTextColor(Color.RED);
			}
		}
		
		// Thumbnail
		String location = cursor.getString(3);
//		Log.d("CURSOR ADAPTER", "HERE1");
		if (location != null) {
			Log.d("CURSOR ADAPTER", "IMG LOCATION: " + location);
			Bitmap bm = BitmapFactory.decodeFile(location);
			
			ImageView imgThumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
			imgThumbnail.setImageBitmap(bm);
		}
//		Log.d("CURSOR ADAPTER", "HERE3");
		
		return view;
	}
}
