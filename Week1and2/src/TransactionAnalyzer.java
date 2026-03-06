import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);
                System.out.println("(" + other.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    public void findTwoSumWithTimeWindow(int target, long windowMillis) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= windowMillis) {
                    System.out.println("(" + other.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {
                System.out.println("Duplicate: " + key);
                for (Transaction t : list) {
                    System.out.println("Transaction ID: " + t.id + " Account: " + t.account);
                }
            }
        }
    }

    public void findKSum(int k, int target) {
        kSumHelper(0, k, target, new ArrayList<>());
    }

    private void kSumHelper(int start, int k, int target, List<Transaction> current) {

        if (k == 0 && target == 0) {
            for (Transaction t : current) {
                System.out.print(t.id + " ");
            }
            System.out.println();
            return;
        }

        if (k == 0 || start >= transactions.size()) {
            return;
        }

        for (int i = start; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            current.add(t);
            kSumHelper(i + 1, k - 1, target - t.amount, current);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        analyzer.addTransaction(new Transaction(1, 500, "Store A", "acc1", 1000));
        analyzer.addTransaction(new Transaction(2, 300, "Store B", "acc2", 1500));
        analyzer.addTransaction(new Transaction(3, 200, "Store C", "acc3", 2000));
        analyzer.addTransaction(new Transaction(4, 500, "Store A", "acc4", 2100));

        System.out.println("Two Sum:");
        analyzer.findTwoSum(500);

        System.out.println("\nDuplicates:");
        analyzer.detectDuplicates();

        System.out.println("\nK Sum:");
        analyzer.findKSum(3, 1000);
    }
}