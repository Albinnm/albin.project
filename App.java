import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {
        StoreModel model = new StoreModel();
        ProductFactory productFactory = new ProductFactory(model);
        PaymentProcessorFactory paymentFactory = new PaymentProcessorFactory();
        StoreController controller = new StoreController(model, productFactory, paymentFactory);
        ConsoleView view = new ConsoleView(controller);

        // Seed demo data
        Admin admin = new Admin(1, "admin", "admin@niche.store");
        admin.createProduct(controller, "Minimalist Silver Ring", "Handcrafted ring in sterling silver",
                new BigDecimal("39.99"), 25, Product.Category.RING);
        admin.createProduct(controller, "Gold Pearl Necklace", "Elegant pearl necklace in 18k gold",
                new BigDecimal("119.00"), 10, Product.Category.NECKLACE);
        admin.createProduct(controller, "Rose Gold Bracelet", "Modern bracelet, rose-gold finish",
                new BigDecimal("69.50"), 15, Product.Category.BRACELET);
        admin.createProduct(controller, "Emerald Stud Earrings", "Small emerald studs, premium finish",
                new BigDecimal("149.99"), 8, Product.Category.EARRINGS);

        // Demo customer
        Customer customer = new Customer(2, "albin", "albin@example.com");

        // MVC: View drives the app, Controller updates Model
        view.run(customer, admin);
    }
}
