package com.esmertec.memo.provider;

import android.net.ContentURI;
import android.provider.BaseColumns;

public class Memo {
	public static final class Memos implements BaseColumns {
		
		public static final String DEFAULT_SORT_ORDER = "modified DESC";//"date * 1440 + time DESC";

		public static final ContentURI CONTENT_URI = ContentURI
				.create("content://com.esmertec.memo/memos");

		public static final String TITLE = "title";
		public static final String CONTACTS = "contacts";
		public static final String LOCATION = "location";
		public static final String TIME = "time";

		/**
		 * The timestamp for when the note was created
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CREATED_DATE = "created";

		/**
		 * The timestamp for when the note was last modified
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String MODIFIED_DATE = "modified";

	}
	
	
	public static final class Tags implements BaseColumns{
		public static final String DEFAULT_SORT_ORDER = "modified DESC";//"date * 1440 + time DESC";

		public static final ContentURI CONTENT_URI = ContentURI
				.create("content://com.esmertec.memo/tags");

		public static final String NAME = "name";

		/**
		 * The timestamp for when the note was created
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CREATED_DATE = "created";

		/**
		 * The timestamp for when the note was last modified
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String MODIFIED_DATE = "modified";
	}

}
