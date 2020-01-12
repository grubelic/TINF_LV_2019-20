package hr.fer.tinf.lab.zad3;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryBlockCodeTests {
    @Test
    public void constructor() {
        assertThrows(IllegalArgumentException.class,
            () -> new BinaryBlockCode(0));
        assertThrows(IllegalArgumentException.class,
            () -> new BinaryBlockCode(Integer.MIN_VALUE));
        assertThrows(IllegalArgumentException.class,
            () -> new BinaryBlockCode(Integer.MAX_VALUE));
        assertDoesNotThrow(() -> new BinaryBlockCode(2004));
        assertDoesNotThrow(() -> new BinaryBlockCode(1));

        int n = 1;
        assertEquals(n, new BinaryBlockCode(n).getN());
    }

    @Test
    public void isLinear() {
        BinaryBlockCode bbc = new BinaryBlockCode(6);
        String[] binaryStrings = new String[] {
            "000000", "001110", "010101", "011011",
            "100011", "101101", "110110", "111000"
        };
        for (String s : binaryStrings) {
            bbc.addCodeWord(new BinaryVector(s), null);
        }
        assertTrue(bbc.isLinear());
        bbc.removeCodeword(new BinaryVector(6));
        assertFalse(bbc.isLinear());
    }

    @Test
    public void correctDecodingProbability() {
        BinaryBlockCode bbc = new BinaryBlockCode(6);
        String[] binaryStrings = new String[] {
            "000000", "001011", "010110", "011101",
            "100101", "101110", "110011", "111000"
        };
        for (String s : binaryStrings) {
            bbc.addCodeWord(new BinaryVector(s), null);
        }
        assertTrue(bbc.correctDecodingProbability(0.07) < 0.94);
        assertTrue(bbc.correctDecodingProbability(0.07) > 0.939);
    }
}
