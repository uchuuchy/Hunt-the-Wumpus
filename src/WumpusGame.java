import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class WumpusGame {
    // Helper Class(es)
    public static class Player {
        public int loc;
        private int arrows;

        public Player(int loc){
            this.loc = loc;
            this.arrows = 5;
        }

        public void move(int dest){
            if(isIn(dest, map.getNeighbors(loc)))
                loc = dest;
        }

        public void setLoc(int dest){
            loc = dest;
        }

        public void shoot(int[] dests) {
            Random random = new Random(System.currentTimeMillis());
            int cur = loc;
            for (int i = 0; i < dests.length; i++) {
                if (isIn(dests[i], map.getNeighbors(cur)))
                    cur = dests[i];
                else {
                    int r = random.nextInt(2);
                    cur = map.getNeighbors(cur)[r].getV();
                }

                if (map.getVertex(cur).wump.isHasWumpus()) {
                    System.out.println("AHA! YOU GOT THE WUMPUS!\nHEE HEE HEE - THE WUMPUS'LL GET YOU NEXT TIME\n");
                    endOfGame = true;
                } else if (cur == loc) {
                    System.out.println("OUCH! ARROW GOT YOU\nHA HA HA - YOU LOSE!\n");
                    endOfGame = true;
                }
            }
            arrows--;
            if (arrows <= 0) {
                System.out.println("MISSED\nHA HA HA - YOU LOSE!\n");
                endOfGame = true;
            }
            map.awakenWumpus();
        }

        public void viewSurrounding(){
            WumpusGraph.Vertex[] neighbors = map.getNeighbors(loc);
            for(WumpusGraph.Vertex v : neighbors){
                if(v.wump.isHasWumpus())
                    System.out.println("I SMELL A WUMPUS");
                else if(v.isHasBats())
                    System.out.println("BATS NEARBY");
                else if(v.isHasPit())
                    System.out.println("I FEEL A DRAFT");
            }
        }

        public void printLocation(){
            System.out.println("YOU ARE IN TUNNEL "+loc);
            WumpusGraph.Vertex[] neighbors = map.getNeighbors(loc);
            System.out.print("TUNNELS LEAD TO ");
            for(int i = 0; i < 2; i++)
                System.out.print(neighbors[i].getV()+", ");
            System.out.println("AND "+neighbors[2].getV());
        }

        public void checkHazards(){
            WumpusGraph.Vertex v = map.getVertex(loc);
            if(v.wump.isHasWumpus()){
                if(v.wump.isAwake()){
                    System.out.println("TSK TSK TSK- WUMPUS GOT YOU!");
                    endOfGame = true;
                } else {
                    System.out.println("...OOPS! BUMPED A WUMPUS!");
                    map.awakenWumpus();
                }
            }
            else if(v.isHasBats()){
                Random random = new Random(System.currentTimeMillis());
                int r = random.nextInt(20 - 1) + 1;
                System.out.println("ZAP-- SUPER BAT SNATCH!\nELSEWHEREVILLE FOR YOU!");
                you.setLoc(r);
                you.checkHazards();
            }
            else if(v.isHasPit()){
                System.out.println("YYYIIIIEEEE . . . FELL IN PIT\nHA HA HA - YOU LOSE!");
                endOfGame = true;
            }
        }
    }

    // Wumpus Game Start
    // Variables
    private static final WumpusGraph map = new WumpusGraph();
    private static Player you;
    private static boolean endOfGame = false;

    // Functions
    public static boolean isIn(int x, WumpusGraph.Vertex[] arr){
        for (WumpusGraph.Vertex j : arr) {
            if (x == j.getV())
                return true;
        }
        return false;
    }

    public static void playIntro() {
        String intro = "WELCOME TO 'HUNT THE WUMPUS'\nTHE WUMPUS LIVES IN A CAVE OF 20 ROOMS.\nEACH ROOM HAS 3 TUNNELS LEADING TO OTHER ROOMS.\n(LOOK AT A DODECAHEDRON TO SEE HOW THIS WORKS-\nIF YOU DON'T KNOW WHAT A DODECHADRON IS, ASK SOMEONE)\n\nHAZARDS:\nBOTTOMLESS PITS - TWO ROOMS HAVE BOTTOMLESS PITS IN THEM.\nIF YOU GO THERE, YOU FALL INTO THE PIT (& LOSE!)\nSUPER BATS - TWO OTHER ROOMS HAVE SUPER BATS.\nIF YOU GO THERE, A BAT GRABS YOU AND TAKES YOU\nTO SOME OTHER ROOM AT RANDOM (WHICH MIGHT BE TROUBLESOME)\nWUMPUS: THE WUMPIS IS NOT BOTHERED BY THE\nHAZARDS (HE HAS SUCKER FEET AND IS\nTOO BIG FOR A BAT TO LIFT)\nUSUALLY HE IS ASLEEP. TWO THINGS THAT WAKE HIM UP:\nYOUR ENTERING HIS ROOM OR YOUR SHOOTING AN ARROW.\nIF THE WUMPUS WAKES, HE MOVES (P=.75) ONE ROOM\nOR STAYS STILL (P=.25).\nAFTER THAT, IF HE IS WHERE YOU ARE,\nHE EATS YOU UP (& YOU LOSE!)\n\nYOU:\nEACH TURN YOU MAY MOVE OR SHOOT A CROOKED ARROW\nMOVING: YOU CAN GO ONE ROOM (THRU ONE TUNNEL)\nARROWS: YOU HAVE 5 ARROWS. YOU LOSE WHEN YOU RUN OUT.\nEACH ARROW CAN GO FROM 1 TO 5 ROOMS.\nYOU AIM BY TELLING THE COMPUTER THE ROOMS YOU WANT THE ARROW TO GO TO.\nIF THE ARROW CAN'T GO THAT WAY (IE NO TUNNEL)\nIT MOVES AT RANDOM TO THE NEXT ROOM.\nIF THE ARROW HITS THE WUMPUS, YOU WIN.\nIF THE ARROW HITS YOU, YOU LOSE.\n\nWARNINGS:\nWHEN YOU ARE ONE ROOM AWAY FROM WUMPUS OR HARZARD,\nTHE COMPUTER SAYS:\nWUMPUS-\t'I SMELL A WUMPUS'\nBAT   -\t'BATS NEARBY'\nPIT   -\t'I FEEL A DRAFT'\nHUNT THE WUMPUS\n";
        System.out.println(intro);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(15);
        Scanner read = new Scanner(System.in);
        Random random = new Random(System.currentTimeMillis());
        int r = random.nextInt(20 - 1) + 1;
        you = new Player(r);
        playIntro();
        while(!endOfGame){
            String choice = "\0";
            int stops = -1;

            you.viewSurrounding();
            you.printLocation();

            while((!Objects.equals(choice, "M")) && (!Objects.equals(choice, "S"))) {
                System.out.print("\nSHOOT OR MOVE (S-M)? ");
                choice = read.next();
            }
            switch(choice){
                case "S":
                    while(0 >= stops || stops > 5){
                        System.out.print("NO. OF ROOMS(1-5)? ");
                        stops = read.nextInt();
                    }
                    int[] dests = new int[stops];
                    for(int i = 0; i < stops; i++){
                        System.out.print("ROOM #? ");
                        dests[i] = read.nextInt();
                    }
                    you.shoot(dests);
                    break;
                case "M":
                    int dest = -1;
                    while(!isIn(dest, map.getNeighbors(you.loc))){
                        System.out.print("WHERE TO? ");
                        dest = read.nextInt();
                    }
                    you.move(dest);
                    break;
                default:
                    break;
            }
            if(endOfGame)
                break;
            if(map.findWumpus().wump.isAwake()){
                map.moveWumpus();
            }
            you.checkHazards();
        }
    }
}