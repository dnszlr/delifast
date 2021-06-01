package mobilecomputing.delifast.entities;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import mobilecomputing.delifast.delifastEnum.NotificationType;
import mobilecomputing.delifast.others.CurrencyFormatter;

@IgnoreExtraProperties
public class Notification extends DelifastEntity {

    private String userId;
    private String orderId;
    @Exclude
    private String text;
    private NotificationType type;
    @ServerTimestamp
    private Date notificationTime;

    public Notification() {

    }

    public Notification(String userId, String orderId, NotificationType type, String message, Date notificationTime) {
        this.userId = userId;
        this.orderId = orderId;
        this.type = type;
        this.notificationTime = notificationTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getText() {
        return this.text;
    }

    public void setText(Order order) {
        switch (type) {
            case ORDER_ACCEPTED_BY_SUPPLIER:
                this.text = "Ihre Bestellung vom " + order.getOrderTime() + " wurde akzeptiert und wird nun ausgeführt.";
                break;
            case ORDER_CANCELED_BY_SUPPLIER:
                this.text = "Der Lieferant hat Ihre Bestellung vom " + order.getOrderTime() + " leider gecancelt.";
                break;
            case ORDER_DONE_BY_CUSTOMER:
                this.text = "Ihr Auftrag vom " + order.getOrderTime() + " wurde erfolgreich abgeschlossen. Sie erhalten " + CurrencyFormatter.doubleToUIRep(order.getCustomerFee()) + "€ in kürze.";
                break;
            case RATING_CUSTOMER:
                this.text = "Ihre Bestellung vom " + order.getOrderTime() + " wurde erfolgreich abgeschlossen. Wenn Sie möchten, können Sie den Lieferanten bewerten.";
                break;
            case RATING_SUPPLIER:
                this.text = "Ihr Auftrag vom " + order.getOrderTime() + " wurde erfolgreich abgeschlossen. Wenn Sie möchten, können Sie den Auftraggeber bewerten.";
                break;
            default:
                this.text = "Notification Fehler, entschuldigen Sie die Störung.";
        }
    }


}
