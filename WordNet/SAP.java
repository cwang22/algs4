import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;

    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    public int length(int v, int w) {
        return new CalculateSAP(v, w).length;
    }

    public int ancestor(int v, int w) {
        return new CalculateSAP(v, w).ancestor;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return new CalculateSAP(v, w).length;
    }


    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return new CalculateSAP(v, w).ancestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private class CalculateSAP {
        int length = Integer.MAX_VALUE;
        int ancestor = -1;

        CalculateSAP(int v, int w) {
            validate(v);
            validate(w);
            BreadthFirstDirectedPaths pathFromV = new BreadthFirstDirectedPaths(G, v);
            BreadthFirstDirectedPaths pathFromW = new BreadthFirstDirectedPaths(G, w);
            calculate(pathFromV, pathFromW);
        }


        CalculateSAP(Iterable<Integer> v, Iterable<Integer> w) {
            if (v == null || w == null) throw new IllegalArgumentException();

            for (int i : v) {
                validate(i);
            }

            for (int i : w) {
                validate(i);
            }

            BreadthFirstDirectedPaths pathFromV = new BreadthFirstDirectedPaths(G, v);
            BreadthFirstDirectedPaths pathFromW = new BreadthFirstDirectedPaths(G, w);
            calculate(pathFromV, pathFromW);
        }

        private void calculate(BreadthFirstDirectedPaths pathFromV, BreadthFirstDirectedPaths pathFromW) {
            for (int i = 0; i < G.V(); i++) {
                if (pathFromV.hasPathTo(i) && pathFromW.hasPathTo(i)) {
                    if (length > pathFromV.distTo(i) + pathFromW.distTo(i)) {
                        length = pathFromV.distTo(i) + pathFromW.distTo(i);
                        ancestor = i;
                    }
                }
            }
            length = length == Integer.MAX_VALUE ? -1 : length;
        }

        private void validate(int i) {
            if (i < 0 || i >= G.V()) throw new IllegalArgumentException();
        }
    }
}