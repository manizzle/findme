/*
 * Copyright (c) 2009 Murtaza Munaim
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.util.Scanner;

public class SolvePuzzle {
	public static void main (String args[]) {
		int maxCost = -1, numSols = 0, ndx = 0, counter = 0;
		String probStr = "<None>", solveStr = "<None>";
		Scanner scanner = new Scanner(System.in);
		Solver solver = null;
		Problem problem = null;

		do {
			if (solver != null && problem != null)  
				break;
			System.out.print("Enter problem type, solution type," +
			 " max cost and max # of solutions: "); 
			
			try {
				problem = (Problem)Class.forName(probStr = scanner.next())
				 .newInstance();
				solver = (Solver)Class.forName(solveStr = scanner.next())
				 .newInstance();
				maxCost = scanner.nextInt();
				numSols = scanner.nextInt();
			}

			catch (Exception exception) {
				if (problem == null)
					System.out.println(probStr +
					 " isn't a valid problem class.");
				if (solver == null)
					System.out.println(solveStr +
					 " isn't a valid solver class.");

				if (maxCost == -1)
					System.out.println("Please enter a " +
					 "nonnegative integer for max cost.");
				if (numSols == 0)
					System.out.println("Please enter a positive " +
					 "integer for number of solutions.");
			}
		}
		while (true);

		try {
			System.out.println("Enter puzzle problem: ");
			problem.read(scanner);  
			Solver.Solution solutions[] = solver.solveProblem(problem,
			 maxCost, numSols);
			if (solutions.length == 0) {
				System.out.println("No solution exists.");
			} 
			else 
				if (probStr.equals("edu.calpoly.csc108.SudokuPuzzle"));

			else {
				System.out.println("Answers are: ");
				for (ndx = 0; ndx < solutions.length; ndx++) {
					Solver.Solution solution = solutions[ndx];
					System.out.println("Answer " + ndx + " with cost "
					 + solution.mCost);

					Problem.Step steps[] = solution.mSteps;
					for (counter = 0; counter < steps.length; counter++) {
						Problem.Step step = steps[counter];
						System.out.println("   " + step);
					}
				}
			}
		}
		catch (Exception ex) {
			System.out.println("Read error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}