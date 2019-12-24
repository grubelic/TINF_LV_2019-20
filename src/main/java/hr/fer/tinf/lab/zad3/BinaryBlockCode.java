package hr.fer.tinf.lab.zad3;

public class BinaryBlockCode {
    private final int n;

    public BinaryBlockCode(int n) {
        if (n < 1) {
            throw new IllegalArgumentException(
                "Block size has to be a natural number"
            );
        }
        this.n = n;
    }

    public int getN() {
        return n;
    }

    // TODO: implement following methods
    public int getK() {
        return 0;
    }

    public boolean isLinear() {
        return false;
    }

    public BinaryVector[][] getStandardArray() {
        return null;
    }

    public double correctDecodingProbability() {
        return 0.d;
    }

    public String decode(BinaryVector codeword) {
        return null;
    }

    public void removeCodeword(BinaryVector bs) {
    }

    public void addCodeWord(BinaryVector newCodeword, String newSymbol) {
    }
}
