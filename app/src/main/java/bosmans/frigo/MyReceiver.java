package bosmans.frigo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent it = new Intent(context, MainActivity.class);
        createNotification(context, it, "new mensage", "body!", "this is a mensage");
        addNotification(context, intent);

    }


    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence descricao) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.applogopngdraw);
        builder.setContentIntent(p);
        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.applogopngdraw, n);
        //create a vibration
        try {

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {
        }
    }

    public void addNotification(Context context, Intent intent) {

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }

        }


        Intent resultIntent = new Intent(context, LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("TITLE")
                .setContentText("SUB-TITLE")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID. This is required for compatibility with Android 8.0 (API level 26) and higher, but is ignored by older versions By default, the notification's text content is truncated to fit one line. If you want your notification to be longer, you can enable an expandable notification by adding a style template with setStyle(). For example, the following code creates a larger text area"))
                .setSmallIcon(R.drawable.applogopngdraw)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(android.R.color.holo_red_dark))
                .addAction(R.drawable.ic_launcher_foreground, "Call", resultPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "More", resultPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "And more", resultPendingIntent);


        if (notificationManager != null) {

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

    }
}