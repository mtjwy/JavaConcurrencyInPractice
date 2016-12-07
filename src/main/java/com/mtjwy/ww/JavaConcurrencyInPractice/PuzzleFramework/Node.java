package com.mtjwy.ww.JavaConcurrencyInPractice.PuzzleFramework;

import java.util.LinkedList;
import java.util.List;

/**
 * Node represents a position that has been reached through
 * some series of moves.
 * @author yu
 *
 * @param <P>
 * @param <M>
 */
class Node<P, M> {
	final P pos;//current position
	final M move;// a move that created current position
	final Node<P, M> prev;
	
	Node(P pos, M move, Node<P, M> prev) {
		this.pos = pos;
		this.move = move;
		this.prev = prev;
	}
	
	/**
	 * Reconstruct the sequence of moves that
	 * led to the current position
	 * @return
	 */
	List<M> asMoveList() {
		List<M> res = new LinkedList<M>();
		for (Node<P, M> n = this; n.move != null; n = n.prev) {
			res.add(0, n.move);
		}
		return res;
	}

}
