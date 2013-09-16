/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.wizard;

import android.content.Context;
import android.util.Log;

import com.twilio.client.Twilio;
//import com.twilio.client.impl.net.HttpHelper;
import com.twilio.client.Device;

public class GroupPhone implements Twilio.InitListener {
	public GroupPhone(Context context) {
		Twilio.initialize(context, this /* Twilio.InitListener */);
	}

	@Override
	/* Twilio.InitListener method */
	public void onInitialized() {
		Log.d(AppConstants.TAG, "Twilio SDK is ready");

		try {
//			String capabilityToken = HttpHelper.httpGet(AppConstants.BaseUrl + AppConstants.AppidExtension);
//			device = Twilio.createDevice(capabilityToken, null /* DeviceListener */);
		} catch (Exception e) {
			Log.e(AppConstants.TAG, "Failed to obtain capability token: " + e.getLocalizedMessage());
		}
	}

	@Override
	/* Twilio.InitListener method */
	public void onError(Exception e) {
		Log.e(AppConstants.TAG, "Twilio SDK couldn't start: " + e.getLocalizedMessage());
	}

	@Override
	protected void finalize() {
//		if (device != null)
//			device.release();
	}
}
