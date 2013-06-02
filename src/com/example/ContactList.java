/*
 * 
 * @author Darshan Mehta darsh_mehta@yahoo.com
 * 
 */

package com.example;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ContactList {
	private List<Contact> contacts = new ArrayList<Contact>();

	public List<Contact> getContacts() {
		return contacts;
	}

	public Contact getContact(int position) {
		return contacts.get(position);
	}
	
	public void addContact(Contact contact) {

		Log.d("ModelList.java", "Add Contact: "
//				+ contact.getContactNo().toString() + " "
				+ contact.getId().toString() + contact.getName().toString() + " " );
//				+ contact.getContactNoType().toString() + contact.isSelected());
		this.contacts.add(contact);
	}
}
