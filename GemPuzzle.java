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

import java.util.*;

public class GemPuzzle implements Problem {
	public static final int MAX_DIM = 10;
	static GemStep [] mSteps = new GemStep[] {GemStep.north, GemStep.east,
	 GemStep.south, GemStep.west};
	GemState mInitialState;

	private static class GemState implements Problem.State {
		private byte[] mBoard;
		private int mRowInput;
		private int mColInput;
		private int mRow;
		private int mCol;

		private static class GemIterator implements Iterator {
			int mNext = 0;

			public Problem.Step next() {return GemPuzzle.mSteps[mNext++];}

			public boolean hasNext() {return mNext < GemPuzzle.mSteps.length;}

			public void remove() {throw new UnsupportedOperationException();}
		}

		public Iterator iterator() {return new GemIterator();}

		public Problem.State applyStep(Problem.Step step) {
			int arrSize = 0;
			GemStep gemStep = (GemStep)step;
			GemState gemState = new GemState();  
			
			gemState.mRow = mRow + gemStep.mRowChange;
			gemState.mCol = mCol + gemStep.mColChange;  
			if (gemState.mRow < 0 || gemState.mRow >= mRowInput ||
			 gemState.mCol < 0 || gemState.mCol >= mColInput) {
				gemState.mBoard = null;
			}

			else {
				gemState.mRowInput = mRowInput;
				gemState.mColInput = mColInput;
				arrSize = gemState.mRow * mColInput + gemState.mCol;
				gemState.mBoard = (byte[])mBoard.clone();
				gemState.mBoard[mRow * mColInput + mCol] =
				 gemState.mBoard[arrSize];
				gemState.mBoard[arrSize] = 0;
			}
			return gemState;
		}

		public boolean equals(Object obj) {
			return Arrays.equals(mBoard, ((GemState)obj).mBoard);
		}
	}

	public enum GemStep implements Problem.Step {
		north (-1, 0),
		east (0, 1),
		south (1, 0),
		west (0, -1);   
		public int mRowChange;
		public int mColChange;

		private GemStep(int row, int col) {mRowChange = row; mColChange = col;}

		public int getCost() {return 1;} 
	}

	public Problem.State getInitialState() {return mInitialState;}

	public int getCost(Problem.State state) {  
		int cost = 0, ndx = 0, dividend = 0, remainder = 0;

		GemState gemState = (GemState)state; 
		for (ndx = 0; ndx < gemState.mBoard.length; ndx++)
			if (gemState.mBoard[ndx] != 0 && gemState.mBoard[ndx] - 1 != ndx) {
				dividend = (gemState.mBoard[ndx] - 1) 
				 / gemState.mColInput - ndx / gemState.mColInput;
				remainder = (gemState.mBoard[ndx] - 1) 
				 % gemState.mColInput - ndx % gemState.mColInput;  
				cost += Math.abs(dividend) + Math.abs(remainder); 
			}
		return cost;
	}

	public boolean isValid(Problem.State state) {
		return ((GemState)state).mBoard != null;
	}

	public void read(Scanner scanner) throws Exception { 
		int ndx = 0;

		GemState gemstate = new GemState();
		gemstate.mRowInput = scanner.nextInt();
		gemstate.mColInput = scanner.nextInt(); 
		if (gemstate.mRowInput > 10 || gemstate.mRowInput < 1 ||
		 gemstate.mColInput > 10 || gemstate.mColInput < 1)
			throw new Exception("Dimensions must be between 1 and 10");

		gemstate.mBoard = new byte[gemstate.mRowInput * gemstate.mColInput];   
		for (ndx = 0; ndx < gemstate.mBoard.length; ndx++)    
			if ((gemstate.mBoard[ndx] = scanner.nextByte()) == 0) {
				gemstate.mRow = ndx / gemstate.mColInput;
				gemstate.mCol = ndx % gemstate.mColInput;
			}

		byte boardCheck[] = gemstate.mBoard.clone();
		Arrays.sort(boardCheck);   
		for (ndx = 0; ndx < boardCheck.length; ndx++) {
			if (boardCheck[ndx] < ndx)
				throw new Exception("Board repeats value " + boardCheck[ndx]);
			if (boardCheck[ndx] > ndx)
				throw new Exception("Board skips value " + ndx);
		}
		mInitialState = gemstate;
	}
}
