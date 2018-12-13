package edu.smith.cs.csc212.p5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SudokuGame {

	public int board[][];
	public static int easy[][] = { { 0, 0, 8, 0, 9, 1, 7, 0, 4 }, { 7, 0, 0, 8, 2, 0, 0, 9, 3 },
			{ 9, 5, 2, 0, 4, 0, 1, 0, 0 }, { 0, 3, 9, 1, 0, 4, 6, 0, 0 }, { 6, 8, 1, 0, 0, 0, 3, 4, 0 },
			{ 0, 0, 0, 6, 3, 9, 0, 5, 1 }, { 8, 2, 0, 0, 7, 3, 9, 0, 0 }, { 0, 0, 0, 2, 6, 8, 0, 3, 7 },
			{ 4, 7, 3, 0, 0, 0, 0, 8, 6 } };

	public static int medium[][] = { { 3, 0, 6, 5, 0, 8, 4, 0, 0 }, { 5, 2, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 8, 7, 0, 0, 0, 0, 3, 1 }, { 0, 0, 3, 0, 1, 0, 0, 8, 0 }, { 9, 0, 0, 8, 6, 3, 0, 0, 5 },
			{ 0, 5, 0, 0, 9, 0, 6, 0, 0 }, { 1, 3, 0, 0, 0, 0, 2, 5, 0 }, { 0, 0, 0, 0, 0, 0, 0, 7, 4 },
			{ 0, 0, 5, 2, 0, 6, 3, 0, 0 } };
	public static int difficult[][] = { { 9, 0, 0, 0, 0, 8, 7, 0, 5 }, { 5, 0, 0, 0, 0, 0, 1, 0, 0 },
			{ 0, 3, 2, 0, 5, 6, 0, 0, 0 }, { 0, 0, 0, 0, 4, 7, 0, 0, 0 }, { 0, 0, 8, 0, 0, 0, 5, 0, 0 },
			{ 0, 0, 0, 3, 8, 0, 0, 0, 0 }, { 0, 0, 0, 2, 1, 0, 3, 9, 0 }, { 0, 0, 9, 0, 0, 0, 0, 0, 4 },
			{ 3, 0, 4, 5, 9, 1, 7, 0, 3 } };
	public static int n = 9;
	public static boolean isComplete = false;

	public SudokuGame(int sudoku[][]) {
		this.board = sudoku;

	}

	/**
	 * Does the input number satisfy the rules of Sudoku? To be considered as
	 * solved, a Sudoku grid must respect some constraints : Each row that composes
	 * the grid must contain all of the digits from 1 to 9 Each column that composes
	 * the grid must contain all of the digits from 1 to 9 Each nine 3x3 subgrids
	 * composing the grid must contain all of the digits from 1 to 9
	 *
	 * @return boolean true(valid) or false(invalid)
	 */
	public boolean isValid(int i, int j, int x) {

		// Is 'x' used in row.
		for (int jj = 0; jj < n; jj++) {
			if (board[i][jj] == x) {
				return false;
			}
		}

		// Is 'x' used in column.
		for (int ii = 0; ii < n; ii++) {
			if (board[ii][j] == x) {
				return false;
			}
		}

		// Is 'x' used in sudoku 3x3 box.
		int boxRow = i - i % 3;
		int boxColumn = j - j % 3;

		for (int ii = 0; ii < 3; ii++) {
			for (int jj = 0; jj < 3; jj++) {
				if (board[boxRow + ii][boxColumn + jj] == x) {
					return false;
				}
			}
		}

		// If all rules are satisfied
		return true;
	}

	/**
	 * To keep a record of which position can be modified by the user true =
	 * modifiable false = not modifiable
	 * 
	 * @return a 2D boolean array table
	 */
	public boolean isModifiable(int i, int j) {
		boolean[][] A;
		A = new boolean[n][n];

		// "0" positions can be changed
		// other numbers are static which belong to the original Sudoku puzzle
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				if (board[row][col] != 0) {
					A[row][col] = false;
				} else {
					A[row][col] = true;
				}
			}
		}
		if(A[i][j] == false) {
			return false;
		}
		return true;
	
	}

	/**
	 * Is the puzzle complete? true = complete false = not complete
	 * 
	 * @return boolean true or false
	 */
	public boolean isComplete() {

		// if the board contains 0, then there's still way to go!
		for (int ii = 0; ii < n; ii++) {
			for (int jj = 0; jj < n; jj++) {
				if (board[ii][jj] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This is our Sudoku game
	 * 
	 */
	public static void main(String args[]) throws IOException {
		long start = System.currentTimeMillis() / 1000;

		BufferedReader myReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please choose a difficulty level: Easy / Medium / Difficult");

		String Levels = myReader.readLine();
		List<String> level = WordSplitter.splitTextToWords(Levels);
		String lv = level.get(0);
		
		SudokuGame sudoku;

		if (lv.equals("easy")) {
			sudoku = new SudokuGame(easy);

		} else if (lv.equals("medium")) {
			sudoku = new SudokuGame(medium);
		} else {
			sudoku = new SudokuGame(difficult);
		}


		while (true) {

			// print out the puzzle
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					System.out.print(sudoku.board[i][j] + " ");
				}
				System.out.println();
			}

			// ask the user for input
			BufferedReader Reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Input your position and number :");

			// To read data using readLine and WordSplitter
			String myInput = Reader.readLine();
			List<String> input = WordSplitter.splitTextToWords(myInput);
			int i = Integer.parseInt(input.get(0));
			int j = Integer.parseInt(input.get(1));
			int number = Integer.parseInt(input.get(2));

			// If the position is modifiable
			if (sudoku.isModifiable(i, j)) {
				// If the input is valid
				if (sudoku.isValid(i, j, number)) {
					sudoku.board[i][j] = number;
				} else {
				//	sudoku.board[i][j] = number;
					System.out.println("Your input is invalid. \n");
				}
			} else {
				System.out.println(
						"This is the original number that cannot be changed. \n" + "Choose another position. \n");
			}

			// Insert a timer.
			long end = System.currentTimeMillis() / 1000;
			System.out.println("You've used " + (end - start) + " seconds so far.");

			// If the puzzle is not complete yet, continue
			if (!sudoku.isComplete()) {

				continue;
			}

			// If the puzzle is complete, exit the loop
			break;

		}
		// We're done here!
		System.out.println("Yay, you win!");
	}

}