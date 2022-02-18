package org.example;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

//Bryson Paul       2/02/2022       Arup Guha COP3503 Destroy Solution
public class destroy
{
    public static void main( String[] args ) {
        Scanner sc = new Scanner(System.in);

        djSet computers = new djSet(sc.nextInt());
        int numberOfConnections = sc.nextInt();
        String[] connectionsDestroyed = new String[sc.nextInt()];
        //finalOutputs is a long to make sure our values cannot go over
        ArrayList<String> currentConnections = new ArrayList<>();
        ArrayList<Long> finalOutputs = new ArrayList<>();
        //takes in both integers for the connection line
        for(int x=0;x<numberOfConnections;x++){
            String hold = sc.next()+" "+ sc.next();
            currentConnections.add(hold);
        }
        //takes out the connections that have been marked as destroyed and places them into a new array
        for(int x=0;x<connectionsDestroyed.length;x++){
            int hold = sc.nextInt()-1;
            connectionsDestroyed[x]=currentConnections.get(hold);
        }
        //deletes each connection added to connectionsDestroyed, as they were already added
        for(int x=0;x<connectionsDestroyed.length;x++){
            currentConnections.remove(connectionsDestroyed[x]);
        }
        //adds rest of values to dj set
        for(int x=0;x<currentConnections.size();x++){
            computers.addToDJSet(currentConnections.get(x));
        }
        //shows current state of the array missing the conections
        finalOutputs.add(computers.findConnectivity());
        for(int x=connectionsDestroyed.length-1;x>=0;x--){
            //starts adding from the back to simulate severing from the front
            computers.addToDJSet(connectionsDestroyed[x]);
            finalOutputs.add(computers.findConnectivity());//adds it to end
        }
        //prints out the final amount of things from the back forward to simulate "severing" each connection
        while(finalOutputs.size()>0){
            System.out.println(finalOutputs.remove(finalOutputs.size()-1));
        }

    }
}
class djSet {
    ArrayList<Integer>[] set;
    public djSet(int x){
        this.set = new ArrayList[x+1];
        //makes each set at least contain itself
        for(int i=0;i<set.length;i++){
            set[i]= new ArrayList<Integer>();
            set[i].add(i);
        }
    }
    //adds string of two sets to each other
    public void addToDJSet(String s){
        //parses string into two sets
        int set1 = Integer.parseInt(s.substring(0,1));
        int set2 = Integer.parseInt(s.substring(2,3));
        //our DJ set makes it so that the bigger of the two connects to the other one
        if(set1 < set2){
            //done so to make sure it is not a root anymore
            if(set[set2].contains(set2)){
                set[set2].remove((Object)set2);
            }
            //if its already contained, no need to do it twice
            if(set[set2].contains(set1)) return;
            set[set2].add(set1);
        }
        else{
            //done so to make sure it is not a root anymore
            if(set[set1].contains(set1)){
                set[set1].remove((Object)set1);
            }
            //if its already contained, no need to do it twice
            if(set[set1].contains(set2)) return;
            set[set1].add(set2);
        }
    }
    public int findSet(int index, ArrayList<Integer> used){
        ArrayList<Integer> roots = new ArrayList<>();
        //we found our root
        if(set[index].contains(index)){
            return index;
        }
        //check to see if it has been used yet to make sure I am not going between a circular set for eons
        for(Integer num:set[index]){
            if(!used.contains(num)){
                used.add(num);
                roots.add(findSet(num,used));
                used.remove((Object)num);
                if(roots.contains(-1)){
                    return -1;
                }
            }
        }
        //removes any duplicates which may occur
        removeDuplicates(roots);
        if(roots.size()==0 || roots==null){
            return -1;
        }
        if(roots.size()==1){
            return roots.get(0);
        }
        else{
            Collections.sort(roots);
            for(int x=1;x<roots.size();x++){
                String s= roots.get(0)+" "+roots.get(x);
                addToDJSet(s);
            }
            return -1;
        }

    }

    void removeDuplicates(ArrayList<Integer> roots) {
        for(int x=0;x<roots.size()-1;x++){
            for(int y=x+1;y<roots.size();y++){
                if(roots.get(x)==roots.get(y)){
                    roots.remove(y);
                    x=0;
                }
            }
        }
    }
    //finds the connectivity by going through the array and groups similar roots together. At end squares each similar root total and sums it
    public long findConnectivity(){
        //we start at set 1 because set 0 is there to preserve sanity when reading
        HashMap<Integer,Integer> hmap= new HashMap<>();
        for(int x=1;x<set.length;x++){
            int hold = findSet(x,new ArrayList<Integer>());
            //hold will be negative 1 in a case where x has more than one root it can go to. On the next run through however, x should be facing a single root
            if(hold==-1){
                x=0;
                hmap.clear();
                continue;
            }
            //if the hmap already has one of similar root, we simply add 1 to the existing one. If not, we add the root to the hashmap
            if(hmap.containsKey(hold)){
                hmap.put(hold,hmap.get(hold)+1);
            }
            else hmap.put(hold,1);
        }
        int sum=0;
        //adds the sum of squares for each spot of the hashmap
        for(int x=1;x<set.length;x++){
            if(hmap.get(x)==null || hmap.get(x)==0) continue;
            int amountOfOccurrences=hmap.get(x);
            sum+= amountOfOccurrences*amountOfOccurrences;
        }
        return sum;
    }
    //prints out the set (testing purposes)
    public void printSet(){
        for(int x =0;x<set.length;x++){
            System.out.print("SET # "+x+":");
            for(int y=0;y<set[x].size();y++){
                System.out.print(set[x].get(y)+" ");
            }
            System.out.println();
        }
    }
}