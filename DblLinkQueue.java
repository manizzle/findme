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

public class DblLinkQueue<Item> implements Queue<Item> {	

	protected static class Node { 
		public Object data;
		public Node prev;
		public Node next;

		public Node(Object dataPoint, Node prevPoint, Node nextPoint) {
			data = dataPoint; 
			prev = prevPoint; 
			next = nextPoint;
		}
	}

	protected Node mHead;

	public DblLinkQueue() {
		mHead = new Node(null, null, null);
		mHead.next = mHead;
		mHead.prev = mHead;
	}

	public void add(Item val) {
		mHead.prev= new Node(val, mHead.prev ,mHead);
		mHead.prev.prev.next = mHead.prev;
	}

	public void remove() {
		if (mHead.prev == mHead)
			throw new NullPointerException();
		else {
			mHead.next = mHead.next.next;
			mHead.next.prev = mHead;
			return;
		}	
	}

	public Item getFront() {
		if (mHead.prev == mHead)
			throw new NullPointerException();
		else
			return (Item) mHead.next.data;
	}

	public boolean isEmpty() {return mHead.prev == mHead;}

	public boolean equals(Object obj) {
		Node temp1;
		Node temp2;

		if (!(obj instanceof DblLinkQueue<?>))
			return false;
		DblLinkQueue<Item> dblQueue = (DblLinkQueue<Item>)obj;
		temp1 = mHead.prev;
		temp2 = dblQueue.mHead.prev;

		while (temp1 != mHead && temp2 != dblQueue.mHead) {
			if (!(temp1.data).equals(temp2.data))
				break;
			temp1 = temp1.prev;
			temp2 = temp2.prev;
		}

		return ((temp2 == dblQueue.mHead) && (temp1 == mHead));	   	   
	}

	public Object clone() {
		DblLinkQueue<Item> dblLink = null;
		Node temp;

		try {
			dblLink = (DblLinkQueue)super.clone();
			dblLink.mHead = new Node(null, null, null);  
			dblLink.mHead.next = dblLink.mHead;
			dblLink.mHead.prev = dblLink.mHead;

			for (temp = mHead.next; temp != mHead; temp = temp.next) {
				dblLink.mHead.prev = new Node(temp.data, dblLink.mHead.prev,
				 dblLink.mHead);  
				dblLink.mHead.prev.prev.next = dblLink.mHead.prev;
			}
		}
		catch (CloneNotSupportedException ex) {}

		return dblLink;
	}	
}