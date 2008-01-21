package com.esmertec.memo;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentProviderDatabaseHelper;
import android.content.ContentURIParser;
import android.content.ContentValues;
import android.content.QueryBuilder;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ContentURI;
import android.text.TextUtils;
import android.util.Log;

import com.esmertec.provider.Memo;

public class MemoProvider extends ContentProvider {

	private SQLiteDatabase mDB;

	private static final String DATABASE_NAME = "memo.db";
	private static final int DATABASE_VERSION = 2;

	private static final ContentURIParser URI_MATCHER;

	private static final int MEMOS = 1;
	private static final int MEMO_ID = 2;

	private static final HashMap<String, String> MEMO_LIST_PROJECTION_MAP;

	private static class DatabaseHelper extends ContentProviderDatabaseHelper {

		@Override
		public void onCreate(SQLiteDatabase db) {
			try{
			db.execSQL("CREATE TABLE memos (_id INTEGER PRIMARY KEY,"
					+ "activity TEXT," + "description TEXT," + "contact TEXT,"
					+ "location TEXT," + "date INTEGER," + "time INTEGER,"
					+ "created INTEGER," + "modified INTEGER" + ");");
			}catch (Exception e) {
				Log.v("qinyu", "Create db " + e.getMessage());
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS memos");
			onCreate(db);
		}

	}

	@Override
	public int delete(ContentURI uri, String where, String[] whereArgs) {
		int code = URI_MATCHER.match(uri);
		int count = 0;
		switch (code) {
		case MEMOS:
			count = mDB.delete("memos", where, whereArgs);
			break;
		case MEMO_ID:
			String id = uri.getPathSegment(1);
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
	public String getType(ContentURI uri) {
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
	public ContentURI insert(ContentURI uri, ContentValues initialValues) {
		if (URI_MATCHER.match(uri) != MEMOS) {
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		if (!values.containsKey(Memo.Memos.ACTIVITY)) {
			values.put(Memo.Memos.ACTIVITY, "Something");
		}

		if (!values.containsKey(Memo.Memos.CONTACT)) {
			values.put(Memo.Memos.CONTACT, "");
		}

		if (!values.containsKey(Memo.Memos.LOCATION)) {
			values.put(Memo.Memos.LOCATION, "");
		}

		if (!values.containsKey(Memo.Memos.DESCRIPTION)) {
			values.put(Memo.Memos.DESCRIPTION, "");
		}

		if (!values.containsKey(Memo.Memos.DATE)) {
			values.put(Memo.Memos.DATE, -1);
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
			ContentURI newUri = Memo.Memos.CONTENT_URI.addId(rowID);
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
	public Cursor query(ContentURI uri, String[] projection, String selection,
			String[] selectionArgs, String groupBy, String having,
			String sortOrder) {
		QueryBuilder qb = new QueryBuilder();

        switch (URI_MATCHER.match(uri)) {
        case MEMOS:
            qb.setTables("memos");
            qb.setProjectionMap(MEMO_LIST_PROJECTION_MAP);
            break;

        case MEMO_ID:
            qb.setTables("memos");
            qb.appendWhere("_id=" + uri.getPathSegment(1));
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

        Cursor c = qb.query(mDB, projection, selection, selectionArgs, groupBy,
                having, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(ContentURI uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int code = URI_MATCHER.match(uri);
		int count = 0;
		switch (code) {
		case MEMOS:
			count = mDB.update("memos", values, selection, selectionArgs);
			break;
		case MEMO_ID:
			String id = uri.getPathSegment(1);
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
		URI_MATCHER = new ContentURIParser(ContentURIParser.NO_MATCH);
		URI_MATCHER.addURI("com.esmertec.provider.Memo", "memos", MEMOS);
		URI_MATCHER.addURI("com.esmertec.provider.Memo", "memos/#", MEMO_ID);

		MEMO_LIST_PROJECTION_MAP = new HashMap<String, String>();
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.ACTIVITY, "activity");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.DESCRIPTION, "description");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.CONTACT, "contact");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.LOCATION, "location");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.DATE, "date");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.TIME, "time");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.CREATED_DATE, "created");
		MEMO_LIST_PROJECTION_MAP.put(Memo.Memos.MODIFIED_DATE, "modified");

	}

}