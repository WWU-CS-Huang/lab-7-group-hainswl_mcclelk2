package lab7;
/*
 * Authors: Lawson Hainsworth & Koben M
 * Date: 5/5/2025
 * Purpose: COMPLETE!
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class HuffmanTest {

    @Test
    public void testCountFrequencies() { //Test that countFrequencies method correctly counts character frequencies
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

    @Test
    public void testBuildHuffmanTree() { // Test that buildHuffmanTree constructs a valid Huffman tree
        //set input string
        String input = "abbaca";

        //create a huffmanTree object
        Huffman huffmanTree = new Huffman();

        //get the root of the huffmanTree by calling buildHuffmanTree.
        huffmanTree.buildHuffmanTree(input);

        //Make sure root is not null.
        assertNotNull("Root node should not be null", huffmanTree.root);

        //Ensure that the root node's frequency is the sum of all the frequencies in the frequency map.
        assertEquals("Root frequency should be sum of all frequencies", 6, (int) huffmanTree.root.frequency);
    }
}
