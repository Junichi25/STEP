import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class MultiLevelCache {

    private LinkedHashMap<String, VideoData> L1;
    private HashMap<String, VideoData> L2;
    private HashMap<String, VideoData> L3;

    private HashMap<String, Integer> accessCount;

    private int L1Capacity = 10000;
    private int L2Capacity = 100000;
    private int promoteThreshold = 3;

    private int L1Hits = 0, L1Misses = 0;
    private int L2Hits = 0, L2Misses = 0;
    private int L3Hits = 0, L3Misses = 0;

    public MultiLevelCache() {
        L1 = new LinkedHashMap<String, VideoData>(L1Capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1Capacity;
            }
        };
        L2 = new LinkedHashMap<String, VideoData>(L2Capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L2Capacity;
            }
        };
        L3 = new HashMap<>();
        accessCount = new HashMap<>();
    }

    public void addToDatabase(VideoData video) {
        L3.put(video.videoId, video);
    }

    public VideoData getVideo(String videoId) {

        long start = System.currentTimeMillis();

        if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 HIT (" + (System.currentTimeMillis() - start) + "ms)");
            return L1.get(videoId);
        } else {
            L1Misses++;
        }

        if (L2.containsKey(videoId)) {
            L2Hits++;
            System.out.println("L1 MISS → L2 HIT (" + (System.currentTimeMillis() - start) + "ms)");
            accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);

            if (accessCount.get(videoId) >= promoteThreshold) {
                L1.put(videoId, L2.get(videoId));
                System.out.println("Promoted to L1");
            }

            return L2.get(videoId);
        } else {
            L2Misses++;
        }

        if (L3.containsKey(videoId)) {
            L3Hits++;
            System.out.println("L1 MISS → L2 MISS → L3 HIT (" + (System.currentTimeMillis() - start) + "ms)");
            VideoData v = L3.get(videoId);
            L2.put(videoId, v);
            accessCount.put(videoId, 1);
            return v;
        } else {
            L3Misses++;
        }

        System.out.println("Video not found");
        return null;
    }

    public void getStatistics() {

        int totalL1 = L1Hits + L1Misses;
        int totalL2 = L2Hits + L2Misses;
        int totalL3 = L3Hits + L3Misses;

        double L1HitRate = totalL1 == 0 ? 0 : (L1Hits * 100.0 / totalL1);
        double L2HitRate = totalL2 == 0 ? 0 : (L2Hits * 100.0 / totalL2);
        double L3HitRate = totalL3 == 0 ? 0 : (L3Hits * 100.0 / totalL3);

        System.out.println("L1 Hit Rate: " + L1HitRate + "%");
        System.out.println("L2 Hit Rate: " + L2HitRate + "%");
        System.out.println("L3 Hit Rate: " + L3HitRate + "%");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        for (int i = 1; i <= 5; i++) {
            cache.addToDatabase(new VideoData("video_" + i, "Content of video " + i));
        }

        cache.getVideo("video_1");
        cache.getVideo("video_1");
        cache.getVideo("video_2");
        cache.getVideo("video_3");
        cache.getVideo("video_1");

        cache.getStatistics();
    }
}