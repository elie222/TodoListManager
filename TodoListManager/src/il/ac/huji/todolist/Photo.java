package il.ac.huji.todolist;

import java.net.MalformedURLException;
import java.net.URL;

public class Photo {
	
	private String _id;
	private String _secret;
	private String _server;
	private int _farm;

	public Photo(String id, String secret, String server, int farm) {
		_id = id;
		_secret = secret;
		_server = server;
		_farm = farm;
	}
	
	public URL getImageThumbnailURL() throws MalformedURLException {
		return new URL("http://farm"+_farm+".staticflickr.com/"+_server+"/"+_id+"_"+_secret+"_t.jpg");
		//http://farm9.staticflickr.com/8522/8680479597_e5c5bf755d_t.jpg
	}
	
	public String getId() {
		return _id;
	}

}
