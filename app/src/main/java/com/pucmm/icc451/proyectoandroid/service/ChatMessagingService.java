package com.pucmm.icc451.proyectoandroid.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pucmm.icc451.proyectoandroid.enums.Extras;
import com.pucmm.icc451.proyectoandroid.repository.AuthRepository;
import com.pucmm.icc451.proyectoandroid.ui.ChatActivity;
import com.pucmm.icc451.proyectoandroid.ui.ChatsListActivity;

import java.util.Map;

public class ChatMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "chat_messages_channel";
    private AuthRepository authRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        authRepository = new AuthRepository();
        createNotificationChannel();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        FirebaseUser user = authRepository.getCurrentUser();
        if (user != null) {
            authRepository.saveDeviceToken(user.getUid(), token);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> data = message.getData();
        String title = data.get("title");
        String body = data.get("body");

        if (message.getNotification() != null) {
            if (title == null) title = message.getNotification().getTitle();
            if (body == null) body = message.getNotification().getBody();
        }

        if (title == null) title = "Nuevo mensaje";
        if (body == null) body = "";

        showNotification(title, body, data.get("senderId"), data.get("senderName"));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Mensajes de chat",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String body, String senderId, String senderName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        int notificationId = (int) System.currentTimeMillis();

        // Si el mensaje trae al remitente, abre ese chat; si no, la lista de chats
        Intent intent;
        if (senderId != null) {
            intent = new Intent(this, ChatActivity.class);
            intent.putExtra(Extras.EXTRA_TARGET_USER_ID.name(), senderId);
            intent.putExtra(Extras.EXTRA_TARGET_USER_NAME.name(), senderName);
        } else {
            intent = new Intent(this, ChatsListActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                notificationId,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // cámbialo por tu propio icono
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(notificationId, builder.build());
    }
}
