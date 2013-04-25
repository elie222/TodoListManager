package il.ac.huji.todolist;

import java.util.Date;

public class Task implements ITodoItem {
	
	private String _title;
	private Date _dueDate;
	
	public Task(String title, Date dueDate) {
		_title = title;
		_dueDate = dueDate;
	}

	public String getTitle() {
		return _title;
	}
	
	public Date getDueDate() {
		return _dueDate;
	}
}
