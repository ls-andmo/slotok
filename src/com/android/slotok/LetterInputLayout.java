package com.android.slotok;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class LetterInputLayout extends LinearLayout{

	EditText editText;
	Button crackButton;
	ListView listView;
	public LetterInputLayout(MainActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.letter_input, this, true);
		
		editText = (EditText)findViewById(R.id.edit_text);
		crackButton = (Button)findViewById(R.id.crack_button);
		listView = (ListView)findViewById(R.id.gen_words);
		
		hookupButton(activity);
	}
	
	private void hookupButton(final MainActivity activity) {
		crackButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				//editText.setText("Generation words started...");
				Editable editLetters = editText.getText();
				String letters = editLetters.toString();
				letters.toLowerCase();
				letters.trim();
				if (letters.length() != 16) {
					//do something to warn
					return;
				}
				InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE); 

				inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
				                                     InputMethodManager.HIDE_NOT_ALWAYS);
				
				activity.doGeneration(letters);
				//find word from table
			}
		});
	}
	
	public void setListViewAdapter(ListAdapter adapter) {
		listView.setAdapter(adapter);		
	}

}
