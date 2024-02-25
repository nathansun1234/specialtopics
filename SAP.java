import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new NullPointerException();
        }
        else {
            this.G = new Digraph(G);
        }

    }
 
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= G.V() || w >= G.V()) {
            throw new IllegalArgumentException();
        }
        else {
            BreadthFirstDirectedPaths vbsf = new BreadthFirstDirectedPaths(G, v);
            BreadthFirstDirectedPaths wbsf = new BreadthFirstDirectedPaths(G, w);

            int ancestor = ancestor(v, w);

            if (ancestor == -1) {
                return -1;
            }

            else {
                return vbsf.distTo(ancestor) + wbsf.distTo(ancestor);
            }
        }
    }
 
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= G.V() || w >= G.V()) {
            throw new IllegalArgumentException();
        }
        else {
            BreadthFirstDirectedPaths vbsf = new BreadthFirstDirectedPaths(G, v);
            BreadthFirstDirectedPaths wbsf = new BreadthFirstDirectedPaths(G, w);

            int ancestor = -1;
            int currentShortest = Integer.MAX_VALUE;

            for (int n = 0; n < this.G.V(); n++) {
                int distance = vbsf.distTo(n) + wbsf.distTo(n);
                if (vbsf.hasPathTo(n) && wbsf.hasPathTo(n) && (distance < currentShortest)) {
                    ancestor = n;
                    currentShortest = distance;
                }
            }

            return ancestor;
        }
    }
 
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        for (Object obj : v) {
            Integer asInt = (Integer) obj;
            if ( obj == null || asInt < 0 || asInt >= this.G.V()) {
                throw new IllegalArgumentException();
            }    
        }
            
        for (Object obj : w) {
            Integer asInt = (Integer) obj;
            if ( obj == null || asInt < 0 || asInt >= this.G.V()) {
                throw new IllegalArgumentException();
            }    
        }

        BreadthFirstDirectedPaths vbsf = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wbsf = new BreadthFirstDirectedPaths(G, w);

        int ancestor = ancestor(v, w);

        if (ancestor == -1) {
            return -1;
        }

        else {
            return vbsf.distTo(ancestor) + wbsf.distTo(ancestor);
        }
    }
 
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        for (Object obj : v) {
            Integer asInt = (Integer) obj;
            if ( obj == null || asInt < 0 || asInt >= this.G.V()) {
                throw new IllegalArgumentException();
            }    
        }
            
        for (Object obj : w) {
            Integer asInt = (Integer) obj;
            if ( obj == null || asInt < 0 || asInt >= this.G.V()) {
                throw new IllegalArgumentException();
            }    
        }

        BreadthFirstDirectedPaths vbsf = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wbsf = new BreadthFirstDirectedPaths(G, w);

        int ancestor = -1;
        int currentShortest = Integer.MAX_VALUE;

        for (int n = 0; n < this.G.V(); n++) {
            int distance = vbsf.distTo(n) + wbsf.distTo(n);
            if (vbsf.hasPathTo(n) && wbsf.hasPathTo(n) && (distance < currentShortest)) {
                ancestor = n;
                currentShortest = distance;
            }
        }

        return ancestor;

    }
 }
