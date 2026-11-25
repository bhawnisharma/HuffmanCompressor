package model;

public class CodeEntry {

    private String symbol;
    private int frequency;
    private String huffmanCode;

    public CodeEntry(String symbol, int frequency, String huffmanCode) {
        this.symbol = symbol;
        this.frequency = frequency;
        this.huffmanCode = huffmanCode;
    }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }
    public String getHuffmanCode() { return huffmanCode; }
    public void setHuffmanCode(String huffmanCode) { this.huffmanCode = huffmanCode; }
}