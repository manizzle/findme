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

public class BFSSolver implements Solver {

	private static class BFSStep {
		private Problem.Step mStep;
		private Problem.State mState;

		public boolean equals(Object obj) {
			BFSStep bfsStep = (BFSStep)obj;
			return mState.equals(bfsStep.mState);
		}
		private BFSStep(Problem.State state, Problem.Step step) {
			mStep = step;
			mState = state;
		}
	}

	public Solver.Solution[] solveProblem(Problem problem, int maxCost,
	 int numSols) {
		LinkedList linkedList = new LinkedList();
		LinkStack linkStack = new LinkStack();
		LinkQueue linkQueue = new LinkQueue();  
		int ndx;

		linkStack.push(new BFSStep(problem.getInitialState(), null));
		linkQueue.add(linkStack);
		label1:
			do {
				if (linkQueue.isEmpty())
					break;

				Stack initStack = (Stack)linkQueue.getFront();
				Problem.State initState = ((BFSStep)initStack.top()).mState; 
				Iterator itr = initState.iterator();
				do {
					if (!itr.hasNext())
						break;
					Problem.Step step = (Problem.Step)itr.next();
					Problem.State state = initState.applyStep(step);
					
					if (problem.isValid(state)) {
						BFSStep bfsstep = new BFSStep(state, step);
						if (!initStack.isOn(bfsstep)) {
							Stack stack = (Stack)initStack.clone();
							stack.push(bfsstep);

							if (problem.getCost(state) == 0) {
								Solver.Solution solution =
								 new Solver.Solution();
								solution.mCost = stack.getCount() - 1;
								
								solution.mSteps =
								 new Problem.Step[solution.mCost];
								ndx = solution.mSteps.length - 1;
								for (; ndx >= 0; ndx--) {
									solution.mSteps[ndx] =
									 ((BFSStep)stack.top()).mStep;
									stack.pop();
								}
								
								linkedList.add(solution);
								if (linkedList.size() >= numSols)
									break label1;
							}
							else {
								if (stack.getCount() - 1 < maxCost)
									linkQueue.add(stack);
							}
						}
					}
				} while (true);
				linkQueue.remove();
			} while (true);
		return (Solver.Solution[])linkedList.toArray(new Solver.Solution[0]);
	}
}