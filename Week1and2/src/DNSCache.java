import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCache {

    private int capacity = 5;

    private LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > capacity;
                }
            };

    private int hits = 0;
    private int misses = 0;

    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            System.out.println("Cache HIT → " + entry.ipAddress);
            return entry.ipAddress;
        }

        misses++;

        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 5));

        System.out.println("Cache MISS → Query upstream → " + ip);

        return ip;
    }

    private String queryUpstreamDNS(String domain) {
        return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }

    public void cleanExpiredEntries() {
        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> e = it.next();
            if (e.getValue().isExpired()) {
                it.remove();
            }
        }
    }

    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0) / total;

        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws Exception {

        DNSCache cache = new DNSCache();

        cache.resolve("google.com");
        cache.resolve("google.com");

        Thread.sleep(6000);

        cache.resolve("google.com");

        cache.getCacheStats();
    }
}