package com.esmertec.memo.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MemoProvider extends ContentProvider {

	private static final String TAG = MemoProvider.class.getName();

	public static final String ACTION_SET_TIME = "com.esmertec.memos.action.SET_TIME";
	public static final String ACTION_SET_LOCATION = "com.esmertec.memos.action.SET_LOCATION";
	public static final String ACTION_EDIT_CONTACTS = "com.esmertec.memos.action.EDIT_CONTACTS";
	public static final String ACTION_EDIT_TITLE = "com.esmertec.memos.action.EDIT_TITLE";

	private SQLiteDatabase mDB;

	private static final String DATABASE_NAME = "memo.db";
	private static final int DATABASE_VERSION = 3;

	private static final UriMatcher URI_MATCHER;

	private static final int MEMOS = 1;
	private static final int MEMO_ID = 2;

	private static final HashMap<String, String> MEMO_LIST_PROJECTION_MAP;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE memos (_id INTEGER PRIMARY KEY,"
					+ "title TEXT," + "contacts TEXT," + "location TEXT,"
					+ "time INTEGER," + "created INTEGER," + "modified INTEGER"
					+ ");");

			db.execSQL("CREATE TABLE tags (_id INTEGER PRIMARY KEY,"
					+ "name TEXT," + "created INTEGER," + "modified INTEGER"
					+ ");");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS memos");
			db.execSQL("DROP TABLE IF EXISTS tags");
			onCreate(db);
		}

	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int code = URI_MATCHER.match(uri);
		int count = 0;
		switch (code) {
		case MEMOS:
			count = mDB.delete("memos", where, whereArgs);
			break;
		case MEMO_ID:
			String id = uri.getPathSegments().get(1);
			count = mDB.delete("memos",
					"_id="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		int code = URI_MATCHER.match(uri);
		switch (code) {
		case MEMOS:
			return "vnd.esmertec.cursor.dir/memo";
		case MEMO_ID:
			return "vnd.esmertec.cursor.item/memo";
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (URI_MATCHER.match(uri) != MEMOS) {
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		if (!values.containsKey(Memo.Memos.TITLE)) {
			values.put(Memo.Memos.TITLE, "Something");
		}

		if (!values.containsKey(Memo.Memos.CONTACTS)) {
			values.put(Memo.Memos.CONTACTS, "");
		}

		if (!values.containsKey(Memo.Memos.LOCATION)) {
			values.put(Memo.Memos.LOCATION, "");
		}

		if (!values.containsKey(Memo.Memos.TIME)) {
			values.put(Memo.Memos.TIME, -1);
		}

		Long now = Long.valueOf(System.currentTimeMillis());

		// Make sure that the fields are all set
		if (!values.containsKey(Memo.Memos.CREATED_DATE)) {
			values.put(Memo.Memos.CREATED_DATE, now);
		}

		if (!values.containsKey(Memo.Memos.MODIFIED_DATE)) {
			values.put(Memo.Memos.MODIFIED_DATE, now);
		}

		long rowID = mDB.insert("memos", "activity", values);
		if (rowID > 0) {
			Uri newUri = ContentUris.withAppendedId(Memo.Memos.CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		DatabaseHelper dbHelper = new DatabaseHelper();
		mDB = dbHelper.openDatabase(getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);
		return (mDB == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, 
			String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (URI_MATCHER.match(uri)) {
		case MEMOS:
			qb.setTables("memos");
			qb.setProjectionMap(MEMO_LIST_PROJECTION_MAP);
			break;

		case MEMO_ID:
			qb.setTables("memos");
			qb.appendWhere("_id=" + uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}

		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Memo.Memos.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int code = URI_MATCHER.match(uri);
		int count = 0;
		switch (code) {
		case MEMOS:
			count = mDB.update("memos", values, selection, selectionArgs);
			break;
		case MEMO_ID:
			String id = uri.getPathSegments().get(1);
			count = mDB.update("memos", values, "_id="
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;

	}

	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI("com.esmertec.memo", "memos", MEMOS);
		URI_MATCHER.addURI("com.esmertec.memo", "memos/#", MEMO_ID);

		MEMO_LIST_PROJECTION_MAP = new HashMap<String, String>();
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos._ID, "_id");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.TITLE, "title");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.CONTACTS, "contacts");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.LOCATION, "location");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.TIME, "time");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.CREATED_DATE, "created");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.MODIFIED_DATE, "modified");

	}

}