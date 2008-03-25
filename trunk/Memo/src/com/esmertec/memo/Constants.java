
package com.esmertec.memo;

import com.esmertec.memo.provider.Memo;

public class Constants {

    public static final String[] ALL_COLUMNS_PROJECTION =
            new String[] { Memo.Memos._ID, Memo.Memos.TITLE,
                    Memo.Memos.CONTACTS, Memo.Memos.LOCATION, Memo.Memos.TIME, };
    public static final int ALL_COLUMN_ID = 0;
    public static final int ALL_COLUMN_TITLE = 1;
    public static final int ALL_COLUMN_CONTACT = 2;
    public static final int ALL_COLUMN_LOCATION = 3;
    public static final int ALL_COLUMN_TIME = 4;

    public static final String PREF_LOCATION_LABEL = "LOCATION_LABEL";
    public static final String PREF_LOCATION_TEXT = "LOCATION_TEXT";

    public static final String INTENT_AUTOCOMPLETE_ADAPTER =
            "AUTOCOMPLETE_ADAPTER";

}
