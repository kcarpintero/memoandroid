package com.esmertec.memo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.esmertec.memo.R;

public class SaveLocation extends Activity {

	private EditText mEditLabel;
	private Button mButtonSave;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.save_location);

		mEditLabel = (EditText) findViewById(R.id.edit_location_label);

		mButtonSave = (Button) findViewById(R.id.button_save_location);
		mButtonSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				setResult(RESULT_OK, mEditLabel.getText().toString());
				finish();
			}
		});
	}

}
