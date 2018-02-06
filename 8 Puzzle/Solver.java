import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solver {
    private Deque<Board> solution;

    private class Node implements Comparable<Node> {
        private Board board;
        private Node prev;
        private int moves;
        private int priority;

        private Node(Board board, Node prev) {
            this.board = board;
            this.prev = prev;
            if (prev == null) moves = 0;
            else moves = prev.moves + 1;
            priority = moves + board.manhattan();
        }

        private Deque<Board> path() {
            Deque<Board> queue = new ArrayDeque<>();
            Node current = this;

            while (current != null) {
                queue.addFirst(current.board);
                current = current.prev;
            }

            return queue;
        }

        @Override
        public int compareTo(Node n) {
            return priority - n.priority;
        }
    }

    public Solver(Board initial) {
        Node node = new Node(initial, null);
        Node twinNode = new Node(initial.twin(), null);

        MinPQ<Node> PQ = new MinPQ<>();
        MinPQ<Node> twinPQ = new MinPQ<>();

        PQ.insert(node);
        twinPQ.insert(twinNode);

        while (true) {
            Node current = PQ.delMin();

            if (current.board.isGoal()) {
                solution = current.path();
                break;
            } else {
                for (Board b : current.board.neighbors()) {
                    if (current.prev != null && b.equals(current.prev.board)) continue;
                    Node next = new Node(b, current);
                    PQ.insert(next);
                }
            }

            Node twinCurrent = twinPQ.delMin();

            if (twinCurrent.board.hamming() == 0) {
                break;
            } else {
                for (Board b : twinCurrent.board.neighbors()) {
                    if (twinCurrent.prev != null && b.equals(twinCurrent.prev.board)) continue;
                    Node next = new Node(b, twinCurrent);
                    twinPQ.insert(next);
                }
            }
        }
    }

    public boolean isSolvable() {
        return solution != null;
    }

    public int moves() {
        return solution == null ? -1 : solution.size() - 1;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}