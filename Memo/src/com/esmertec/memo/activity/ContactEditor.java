package com.esmertec.memo.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ContentURI;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.esmertec.memo.R;
import com.esmertec.memo.provider.Memo;
import com.esmertec.memo.provider.MemoProvider;
import com.google.wireless.gdata.data.StringUtils;

public class ContactEditor extends ListActivity {

	private static final String TAG = ContactEditor.class.getName();

	private ContentURI mURI;
	
	private boolean mHasContacts;

	private AutoCompleteTextView mTextContact;

	@Override
	protected void onCreate(Bundle icicle) {

		super.onCreate(icicle);

		Intent intent = getIntent();

		if (intent == null) {
			finish();
			return;
		}

		if (!intent.getAction().equals(MemoProvider.ACTION_EDIT_CONTACTS)) {
			finish();
			return;
		}

		mURI = intent.getData();

		if (mURI == null) {
			finish();
			return;
		}

		setContentView(R.layout.contacts_editor);

		refreshList();

		Button button = (Button) findViewById(R.id.button_save_auto_complete_contact);

		mTextContact = (AutoCompleteTextView) findViewById(R.id.auto_complete_contact);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				addContact(mTextContact.getText().toString());
				refreshList();
			}
		});

	}

	private void refreshList() {
		Cursor cursor = managedQuery(mURI, new String[] { Memo.Memos.CONTACTS,
				Memo.Memos._ID }, null, null);
		cursor.first();
		String allContacts = cursor.getString(0);

		String[] contactList = new String[] {};

		if (!StringUtils.isEmpty(allContacts)) {
			contactList = allContacts.split(", ");
		}
		mHasContacts = contactList.length > 0;
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, contactList));
	}

	private void deleteContact(String str) {
		Log.v(TAG, "Delete " + str);
		Cursor cursor = managedQuery(mURI, new String[] { Memo.Memos.CONTACTS,
				Memo.Memos._ID }, null, null);
		cursor.first();
		String allContacts = cursor.getString(0);

		Log.v(TAG, "old allContacts = " + allContacts);
		String[] contactList = new String[] {};

		if (!StringUtils.isEmpty(allContacts)) {
			contactList = allContacts.split(", ");
		}

		allContacts = "";
		for (String contact : contactList) {
			if (contact.equals(str)) {
				continue;
			}
			if (!StringUtils.isEmpty(allContacts)) {
				allContacts += ", ";
			}
			allContacts += contact;
		}

		Log.v(TAG, "new allContacts = " + allContacts);
		cursor.updateString(0, allContacts);
		managedCommitUpdates(cursor);
		cursor.deactivate();
	}

	private void addContact(String str) {
		Log.v(TAG, "Add " + str);
		Cursor cursor = managedQuery(mURI, new String[] { Memo.Memos.CONTACTS,
				Memo.Memos._ID }, null, null);
		cursor.first();
		String allContacts = cursor.getString(0);

		String[] contactList = new String[] {};

		if (!StringUtils.isEmpty(allContacts)) {
			contactList = allContacts.split(", ");
		}

		allContacts = str;
		for (String contact : contactList) {
			if (contact.equals(str)) {
				continue;
			}
			if (!StringUtils.isEmpty(allContacts)) {
				allContacts += ", ";
			}
			allContacts += contact;
		}

		cursor.updateString(0, allContacts);
		managedCommitUpdates(cursor);
		cursor.deactivate();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		deleteContact(((TextView) v).getText().toString());
		refreshList();
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		// TODO Call the selected contact 
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mHasContacts){
			//TODO Add the menu options to call selected contact
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	

}
