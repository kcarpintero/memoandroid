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
import android.widget.ListAdapter;
import android.widget.ListView;

import com.esmertec.memo.Constants;
import com.esmertec.memo.R;
import com.esmertec.memo.adapter.MemoListAdapter;
import com.esmertec.memo.provider.Memo;

public class MemoList extends ListActivity {
	
	private static final String TAG = "memolist";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		Log.d(TAG, "onCreate");
//		SortedMap<String, Charset> sm = Charset.availableCharsets();
//		for ( Charset sc : sm.values()){
//			Log.d(TAG, sc.toString());
//		}
		
		super.onCreate(icicle);

		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(Memo.Memos.CONTENT_URI);
		}
		cursor = managedQuery(getIntent().getData(), Constants.ALL_COLUMNS_PROJECTION, null, null);

//		ListAdapter adapter = new SimpleCursorAdapter(this,
//				R.layout.memo_item_layout, cursor, new String[] {
//						Memo.Memos.ACTIVITY, Memo.Memos.TIME }, new int[] {
//						R.id.activity_column, R.id.time_column });
		ListAdapter adapter = new MemoListAdapter(this,
				R.layout.memo_item_layout, cursor);
		setListAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.FIRST, ADD_MEMO_ID, R.string.menu_add_memo);
		

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (cursor.count() > 0) {
			ContentURI uri = getIntent().getData().addId(cursor.getLong(Constants.ALL_COLUMN_ID));
			Intent[] specifics = new Intent[] { new Intent(Intent.EDIT_ACTION, uri) };
			
			Intent intent = new Intent(null, uri);
			intent.addCategory(Intent.SELECTED_ALTERNATIVE_CATEGORY);
			
			menu.addIntentOptions(Menu.SELECTED_ALTERNATIVE, 0, null,
					specifics, intent, Menu.NO_SEPARATOR_AFTER, null);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		if (item.getId() == ADD_MEMO_ID) {
			Intent intent = new Intent(Intent.INSERT_ACTION, getIntent()
					.getData());
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {		
		super.onListItemClick(l, v, position, id);
		ContentURI uri = getIntent().getData().addId(cursor.getLong(0));
		Intent intent = new Intent(Intent.EDIT_ACTION, uri);
		startActivity(intent);
	}



	private final static int ADD_MEMO_ID = 1;

	private Cursor cursor;


	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onFreeze(Bundle outState) {
		Log.d(TAG, "onFreeze");
		super.onFreeze(outState);
	}

	
	

}