package il.ac.huji.todolist;

// TODO change the type of createdAt to Date.

public class Tweet {

	private long _id;
	private String _text;
	private String _createdAt;
	
	public Tweet(long id, String text, String createdAt) {
		_id = id;
		_text = text;
		_createdAt = createdAt;
	}
	
	
	public long getId() {
		return _id;
	}


	public void setId(long id) {
		this._id = id;
	}
	
	
	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
	}


	public String getCreatedAt() {
		return _createdAt;
	}


	public void setCreatedAt(String createdAt) {
		this._createdAt = createdAt;
	}
	
	
}
