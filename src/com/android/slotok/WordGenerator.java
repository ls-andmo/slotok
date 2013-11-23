package com.android.slotok;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

public class WordGenerator {
	/*private enum Direction {
		North,
		East,
		South,
		West
	};
	static private int BOARD_SIZE = 16;*/
	private MainActivity mainActivity;
	private int numOfCombinations = 0;
	//private HashSet<Vector<Integer>> combinations = new HashSet<Vector<Integer>>();
	private Vector<Vector<Integer>> combinations = new Vector<Vector<Integer>>(); 
	
	WordGenerator(MainActivity activity) {
		mainActivity = activity;
	}
	
	public Vector<Vector<Integer>> getCombinations() {
		return combinations;		
	}

	public Integer generateCombinations() {
		try {
			FileOutputStream outputStream = mainActivity.openFileOutput("all.txt", Context.MODE_PRIVATE);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			for (int i = 1; i <= Board.Size; i++) {
				Vector<Integer> combination = new Vector<Integer>();
				//int[] combination = new int[16];
				numOfCombinations++;
				combination.add(i);	
				//combination[0] = i;
				findNextPosition(i, combination, bos);	
			}			
			bos.close();
			outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
			
		Log.i("slotok", "Total num of combinations = " + numOfCombinations);
		return combinations.size();
	}

	public Integer generateCombinationsForCornerPoint() {
		try {
			FileOutputStream outputStream = mainActivity.openFileOutput("corner.dat", Context.MODE_PRIVATE);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			
			combinations = new Vector<Vector<Integer>>();
	        int cornerValue = 1;
			Vector<Integer> combination = new Vector<Integer>();
			numOfCombinations++;
			combination.add(cornerValue);
			findNextPosition(cornerValue, combination, bos);
			
			bos.close();
			outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
		Log.i("slotok", "Generation for corner point completed");
		return numOfCombinations;
	}

	public Integer generateCombinationsForSidePoint() {
		try {
			FileOutputStream outputStream = mainActivity.openFileOutput("side.dat", Context.MODE_PRIVATE);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			
			combinations = new Vector<Vector<Integer>>();
	        int cornerValue = 2;
			Vector<Integer> combination = new Vector<Integer>();
			numOfCombinations++;
			combination.add(cornerValue);
			findNextPosition(cornerValue, combination, bos);
			
			bos.close();
			outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
		Log.i("slotok", "Generation for corner point completed");
		return numOfCombinations;
	}

	public Integer generateCombinationsForMiddlePoint() {
		try {
			FileOutputStream outputStream = mainActivity.openFileOutput("middle.dat", Context.MODE_PRIVATE);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);

			combinations = new Vector<Vector<Integer>>();
	        int middleValue = 6;
			Vector<Integer> combination = new Vector<Integer>();
			numOfCombinations++;
			combination.add(middleValue);
			findNextPosition(middleValue, combination, bos);
			Log.i("slotok", "Generation for corner point completed " + numOfCombinations);
			//mainActivity.writeCombinationsToFile("corner.txt", combinations);
			
			bos.close();
			//osw.close();
			outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return numOfCombinations;
	}
	/*private void findNextPosition(int currentPosition, 
			Vector<Integer> combination, BufferedOutputStream bos) {
		for (int i = 0; i < 8; i++) {
			int [] directionCoordinates = Board.directions[i];
			Pair<Integer, Integer> currPosCoordinates = 
				Board.getCoordinates(currentPosition);
			int newX = currPosCoordinates.first + directionCoordinates[0];
			int newY = currPosCoordinates.second + directionCoordinates[1];
			int newPosition = Board.getPosition(newX, newY);
			if (newX < 0 || newX > Board.Length -1 ||
				newY < 0 || newY > Board.Height - 1 ||
			    combination.contains(newPosition)) continue;
			Vector<Integer> newCombination = new Vector<Integer>(combination);
			numOfCombinations ++;
			newCombination.add(newPosition);
			if (newCombination.size() >= 3) {
				writeCombinationToFile(newCombination, bos);
				//mainActivity.checkIfWordCorrect(newCombination);
			}
			if (combination.size() >= Board.Size) continue;
			findNextPosition(newPosition, newCombination, bos);			
		}
	}	*/
	private void findNextPosition(int currentPosition, 
			Vector<Integer> combination, BufferedOutputStream bos) {
		for (int i = 0; i < 8; i++) {
			int [] directionCoordinates = Board.directions[i];
			Pair<Integer, Integer> currPosCoordinates = 
				Board.getCoordinates(currentPosition);
			int newX = currPosCoordinates.first + directionCoordinates[0];
			int newY = currPosCoordinates.second + directionCoordinates[1];
			int newPosition = Board.getPosition(newX, newY);
			if (newX < 0 || newX > Board.Length -1 ||
				newY < 0 || newY > Board.Height - 1 ||
			    combination.contains(newPosition)) continue /*false*/;
			Vector<Integer> newCombination = new Vector<Integer>(combination);
			numOfCombinations ++;
			newCombination.add(newPosition);
			if (newCombination.size() >= 3) {
				writeCombinationToFile(newCombination, bos);
				//mainActivity.checkIfWordCorrect(newCombination);
			}
			if (combination.size() >= Board.Size) continue;
			findNextPosition(newPosition, newCombination, bos);			
		}
	}
	
	void writeCombinationToFile(Vector<Integer> combinationToWrite, BufferedOutputStream bos) {
		try {
		    String stringComb = "";
		    for (Integer j : combinationToWrite) {
		    	stringComb += Integer.toHexString(j - 1);
		    }
		    stringComb += '|';
		    bos.write(stringComb.getBytes(Charset.forName("UTF-8")));
		    Log.i("slotok", "combinationToWrite = " + combinationToWrite);
		    Log.i("slotok", "stringComb = " + stringComb);
		    bos.flush();
		} catch(Exception e) {
			  e.printStackTrace();
	    }
	}
	
	/*void writeCombinationToFile(Vector<Integer> combinationToWrite, BufferedOutputStream bos) {
		try {
		    long combinationPart1 = 0;
		    long combinationPart2 = 0;
		    int k = 1;
		    int combintionLength = combinationToWrite.size();
		    Integer[] array = new Integer[combintionLength];
		    int numOfBytesToWrite;
		    boolean isLengthIsEven = combintionLength % 2 == 0;
		    numOfBytesToWrite = combintionLength / 2 + 1;
		    byte[] byteArray = new byte[numOfBytesToWrite];
		    combinationToWrite.toArray(array);	
		    int i = 0;
		    // 1st byte is size is even we save size twice in one byte
		    if (isLengthIsEven) {
		    	byteArray[0] = (byte)((combintionLength - 1) << 4 + (combintionLength - 1));		    	
		    } else {
		    	byteArray[0] = (byte)((combintionLength - 1) << 4 + (combintionLength - 1));		    	
		    }
		    while (i < numOfBytesToWrite) {
		    	if (isLengthIsEven)
		    	byteArray[i] = (byte)((array[i * 2] - 1) << 4 + (array[i * 2 +1] - 1));
		    	i++;
		    }
		    if (isLengthIsEven) {
		    	byteArray[i] = (byte)((array[i * 2] - 1) << 4 + (array[i * 2] - 1));
		    }
		    
		    bos.write(oneByte)
		    osw.writeLong(combinationPart2);
		    Log.i("slotok", "Long 1 = " + combinationPart1);
		    Log.i("slotok", "Long 2 = " + combinationPart2);
		    osw.flush();
		} catch(Exception e) {
			  e.printStackTrace();
	    }
	}*/
	
	void writeCombinationToFile(Vector<Integer> combinationToWrite, DataOutputStream osw) {
		try {
		    long combinationPart1 = 0;
		    long combinationPart2 = 0;
		    int k = 1;
		    for (Integer j : combinationToWrite) {
		    	if (k <= 8) {
		    		combinationPart1 += j * Math.pow(100, (k-1));
		    	} else {
		    		combinationPart2 += j * Math.pow(100, ((k - 8) -1));				    		
		    	}
		    	k++;
		    }
		    osw.writeLong(combinationPart1);
		    osw.writeLong(combinationPart2);
		    Log.i("slotok", "Long 1 = " + combinationPart1);
		    Log.i("slotok", "Long 2 = " + combinationPart2);
		    osw.flush();
		} catch(Exception e) {
			  e.printStackTrace();
	    }
	}
}
