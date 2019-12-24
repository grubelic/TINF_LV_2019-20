package hr.fer.tinf.lab.zad3;

import java.util.Arrays;

/**
 * Class representing a binary vector, used as codewords
 */
public class BinaryVector {

    private final boolean[] bitArray;

    /**
     * Default constructor, generates empty BitSet
     */
    public BinaryVector() {
        this(0);
    }

    public BinaryVector(boolean[] bitArray) {
        this.bitArray = bitArray.clone();
    }

    public BinaryVector(int length) {
        this.bitArray = new boolean[length];
    }

    public BinaryVector(String bitString) {
        this(bitString.length());
        for (int i = 0; i < bitString.length(); i++) {
            switch (bitString.charAt(i)) {
                case '1':
                    set(i);
                case '0':
                    break;
                default:
                    throw new IllegalArgumentException("Not a bit set");
            }
        }
    }

    public BinaryVector(byte... bytes) {
        this(bytes.length * 8, bytes);
    }

    /**
     * Creates a codeword using n least significant bits of bytes param
     * Big-endian
     * @param n size of the codeword
     * @param bytes source for filling bits
     */
    public BinaryVector(int n, byte... bytes) {
        this(n);
        int i = 0;
        for (byte bits : bytes) {
            for (int j = 8; j > 0 && i < dim(); --j, bits >>>= 1, i++) {
                if ((bits & 1) != 0) set(bitArray.length - 1 - i);
            }
        }
    }

    public BinaryVector(long... longs) {
        this(longs.length * 32, longs);
    }

    /**
     * Creates a codeword using n least significant bits of longs param
     * Big-endian
     * @param n size of the codeword
     * @param longs source for filling bits
     */
    public BinaryVector(int n, long... longs) {
        this(n);
        int i = 0;
        for (long bits : longs) {
            for (int j = 64; j > 0 && i < dim(); --j, bits >>>= 1, i++) {
                if ((bits & 1) != 0) set(bitArray.length - 1 - i);
            }
        }
    }

    public int dim() {
        return bitArray.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean it : bitArray) {
            sb.append(it ? '1' : '0');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryVector)) return false;
        BinaryVector that = (BinaryVector) o;
        return Arrays.equals(bitArray, that.bitArray);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bitArray);
    }

    public void set(boolean value) {
        Arrays.fill(bitArray, value);
    }

    public void set(int index) {
        bitArray[index] = true;
    }

    public void set(int index, boolean value) {
        bitArray[index] = value;
    }

    public boolean get(int index) {
        return bitArray[index];
    }

    public void add(BinaryVector v) {
        if (dim() != v.dim()) {
            throw new IllegalArgumentException("Vector dimensions don't match");
        }
        for (int i = 0; i < bitArray.length; i++) {
            bitArray[i] ^= v.get(i);
        }
    }

    public void scale(boolean factor) {
        if (!factor) {
            set(false);
        }
    }
}
