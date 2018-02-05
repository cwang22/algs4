import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final Map<Integer, String> words;
    private final Map<String, Set<Integer>> indexes;
    private final Set<String> nouns;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        words = new HashMap<>();
        indexes = new HashMap<>();
        nouns = new HashSet<>();

        In synsetsFile = new In(synsets);

        while (synsetsFile.hasNextLine()) {
            String[] line = synsetsFile.readLine().split(",");
            words.put(Integer.valueOf(line[0]), line[1]);

            for (String noun : line[1].split(" ")) {
                nouns.add(noun);

                Set<Integer> index = indexes.containsKey(noun) ? indexes.get(noun) : new HashSet<>();
                index.add(Integer.valueOf(line[0]));
                indexes.put(noun, index);
            }
        }

        Digraph g = new Digraph(words.size());

        In hypernymsFile = new In(hypernyms);

        while (hypernymsFile.hasNextLine()) {
            String[] line = hypernymsFile.readLine().split(",");
            int id = Integer.parseInt(line[0]);

            for (int i = 1; i < line.length; i++) {
                g.addEdge(id, Integer.parseInt(line[i]));
            }
        }

        DirectedCycle cycle = new DirectedCycle(g);

        if (cycle.hasCycle() || !rootedDAG(g)) {
            throw new IllegalArgumentException();
        }

        sap = new SAP(g);
    }

    private boolean rootedDAG(Digraph g) {
        int roots = 0;

        for (int i = 0; i < g.V(); i++) {
            if (!g.adj(i).iterator().hasNext()) {
                roots++;
                if (roots > 1) {
                    return false;
                }
            }
        }

        return roots == 1;
    }

    public Iterable<String> nouns() {
        return nouns;
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nouns.contains(word);
    }

    public int distance(String a, String b) {
        if (a == null || b == null) throw new IllegalArgumentException();
        return sap.length(get(a), get(b));
    }

    public String sap(String a, String b) {
        if (a == null || b == null) throw new IllegalArgumentException();
        int id = sap.ancestor(get(a), get(b));
        return words.get(id);
    }

    private Set<Integer> get(String noun) {
        if (!indexes.containsKey(noun)) throw new IllegalArgumentException();
        return indexes.get(noun);
    }

    public static void main(String[] args) {

    }
}
