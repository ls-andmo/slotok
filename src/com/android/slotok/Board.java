package com.android.slotok;

import android.util.Pair;

public class Board {
	public static final int Length = 4;
	public static final int Height = 4;
	public static final int Size = Length * Height;
	public static char[] letters = {'j', 'u', 't', 'n', 'e', 's',
			'e', 'y', 'o', 'r', 'y', 'w', 'p', 't', 'p', 'o'};
	private static final int[][] numbers = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12},
			{13, 14, 15, 16}};	
	//private static final int[][] numbers = {{1, 2}, {3, 4}};
	//private static final int[][] numbers = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
	public static final int[][] directions = {{0, -1}, {1, -1}, {1, 0}, 
		{1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};
	//public static final int[][] directions = {{0, -1}, {1, 0}, 
	//	{0, 1}, {-1, 0}};
	enum CORNER_POINT_POSITION {TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT};
	enum MIDDLE_POINT_POSITION {TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT};
	enum SIDE_POINT_POSITION {TOPSIDE_LEFT, TOPSIDE_RIGHT,
		RIGHTSIDE_TOP, RIGHTSIDE_BOTTOM, BOTTOMSIDE_RIGHT, 
		BOTTOMSIDE_LEFT, LEFTSIDE_BOTTOM, LEFTSIDE_TOP};
	public static final int[][] CORNER_TRANSITION_ARRAY = {
		{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, 
		{4, 3, 2, 1, 8, 7, 6, 5, 4, 12, 11, 10, 9, 16, 15, 14, 13},
		{16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
		{13, 14, 15, 16, 9, 10, 11, 12, 5, 6, 7, 8, 1, 2, 3, 4}};
	public static final int[][] MIDDLE_TRANSITION_ARRAY = {
		{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, 
		{4, 3, 2, 1, 8, 7, 6, 5, 4, 12, 11, 10, 9, 16, 15, 14, 13},
		{16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
		{13, 14, 15, 16, 9, 10, 11, 12, 5, 6, 7, 8, 1, 2, 3, 4}};
	public static final int[][] SIDE_TRANSITION_ARRAY = {
		{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, 
		{4, 3, 2, 1, 8, 7, 6, 5, 4, 12, 11, 10, 9, 16, 15, 14, 13},
		{4, 8, 12, 16, 3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13}, 
		{16, 12, 8, 4, 15, 11, 7, 3, 14, 10, 6, 2, 13, 9, 5, 1},
		{16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
		{13, 14, 15, 16, 9, 10, 11, 12, 5, 6, 7, 8, 1, 2, 3, 4},
		{13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3, 16, 12, 8, 4},
		{1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8, 12, 16}};

	public static int findPoint(int i, int j) {return numbers[i][j];}
	/*public static char findLetter(int i, int j) {return letters[i][j];}
	public static char findLetter(int position) {
		Pair<Integer, Integer> coords = getCoordinates(position);
		return letters[coords.first][coords.second];
	}*/
	public static Pair<Integer, Integer> getCoordinates(int number) {
		int y = (number - 1) / Height;
		int x = number - y * Height - 1;
		return new Pair<Integer, Integer>(x, y);
	}
	public static int getPosition(int x, int y) {
		return x + 1 + (y * Height);
	}
	
}
