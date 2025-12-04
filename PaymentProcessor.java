import java.math.BigDecimal;

public interface PaymentProcessor {
    record Result(boolean success, String reference, String message) {}

    Result charge(BigDecimal amount);
}
