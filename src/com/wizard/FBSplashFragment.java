/**
 * 
 */
package com.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author AMehta
 * 
 */
public class FBSplashFragment extends android.support.v4.app.Fragment {

	/**
	 * 
	 */
	public FBSplashFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fb_splash, container, false);
		return view;
	}

}
