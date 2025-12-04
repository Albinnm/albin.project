import java.math.BigDecimal;

public class Admin extends User {
    public Admin(long id, String username, String email) {
        super(id, username, email, Role.ADMIN);
    }

    public Product createProduct(StoreController controller, String name, String desc,
                                 BigDecimal price, int stock, Product.Category category) {
        return controller.adminCreateProduct(this, name, desc, price, stock, category);
    }

    public void updateStock(StoreController controller, long productId, int newStock) {
        controller.adminUpdateStock(this, productId, newStock);
    }

    public void deleteProduct(StoreController controller, long productId) {
        controller.adminDeleteProduct(this, productId);
    }
}
