package com.esmertec.memo;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Menu.Item;

import com.esmertec.provider.Memo;

public class MemoList extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.memo_list_layout);
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(Memo.Memos.CONTENT_URI);
        }
        Log.v("qinyu", getIntent().getData().toString());
        cursor = managedQuery(getIntent().getData(), PROJECTION, null, null);
        

//		cursor = new MemoProvider().query(Memo.Memos.CONTENT_URI, PROJECTION,
//				null, null, null, null, null);
//
//		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.id.memo_item, cursor, PROJECTION, new int[]{R.id.activity_column, R.id.time_column});
//		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.FIRST, ADD_MEMO_ID, R.string.menu_add_memo);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		if (item.getId() == ADD_MEMO_ID) {
			Intent intent = new Intent(Intent.INSERT_ACTION, getIntent().getData());
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	private final static int ADD_MEMO_ID = 1;

	private Cursor cursor;

	private static final String[] PROJECTION = {
		Memo.Memos.ACTIVITY,
		Memo.Memos.DATE,
//		Memo.Memos._ID,
		Memo.Memos.TIME
	};

}