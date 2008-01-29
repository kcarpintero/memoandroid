package com.esmertec.memo.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ContentURI;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.esmertec.memo.R;
import com.esmertec.memo.provider.Memo;
import com.esmertec.memo.provider.MemoProvider;

public class TimeSetter extends Activity {
	private Calendar mCalendar;

	private ContentURI mURI;

	private DatePicker.OnDateSetListener mDateSetListener = new DatePicker.OnDateSetListener() {
		public void dateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mCalendar.set(Calendar.YEAR, year);
			mCalendar.set(Calendar.MONTH, monthOfYear);
			mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//			new TimePickerDialog(TimeSetter.this, mTimeSetListener, null,
//					mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar
//							.get(Calendar.MINUTE), false).show();

		}
	};

	private TimePicker.OnTimeSetListener mTimeSetListener = new TimePicker.OnTimeSetListener() {

		public void timeSet(TimePicker view, int hourOfday, int minute) {
			mCalendar.set(Calendar.HOUR_OF_DAY, hourOfday);
			mCalendar.set(Calendar.MINUTE, minute);
			mCalendar.set(Calendar.SECOND, 0);
			Cursor cursor = managedQuery(mURI,
					new String[] { Memo.Memos.TIME }, null, null);
			cursor.updateLong(0, mCalendar.getTimeInMillis());
			cursor.commitUpdates();
			cursor.deactivate();
			finish();
			// cursor.first();
			// do {
			//
			// } while (cursor.next());
		}

	};

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Intent intent = getIntent();

		if (intent == null) {
			finish();
			return;
		}

		if (intent.getAction().equals(MemoProvider.ACTION_SET_TIME)) {
			mURI = intent.getData();
			if (mURI == null) {
				finish();
				return;
			}

		}
		setContentView(R.layout.time_setter);
		
		mCalendar = Calendar.getInstance();

		DatePicker dp = (DatePicker) findViewById(R.id.datepicker);
		dp.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH), Calendar.SUNDAY,
				mDateSetListener);

		TimePicker tp = (TimePicker) findViewById(R.id.timepicker);
		tp.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
		tp.setCurrentMinute(mCalendar
				.get(Calendar.MINUTE));
		tp.setOnTimeFilledIn(mTimeSetListener);

	}
}
