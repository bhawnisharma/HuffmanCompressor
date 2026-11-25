package algorithm;

import model.HuffmanNode;
import java.util.Map;
import java.util.HashMap;

public class HuffmanDecoder {
    public String decode(String bits, Map<Character, String> codeTable) {

    if (bits == null || codeTable == null || codeTable.isEmpty()) {
        throw new IllegalArgumentException(
            "Invalid encoded data or code table."
        );
    }

    Map<String, Character> reverseCodes = new HashMap<>();

    for (Map.Entry<Character, String> entry : codeTable.entrySet()) {
        reverseCodes.put(entry.getValue(), entry.getKey());
    }

    StringBuilder decoded = new StringBuilder();
    StringBuilder currentCode = new StringBuilder();

    for (int i = 0; i < bits.length(); i++) {

        char bit = bits.charAt(i);

        if (bit != '0' && bit != '1') {
            throw new IllegalArgumentException("Invalid binary input.");
        }

        currentCode.append(bit);

        if (reverseCodes.containsKey(currentCode.toString())) {
            decoded.append(reverseCodes.get(currentCode.toString()));
            currentCode.setLength(0);
        }
    }

    if (currentCode.length() > 0) {
        throw new IllegalArgumentException("Incomplete encoded data.");
    }

    return decoded.toString();
}

    public String decode(String bits, HuffmanNode root) {

       if (bits == null || root == null) {
    throw new IllegalArgumentException(
        "Encoded bits and Huffman tree cannot be null."
    );
}
        // Special case: only one unique character
        if (root.isLeaf()) {
    for (int i = 0; i < bits.length(); i++) {
        if (bits.charAt(i) != '0') {
            throw new IllegalArgumentException(
                "Invalid encoded data for single-character tree."
            );
        }
    }

    return String.valueOf(root.getCh()).repeat(bits.length());
}

        StringBuilder decoded = new StringBuilder();

        HuffmanNode current = root;

        for (int i = 0; i < bits.length(); i++) {

            char bit = bits.charAt(i);

            if (bit == '0') {
                current = current.getLeft();
            } else if (bit == '1') {
                current = current.getRight();
            } else {
                throw new IllegalArgumentException("Invalid binary input");
            }

            if (current == null) {
                throw new IllegalArgumentException("Invalid encoded data");
            }

            // If we reach a leaf, recover the character
            if (current.isLeaf()) {
                decoded.append(current.getCh());
                current = root;
            }
        }

        if (current != root) {
            throw new IllegalArgumentException("Incomplete encoded data");
        }

        return decoded.toString();
    }
}
