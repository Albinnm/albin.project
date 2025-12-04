import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
    public enum Status { CREATED, PAID, CANCELLED }

    public static class Line {
        private final Product product;
        private final int quantity;
        private final BigDecimal unitPrice;

        public Line(Product product, int quantity, BigDecimal unitPrice) {
            this.product = Objects.requireNonNull(product, "product");
            if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
            this.quantity = quantity;
            this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }

        public BigDecimal lineTotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    private final long id;
    private final long customerId;
    private final LocalDateTime createdAt = LocalDateTime.now();

    private Status status = Status.CREATED;
    private final List<Line> lines = new ArrayList<>();

    private ShippingAddress shippingAddress;
    private Payment payment = new Payment();

    public Order(long id, long customerId) {
        if (id <= 0) throw new IllegalArgumentException("id must be > 0");
        if (customerId <= 0) throw new IllegalArgumentException("customerId must be > 0");
        this.id = id;
        this.customerId = customerId;
    }

    public long getId() { return id; }
    public long getCustomerId() { return customerId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Line> getLines() { return Collections.unmodifiableList(lines); }
    public void addLine(Line line) { lines.add(line); }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public BigDecimal total() {
        BigDecimal t = BigDecimal.ZERO;
        for (Line l : lines) t = t.add(l.lineTotal());
        return t;
    }

    @Override public String toString() {
        return "Order#" + id + " customer=" + customerId + " status=" + status + " total=" + total() + " createdAt=" + createdAt;
    }
}
