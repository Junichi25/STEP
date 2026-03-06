import java.util.*;

public class FlashSaleInventoryManager {

    private HashMap<String, Integer> stock = new HashMap<>();
    private LinkedHashMap<Integer, String> waitingList = new LinkedHashMap<>();

    public FlashSaleInventoryManager() {
        stock.put("IPHONE15_256GB", 100);
    }

    public void checkStock(String productId) {
        int available = stock.getOrDefault(productId, 0);
        System.out.println(available + " units available");
    }

    public synchronized void purchaseItem(String productId, int userId) {

        int available = stock.getOrDefault(productId, 0);

        if (available > 0) {
            available = available - 1;
            stock.put(productId, available);
            System.out.println("Success! " + available + " units remaining");
        } else {
            waitingList.put(userId, productId);
            int position = waitingList.size();
            System.out.println("Stock finished. Added to waiting list. Position #" + position);
        }
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        manager.checkStock("IPHONE15_256GB");

        manager.purchaseItem("IPHONE15_256GB", 12345);
        manager.purchaseItem("IPHONE15_256GB", 67890);

        for(int i = 0; i < 100; i++){
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        manager.purchaseItem("IPHONE15_256GB", 99999);
    }
}