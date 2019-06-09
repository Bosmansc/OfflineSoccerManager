package bosmans.frigo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jaison on 17/06/17.
 */

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

                NotificationScheduler.setReminder(context, AlarmReceiver.class, LoginActivity.hours, LoginActivity.minutes);
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.addNotification(context, MainActivity.class,
                "You have 5 unwatched videos", "Watch them now?");

    }
}
