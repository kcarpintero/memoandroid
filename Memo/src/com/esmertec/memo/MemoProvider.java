package com.esmertec.memo;

import android.content.ContentValues;
import android.content.DatabaseContentProvider;
import android.database.Cursor;
import android.net.ContentURI;

public class MemoProvider extends DatabaseContentProvider {

	public MemoProvider(String dbName, int dbVersion) {
		super(dbName, dbVersion);
		// TODO Auto-generated constructor stub
	}

	public void bootstrapDatabase() {

	}

	public int deleteInternal(ContentURI url, String selection,
			String[] selectionArgs) {
		return 0;
	}

	public ContentURI insertInternal(ContentURI url, ContentValues values) {
		return null;
	}

	public Cursor queryInternal(ContentURI url, String[] projection,
			String selection, String[] selectionArgs, String groupBy,
			String having, String sortOrder) {
		return null;
	}

	public int updateInternal(ContentURI url, ContentValues values,
			String selection, String[] selectionArgs) {
		return 0;
	}

	public void upgradeDatabase(int oldVersion, int newVersion) {

	}

	public String getType(ContentURI url) {
		return null;
	}
}