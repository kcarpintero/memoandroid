package com.esmertec.memo;

import java.util.*;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ContentURI;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class MemoEdit extends Activity {

	private AutoCompleteTextView mEditTag;
	private AutoCompleteTextView mEditContact;

	private EditText mEditDesc;

	private DatePicker.OnDateSetListener mDateSetListener = new DatePicker.OnDateSetListener() {
		public void dateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getDefault());
			Log.v("qinyu", "create calendar");
			cal.set(year, monthOfYear, dayOfMonth);
			Log.v("qinyu", "set time 1");

		}
	};

	private TimePicker.OnTimeSetListener mTimeSetListener = new TimePicker.OnTimeSetListener() {

		public void timeSet(TimePicker view, int hourOfday, int minute) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.edit_memo_layout);

		mEditTag = (AutoCompleteTextView) findViewById(R.id.edit_tag);
		mEditContact = (AutoCompleteTextView) findViewById(R.id.edit_contact);
		mEditDesc = (EditText) findViewById(R.id.edit_desc);

		Button changeDate = (Button) findViewById(R.id.open_date);
		changeDate.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				new DatePickerDialog(MemoEdit.this, mDateSetListener, 2008, 1,
						1, Calendar.SUNDAY).show();
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
				

//				ContentURI geoURI = ContentURI.create("geo:0,0?q=" + "chengdu");
//				Intent mapViewIntent = new Intent(
//						android.content.Intent.VIEW_ACTION, geoURI);
//				startActivity(mapViewIntent);
				Intent mapViewIntent = new Intent(MemoEdit.this, BrowseMap.class);

				startSubActivity(mapViewIntent, 0);

			}

		});
	}

}
