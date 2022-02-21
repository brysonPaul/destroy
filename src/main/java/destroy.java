import java.util.Scanner;
//Bryson Paul       2/02/2022       Arup Guha COP3503 Destroy Solution
public class destroy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        djSet computers = new djSet(sc.nextInt());
        String[] currentConnections = new String[sc.nextInt()];
        int[] indexesOfConnectionsDestroyed = new int[sc.nextInt()];
        //finalOutputs is a long to make sure our values cannot go over
        long[] finalOutputs = new long[indexesOfConnectionsDestroyed.length + 1];
        //takes in both integers for the connection line
        for (int x = 0; x < currentConnections.length; x++) {
            String hold = sc.next() + " " + sc.next();
            currentConnections[x] = hold;
        }
        //marks indices which will be destroyed
        for (int x = 0; x < indexesOfConnectionsDestroyed.length; x++) {
            int hold = sc.nextInt() - 1;
            indexesOfConnectionsDestroyed[x] = hold;
        }
        //adds rest of values to dj set, and disregards marked ones from the djSet
        for (int x = 0; x < currentConnections.length; x++) {
            int hold = contains(x, indexesOfConnectionsDestroyed);
            if (hold == -1) {
                computers.union(currentConnections[x]);
            }
        }
        //adds current severed version to our finaloutputs
        finalOutputs[finalOutputs.length - 1] = computers.findConnectivity();
        for (int x = indexesOfConnectionsDestroyed.length - 1; x >= 0; x--) {
            //starts adding connections from the back to the front, and then puts them into the array
            computers.union(currentConnections[indexesOfConnectionsDestroyed[x]]);
            finalOutputs[x] = computers.findConnectivity();//adds it to end
        }
        //because everything was added from the back-up, I can loop through normally and print out everything in "order"
        for (int x = 0; x < finalOutputs.length; x++) {
            System.out.println(finalOutputs[x]);
        }

    }

    //sees if the value is in the arr
    public static int contains(int value, int[] arr) {
        for (int x = 0; x < arr.length; x++) {
            if (value == arr[x]) {
                return x;
            }
        }
        return -1;
    }
}

class djSet {
    long[] set;

    public djSet(int x) {
        set = new long[x + 1];
        //makes each set at least contain itself
        for (long i = 0; i < set.length; i++) {
            set[(int)i] = i;
        }
    }

    //adds string of two sets to each other
    public void union(String s) {
        //parses string into two sets
        String[] sets = s.split(" ");
        int set1 = Integer.parseInt(sets[0]);
        int set2 = Integer.parseInt(sets[1]);
        long root1 = findSet(set1);
        long root2 = findSet(set2);

        if (root1 == root2) return;

        // Attach tree of v2 to tree of v1.
        set[(int) root2] = root1;
    }

    //returns the root of the set working with, and does path compression on the way out for speed ups later
    public long findSet(long index) {
        //we found our root
        if (set[(int) index] == index) {
            return index;
        }
        //recursively finds the highest root, then does path compression to make sure everything else is also apart of the set
        long hold = findSet(set[(int) index]);
        set[(int) index] = hold;
        return hold;
    }

    //finds the connectivity by going through the array and groups similar roots together. At end squares each similar root total and sums it
    public long findConnectivity() {
        long sum = 0;
        long[] arr = new long[set.length];
        //we have to start at one because we technically make a zero'th set, thus offsetting each number by one
        for (int x = 1; x < arr.length; x++) {
            arr[(int)findSet(set[x])] += 1;
        }
        for (int x = 0; x < arr.length; x++) {
            if (arr[x] == 0) {
                continue;
            }
            sum += arr[x] * arr[x];
        }
        return sum;
    }

    //prints out the set (testing purposes)
    public void printSet() {
        for (int x = 0; x < set.length; x++) {
            System.out.println("SET # " + x + ": " + set[x]);
        }
    }
}