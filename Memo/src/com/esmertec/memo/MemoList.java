package com.esmertec.memo;

import android.app.Activity;
import android.os.Bundle;

public class MemoList extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.edit_layout);
    }
}