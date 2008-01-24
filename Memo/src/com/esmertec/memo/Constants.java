package com.esmertec.memo;

import com.esmertec.memo.provider.Memo;

public class Constants {

	public static final String[] ALL_COLUMNS_PROJECTION = new String[] {
			Memo.Memos._ID, Memo.Memos.ACTIVITY, Memo.Memos.CONTACT,
			Memo.Memos.LOCATION, Memo.Memos.DESCRIPTION, Memo.Memos.TIME, };
	public static final int ALL_COLUMN_ID = 0;
	public static final int ALL_COLUMN_ACTIVITY = 1;
	public static final int ALL_COLUMN_CONTACT = 2;
	public static final int ALL_COLUMN_LOCATION = 3;
	public static final int ALL_COLUMN_DESCRIPTION = 4;
	public static final int ALL_COLUMN_TIME = 5;
	
	public static final String PREF_LOCATION_LABEL = "LOCATION_LABEL";
	public static final String PREF_LOCATION_TEXT = "LOCATION_TEXT";

}
