package mobilecomputing.delifast.entities;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import mobilecomputing.delifast.delifastEnum.NotificationType;

public class Notification extends DelifastEntity {

    private String userId;
    private NotificationType type;
    @ServerTimestamp
    private Date notificationTime;

    public Notification(String userId, NotificationType type, String message, Date notificationTime) {
        this.userId = userId;
        this.type = type;
        this.notificationTime = notificationTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
