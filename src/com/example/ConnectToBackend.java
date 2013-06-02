/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.example;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Application;
import android.os.AsyncTask;

import com.example.MainActivity;
import com.google.gson.JsonObject;

public class ConnectToBackend extends AsyncTask<String, Integer, JsonObject> {
	String url;
	List<NameValuePair> nameValuePairs;
	IBackendConnectionCallback callbackActivity;
	MainActivity app;
	String protocol;
	
	public ConnectToBackend(String url, List<NameValuePair> nameValuePairs, 
			IBackendConnectionCallback callback, MainActivity app, String protocol) {
		this.url = url;
		this.nameValuePairs = nameValuePairs;
		this.callbackActivity = callback;
		this.app = app;
		this.protocol = protocol;
	}

	public ConnectToBackend(String url, List selectedContactList, MainActivity application, String protocol) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.nameValuePairs = selectedContactList;
		this.app = application;
		this.protocol = protocol;
	}

	@Override
	protected JsonObject doInBackground(String... arg0) {
		// Add user name and password
//		return Util.postData(app, url, nameValuePairs, protocol);
		return null;
	} //

	@Override
	protected void onPostExecute(JsonObject result) { //
//		callbackActivity.callback(result);
	}
}