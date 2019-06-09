package bosmans.frigo;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {

    private static final String APP_SHARED_PREFS = "RemindMePref";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private static final String reminderStatus="reminderStatus";
    private static final String hour="hour";
    private static final String min="min";
    private static final String username="username";
    private static final String password="password";

    public LocalData(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    // Settings Page Set Reminder

    public boolean getReminderStatus()
    {
        return appSharedPrefs.getBoolean(reminderStatus, false);
    }

    public void setReminderStatus(boolean status)
    {
        prefsEditor.putBoolean(reminderStatus, status);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int get_hour()
    {
        return appSharedPrefs.getInt(hour, 20);
    }

    public void set_hour(int h)
    {
        prefsEditor.putInt(hour, h);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_min()
    {
        return appSharedPrefs.getInt(min, 0);
    }

    public void set_min(int m)
    {
        prefsEditor.putInt(min, m);
        prefsEditor.commit();
    }

    // Settings Page userame

    public String get_username()
    {
        return appSharedPrefs.getString(username, "");
    }

    public void set_usernmae(String un)
    {
        prefsEditor.putString(username, un);
        prefsEditor.commit();
    }

    // Settings Page password

    public String get_password()
    {
        return appSharedPrefs.getString(password, "");
    }

    public void set_password(String pw)
    {
        prefsEditor.putString(password, pw);
        prefsEditor.commit();
    }

    // reset

    public void reset()
    {
        prefsEditor.clear();
        prefsEditor.commit();

    }

}
