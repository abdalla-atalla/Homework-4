package edu.miracosta.cs113;

import java.util.AbstractSequentialList;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.*;

public class DoubleLinkedList<E> extends AbstractSequentialList<E> { // Data fields
	private Node<E> head = null; // points to the head of the list
	private Node<E> tail = null; // points to the tail of the list
	private int size = 0; // the number of items in the list

	public void add(int index, E obj) {
		listIterator(index).add(obj);
	}

	public void addFirst(E obj) {
		Node<E> firstNode = new Node<E>(obj);
		firstNode.next = head;
		head.prev = firstNode;
		head = firstNode;
		size++;
	}

	public void addLast(E obj) {
		Node<E> lastNode = new Node<>(obj);
		lastNode.prev = tail;
		tail.next = lastNode;
		tail = lastNode;
		size++;
	}

	public E get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<E> iter = listIterator(index);
		return iter.next();
	}

	public E getFirst() {
		return head.data;
	}

	public E getLast() {
		return tail.data;
	}

	public int size() {
		return size;
	}

	public E remove(int index) {
		E returnValue = null;
		ListIterator<E> iter = listIterator(index);
		if (iter.hasNext()) {
			returnValue = iter.next();
			iter.remove();
		} else {
			throw new IndexOutOfBoundsException();
		}
		return returnValue;
	}

	public Iterator iterator() {
		return new ListIter(0);
	}

	public ListIterator listIterator() {
		return new ListIter(0);
	}

	public ListIterator listIterator(int index) {
		return new ListIter(index);
	}

	public ListIterator listIterator(ListIterator iter) {
		return new ListIter((ListIter) iter);
	}

// Inner Classes
	private static class Node<E> {
		private E data;
		private Node<E> next = null;
		private Node<E> prev = null;

		private Node(E dataItem) // constructor
		{
			data = dataItem;
		}
	} // end class Node

	public class ListIter implements ListIterator<E> {
		private Node<E> nextItem; // the current node
		private Node<E> lastItemReturned; // the previous node
		private int index = 0; //

		public ListIter(int i) // constructor for ListIter class
		{
			if (i < 0 || i > size) {
				throw new IndexOutOfBoundsException("Invalid index " + i);
			}
			lastItemReturned = null;

			if (i == size) // Special case of last item
			{
				index = size;
				nextItem = null;
			} else // start at the beginning
			{
				nextItem = head;
				for (index = 0; index < i; index++)
					nextItem = nextItem.next;
			} // end else
		} // end constructor

		public ListIter(ListIter other) {
			nextItem = other.nextItem;
			index = other.index;
		}

		public boolean hasNext() {
			return nextItem != null;
		}

		public boolean hasPrevious() {
			if (size == 0)
				return false;
			return (nextItem == null && size != 0) || nextItem.prev != null;
		}

		public int previousIndex() {
			return index - 1;
		}

		public int nextIndex() {
			return index;
		}

		public void set(E o) {
			if (lastItemReturned != null) {
				lastItemReturned.data = o;
			} else {
				throw new IllegalStateException();
			}
		}

		public void remove() {
			if (lastItemReturned == null) {
				throw new IllegalStateException();
			} // special case if there is only one node left,
			if (head == tail) {
				head = null;
				tail = null;

			} else if (lastItemReturned == head) {// to remove the head
				head = nextItem;
				head.prev = null;

			} else if (lastItemReturned == tail) {
				lastItemReturned.prev.next = null;
				tail = lastItemReturned.prev;
				tail.next = null;

			} else if (lastItemReturned != head && lastItemReturned != tail) {
				lastItemReturned.next.prev = lastItemReturned.prev;
				lastItemReturned.prev.next = lastItemReturned.next;
			}
			size--;
			index--;
		}

		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			} else {

				lastItemReturned = nextItem;

				nextItem = nextItem.next;

				index++;
				return lastItemReturned.data;
			}
		}

		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (nextItem == null) {
				nextItem = tail;
			} else {
				nextItem = nextItem.prev;
			}
			lastItemReturned = nextItem;
			index--;
			return lastItemReturned.data;
		}

		public void add(E obj) {

			Node newNode = new Node<E>(obj);

			if (head == null) { // If List is empty
				head = new Node<E>(obj);
				tail = head;
			} else if (nextItem == head) { // Insert at the head
				nextItem.prev = newNode;
				newNode.next = nextItem;
				head = newNode;
			} else if (nextItem == null) { // Insert at the tail
				tail.next = newNode;
				newNode.prev = tail;
				tail = newNode;
			} else { // Insert anywhere
				newNode.next = nextItem;
				newNode.prev = nextItem.prev;
				nextItem.prev.next = newNode;
				nextItem.prev = newNode;
			}

			size++;
			index++;
			lastItemReturned = null;
		}
	}// end of inner class ListIter
}// end of class DoubleLinkedList