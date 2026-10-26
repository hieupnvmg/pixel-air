package dev.pixelair.launcher;
import android.content.SharedPreferences;
final class LauncherDefaults {
 static void apply(SharedPreferences prefs){if(!prefs.getBoolean("defaults09",false))prefs.edit().putInt("drawerStyle",0).putInt("drawerOpacity",80).putBoolean("sortByUsage",true).putBoolean("groupByCategory",true).putBoolean("defaults09",true).apply();}
}
