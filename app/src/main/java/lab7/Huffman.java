package lab7;
/*
 * Authors: Lawson Hainsworth & Koben M
 * Date: 5/5/2025
 * Purpose: Implements a Huffman code tree to compress and decompress text. Main method first reads an 
 * input string from a file, builds a freqeuncy-based huffman tree, generates huffman codes for each character,
 * encodes the input text into a bitstring, and then decodes that to verify it works properly. 
 */

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Huffman {

    public HashMap<Character, Integer> map = new HashMap<>(); //'map' (for frequency map) is a class-wide variable
    public Node root = null; //root Node of huffman tree. Default null, until buildHuffmanTree is called

    /* Reads input from a file, builds a Huffman tree, encodes and decodes the input string, and prints results.
     * Pre: (args.length > 0), and args[0] exists and is readable.
     */
    public static void main(String[] args) {
        //get file from first command line arg
        String fileName = args[0];
        File file = new File(fileName);
        Scanner scan;
        try{
            scan = new Scanner(file);
        }   
        catch (FileNotFoundException e){
            System.out.println("File not found.");
            return;
        }
        
        //get input string (may need to check that scan isn't null);
        String inputString = "";
        while(scan.hasNextLine()){
            inputString = inputString + scan.nextLine();
        }

        scan.close();

        //Create a new huffman object.
        Huffman huffmanTree = new Huffman();

        //Create frequency hashMap to store characters and frequency
        huffmanTree.countFrequencies(inputString);

        //Build the huffman tree.
        huffmanTree.buildHuffmanTree();

        //get encoded inputString, then decode the encoded string.
        String encodedString = huffmanTree.encodeString(inputString);
        String decodedString = huffmanTree.decodeBitString(encodedString);
        //When the input string is less than 100 characters...
        if(inputString.length() < 100){
            //print original input string
            System.out.println("Input string: " + inputString);

            //print the encoded bitstring

            System.out.println("Encoded string: " + encodedString);

            //print the result of decoding the encoded bitstring
            System.out.println("Decoded string: " + decodedString);
        }

        //print a boolean that confirms decoded equals input
        System.out.println("Decoded equals input: " + decodedString.equals(inputString));


        //get, and then print the compression ratio
        double compressionRatio = ((double) encodedString.length() / (double) inputString.length() / 8.0);
        System.out.println("Compression ratio: " + compressionRatio);

    }

    /* Counts the frequency of each character in the input string and builds the frequency map.
     * Pre: (Input != null).
     */
    public void countFrequencies(String input){
        for(int i = 0; i < input.length(); i++){
            Character c = Character.valueOf(input.charAt(i)); //convert char to Character
            map.put(c, (map.getOrDefault(c, 0) + 1)); //Default to 0 if key is not in map.
        }
    }

    /* Constructs a Huffman tree given an input string, and then sets the root of the tree.
     * Pre: (Input.lenghth() > 0).
     */
    public void buildHuffmanTree(){
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
        root = (treeBuilder.poll()); //extract and set the final node in treeBuilder to the root (which is now the root of the huffman tree).
    }

    /*  Creates a map, with every character in the huffman tree as the keys, and their bitstring 'paths' as their values
     *  Pre: (root != null).
     */
    public HashMap<Character, String> huffmanCodeMap(Node root){
        HashMap<Character, String> codeMap = new HashMap<>();

        //recursively build the codeMap
        buildHuffmanCodeMap(root, "", codeMap);
        return codeMap;
    }

    /* Does the recursive building of the code map for the huffmanCodeMap method.
     * Pre: (curNode != null), and (codeMap != null).
     */
    private void buildHuffmanCodeMap(Node curNode, String path, Map<Character, String> codeMap){
        //leaf is node (Base Case)
        if(isLeafNode(curNode)){
            if(path.equals("")){ //if path is empty
                codeMap.put(curNode.character, "0"); //In the case that the root node is a leaf, set it's huffman code to 0 in the map.
            } else {
                codeMap.put(curNode.character, path);//put path value in map with node's character as the key.
            }
            return;
        }

        //otherwise, recursively call on the left and right children of curNode.
        if(curNode.left != null){ //Add 0 to path when going left in tree
            buildHuffmanCodeMap(curNode.left, path + "0", codeMap);
        }
        if(curNode.right != null) { //Add 1 to path, since going right in tree.
            buildHuffmanCodeMap(curNode.right, path + "1", codeMap);
        }
    }


    /* Encodes the input string into a Huffman-encoded bitstring using the built tree.
     * (OriginalString != null), (root != null), and buildHuffmanTree() must be called.
     */
    public String encodeString(String originalString){
        //Use string builder for efficency!
        StringBuilder bitString = new StringBuilder();

        //For encode, build a map;
        HashMap<Character, String> codeMap = huffmanCodeMap(root);

        //Traverse through huffman tree, create a bitString of the input using the huffmanCodeMap
        for(int i = 0; i < originalString.length(); i++){ //for every character in original string...
            bitString.append(codeMap.get(originalString.charAt(i))); //convert the current Char to it's huffman code, and append it to the bitstring.
        }
        
        return bitString.toString();
    }

    /* (use tree traversal for decode, not map)
     * Pre: (bitString != null), (root != null).
     */
    public String decodeBitString(String bitString){
        String decodedString = "";
        Node curNode = root;
        for(int i = 0; i < bitString.length(); i++){
            if(isLeafNode(curNode)){
                decodedString += curNode.character;
                curNode = root;
            }
            if(bitString.charAt(i) == 1){
                curNode = curNode.right;
            }
            else{
                curNode = curNode.left;
            }
        }
        
        return decodedString;
    }


    /* Returns true if given node is leaf, and false if otherwise.
     * Pre: (node != null).
     */
    private boolean isLeafNode(Node node){
        if((node.left == null) && (node.right == null) && (node.character != null)){
            return true;
        } else {
            return false;
        }
    }

    /* Represents a node in the Huffman tree. Implements Comparable<Node> for Priority Queue, based on frequency.
     * Has a constructor for leaf nodes, and internal nodes.
     * Pre: Both Nodes in compareTo must be non-null.
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

        @Override //Must override the default compareTo method for Node, so comparisons can be made between Nodes based on their frequencies
        public int compareTo(Node otherNode) {
            return Integer.compare(this.frequency, otherNode.frequency); //(can use Integer.compare() to shorten up method)
        }
    }
}
