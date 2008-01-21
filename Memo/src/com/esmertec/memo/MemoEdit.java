package com.esmertec.memo;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.ContentURI;
import android.os.Bundle;
import android.util.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.esmertec.provider.Memo;

public class MemoEdit extends Activity {

	private int mState;

	private ContentURI mURI;

	private static final int STATE_INSERT = 0;

	private static final int STATE_EDIT = 1;
	
    private static final String[] PROJECTION = new String[] {
        Memo.Memos._ID, // 0
        Memo.Memos.ACTIVITY, // 1
        Memo.Memos.CONTACT, // 2
        Memo.Memos.LOCATION,
        Memo.Memos.TIME,
};

	private AutoCompleteTextView mEditActivity;
	private AutoCompleteTextView mEditContact;

	private EditText mEditDesc;
	
	private TextView mShowTime;
	
	private Calendar mCalendar;
	
	private Cursor mCursor;

	private DatePicker.OnDateSetListener mDateSetListener = new DatePicker.OnDateSetListener() {
		public void dateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mCalendar.set(Calendar.YEAR, year);
			mCalendar.set(Calendar.MONTH, monthOfYear);
			mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			Calendar cal = Calendar.getInstance();
			new TimePickerDialog(MemoEdit.this, mTimeSetListener, null, cal
					.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
					.show();

		}
	};

	private TimePicker.OnTimeSetListener mTimeSetListener = new TimePicker.OnTimeSetListener() {

		public void timeSet(TimePicker view, int hourOfday, int minute) {
			mCalendar.set(Calendar.HOUR_OF_DAY, hourOfday);
			mCalendar.set(Calendar.MINUTE, minute);
			DateFormat.format("MMM dd, yyyy h:mmaa", mCalendar);
		}

	};

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Intent intent = getIntent();

		if (intent.getAction().equals(Intent.EDIT_ACTION)) {
			mState = STATE_EDIT;
			mURI = intent.getData();
		} else if (intent.getAction().equals(Intent.INSERT_ACTION)) {
			mState = STATE_INSERT;
			mURI = getContentResolver().insert(intent.getData(), null);
			if (mURI == null) {
				Log.e("Memos", "Failed to insert new memo into "
						+ getIntent().getData());
				finish();
				return;
			}

			// The new entry was created, so assume all will end well and
			// set the result to be returned.
			setResult(RESULT_OK, mURI.toString());
		}else {
			 // Whoops, unknown action!  Bail.
            Log.e("Memos", "Unknown action, exiting");
            finish();
            return;
		}

		setContentView(R.layout.edit_memo_layout);
		mCursor = managedQuery(mURI, PROJECTION, null, null);

		mEditActivity = (AutoCompleteTextView) findViewById(R.id.edit_tag);
		mEditContact = (AutoCompleteTextView) findViewById(R.id.edit_contact);
		mEditDesc = (EditText) findViewById(R.id.edit_desc);

		mCalendar = Calendar.getInstance();

		Button changeDate = (Button) findViewById(R.id.set_time);
		changeDate.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Calendar cal = Calendar.getInstance();
				new DatePickerDialog(MemoEdit.this, mDateSetListener, cal
						.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
						.get(Calendar.DAY_OF_MONTH), Calendar.SUNDAY).show();
			}

		});

		Button changeTime = (Button) findViewById(R.id.open_time);
		changeTime.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				new TimePickerDialog(MemoEdit.this, mTimeSetListener, null, 0,
						0, false).show();

			}

		});

		Button changeLocation = (Button) findViewById(R.id.open_location);

		changeLocation.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				// ContentURI geoURI = ContentURI.create("geo:0,0?q=" +
				// "chengdu");
				// Intent mapViewIntent = new Intent(
				// android.content.Intent.VIEW_ACTION, geoURI);
				// startActivity(mapViewIntent);
				Intent mapViewIntent = new Intent(MemoEdit.this,
						BrowseMap.class);

				startSubActivity(mapViewIntent, 0);

			}

		});
	}

}
