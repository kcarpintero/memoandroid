package com.esmertec.provider;

import android.net.ContentURI;
import android.provider.BaseColumns;

public class Memo {
	public static final class Memos implements BaseColumns {
		
		public static final String DEFAULT_SORT_ORDER = "modified DESC";//"date * 1440 + time DESC";

		public static final ContentURI CONTENT_URI = ContentURI
				.create("content://com.esmertec.provider.Memo/memos");

		public static final String ACTIVITY = "activity";
		public static final String DESCRIPTION = "description";
		public static final String CONTACT = "contact";
		public static final String LOCATION = "location";

		public static final String DATE = "date";
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

}
