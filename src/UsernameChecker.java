import java.util.*;

public class UsernameChecker {

    // Stores already registered usernames
    private HashSet<String> usernames = new HashSet<>();

    // Stores how many times a username was attempted
    private HashMap<String, Integer> attempts = new HashMap<>();

    // Register a username
    public void registerUser(String username) {
        usernames.add(username.toLowerCase());
    }

    // Check if username is available
    public boolean checkAvailability(String username) {
        username = username.toLowerCase();

        // Increase attempt frequency
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        return !usernames.contains(username); // O(1)
    }

    // Suggest similar available usernames
    public List<String> suggestAlternatives(String username) {
        username = username.toLowerCase();
        List<String> suggestions = new ArrayList<>();

        int count = 1;
        while (suggestions.size() < 3) {
            String newUsername = username + count;
            if (!usernames.contains(newUsername)) {
                suggestions.add(newUsername);
            }
            count++;
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {
        String maxUser = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attempts.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxUser = entry.getKey();
            }
        }

        return maxUser + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        UsernameChecker system = new UsernameChecker();

        system.registerUser("john_doe");
        system.registerUser("admin");

        System.out.println(system.checkAvailability("john_doe"));   // false
        System.out.println(system.checkAvailability("jane_smith")); // true

        System.out.println(system.suggestAlternatives("john_doe"));
        System.out.println(system.getMostAttempted());
    }
}