package algorithm;

import java.util.Map;
import model.HuffmanNode;
import model.CompressionResult;
import util.ValidationUtil;

public class HuffmanEncoder {

    // Complete compression method
    public CompressionResult encode(String text) {

        ValidationUtil.validateText(text);

        HuffmanTree tree = new HuffmanTree();

        Map<Character, Integer> frequency =
                tree.buildFrequencyMap(text);

        HuffmanNode root = tree.buildTree(frequency);

        Map<Character, String> codes =
                tree.generateCodes(root);

        String encoded = encode(text, codes);

        return new CompressionResult(
            text, encoded, frequency, codes
        );
    }

    // Binary encoding method
    public String encode(String text, Map<Character, String> codes) {

        if (text == null || codes == null) {
            throw new IllegalArgumentException(
                "Text and Huffman codes cannot be null."
            );
        }

        StringBuilder encoded = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {

            char ch = text.charAt(i);

            if (!codes.containsKey(ch)) {
                throw new IllegalArgumentException(
                    "No Huffman code found for character: " + ch
                );
            }

            encoded.append(codes.get(ch));
        }

        return encoded.toString();
    }
}
