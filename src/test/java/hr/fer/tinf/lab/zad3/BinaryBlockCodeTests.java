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
        assertDoesNotThrow(() -> new BinaryBlockCode(2004));
        assertDoesNotThrow(() -> new BinaryBlockCode(1));
        assertDoesNotThrow(() -> new BinaryBlockCode(Integer.MAX_VALUE));

        int n = Integer.MAX_VALUE;
        assertEquals(n, new BinaryBlockCode(n).getN());
    }
}
