package il.ac.huji.todolist;

import java.util.Date;

public class Task implements ITodoItem {
	
	private String _title;
	private Date _dueDate;
	private String _thumbnailLocation;
	
	public Task(String title, Date dueDate) {
		_title = title;
		_dueDate = dueDate;
		_thumbnailLocation = null;
	}
	
	public Task(String title, Date dueDate, String thumbnailLocation) {
		_title = title;
		_dueDate = dueDate;
		_thumbnailLocation = thumbnailLocation;
	}

	public String getTitle() {
		return _title;
	}
	
	public Date getDueDate() {
		return _dueDate;
	}
	
	public String getThumbnailLocation() {
		return _thumbnailLocation;
	}
	
	public void setThumbnailLocation(String thumbnailLocation) {
		_thumbnailLocation = thumbnailLocation;
	}
}
