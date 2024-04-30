/**
 * @author Nathan Sun attests that this code is their original work and was written in compliance with the
 * class Academic Integrity and Collaboration Policy found in the syllabus. 
 */

// i think the hardest part for me was figuring out the numbers for the size of the flow network, source
// node, and target node

// size of the flow network is (numberOfTeams + numMatches + 2) because its the number of teams, number of
// matchups, the start node, and the target node

// s creates a vertex that comes after all the team vertices and potential matchup vertices in the network
// to act as a convenient starting point for the edges representing the flow from teams to the target

// (numberOfTeams + numMatches) represents the vertex just before the sink vertex, so adding 1 means that t
// is positioned immediately after all other vertices in the network, including the source, team, and
// potential matchup vertices

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
        
        teams = new String[numberOfTeams];
        losses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        wins = new int[numberOfTeams];
        certificates = new HashMap<Integer, Bag<Integer>>(numberOfTeams);
        games = new int[numberOfTeams][numberOfTeams];
        
        // populate arrays with input file
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
        // find the index of the team were asking about
        int teamIndex = -1;

        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team)) {
                teamIndex = i;
            }
        }

        if (teamIndex == -1) {
            throw new java.lang.IllegalArgumentException();

        }

        // get that index from array of wins
        return wins[teamIndex];
    }

    // number of losses for given team
    public int losses(String team) {
        // find the index of the team were asking about
        int teamIndex = -1;

        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team)) {
                teamIndex = i;
            }
        }

        if (teamIndex == -1) {
            throw new java.lang.IllegalArgumentException();

        }

        // get that index from array of losses
        return losses[teamIndex];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        // find the index of the team were asking about
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
        // find the index of the teams were asking about
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

        // get those indexes from array of games
        return games[teamIndex1][teamIndex2];
    }
    
    private FlowNetwork createFlowNetwork(int teamEx) {
        // number of teams choose two (combinations)
        int numMatches = numberOfTeams * (numberOfTeams - 1) / 2;
        
        int s = numberOfTeams + numMatches;
        int t = numberOfTeams + numMatches + 1;
        
        int possibleWins = wins[teamEx] + remaining[teamEx];

        FlowNetwork fn = new FlowNetwork(numberOfTeams + numMatches + 2);

        // create edges from team vertices to the sink vertex
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != teamEx)
               fn.addEdge(new FlowEdge(i, t, possibleWins - wins[i]));
        }

        int g = 0;

        // create edges from the source vertex to the game vertices and from game vertices to team vertices
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
        
    // is given team eliminated?
    public boolean isEliminated(String team) {
        int teamIndex = -1;

        // find the index of the team were asking about
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i].equals(team)) {
                teamIndex = i;
            }
        }
    
        if (teamIndex == -1) {
            throw new java.lang.IllegalArgumentException();
        }
    
        // If the certificates map does not contain a certificate for this team, calculate it
        if (!certificates.containsKey(teamIndex)) {
            // maximum number of wins possiblyachievable by the team
            int maxNumWins = wins[teamIndex] + remaining[teamIndex];
            
            boolean done = false;

            // check if any other team has more wins than the maximum achievable wins by this team
            for (int i = 0; i < numberOfTeams; i++) {
                if (wins[i] > maxNumWins) {
                    // if so, add the index of the winning team to the certificate bag
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(i);
                    certificates.put(teamIndex, bag);
                    done = true;                
                }
            }
            
            // if no other team has more wins, proceed with flow network analysis
            if (!done) {
                FlowNetwork fn = createFlowNetwork(teamIndex);
                FordFulkerson maxflow = createFordFulkerson(fn);
                Bag<Integer> bag = new Bag<Integer>();
    
                boolean isFull = true;
                // check if the flow network is full
                for (FlowEdge edge : fn.adj(fn.V() - 2)) {
                    if (edge.capacity() != edge.flow()) {
                        isFull = false;
                    }  
                }
        
                // if the flow network is not full, determine the subset of teams that eliminate the given team
                if (!isFull) {
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

        // if the certificates map does not contain a certificate for this team, calculate it
        if (!certificates.containsKey(teamIndex)) {
            // calculate the maximum number of wins achievable by the team
            int maxNumWins = wins[teamIndex] + remaining[teamIndex];
            boolean done = false;

            // check if any other team has more wins than the maximum achievable wins by this team
            for (int i = 0; i < numberOfTeams; i++) {
                if (wins[i] > maxNumWins) {
                    // if so, add the index of the winning team to the certificate bag
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(i);
                    certificates.put(teamIndex, bag);
                    done = true;                
                }
            }
            
            // if no other team has more wins, proceed with flow network analysis
            if (!done) {
                FlowNetwork fn = createFlowNetwork(teamIndex);
                FordFulkerson maxflow = createFordFulkerson(fn);
                Bag<Integer> bag = new Bag<Integer>();

                boolean isFull = true;

                // check if the flow network is full
                for (FlowEdge edge : fn.adj(fn.V() - 2)) {
                    if (edge.capacity() != edge.flow()) {
                        isFull = false;
                    }  
                }

                // if the flow network is not full, determine the subset of teams that eliminate the given team
                if (!isFull) {
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
            
        // otherwise, convert the indices in the certificate bag to team names and return
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