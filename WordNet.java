import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedDFS;


public class WordNet {

    private Map<String, Bag<Integer>> nounsToIDs;
    private Map<Integer, String> iDsToSynsets;
    private SAP sap;
    private Digraph G; 

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        else {
            nounsToIDs = new HashMap<String, Bag<Integer>>();
            iDsToSynsets = new HashMap<Integer, String>();

            In in = new In(synsets);

            while (!in.isEmpty()) {
                // gets each line of file
                String[] line = in.readLine().split(",");
                int id = Integer.parseInt(line[0]);

                // splits line into nouns
                String[] nouns = line[1].split(" ");

                // maps id to corresponding synset
                iDsToSynsets.put(id, line[1]);

                // maps noun to corresponding id for each noun
                for (String noun : nouns) {
                    // if it alr has the noun then we just need to add the id
                    if (nounsToIDs.containsKey(noun)) {
                        nounsToIDs.get(noun).add(id);
                    }
                    // otherwise we need to add the noun to the map and addd the id to it
                    else {
                        nounsToIDs.put(noun, new Bag<Integer>());
                        nounsToIDs.get(noun).add(id);
                    }

                }
            }

            G = new Digraph(iDsToSynsets.size());

            in = new In(hypernyms);

            while (!in.isEmpty()) {
                // gets each line of file
                String[] line = in.readLine().split(",");

                // gets the first word in the line as a vertex
                int vertex = Integer.parseInt(line[0]);

                // add edge for each word in the line except the first to the first
                for (int i = 1; i < line.length; i++) {
                    G.addEdge(vertex, Integer.parseInt(line[i]));
                }
            }

            // check for cycles
            DirectedCycle directedCycle = new DirectedCycle(G);

            if (directedCycle.hasCycle()) {
                throw new IllegalArgumentException();
            }

            // check for root of graph
            ArrayList<Integer> roots = new ArrayList<Integer>();

            for (int i = 0; i < G.V(); i++) {
                DirectedDFS dfs = new DirectedDFS(G, i);

                // its a root if theres exactly one vertex that reaches it
                if (dfs.count() == 1) {
                    roots.add(i);
                }
            }

            if (roots.size() != 1) {
                throw new IllegalArgumentException();
            }

            sap = new SAP(G);
        }   
    }
 
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsToIDs.keySet();
    }
 
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        else {
            return nounsToIDs.containsKey(word);
        }
    }
 
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !nounsToIDs.containsKey(nounA) || !nounsToIDs.containsKey(nounB)) {
            throw new IllegalArgumentException();
        }
        else {
            return sap.length(nounsToIDs.get(nounA), nounsToIDs.get(nounB));
        }
    }
 
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !nounsToIDs.containsKey(nounA) || !nounsToIDs.containsKey(nounB)) {
            throw new IllegalArgumentException();
        }
        else {
            int ancestor = sap.ancestor(nounsToIDs.get(nounA), nounsToIDs.get(nounB));
            return iDsToSynsets.get(ancestor);
        } 
    }
 }