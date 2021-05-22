package mobile_computing.delifast.delifastEnum;

public enum OrderStatus {

    OPEN("offen"),
    PAYED("bezahlt"),
    ACCEPTED("angenommen"),
    CANCELED("storniert"),
    DONE("beendet");

    private final String orderType;

    private OrderStatus(String orderType) {

        this.orderType = orderType;
    }

    public String getOrderType() {
        return this.orderType;
    }
}
