import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class Graph {
    int numVertex;  // Number of vertices in the graph.
    GraphNode[] graphNodes;  // Adjacency list for graph.
    String graphName;  //The file from which the graph was created.


    public Graph() {
        this.numVertex = 0;
        this.graphName = "";
    }

    public Graph(int numVertex) {
        this.numVertex = numVertex;
        graphNodes = new GraphNode[numVertex];
        for (int i = 0; i < numVertex; i++) {
            graphNodes[i] = new GraphNode( i );
        }
    }

    public boolean addEdge(int source, int destination) {
        if (source < 0 || source >= numVertex) return false;
        if (destination < 0 || destination >= numVertex) return false;
        //add edge
        graphNodes[source].addEdge( source, destination );
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "The Graph " + graphName + " \n" );

        for (int i = 0; i < numVertex; i++) {
            sb.append( graphNodes[i].toString() );
        }
        return sb.toString();
    }

    public void makeGraph(String filename) {
        try {
            graphName = filename;
            Scanner reader = new Scanner( new File( filename ) );
            System.out.println( "\n" + filename );
            numVertex = reader.nextInt();
            graphNodes = new GraphNode[numVertex];
            for (int i = 0; i < numVertex; i++) {
                graphNodes[i] = new GraphNode( i );
            }
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                graphNodes[v1].addEdge( v1, v2 );
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void clearAllPred() {
        for (int i = 0; i < numVertex; i++) {
            graphNodes[i].p1.clear();
            graphNodes[i].p2.clear();
        }
    }


    public String reportPath(StringBuilder path) {

        return path.toString();
    }

    public int findDistance(int n ){
        int totalDistance = 0;
        for (int i=0; i<this.graphNodes.length; i++){
            totalDistance += lca(n, i);
            System.out.println("lca("+n+","+i+") = " + lca(n,i));
        }
        System.out.println("lca(3,7) = " + lca(3,7));
        System.out.println("lca(5,6) = " + lca(5,6));
        System.out.println("lca(9,1) = " + lca(9,1));
        return totalDistance;
    }
    public int lca(int v1, int v2) {
        PathInfo best = new PathInfo( );
        ArrayList<EdgeInfo> v1Path = pathFinder(v1);
        ArrayList<EdgeInfo> v2Path = pathFinder(v2);
        int lca = -3;
        int distance = -1;
        boolean done = false;
        for (int i=0; i<v1Path.size(); i++){
            for (int j=0; j<v2Path.size(); j++){
                if (v1Path.get(i) == v2Path.get(j) && !done) {
                    lca = v1Path.get(i).from;
                    distance = i + j;
                    done = true;
                }
                else if (done){
                    v2Path.remove(j);
                }
                if (done){
                    v1Path.remove(i);}
            }
        }
        best.set(lca, distance);

        StringBuilder path = new StringBuilder();
        for (int i=0; i<v1Path.size(); i++){
            path.append(v1Path.get(i).from).append(" ");
        }
        for (int i=v2Path.size()-1; i>=0;i--){
            path.append(v2Path.get(i).from).append(" ");
        }

        clearAllPred();
        return best.dist;
    }



    /**
     * Computes the least common ancestor of v1 and v2, prints the length of the path, the ancestor, and the path itself.
     *
     * @param v1: first vertex
     * @param v2: second vertex
     * @return returns the length of the shortest ancestral path.
     */
    public int printLca(int v1, int v2) {
        PathInfo best = new PathInfo( );
        ArrayList<EdgeInfo> v1Path = pathFinder(v1);
        ArrayList<EdgeInfo> v2Path = pathFinder(v2);

        System.out.println("==================");
        for (EdgeInfo e : v1Path){
            System.out.print(e);
        }
        System.out.println("\n-------------------------");
        for(EdgeInfo e : v2Path){
            System.out.print(e);
        }
        System.out.println("\n=======================");



        int lca = -1;
        int distance = -1;
        boolean done = false;
        for (int i=0; i<v1Path.size(); i++){
            for (int j=0; j<v2Path.size(); j++){
                if (v1Path.get(i) == v2Path.get(j) && !done) {
                    lca = v1Path.get(i).from;
                    distance = i + j;
                    done = true;
                }
                else if(v1Path.get(i).to == 0 || v1 == 0 || v2 == 0){
                    lca = 0;
                    distance = i + j + 2;
                    System.out.println("j = " + j);
                    System.out.println("i = " + i);
                    done = true;
                }
                else if (done){
                    v2Path.remove(j);
                }
                if (done){
                v1Path.remove(i);}
            }
        }

        StringBuilder path = new StringBuilder();
        for (int i=0; i<v1Path.size(); i++){
            path.append(v1Path.get(i).from).append(" ");
        }
        for (int i=v2Path.size()-1; i>=0;i--){
            path.append(v2Path.get(i).from).append(" ");
        }
        best.set(lca, distance);
        System.out.println( graphName + " Best lca of " + v1 + " and " + v2 + " Distance: " + best.dist + " Ancestor " + best.pred + " Path:" + reportPath( path) );

        clearAllPred();
        return best.dist;
    }

    public ArrayList<EdgeInfo> pathFinder(int v){
        ArrayList<EdgeInfo> path = new ArrayList<>();
        if (graphNodes[v].succ.size() > 0){
            path.add(graphNodes[v].succ.get(0));
            path.addAll(pathFinder(graphNodes[v].succ.get(0).to));
        }

        return path;
    }

    public int outcast(int[] v) {
        int[] distances = new int[v.length];
        int outcast = 0;
        for (int i=0; i<v.length; i++){
            distances[i] = findDistance(v[i]);
        }
        for (int i=0; i<distances.length; i++){
            if (distances[i] > outcast){
                outcast = v[i];
            }
        }
        System.out.println(Arrays.toString(distances));


        System.out.println( "The outcast of " + Arrays.toString( v ) + " is " + outcast );
        return outcast;
    }

    public static void main(String[] args) {
        Graph graph1 = new Graph();
        graph1.makeGraph( "digraph1.txt" );
//        System.out.println(graph1.toString());
        int[] set1 = {7, 10, 2};
        int[] set2 = {7, 17, 5, 11, 4, 23};
        int[] set3 = {10, 17, 13};

        graph1.printLca( 3,0 );
//        graph1.printLca( 5, 6 );
//        graph1.printLca( 9, 1 );
//        graph1.outcast( set1 );
//
//        Graph graph2 = new Graph();
//        graph2.makeGraph( "digraph2.txt" );
//        //System.out.println(graph2.toString());
//        graph2.lca( 3, 24 );
//
//        graph2.outcast( set3 );
//        graph2.outcast( set2 );
//        graph2.outcast( set1 );
    }
}