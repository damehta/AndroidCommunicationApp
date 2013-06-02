/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {
	static final String TAG = "NetworkReceiver";
	public static boolean isNetworkDown = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isNetworkDown = intent.getBooleanExtra(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, false); //
		if (isNetworkDown) {
			Log.d(TAG, "onReceive: NOT connected, stopping UpdaterService");
			isNetworkDown = true;
		} else {
			Log.d(TAG, "onReceive: connected, starting UpdaterService");
			isNetworkDown = false;
		}
	}

}