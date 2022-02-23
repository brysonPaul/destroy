import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

//Bryson Paul       2/02/2022       Arup Guha COP3503 Destroy Solution
public class destroy {
    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(r.readLine());
        djSet computers = new djSet(Integer.parseInt(st.nextToken()));
        connection[] currentConnections = new connection[Integer.parseInt(st.nextToken())];
        connection[] connectionsDestroyed = new connection[Integer.parseInt(st.nextToken())];
        //finalOutputs is a long to make sure our values cannot go over
        long[] finalOutputs = new long[connectionsDestroyed.length + 1];
        //takes in both integers for the connection line
        for (int x = 0; x < currentConnections.length; x++) {
            st = new StringTokenizer(r.readLine());
            currentConnections[x] = new connection(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        }
        //marks indices which will be destroyed
        for (int x = 0; x < connectionsDestroyed.length; x++) {
            int hold = Integer.parseInt(r.readLine()) - 1;
            connectionsDestroyed[x] = currentConnections[hold];
            currentConnections[hold] = null;
        }
        //adds rest of values to dj set, and disregards marked ones from the djSet
        for (int x = 0; x < currentConnections.length; x++) {
            if (currentConnections[x] != null) {
                computers.union(currentConnections[x]);
            }
        }
        //adds current severed version to our finaloutputs
        finalOutputs[finalOutputs.length - 1] = computers.findConnectivity();
        for (int x = connectionsDestroyed.length - 1; x >= 0; x--) {
            //starts adding connections from the back to the front, and then puts them into the array
            computers.union(connectionsDestroyed[x]);
            finalOutputs[x] = computers.findConnectivity();//adds it to end
        }
        //because everything was added from the back-up, I can loop through normally and print out everything in "order"
        for (int x = 0; x < finalOutputs.length; x++) {
            System.out.println(finalOutputs[x]);
        }

    }
}

class connection {
    int c1;
    int c2;

    public connection(int c1, int c2) {
        this.c1 = c1;
        this.c2 = c2;
    }
}

class djSet {
    int[] set;
    int[] size;
    HashSet<Integer> rootInd;

    public djSet(int x) {
        set = new int[x + 1];
        size = new int[x + 1];
        rootInd = new HashSet<>();
        //makes each set at least contain itself
        for (int i = 0; i < set.length; i++) {
            set[i] = i;
            size[i] = 1;
        }
        size[0] = 0;
    }

    //adds string of two sets to each other
    public void union(connection c) {
        //parses string into two sets
        int root1 = findSet(c.c1);
        int root2 = findSet(c.c2);

        if (root1 == root2) return;

        // Attach tree of v2 to tree of v1, then add the size of root2 into the size array of root1
        set[root2] = root1;
        size[(int) root1] += size[(int) root2];
        size[(int) root2] = 0;
        rootInd.add(root1);
        rootInd.remove((Object) root2);
    }

    //returns the root of the set working with, and does path compression on the way out for speed ups later
    public int findSet(int index) {
        //we found our root
        if (set[(int) index] == index) {
            return index;
        }
        //recursively finds the highest root, then does path compression to make sure everything else is also apart of the set
        int hold = findSet(set[(int) index]);
        set[index] = hold;
        return hold;
    }

    //finds the connectivity by going through the array and groups similar roots together. At end squares each similar root total and sums it
    public long findConnectivity() {
        long sum = 0;
        //I make the total amount of this the size-1, which is equal to the amount of actual nodes the set can work with
        long tot = size.length - 1;
        Iterator I = rootInd.iterator();
        while (I.hasNext()) {
            long hold = size[(int) I.next()];
            sum += (hold * hold);
            tot -= hold;
        }
        //this works because the total will hold all the size 1 arrays, and 1^2 = 1
        return (sum + tot);
    }

    //prints out the set (testing purposes)
    public void printSet() {
        for (int x = 0; x < set.length; x++) {
            System.out.println("SET # " + x + ": " + set[x]);
        }
    }
}