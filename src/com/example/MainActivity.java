/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.example;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.R;
import com.twilio.client.Device;

public class MainActivity extends ListActivity {
	private Activity curentActivity;
	private ToggleButton speakerBtn;
	private ToggleButton selectBtn;
	private Button sendTextBtn;
	private int count;
	private ContactAdapter mAdapter;
	protected CheckBox selectedContact;
	protected String[] arrId;
	protected ContactList contactList = new ContactList();
	protected Parcelable state;
	Activity parentActivity;
	Device device = null;
	private boolean isSpeakerEnabled = false;
	private boolean isCallConnected = false;
	private boolean isInitInProgress = false;
	private boolean isInstantiated = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		selectedContact = (CheckBox) findViewById(R.id.selectContect);
		Log.d("MainActivity.java", "onCreate - After mContactList ");
		// mSelectContactControl = (CheckBox) findViewById(R.id.selectContect);
		// mSelectContact = false;
		// mSelectContactControl.setChecked(mSelectContact);

		// Populate the contact list
		populateContactList();
		// ContactAdapter adapter = new ContactAdapter(this,
		// R.layout.activity_main, getContacts(), null, null,null);
		// this.setListAdapter(getListAdapter());
		selectBtn = (ToggleButton) findViewById(R.id.placeCall);
		sendTextBtn = (Button) findViewById(R.id.TextMsg);
		speakerBtn = (ToggleButton) findViewById(R.id.btnSpeaker);

		speakerBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				enableSpeakerphone();
			}
		});
		sendTextBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendTextMsg();
			}
		});
		selectBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final int len = count;
				int cnt = 0;
				Log.d("selectedContacts", "selectedContacts onClick");
				if (mAdapter.selectedContactList.size() < 2) {
					Toast.makeText(getBaseContext(), (String) "Please select at least 2 contacts", Toast.LENGTH_LONG)
							.show();
					selectBtn.setChecked(false);
				} else {
					handleConnect(v);
				}
				Log.d("selectedContacts", "selectedContacts onClick" + mAdapter.selectedContactList);

				// for (int i = 0; i < len; i++) {
				// if (selectedContact.isChecked()) {
				// cnt++;
				//
				// Log.d("selectedContacts", "selectedContacts: "
				// + i);
				// // selectedContacts = selectedContacts +
				// contactList.getContact(i).getName() + ",";
				// }
				// }
				Log.d("selectedContacts", "selectedContacts: " + len);
				// if (cnt == 0){
				// Toast.makeText(getApplicationContext(),
				// "Please select at least one image",
				// Toast.LENGTH_LONG).show();
				// } else {
				// Toast.makeText(getApplicationContext(),
				// "You've selected Total " + cnt + " image(s).",
				// Toast.LENGTH_LONG).show();
				// // Log.d("selectedContacts", selectedContacts);
				// Log.d("selectedContacts", "selectedContacts else");
				// }

			}
		});

	}

	private void handleConnect(View v) {
		if (isCallConnected) {
			isCallConnected = false;
			// disconnect();
			isSpeakerEnabled = true;
			enableSpeakerphone();
			speakerBtn.setChecked(false);
			speakerBtn.setEnabled(false);
		} else {
			isCallConnected = true;
			connect();
			speakerBtn.setEnabled(true);
		}

	}

	public void connect() {
		Log.d("connect", "connect 1");
		// capability.generate(expires=30);
		// Long expiration = (Long)
		// device.getCapabilities().get(Capability.EXPIRATION);
		Long expiration = (long) 30;
		Log.d("connect", "connect 1" + expiration);
		if (!isCallConnected
				&& (expiration <= (System.currentTimeMillis() / 1000) || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)) {
			isCallConnected = true;
			Log.d("connect", "connect before getToken");
			getToken();
		} else {
			getToken();
		}

		Log.d("connect", "connect 2 " + expiration);
	}

	private void getToken() {

		Log.d("connect", "getToken 1");
		if (isInitInProgress) {
			return;
		}

		Log.d("connect", "getToken 2");
		if (device != null) {
			device.release();
			device = null;
		}
		isInitInProgress = true;
		Log.d("connect", "getToken 3");

		String url = AppConstants.BaseUrl + AppConstants.TokenExtension + "/" + AppConstants.userPhone.replace("+", "");
		isInstantiated = true;

		Log.d("connect", "url" + url);
		new ConnectToBackend(url, (List) mAdapter.selectedContactList, this, "GET").execute((String) null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		// Log.d("MainActivity.java", "onCreateOptionsMenu");
		return true;
	}

	private void enableSpeakerphone() {
		AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		if (!isSpeakerEnabled) {
			audioManager.setSpeakerphoneOn(true);
			isSpeakerEnabled = true;
			speakerBtn.setClickable(true);
			Log.d("selectedContacts", "speakerBtn: True");
		} else {
			audioManager.setSpeakerphoneOn(false);
			isSpeakerEnabled = false;
			speakerBtn.setClickable(false);
			Log.d("selectedContacts", "speakerBtn: False");
		}
	}

	private void sendTextMsg() {
		String phoneNumber = "";
		Log.d("sendTextMsg", "sendTextMsg onClick" + mAdapter.selectedContactList);
		if (mAdapter.selectedContactList.size() < 1) {
			Toast.makeText(getBaseContext(), (String) "Please select at least one contact", Toast.LENGTH_LONG).show();
		} else if (mAdapter.selectedContactList.size() > 5) {
			Toast.makeText(getBaseContext(), (String) "Please select max 5 contacts", Toast.LENGTH_LONG).show();
		} else {
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Invitation SMS")
					.setMessage("Are you sure you want to send SMS?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String message = "Msg from android app";
							String csvPhoneNumbers = mAdapter.selectedContactList.toString().replace("[", "").replace("]", "").replace(", ", ",");
							csvPhoneNumbers = csvPhoneNumbers +",4323634828";
							Log.d("sendTextMsg", "sendTextMsg -- csv phone number list " + csvPhoneNumbers);
							SmsManager sms = SmsManager.getDefault();
							sms.sendTextMessage(csvPhoneNumbers, null, message, null, null);
//							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + csvPhoneNumbers)));
						}

					}).setNegativeButton("No", null).show();
			// WORKING code to invoke Native SMS interface //
			// Uri uri = Uri.parse("smsto:" + csv);
			// Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			// it.putExtra("sms_body", message);
			// startActivity(it);
			// WORKING code to invoke Native SMS interface //
		}

	}

	private void populateContactList() {
		// Build adapter with contact entries

		Log.d("MainActivity.java", "populateContactList start");
		Cursor cursor = getContacts();
		this.count = cursor.getCount();
		String[] fields = new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE,
				ContactsContract.CommonDataKinds.Phone.LABEL };
		// SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		// R.layout.contact_item, cursor, fields,
		// new int[] { R.id.selectContect });
		// SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		// Android.R.layout.simple_list_item_multiple_choice, c, columns, new
		// int {android.R.id.text1});
		// mContactList.setAdapter(adapter);
		mAdapter = new ContactAdapter(this, contactList, R.layout.contact_item, cursor, fields,
				new int[] { R.id.selectContect }, null);
		this.setListAdapter(mAdapter);

		// this.getListAdapter().
		// for (int i = 0; i < this.count; i++) {
		// cursor.moveToPosition(i);
		// this.names[i] = cursor
		// .getString(cursor
		// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		// this.arrId[i] = cursor
		// .getString(cursor
		// .getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
		// Log.d("populateContactList", " " + this.arrId[i]);
		// }
		Log.d("MainActivity.java", " " + this.count);
		Log.d("MainActivity.java", "adapter.selectedContactList" + mAdapter.selectedContactList.toString());
		ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// cursor.close();
		Log.d("MainActivity.java", "populateContactList end");
	}

	/**
	 * Obtains the contact list for the currently selected account.
	 * 
	 * @return A cursor for for accessing the contact list.
	 */
	private Cursor getContacts() {
		ContentResolver cr = getContentResolver();
		Log.d("MainActivity.java", "getContacts start");
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL };

		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		Log.d("MainActivity.java", "getContacts end");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Contact c = new Contact();
				c.setId(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
				c.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
				c.setNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				// c.setType(cursor.getString(cursor
				// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
				int colPhoneTypeIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
				int type = cursor.getInt(colPhoneTypeIndex);
				String label = null;
				if (type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
					label = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
				}
				String text = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(getResources(), type, label);
				c.setType(text);
				c.setIsSelected(false);
				contactList.addContact(c);
			}
		}
		cursor.moveToFirst();
		return cursor;

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		state = getListView().onSaveInstanceState();
		outState.putParcelable("S", state);
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateContactList();
		if (state != null) {
			getListView().onRestoreInstanceState(state);
			state = null;
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle instate) {
		super.onRestoreInstanceState(instate);
		state = instate.getParcelable("S");
	}

	// @Override
	// protected void onListItemClick(ListView l, View v, int position, long id)
	// {
	// super.onListItemClick(l, v, position, id);
	// Object o = this.getListAdapter().getItem(position);
	// Contact c = (Contact) o;
	// Toast.makeText(this, c.getName(), Toast.LENGTH_SHORT).show();
	// }

	// private String getSavedItems() {
	// String savedItems = "";
	// int count = this.getListAdapter().getCount();
	// for (int i = 0; i < count; i++) {
	// if (this.mContactList.isItemChecked(i)) {
	// if (savedItems.length() > 0) {
	// savedItems += "," + this.mContactList.getItemAtPosition(i);
	// } else {
	// savedItems += this.mContactList.getItemAtPosition(i);
	// }
	// }
	// }
	// return savedItems;
	// }
}
