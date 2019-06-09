package bosmans.frigo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");

                int hours = 12;
                int minutes = 14;

                NotificationScheduler.setReminder(context, AlarmReceiver.class, MainActivity.hours, MainActivity.minutes);
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.addNotification(context, MainActivity.class);
       // NotificationScheduler.getGame(context, MainActivity.class, "Online Soccer manager", "Next game");

    }
}
