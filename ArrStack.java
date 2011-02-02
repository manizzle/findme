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

public class ArrStack<Item> implements Stack<Item> {  
	final int INCREMENT = 10;
	int count;
	Object data [];

	public ArrStack() {
		count = 0;
		data = new Object[INCREMENT];
	}

	public void push(Item val) {
		if (count == data.length) {
			Object newData [] = new Object[data.length + INCREMENT];
			System.arraycopy(data, 0, newData, 0, data.length);
			data = newData;
		}
		data[count++] = val;
	}

	public Item top() {
		if (count == 0)
			throw new IndexOutOfBoundsException("Stack Underflow");
		return (Item)(data[count-1]);
	}

	public void pop() {
		if (count == 0)
			throw new IndexOutOfBoundsException("Stack Underflow");
		count--;
	}

	public boolean isOn(Item val) {
		int ndx;

		for (ndx = 0; ndx < count && !data[ndx].equals(val); ndx++)
			;
		return (ndx < count);
	}

	public boolean isEmpty() {return (count == 0);}

	public int getCount() {return count;}

	public Object clone() {
		ArrStack<Item> arrStack = null;

		try {
			arrStack = (ArrStack<Item>) super.clone();
			arrStack.data = new Object[count];
			System.arraycopy(data, 0, arrStack.data, 0, count);
		}
		
		catch (CloneNotSupportedException ex){
			System.out.println("Clone Not Supported");
		}
		return arrStack;
	}
}