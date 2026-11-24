
import algorithm.HuffmanEncoder;
import algorithm.HuffmanDecoder;

import model.CompressionResult;
import model.CodeEntry;
import util.SymbolUtil;
import util.ValidationUtil;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
   String text = "BANANA";
      ValidationUtil.validateText(text);

      HuffmanEncoder encoder = new HuffmanEncoder();

CompressionResult result = encoder.encode(text);

Map<Character, Integer> frequency = result.getFrequencyMap();
Map<Character, String> codes = result.getCodeTable();

String encoded = result.getEncodedBits();

        // Step 6: Decode encoded text
        HuffmanDecoder decoder = new HuffmanDecoder();

        String decoded = decoder.decode(encoded, codes);
        System.out.println("\nHuffman Code Table:");
System.out.println("Symbol\tFrequency\tCode");

for (CodeEntry entry : result.getCodeEntries()) {
    System.out.println(
        SymbolUtil.toSymbolName(entry.getSymbol()) + "\t" +
        entry.getFrequency() + "\t\t" +
        entry.getHuffmanCode()
    );
}

        // Display results
        System.out.println("Original Text: " + text);
        System.out.println("\nFrequencies:");
for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
    System.out.println(
        SymbolUtil.toSymbolName(entry.getKey()) +
        " = " + entry.getValue()
    );
}
        System.out.println("\nHuffman Codes:");
for (Map.Entry<Character, String> entry : codes.entrySet()) {
    System.out.println(
        SymbolUtil.toSymbolName(entry.getKey()) +
        " = " + entry.getValue()
    );
}
        System.out.println("Encoded Binary: " + encoded);

        System.out.println("Original Size: " +
                result.getOriginalBits() + " bits");

        System.out.println("Compressed Size: " +
                result.getCompressedBits() + " bits");

        System.out.println("Space Saved: " +
                result.getSavedPercent() + "%");

        System.out.println("Compression Ratio: " +
                result.getRatio());

        System.out.println("Decoded Text: " + decoded);

        System.out.println("Compression Successful: " +
                result.isCompressionSuccessful(decoded));
    }
}


