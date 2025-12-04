import java.time.LocalDateTime;

public class Payment {
    public enum Method { CARD, PAYPAL, CASH_ON_DELIVERY }
    public enum Status { PENDING, PAID, FAILED }

    private Method method = Method.CARD;
    private Status status = Status.PENDING;
    private String reference = "";
    private LocalDateTime paidAt;

    public Payment() {}

    public Payment(Method method) {
        this.method = method;
    }

    public Method getMethod() { return method; }
    public void setMethod(Method method) { this.method = method; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = (reference == null) ? "" : reference; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    @Override public String toString() {
        return "Payment{method=" + method + ", status=" + status + ", ref='" + reference + "', paidAt=" + paidAt + "}";
    }
}
