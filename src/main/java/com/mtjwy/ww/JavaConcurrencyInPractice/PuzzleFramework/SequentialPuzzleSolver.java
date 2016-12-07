package com.mtjwy.ww.JavaConcurrencyInPractice.PuzzleFramework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A sequential solver for the puzzle framework.
 * 
 * Performs a depth-first search of the puzzle space.
 * @author yu
 *
 * @param <P>
 * @param <M>
 */
public class SequentialPuzzleSolver<P, M> {
	private final Puzzle<P, M> puzzle;
	private final Set<P> seen = new HashSet<P>();
	
	public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
		this.puzzle = puzzle;
	}
	
	public List<M> solve() {
		P pos = puzzle.initialPosition();
		return depthFirstSearch(new Node<P, M>(pos, null, null));
	}
	
	/*
	 * Perform a depth-first search of the puzzle space.
	 * Terminates when it finds a solution, which
	 * is not necessarily the shortest solution.
	 */
	private List<M> depthFirstSearch(Node<P, M> node) {
		if (!seen.contains(node.pos)) {
			seen.add(node.pos);
			if (puzzle.isGoal(node.pos)){
				return node.asMoveList();
			}
			for (M move : puzzle.legalMoves(node.pos)) {
				P pos = puzzle.move(node.pos, move);
				Node<P, M> child = new Node<P, M>(pos, move, node);
				List<M> result = depthFirstSearch(child);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

}
