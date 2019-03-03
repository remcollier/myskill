package REST.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {	
	public static WebResponse sendRequest(RequestObject requestObject) {
		try {
			// Create Connection
			HttpURLConnection connection = (HttpURLConnection) new URL(requestObject.url).openConnection();
			connection.setRequestMethod(requestObject.method);
			connection.setRequestProperty("Content-Type", requestObject.type);
			connection.setRequestProperty("User-Agent", "ASTRA/1.0");

			// Send Message Body (if existing)
			if (requestObject.content != null) {
				connection.setDoOutput(true);
				PrintStream out = new PrintStream(connection.getOutputStream());
				out.println(requestObject.content);
			}
			
			// Read Response
			int responseCode = connection.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(connection.getInputStream()));
			
			String inputLine;
			StringBuffer buf = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				buf.append(inputLine);
			}
			in.close();
			
			return new WebResponse(responseCode, buf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}