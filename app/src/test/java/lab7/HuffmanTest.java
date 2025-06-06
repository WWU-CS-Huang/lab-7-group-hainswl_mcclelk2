package lab7;
/*
 * Authors: Lawson Hainsworth & Koben M
 * Date: 5/5/2025
 * Purpose: Contains a couple brief unit tests for the Huffman class. Briefly checks that countFrequencies()
 * and buildHuffmanTree() work properly.
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class HuffmanTest {

    @Test //Test that countFrequencies method correctly counts character frequencies
    public void testCountFrequencies() { 
        //set input string
        String input = "hello";

        //call countFrequencies from the Huffman class
        Huffman huffmanTree = new Huffman();
        huffmanTree.countFrequencies(input);

        //create a new map for the expected key-value pairs after countFrequencies is called on the input string
        Map<Character, Integer> expected = new HashMap<>();
        expected.put('h', 1);
        expected.put('e', 1);
        expected.put('l', 2);
        expected.put('o', 1);

        //Ensure that the actual map matchs the expected map
        assertEquals("Frequency map should match expected map", expected, huffmanTree.map);
    }

    @Test // Test that buildHuffmanTree constructs a valid Huffman tree
    public void testBuildHuffmanTree() { 
        //set input string
        String input = "abbaca";

        //create a huffmanTree object
        Huffman huffmanTree = new Huffman();
        huffmanTree.countFrequencies(input);
        //get the root of the huffmanTree by calling buildHuffmanTree.
        huffmanTree.buildHuffmanTree();

        //Make sure root is not null.
        assertNotNull("Root node should not be null", huffmanTree.root);

        //Ensure that the root node's frequency is the sum of all the frequencies in the frequency map.
        assertEquals("Root frequency should be sum of all frequencies", 6, (int) huffmanTree.root.frequency);
    }
}
