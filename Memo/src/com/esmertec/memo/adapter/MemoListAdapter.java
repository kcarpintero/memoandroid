package com.esmertec.memo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.DateFormat;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.esmertec.memo.Constants;
import com.esmertec.memo.R;

public class MemoListAdapter extends ResourceCursorAdapter {

	public MemoListAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		((TextView)view.findViewById(R.id.activity_column)).setText(cursor.getString(Constants.ALL_COLUMN_TITLE));
		((TextView)view.findViewById(R.id.time_column)).setText(DateFormat.format("MMM dd, yyyy h:mmaa", cursor.getLong(Constants.ALL_COLUMN_TIME)));
	}

}
