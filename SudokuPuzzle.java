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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class SudokuPuzzle implements Problem {

	private static int mRows   = 9;
	private static int mCols   = 9;
	private static int mBox    = 3;
	private static byte[] mPuzzleInput;
	static Step[] mSteps  = createSteps();
	
	private static class SudokuStep implements Step {
		protected byte mStep;

		public SudokuStep(byte step) {mStep = step;}
		
		public int getCost() {return 1;}
		public String toString() {return "" + mStep;}
	}

	public static class SudokuState implements State {
		private byte mBoard[];

		private class SudokuIterator implements Iterator {
			protected int mNext = 0;

			public Step next() {return mSteps[mNext++];}

			public boolean hasNext() {return (mNext < mSteps.length);}

			public void remove() {throw new UnsupportedOperationException();}
		}
		
		public SudokuState(byte inputBoard[]) {
			mBoard = new byte[mRows * mCols];
			System.arraycopy(inputBoard, 0, mBoard, 0, inputBoard.length);
		}
		
		public Iterator iterator() {return new SudokuIterator();}

		public Problem.State applyStep(Step step) {
			int ndx = 0;
			SudokuStep dokuStep = (SudokuStep) step;
			SudokuState dokuState = new SudokuState(mBoard);
			
			for (ndx = 0; ndx < mBoard.length; ndx++) {
				if (mBoard[ndx] == 0) {
					dokuState.mBoard[ndx] = dokuStep.mStep;
					break;
				}
			}
			return dokuState;
		}

		public String toString() {
			int ndx;
			String board = new String();
			for (ndx = 0; ndx < mBoard.length; ndx++) {
				board = (board + (mBoard[ndx] + "")); 
				if ((ndx + 1) % 9 == 0)
					board += "\n";	
				else
					board += " ";
			}
			return board;
		}

		public boolean equals(State st) {
			return Arrays.equals(mBoard, ((SudokuState) st).mBoard);
		}
	}

	public static Step[] createSteps() {
		SudokuStep[] allDoku = new SudokuStep[mRows];
		byte step;
		
		for (step = 1; step <= mRows; step++) {
			allDoku[step - 1] = new SudokuStep(step);
		}
		return allDoku;
	}
	
	public void read(Scanner in) throws Exception {
		mPuzzleInput = new byte[mRows * mCols];
		int puzzleNdx = 0;
		
		for (puzzleNdx = 0; puzzleNdx < mPuzzleInput.length; puzzleNdx++)
			mPuzzleInput[puzzleNdx] = (byte) in.nextInt();
		
		if (!isValid(new SudokuState(mPuzzleInput)))
			throw new Exception ("Invalid Input on board");
	}

	public State getInitialState() {
		SudokuState newDoku = new SudokuState(mPuzzleInput);
		return newDoku;
	}

	 public boolean isValid(State st) {
	      SudokuState dokuState = (SudokuState) st;
	      byte[] stateBoard = dokuState.mBoard;
	      byte[] sections = new byte[mCols + 1];
	      int row = 0, col = 0, ndx2 = 0, ndx3 = 0, ndx = 0;
	      

	      for (row = 0; row < mBox; row++) {
	         for (col = 0; col < mBox; col++) {
	            for (ndx2 = 0; ndx2 < mBox; ndx2++)
	               for (ndx3 = 0; ndx3 < mBox; ndx3++)
	                  sections[stateBoard[((row * mRows * mBox) + (col * mBox) + (ndx2 * mRows) + ndx3)]]++;
	                  
	            for (ndx = 1; ndx < mCols + 1; ndx++) {
	               if (sections[ndx] > 1) 
	                  return false;
	            }
	            sections = new byte[mCols + 1];
	         }
	      }
	      

	      for (row = 0; row < mRows; row++) {
	         for (col = 0; col < mCols; col++) {
	            sections[stateBoard[(row + (col * mCols))]]++;

	         }
	         for (ndx = 1; ndx < mCols + 1; ndx++) {
	            if (sections[ndx] > 1) 
	               return false;
	         }
	         sections = new byte[mCols + 1];
	      }
	      

	      for (row = 0; row < mRows; row++) {
	         for (col = 0; col < mCols; col++) {
	            sections[stateBoard[((row * mRows) + col)]]++;
	         }
	         for (ndx = 1; ndx < mCols + 1; ndx++) {
	            if (sections[ndx] > 1) 
	               return false;
	         }
	         sections = new byte[mCols + 1];
	      }
	      
	      return true;        
	   }

	public int getCost(State st) {
		SudokuState newDoku = (SudokuState) st;
		int ret = 0, ndx = 0;
		
		if (isValid(newDoku))
			for(ndx = 0; ndx < newDoku.mBoard.length; ndx++)
				if (newDoku.mBoard[ndx] == 0) {
					ret = 1;
					break;
				}

		if (ret == 0) {
			System.out.println(newDoku);
			System.exit(0);
		}
		return ret;
	}
}