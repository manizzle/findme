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

public class DFSSolver implements Solver {
	private static class DFSStep {
		private Problem.Step mStep;
		private Problem.State mState;
		private Iterator mItr;

		private DFSStep(Problem.State state, Problem.Step step) {
			mStep = step;
			mState = state;
			mItr = state.iterator();
		}
		public boolean equals(Object obj) {
			DFSStep dfsStep = (DFSStep)obj;
			return mState.equals(dfsStep.mState);
		}
	}

	public Solver.Solution[] solveProblem(Problem problem, 
	 int maxCost, int numSols) {  
		int ndx = 0;  
		LinkedList linkedList = new LinkedList();
		ArrStack arrStack = new ArrStack();  

		arrStack.push(new DFSStep(problem.getInitialState(), null));    
		do {
			if (arrStack.isEmpty() || (linkedList.size() >= numSols))
				break;              
			DFSStep dfsStep = (DFSStep)arrStack.top();

			if ((dfsStep.mItr.hasNext()) &&
			 (arrStack.getCount() - 1 < maxCost)) { 
				Problem.Step step = (Problem.Step)dfsStep.mItr.next();
				Problem.State state = dfsStep.mState.applyStep(step);  

				if (problem.isValid(state)) {
					DFSStep nextDfsStep = new DFSStep(state, step);
					if (!arrStack.isOn(nextDfsStep)) {        
						arrStack.push(nextDfsStep);  

						if (problem.getCost(((DFSStep)arrStack.top()).mState)
						 == 0) {         
							ArrStack solvedStack = (ArrStack)arrStack.clone();
							arrStack.pop();
							Solver.Solution solution = new Solver.Solution();
							solution.mCost = solvedStack.getCount() - 1;
							solution.mSteps = new Problem.Step[solution.mCost];

							for (ndx = solution.mSteps.length - 1; ndx >= 0;
							 ndx--) {
								solution.mSteps[ndx] =
								 ((DFSStep)solvedStack.top()).mStep;
								solvedStack.pop();
							}
							linkedList.add(solution);
						}
					}
				}
			}      
			else {
				arrStack.pop();  
			}
		}
		while (true);  
		return (Solver.Solution[])linkedList.toArray(new Solver.Solution[0]);
	}
}