
package com.esmertec.memo.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.esmertec.memo.Constants;
import com.esmertec.memo.R;
import com.esmertec.memo.activity.MemoEditor.ContactListAdapter;

public class AutoCompleteInputDialog extends Activity {

    public static final String CONTACT_ADAPTER = "Contact Adapter";

    public static final String TAG_ADAPTER = "Tag Adapter";

    private AutoCompleteTextView mAutoCompleteTxt;
    private Button mButtonSave, mButtonCancel;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.autocomplete_input);

        mAutoCompleteTxt =
                (AutoCompleteTextView) findViewById(R.id.auto_complete_txt);
        Intent intent = getIntent();
        if (intent != null) {
            if (CONTACT_ADAPTER.equals(intent
                    .getStringExtra(Constants.INTENT_AUTOCOMPLETE_ADAPTER))) {
                ContentResolver content = getContentResolver();
                Cursor cursor =
                        content.query(Contacts.People.CONTENT_URI,
                                MemoEditor.PEOPLE_PROJECTION, null, null,
                                Contacts.People.DEFAULT_SORT_ORDER);
                ContactListAdapter adapter =
                        new ContactListAdapter(cursor, this);
                mAutoCompleteTxt.setAdapter(adapter);
            }
        }

        mButtonSave = (Button) findViewById(R.id.button_save_auto_complete_txt);
        mButtonSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setResult(RESULT_OK, mAutoCompleteTxt.getText().toString());
                finish();
            }
        });

        mButtonCancel =
                (Button) findViewById(R.id.button_cancel_auto_complete_txt);
        mButtonCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

}
