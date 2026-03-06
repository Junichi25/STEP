import java.util.*;

class Event {
    String url;
    String userId;
    String source;

    Event(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class RealTimeAnalytics {

    private HashMap<String, Integer> pageViews = new HashMap<>();
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(Event event) {

        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);

        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        trafficSources.put(event.source, trafficSources.getOrDefault(event.source, 0) + 1);
    }

    public void getDashboard() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        System.out.println("Top Pages:");

        int rank = 1;
        while (!pq.isEmpty() && rank <= 10) {

            Map.Entry<String, Integer> entry = pq.poll();
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url + " - " + views + " views (" + unique + " unique)");
            rank++;
        }

        int total = 0;
        for (int c : trafficSources.values()) {
            total += c;
        }

        System.out.println("\nTraffic Sources:");

        for (String source : trafficSources.keySet()) {
            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;
            System.out.println(source + ": " + percent + "%");
        }
    }

    public static void main(String[] args) throws Exception {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        analytics.processEvent(new Event("/article/breaking-news", "user_456", "facebook"));
        analytics.processEvent(new Event("/sports/championship", "user_123", "direct"));
        analytics.processEvent(new Event("/sports/championship", "user_789", "google"));

        analytics.getDashboard();
    }
}