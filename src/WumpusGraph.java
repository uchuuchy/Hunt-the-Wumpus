import java.util.Random;

public class WumpusGraph {
    // Helper Classes
    public static class Vertex {
        // Helper Class
        public static class Wumpus {
            private boolean hasWumpus;
            private boolean awake;

            public Wumpus(){
                this.hasWumpus = false;
                this.awake = false;
            }

            public boolean isHasWumpus() {
                return hasWumpus;
            }

            public void setHasWumpus(boolean hasWumpus) {
                this.hasWumpus = hasWumpus;
            }

            public boolean isAwake() {
                return awake;
            }

            public void setAwake(boolean awake) {
                this.awake = awake;
            }
        }

        // Vertex Start
        private final int v;
        public Wumpus wump;
        private boolean hasPit;
        private boolean hasBats;

        public Vertex(int v){
            this.v = v;
            wump = new Wumpus();
            hasPit = false;
            hasBats = false;
        }

        public int getV() {
            return v;
        }

        public boolean isHasPit() {
            return hasPit;
        }

        public boolean isHasBats() {
            return hasBats;
        }

        public void setHasPit(boolean hasPit) {
            this.hasPit = hasPit;
        }

        public void setHasBats(boolean hasBats) {
            this.hasBats = hasBats;
        }
    }

    public static class Edge {
        private final int src;
        private final int dest;

        public Edge(int src, int dest){
            this.src = src;
            this.dest = dest;
        }

        public int getSrc() {
            return src;
        }

        public int getDest() {
            return dest;
        }
    }

    // Wumpus Graph Start

    private final Vertex[] vertices = new Vertex[20];
    private final Edge[] edges = new Edge[60];

    public WumpusGraph(){
        for(int i = 1; i <= 20; i++)
            vertices[i-1] = new Vertex(i);
        insertEdges();
        randomizeMap();
    }

    private void insertEdges(){
        int j = 0;
        for(int i = 2; i <= 19; i++){ // Runs 17 times
            edges[j] = new Edge(i, i + 1);
            edges[j+1] = new Edge(i, i - 1);
            j += 2;
        } // Next available slot: edges[36]

        edges[36] = new Edge(1, 2);
        edges[37] = new Edge(1, 5);
        edges[38] = new Edge(1, 8);
        edges[39] = new Edge(2, 10);
        edges[40] = new Edge(3, 12);
        edges[41] = new Edge(4, 14);
        edges[42] = new Edge(5, 1);
        edges[43] = new Edge(6, 15);
        edges[44] = new Edge(7, 17);
        edges[45] = new Edge(8, 1);
        edges[46] = new Edge(9, 18);
        edges[47] = new Edge(10, 2);
        edges[48] = new Edge(11, 19);
        edges[49] = new Edge(12, 3);
        edges[50] = new Edge(13, 20);
        edges[51] = new Edge(14, 4);
        edges[52] = new Edge(15, 6);
        edges[53] = new Edge(16, 20);
        edges[54] = new Edge(17, 7);
        edges[55] = new Edge(18, 9);
        edges[56] = new Edge(19, 11);
        edges[57] = new Edge(20, 13);
        edges[58] = new Edge(20, 16);
        edges[59] = new Edge(20, 19);
    }

    private void randomizeMap(){
        Random random = new Random(System.currentTimeMillis());
        int r = random.nextInt(20 - 1) + 1;
        vertices[r].wump.setHasWumpus(true);
        r = random.nextInt(20 - 1) + 1;
        vertices[r].setHasPit(true);
        r = random.nextInt(20 - 1) + 1;
        vertices[r].setHasPit(true);
        r = random.nextInt(20 - 1) + 1;
        vertices[r].setHasBats(true);
        r = random.nextInt(20 - 1) + 1;
        vertices[r].setHasBats(true);
    }

    public Vertex[] getNeighbors(int src){
        Vertex[] neighbors = new Vertex[3];
        int j = 0;
        for(int i = 0; i < 60; i++){
            if(j == 3)
                break;
            if(src == edges[i].getSrc()){
                neighbors[j] = vertices[edges[i].getDest() - 1];
                j++;
            }
        }
        return neighbors;
    }

    public Vertex getVertex(int src){
        if(src > 20)
            return new Vertex(-1);
        return vertices[src-1];
    }

    public Vertex findWumpus(){
        for(Vertex v : vertices)
            if(v.wump.isHasWumpus())
                return v;
        return null;
    }

    public void moveWumpus(){
        Vertex wumpHome = findWumpus();
        if(!wumpHome.wump.isAwake())
            return;
        Vertex[] neighbors = getNeighbors(wumpHome.getV());
        Random random = new Random(System.currentTimeMillis());
        int r = random.nextInt(2);
        wumpHome.wump.setHasWumpus(false);
        wumpHome.wump.setAwake(false);
        neighbors[r].wump.setHasWumpus(true);
        neighbors[r].wump.setAwake(true);
    }

    public void awakenWumpus(){
        Random random = new Random(System.currentTimeMillis());
        double r = random.nextDouble(1.00 - 0.01) + 0.01;
        Vertex wumpHome = findWumpus();
        if(wumpHome.wump.isAwake())
            return;
        wumpHome.wump.setAwake(true);
        if(r <= 0.25)
            return;
        moveWumpus();
    }
}
