public class ShippingAddress {
    private String fullName;
    private String phone;
    private String email;
    private String line1;
    private String line2;
    private String city;
    private String country;

    public ShippingAddress(String fullName, String phone, String email,
                           String line1, String line2, String city, String country) {
        this.fullName = req(fullName, "fullName");
        this.phone = req(phone, "phone");
        this.email = req(email, "email");
        this.line1 = req(line1, "line1");
        this.line2 = (line2 == null) ? "" : line2.trim();
        this.city = req(city, "city");
        this.country = req(country, "country");
    }

    private static String req(String v, String name) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(name + " required");
        return v.trim();
    }

    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullName).append("\n");
        sb.append(line1).append("\n");
        if (!line2.isBlank()) sb.append(line2).append("\n");
        sb.append(city).append("\n");
        sb.append(country).append("\n");
        sb.append("Phone: ").append(phone).append("\n");
        sb.append("Email: ").append(email);
        return sb.toString();
    }
}
