public class PathInfo {
    PathInfo() {
        clear();
    }

    public void set(int node, int dist) {
        this.pred = node;
        this.dist = dist;
    }

    public void clear() {
        this.pred = -1;
        this.dist = 99;
    }

    public String toString() {
        return "[" + dist + " Pred:" + pred + "] ";
    }

    int dist;
    int pred;
}
