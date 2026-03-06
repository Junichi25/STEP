import java.util.*;

class TokenBucket {
    int tokens;
    int maxTokens;
    double refillRate;
    long lastRefillTime;

    TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    void refill() {
        long now = System.currentTimeMillis();
        double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;
        tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);
        lastRefillTime = now;
    }

    boolean allowRequest() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }
}

public class RateLimiter {

    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int LIMIT = 1000;
    private int WINDOW_SECONDS = 3600;

    public synchronized void checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(LIMIT, (double) LIMIT / WINDOW_SECONDS));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            System.out.println("Allowed (" + bucket.tokens + " requests remaining)");
        } else {
            System.out.println("Denied (0 requests remaining, retry later)");
        }
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No requests made yet");
            return;
        }

        int used = bucket.maxTokens - bucket.tokens;

        System.out.println("{used: " + used +
                ", limit: " + bucket.maxTokens +
                ", remaining: " + bucket.tokens + "}");
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        limiter.checkRateLimit("abc123");
        limiter.checkRateLimit("abc123");
        limiter.checkRateLimit("abc123");

        limiter.getRateLimitStatus("abc123");
    }
}