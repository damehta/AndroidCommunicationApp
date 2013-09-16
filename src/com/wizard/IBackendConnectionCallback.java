/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.wizard;

import com.google.gson.JsonObject;

public interface IBackendConnectionCallback {
	void callback(JsonObject response);
}
