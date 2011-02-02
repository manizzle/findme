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

public class ArrQueue<Item> implements Queue<Item> {

	protected static final int INCR_SIZE = 10;
	protected Object[] mData = new Object[INCR_SIZE];
	protected int mHead;
	protected int mTail;
	protected boolean mEmpty = true;

	public ArrQueue() {};

	public Item getFront() {return (Item)mData[mHead];}

	public boolean isEmpty() {return mEmpty;}

	public void add(Item val) {
		mData[mTail++] = val;
		if (mTail == mData.length)
			mTail = 0;

		if (mTail == mHead) {
			Object[] newData = new Object[mData.length + 10];
			if (mHead < mTail)
				System.arraycopy(mData, mHead, newData, 0, mTail - mHead);

			else {
				System.arraycopy(mData, mHead, newData, 0,
				 (mData.length - mHead));
				System.arraycopy(mData, 0, newData, mData.length - mHead, 
				 mTail);
			}

			mHead = 0;
			mTail = mData.length;
			mData = newData;	   
		}   
		mEmpty = false;	   
	}

	public void remove() {  
		if (++mHead == mData.length) 	
			mHead = 0;
		mEmpty = (mHead == mTail);
	}

	public boolean equals(Object obj) {
		int newHead, oldHead;

		if (!(obj instanceof ArrQueue<?>)) 
			return false;
		ArrQueue<Item> newarrqueue = (ArrQueue<Item>) obj;  
		newHead = newarrqueue.mHead; 
		oldHead = mHead;

		while (newHead != newarrqueue.mTail && oldHead != mTail) {
			if (!(mData[oldHead]).equals(newarrqueue.mData[newHead]))	
				break;
			if (++oldHead == mData.length)	
				oldHead = 0;
			if (++newHead == newarrqueue.mData.length)	
				newHead = 0;    
		}	
		return ((oldHead == mTail) && (newHead == newarrqueue.mTail));       	   
	}

	public Object clone() {
		ArrQueue<Item> newQueue = null;

		try {
			newQueue = (ArrQueue<Item>)super.clone(); 
			newQueue.mData = new Object[mData.length];
			System.arraycopy(mData, 0, newQueue.mData, 0, mData.length);
		}
		catch (CloneNotSupportedException ex) {};

		return newQueue;
	}
}
