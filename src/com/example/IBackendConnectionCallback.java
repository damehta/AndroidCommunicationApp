/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.example;

import com.google.gson.JsonObject;

public interface IBackendConnectionCallback {
	void callback(JsonObject response);
}
