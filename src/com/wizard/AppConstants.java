/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */


package com.wizard;

import android.content.Context;
import android.content.Intent;

public class AppConstants {
	public static final String TAG = "Contact List";
	public static final String BaseUrl = "http://localhost:8000/";

	public static final String LoginExtension = "api/tn";
	public static final String AppidExtension = "api/appid";
	public static final String CallExtension = "api/call";
	public static final String TokenExtension = "api/token";
	public static final String ConfExtension = "api/conf";

	public static final int MAX_ATTEMPTS = 5;
	public static final int BACKOFF_MILLI_SECONDS = 2000;
	public static final String Error = "error";
	public static final String UserPhone = "userPhone";
	public static final String FromGCM = "fromGCM";
	public static final String Token = "token";
	public static final String IsListenMode = "isListenMode";
	public static final String userPhone = "+14088377852";

	public final int maxSelection = 5;
	/**
	 * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
	 */
	static final String SERVER_URL = "http://localhost:8080";

	/**
	 * Google API project id registered to use GCM.
	 */
	public static final String SENDER_ID = "361993694766";

	/**
	 * Intent used to display a message in the screen.
	 */
	public static final String DISPLAY_MESSAGE_ACTION = "DISPLAY_MESSAGE";

	/**
	 * Intent's extra that contains the message to be displayed.
	 */
	public static final String EXTRA_MESSAGE = "message";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}
