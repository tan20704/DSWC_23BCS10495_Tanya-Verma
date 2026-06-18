import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> cart = new ArrayList<>();
        cart.add("ItemA");
        cart.add("Out_Of_Stock");
        cart.add("ItemB");
        cart.add("Out_Of_Stock");
        cart.add("ItemC");

        try {
            // This will throw ConcurrentModificationException when removing during for-each.
            for (String item : cart) {
                if (item.equals("Out_Of_Stock")) {
                    cart.remove(item);
                }
            }
        } catch (Exception ex) {
            System.out.println("Caught exception while modifying list during iteration: " + ex);
        }

        // Safely remove out-of-stock items using an explicit iterator.
        Iterator<String> iterator = cart.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("Out_Of_Stock")) {
                iterator.remove();
            }
        }

        System.out.println("Cart after safe cleanup: " + cart);
    }
}
