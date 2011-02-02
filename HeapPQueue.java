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

public class HeapPQueue<Item extends Object> implements Queue<Item> {
	protected static class Entry implements Cloneable {
		int ndx, handle;
		long seq;
		Object val;
		static long nextSeq;

		public Entry(Entry newEntry) {
			handle = newEntry.handle;
			ndx = newEntry.ndx;
			val = newEntry.val;
			seq = (long)(nextSeq++);
		}

		public Entry(int handleInput, int ndxInput) {
			handle = handleInput;
			ndx = ndxInput;
		}
	}

	protected static final int STD_INCR = 10;
	protected int mIncrSize;
	protected Entry[] mData;
	protected Entry[] mHandles;
	protected int mEnd;
	protected Comparator<Entry> mCmp;

	public HeapPQueue(Comparator<Item> cmp) {
		this(cmp, STD_INCR);
	}

	public HeapPQueue(Comparator<Item> cmp, int incrSize) {
		int ndx;
		
		mCmp = new HeapComparator(cmp);
		mIncrSize = incrSize;
		mData = new Entry[mIncrSize];
		mHandles = new Entry[mIncrSize];

		for (ndx = 0; ndx < mHandles.length; ++ndx) {
			mData[ndx] = mHandles[ndx] = new Entry(ndx, ndx);
		}
	}

	protected static class HeapComparator implements Comparator {

		protected Comparator mCmp;

		public HeapComparator(Comparator cmp) {mCmp = cmp;}

		public int compare(Object entry1, Object entry2) {
			return compare((Entry)entry1, (Entry)entry2);
		}

		public int compare(Entry entry1, Entry entry2) {
			int compReturn;
			
			compReturn = mCmp.compare(entry1.val, entry2.val);
			if (compReturn == 0) {
				compReturn =(entry1.seq >= entry2.seq ?
				 (entry1.seq <= entry2.seq ? 0 : -1) : 1);
			}
			return compReturn;
		}
	}

	protected final void moveEntry(Entry val, int distance) {
		mData[distance] = val;
		val.ndx = distance;
	}

	public void add(Item addObj) {
		int endNdx, endHandle;
		
		if (mEnd == mData.length) {
			Entry[] entries = new Entry[mData.length + mIncrSize];
			System.arraycopy(mData, 0, entries, 0, mData.length);
			mData = entries;
			entries = new HeapPQueue.Entry[mHandles.length + mIncrSize];
			System.arraycopy(mHandles, 0, entries, 0, mHandles.length);
			mHandles = entries;

			for (endNdx = mEnd; endNdx < mHandles.length; ++endNdx) {
				mHandles[endNdx] = mData[endNdx] =
				 new HeapPQueue.Entry(endNdx, endNdx);
			}
		}
		endHandle = mData[mEnd].handle;
		mData[mEnd].seq = (long)(HeapPQueue.Entry.nextSeq++);
		mData[mEnd++].val = addObj;
		promote(mData[mEnd - 1], mEnd - 1);
	}

	public void remove() {
		if (mEnd <= 0) {
			throw new
			 IndexOutOfBoundsException("Attempt to access empty heap");
		} 
		else {
			remove(mData[0].handle);
		}
	}

	public void remove(int removeNdx) {
		Entry replaceEntry;
		int removeNdxVal;

		replaceEntry = mData[--mEnd];
		removeNdxVal = mHandles[removeNdx].ndx;
		moveEntry(mHandles[removeNdx], mEnd);
		moveEntry(replaceEntry, removeNdxVal);
		adjust(replaceEntry.handle);
	}

	public Item getFront() {
		if (mEnd <= 0) {
			throw new
			 IndexOutOfBoundsException("Attempt to access empty heap");
		} 
		else {
			return (Item) mData[0].val;
		}
	}

	public boolean isEmpty() {return mEnd == 0;}

	public void adjust(int ndx) {
		Entry tmp;
		int ndxVal;
		
		tmp = mHandles[ndx];
		ndxVal = tmp.ndx;
		if (ndxVal > 0 && mCmp.compare(tmp, mData[(ndxVal - 1) / 2]) > 0) {
			promote(tmp, ndxVal);
		} 
		else {
			demote(tmp, ndxVal);
		}
	}

	protected void promote(Entry val, int ndx) {
		int childNdx;
		
		for (childNdx = (ndx - 1) / 2; ndx > 0 && mCmp.compare(val,
		 mData[childNdx]) > 0; childNdx = (childNdx - 1) / 2) {
			moveEntry(mData[childNdx], ndx);
			ndx = childNdx;
		}
		moveEntry(val, ndx);
	}

	protected void demote(Entry val, int ndx) {
		int childNdx, childCtr;
		
		childNdx = 2 * ndx + 1;
		for (childCtr = childNdx + 1;
		 childNdx < mEnd; childCtr = childNdx + 1) {
			if (childCtr < mEnd && mCmp.compare(val, mData[childCtr]) < 0 
			 && mCmp.compare(mData[childNdx], mData[childCtr]) < 0) {
				
				moveEntry(mData[childCtr], ndx);
				ndx = childCtr;
			}
			else {
				if (mCmp.compare(val, mData[childNdx]) >= 0) {
					break;
				}
				moveEntry(mData[childNdx], ndx);
				ndx = childNdx;
			}
			childNdx = 2 * ndx + 1;
		}
		moveEntry(val, ndx);
	}   
   
	public Object clone() {
		try {
			HeapPQueue cloneHeap = (HeapPQueue)super.clone();
			int ndx;
			
			cloneHeap.mData = new HeapPQueue.Entry[mData.length];
			cloneHeap.mHandles = new HeapPQueue.Entry[mHandles.length];

			for (ndx = 0; ndx < mData.length; ++ndx) {
				cloneHeap.mData[ndx] = cloneHeap.mHandles[mData[ndx].handle]
				 = new Entry(mData[ndx]);
			}
			return cloneHeap;
		} 
		catch (CloneNotSupportedException ex) {
			return null;
		}
	}
}
