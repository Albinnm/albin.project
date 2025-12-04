import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    // productId -> qty
    private final Map<Long, Integer> items = new LinkedHashMap<>();

    public Map<Long, Integer> items() {
        return Collections.unmodifiableMap(items);
    }

    public boolean isEmpty() { return items.isEmpty(); }

    public void add(long productId, int qty) {
        if (qty <= 0) qty = 1;
        items.put(productId, items.getOrDefault(productId, 0) + qty);
    }

    public void update(long productId, int qty) {
        if (qty <= 0) items.remove(productId);
        else items.put(productId, qty);
    }

    public void remove(long productId) { items.remove(productId); }

    public void clear() { items.clear(); }
}
