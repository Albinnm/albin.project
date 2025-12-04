public class Customer extends User {
    private final Cart cart = new Cart();

    public Customer(long id, String username, String email) {
        super(id, username, email, Role.CUSTOMER);
    }

    public Cart getCart() { return cart; }
}
