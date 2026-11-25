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

    public char getSymbol() { return symbol; }
    public void setSymbol(char symbol) { this.symbol = symbol; }
    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }
    public String getHuffmanCode() { return huffmanCode; }
    public void setHuffmanCode(String huffmanCode) { this.huffmanCode = huffmanCode; }
}