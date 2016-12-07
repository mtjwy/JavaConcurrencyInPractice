package com.mtjwy.ww.JavaConcurrencyInPractice.PuzzleFramework;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ConcurrentPuzzleSolver<P, M> {
	
	private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;
    protected final ValueLatch<Node<P, M>> solution = new ValueLatch<Node<P, M>>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P, Boolean>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
          /*set to discard policy, so that subsequent attempts to execute new task after 
            shutting down the executor fail silently, and we don't need to deal with 
            RejectedExecutionException*/
        }
    }

    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            // block until solution found
            Node<P, M> solnPuzzleNode = solution.getValue();
            return (solnPuzzleNode == null) ? null : solnPuzzleNode.asMoveList();
        } finally {
        	//The first thread to find a solution shuts down the Executor, 
            // prevent new tasks from being accepted.
            exec.shutdown();
        }
    }

    protected Runnable newTask(P p, M m, Node<P, M> n) {
        return new SolverTask(p, m, n);
    }

    protected class SolverTask extends Node<P, M> implements Runnable {
        SolverTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
        }

        /*
         * First consults the solution latch and stops if a solution has already been found.
         * Else evaluating the set of possible next positions, and submitting unsearched
         * positions to an Executor.
         * (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            if (solution.isSet()      //putIfAbsent atomically add a position only if it was not previously known
                    || seen.putIfAbsent(pos, true) != null)//putIfAbsent returns the previous value associated with the specified key, or null if there was no mapping for the key. 
                return; // already solved or seen this position
            if (puzzle.isGoal(pos))
                solution.setValue(this);
            else
                for (M m : puzzle.legalMoves(pos))//performs a breadth-first search
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
        }
    }

}
