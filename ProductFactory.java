import java.math.BigDecimal;
import java.util.UUID;

public class ProductFactory {
    private final StoreModel model;

    public ProductFactory(StoreModel model) {
        this.model = model;
    }

    public Product create(String name, String description, BigDecimal price, int stock, Product.Category category) {
        long id = model.nextProductId();
        String sku = buildSku(category, name);
        return new Product(id, sku, name, description, price, stock, category);
    }

    private String buildSku(Product.Category category, String name) {
        String base = name.trim().toUpperCase().replaceAll("[^A-Z0-9]+", "-");
        String rand = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return category.name() + "-" + base + "-" + rand;
    }
}
