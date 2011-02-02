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

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class AStarSolver implements Solver {

	private static class AStarStep {
		public Problem.Step mStep;
		public Problem.State mState;

		public AStarStep(Problem.State state, Problem.Step step) {
			mStep = step;
			mState = state;
		}

		public boolean equals(Object inputAStep) {
			AStarStep aStep;
			aStep = (AStarStep)inputAStep;
			return mState.equals(aStep.mState);
		}
	}

	private static class AStarStack extends LinkStack {
		private int mHeadCost;
		private int mCost;
		private Problem mPrb;

		public AStarStack(Problem prob) {mPrb = prob;}

		public int getCost() {return mCost;}
		
		public void push(Object obj) {
			push((AStarStep)obj);
		}
		
		public void push(AStarStep currStep) {
			int currCost;
			
			currCost = mPrb.getCost(currStep.mState);
			mCost += currCost - mHeadCost;
			if (currStep.mStep != null) {
				mCost += currStep.mStep.getCost();
			}
			mHeadCost = currCost;
			super.push((Object)currStep);
		}
	}

	public class AStarComp implements Comparator {
		
		public int compare(AStarStack stack1, AStarStack stack2) {
			int cost1, cost2;
			cost1 = stack1.getCost();
			cost2 = stack2.getCost();
			return (cost1 > cost2) ? -1 : (cost1 == cost2 ? 0 : 1);
		}
		
		public int compare(Object stack1, Object stack2) {
			return compare((AStarStack)stack1, (AStarStack)stack2);
		}   
	}

	public Solver.Solution[] solveProblem(Problem problem, int maxCost,
	 int numSols) {
		LinkedList linkedList = new LinkedList();
		AStarStack astarStack = new AStarStack(problem);
		AStarStack initStack = null;
		HeapPQueue heap = new HeapPQueue(new AStarComp());  
		int ndx;

		astarStack.push(new AStarStep(problem.getInitialState(), null));
		heap.add(astarStack);
		while (!heap.isEmpty() && linkedList.size() < numSols) {		
			initStack = (AStarStack)heap.getFront();
			heap.remove();
			Problem.State initState = ((AStarStep)initStack.top()).mState; 
			
			if (problem.getCost(initState) == 0) {
				Solver.Solution solution =
					new Solver.Solution();
				solution.mCost = initStack.getCost();

				solution.mSteps =
					new Problem.Step[solution.mCost];
				ndx = solution.mSteps.length - 1;
				for (; ndx >= 0; ndx--) {
					solution.mSteps[ndx] =
						((AStarStep)initStack.top()).mStep;
					initStack.pop();
				}
				linkedList.add(solution);
			}
			
			else {
				Iterator itr = initState.iterator();
				while (itr.hasNext()) {
					Problem.Step step = (Problem.Step)itr.next();
					Problem.State state = initState.applyStep(step);

					if (problem.isValid(state)) {
						AStarStep astarStep= new AStarStep(state, step);
						if (!initStack.isOn(astarStep)) {
							
							AStarStack stack = (AStarStack)initStack.clone();
							stack.push(astarStep);
							if (stack.getCost()  <= maxCost) {
								heap.add(stack);
							}
						}
					}
				}
			}
		} 
		return (Solver.Solution[])linkedList.toArray(new Solver.Solution[0]);
	}
}
