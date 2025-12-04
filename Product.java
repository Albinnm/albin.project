import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    public enum Category { RING, NECKLACE, BRACELET, EARRINGS, OTHER }

    private final long id;
    private final String sku;

    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Category category;

    public Product(long id, String sku, String name, String description, BigDecimal price, int stock, Category category) {
        if (id <= 0) throw new IllegalArgumentException("id must be > 0");
        if (sku == null || sku.isBlank()) throw new IllegalArgumentException("sku required");
        setName(name);
        setDescription(description);
        setPrice(price);
        setStock(stock);
        setCategory(category);

        this.id = id;
        this.sku = sku.trim();
    }

    public long getId() { return id; }
    public String getSku() { return sku; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        this.name = name.trim();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = (description == null) ? "" : description.trim();
    }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) {
        Objects.requireNonNull(price, "price");
        if (price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("price must be > 0");
        this.price = price;
    }

    public int getStock() { return stock; }
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("stock cannot be negative");
        this.stock = stock;
    }

    public Category getCategory() { return category; }
    public void setCategory(Category category) {
        this.category = Objects.requireNonNull(category, "category");
    }

    public boolean isInStock(int qty) {
        return qty > 0 && stock >= qty;
    }

    public void decreaseStock(int qty) {
        if (!isInStock(qty)) throw new IllegalArgumentException("Not enough stock for " + name);
        stock -= qty;
    }

    public void increaseStock(int qty) {
        if (qty > 0) stock += qty;
    }

    @Override public String toString() {
        return "[" + id + "] " + name + " (" + category + ") - " + price + " EUR | stock=" + stock + " | sku=" + sku;
    }
}
