package algorithm;

import model.HuffmanNode;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

import java.util.TreeMap;

public class HuffmanTree {

    // Count how many times each character appears
    public Map<Character, Integer> buildFrequencyMap(String text) {

        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (int i = 0; i < text.length(); i++) {

            char ch = text.charAt(i);

            frequencyMap.put(
                ch,
                frequencyMap.getOrDefault(ch, 0) + 1
            );
        }

        return frequencyMap;
    }
    public HuffmanNode buildTree(Map<Character, Integer> freqMap) {
        if (freqMap == null || freqMap.isEmpty()) {
    return null;
}

    PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

    int order = 0;

    // Create one node for each character
    for (Map.Entry<Character, Integer> entry :
        new TreeMap<>(freqMap).entrySet()) {

        HuffmanNode node = new HuffmanNode(
            entry.getKey(),
            entry.getValue(),
            order++
        );

        pq.add(node);
    }

    // Combine the two nodes with smallest frequencies
    while (pq.size() > 1) {

        HuffmanNode left = pq.poll();
        HuffmanNode right = pq.poll();

        HuffmanNode parent = new HuffmanNode(left, right, order++);

        pq.add(parent);
    }

    return pq.poll();
}
public Map<Character, String> generateCodes(HuffmanNode root) {

    Map<Character, String> codes = new HashMap<>();

    generateCodesHelper(root, "", codes);

    return codes;
}

private void generateCodesHelper(
        HuffmanNode node,
        String code,
        Map<Character, String> codes) {

    if (node == null) {
        return;
    }

    // If node is a leaf, store its code
    if (node.isLeaf()) {
        codes.put(node.getCh(), code.isEmpty() ? "0" : code);
    }
generateCodesHelper(node.getLeft(), code + "0", codes);
generateCodesHelper(node.getRight(), code + "1", codes);

}
}
