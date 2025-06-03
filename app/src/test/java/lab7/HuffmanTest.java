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

    @Before
    public void setUp() {
        // Initialize the static map before each test to ensure a clean state
        Huffman.map = new HashMap<>();
    }

    @Test
    public void testCountFrequencies() { // Test that countFrequencies method correctly counts character frequencies
        //set input string
        String input = "hello";

        //call countFrequencies from the Huffman class
        Huffman.countFrequencies(input);

        //create a new map for the expected key-value pairs after countFrequencies is caleld on the input string
        Map<Character, Integer> expected = new HashMap<>();
        expected.put('h', 1);
        expected.put('e', 1);
        expected.put('l', 2);
        expected.put('o', 1);

        //Ensure that the actual map matchs the expected map
        assertEquals("Frequency map should match expected map", expected, Huffman.map);
    }

    @Test
    public void testBuildHuffmanTree() { // Test that buildHuffmanTree constructs a valid Huffman tree
        //set input string
        String input = "abbaca";

        //call countFrequencies from the Huffman class
        Huffman.countFrequencies(input);

        //get the root of the huffmanTree by calling buildHuffmanTree.
        Huffman.Node root = Huffman.buildHuffmanTree();

        //Make sure root is not null.
        assertNotNull("Root node should not be null", root);

        //Ensure that the root node's frequency is the sum of all the frequencies in the frequency map.
        assertEquals("Root frequency should be sum of all frequencies", 6, (int) root.frequency);
    }
}
