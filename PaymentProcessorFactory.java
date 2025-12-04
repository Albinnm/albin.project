import java.math.BigDecimal;
import java.util.UUID;

public class PaymentProcessorFactory {
    public PaymentProcessor get(Payment.Method method) {
        return switch (method) {
            case CARD -> new Card();
            case PAYPAL -> new Paypal();
            case CASH_ON_DELIVERY -> new Cod();
        };
    }

    private static final class Card implements PaymentProcessor {
        public Result charge(BigDecimal amount) {
            return new Result(true, "CARD-" + UUID.randomUUID(), "Card payment approved");
        }
    }

    private static final class Paypal implements PaymentProcessor {
        public Result charge(BigDecimal amount) {
            return new Result(true, "PAYPAL-" + UUID.randomUUID(), "PayPal payment approved");
        }
    }

    private static final class Cod implements PaymentProcessor {
        public Result charge(BigDecimal amount) {
            return new Result(true, "COD-" + UUID.randomUUID(), "Cash on delivery selected");
        }
    }
}
