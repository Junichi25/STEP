import java.util.*;

class AutocompleteSystem {

    private HashMap<String, Integer> queryFrequency = new HashMap<>();

    public void addQuery(String query) {
        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);
    }

    public List<String> search(String prefix) {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                pq.add(entry);
            }
        }

        List<String> results = new ArrayList<>();

        int count = 0;
        while (!pq.isEmpty() && count < 10) {
            Map.Entry<String, Integer> entry = pq.poll();
            results.add(entry.getKey() + " (" + entry.getValue() + " searches)");
            count++;
        }

        return results;
    }

    public void updateFrequency(String query) {
        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("java 21 features");

        List<String> suggestions = system.search("jav");

        for (String s : suggestions) {
            System.out.println(s);
        }

        system.updateFrequency("java 21 features");
    }
}