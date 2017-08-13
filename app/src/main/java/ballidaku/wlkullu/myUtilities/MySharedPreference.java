package ballidaku.wlkullu.myUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sharanpalsingh on 16/07/17.
 */

public class MySharedPreference {

    public final String PreferenceName = "MySharedPreference";


    public MySharedPreference()
    {
    }

    public static MySharedPreference   instance = new MySharedPreference();

    public static MySharedPreference getInstance()
    {
        return instance;
    }

    public SharedPreferences getPreference(Context context)
    {
        return context.getSharedPreferences(PreferenceName, Activity.MODE_PRIVATE);
    }

    public void saveHeading(Context context, String heading)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(MyConstants.HEADING, heading);
        editor.apply();
    }


    public String getHeading(Context context)
    {
        return getPreference(context).getString(MyConstants.HEADING,"");
    }

    public boolean isLogined(Context context)
    {
        return getPreference(context).getBoolean(MyConstants.IS_LOGIN,false);
    }


    public void setLogin(Context context,boolean isLogin)
    {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(MyConstants.IS_LOGIN, isLogin);
        editor.apply();
    }
}
