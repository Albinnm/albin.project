import java.util.*;

public class Catalog {
    private final Map<Long, Product> byId = new LinkedHashMap<>();
    private final Map<String, Long> skuToId = new HashMap<>();

    public List<Product> listAll() {
        return new ArrayList<>(byId.values());
    }

    public Product get(long id) {
        Product p = byId.get(id);
        if (p == null) throw new IllegalArgumentException("Product not found: " + id);
        return p;
    }

    public void add(Product p) {
        if (skuToId.containsKey(p.getSku())) throw new IllegalArgumentException("SKU already exists: " + p.getSku());
        byId.put(p.getId(), p);
        skuToId.put(p.getSku(), p.getId());
    }

    public void delete(long id) {
        Product p = byId.remove(id);
        if (p != null) skuToId.remove(p.getSku());
    }

    public boolean exists(long id) { return byId.containsKey(id); }
}
