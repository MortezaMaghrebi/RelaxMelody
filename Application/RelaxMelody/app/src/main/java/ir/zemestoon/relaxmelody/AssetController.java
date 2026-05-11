package ir.zemestoon.relaxmelody;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;

public class AssetController {
    MainActivity activity;
    final String MY_PREFS_NAME = "PREFS_SILENT_SOUND";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public static AssetController instance;

    public static synchronized AssetController getInstance(MainActivity activity) {
        if (instance == null) {
            instance = new AssetController(activity);
        }
        return instance;
    }

    public AssetController(MainActivity activity) {
        this.activity = activity;
        editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    }

    public void loadSoundListFromAssets() {


        String fileName = "sound_list.txt";
        if (ir.zemestoon.relaxmelody.BillingManager.getInstance(activity).isPremiumActivated()) {
            fileName = "sound_list_pro.txt";
        }

        String content = readFromAssets(fileName);
        if (content != null && !content.isEmpty()) {
            boolean changed = setSoundList(content.trim());
            if (changed) {
                activity.loadSounds();
            }
        } else {
            // فایل در assets یافت نشد
            // می‌توانید در صورت نیاز خطایی لاگ کنید یا Toast بزنید
        }
    }
    public void loadMixedListFromAssets()  {
        String fileName = "mixed_list.txt";
        if (ir.zemestoon.relaxmelody.BillingManager.getInstance(activity).isPremiumActivated()) {
            fileName = "mixed_list_pro.txt.txt";
        }

        String content = readFromAssets(fileName);
        if (content != null && !content.isEmpty()) {
            boolean changed = setMixedList(content.trim());
            if (changed) {
                activity.loadMixes();
                //ToastUtils.showSafeToast(activity, "لیست میکس ها آپدیت شد");
            }
        } else {
            // فایل در assets یافت نشد - می‌توانید خالی بگذارید یا لاگ کنید
        }
    }
    private String readFromAssets(String fileName) {
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setSoundList(String soundList) {
        boolean changed = !getSoundList().trim().equals(soundList.trim());
        editor.putString("soundlist", soundList);
        editor.commit();
        return changed;
    }


    public String getSoundList() {
        String soudnlist = prefs.getString("soundlist", "");
        return prefs.getString("soundlist","");
    }

    public boolean setMixedList(String mixedList) {
        boolean changed = !getMixedList().trim().equals(mixedList.trim());
        editor.putString("mixedlist", mixedList);
        editor.commit();
        return changed;
    }

    public String getMixedList() {
        String mixedList = prefs.getString("mixedlist", "");
        return mixedList;
    }

}
