package model;

import java.sql.Timestamp;
import java.util.List;

public class CompressionRecord {
    private int id;
    private String originalText;
    private String encodedBits;
    private int originalBits;
    private int compressedBits;
    private double savedPercent;
    private Timestamp createdAt;
    private List<CodeEntry> codeEntries;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }
    public String getEncodedBits() { return encodedBits; }
    public void setEncodedBits(String encodedBits) { this.encodedBits = encodedBits; }
    public int getOriginalBits() { return originalBits; }
    public void setOriginalBits(int originalBits) { this.originalBits = originalBits; }
    public int getCompressedBits() { return compressedBits; }
    public void setCompressedBits(int compressedBits) { this.compressedBits = compressedBits; }
    public double getSavedPercent() { return savedPercent; }
    public void setSavedPercent(double savedPercent) { this.savedPercent = savedPercent; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public List<CodeEntry> getCodeEntries() { return codeEntries; }
    public void setCodeEntries(List<CodeEntry> codeEntries) { this.codeEntries = codeEntries; }
}