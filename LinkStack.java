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

public class LinkStack<Item> implements Stack<Item> {
	protected Node mHead;
	protected int mCount;

	protected static class Node {
		Object data;
		Node next;

		public Node(Object obj, Node node) {data = obj; next = node;}
	}

	public LinkStack() {mHead = null;}

	public void push(Item obj) {
		mCount++; 
		mHead = new Node(obj, mHead);
	}

	public Object clone() {
		Object obj = null;
		try {
			obj = super.clone();
		}
		catch (CloneNotSupportedException ex) {}
		
		return obj;
	}

	public Item top() {
		if (mHead == null)
			throw new NullPointerException("Stack underflow");
		else
			return (Item) mHead.data;
	}

	public void pop() {
		if (mHead == null)
			throw new NullPointerException("Stack underflow");
		else {
			mHead = mHead.next;
			mCount--;
		}
	}

	public int getCount() {return mCount;}

	public boolean isEmpty() {return mHead == null;}

	public boolean isOn(Item obj) {
		Node tmp;
		for (tmp = mHead; tmp != null && !tmp.data.equals(obj); tmp = tmp.next)
			;
		return tmp != null;
	}

	public boolean equals(Object obj) {
		LinkStack linkStack = (LinkStack)obj;
		Node curr = mHead;
		Node input = linkStack.mHead;

		if (!(obj instanceof LinkStack))
			return false;
		while (curr != null && input != null && curr.data.equals(input.data)) {
			curr = curr.next;
			input = input.next;
		}
		return curr == null && input == null;
	}
}