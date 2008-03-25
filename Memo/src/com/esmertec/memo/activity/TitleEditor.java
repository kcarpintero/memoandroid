
package com.esmertec.memo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.esmertec.memo.R;
import com.esmertec.memo.provider.Memo;
import com.esmertec.memo.provider.MemoProvider;
import com.google.wireless.gdata.data.StringUtils;

public class TitleEditor extends Activity {

    private Uri mURI;

    private EditText mEdit;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        if (!intent.getAction().equals(MemoProvider.ACTION_EDIT_TITLE)) {
            finish();
            return;
        }

        mURI = intent.getData();

        if (mURI == null) {
            finish();
            return;
        }

        Cursor cursor =
                managedQuery(mURI, new String[] { Memo.Memos.TITLE,
                        Memo.Memos._ID }, null, null);

        setContentView(R.layout.title_editor);

        mEdit = (EditText) findViewById(R.id.edit_title);

        cursor.first();
        String title = cursor.getString(0);
        if (StringUtils.isEmpty(title)) {
            title = "Something";
        }

        mEdit.setText(title);

        Button button = (Button) findViewById(R.id.button_save_title);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Cursor cursor =
                        managedQuery(mURI, new String[] { Memo.Memos.TITLE,
                                Memo.Memos._ID }, null, null);
                cursor.first();

                cursor.updateString(0, mEdit.getText().toString());

                managedCommitUpdates(cursor);

                cursor.deactivate();

                finish();
            }
        });

    }

}
