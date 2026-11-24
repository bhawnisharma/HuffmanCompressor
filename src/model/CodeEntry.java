
package model;

public class CodeEntry {

    private char symbol;
private int frequency;
private String huffmanCode;

    public CodeEntry(char symbol, int frequency, String huffmanCode) {
        this.symbol = symbol;
        this.frequency = frequency;
        this.huffmanCode = huffmanCode;
    }
    public char getSymbol() {
    return symbol;
}

public int getFrequency() {
    return frequency;
}

public String getHuffmanCode() {
    return huffmanCode;
}
}

