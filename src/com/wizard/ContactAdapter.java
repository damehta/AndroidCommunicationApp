/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.wizard;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wizard.AppConstants;
import com.wizard.R;

public class ContactAdapter extends CursorAdapter {

	private Context mContext;
	private Cursor mCursor;
	private LayoutInflater mInflater;
	private ContactList contactList;
	public ArrayList<String> selectedContactList = new ArrayList<String>();

	public ContactAdapter(Context context, ContactList contactList, int layout, Cursor c, String[] from, int[] to,
			ContentResolver co) {
		super(context, c);
		this.mCursor = c;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.contactList = contactList;
	}

	static class ViewHolder {
		protected TextView name;
		protected TextView contactType;
		protected TextView contactLabel;
		protected TextView contactNumber;
		private CheckBox checkbox;
		private int id;

		protected void setContact(Contact contact) {
			name.setText(contact.getName());
			contactNumber.setText(contact.getNumber());
			contactType.setText(contact.getType());
			// contactLabel.setText(contact.getLabel());
			this.checkbox.setSelected(contact.isSelected());
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = this.mInflater.inflate(R.layout.contact_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.selectContect);
			// viewHolder.name
			// .setText(this.mCursor.getString(this.mCursor
			// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			// viewHolder.name.setText(contactList.getContact(position).getName());
			viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.selectContect);
			viewHolder.checkbox.setChecked(contactList.getContact(position).isSelected());

			viewHolder.contactType = (TextView) convertView.findViewById(R.id.contactType);
			viewHolder.contactNumber = (TextView) convertView.findViewById(R.id.contactNumer);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// this.mCursor.moveToPosition(position);
		viewHolder.checkbox.setId(position);
		viewHolder.checkbox.setTag(position);
		viewHolder.name.setText(contactList.getContact(position).getName());
		viewHolder.contactType.setText(contactList.getContact(position).getType());
		viewHolder.contactNumber.setText(contactList.getContact(position).getNumber());
		viewHolder.checkbox.setChecked(contactList.getContact(position).isSelected());

		final int pos = position;
		viewHolder.checkbox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("ContactAdapter.java", "contactCheckbox.setOnClickListener Checkbox OnClick"
						+ selectedContactList.size());
				CheckBox cb = (CheckBox) v;

				if (cb.isChecked()) {
					if (selectedContactList.size() >= AppConstants.MAX_ATTEMPTS) {
						cb.setChecked(false);
						Toast.makeText(mContext, (String) "Group call is limited to 5 contacts", Toast.LENGTH_LONG)
								.show();
					} else {
//						cb.setChecked(true);
						contactList.getContact(pos).setIsSelected(true);
						Log.d("ContactAdapter.java", "contactCheckbox. false");
						selectedContactList.add(contactList.getContact(pos).getNumber());
					}
				} else {
//					cb.setChecked(false);
					contactList.getContact(pos).setIsSelected(false);
					Log.d("ContactAdapter.java", "contactCheckbox. true" + contactList.getContact(pos).getNumber());

					if (selectedContactList.contains((String) contactList.getContact(pos).getNumber())) {
						selectedContactList.remove((String) contactList.getContact(pos).getNumber());
					}
				}
				Log.d("ContactAdapter.java", "contactCheckbox. true" + selectedContactList.toString());
			}

		});
		// viewHolder.name
		// .setText(this.mCursor.getString(this.mCursor
		// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
		// viewHolder.checkbox.setChecked(contactList.getContact(position)
		// .isSelected());
		// viewHolder.checkbox.setChecked(false);
		return convertView;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.contact_item, parent, false);
		bindView(v, mContext, cursor);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView contactName = (TextView) view.findViewById(R.id.selectContect);
		contactName.setText(cursor.getString((mCursor
				.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));

		// adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
		// // public boolean setViewValue(View view, Cursor cursor,
		// // int columnIndex) {
		// int colPhoneTypeIndex = cursor
		// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
		// if (columnIndex != colPhoneTypeIndex) {
		// return false;
		// }
		// int type = cursor.getInt(colPhoneTypeIndex);
		// String label = null;
		// if (type == Phone.TYPE_CUSTOM) {
		// label = cursor.getString(cursor
		// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
		// }
		// String text = (String) Phone.getTypeLabel(getResources(), type,
		// label);
		// ((TextView) view).setText(text.trim());
		// return true;
		// }
		// });
		// CheckBox contactCheckbox = (CheckBox) view
		// .findViewById(R.id.selectContect);
		// contactCheckbox.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// CheckBox checkBox = (CheckBox) v
		// .findViewById(R.id.selectContect);
		// if (checkBox.isChecked()) {
		// Log.d("ContactAdapter.java",
		// "contactCheckbox.setOnClickListener false");
		// checkBox.setChecked(false);
		// } else {
		// Log.d("ContactAdapter.java",
		// "contactCheckbox.setOnClickListener true");
		// checkBox.setChecked(true);
		// }
		// }
		// });

	}
}
