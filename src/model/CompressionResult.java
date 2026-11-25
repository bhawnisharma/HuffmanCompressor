package model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class CompressionResult {

    private String originalText;
    private String encodedBits;

    private Map<Character, Integer> frequencyMap;
    private Map<Character, String> codeTable;

    private List<CodeEntry> codeEntries;

    private int originalBits;
    private int compressedBits;
    private double savedPercent;
    private double ratio;

    public CompressionResult(
            String originalText,
            String encodedBits,
            Map<Character, Integer> frequencyMap,
            Map<Character, String> codeTable) {

        this.originalText = originalText;
        this.encodedBits = encodedBits;

        this.frequencyMap = new HashMap<>(frequencyMap);
        this.codeTable = new HashMap<>(codeTable);

        this.codeEntries = new ArrayList<>();

        for (char ch : frequencyMap.keySet()) {

            CodeEntry entry = new CodeEntry(
                String.valueOf(ch),
                frequencyMap.get(ch),
                codeTable.get(ch)
            );

            codeEntries.add(entry);
        }

        this.originalBits = originalText.length() * 8;
        this.compressedBits = encodedBits.length();

        if (originalBits > 0) {

            this.savedPercent =
                ((double) (originalBits - compressedBits)
                / originalBits) * 100;

            this.ratio = compressedBits > 0
                ? (double) originalBits / compressedBits
                : 0;

        } else {
            this.savedPercent = 0;
            this.ratio = 0;
        }
    }

    public boolean isCompressionSuccessful(String decodedText) {
        return originalText.equals(decodedText);
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getEncodedBits() {
        return encodedBits;
    }

    public Map<Character, Integer> getFrequencyMap() {
        return frequencyMap;
    }

    public Map<Character, String> getCodeTable() {
        return codeTable;
    }

    public List<CodeEntry> getCodeEntries() {
        return codeEntries;
    }

    public int getOriginalBits() {
        return originalBits;
    }

    public int getCompressedBits() {
        return compressedBits;
    }

    public double getSavedPercent() {
        return savedPercent;
    }

    public double getRatio() {
        return ratio;
    }
}