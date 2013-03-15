package il.ac.huji.todolist;

import java.util.List;

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
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Task task = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		
		txtTitle.setText(task.getName());
		
		if (position % 2 == 1) {
			txtTitle.setTextColor(Color.BLUE);
		} else {
			txtTitle.setTextColor(Color.RED);
		}
		
		return view;
		
	}
	
}
