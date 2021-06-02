package mobilecomputing.delifast.entities;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mobilecomputing.delifast.delifastEnum.NotificationType;
import mobilecomputing.delifast.others.CurrencyFormatter;
import mobilecomputing.delifast.others.DelifastConstants;

@IgnoreExtraProperties
public class Notification extends DelifastEntity {

    private String userId;
    private String orderId;
    @Exclude
    private String text;
    private NotificationType type;
    @ServerTimestamp
    private Date notificationTime;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DelifastConstants.TIMEFORMAT, Locale.GERMANY);

    public Notification() {

    }

    public Notification(String userId, String orderId, NotificationType type) {
        this.userId = userId;
        this.orderId = orderId;
        this.type = type;

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
    @Exclude
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public void createText(Order order) {
        switch (type) {
            case ORDER_ACCEPTED_BY_SUPPLIER:
                this.text = "Ihre Bestellung vom " + simpleDateFormat.format(order.getOrderTime()) + " wurde akzeptiert und nun auf dem Weg zu Ihnen.";
                break;
            case ORDER_CANCELED_BY_SUPPLIER:
                this.text = "Der Lieferant hat Ihre Bestellung vom " + simpleDateFormat.format(order.getOrderTime()) + " leider gecancelt.";
                break;
            case ORDER_DONE_BY_CUSTOMER:
                this.text = "Ihr Auftrag vom " + simpleDateFormat.format(order.getOrderTime()) + " wurde erfolgreich abgeschlossen. Sie erhalten " + CurrencyFormatter.doubleToUIRep(order.getCustomerFee()) + "€ in kürze.";
                break;
            case RATING_CUSTOMER:
                this.text = "Ihre Bestellung vom " + simpleDateFormat.format(order.getOrderTime()) + " wurde erfolgreich abgeschlossen. Wenn Sie möchten, können Sie den Lieferanten bewerten.";
                break;
            case RATING_SUPPLIER:
                this.text = "Ihr Auftrag vom " + simpleDateFormat.format(order.getOrderTime()) + " wurde erfolgreich abgeschlossen. Wenn Sie möchten, können Sie den Auftraggeber bewerten.";
                break;
            default:
                this.text = "Notification Fehler, entschuldigen Sie die Störung.";
        }
    }


}
