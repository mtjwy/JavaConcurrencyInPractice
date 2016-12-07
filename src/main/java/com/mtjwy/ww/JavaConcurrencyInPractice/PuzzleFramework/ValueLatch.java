package com.mtjwy.ww.JavaConcurrencyInPractice.PuzzleFramework;

import java.util.concurrent.CountDownLatch;

public class ValueLatch<T> {

	private T value = null;
	private final CountDownLatch done = new CountDownLatch(1);

	/**
	 * Test whether value has been set
	 * @return
	 */
	public boolean isSet() {
		return (done.getCount() == 0);
	}

	/**
	 * Set value and decrement CountDownLatch
	 * @param newValue
	 */
	public synchronized void setValue(T newValue) {
		if (!isSet()) {
			value = newValue;
			done.countDown();
		}
	}

	public T getValue() throws InterruptedException {
		done.await();//block until CountDownLatch reaches 0 (that is some thread has set the value)
		synchronized (this) {
			return value;
		}
	}

}
