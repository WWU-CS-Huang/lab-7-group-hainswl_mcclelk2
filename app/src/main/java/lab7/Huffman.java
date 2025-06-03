package lab7;
/*
 * Authors: Lawson Hainsworth & Koben M
 * Date: 5/5/2025
 * Purpose: COMPLETE!
 */
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;


public class Huffman {

    public static Map<Character, Integer> map = new HashMap<>(); //'map' is a class-wide variable

    /*
     * COMPLETE SPEC!
     */
    public static void main(String[] args) {
        String fileName = args[0];
        File file = new File(fileName);
        Scanner scan;
        try{
            scan = new Scanner(file);
        }   
        catch (FileNotFoundException e){
            return;
        }
        // map = new HashMap<>();
        countFrequencies(scan.toString());
        buildHuffmanTree();

    }

    /* Counts the frequency of each character in the input string and updates the frequency map.
     * Pre: Input is non-null
     */
    public static void countFrequencies(String input){
        for(int i = 0; i < input.length(); i++){
            Character c = Character.valueOf(input.charAt(i)); //convert char to Character
            map.put(c, (map.getOrDefault(c, 0) + 1)); //Default to 0 if key is not in map.
        }
    }

    /* Constructs a Huffman tree using the frequencies stored in the map, and returns the root node (using PriorityQueue).
     * Pre: map is not empty
     */
    public static Node buildHuffmanTree(){
        //Intiate a priority queue to build the huffman tree.
        PriorityQueue<Node> treeBuilder = new PriorityQueue<Node>();

        //Iterate through map entries, simultaneously construct the leaf nodes and add those to the priority queue.
        for(Character curCharacter : map.keySet()){ //For every key in map's key set...
            Integer curFrequency = map.get(curCharacter);
            Node curNode = new Node(curFrequency, curCharacter);
            treeBuilder.add(curNode);
        }

        //While the priority queue, treeBuilder, has more than 1 node, build the huuffman tree.
        while(treeBuilder.size() > 1){
            //Since it is a min priority queue, pull out the first two nodes in treeBuilder to combine.
            Node firstNode = treeBuilder.poll();
            Node secondNode = treeBuilder.poll();

            //New frequency is equal to the sum of the two lowest frequencies.
            Integer newFrequency = firstNode.frequency + secondNode.frequency;

            //Create a new interal node with that new frequency (w/ Character = null), with those nodes as it's children.
            Node newParent = new Node(newFrequency, firstNode, secondNode);

            //Add parent back to treeBuilder.
            treeBuilder.add(newParent);
        }
        return (treeBuilder.poll()); //extract and return the final node in treeBuilder (which is now the root of the huffman tree).
    }

    /*
     * COMPLETE SPEC!
     */
    public static class Node implements Comparable<Node>{ //Since the 'add' method in PriorityQueue uses comparisons, must specify the field by which to compare nodes by
        //Fields for node.
        int frequency;
        Character character;
        Node left;
        Node right;

        //Constructor for leaf nodes
        Node(int frequency, Character character){
            this.left = null; //leaf node, so set node's children to null.
            this.right = null;
            this.frequency = frequency;
            this.character = character;
        }

        //Constructor for internal nodes
        Node(int frequency, Node left, Node right){
            this.left = left;
            this.right = right;
            this.frequency = frequency;
            this.character = null; //Internal node, so set node's character field to null.
        }

        @Override //Must override the default compareTo method for Node, to base the comparisons between Nodes on frequency
        public int compareTo(Node otherNode) {
            return Integer.compare(this.frequency, otherNode.frequency); //(can use Integer.compare() to shorten up method)
        }
    }
}
