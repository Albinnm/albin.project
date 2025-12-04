import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private final StoreController controller;
    private final Scanner in = new Scanner(System.in);

    public ConsoleView(StoreController controller) {
        this.controller = controller;
    }

    public void run(Customer customer, Admin admin) {
        while (true) {
            System.out.println("\n=== NICHE JEWELRY STORE ===");
            System.out.println("1) Browse products");
            System.out.println("2) View cart");
            System.out.println("3) Checkout");
            System.out.println("4) Admin panel");
            System.out.println("0) Exit");
            System.out.print("> ");

            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> browse(customer);
                    case "2" -> showCart(customer);
                    case "3" -> checkout(customer);
                    case "4" -> adminPanel(admin);
                    case "0" -> { System.out.println("Bye!"); return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void browse(Customer customer) {
        List<Product> products = controller.listProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("\n--- Products ---");
        for (Product p : products) System.out.println(p);

        System.out.print("\nEnter product id to add to cart (or blank to go back): ");
        String s = in.nextLine().trim();
        if (s.isBlank()) return;

        long id = Long.parseLong(s);
        System.out.print("Qty: ");
        int qty = Integer.parseInt(in.nextLine().trim());

        controller.addToCart(customer, id, qty);
        System.out.println("Added to cart.");
    }

    private void showCart(Customer customer) {
        System.out.println("\n--- Cart ---");
        if (customer.getCart().isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        for (String line : controller.cartSummary(customer)) System.out.println(line);
        System.out.println("TOTAL: " + controller.cartTotal(customer) + " EUR");

        System.out.print("Type 'remove' to remove item, 'clear' to clear cart, or Enter to go back: ");
        String cmd = in.nextLine().trim().toLowerCase();
        if (cmd.equals("clear")) {
            customer.getCart().clear();
            System.out.println("Cart cleared.");
        } else if (cmd.equals("remove")) {
            System.out.print("Product id to remove: ");
            long id = Long.parseLong(in.nextLine().trim());
            customer.getCart().remove(id);
            System.out.println("Removed (if existed).");
        }
    }

    private void checkout(Customer customer) {
        if (customer.getCart().isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("\n--- Checkout ---");
        System.out.println("TOTAL: " + controller.cartTotal(customer) + " EUR");

        System.out.print("Full name: "); String fullName = in.nextLine().trim();
        System.out.print("Phone: "); String phone = in.nextLine().trim();
        System.out.print("Email: "); String email = in.nextLine().trim();
        System.out.print("Address line 1: "); String line1 = in.nextLine().trim();
        System.out.print("Address line 2 (optional): "); String line2 = in.nextLine().trim();
        System.out.print("City: "); String city = in.nextLine().trim();
        System.out.print("Country: "); String country = in.nextLine().trim();

        ShippingAddress addr = new ShippingAddress(fullName, phone, email, line1, line2, city, country);

        System.out.println("Payment method: 1) CARD  2) PAYPAL  3) CASH_ON_DELIVERY");
        System.out.print("> ");
        String pm = in.nextLine().trim();

        Payment.Method method = switch (pm) {
            case "1" -> Payment.Method.CARD;
            case "2" -> Payment.Method.PAYPAL;
            case "3" -> Payment.Method.CASH_ON_DELIVERY;
            default -> throw new IllegalArgumentException("Invalid payment method");
        };

        Order order = controller.checkout(customer, addr, method);
        System.out.println("\n✅ Order result:");
        System.out.println(order);
        System.out.println(order.getPayment());
        System.out.println("\nShipping:\n" + order.getShippingAddress().format());
    }

    private void adminPanel(Admin admin) {
        while (true) {
            System.out.println("\n=== ADMIN PANEL ===");
            System.out.println("1) List products");
            System.out.println("2) Add product");
            System.out.println("3) Update stock");
            System.out.println("4) Delete product");
            System.out.println("5) List orders");
            System.out.println("0) Back");
            System.out.print("> ");

            String c = in.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> controller.listProducts().forEach(System.out::println);
                    case "2" -> adminAdd(admin);
                    case "3" -> adminStock(admin);
                    case "4" -> adminDelete(admin);
                    case "5" -> controller.adminListOrders(admin).forEach(System.out::println);
                    case "0" -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void adminAdd(Admin admin) {
        System.out.print("Name: "); String name = in.nextLine().trim();
        System.out.print("Description: "); String desc = in.nextLine().trim();
        System.out.print("Price (e.g. 49.99): "); BigDecimal price = new BigDecimal(in.nextLine().trim());
        System.out.print("Stock: "); int stock = Integer.parseInt(in.nextLine().trim());

        System.out.println("Category: 1) RING 2) NECKLACE 3) BRACELET 4) EARRINGS 5) OTHER");
        System.out.print("> ");
        String cat = in.nextLine().trim();
        Product.Category category = switch (cat) {
            case "1" -> Product.Category.RING;
            case "2" -> Product.Category.NECKLACE;
            case "3" -> Product.Category.BRACELET;
            case "4" -> Product.Category.EARRINGS;
            case "5" -> Product.Category.OTHER;
            default -> throw new IllegalArgumentException("Invalid category");
        };

        Product p = admin.createProduct(controller, name, desc, price, stock, category);
        System.out.println("Created: " + p);
    }

    private void adminStock(Admin admin) {
        System.out.print("Product id: "); long id = Long.parseLong(in.nextLine().trim());
        System.out.print("New stock: "); int stock = Integer.parseInt(in.nextLine().trim());
        admin.updateStock(controller, id, stock);
        System.out.println("Updated.");
    }

    private void adminDelete(Admin admin) {
        System.out.print("Product id: "); long id = Long.parseLong(in.nextLine().trim());
        admin.deleteProduct(controller, id);
        System.out.println("Deleted (if existed).");
    }
}
