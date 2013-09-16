/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.wizard;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import net.hockeyapp.android.CheckUpdateTask;


public class Util {
	private static final String TAG = "Util";
	private static final Random random = new Random();
    /**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "361993694766";


	private static JsonObject readInputStream(AndroidHttpClient client,
			InputStream is) {
		if (is != null) {
			JsonObject jObject = null;
			try {
				String str = toString(is);
				int idx = str.indexOf("{");
				if (idx > -1) {
					str = str.substring(idx);
				}
				Reader reader = new InputStreamReader(is);

				JsonParser jParser = new JsonParser();
				jObject = jParser.parse(str).getAsJsonObject();
			} catch (Exception ex) {
				return null;
			} finally {
				client.close();
			}
			return jObject;
		}
		client.close();
		return null;
	}

	public static String toString(InputStream is) {
		final int buffer_size = 1024;
		StringBuilder builder = new StringBuilder("");
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				builder.append(new String(bytes, 0, count, "UTF-8"));
			}
		} catch (Exception ex) {
			return null;
		}
		return builder.toString();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static String checkPhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.length() < 10) {
			return null;
		}

		phoneNumber = phoneNumber.replace(" ", "").replace("(", "")
				.replace(")", "").replace("+", "").replace("-", "");
		if (phoneNumber.startsWith("1")) {
			if (phoneNumber.length() != 11) {
				return null;
			} else {
				return "+" + phoneNumber;
			}
		} else if (phoneNumber.length() != 10) {
			return null;
		} else {
			try {
				long phone = Long.parseLong(phoneNumber);
			} catch (Exception ex) {
				return null;
			}
		}
		return "+1" + phoneNumber;
	}

	public static String formatPhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.length() != 12) {
			return null;
		}

		return "(" + phoneNumber.substring(2, 5) + ") "
				+ phoneNumber.substring(5, 8) + "-" + phoneNumber.substring(8);
	}

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 */
	private static void post(String endpoint, Map<String, String> params)
			throws IOException {
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder(params.get("inputJson"));
		// Iterator<Entry<String, String>> iterator =
		// params.entrySet().iterator();
		// // constructs the POST body using the parameters
		// while (iterator.hasNext()) {
		// Entry<String, String> param = iterator.next();
		// bodyBuilder.append(param.getKey()).append('=')
		// .append(param.getValue());
		// if (iterator.hasNext()) {
		// bodyBuilder.append('&');
		// }
		// }
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}


	/**
	 * Function to convert milliseconds time to Timer Format
	 * Hours:Minutes:Seconds
	 * */
	public static String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Add hours if there
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}


}

class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
	public static final String METHOD_NAME = "DELETE";

	public String getMethod() {
		return METHOD_NAME;
	}

	public HttpDeleteWithBody(final String uri) {
		super();
		setURI(URI.create(uri));
	}

	public HttpDeleteWithBody(final URI uri) {
		super();
		setURI(uri);
	}

	public HttpDeleteWithBody() {
		super();
	}
}
