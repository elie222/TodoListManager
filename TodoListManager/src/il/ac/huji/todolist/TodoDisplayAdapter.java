package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoDisplayAdapter extends ArrayAdapter<Task> {

	public TodoDisplayAdapter(TodoListManagerActivity activity, List<Task> tasks) {
		super(activity, android.R.layout.simple_list_item_1, tasks);
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Task task = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		
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
			
			if (currentDate.after(task.getDueDate()) && !(formattedDueDate.equals(formattedCurrentDate)))
			{
				txtTitle.setTextColor(Color.RED);
				txtDueDate.setTextColor(Color.RED);
			}
		}
		return view;
	}
}
