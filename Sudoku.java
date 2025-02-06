import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sudoku {
	private static int[][][] sudokus;
	private static int currentSudoku;
	private static BufferedReader reader;
	// Guesses will be structured:
	// 0-8 for row 1, 9-17 for row 2, etc.
	// for example: row 5 column 3 = 38 (4 * 9 + 3 (-1 cuz index))
	private static DynamicArray guesses;
	private static boolean removed;
	private static int sudokusAmount;
	private static String filename;

	public static void main(String[] args) {
		currentSudoku = 0;
		if (args.length != 1) {
			System.out.println("You must give a valid text file as input.");
			return;
		}
		filename = args[0];

		if (args.length == 0) {
			System.out.println("No sudoku provided.");
			return;
		}

		ReadSudoku();
		currentSudoku = 0;
		long startTime = System.nanoTime();
		
		for (int j = 0; j < sudokusAmount; j++) {
			SolveSudoku();
			// PrintBoard();
			currentSudoku++;
		}
		long endTime = System.nanoTime();

		SaveResults();
		System.out.println("Time: " + ((float)(endTime - startTime) / (float)1000000) + " milliseconds");
	}

	private static void SaveResults() {

		String output = "";
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter("Results.txt", false));

			for (int[][] sudoku : sudokus) {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						output += sudoku[i][j];
					}
				} output += "\n";
			}
			writer.write(output);
            writer.close();

        } catch (IOException ioe) {
            System.out.println("Couldn't write to file");
        }
	}

	private static void SolveSudoku() {

		guesses = new DynamicArray();
		guesses.append(NextGuess());
		removed = false;
		int latest = guesses.top();
		
		while (latest != -1) {

			int x = latest % 9;
			int y = latest / 9;
			int val = sudokus[currentSudoku][y][x];
			int next = GetNextValid(x, y, val);

			if (next == -1) { // No more guesses possible for the square
				sudokus[currentSudoku][y][x] = 0;
				guesses.pop();
				latest = guesses.top();
				continue;
			}
			
			sudokus[currentSudoku][y][x] = next;
			guesses.append(NextGuess());
			latest = guesses.top();
			
		}
	}

	private static int NextGuess() {
		int latest_guess = guesses.top();
		int latestX = latest_guess % 9;
		int latestY = latest_guess / 9;

		do {
			if (latestY == 8 && latestX == 8)
				return -1;
			if (latestX++ == 8) {
				latestX = 0;
				latestY++;
			}
		} while (sudokus[currentSudoku][latestY][latestX] != 0);

		// Now we found an empty slot to guess in
		return latestY * 9 + latestX;
	}

	private static void ReadSudoku() {
		
		try {
			sudokusAmount = (int)Files.lines(Paths.get(filename)).count();
			sudokus = new int[sudokusAmount][9][9];

			reader = new BufferedReader(new FileReader(filename));
			int row = 0;
			String line;
			while ((line = reader.readLine()) != null) {

				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						char c = line.charAt(i*9+j);
						if (c == '.') {
							sudokus[row][i][j] = 0;
							continue;
						}
						sudokus[row][i][j] = (int)(c - '0');
					}
				} row++;
			}

		} catch (IOException e) {
			System.out.println(e);
		}

	}

	private static int GetNextValid(int startX, int startY, int current) {
		// Get the first allowed number that's greater than the one already in the cell
		int used = 0;
	
		// Check row
		for (int x = 0; x < 9; x++) {
			int val = sudokus[currentSudoku][startY][x];
			used |= (1<<val);
		}
	
		// Check column
		for (int y = 0; y < 9; y++) {
			int val = sudokus[currentSudoku][y][startX];
			used |= (1<<val);
		}
	
		// Check box
		int minX = startX - startX % 3;
		int minY = startY - startY % 3;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				int val = sudokus[currentSudoku][minY + y][minX + x];
				used |= (1<<val);
			}
		}
	
		// Start at the next possible number and find the first one that is legal
		for (int num = current + 1; num <= 9; num++) {
			if ((used & (1<<num)) == 0) {
				return num;
			}
		}
	
		// No valid number found
		return -1;
	}

	private static void PrintBoard() {
		try {
			int row = 0;
			for (int[] i : sudokus[currentSudoku]) {
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

			} System.out.println("\n");
		} catch (Exception e) {
			return;
		}
	} 
}
