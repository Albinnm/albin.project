import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoreModel {
    private final Catalog catalog = new Catalog();
    private final List<Order> orders = new ArrayList<>();

    private long nextProductId = 100;
    private long nextOrderId = 5000;

    public Catalog getCatalog() { return catalog; }

    public long nextProductId() { return nextProductId++; }
    public long nextOrderId() { return nextOrderId++; }

    public void addOrder(Order order) { orders.add(order); }
    public List<Order> getOrders() { return Collections.unmodifiableList(orders); }
}
