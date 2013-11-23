package com.android.slotok;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import com.google.common.io.ByteStreams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private boolean combinationReady = false;
	private HashSet<String> words = new HashSet<String>();
	private HashSet<String> wordsWithLetters = new HashSet<String>();
	private Vector<String> wordsWithLetters1 = new Vector<String>();
	private String letters;
	private WordGenerator wordGen;
	private LetterInputLayout mainView = null;
	private ArrayAdapter<String> adapter;
    private ArrayList<String> goodWords = new ArrayList<String>();
    
    enum BordPointType {CORNER, MIDDLE, SIDE};

	private class GenerateCombinationTask extends AsyncTask<BordPointType, Void, Integer> {
		protected Integer doInBackground(BordPointType... pointType) {
			//Try to load from a file
	        switch(pointType[0]) {
	        case CORNER:
				return wordGen.generateCombinationsForCornerPoint();
	        case MIDDLE:
				return wordGen.generateCombinationsForMiddlePoint();
	        case SIDE:
				return wordGen.generateCombinationsForSidePoint();
	        }
	        return 0;
		}
		protected void onPostExecute(Integer result) {
			Log.i("slotok", "Number of generated combinations = " +result);
			combinationReady = true;
		}
	}

	private class GenerateWordsTask extends AsyncTask<Void, Void, Integer> {
		protected Integer doInBackground(Void... values) {
			/*while (combinationReady == false) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			int wordCount = 0;
			//HashSet<Vector<Integer>> combinations = wordGen.getCombinations();

			readAndCheckCombinationsFromFile(BordPointType.CORNER);
			readAndCheckCombinationsFromFile(BordPointType.MIDDLE);
			readAndCheckCombinationsFromFile(BordPointType.SIDE);
			/*Vector<Vector<Integer>> combinations = readCombinationsFromFile("corner.txt");
			for (Vector<Integer> combination : combinations) {
				if (checkIfWordCorrect(combination)) {
					wordCount++;
				}
			}
			int points = 0;
			for (String goodWord : goodWords) {
				points += Math.pow(goodWord.length() - 2, 2);			
			}
			Log.i("slotok", "Total point for this game = " + points);*/
			return wordCount;
		}
		protected void onPostExecute(Integer result) {
			Log.i("slotok", "Number of good words = " + result);
		}
	}
	
	public boolean checkIfWordCorrect(Vector<Integer> combination) {
		String word = new String();
		for (Integer i : combination) {
			word += letters.charAt(i-1);				
		}
		//Log.i("slotok", "Words from combination = " + word);
		if (/*wordsWithLetters1*/wordsWithLetters.contains(word)) {
			goodWords.add(word);
			//adapter.notifyDataSetChanged();
			Log.i("slotok", word);
			return true;
		}
		return false;
	}
	
	public void writeCombinationsToFile(String filename, Vector<Vector<Integer>> combinations) {	
		//File file = new File(getFilesDir(), filename);	
	
		int combinationCount = 0;
		FileOutputStream outputStream;
	
		try {
		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
		  OutputStreamWriter osw = new OutputStreamWriter(outputStream);
		  for (Vector<Integer> combination : combinations) {			  
			  String combinationString = new String();
			  for (Integer i : combination) {
				  combinationString += i;
				  combinationString +='|';
			  }			  
			  combinationString += '\n';
			  osw.write(combinationString);
			  combinationCount++;
			  osw.flush();
		  } 
		  osw.close();
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
        Log.i("slotok", "Number of written combination to a file = " + combinationCount);
	}
	
	public void readAndCheckCombinationsFromFile(BordPointType pointType) {
        Log.i("slotok", "readAndCheckCombinationsFromFile start");
        int combinationCount = 0;
        String filename = "";
        int [][] transitionArray = null;
        switch(pointType) {
        case CORNER:
        	filename = "corner.dat";
        	transitionArray = Board.CORNER_TRANSITION_ARRAY;
        	break;
        case MIDDLE:
        	transitionArray = Board.MIDDLE_TRANSITION_ARRAY;
        	filename = "middle.dat";
        	break;
        case SIDE:
        	transitionArray = Board.SIDE_TRANSITION_ARRAY;
        	filename = "side.dat";
        	break;
        }
        try {
            InputStream inputStream = openFileInput(filename);
            byte[] fileBytes = ByteStreams.toByteArray(inputStream);
            Log.i("slotok", "Byte array size = " + fileBytes.length);
            char[] word = new char[16];
            int[] combination = new int[16];
            char[][] wordsFromcombination = new char[transitionArray.length][16];
            int position = 0;
            String wordToCheck = "";
            for (int i = 0; i < fileBytes.length; i++) {
               //if ((char)fileBytes[i] == '|') {
               if (fileBytes[i] == 124) {
            	   for (int j = 0; j < position; j++) {
	            	   for (int k = 0; k < transitionArray.length; k++) {
	             	   //wordToCheck = new String(word, 0, position);
	            		   wordsFromcombination[k][j] = 
	            			   Board.letters[transitionArray[k][combination[j]] - 1];            	 	  
	            	   }
            	   }
            	   for (int j = 0; j < wordsFromcombination.length; j++) {
	             	   wordToCheck = new String(wordsFromcombination[j], 0, position);
	          		   if (/*wordsWithLetters1*/wordsWithLetters.contains(wordToCheck)) {
	        		 	   if (!goodWords.contains(wordToCheck)) {
	        		 		   final String goodWord = wordToCheck;
	          			       this.runOnUiThread(new Runnable() {
	          			    	   @Override
	          			    	   public void run() {
	    	          			       goodWords.add(goodWord);
	    		        		 	   adapter.notifyDataSetChanged();	          			    		   
	          			    	   }
	          			       });
		        			   Log.i("slotok", wordToCheck);
	        		 	   }
	        		   }
            	   }
            	   position = 0;
            	   continue;
               }
               int letterPosition = Character.digit(fileBytes[i], 16);
               /*if (position >= 16 || letterPosition < 0 || letterPosition >= 16) {
                   Log.i("slotok", "byte count = " + i);
                   Log.i("slotok", "position = " + position);
                   Log.i("slotok", "Character.digit(fileBytes[i], 16) = " + letterPosition);
               }*/
               //word[position] = Board.letters[letterPosition];
               combination[position] = letterPosition;
               position++;
            }
        }
        catch (FileNotFoundException e) {
            Log.e("slotok", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("slotok", "Can not read file: " + e.toString());
        }
        Log.i("slotok", "readAndCheckCombinationsFromFile end");
        Log.i("slotok", "Number of read combination from a file = " + combinationCount);
	}
	
	public Vector<Vector<Integer>> readCombinationsFromFile(String filename) {
		Vector<Vector<Integer>> combinations = new Vector<Vector<Integer>>();
		//File file = new File(getFilesDir(), filename);	
	    int combinationCount = 0;
        try {
            InputStream inputStream = openFileInput(filename);
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                	
                	Vector<Integer> combination = new Vector<Integer>();
                	String[] result = receiveString.split("\\|");
                	for (String i : result) {
                		combination.add(Integer.parseInt(i));
                	}
                	combinationCount++;
                	//Log.i("slotok", "comb " + combinationCount + " " + combination);
                	//combinations.add(combination);
                	if (checkIfWordCorrect(combination)) {
                    	Log.i("slotok", "comb " + combinationCount + " " + combination);
                	}
                	combinationCount++;
                }                 
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("slotok", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("slotok", "Can not read file: " + e.toString());
        }
        Log.i("slotok", "Number of read combination from a file = " + combinationCount);
        return combinations;
	}
	
	public void doGeneration(String inputLetters) {
		letters = inputLetters;
		goodWords.clear();
		//goodWords = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
        //		goodWords);
        //mainView.setListViewAdapter(adapter);
		this.runOnUiThread(new Runnable() {
    	   @Override
	       public void run() {
    		   adapter.notifyDataSetChanged();	          			    		   
	       }
	    });
		Log.i("slotok", "inputLetters = " + inputLetters);
		Board.letters = inputLetters.toCharArray();
		//Filter out words
		for (String word : words) {
			boolean goodWord = true;
			char [] charArray = word.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if (letters.indexOf(charArray[i]) == -1) {
					goodWord = false;
					break;
				}
			}
			if (goodWord) {
			    wordsWithLetters.add(word);
			}
		}
		Log.i("slotok", "wordsWithLetters = " + wordsWithLetters.size());
		//new GenerateCombinationTask().execute();
		new GenerateWordsTask().execute();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		Log.i("slotok", "character test 0 = " + Character.digit('0', 16));
		Log.i("slotok", "character test e = " + Character.digit('e', 16));
		Log.i("slotok", "character test f = " + Character.digit('f', 16));
    	wordsWithLetters1.add("stereotypowy");
    	wordGen = new WordGenerator(this);
        super.onCreate(savedInstanceState);
        mainView = new LetterInputLayout(this);
        //setContentView(R.layout.letter_input);
        setContentView(mainView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
        		goodWords);
        mainView.setListViewAdapter(adapter);
        readWordsFromAssets();
        File file = new File(getFilesDir(), "corner.dat");
        Log.i("slotok", "File with table = " + file);
        Log.i("slotok", "Position file with table = " + getFilesDir());
        //file.delete();
        if (!file.exists()) {
        	new GenerateCombinationTask().execute(BordPointType.CORNER);
        }/* else {
        	copyFileFromProjectFilesToSDCard("corner.txt");
        }*/
        
        file = new File(getFilesDir(), "middle.dat");
        if (!file.exists()) {
        	new GenerateCombinationTask().execute(BordPointType.MIDDLE);
        }
        file = new File(getFilesDir(), "side.dat");
        if (!file.exists()) {
        	new GenerateCombinationTask().execute(BordPointType.SIDE);
        }
    }
    
    private void readWordsFromAssets() {
	    InputStream stream;
		try {
			stream = getAssets().open("words.txt");
		    BufferedReader in=
		        new BufferedReader(new InputStreamReader(stream));
		    String word;
		
		    while ((word = in.readLine()) != null) {
		    	words.add(word);
		    }

			Log.i("slotok", "Number of read words = " + words.size());
		    in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    // Function to copy file from Assets to the SDCard
    public void copyFileFromProjectFilesToSDCard(String filename){
        try {

            InputStream is = openFileInput(filename);
            //InputStreamReader isr = new InputStreamReader(is);
            //BufferedReader bufferedReader = new BufferedReader(isr);
            DataInputStream dis = new DataInputStream(is);
            FileOutputStream fos;
            File path = new File(Environment.getExternalStorageDirectory()+"/Slotok/");
            if (!path.exists()) {
            	path.mkdirs();
            }
            fos = new FileOutputStream(path.getAbsolutePath() + filename);
            DataOutputStream dos = new DataOutputStream(fos);
            byte[] b = new byte[8];
            int i;
            //String receiveString = "";
            long fileData = 0;
            while (dis.available() > 0) {
            	fileData = dis.readLong();
            	dos.writeLong(fileData);
            	//receiveString += "\n";
                //fos.write(receiveString.getBytes(), 0, receiveString.getBytes().length);
            }
            fos.flush();
            dis.close();
            dos.close();
            fos.close();
            is.close();
        } 
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }    

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /*
	public void readAndCheckCombinationsFromFile(String filename) {
        Log.i("slotok", "readAndCheckCombinationsFromFile start");
		Vector<Vector<Integer>> combinations = new Vector<Vector<Integer>>();
        int combinationCount = 0;
        try {
            InputStream inputStream = openFileInput(filename);
            byte[] fileBytes = ByteStreams.toByteArray(inputStream);
             
            if (inputStream != null ) {
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                String receiveString = "";
                int SIZE = 1024;
                 
                byte[] barray = new byte[SIZE];
                long checkSum = 0L;
                int nRead;
                while ((nRead=bis.read( barray, 0, SIZE )) != -1) {
                    receiveString = new String(barray, 0, nRead, Charset.forName("UTF-8"));
                    String[] wordsToCheck = receiveString.split("\\|");
                    for (String word : wordsToCheck) {
                    	char[] array = word.toCharArray();
                    	Vector<Integer> combination = new Vector<Integer>();
	                	for (char i : array) {
	                		combination.add(Character.digit(i, 16) + 1);
	                	}	                	
	                	if (checkIfWordCorrect(combination)) {
		                    Log.i("slotok", "word = " + word);
		                    Log.i("slotok", "combination = " + combination);
		                    Log.i("slotok", "receiveString = " + receiveString);	                    	
	                    }
                    }
                    for (int i = 0; i < nRead; i++) {
                        checkSum += barray[i];
                        //Log.i("slotok", "Read data = " + receiveString);                        
                    }
                }
                bis.close();
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("slotok", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("slotok", "Can not read file: " + e.toString());
        }
        Log.i("slotok", "readAndCheckCombinationsFromFile end");
        Log.i("slotok", "Number of read combination from a file = " + combinationCount);
	}*/
    
}
