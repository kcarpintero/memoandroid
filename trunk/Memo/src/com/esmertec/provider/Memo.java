package com.esmertec.provider;

import android.net.ContentURI;
import android.provider.BaseColumns;

public class Memo {
	public static final class Memos implements BaseColumns {

		public static final ContentURI CONTENT_URI = ContentURI
				.create("content://com.esmertec.memo/Memos");

	}

}
