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


public class MemoEditor extends Activity {

	private int mState;

	private ContentURI mURI;

	private static final int STATE_INSERT = 0;

	private static final int STATE_EDIT = 1;

	private AutoCompleteTextView mEditActivity;
	private AutoCompleteTextView mEditContact;

	private EditText mEditDesc;

	private TextView mTextTime, mTextLocation;

	private Calendar mCalendar;

	private Cursor mCursor;

	private DatePicker.OnDateSetListener mDateSetListener = new DatePicker.OnDateSetListener() {
		public void dateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mCalendar.set(Calendar.YEAR, year);
			mCalendar.set(Calendar.MONTH, monthOfYear);
			mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			Calendar cal = Calendar.getInstance();
			new TimePickerDialog(MemoEditor.this, mTimeSetListener, null, cal
					.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
					.show();

		}
	};

	private TimePicker.OnTimeSetListener mTimeSetListener = new TimePicker.OnTimeSetListener() {

		public void timeSet(TimePicker view, int hourOfday, int minute) {
			mCalendar.set(Calendar.HOUR_OF_DAY, hourOfday);
			mCalendar.set(Calendar.MINUTE, minute);
			mTextTime.setText(DateFormat.format("MMM dd, yyyy h:mmaa",
					mCalendar));
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
		} else {
			// Whoops, unknown action! Bail.
			Log.e("Memos", "Unknown action, exiting");
			finish();
			return;
		}

		setContentView(R.layout.edit_memo_layout);
		mCursor = managedQuery(mURI, Constants.ALL_COLUMNS_PROJECTION, null, null);
		mCursor.first();
		Log.v("qinyu", mURI.toString());
		Log.v("qinyu", mCursor.toString());
		Log.v("qinyu", mCursor.getString(Constants.ALL_COLUMN_ACTIVITY));

		mCalendar = Calendar.getInstance();

		mEditActivity = (AutoCompleteTextView) findViewById(R.id.edit_tag);
		mEditContact = (AutoCompleteTextView) findViewById(R.id.edit_contact);
		mEditDesc = (EditText) findViewById(R.id.edit_desc);
		mTextLocation = (TextView) findViewById(R.id.text_location);
		mTextTime = (TextView) findViewById(R.id.text_time);

		pullData();

		Button changeDate = (Button) findViewById(R.id.set_time);
		changeDate.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Calendar cal = Calendar.getInstance();
				new DatePickerDialog(MemoEditor.this, mDateSetListener, cal
						.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
						.get(Calendar.DAY_OF_MONTH), Calendar.SUNDAY).show();
			}

		});

		Button changeLocation = (Button) findViewById(R.id.set_location);

		changeLocation.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {


				Intent mapViewIntent = new Intent(MemoEditor.this,
						BrowseMap.class);
				startSubActivity(mapViewIntent, 0);

			}

		});
		
		Button saveMemo = (Button) findViewById(R.id.save_memo);

		saveMemo.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				pushData();
				mCursor.deactivate();
				mCursor = null;
				finish();
			}

		});
	}

	private void pullData() {
		mEditActivity.setText(mCursor, Constants.ALL_COLUMN_ACTIVITY);
		mEditContact.setText(mCursor, Constants.ALL_COLUMN_CONTACT);
		mEditDesc.setText(mCursor, Constants.ALL_COLUMN_DESCRIPTION);
		mTextLocation.setText(mCursor, Constants.ALL_COLUMN_LOCATION);
		long time = mCursor.getLong(Constants.ALL_COLUMN_TIME);
		if (time < 0) {
			time = System.currentTimeMillis();
		}
		mCalendar.setTimeInMillis(time);
		mTextTime.setText(DateFormat.format("MMM dd, yyyy h:mmaa", mCalendar));
	}
	
	private void pushData(){
		mCursor.updateString(Constants.ALL_COLUMN_ACTIVITY, mEditActivity.getText().toString());
		mCursor.updateString(Constants.ALL_COLUMN_CONTACT, mEditContact.getText().toString());
		mCursor.updateString(Constants.ALL_COLUMN_DESCRIPTION, mTextLocation.getText().toString());
		mCursor.updateString(Constants.ALL_COLUMN_DESCRIPTION, mEditDesc.getText().toString());
		mCursor.updateLong(Constants.ALL_COLUMN_TIME, mCalendar.getTimeInMillis());
		mCursor.commitUpdates();
		mCursor.requery();
	}
	

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		if (mState == STATE_INSERT){
			mCursor.deleteRow();
			mCursor.deactivate();
			mCursor = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}
	
	@Override
	protected void onFreeze(Bundle outState) {
		Log.d(TAG, "onFreeze");
		super.onFreeze(outState);
	}
	
	private static final String TAG = "memoeditor";
	
	
}
