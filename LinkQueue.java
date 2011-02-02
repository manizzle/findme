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

class LinkQueue<Item> implements Queue<Item> {

	protected static class Node {

		public Object data;
		public Node next;

		public Node(Object dataPoint, Node nextPoint) {
			data = dataPoint; 
			next = nextPoint;
		}
	}

	protected Node mHead;
	protected Node mTail;

	public LinkQueue() {
		mHead =  null;
		mTail = null;
	}

	public void add(Item val) {
		if (mHead == null) {
			mTail = mHead = new Node(val, null);
			return;
		}

		else {
			mTail =  mTail.next = new Node(val, null);
			return;
		}
	}

	public Item getFront() {
		if (mHead == null)
			throw new NullPointerException();
		else
			return (Item) mHead.data;
	}

	public void remove() {
		if (mHead == null)
			throw new NullPointerException();
		else {
			mHead = mHead.next;
			return;
		}
	}

	public boolean isEmpty() {return mHead == null;}

	public boolean equals(Object input) {
		LinkQueue<Item> obj = null;
		Node temp1 = null, temp2 = null;

		if (!(input instanceof LinkQueue<?>))
			return false;	
		obj = (LinkQueue<Item>) input;
		temp1 = mHead;
		temp2 = obj.mHead;

		while (temp1 != null && temp2 != null) {
			if (!(temp1.data).equals(temp2.data)) {
				break;
			}
			temp1 = temp1.next;
			temp2 = temp2.next;
		}
		return ( (temp1 == null) && (temp2 == null) );
	}

	public Object clone() {
		LinkQueue<Item> linkQueue = null;

		try {
			linkQueue = (LinkQueue<Item>)super.clone();
			Node nextNode = null;
			Node tempItr = null;

			for (tempItr = mHead; tempItr != null; tempItr = tempItr.next) {
				if (nextNode == null)
					nextNode = linkQueue.mHead = new Node(tempItr.data, null);
				else {
					nextNode.next = new Node(tempItr.data, null);
					nextNode = nextNode.next;
				}
			}	
		}
		catch (CloneNotSupportedException ex) {}
		
		return linkQueue;
	}   
}