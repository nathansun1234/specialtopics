import java.util.HashMap;
import java.util.Arrays;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

public class BaseballElimination {
    private final int numberOfTeams;
    private String[] teams;
    private int[] losses;
    private int[] remaining;
    private int[] wins;
    private HashMap<Integer, Bag<Integer>> certificates;
    private int[][] games;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();        
        
        certificates = new HashMap<Integer, Bag<Integer>>(numberOfTeams);
        
        teams = new String[numberOfTeams];
        losses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        wins = new int[numberOfTeams];
        games = new int[numberOfTeams][numberOfTeams];
        
        for (int i = 0; i < numberOfTeams; i++) {
            teams[i] = in.readString();
            wins[i] = in.readInt();           
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < numberOfTeams; j++) {
                games[i][j] = in.readInt();
            }
        }
    }
    
    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        int teamIndex = -1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team)) {
                teamIndex = i;
            }
        }

        if (teamIndex == -1) {
            throw new java.lang.IllegalArgumentException();

        }

        return wins[teamIndex];
    }

    // number of losses for given team
    public int losses(String team) {
        int teamIndex = -1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team)) {
                teamIndex = i;
            }
        }

        if (teamIndex == -1) {
            throw new java.lang.IllegalArgumentException();

        }

        return losses[teamIndex];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        int teamIndex = -1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team)) {
                teamIndex = i;
            }
        }

        if (teamIndex == -1) {
            throw new java.lang.IllegalArgumentException();

        }

        return remaining[teamIndex];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int teamIndex1 = -1;
        int teamIndex2 = -1;

        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team1)) {
                teamIndex1 = i;
            }

            if (teams[i].equals(team2)) {
                teamIndex2 = i;
            }
        }

        if (teamIndex1 == -1 || teamIndex2 == -1) {
            throw new java.lang.IllegalArgumentException();
        }

        return this.games[teamIndex1][teamIndex2];
    }
    
    private FlowNetwork createFlowNetwork(int teamEx) {
        int numMatches = numberOfTeams * (numberOfTeams - 1) / 2;
        
        int s = numberOfTeams + numMatches;
        int t = numberOfTeams + numMatches + 1;
        
        int possibleWins = wins[teamEx] + remaining[teamEx];

        FlowNetwork fn = new FlowNetwork(numberOfTeams + numMatches + 2);

        for (int i = 0; i < numberOfTeams; i++) {
            if (i != teamEx)
               fn.addEdge(new FlowEdge(i, t, possibleWins - wins[i]));
        }

        int g = 0;
        
        for (int i = 0; i < numberOfTeams; i++) {
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (i != teamEx && j != teamEx) {
                    fn.addEdge(new FlowEdge(s, numberOfTeams + g, games[i][j]));
                    fn.addEdge(new FlowEdge(numberOfTeams + g, i, Double.MAX_VALUE));
                    fn.addEdge(new FlowEdge(numberOfTeams + g, j, Double.MAX_VALUE));
                }
                g++;
            }       
        }

        return fn;
    }
    
    private FordFulkerson createFordFulkerson(FlowNetwork fn) {
        FordFulkerson ff = new FordFulkerson(fn, fn.V() - 2, fn.V() - 1);
        return ff;
    }
    
    private boolean isFull(FlowNetwork fn) {
        for (FlowEdge edge : fn.adj(fn.V() - 2)) {
            if (edge.capacity() != edge.flow()) {
                return false;
            }  
        }

        return true;
    }
        
    // is given team eliminated?
    public boolean isEliminated(String team) {
        int teamIndex = -1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team)) {
                teamIndex = i;
            }
        }

        if (teamIndex == -1) {
            throw new java.lang.IllegalArgumentException();

        }

        if (!certificates.containsKey(teamIndex)) {
            int maxNumWins = wins[teamIndex] + remaining[teamIndex];
            boolean done = false;
            for (int i = 0; i < numberOfTeams; i++) {
                if (wins[i] > maxNumWins) {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(i);

                    certificates.put(teamIndex, bag);
                    done = true;                
                }
            }
            
            if (!done) {
                FlowNetwork fn = createFlowNetwork(teamIndex);
                FordFulkerson maxflow = createFordFulkerson(fn);
                Bag<Integer> bag = new Bag<Integer>();

                if (!isFull(fn)) {
                    for (int v = 0; v < numberOfTeams; v++)
                    {
                        if (maxflow.inCut(v))
                            bag.add(v);
                    }            
                }

                certificates.put(teamIndex, bag);
            }
        }

        return !certificates.get(teamIndex).isEmpty();
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int teamIndex = -1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team)) {
                teamIndex = i;
            }
        }

        if (teamIndex == -1) {
            throw new java.lang.IllegalArgumentException();

        }

        if (!certificates.containsKey(teamIndex)) {
            int maxNumWins = wins[teamIndex] + remaining[teamIndex];
            boolean done = false;
            for (int i = 0; i < numberOfTeams; i++) {
                if (wins[i] > maxNumWins) {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(i);

                    certificates.put(teamIndex, bag);
                    done = true;                
                }
            }
            
            if (!done) {
                FlowNetwork fn = createFlowNetwork(teamIndex);
                FordFulkerson maxflow = createFordFulkerson(fn);
                Bag<Integer> bag = new Bag<Integer>();

                if (!isFull(fn)) {
                    for (int v = 0; v < numberOfTeams; v++)
                    {
                        if (maxflow.inCut(v))
                            bag.add(v);
                    }            
                }

                certificates.put(teamIndex, bag);
            }
        }
            
        if (certificates.get(teamIndex).isEmpty()) {
            return null;
        }
            
        Bag<String> certificate = new Bag<String>();

        for (int i : certificates.get(teamIndex))
        {
            certificate.add(teams[i]);
        }
        return certificate;
    }
    
    public static void main(String[] args) {

    }
}