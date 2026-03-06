import java.util.*;

public class PlagiarismDetector {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private HashMap<String, List<String>> documentNgrams = new HashMap<>();
    private int N = 5;

    public void addDocument(String docId, String text) {

        List<String> words = Arrays.asList(text.split(" "));
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.size() - N; i++) {

            String gram = String.join(" ", words.subList(i, i + N));
            ngrams.add(gram);

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }

        documentNgrams.put(docId, ngrams);
    }

    public void analyzeDocument(String docId) {

        List<String> ngrams = documentNgrams.get(docId);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            Set<String> docs = ngramIndex.getOrDefault(gram, new HashSet<>());

            for (String otherDoc : docs) {

                if (!otherDoc.equals(docId)) {
                    matchCount.put(otherDoc, matchCount.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matches with " + doc + ": " + matches);
            System.out.println("Similarity: " + similarity + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            }
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        detector.addDocument("essay_089",
                "data structures and algorithms are important for computer science students");

        detector.addDocument("essay_092",
                "data structures and algorithms are important for computer science learning");

        detector.addDocument("essay_123",
                "data structures and algorithms are important for computer science students");

        detector.analyzeDocument("essay_123");
    }
}