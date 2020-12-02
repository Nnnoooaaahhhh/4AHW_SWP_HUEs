import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.json.JSONException;

public class testiMain {

	public static void main(String[] args) throws JSONException, MalformedURLException, IOException, SQLException {
		methods a = new methods();
	
		a.connect(a.decide());
		a.database();
	}

}
