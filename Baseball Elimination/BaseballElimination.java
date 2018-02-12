import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseballElimination {
    private final int n; // number of teams
    private final int v; // number of vertices

    private final List<String> teams;
    private final Map<String, Integer> map;

    private final String[] names;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] games;

    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();

        In in = new In(filename);

        n = in.readInt();
        v = n * (n - 1) / 2 + n + 2;

        teams = new ArrayList<>();
        map = new HashMap<>();

        names = new String[n];
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        games = new int[n][n];

        for (int i = 0; i < n; i++) {
            String name = in.readString();
            teams.add(name);
            map.put(name, i);
            names[i] = name;
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < n; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return n;
    }

    public Iterable<String> teams() {
        return teams;
    }

    public int wins(String team) {
        if (team == null || !map.containsKey(team)) throw new IllegalArgumentException();
        return wins[map.get(team)];
    }

    public int losses(String team) {
        if (team == null || !map.containsKey(team)) throw new IllegalArgumentException();
        return losses[map.get(team)];
    }

    public int remaining(String team) {
        if (team == null || !map.containsKey(team)) throw new IllegalArgumentException();
        return remaining[map.get(team)];
    }

    public int against(String team1, String team2) {
        if (team1 == null || !map.containsKey(team1)) throw new IllegalArgumentException();
        if (team2 == null || !map.containsKey(team2)) throw new IllegalArgumentException();
        return games[map.get(team1)][map.get(team2)];
    }

    public boolean isEliminated(String team) {
        if (team == null || !map.containsKey(team)) throw new IllegalArgumentException();
        int index = map.get(team);
        int total = 0;

        for (int i = 0; i < n; i++) {
            if (i != index && wins[index] + remaining[index] < wins[i]) return true;
            for (int j = i + 1; j < n; j++) total += games[i][j];
        }

        FordFulkerson maxFlow = maxFlow(index);
        return (int) maxFlow.value() != total;

    }

    private FordFulkerson maxFlow(int index) {
        int e = 1;

        FlowNetwork G = new FlowNetwork(v);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                G.addEdge(new FlowEdge(0, e, games[i][j]));
                G.addEdge(new FlowEdge(e, v - n - 1 + i, Double.POSITIVE_INFINITY));
                G.addEdge(new FlowEdge(e, v - n - 1 + j, Double.POSITIVE_INFINITY));
                e++;
            }

            if (wins[index] + remaining[index] - wins[i] > 0)
                G.addEdge(new FlowEdge(v - 1 - n + i, v - 1, wins[index] + remaining[index] - wins[i]));
        }

        return new FordFulkerson(G, 0, v - 1);
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !map.containsKey(team)) throw new IllegalArgumentException();
        if (!isEliminated(team)) return null;
        List<String> subset = new ArrayList<>();
        int index = map.get(team);

        for (int i = 0; i < n; i++) {
            if (wins[index] + remaining[index] - wins[i] < 0) subset.add(names[i]);
        }

        if (!subset.isEmpty()) return subset;

        FordFulkerson maxFlow = maxFlow(index);

        for (int i = 0; i < n; i++) {
            if (i != index && maxFlow.inCut(v - 1 - n + i)) subset.add(names[i]);
        }

        return subset;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
