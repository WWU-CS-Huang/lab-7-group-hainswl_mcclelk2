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

    public HashMap<Character, Integer> map = new HashMap<>(); //'map' (for frequency map) is a class-wide variable
    public Node root = null; //root Node of huffman tree. Default null, until buildHuffmanTree is called

    /*
     * COMPLETE SPEC!
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

        //Build the huffman tree.
        huffmanTree.buildHuffmanTree(inputString);

        //When the input string is less than 100 characters...
        if(inputString.length() < 100){
            //print original input string
            System.out.println("Input string: " + inputString);

            //print the encoded bitstring
            String encodedString = huffmanTree.encodeString(inputString);
            System.out.println("Encoded string: " + encodedString);

            //print the result of decoding the encoded bitstring
            System.out.println("Decoded string: " + huffmanTree.decodeBitString(encodedString));
        }

        //print a boolean that confirms decoded equals encoded

        //print the compression ratio

    }

    /* Counts the frequency of each character in the input string and updates the frequency map.
     * Pre: Input is non-null
     */
    public void countFrequencies(String input){
        for(int i = 0; i < input.length(); i++){
            Character c = Character.valueOf(input.charAt(i)); //convert char to Character
            map.put(c, (map.getOrDefault(c, 0) + 1)); //Default to 0 if key is not in map.
        }
    }

    /* Constructs a Huffman tree by first calling countFrequenices, before 
     * building the huffman tree with a priority queue. Also sets the variable 'root' to the root of the 
     * resulting huffman tree.
     * Pre: Input string non-empty
     */
    public void buildHuffmanTree(String input){
        //first, call count frequencies.
        countFrequencies(input);

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
     *  Pre:
     */
    public HashMap<Character, String> huffmanCodeMap(Node root){
        HashMap<Character, String> codeMap = new HashMap<>();

        //recursively build the codeMap
        buildHuffmanCodeMap(root, "", codeMap);
        return codeMap;
    }

    /* Does the recursive building of the code map for the huffmanCodeMap method.
     * Pre: curNode != null
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


    /* (use map of <character, huffmanCode> for encode)
     * 
     */
    public String encodeString(String originalString){
        String bitString = "";

        //For encode, build a map;
        HashMap<Character, String> codeMap = huffmanCodeMap(root);


        //Traverse through huffman tree, create a bitString of the input using the huffmanCodeMap
        for(int i = 0; i < originalString.length(); i++){ //for every character in original string...
            bitString = bitString + codeMap.get(originalString.charAt(i)); //convert the current Char to it's huffman code, and append it to the bitstring.
        }
        return bitString;
    }

    /* (use tree traversal for decode, not map)
     * Pre: Huffmantree must be built, and must input a valid bitString.
     */
    public String decodeBitString(String bitString){
        String decodedString = "";
        Node curNode = root;
        
        return decodedString;
    }

    /* Returns true if given node is leaf, and false if otherwise.
     */
    private boolean isLeafNode(Node node){
        if((node.left == null) && (node.right == null) && (node.character != null)){
            return true;
        } else {
            return false;
        }
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

        @Override //Must override the default compareTo method for Node, so comparisons can be made between Nodes based on their frequencies
        public int compareTo(Node otherNode) {
            return Integer.compare(this.frequency, otherNode.frequency); //(can use Integer.compare() to shorten up method)
        }
    }
}
