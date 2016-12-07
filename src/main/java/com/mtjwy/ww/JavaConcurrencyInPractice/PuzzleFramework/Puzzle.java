package com.mtjwy.ww.JavaConcurrencyInPractice.PuzzleFramework;

import java.util.Set;

public interface Puzzle<P, M> {
	/**
	 * an initial position
	 * @return
	 */
	P initialPosition();
	
	/**
	 * a goal position
	 * @param position
	 * @return
	 */
	boolean isGoal(P position);
	
	/**
	 * a set of legal moves from a given position 
	 * @param position
	 * @return
	 */
	Set<M> legalMoves(P position);
	
	/**
	 * the result of applying a move to a position
	 * @param position
	 * @param move
	 * @return
	 */
	P move(P position, M move);
}
