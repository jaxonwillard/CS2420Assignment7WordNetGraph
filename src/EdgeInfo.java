public class EdgeInfo {

    public EdgeInfo(int from, int to){
        this.from = from;
        this.to = to;

    }
    public String toString(){
        return from + "->" + to + " " ;
    }

    int from;        // source of edge
    int to;          // destination of edge


}
