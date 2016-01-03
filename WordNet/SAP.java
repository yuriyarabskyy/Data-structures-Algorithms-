import edu.princeton.cs.algs4.*;

/**
 * Created by yuriyarabskyy on 09/11/15.
 */
public class SAP {
    final private Digraph digraph;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        digraph = new Digraph(G.V());
        for(int i=0; i<G.V();i++){
            for(int v : G.adj(i)){
                digraph.addEdge(i,v);
            }
        }
    }

    private Finder finder(int v, int w){
        BreadthFirstDirectedPaths v_paths = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths w_paths = new BreadthFirstDirectedPaths(digraph, w);
        return new Finder(digraph,v,v_paths,w_paths);
    }

    private Finder finder(Iterable<Integer> v,Iterable<Integer> w){
        BreadthFirstDirectedPaths v_paths = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths w_paths = new BreadthFirstDirectedPaths(digraph, w);
        return new Finder(digraph,v,v_paths,w_paths);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        Finder find = finder(v,w);
        return find.getMin();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        Finder find = finder(v,w);
        return find.getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        Finder find = finder(v,w);
        return find.getMin();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        Finder find = finder(v,w);
        return find.getAncestor();
    }

    final private class Finder {
        public boolean[] marked;  // marked[v] = true if v is reachable from s
        public int s;       // source vertex
        public BreadthFirstDirectedPaths v_dist, w_dist;
        public int min, ancestor;
        public static final int INFINITY = Integer.MAX_VALUE;
        public Iterable<Integer> it;

        public Finder(Digraph G, int s, BreadthFirstDirectedPaths v_dist, BreadthFirstDirectedPaths w_dist) {
            marked = new boolean[G.V()];
            this.s = s;
            this.v_dist = v_dist;
            this.w_dist = w_dist;
            min = INFINITY;
            dfs(G, s);
        }

        public Finder(Digraph G, Iterable<Integer> s, BreadthFirstDirectedPaths v_dist, BreadthFirstDirectedPaths w_dist) {
            marked = new boolean[G.V()];
            this.it = s;
            this.v_dist = v_dist;
            this.w_dist = w_dist;
            min = Integer.MAX_VALUE;
            for(int v : s) dfs(G, v);
        }


        private void dfs(Digraph G, int v) {
            marked[v] = true;
            if(v_dist.distTo(v)<INFINITY&&w_dist.distTo(v)<INFINITY){
                int sum = v_dist.distTo(v)+w_dist.distTo(v);
                if(sum<min){
                    min = sum;
                    ancestor = v;
                }
            }
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    dfs(G, w);
                }
            }
        }

        public int getMin(){
            if(min<INFINITY) return min;
            return -1;
        }

        public int getAncestor(){
            if(min<INFINITY) return ancestor;
            return -1;
        }
    }

    // do unit testing of this class
    public static void main(String[] args){
        In in = new In("/Volumes/Data/Projects/WordNet/src/wordnet/digraph2.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
