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
    private final int ink = 0xFFE8EDF8, muted = 0xFFAAB4C9, accent = 0xFFBDD5FF;

    private int dp(float n) { return Math.round(n * getResources().getDisplayMetrics().density); }
    private GradientDrawable background(int color, int radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }
    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color); return t;
    }
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("home", 0);LauncherDefaults.apply(prefs);
        getWindow().setStatusBarColor(0xFF10151F);
        getWindow().setNavigationBarColor(0xFF10151F);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(0xFF10151F);
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

        section(getString(R.string.ui_080));
        LinearLayout motion = card();
        switchRow(motion,getString(R.string.unlock_animation_title),getString(R.string.unlock_animation_detail),"unlockAnimation",true);divider(motion);
        choiceRow(motion, getString(R.string.ui_081), getString(R.string.ui_082),
                "motionStyle", new String[]{getString(R.string.ui_083), getString(R.string.ui_084), getString(R.string.ui_085)}, 1);
        divider(motion);
        choiceRow(motion, getString(R.string.ui_086), getString(R.string.ui_087),
                "swipeSensitivity", new String[]{getString(R.string.ui_088), getString(R.string.ui_084), getString(R.string.ui_089)}, 1);

        section(getString(R.string.ui_090));
        LinearLayout apps = card();
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
        card.setPadding(dp(16), dp(8), dp(16), dp(8)); card.setBackground(background(0xFF1C2433, 22));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2); p.bottomMargin = dp(4); body.addView(card, p); return card;
    }
    private LinearLayout labels(String title, String detail) {
        LinearLayout labels = new LinearLayout(this); labels.setOrientation(LinearLayout.VERTICAL); labels.setPadding(0, dp(12), 0, dp(12));
        labels.addView(text(title, 16, ink)); TextView sub = text(detail, 12, muted); sub.setPadding(0, dp(5), 0, 0); labels.addView(sub); return labels;
    }
    private void divider(LinearLayout parent) { View line = new View(this); line.setBackgroundColor(0xFF2D374A); parent.addView(line, new LinearLayout.LayoutParams(-1, dp(1))); }
    private void switchRow(LinearLayout parent, String title, String detail, String key, boolean defaultValue) {
        LinearLayout row = new LinearLayout(this); row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(labels(title, detail), new LinearLayout.LayoutParams(0, -2, 1));
        Switch toggle = new Switch(this); toggle.setContentDescription(title); toggle.setChecked(prefs.getBoolean(key, defaultValue));
        toggle.setOnCheckedChangeListener((button, checked) -> prefs.edit().putBoolean(key, checked).apply()); row.addView(toggle);
        row.setOnClickListener(v -> toggle.setChecked(!toggle.isChecked())); parent.addView(row);
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
