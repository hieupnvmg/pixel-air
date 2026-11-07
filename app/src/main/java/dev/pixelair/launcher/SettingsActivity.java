package dev.pixelair.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.role.RoleManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.*;

/** Dedicated launcher preferences; shared with Home without resetting its workspace. */
public final class SettingsActivity extends Activity {
    private SharedPreferences prefs;
    private LinearLayout body;
    private LauncherPalette palette = LauncherPalette.classic();
    private int ink = palette.settingsText, muted = palette.settingsMuted, accent = palette.primary;

    private int dp(float n) { return Math.round(n * getResources().getDisplayMetrics().density); }
    private GradientDrawable background(int color, int radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }
    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color); return t;
    }
    @Override public void onCreate(Bundle state) {
        prefs = getSharedPreferences("home", 0);
        palette = LauncherPalette.load(this, prefs); setTheme(palette.theme());
        ink = palette.settingsText; muted = palette.settingsMuted; accent = palette.primary;
        super.onCreate(state);
        LauncherDefaults.apply(prefs);
        getWindow().setStatusBarColor(palette.settingsBackground);
        getWindow().setNavigationBarColor(palette.settingsBackground);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(palette.settingsBackground);
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            android.graphics.Insets bars = insets.getInsets(WindowInsets.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom); return insets;
        });
        setContentView(root);
        LinearLayout toolbar = new LinearLayout(this); toolbar.setGravity(Gravity.CENTER_VERTICAL);
        TextView back = text("‹", 34, accent); back.setGravity(Gravity.CENTER); back.setContentDescription(getString(R.string.ui_068));
        toolbar.addView(back, new LinearLayout.LayoutParams(dp(56), dp(56))); back.setOnClickListener(v -> finish());
        TextView title = text(getString(R.string.ui_025), 21, ink); title.setTypeface(null, Typeface.BOLD);
        toolbar.addView(title, new LinearLayout.LayoutParams(0, dp(56), 1)); title.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(toolbar);
        ScrollView scroll = new ScrollView(this); scroll.setVerticalScrollBarEnabled(false); scroll.setFillViewport(true);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        body = new LinearLayout(this); body.setOrientation(LinearLayout.VERTICAL); body.setPadding(dp(20), dp(8), dp(20), dp(24));
        scroll.addView(body);
        TextView subtitle = text(getString(R.string.ui_069), 14, muted);
        subtitle.setPadding(dp(2), 0, 0, dp(16)); body.addView(subtitle);

        section(getString(R.string.ui_070));
        LinearLayout appearance = card();
        switchRow(appearance, getString(R.string.ui_071), getString(R.string.ui_072), "labels", true);
        divider(appearance);
        switchRow(appearance, getString(R.string.ui_073), getString(R.string.ui_074), "showPageDots", true);
        divider(appearance);
        addSearchPosition(appearance);
        divider(appearance);addIconSize(appearance);
        divider(appearance);
        Switch dynamicColor = switchRow(appearance, getString(R.string.dynamic_color_title), getString(R.string.dynamic_color_detail), LauncherPalette.PREF_KEY, LauncherPalette.DEFAULT_DYNAMIC);
        dynamicColor.setOnCheckedChangeListener((button, checked) -> { prefs.edit().putBoolean(LauncherPalette.PREF_KEY, checked).apply(); recreate(); });
        divider(appearance);
        switchRow(appearance, getString(R.string.drawer_backdrop_title), getString(R.string.drawer_backdrop_detail), DrawerBackdrop.PREF_KEY, DrawerBackdrop.DEFAULT);

        section(getString(R.string.ui_075));
        LinearLayout icons = card();
        TextView selectedPack = text(iconPackLabel(), 14, accent);
        LinearLayout choosePack = labels(getString(R.string.ui_076), getString(R.string.ui_077)); choosePack.addView(selectedPack);
        choosePack.setOnClickListener(v -> chooseIconPack(selectedPack)); icons.addView(choosePack);
        divider(icons);
        actionRow(icons, getString(R.string.ui_078), getString(R.string.ui_079), () -> {
            Intent market = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("market://search?q=icon%20pack&c=apps"));
            try { startActivity(market); } catch (Exception e) { safe(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/search?q=icon%20pack&c=apps"))); }
        });
        divider(icons);
        switchRow(icons, getString(R.string.themed_icons_title), getString(R.string.themed_icons_detail), ThemedIcons.PREF_KEY, ThemedIcons.DEFAULT);
        divider(icons);
        switchRow(icons, getString(R.string.dots_title), getString(R.string.dots_detail), NotificationDots.PREF_KEY, NotificationDots.DEFAULT);
        dotsAccess = text("", 13, accent); dotsAccess.setPadding(0, 0, 0, dp(12)); icons.addView(dotsAccess);
        dotsAccess.setOnClickListener(v -> openNotificationAccess());
        updateDotsAccess();

        section(getString(R.string.ui_080));
        LinearLayout motion = card();
        switchRow(motion,getString(R.string.unlock_animation_title),getString(R.string.unlock_animation_detail),"unlockAnimation",true);divider(motion);
        choiceRow(motion, getString(R.string.ui_081), getString(R.string.ui_082),
                "motionStyle", new String[]{getString(R.string.ui_083), getString(R.string.ui_084), getString(R.string.ui_085)}, 1);
        divider(motion);
        choiceRow(motion, getString(R.string.ui_086), getString(R.string.ui_087),
                "swipeSensitivity", new String[]{getString(R.string.ui_088), getString(R.string.ui_084), getString(R.string.ui_089)}, 1);
        divider(motion);
        choiceRow(motion, getString(R.string.gesture_swipe_down_title), getString(R.string.gesture_swipe_down_detail),
                "swipeDownAction", new String[]{getString(R.string.gesture_none), getString(R.string.gesture_notifications)}, 1);
        divider(motion);
        Switch doubleTap = switchRow(motion, getString(R.string.gesture_lock_title), getString(R.string.gesture_lock_detail), "doubleTapLock", false);
        doubleTap.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean("doubleTapLock", checked).apply();
            if (checked && !LauncherAccessibility.enabled(this))
                new AlertDialog.Builder(this).setTitle(getString(R.string.gesture_lock_title)).setMessage(getString(R.string.gesture_lock_needs_service))
                        .setPositiveButton(getString(R.string.gesture_open_accessibility), (d, w) -> HomeActivity.openAccessibilitySettings(this))
                        .setNegativeButton(getString(R.string.ui_035), null).show();
        });

        section(getString(R.string.ui_090));
        LinearLayout apps = card();
        choiceRow(apps,getString(R.string.drawer_scroll_title),getString(R.string.drawer_scroll_detail),"drawerOrientation",
                new String[]{getString(R.string.drawer_vertical),getString(R.string.drawer_horizontal)},0);
        divider(apps);
        addDrawerAppearance(apps);
        divider(apps);
        LinearLayout sort = labels(getString(R.string.ui_008), getString(R.string.ui_091)); apps.addView(sort);
        RadioGroup group = new RadioGroup(this);
        RadioButton abc = radio(getString(R.string.ui_032)), frequent = radio(getString(R.string.ui_033)), categories=radio(getString(R.string.ui_034));categories.setId(View.generateViewId());
        abc.setId(View.generateViewId()); frequent.setId(View.generateViewId());
        group.addView(abc); group.addView(frequent);group.addView(categories); group.check(prefs.getBoolean("groupByCategory",true)?categories.getId():prefs.getBoolean("sortByUsage", true) ? frequent.getId() : abc.getId());
        group.setOnCheckedChangeListener((g, id) -> prefs.edit().putBoolean("sortByUsage", id != abc.getId()).putBoolean("groupByCategory",id==categories.getId()).apply());
        apps.addView(group);
        divider(apps);actionRow(apps,getString(R.string.ui_092),getString(R.string.ui_093),()->action("categories"));
        divider(apps);actionRow(apps, getString(R.string.hidden_apps_title), getString(R.string.hidden_apps_detail), this::chooseHiddenApps);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            divider(apps);switchRow(apps, getString(R.string.private_space), getString(R.string.private_space_detail), PrivateSpace.PREF_KEY, true);
        }

        section(getString(R.string.search_section));
        LinearLayout search = card();
        switchRow(search, getString(R.string.search_shortcuts), getString(R.string.search_shortcuts_detail), "searchShortcuts", true);
        divider(search);
        switchRow(search, getString(R.string.search_settings), getString(R.string.search_settings_detail), "searchSettings", true);
        divider(search);
        contactsSwitch = switchRow(search, getString(R.string.search_contacts), getString(R.string.search_contacts_detail), "searchContacts", false);
        if (contactsSwitch.isChecked() && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != android.content.pm.PackageManager.PERMISSION_GRANTED) contactsSwitch.setChecked(false);
        contactsSwitch.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean("searchContacts", checked).apply();
            if (checked && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != android.content.pm.PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, CONTACTS);
        });
        divider(search);
        switchRow(search, getString(R.string.search_web), getString(R.string.search_web_detail), "searchWeb", true);

        section(getString(R.string.ui_094));
        LinearLayout workspace = card();
        actionRow(workspace, getString(R.string.ui_095), getString(R.string.ui_096), () -> action("addPage"));
        divider(workspace);
        actionRow(workspace, getString(R.string.ui_097), getString(R.string.ui_098), () -> action("removePage"));
        divider(workspace);
        actionRow(workspace, getString(R.string.ui_099), getString(R.string.ui_100), () -> action("editSlots"));
        divider(workspace);
        actionRow(workspace, getString(R.string.ui_056), getString(R.string.ui_101), () -> action("addWidget"));
        divider(workspace);
        actionRow(workspace,getString(R.string.ui_055),getString(R.string.ui_102),()->action("moveWidget"));
        divider(workspace);
        actionRow(workspace, getString(R.string.ui_061), getString(R.string.ui_103), () -> action("removeWidget"));

        section(getString(R.string.backup_section));
        LinearLayout backup = card();
        actionRow(backup, getString(R.string.backup_title), getString(R.string.backup_detail), this::startBackup);
        divider(backup);
        actionRow(backup, getString(R.string.restore_title), getString(R.string.restore_detail), this::startRestore);

        section(getString(R.string.ui_104));
        LinearLayout system = card();
        actionRow(system, getString(R.string.ui_105), getString(R.string.ui_106), () -> {
            RoleManager rm = getSystemService(RoleManager.class);
            if (rm.isRoleAvailable(RoleManager.ROLE_HOME) && !rm.isRoleHeld(RoleManager.ROLE_HOME))
                startActivityForResult(rm.createRequestRoleIntent(RoleManager.ROLE_HOME), 10);
            else safe(new Intent(Settings.ACTION_HOME_SETTINGS));
        });
        divider(system);
        actionRow(system, getString(R.string.ui_044), getString(R.string.ui_107), () -> safe(new Intent(Intent.ACTION_SET_WALLPAPER)));
        TextView footer = text(getString(R.string.ui_108), 12, muted);
        footer.setGravity(Gravity.CENTER); footer.setPadding(0, dp(24), 0, dp(8)); body.addView(footer);
        root.requestApplyInsets();
        getOnBackInvokedDispatcher().registerOnBackInvokedCallback(0, this::finish);
    }
    private static final int BACKUP = 30, RESTORE = 31, CONTACTS = 32;
    private Switch contactsSwitch;
    @Override public void onRequestPermissionsResult(int request, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(request, permissions, results);
        if (request != CONTACTS || contactsSwitch == null) return;
        boolean granted = results.length > 0 && results[0] == android.content.pm.PackageManager.PERMISSION_GRANTED;
        if (!granted) { contactsSwitch.setChecked(false); Toast.makeText(this, getString(R.string.search_contacts_denied), Toast.LENGTH_LONG).show(); }
    }
    /** Apps hidden here leave the drawer but stay on Home and in search. */
    private void chooseHiddenApps() {
        android.content.pm.PackageManager pm = getPackageManager();
        java.util.ArrayList<String[]> entries = new java.util.ArrayList<>();
        for (android.content.pm.ResolveInfo r : pm.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0)) {
            if (r.activityInfo.packageName.equals(getPackageName())) continue;
            entries.add(new String[]{new android.content.ComponentName(r.activityInfo.packageName, r.activityInfo.name).flattenToString(), r.loadLabel(pm).toString()});
        }
        java.text.Collator collator = java.text.Collator.getInstance(); entries.sort((a, b) -> collator.compare(a[1], b[1]));
        String[] labels = new String[entries.size()]; boolean[] checked = new boolean[entries.size()];
        for (int n = 0; n < entries.size(); n++) { labels[n] = entries.get(n)[1]; checked[n] = prefs.getBoolean("hidden:" + entries.get(n)[0], false); }
        new AlertDialog.Builder(this).setTitle(getString(R.string.hidden_apps_title))
                .setMultiChoiceItems(labels, checked, (d, which, value) -> checked[which] = value)
                .setPositiveButton(getString(R.string.ui_057), (d, w) -> {
                    SharedPreferences.Editor edit = prefs.edit();
                    for (int n = 0; n < entries.size(); n++) { String key = "hidden:" + entries.get(n)[0]; if (checked[n]) edit.putBoolean(key, true); else edit.remove(key); }
                    edit.apply();
                })
                .setNegativeButton(getString(R.string.ui_035), null).show();
    }
    private void startBackup() {
        String day = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ROOT).format(new java.util.Date());
        Intent create = new Intent(Intent.ACTION_CREATE_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE)
                .setType("application/json").putExtra(Intent.EXTRA_TITLE, "pixel-air-" + day + ".json");
        try { startActivityForResult(create, BACKUP); } catch (Exception e) { safe(create); }
    }
    private void startRestore() {
        Intent open = new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE)
                .setType("*/*").putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/json", "text/plain", "application/octet-stream"});
        try { startActivityForResult(open, RESTORE); } catch (Exception e) { safe(open); }
    }
    @Override protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (result != RESULT_OK || data == null || data.getData() == null) return;
        if (request == BACKUP) writeBackup(data.getData());
        else if (request == RESTORE) readBackup(data.getData());
    }
    /** Writes every portable "home" preference with its type, so a restore puts back exactly the same values. */
    private void writeBackup(android.net.Uri target) {
        try {
            org.json.JSONObject values = new org.json.JSONObject();
            for (java.util.Map.Entry<String, ?> entry : prefs.getAll().entrySet()) {
                if (!LayoutBackup.portable(entry.getKey()) || entry.getValue() == null) continue;
                Object value = entry.getValue(); String type;
                if (value instanceof String) type = "string"; else if (value instanceof Integer) type = "int";
                else if (value instanceof Long) type = "long"; else if (value instanceof Float) type = "float";
                else if (value instanceof Boolean) type = "boolean";
                else if (value instanceof java.util.Set) { type = "set"; value = new org.json.JSONArray((java.util.Set<?>) value); }
                else continue;
                values.put(entry.getKey(), new org.json.JSONObject().put("type", type).put("value", value));
            }
            org.json.JSONObject file = new org.json.JSONObject().put("app", LayoutBackup.APP).put("format", LayoutBackup.FORMAT)
                    .put("created", System.currentTimeMillis()).put("prefs", values);
            try (java.io.OutputStream out = getContentResolver().openOutputStream(target, "wt")) {
                if (out == null) throw new java.io.IOException("No output stream");
                out.write(file.toString(1).getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            Toast.makeText(this, getString(R.string.backup_done), Toast.LENGTH_SHORT).show();
        } catch (Exception e) { Toast.makeText(this, getString(R.string.backup_failed), Toast.LENGTH_LONG).show(); }
    }
    private void readBackup(android.net.Uri source) {
        org.json.JSONObject values;
        try (java.io.InputStream in = getContentResolver().openInputStream(source)) {
            if (in == null) throw new java.io.IOException("No input stream");
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream(); byte[] chunk = new byte[8192]; int n;
            while ((n = in.read(chunk)) > 0) { buffer.write(chunk, 0, n); if (buffer.size() > 4 << 20) throw new java.io.IOException("Too large"); }
            org.json.JSONObject file = new org.json.JSONObject(buffer.toString("UTF-8"));
            if (!LayoutBackup.APP.equals(file.optString("app")) || file.optInt("format") != LayoutBackup.FORMAT) throw new IllegalArgumentException("Not a Pixel Air backup");
            values = file.getJSONObject("prefs");
        } catch (Exception e) { Toast.makeText(this, getString(R.string.restore_invalid), Toast.LENGTH_LONG).show(); return; }
        new AlertDialog.Builder(this).setTitle(getString(R.string.restore_title)).setMessage(getString(R.string.restore_confirm))
                .setPositiveButton(getString(R.string.restore_apply), (d, w) -> applyBackup(values))
                .setNegativeButton(getString(R.string.ui_035), null).show();
    }
    /** Replaces portable preferences with the backup's; widget state stays as it is on this phone. */
    private void applyBackup(org.json.JSONObject values) {
        SharedPreferences.Editor edit = prefs.edit();
        try {
            for (String key : prefs.getAll().keySet()) if (LayoutBackup.portable(key)) edit.remove(key);
            java.util.ArrayList<String> keys = new java.util.ArrayList<>(); for (java.util.Iterator<String> it = values.keys(); it.hasNext(); ) keys.add(it.next());
            for (String key : LayoutBackup.restorable(keys)) {
                org.json.JSONObject entry = values.getJSONObject(key);
                switch (entry.getString("type")) {
                    case "string": edit.putString(key, entry.getString("value")); break;
                    case "int": edit.putInt(key, entry.getInt("value")); break;
                    case "long": edit.putLong(key, entry.getLong("value")); break;
                    case "float": edit.putFloat(key, (float) entry.getDouble("value")); break;
                    case "boolean": edit.putBoolean(key, entry.getBoolean("value")); break;
                    case "set": { org.json.JSONArray array = entry.getJSONArray("value"); java.util.HashSet<String> set = new java.util.HashSet<>();
                        for (int n = 0; n < array.length(); n++) set.add(array.getString(n)); edit.putStringSet(key, set); break; }
                    default: break;
                }
            }
        } catch (Exception e) { Toast.makeText(this, getString(R.string.restore_invalid), Toast.LENGTH_LONG).show(); return; }
        edit.apply(); // in memory at once, so the recreated Home reads the restored values
        Toast.makeText(this, getString(R.string.restore_done), Toast.LENGTH_SHORT).show();
        action("reload");
    }
    private TextView dotsAccess;
    @Override protected void onResume() { super.onResume(); updateDotsAccess(); }
    private void updateDotsAccess() {
        if (dotsAccess == null) return;
        boolean granted = NotificationDots.accessGranted(this);
        dotsAccess.setText(getString(granted ? R.string.dots_access_granted : R.string.dots_access_needed));
        dotsAccess.setTextColor(granted ? muted : accent); dotsAccess.setClickable(true);
    }
    private void openNotificationAccess() {
        Intent detail = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS)
                .putExtra(Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME, new android.content.ComponentName(this, NotificationDots.class).flattenToString());
        try { startActivity(detail); } catch (Exception e) { safe(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)); }
    }
    private void addIconSize(LinearLayout parent){parent.addView(labels(getString(R.string.ui_109),getString(R.string.ui_110)));ImageView preview=new ImageView(this);preview.setImageDrawable(getDrawable(R.drawable.ic_launcher));LinearLayout.LayoutParams imageSize=new LinearLayout.LayoutParams(dp(prefs.getInt("iconSize",54)),dp(prefs.getInt("iconSize",54)));imageSize.gravity=Gravity.CENTER;parent.addView(preview,imageSize);TextView value=text(prefs.getInt("iconSize",54)+" dp",14,accent);value.setGravity(Gravity.CENTER);parent.addView(value);SeekBar seek=new SeekBar(this);seek.setMax(28);seek.setProgress(prefs.getInt("iconSize",54)-36);seek.setContentDescription(getString(R.string.ui_109));parent.addView(seek,new LinearLayout.LayoutParams(-1,dp(48)));seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar bar,int p,boolean user){int size=p+36;value.setText(size+" dp");preview.getLayoutParams().width=dp(size);preview.getLayoutParams().height=dp(size);preview.requestLayout();if(user)prefs.edit().putInt("iconSize",size).apply();}public void onStartTrackingTouch(SeekBar b){}public void onStopTrackingTouch(SeekBar b){}});}
    private void addDrawerAppearance(LinearLayout parent) {
        parent.addView(labels(getString(R.string.ui_111), getString(R.string.ui_112)));
        RadioGroup style = new RadioGroup(this); RadioButton dark = radio(getString(R.string.ui_113)), glass = radio(getString(R.string.ui_114));
        dark.setId(View.generateViewId()); glass.setId(View.generateViewId()); style.addView(dark); style.addView(glass);
        boolean glassMode = prefs.getInt("drawerStyle",0)==1; style.check(glassMode?glass.getId():dark.getId()); parent.addView(style);
        TextView opacityLabel = text("",14,ink); opacityLabel.setPadding(0,dp(12),0,0); parent.addView(opacityLabel);
        SeekBar opacity = new SeekBar(this); opacity.setContentDescription(getString(R.string.ui_115)); opacity.setMax(65);
        int initialOpacity=prefs.getInt("drawerOpacity",80); opacity.setProgress(initialOpacity-35);opacityLabel.setText(getString(R.string.ui_116)+initialOpacity+"%");parent.addView(opacity,new LinearLayout.LayoutParams(-1,dp(48)));
        opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar b,int p,boolean user){opacityLabel.setText(getString(R.string.ui_116)+(p+35)+"%");if(user)prefs.edit().putInt("drawerOpacity",p+35).apply();}public void onStartTrackingTouch(SeekBar b){}public void onStopTrackingTouch(SeekBar b){}});
        TextView blurLabel=text(getString(R.string.ui_117),14,ink);parent.addView(blurLabel);
        SeekBar blur=new SeekBar(this);blur.setContentDescription(getString(R.string.ui_118));blur.setMax(80);blur.setProgress(prefs.getInt("drawerBlur",48));blur.setEnabled(glassMode);parent.addView(blur,new LinearLayout.LayoutParams(-1,dp(48)));
        blur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar b,int p,boolean user){if(user)prefs.edit().putInt("drawerBlur",p).apply();}public void onStartTrackingTouch(SeekBar b){}public void onStopTrackingTouch(SeekBar b){}});
        boolean supported=getWindowManager().isCrossWindowBlurEnabled();TextView note=text(supported?getString(R.string.ui_119):getString(R.string.ui_120),12,muted);note.setPadding(0,0,0,dp(12));parent.addView(note);
        style.setOnCheckedChangeListener((g,id)->{boolean enabled=id==glass.getId();prefs.edit().putInt("drawerStyle",enabled?1:0).apply();blur.setEnabled(enabled);if(!prefs.contains("drawerOpacity"))opacity.setProgress(80-35);});
    }
    private String iconPackLabel() {
        String selected = prefs.getString("iconPack", "");
        if (selected.isEmpty()) return getString(R.string.ui_121);
        try { return getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(selected, 0)).toString(); }
        catch (Exception e) { return getString(R.string.ui_122); }
    }
    private void chooseIconPack(TextView current) {
        java.util.List<IconPackManager.Pack> packs = IconPackManager.installed(this);
        if (packs.isEmpty()) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.ui_123))
                    .setMessage(getString(R.string.ui_124))
                    .setPositiveButton(getString(R.string.ui_125), (d,w)->{prefs.edit().putString("iconPack", "").apply();current.setText(getString(R.string.ui_121));})
                    .setNegativeButton(getString(R.string.ui_035),null).show(); return;
        }
        String[] labels = new String[packs.size()+1]; labels[0] = getString(R.string.ui_121);
        int selected = 0; String pkg = prefs.getString("iconPack", "");
        for (int n=0;n<packs.size();n++) { labels[n+1]=packs.get(n).label; if (packs.get(n).packageName.equals(pkg)) selected=n+1; }
        new AlertDialog.Builder(this).setTitle(getString(R.string.ui_123))
                .setSingleChoiceItems(labels,selected,(dialog,which)->{
                    prefs.edit().putString("iconPack", which==0?"":packs.get(which-1).packageName).apply();
                    current.setText(labels[which]); dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.ui_035),null).show();
    }
    private void section(String name) {
        TextView label = text(name, 13, accent); label.setTypeface(null, Typeface.BOLD);
        label.setPadding(dp(4), dp(16), 0, dp(10)); body.addView(label);
    }
    private LinearLayout card() {
        LinearLayout card = new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(8), dp(16), dp(8)); card.setBackground(background(palette.card, 22));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2); p.bottomMargin = dp(4); body.addView(card, p); return card;
    }
    private LinearLayout labels(String title, String detail) {
        LinearLayout labels = new LinearLayout(this); labels.setOrientation(LinearLayout.VERTICAL); labels.setPadding(0, dp(12), 0, dp(12));
        labels.addView(text(title, 16, ink)); TextView sub = text(detail, 12, muted); sub.setPadding(0, dp(5), 0, 0); labels.addView(sub); return labels;
    }
    private void divider(LinearLayout parent) { View line = new View(this); line.setBackgroundColor(palette.divider); parent.addView(line, new LinearLayout.LayoutParams(-1, dp(1))); }
    private Switch switchRow(LinearLayout parent, String title, String detail, String key, boolean defaultValue) {
        LinearLayout row = new LinearLayout(this); row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(labels(title, detail), new LinearLayout.LayoutParams(0, -2, 1));
        Switch toggle = new Switch(this); toggle.setContentDescription(title); toggle.setChecked(prefs.getBoolean(key, defaultValue));
        toggle.setOnCheckedChangeListener((button, checked) -> prefs.edit().putBoolean(key, checked).apply()); row.addView(toggle);
        row.setOnClickListener(v -> toggle.setChecked(!toggle.isChecked())); parent.addView(row); return toggle;
    }
    private RadioButton radio(String label) { RadioButton r = new RadioButton(this); r.setText(label); r.setTextColor(ink); r.setTextSize(15); r.setPadding(0, dp(4), 0, dp(4)); r.setMinimumHeight(dp(48)); return r; }
    private void choiceRow(LinearLayout parent, String title, String detail, String key, String[] choices, int defaultValue) {
        LinearLayout row = new LinearLayout(this); row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(labels(title, detail), new LinearLayout.LayoutParams(0, -2, 1));
        int initial = Math.max(0, Math.min(choices.length - 1, prefs.getInt(key, defaultValue)));
        TextView current = text(choices[initial], 14, accent); current.setPadding(dp(12), 0, 0, 0); row.addView(current); row.setContentDescription(title);
        row.setOnClickListener(v -> new AlertDialog.Builder(this).setTitle(title)
                .setSingleChoiceItems(choices, prefs.getInt(key, defaultValue), (d, which) -> {
                    prefs.edit().putInt(key, which).apply(); current.setText(choices[which]); d.dismiss();
                }).setNegativeButton(getString(R.string.ui_035), null).show()); parent.addView(row);
    }
    private void addSearchPosition(LinearLayout parent) {
        parent.addView(labels(getString(R.string.ui_126), getString(R.string.ui_127)));
        SeekBar seek = new SeekBar(this); seek.setMax(36); seek.setContentDescription(getString(R.string.ui_126));
        seek.setProgress(Math.max(0, Math.min(36, prefs.getInt("searchBottomGap", 24) - 12)));
        parent.addView(seek, new LinearLayout.LayoutParams(-1, dp(48)));
        LinearLayout ends = new LinearLayout(this); TextView low = text(getString(R.string.ui_128), 12, muted), high = text(getString(R.string.ui_129), 12, muted);
        high.setGravity(Gravity.END); ends.addView(low, new LinearLayout.LayoutParams(0, -2, 1)); ends.addView(high, new LinearLayout.LayoutParams(0, -2, 1)); parent.addView(ends);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) { if (fromUser) prefs.edit().putInt("searchBottomGap", progress + 12).apply(); }
            public void onStartTrackingTouch(SeekBar bar) {} public void onStopTrackingTouch(SeekBar bar) {}
        });
    }
    private void actionRow(LinearLayout parent, String title, String detail, Runnable action) {
        LinearLayout row = labels(title, detail); row.setContentDescription(title); row.setOnClickListener(v -> action.run()); parent.addView(row);
    }
    private void action(String value) { setResult(RESULT_OK, new Intent().putExtra("action", value)); finish(); }
    private void safe(Intent intent) { try { startActivity(intent); } catch (Exception e) { Toast.makeText(this, getString(R.string.ui_130), Toast.LENGTH_SHORT).show(); } }
}
