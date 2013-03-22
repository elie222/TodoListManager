package il.ac.huji.todolist;

import java.util.Date;

public class Task {
	
	private String _title;
	private String _dueDate;
	
	public Task(String title, Date dueDate) {
		_title = title;
		_dueDate = dueDate.toString();
	}
	
	public Task(String title, String dueDateString) {
		_title = title;
		_dueDate = dueDateString;
	}

	public String getTitle() {
		return _title;
	}
	
	public String getDueDate() {
		return _dueDate;
	}
}
