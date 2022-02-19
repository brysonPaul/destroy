package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

//Bryson Paul       2/02/2022       Arup Guha COP3503 Destroy Solution
//NOTES TO SELF: MAKE A NODE CLASS W TWO INTS TO SPEED UP STRING WORK :))
//this code currently doesnt work, but thats chill. Error happening at contains, so work on that lol
public class destroy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        djSet computers = new djSet(sc.nextInt());
        String[] currentConnections = new String[sc.nextInt()];
        String[] connectionsDestroyed = new String[sc.nextInt()];
        int[] indexesOfConnectionsDestroyed = new int[connectionsDestroyed.length];
        //finalOutputs is a long to make sure our values cannot go over
        long[] finalOutputs = new long[connectionsDestroyed.length+1];
        //takes in both integers for the connection line
        for (int x = 0; x < currentConnections.length; x++) {
            String hold = sc.next() + " " + sc.next();
            currentConnections[x]=hold;
        }
        //marks connections
        for (int x = 0; x < connectionsDestroyed.length; x++) {
            int hold = sc.nextInt() - 1;
            indexesOfConnectionsDestroyed[x] = hold;
        }
        //adds rest of values to dj set, and disregards marked ones from the djSet but puts them into connectionsDestroyed
        for (int x = 0; x < currentConnections.length; x++) {
            int hold = contains(x,indexesOfConnectionsDestroyed);
            if(hold==-1) {
                computers.union(currentConnections[x]);
            }
            else{
                connectionsDestroyed[hold]=currentConnections[x];
            }
        }
        //adds the current severed version of the connections to the back of the finalOutputs array
        finalOutputs[finalOutputs.length-1]=computers.findConnectivity();
        for (int x = connectionsDestroyed.length - 1; x >= 0; x--) {
            //starts adding from the back to simulate severing from the front
            computers.union(connectionsDestroyed[x]);
            finalOutputs[x]=computers.findConnectivity();//adds it to end
        }
        //because everything was added from the back-up, I can loop through normally and print out everything in "order"
        for(int x=0;x<finalOutputs.length;x++){
            System.out.println(finalOutputs[x]);
        }

    }
    public static int contains(int index, int[] arr){
        for(int x=0;x<arr.length;x++){
            if(index == arr[x]){
                return x;
            }
        }
        return -1;
    }
}

class djSet {
    int[] set;

    public djSet(int x) {
        set = new int[x+1];
        //makes each set at least contain itself
        for (int i = 0; i < set.length; i++) {
            set[i]=i;
        }
    }
    //adds string of two sets to each other
    public void union(String s) {
        //parses string into two sets
        String[] sets = s.split(" ");
        int set1 = Integer.parseInt(sets[0]);
        int set2 = Integer.parseInt(sets[1]);
        //our DJ set makes it so that the bigger of the two connects to the other one

        int hold = 0;
        if (set1 < set2) {
            hold = set[set2];
            if(hold!=set2){
                if(hold>set1){
                    set[set2]=set1;
                    union(hold+" "+findSet(set2));
                }
                else{
                    union(set1+ " "+findSet(set2));
                }
            }
            set[set2]=set1;
        } else {
            hold = set[set1];
            if(hold!=set1){
                if(hold>set2){
                    set[set1]=set2;
                    union(hold+" "+findSet(set1));
                }
                else{
                 union(set2+ " "+findSet(set1));
                }
            }
            set[set1]=set2;
        }
    }

    public int findSet(int index) {
        //we found our root
        if (set[index]==index) {
            return index;
        }
        //recursively finds the highest root, then does path compression to make sure everything else is also apart of the set
        int hold = findSet(set[index]);
        set[index]=hold;
        return hold;
    }
    //finds the connectivity by going through the array and groups similar roots together. At end squares each similar root total and sums it
    public long findConnectivity() {
        int sum=0;
        int[] arr = new int[set.length];
        //we have to start at one because we technically make a zero'th set, thus offsetting each number by one
       for(int x=1;x<arr.length;x++){
            arr[findSet(set[x])]+=1;
       }
       for(int x=0;x<arr.length;x++){
           if(arr[x]==0){
               continue;
           }
           sum+=arr[x]*arr[x];
       }
        return sum;
    }

    //prints out the set (testing purposes)
    public void printSet() {
        for (int x = 0; x < set.length; x++) {
            System.out.println("SET # " + x + ": "+set[x]);
        }
    }
}