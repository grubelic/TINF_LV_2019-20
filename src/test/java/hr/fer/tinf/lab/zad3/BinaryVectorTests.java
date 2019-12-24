package hr.fer.tinf.lab.zad3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinaryVectorTests {
    @Test
    public void constructors() {

        // Edge case: vector with size 0
        String emptyString = "";
        BinaryVector zeroDimVector = new BinaryVector();
        assertEquals(zeroDimVector, new BinaryVector(emptyString));
        assertEquals(0, new BinaryVector(emptyString).dim());

        // Test constructor given size only
        String zeroByteString = "00000000";
        BinaryVector zeroByteVector = new BinaryVector(8);
        assertEquals(zeroByteString, zeroByteVector.toString());

        // Test Endianness, constructor that gets longs
        String binary420String = "110100100";
        BinaryVector binary420Vector = new BinaryVector(9, 420L);
        assertEquals(binary420String, binary420Vector.toString());
        assertEquals(binary420String,
            new BinaryVector(binary420String).toString());

        // Test constructor given boolean array
        BinaryVector newBinary420Vector = new BinaryVector(new boolean[] {
            true, true, false, true, false, false, true, false, false
        });
        assertEquals(binary420String, newBinary420Vector.toString());
    }

    @Test
    public void equals() {
        Assertions.assertEquals(
            new BinaryVector(6, 42), new BinaryVector("101010"));

        // Differentiate vectors with leading and trailing zeros
        Assertions.assertNotEquals(new BinaryVector("01"),
            new BinaryVector("010"));
        Assertions.assertNotEquals(new BinaryVector("001"),
            new BinaryVector("0001"));
    }

    @Test
    public void add() {
        BinaryVector bv0b110 = new BinaryVector(3, 0b110);
        BinaryVector bv0b011 = new BinaryVector(3, 0b011);
        BinaryVector bv0b101 = new BinaryVector(3, 0b101);
        bv0b101.add(bv0b110);
        Assertions.assertEquals(bv0b011, bv0b101);
        bv0b101.add(bv0b110);
        Assertions.assertEquals("101", bv0b101.toString());
    }
}
