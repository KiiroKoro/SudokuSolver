import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sudoku {
	private static int[][] sudoku;
	private static BufferedReader reader;
	// Guesses will be structured:
	// 0-8 for row 1, 9-17 for row 2, etc.
	// for example: row 5 column 3 = 38 (4 * 9 + 3 (-1 cuz index))
	private static DynamicArray guesses;
	private static boolean removed;

	public static void main(String[] args) {
		sudoku = new int[9][9];

		if (args.length == 0) {
			System.out.println("No sudoku provided.");
			return;
		}
		
		ReadSudoku(args);
		long startTime = System.currentTimeMillis();

		SolveSudoku();

		long endTime = System.currentTimeMillis();
		PrintBoard();
		System.out.println("Time: " + (endTime - startTime) + " milliseconds");
	}

	private static void SolveSudoku() {

		guesses = new DynamicArray();
		guesses.append(NextGuess());
		removed = false;
		int latest = guesses.top();
		while (latest != -1) {
			int x = latest % 9;
			int y = (int)((latest - x) / 9);

			if (sudoku[y][x] < 9) {
				sudoku[y][x]++;
				removed = false;

				if (CheckValid(x,y))
					guesses.append(NextGuess());

			} else {

				if (CheckValid(x,y) && !removed) {
					guesses.append(NextGuess());
					latest = guesses.top();
					continue;
				}
				
				sudoku[y][x] = 0;
				removed = true;
				guesses.pop();
			}

			latest = guesses.top();
		}
	}

	private static int NextGuess() {
		int latest_guess = guesses.top();
		int latestX = latest_guess % 9;
		int latestY = (int)((latest_guess - latestX) / 9);
		

		do {
			if (latestY == 8 && latestX == 8)
				return -1;
			if (latestX++ == 8) {
				latestX = 0;
				latestY++;
			}
		} while (sudoku[latestY][latestX] != 0);

		// Now we found an empty slot to guess in
		return latestY * 9 + latestX;
	}

	private static void ReadSudoku(String[] args) {
		
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			int row = 0;
			String line = reader.readLine();
			while (line != null) {
				if (row == 10)
					break;

				String[] split = line.split(" ");
				line = reader.readLine();

				if (split.length != 9) {
					System.out.println("Sudoku not valid.");
					return;
				}

				for (int i = 0; i < 9; i++) {
	
					if (split[i].length() != 1) {
						System.out.println("Sudoku is not valid at character: " + split[i]);
						return;
					}

					char num = split[i].charAt(0);
					if (num <= 47 || num >= 58) {
						System.out.println(num + " is not a valid number.");
						return;
					}
				
					sudoku[row][i] = (int)(num - '0');
				} row++;

			}
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	private static boolean CheckValid(int startX, int startY) {

		int[] nums = new int[9];
		// Check row
		for (int x = 0; x < 9; x++) {
				
			int val = sudoku[startY][x];
			if (val == 0)
				continue;
			if (nums[val-1]++ != 0) {
				return false;
			}

		} nums = new int[9];
		// Check column
		for (int y = 0; y < 9; y++) {
			
			int val = sudoku[y][startX];
			if (val == 0)
				continue;

			if (nums[val-1]++ != 0) {
				return false;
			}

		} nums = new int[9];

		// Check box
		int minX = startX - startX % 3;
		int minY = startY - startY % 3;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				
				int val = sudoku[minY + y][minX + x];
				if (val == 0)
					continue;
				if (nums[val-1]++ != 0) {
					return false;
				}
			}
		}

		return true;
	}

	private static void PrintBoard() {
		int row = 0;
		for (int[] i : sudoku) {
			String temp = "";
			int col = 0;
			for (int j : i) {
				if (++col % 3 == 0 && col != 9)
					temp += j + " | ";
				else
					temp += j + " ";
			}
			
			if (++row % 3 == 0 && row != 9)
				System.out.println(temp + "\n----------------------");
			else
				System.out.println(temp + "\n");


		}
	}
}
