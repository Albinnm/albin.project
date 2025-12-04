import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreController {
    private final StoreModel model;
    private final ProductFactory productFactory;
    private final PaymentProcessorFactory paymentFactory;

    public StoreController(StoreModel model, ProductFactory productFactory, PaymentProcessorFactory paymentFactory) {
        this.model = model;
        this.productFactory = productFactory;
        this.paymentFactory = paymentFactory;
    }

    // ===== Customer features =====
    public List<Product> listProducts() {
        return model.getCatalog().listAll();
    }

    public Product getProduct(long id) {
        return model.getCatalog().get(id);
    }

    public void addToCart(Customer customer, long productId, int qty) {
        Product p = getProduct(productId);
        if (!p.isInStock(qty)) throw new IllegalArgumentException("Not enough stock for: " + p.getName());
        customer.getCart().add(productId, qty);
    }

    public List<String> cartSummary(Customer customer) {
        List<String> out = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : customer.getCart().items().entrySet()) {
            Product p = getProduct(e.getKey());
            out.add(p.getName() + " x" + e.getValue() + " = " + p.getPrice().multiply(BigDecimal.valueOf(e.getValue())) + " EUR");
        }
        return out;
    }

    public BigDecimal cartTotal(Customer customer) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> e : customer.getCart().items().entrySet()) {
            Product p = getProduct(e.getKey());
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(e.getValue())));
        }
        return total;
    }

    public Order checkout(Customer customer, ShippingAddress address, Payment.Method method) {
        if (customer.getCart().isEmpty()) throw new IllegalArgumentException("Cart is empty");

        // stock check again
        for (Map.Entry<Long, Integer> e : customer.getCart().items().entrySet()) {
            Product p = getProduct(e.getKey());
            if (!p.isInStock(e.getValue())) throw new IllegalArgumentException("Not enough stock for: " + p.getName());
        }

        Order order = new Order(model.nextOrderId(), customer.getId());
        order.setShippingAddress(address);
        order.setPayment(new Payment(method));

        for (Map.Entry<Long, Integer> e : customer.getCart().items().entrySet()) {
            Product p = getProduct(e.getKey());
            int qty = e.getValue();
            order.addLine(new Order.Line(p, qty, p.getPrice()));
        }

        BigDecimal total = order.total();

        // Factory-created processor
        PaymentProcessor processor = paymentFactory.get(method);
        PaymentProcessor.Result result = processor.charge(total);

        if (result.success()) {
            // decrease stock
            for (Order.Line line : order.getLines()) {
                line.getProduct().decreaseStock(line.getQuantity());
            }
            order.getPayment().setStatus(Payment.Status.PAID);
            order.getPayment().setPaidAt(LocalDateTime.now());
            order.getPayment().setReference(result.reference());
            order.setStatus(Order.Status.PAID);

            model.addOrder(order);
            customer.getCart().clear();
            return order;
        } else {
            order.getPayment().setStatus(Payment.Status.FAILED);
            order.getPayment().setReference(result.reference());
            model.addOrder(order);
            return order;
        }
    }

    // ===== Admin features =====
    public Product adminCreateProduct(Admin admin, String name, String desc, BigDecimal price, int stock, Product.Category category) {
        requireAdmin(admin);
        Product p = productFactory.create(name, desc, price, stock, category);
        model.getCatalog().add(p);
        return p;
    }

    public void adminUpdateStock(Admin admin, long productId, int newStock) {
        requireAdmin(admin);
        Product p = model.getCatalog().get(productId);
        p.setStock(newStock);
    }

    public void adminDeleteProduct(Admin admin, long productId) {
        requireAdmin(admin);
        model.getCatalog().delete(productId);
    }

    public List<Order> adminListOrders(Admin admin) {
        requireAdmin(admin);
        return model.getOrders();
    }

    private void requireAdmin(User u) {
        if (u == null || u.getRole() != User.Role.ADMIN) throw new SecurityException("Admin access required");
    }
}
