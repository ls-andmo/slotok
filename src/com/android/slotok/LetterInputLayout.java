package com.android.slotok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LetterInputLayout extends LinearLayout{

	EditText editText;
	Button crackButton;
	public LetterInputLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.letter_input, this, true);
		
		editText = (EditText)findViewById(R.id.edit_text);
		crackButton = (Button)findViewById(R.id.crack_button);
		
		hookupButton();
	}
	
	private void hookupButton() {
		crackButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				editText.setText("");
			}
		});
	}

}
