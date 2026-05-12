package com.codestoon.relaxmelody;

import static android.content.Context.MODE_PRIVATE;

import static com.codestoon.relaxmelody.BuildConfig.FLAVOR;

import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class NetController {
    MainActivity activity;
    final String MY_PREFS_NAME = "PREFS_SILENT_SOUND";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public static NetController instance;

    public static synchronized NetController getInstance(MainActivity activity) {
        if (instance == null) {
            instance = new NetController(activity);
        }
        return instance;
    }

    public NetController(MainActivity activity) {
        this.activity = activity;
        editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    }

    public void DownloadSoundList() throws UnsupportedEncodingException {
        int counter = prefs.getInt("download_sound_counter",2);
        if(counter<2){
            counter++;
            editor.putInt("download_sound_counter",counter);
            editor.commit();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "https://raw.githubusercontent.com/MortezaMaghrebi/MySoundData/refs/heads/main/soundlist.txt";

        // Variable to store the file content
        final String[] fileContent = {""}; // Using array to allow modification in inner class
        if(BillingManager.getInstance(activity).isPremiumActivated())url=url.replace("soundlist","soundlist_pro");
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editor.putInt("download_sound_counter",0);
                        editor.commit();
                        boolean changed = setSoundList(response.trim());
                        if (changed) {
                            activity.loadSounds();
                            ToastUtils.showSafeToast(activity, "لیست صداها آپدیت شد");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //ToastUtils.showSafeToast(activity, "برای دریافت لیست صداها به اینترنت متصل شوید");
                    }
                }
        );
        queue.getCache().clear();
        queue.add(getRequest);
    }

    public void DownloadMixedList() throws UnsupportedEncodingException {
        int counter = prefs.getInt("download_mixed_counter",2);
        if(counter<2){
            counter++;
            editor.putInt("download_mixed_counter",counter);
            editor.commit();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "https://raw.githubusercontent.com/MortezaMaghrebi/MySoundData/refs/heads/main/mixedlist.txt";

        // Variable to store the file content
        final String[] fileContent = {""}; // Using array to allow modification in inner class
        if(BillingManager.getInstance(activity).isPremiumActivated())url=url.replace("mixedlist","mixedlist_pro");

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editor.putInt("download_mixed_counter",0);
                        editor.commit();
                        boolean changed = setMixedList(response.trim());
                        if (changed) {
                            activity.loadMixes();
                            ToastUtils.showSafeToast(activity, "لیست میکس ها آپدیت شد");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //ToastUtils.showSafeToast(activity, "برای دریافت لیست میکس ها به اینترنت متصل شوید");
                    }
                }
        );
        queue.getCache().clear();
        queue.add(getRequest);
    }

    public void DownloadMessage() throws UnsupportedEncodingException {

        int counter = prefs.getInt("download_message_counter",3);
        if(counter<3){
            counter++;
            editor.putInt("download_message_counter",counter);
            editor.commit();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "https://raw.githubusercontent.com/MortezaMaghrebi/MySoundData/refs/heads/main/message.txt";
        if(FLAVOR=="myket") url = url.replace("message","message_myket");

            // Variable to store the file content
        final String[] fileContent = {""}; // Using array to allow modification in inner class

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editor.putInt("download_message_counter",0);
                        editor.commit();
                        boolean changed = setMessage(response.trim());
                        if (changed) {
                            showMessageDialog(response);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }
        );
        queue.getCache().clear();
        queue.add(getRequest);
    }

    public boolean setSoundList(String soundList) {
        boolean changed = !getSoundList().trim().equals(soundList.trim());
        editor.putString("soundlist", soundList);
        editor.commit();
        return changed;
    }

    public String getSoundList() {
        String soudnlist = prefs.getString("soundlist", "");
        return prefs.getString("soundlist","nature,پرنده,bird.png,assets/nature/bird.mp3,10,0,0\n" +
                "nature,پرندگان,hummingbird.png,assets/nature/birds.mp3,10,0,0\n" +
                "nature,گربه خرخر,cat.png,assets/nature/cat_purring.mp3,10,0,0\n" +
                "nature,جیرجیرک,cricket.png,assets/nature/cricket.mp3,10,0,0\n" +
                "nature,چکه آب,water.png,assets/nature/dripping.mp3,10,0,0\n" +
                "nature,آتش هیزم,campfire.png,assets/nature/firewood.mp3,10,0,0\n" +
                "nature,جنگل,forest.png,assets/nature/forest.mp3,10,0,0\n" +
                "nature,قورباغه,frog.png,assets/nature/frog.mp3,10,0,0\n" +
                "nature,چمنزار,grass.png,assets/nature/grassland.mp3,10,0,0\n" +
                "nature,باران شدید,rain.png,assets/nature/heavy_rain.mp3,10,0,0\n" +
                "nature,پرنده لون,bird.png,assets/nature/loon.mp3,10,0,0\n" +
                "nature,جغد,owl.png,assets/nature/owl.mp3,10,0,0\n" +
                "nature,باران,rain.png,assets/nature/rain.mp3,10,0,0\n" +
                "nature,باران روی سقف,rain.png,assets/nature/rain_on_roof.mp3,10,0,0\n" +
                "nature,باران روی چادر,tent.png,assets/nature/rain_on_tent.mp3,10,0,0\n" +
                "nature,باران روی پنجره,window.png,assets/nature/rain_on_window.mp3,10,0,0\n" +
                "nature,دریا,sea.png,assets/nature/sea.mp3,10,0,0\n" +
                "nature,مرغ دریایی,seagull.png,assets/nature/seagull.mp3,10,0,0\n" +
                "nature,برف,snow.png,assets/nature/snow.mp3,10,0,0\n" +
                "nature,رعد و برق,storm.png,assets/nature/thunder.mp3,10,0,0\n" +
                "nature,زیر آب,submarine.png,assets/nature/under_water.mp3,10,0,0\n" +
                "nature,جریان آب,water.png,assets/nature/water_flow.mp3,10,0,0\n" +
                "nature,آبشار,waterfall.png,assets/nature/waterfall.mp3,10,0,0\n" +
                "nature,نهنگ,whale.png,assets/nature/whale.mp3,10,0,0\n" +
                "nature,باد,wind.png,assets/nature/wind.mp3,10,0,0\n" +
                "nature,گرگ,wolf.png,assets/nature/wolf.mp3,10,0,0\n" +
                "nature,آب روان,water.png,assets/music/dan_gibson/dg_water_flow.mp3,10,0,0\n" +
                "nature,پرنده در باران,bird.png,assets/nature/bird_in_rain.mp3,10,0,0\n" +
                "nature,پرندگان پاییزی,hummingbird.png,assets/nature/birds_autumn.mp3,10,0,0\n" +
                "nature,آواز پرندگان,bird.png,assets/nature/birds_singing.mp3,10,0,0\n" +
                "nature,بلبل,bird.png,assets/nature/bulbul.mp3,10,0,0\n" +
                "nature,جنگل تاریک,forest.png,assets/nature/dark_jungle.mp3,10,0,0\n" +
                "nature,جنگل شب,forest.png,assets/nature/jungle_night.mp3,10,0,0\n" +
                "nature,طبیعت ظهر,sun.png,assets/nature/nature_noon.mp3,10,0,0\n" +
                "nature,طاووس,peacock.png,assets/nature/peacock.mp3,10,0,0\n" +
                "nature,قطرات باران,rain.png,assets/nature/rain_drops.mp3,10,0,0\n" +
                "nature,باران روی سقف ۲,rain.png,assets/nature/rain_on_roof2.mp3,10,0,0\n" +
                "nature,رودخانه,river.png,assets/nature/river.mp3,10,0,0\n" +
                "nature,دریای آرام,sea.png,assets/nature/sea_calm.mp3,10,0,0\n" +
                "nature,دریای شب,sea.png,assets/nature/sea_night.mp3,10,0,0\n" +
                "nature,امواج دریا,waves.png,assets/nature/sea_waves.mp3,10,0,0\n" +
                "nature,امواج آرام دریا ۲,waves.png,assets/nature/sea_waves_calm2.mp3,10,0,0\n" +
                "nature,امواج دریا با باد ملایم,waves.png,assets/nature/sea_waves_light_wind.mp3,10,0,0\n" +
                "nature,رعد و برق ۲,storm.png,assets/nature/thunder2.mp3,10,0,0\n" +
                "nature,باد ۲,wind.png,assets/nature/wind2.mp3,10,0,0\n" +
                "nature,پارو زدن,dinghy.png,assets/nature/oar.mp3,10,0,0\n" +
                "nature,باد ملایم,wind.png,assets/nature/gentle_wind.mp3,10,0,0\n" +
                "nature,جنگل موسمی,rainforest.png,assets/nature/rainforest.mp3,10,0,0\n" +
                "nature,جریان رودخانه,creek.png,assets/nature/river_natural_water.mp3,10,0,0\n" +
                "nature,آب کوهستان,water.png,assets/nature/water_cascade_down_rock.mp3,10,0,0\n" +
                "nature,غروب دریاچه,lake.png,assets/nature/lake_sunset.mp3,10,0,0\n" +
                "music,نوازش فرشته ای,sleep.png,assets/music/dan_gibson/dg_an_angles_caress.mp3,70,0,0\n" +
                "music,رهروی در سرزمین خواب,sleep.png,assets/music/dan_gibson/dg_drifting_in_dreamland.mp3,70,0,0\n" +
                "music,نخستین ستاره افق,sleep.png,assets/music/dan_gibson/dg_first_star_in_sky.mp3,70,0,0\n" +
                "music,فرود آرام,sleep.png,assets/music/dan_gibson/dg_gentle_descent.mp3,70,0,0\n" +
                "music,نیمه شب نیلی,sleep.png,assets/music/dan_gibson/dg_midnight_blue.mp3,70,0,0\n" +
                "music,آرام و آسوده,sleep.png,assets/music/dan_gibson/dg_safe_and_sound.mp3,70,0,0\n" +
                "music,سایه روشن های شفق,sleep.png,assets/music/dan_gibson/dg_twilight_fades.mp3,70,0,0\n" +
                "music,جهانی دور,sleep.png,assets/music/dan_gibson/dg_worlds_away.mp3,70,0,0\n" +
                "music,کاروانسرا,k.png,assets/music/kitaro/kitaro_caravansary.mp3,70,0,0\n" +
                "music,آکوا,water.png,assets/music/kitaro/kitaro_aqua.mp3,70,0,0\n" +
                "music,عاشقانه,k.png,assets/music/kitaro/kitaro_romance.mp3,70,0,0\n" +
                "music,افق درخشان,sun.png,assets/music/kitaro/kitaro_shimmering_horizon.mp3,70,0,0\n" +
                "music,روح دریاچه,lake.png,assets/music/kitaro/kitaro_spirit_of_the_west_lake.mp3,70,0,0\n" +
                "music,فال نیک,lucky.png,assets/music/kitaro/kitaro_auspicious_omen.mp3,70,0,0\n" +
                "music,کاروان,caravan.png,assets/music/kitaro/kitaro_caravan.mp3,70,0,0\n" +
                "music,دکتر سان و چینگ لینگ,doctor.png,assets/music/kitaro/kitaro_dr_sun_and_ching_ling.mp3,70,0,0\n" +
                "music,کوی,carp.png,assets/music/kitaro/kitaro_koi.mp3,70,0,0\n" +
                "music,زیارت,pilgrimage.png,assets/music/kitaro/kitaro_pilgrimage.mp3,70,0,0\n" +
                "music,فواره مقدس,fountain.png,assets/music/kitaro/kitaro_sacred_fountain.mp3,70,0,0\n" +
                "music,جاده ابریشم,road.png,assets/music/kitaro/kitaro_silk_road.mp3,70,0,0\n" +
                "music,خواهران سونگ,sisters.png,assets/music/kitaro/kitaro_soong_sisters.mp3,70,0,0\n" +
                "music,آداجیو,garden.png,assets/music/secret_garden/sg_adagio.mp3,70,0,0\n" +
                "music,همیشه آنجا,pin.png,assets/music/secret_garden/sg_always_there.mp3,70,0,0\n" +
                "music,آپاسیوناتا,garden.png,assets/music/secret_garden/sg_apassionata.mp3,70,0,0\n" +
                "music,کانتولونا,moon.png,assets/music/secret_garden/sg_cantoluna.mp3,70,0,0\n" +
                "music,شانونه,garden.png,assets/music/secret_garden/sg_chanonne.mp3,70,0,0\n" +
                "music,رویاگیر,dreamcatcher.png,assets/music/secret_garden/sg_dreamcatcher.mp3,70,0,0\n" +
                "music,رویای تو,swing.png,assets/music/secret_garden/sg_dreamed_of_you.mp3,70,0,0\n" +
                "music,دوئت,garden.png,assets/music/secret_garden/sg_duo.mp3,70,0,0\n" +
                "music,یخ\u200Cزده در زمان,winter.png,assets/music/secret_garden/sg_fozen_in_time.mp3,70,0,0\n" +
                "music,موج\u200Cهای سبز,deezer.png,assets/music/secret_garden/sg_greenwaves.mp3,70,0,0\n" +
                "music,سرود امید,lullaby.png,assets/music/secret_garden/sg_hymn_to_hope.mp3,70,0,0\n" +
                "music,لوتوس,lotus.png,assets/music/secret_garden/sg_lotus.mp3,70,0,0\n" +
                "music,شب تاریک,midnight.png,assets/music/secret_garden/sg_morketid.mp3,70,0,0\n" +
                "music,حرکت,dancing.png,assets/music/secret_garden/sg_moving.mp3,70,0,0\n" +
                "music,قرن جدید,clock.png,assets/music/secret_garden/sg_new_century.mp3,70,0,0\n" +
                "music,نوكتورن,garden.png,assets/music/secret_garden/sg_nocturn.mp3,70,0,0\n" +
                "music,سادگی,garden.png,assets/music/secret_garden/sg_ode_to_simplicity.mp3,70,0,0\n" +
                "music,پروانه,butterfly.png,assets/music/secret_garden/sg_papillon.mp3,70,0,0\n" +
                "music,پاساكالیا,garden.png,assets/music/secret_garden/sg_passacaglia.mp3,70,0,0\n" +
                "music,پاستورال,garden.png,assets/music/secret_garden/sg_pastorale.mp3,70,0,0\n" +
                "music,شب مقدس,owl.png,assets/music/secret_garden/sg_sacred_night.mp3,70,0,0\n" +
                "music,پناهگاه,tent.png,assets/music/secret_garden/sg_sanctuary.mp3,70,0,0\n" +
                "music,باغ مخفی,garden.png,assets/music/secret_garden/sg_secret_garden.mp3,70,0,0\n" +
                "music,سرناد,garden.png,assets/music/secret_garden/sg_serenade.mp3,70,0,0\n" +
                "music,سیگما,garden.png,assets/music/secret_garden/sg_sigma.mp3,70,0,0\n" +
                "music,آهنگ خواب,sleep.png,assets/music/secret_garden/sg_sleepsong.mp3,70,0,0\n" +
                "music,سونا,garden.png,assets/music/secret_garden/sg_sona.mp3,70,0,0\n" +
                "music,شب طوفانی,storm.png,assets/music/secret_garden/sg_stormy_night.mp3,70,0,0\n" +
                "music,رویا,unicorn.png,assets/music/secret_garden/sg_the_dream.mp3,70,0,0\n" +
                "music,بی\u200Cخیالی,easy.png,assets/music/secret_garden/sg_without_care.mp3,70,0,0\n" +
                "music,سفر,music.png,assets/music/secret_garden/sg_voyage.mp3,70,0,0\n" +
                "music,پاییز,autumn.png,assets/music/brian_crain/brian_autumn.mp3,70,0,0\n" +
                "music,زمین,globe.png,assets/music/brian_crain/brian_earth.mp3,70,0,0\n" +
                "music,آتش,campfire.png,assets/music/brian_crain/brian_fire.mp3,70,0,0\n" +
                "music,یخ,icy.png,assets/music/brian_crain/brian_ice.mp3,70,0,0\n" +
                "music,باران,rain.png,assets/music/brian_crain/brian_rain.mp3,70,0,0\n" +
                "music,برف,snow.png,assets/music/brian_crain/brian_snow.mp3,70,0,0\n" +
                "music,بهار,spring.png,assets/music/brian_crain/brian_spring.mp3,70,0,0\n" +
                "music,تابستان,sun.png,assets/music/brian_crain/brian_summer.mp3,70,0,0\n" +
                "music,آب,water.png,assets/music/brian_crain/brian_water.mp3,70,0,0\n" +
                "music,باد,wind.png,assets/music/brian_crain/brian_wind.mp3,70,0,0\n" +
                "music,زمستان,winter.png,assets/music/brian_crain/brian_winter.mp3,70,0,0\n" +
                "music,انتظار,hourglass.png,assets/music/cheshmazar/awaiting.mp3,70,0,0\n" +
                "music,آزادی,freedom.png,assets/music/cheshmazar/freedom.mp3,70,0,0\n" +
                "music,عشق پرشور,music.png,assets/music/cheshmazar/passion_of_love.mp3,70,0,0\n" +
                "music,عشق پرشور ۲,music.png,assets/music/cheshmazar/passion_of_love_ii.mp3,70,0,0\n" +
                "music,باران عشق,rain.png,assets/music/cheshmazar/rain_of_love.mp3,70,0,0\n" +
                "music,خیزش,sunrise.png,assets/music/cheshmazar/rising.mp3,70,0,0\n" +
                "music,خواب,sleep.png,assets/music/cheshmazar/sleep.mp3,70,0,0\n" +
                "music,دیدار,handshake.png,assets/music/cheshmazar/visit.mp3,70,0,0\n" +
                "music,رقص پروانه,butterfly.png,assets/music/yanni/yanni_butterfly_dance.mp3,70,0,0\n" +
                "music,فلیتسا,music.png,assets/music/yanni/yanni_felitsa.mp3,70,0,0\n" +
                "music,در آینه,mirror.png,assets/music/yanni/yanni_in_the_mirror.mp3,70,0,0\n" +
                "music,فقط یک خاطره,alzheimer.png,assets/music/yanni/yanni_only_a_memory.mp3,70,0,0\n" +
                "music,سوگندهای مخفی,promise.png,assets/music/yanni/yanni_secret_vows.mp3,70,0,0\n" +
                "music,دوست قدیمی,children.png,assets/music/yanni/yanni_so_long_my_friend.mp3,70,0,0\n" +
                "music,به کسی که می\u200Cداند,reading.png,assets/music/yanni/yanni_to_the_one_who_knows.mp3,70,0,0\n" +
                "music,نور شفابخش,sun.png,assets/music/ai/ai_healing_light.mp3,70,0,0\n" +
                "music,امواج شفابخش,waves.png,assets/music/ai/ai_healing_waves.mp3,70,0,0\n" +
                "music,پژواک محیطی زمین,globe.png,assets/music/chris_anes/chris_anes_ambient_echoes_of_the_earth.mp3,70,0,0\n" +
                "music,سرزمین رویاها,dream.png,assets/music/chris_anes/chris_anes_ambient_land_of_dreams.mp3,70,0,0\n" +
                "music,خاطرات,memories.png,assets/music/david_tolk/david_tolk_memories.mp3,70,0,0\n" +
                "music,دعا,pray.png,assets/music/david_tolk/david_tolk_pray.mp3,70,0,0\n" +
                "music,پس از نیمه شب,midnight.png,assets/music/dyathon/dyathon_after_midnight.mp3,70,0,0\n" +
                "music,همه در دریا,sea.png,assets/music/dyathon/dyathon_all_at_sea.mp3,70,0,0\n" +
                "music,باغ کلمات,garden.png,assets/music/dyathon/dyathon_the_garden_of_words.mp3,70,0,0\n" +
                "music,یک بهار سرد,winter.png,assets/music/eamonn_watt/eamonn_watt_one_cold_spring.mp3,70,0,0\n" +
                "music,شبی در پاریس,eiffel_tower.png,assets/music/paul_cardall/paul_cardall_an_evening_in_paris.mp3,70,0,0\n" +
                "music,گل سرخ من,rose.png,assets/music/peder_b_helland/peder_b_helland_my_rose.mp3,70,0,0\n" +
                "music,عمق جنگل,forest.png,assets/music/other/other_deep_in_the_forest.mp3,70,0,0\n" +
                "music,جزر و مد متغیر,tides.png,assets/music/other/other_turning_tides.mp3,70,0,0\n" +
                "noise,نویز قهوه\u200Cای,music.png,assets/noise/brown_noise.mp3,30,0,0\n" +
                "noise,نویز سفید,music.png,assets/noise/white_noise.mp3,30,0,0\n" +
                "wave,آداجیو آلفا,alpha.png,assets/wave/Adagio_Alpha_105_115Hz.mp3,20,0,0\n" +
                "wave,آلفا سعادت,alpha.png,assets/wave/Alpha_Bliss_107_115Hz.mp3,20,0,0\n" +
                "wave,امواج آلفا,alpha.png,assets/wave/Alpha_Brain_Waves.mp3,20,0,0\n" +
                "wave,تمرکز آلفا ۱,alpha.png,assets/wave/Alpha_Focus_107_115Hz.mp3,20,0,0\n" +
                "wave,تمرکز آلفا ۲,alpha.png,assets/wave/Alpha_Focus_127_135Hz.mp3,20,0,0\n" +
                "wave,تمرکز آلفا ۳,alpha.png,assets/wave/Alpha_Focus_97_104Hz.mp3,20,0,0\n" +
                "wave,آلفا اینرورس,alpha.png,assets/wave/Alpha_Innerverse_Reso.mp3,20,0,0\n" +
                "wave,امواج شفاف,alpha.png,assets/wave/Alpha_Lucid_Waves.mp3,20,0,0\n" +
                "wave,مدیتیشن آلفا,alpha.png,assets/wave/Alpha_Meditation.mp3,20,0,0\n" +
                "wave,شب آلفا,alpha.png,assets/wave/Alpha_Night_106_114Hz.mp3,20,0,0\n" +
                "wave,مسیر آلفا,path.png,assets/wave/Alpha_Path_96_105Hz.mp3,20,0,0\n" +
                "wave,رفاه آلفا,alpha.png,assets/wave/Alpha_Prosperity_127_135Hz.mp3,20,0,0\n" +
                "wave,شانت آلفا,alpha.png,assets/wave/Alpha_Shaant_74_82Hz.mp3,20,0,0\n" +
                "wave,سینوس آلفا ۱,alpha.png,assets/wave/Alpha_Sinus_54_57Hz.mp3,20,0,0\n" +
                "wave,سینوس آلفا ۲,alpha.png,assets/wave/Alpha_Sinus_62_66Hz.mp3,20,0,0\n" +
                "wave,سینوس آلفا ۳,alpha.png,assets/wave/Alpha_Sinus_88_94Hz.mp3,20,0,0\n" +
                "wave,سینوس آلفا ۴,alpha.png,assets/wave/Alpha_Sinus_91_101Hz.mp3,20,0,0\n" +
                "wave,روح آلفا,alpha.png,assets/wave/Alpha_Soul_110_117Hz.mp3,20,0,0\n" +
                "wave,کره آلفا,alpha.png,assets/wave/Alpha_Sphere_10Hz.mp3,20,0,0\n" +
                "wave,ترانسند آلفا,alpha.png,assets/wave/Alpha_Transcend_106_114Hz.mp3,20,0,0\n" +
                "wave,یونیورسال آلفا,alpha.png,assets/wave/Alpha_Universal_65_73Hz.mp3,20,0,0\n" +
                "wave,امواج آلفا ۸۸,alpha.png,assets/wave/Alpha_Waves_88_96Hz.mp3,20,0,0\n" +
                "wave,زون آلفا,alpha.png,assets/wave/Alpha_Zone_93_104Hz.mp3,20,0,0\n" +
                "wave,سینوس بتا,beta.png,assets/wave/Beta_Sinus_100_114Hz.mp3,20,0,0\n" +
                "wave,امواج بتا,beta.png,assets/wave/Beta_Waves_110_130Hz.mp3,20,0,0\n" +
                "wave,چرخش دلتا,d.png,assets/wave/Delta_Revolve_125_128Hz.mp3,20,0,0\n" +
                "wave,اکریورم,beta.png,assets/wave/Ecriurem_100_108Hz.mp3,20,0,0\n" +
                "wave,تعادل,balance.png,assets/wave/Equilibrium_96_104Hz.mp3,20,0,0\n" +
                "wave,فلو آلفا,alpha.png,assets/wave/Flow_Alpha_203_211Hz.mp3,20,0,0\n" +
                "wave,حافظه گاما,gamma.png,assets/wave/Gamma_Memory_Training.mp3,20,0,0\n" +
                "wave,سینوس گاما ۱,gamma.png,assets/wave/Gamma_Sinus_100_140Hz.mp3,20,0,0\n" +
                "wave,سینوس گاما ۲,gamma.png,assets/wave/Gamma_Sinus_300_350Hz.mp3,20,0,0\n" +
                "wave,امواج گاما ۸۶,gamma.png,assets/wave/Gamma_Waves_86_89Hz.mp3,20,0,0\n" +
                "wave,گاما ویلو,gamma.png,assets/wave/Gamma_Willow_29_71Hz.mp3,20,0,0\n" +
                "wave,مطالعه داخلی,alpha.png,assets/wave/Inner_Study_110_115Hz.mp3,20,0,0\n" +
                "wave,زندگی,alpha.png,assets/wave/Living_150_158Hz.mp3,20,0,0\n" +
                "wave,لوز آلفا,alpha.png,assets/wave/Luz_Alpha_100_108Hz.mp3,20,0,0\n" +
                "wave,مانترا آلفا_تتا,t.png,assets/wave/Mantra_Alpha_Theta.mp3,20,0,0\n" +
                "wave,فولیا تتا,t.png,assets/wave/Theta_Follia_41_45Hz.mp3,20,0,0\n" +
                "wave,رم تتا,t.png,assets/wave/Theta_Rem_60_66Hz.mp3,20,0,0\n" +
                "wave,راهب آب,water.png,assets/wave/Water_Monk.mp3,20,0,0\n" +
                "story,داستان چمنزار,tree.png,story/story_grassland.mp3,100,0,0\n" +
                "story,داستان قطب شمال,north.png,story/story_north_pole.mp3,100,0,0\n" +
                "story,داستان غواصی,cliff.png,,100,0,0\n" +
                "story,داستان قایق سواری,music.png,,100,0,0\n" +
                "story,داستان دریاچه,lake.png,,100,0,0\n" +
                "story,داستان بزغاله,music.png,,100,0,0\n" +
                "story,داستان کفش آهنی,iron.png,,100,0,0\n" +
                "story,خاله سوسکه,spider.png,,100,0,0\n" +
                "story,خاله پیرزن,old_woman.png,,100,0,0\n" +
                "story,کارخانه شکلات سازی,factory.png,,100,0,0\n" +
                "story,فانوس,lantern.png,,100,0,0\n" +
                "story,کلبه آرامش,cabin.png,,100,0,0\n");
    }

    public boolean setMixedList(String mixedList) {
        boolean changed = !getMixedList().trim().equals(mixedList.trim());
        editor.putString("mixedlist", mixedList);
        editor.commit();
        return changed;
    }

    public String getMixedList() {
        String mixedList = prefs.getString("mixedlist", "m,0,arcticMix,1,ماجرای قطب شمال,assets/covers/arctic_ice_landscape.jpg,1800,سفر به سرزمین یخ\u200Cها\n" +
                "~~\n" +
                "s,باد,wind2,25,0,120,true\n" +
                "s,نهنگ,whale,36,70,110,true\n" +
                "s,پناهگاه,sg_sanctuary,70,0,900,false\n" +
                "s,نوازش فرشته ای,dg_an_angles_caress,80,60,900,false \n" +
                "s,رهروی در سرزمین خواب,dg_drifting_in_dreamland,75,60,900,false \n" +
                "s,آکوا,kitaro_aqua,70,90,900,false\n" +
                "s,امواج شفاف,Alpha_Lucid_Waves,35,100,1800,true\n" +
                "#\n" +
                "m,1,singleTreeMix,2,تک درخت,assets/covers/single_tree_in_bliss.jpg,1800,دویدن در چمنزار\n" +
                "~~\n" +
                "s,کوی,kitaro_koi,30,10,240,true\n" +
                "s,پرندگان پاییزی,birds_autumn,15,0,50,true\n" +
                "s,بلبل,bulbul,3,0,30,true\n" +
                "s,نیمه شب نیلی,dg_midnight_blue,100,240,900,false \n" +
                "s,آداجیو,sg_adagio,35,250,900,false \n" +
                "s,آکوا,kitaro_aqua,30,270,900,false \n" +
                "s,پاییز,brian_autumn,30,290,900,false \n" +
                "s,تمرکز آلفا ۱,Alpha_Focus_107_115Hz,15,0,1200,true\n" +
                "#\n" +
                "m,1,beachMix,3,ساحل آرام,assets/covers/beach_sea_coast_sunset.jpg,1800,ترکیبی آرامش\u200Cبخش از صدای دریا و مرغان دریایی\n" +
                "~~\n" +
                "s,دریای آرام,sea_calm,0,30,0,180,true\n" +
                "s,مرغ دریایی,seagull,10,30,60,false\n" +
                "s,باد,wind,10,20,40,true\n" +
                "s,جاده ابریشم,kitaro_silk_road,100,5,1800,false \n" +
                "s,کاروانسرا,kitaro_caravansary,100,30,1800,false \n" +
                "s,باغ مخفی,sg_secret_garden,100,50,1800,false \n" +
                "s,امواج شفاف,Alpha_Lucid_Waves,7,0,180,true\n" +
                "#\n" +
                "m,1,darkSeaMix,4,دریای تاریک و طوفانی,assets/covers/dark_thunder_sea.jpg,1800,ترکیبی از دریای طوفانی، رعد و برق و موسیقی کاروان\n" +
                "~~\n" +
                "s,زیر آب,under_water,40,0,10,true\n" +
                "s,نهنگ,whale,100,0,30,true\n" +
                "s,رعد و برق,thunder,80,5,10,false\n" +
                "s,رعد و برق,thunder,70,10,30,false\n" +
                "s,رعد و برق,thunder,80,50,70,false\n" +
                "s,رعد و برق,thunder,70,120,140,false\n" +
                "s,رعد و برق,thunder,70,190,240,false\n" +
                "s,دریای شب,sea_night,70,30,60,true\n" +
                "s,کاروان,kitaro_caravan,100,10,1800,false\n" +
                "s,شب طوفانی,sg_stormy_night,90,40,1800,false\n" +
                "s,امواج شفاف,Alpha_Lucid_Waves,25,0,1800,true\n" +
                "#\n" +
                "m,0,forestMix,5,جنگل بارانی,assets/covers/forest_trees_green_nature.jpg,1800,تجربه جنگل در یک روز بارانی با امواج و موسیقی آرامش\u200Cبخش\n" +
                "~~\n" +
                "s,جنگل,forest,5,0,30,true\n" +
                "s,باران,rain,15,0,1800,true\n" +
                "s,پرنده,bird,10,60,400,false\n" +
                "s,پرنده ها,birds,10,40,100,false\n" +
                "s,رعد و برق,thunder,15,120,180,false\n" +
                "s,بهار,brian_spring,100,20,1800,false\n" +
                "s,رهروی در سرزمین خواب,dg_drifting_in_dreamland,100,40,900,false\n" +
                "s,شب طوفانی,sg_stormy_night,60,50,1800,false\n" +
                "s,نوازش فرشته\u200Cای,dg_an_angles_caress,70,60,600,false\n" +
                "s,آلفا شفاف,Alpha_Lucid_Waves,15,0,1800,true\n" +
                "s,آلفا مدیتیشن,Alpha_Meditation,15,60,1800,true\n" +
                "s,مانترا آلفا_تتا,Mantra_Alpha_Theta,15,120,1800,true\n" +
                "#\n" +
                "m,0,mountainMix,6,کوهستان مه\u200Cآلود,assets/covers/mountain_peak_fog.jpg,1800,صدای طبیعت بکر کوهستان\n" +
                "~~\n" +
                "s,باد,wind,45,0,1800,true\n" +
                "s,آب کوهستان,water_cascade_down_rock,10,20,1780,true\n" +
                "s,پرنده,bird,25,90,350,false\n" +
                "s,زمستان,brian_winter,50,0,1800,false\n" +
                "#\n" +
                "m,0,lakeMix,7,دریاچه آرام,assets/covers/lake_reflection_water.jpg,1800,انعکاس آرامش در آب\u200Cهای دریاچه\n" +
                "~~\n" +
                "s,آب کوهستان,water_cascade_down_rock,10,20,1780,true\n" +
                "s,قورباغه,frog,35,45,180,false\n" +
                "s,جیرجیرک,cricket,30,90,240,true\n" +
                "s,آب روان,dg_water_flow,30,0,300,false\n" +
                "s,رقص پروانه,yanni_butterfly_dance,45,0,1800,false\n" +
                "#\n" +
                "m,0,waterfallMix,8,آبشار خروشان,assets/covers/waterfall_river_nature.jpg,1800,انرژی بخش و نشاط آور\n" +
                "~~\n" +
                "s,آبشار,waterfall,65,0,1800,true\n" +
                "s,آب کوهستان,water_cascade_down_rock,10,20,1780,true\n" +
                "s,پرنده,bird,30,45,200,false\n" +
                "s,آزادی,freedom,0,50,0,1800,false\n" +
                "#\n" +
                "m,0,divingStoryMix,9,ماجرای غواصی,assets/covers/scuba_diving_ocean.jpg,1800,سفر به اعماق اقیانوس\n" +
                "~~\n" +
                "s,زیر آب,under_water,60,0,1800,true\n" +
                "s,نهنگ,whale,35,45,120,false\n" +
                "s,چکه آب,dripping,25,30,150,false\n" +
                "s,افق درخشان,kitaro_shimmering_horizon,50,0,1800,false\n" +
                "#\n" +
                "m,0,boatStoryMix,10,قایق سواری آرام,assets/covers/wooden_boat_lake.jpg,1800,قایق سواری آرامش بخش \n" +
                "~~\n" +
                "s,پارو زدن,oar,8,60,80,true\n" +
                "s,باد ملایم,gentle_wind,25,0,1800,true\n" +
                "s,مرغ دریایی,seagull,10,60,200,false\n" +
                "s,مرغ دریایی,seagull,10,0,15,false\n" +
                "s,مرغ دریایی,seagull,15,40,55,false\n" +
                "s,به کسی که می داند,yanni_to_the_one_who_knows,90,0,1800,false\n" +
                "s,همه در دریا,dyathon_all_at_sea,100,40,1800,true\n" +
                "s,نوازش فرشته\u200Cای,dg_an_angles_caress,100,60,600,false \n" +
                "s,رهروی در سرزمین خواب,dg_drifting_in_dreamland,100,80,900,false \n" +
                "s,زندگی,Living_150_158Hz,15,0,180,true\n" +
                "#\n" +
                "m,0,cabinMix,11,کلبه جنگلی,assets/covers/log_cabin_forest.jpg,1800,پناهگاهی در دل طبیعت\n" +
                "~~\n" +
                "s,آتش هیزم,firewood,50,0,1800,true\n" +
                "s,باران,rain,45,0,1800,true\n" +
                "s,جیرجیرک,cricket,30,60,240,true\n" +
                "s,پناهگاه,sg_sanctuary,50,0,1800,false\n" +
                "#\n" +
                "m,0,lanternMix,12,فانوس جادویی,assets/covers/old_lantern_light.jpg,1800,ماجرای فانوس در شب تاریک\n" +
                "~~\n" +
                "s,باد,wind,45,0,1800,true\n" +
                "s,جیرجیرک,cricket,35,30,1770,true\n" +
                "s,جغد,owl,30,150,200,false\n" +
                "s,شب تاریک,sg_morketid,50,0,1800,false\n" +
                "#\n" +
                "m,0,chocolateMix,13,کارخانه شکلات سازی,assets/covers/chocolate_factory_sweet.jpg,1800,ماجرای شیرین در کارخانه شکلات\n" +
                "~~\n" +
                "s,آب کوهستان,water_cascade_down_rock,10,20,1780,true\n" +
                "s,چکه آب,dripping,40,45,150,false\n" +
                "s,پرنده,bird,30,90,200,false\n" +
                "s,بی\u200Cخیالی,sg_without_care,50,0,1800,false\n" +
                "#\n" +
                "m,0,spiderMix,14,خاله سوسکه,assets/covers/spider_web_dew.jpg,1800,ماجرای خاله سوسکه در خانه قدیمی\n" +
                "~~\n" +
                "s,باران روی پنجره,rain_on_window,50,0,1800,true\n" +
                "s,چکه آب,dripping,35,60,150,false\n" +
                "s,جیرجیرک,cricket,30,120,240,true\n" +
                "s,رویا,sg_the_dream,0,50,0,1800,false\n" +
                "#\n" +
                "m,0,goatMix,15,بزغاله کوچولو,assets/covers/goat_farm_animal.jpg,1800,ماجراهای بزغاله در مزرعه\n" +
                "~~\n" +
                "s,چمنزار,grassland,50,0,1800,true\n" +
                "s,پرنده,bird,35,90,200,false\n" +
                "s,چکه آب,dripping,40,150,180,false\n" +
                "s,تابستان,brian_summer,50,0,1800,false\n" +
                "#\n" +
                "m,0,meditationMix,16,مدیتیشن عمیق,assets/covers/yoga_meditation_peace.jpg,1800,مناسب برای تمرین مدیتیشن و یوگا\n" +
                "~~\n" +
                "s,نویز سفید,white_noise,40,0,1800,true\n" +
                "s,آبشار,waterfall,35,10,1790,true\n" +
                "s,سرود امید,sg_hymn_to_hope,60,300,320,false\n" +
                "s,مدیتیشن,brian_earth,50,0,1800,false\n" +
                "#\n" +
                "m,0,nightMix,17,شب آرام,assets/covers/starry_night_sky.jpg,1800,صدای طبیعت در یک شب آرام\n" +
                "~~\n" +
                "s,جیرجیرک,cricket,50,0,1800,true\n" +
                "s,جغد,owl,35,45,200,false\n" +
                "s,باد,wind,25,0,1800,true\n" +
                "s,نیمه شب نیلی,dg_midnight_blue,50,0,1800,false\n" +
                "#\n" +
                "m,0,desertMix,18,بیابان ستاره\u200Cها,assets/covers/desert_sand_dunes.jpg,1800,شبی آرام در دل بیابان\n" +
                "~~\n" +
                "s,باد,wind,50,0,1800,true\n" +
                "s,برف,snow,30,90,240,true\n" +
                "s,دیدار,visit,50,0,1800,false\n" +
                "#\n" +
                "m,0,zenMix,19,باغ ذن,assets/covers/zen_garden_calm.jpg,1800,آرامش در باغ ژاپنی\n" +
                "~~\n" +
                "s,آب کوهستان,water_cascade_down_rock,10,20,1780,true\n" +
                "s,چکه آب,dripping,35,60,80,false\n" +
                "s,پرنده,bird,25,90,200,false\n" +
                "s,لوتوس,sg_lotus,50,0,1800,false\n" +
                "#\n" +
                "m,0,candleMix,20,نور شمع,assets/covers/candle_light_relax.jpg,1800,آرامش در نور شمع\n" +
                "~~\n" +
                "s,نویز قهوه\u200Cای,brown_noise,40,0,1800,true\n" +
                "s,چکه آب,dripping,30,45,150,false\n" +
                "s,خواب,sleep,50,0,1800,false\n" +
                "#\n" +
                "m,0,tropicalMix,21,ساحل گرمسیری,assets/covers/tropical_beach_palm_trees.jpg,1800,گرمای آفتاب و نسیم دریا\n" +
                "~~\n" +
                "s,دریا,sea,70,0,1800,true\n" +
                "s,باد,wind,35,0,1800,true\n" +
                "s,مرغ دریایی,seagull,40,30,300,false\n" +
                "s,عشق پرشور,passion_of_love,50,0,1800,false\n" +
                "#\n" +
                "m,0,rainforestMix,22,جنگل بارانی استوایی,assets/covers/rainforest_jungle_plants.jpg,1800,تنوع صوتی جنگل\u200Cهای بارانی\n" +
                "~~\n" +
                "s,جنگل,forest,60,0,1800,true\n" +
                "s,پرنده,bird,35,45,180,false\n" +
                "s,باران,rain,50,0,1800,true\n" +
                "s,سونا,sg_sona,50,0,1800,false\n");
        return mixedList;
    }



    public boolean setMessage(String message) {
        boolean changed = !getMessage().trim().equals(message.trim());
        editor.putString("message_html", message);
        editor.commit();
        return changed;
    }

    public String getMessage() {
        String message = prefs.getString("message_html", "");
        return message;
    }

    private void showMessageDialog(String htmlContent) {
        // نمایش در ترد اصلی
        activity.runOnUiThread(() -> {
            MessageDialog dialog = new MessageDialog(activity, htmlContent);
            dialog.show();
        });
    }

    public void ShowWebpage() throws UnsupportedEncodingException {

          RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "https://raw.githubusercontent.com/MortezaMaghrebi/MySoundData/refs/heads/main/webpage.txt";
        if(FLAVOR=="myket") url = url.replace("webpage","webpage_myket");

        // Variable to store the file content
        final String[] fileContent = {""}; // Using array to allow modification in inner class

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                           showMessageDialog(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }
        );
        queue.getCache().clear();
        queue.add(getRequest);
    }

}
