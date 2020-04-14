package com.cherryzp.coronasearch.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.cherryzp.coronasearch.R;
import com.cherryzp.coronasearch.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
//        Log.d("FCM log", "Refreshed token" + token);
//        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null && getSharedPreferences("alert", MODE_PRIVATE).getBoolean("alert", true)) {
//            Log.e("FCM log", "알림 메세지: " + remoteMessage.getNotification().getBody());
            String messageBody = remoteMessage.getNotification().getBody();
            String messageTitle = remoteMessage.getNotification().getTitle();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Channel ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher_corona)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "Channel Name";
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notificationBuilder.build());
        }


        //        super.onMessageReceived(remoteMessage);
    }
}

//package com.delio.deliohybrid.Service;
//
//        import android.app.NotificationChannel;
//        import android.app.NotificationManager;
//        import android.app.PendingIntent;
//        import android.content.Context;
//        import android.content.Intent;
//        import android.graphics.Bitmap;
//        import android.graphics.BitmapFactory;
//        import android.media.AudioManager;
//        import android.media.RingtoneManager;
//        import android.net.Uri;
//        import android.os.Build;
//        import android.os.Vibrator;
//        import android.util.Log;
//
//        import androidx.core.app.NotificationCompat;
//
//        import com.delio.deliohybrid.Activity.MainActivity;
//        import com.delio.deliohybrid.Activity.SplashActivity;
//        import com.delio.deliohybrid.R;
//        import com.google.firebase.messaging.FirebaseMessagingService;
//        import com.google.firebase.messaging.RemoteMessage;
//
//        import org.json.JSONObject;
//
//        import java.io.BufferedInputStream;
//        import java.net.URL;
//        import java.net.URLConnection;
//        import java.util.Map;
//
//
//
//
//public class FCMService extends FirebaseMessagingService {
//
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (remoteMessage.getData().size() > 0) {
//            sendNotification(remoteMessage.getData());
//        }
//    }
//
//    private void sendNotification(Map<String, String> msg) {
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Vibrator vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
//        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//
//        PendingIntent contentIntent;
//        int s = (int) System.currentTimeMillis();
//        boolean status = false;
//
//        Uri notification = null;
//
//        try {
//            JSONObject json = new JSONObject(msg.get("message"));
//            String title = json.optString("title");
//            String contents = json.optString("contents");
//            String img_url = json.optString("img_url", "");
//            String mute_yn = json.optString("mute_yn", "N");
//            String target_type = json.optString("target_type");
//            String target_reg_no = json.optString("target_reg_no");
//
//            if(mute_yn.equals("N")) {
//                switch(am.getRingerMode()) {
//                    case AudioManager.RINGER_MODE_NORMAL :
//                        notification = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION);
//                        break;
//                    case AudioManager.RINGER_MODE_VIBRATE :
//                        notification = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION);
//                        vib.vibrate(1500);
//                        break;
//                    case AudioManager.RINGER_MODE_SILENT :
//                        break;
//                }
//            }
//
//            Bitmap imgBitmap = null;
//            if(img_url != null && !img_url.equals("")) {
//                URL url = new URL("" + img_url);
//                URLConnection conn = url.openConnection();
//                conn.connect();
//                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
//                imgBitmap = BitmapFactory.decodeStream(bis);
//                bis.close();
//            }
//
//            //앱이 실행 중이지 않을 때
//            Log.e("!status", !status+"");
//            if (!status) {
//                Intent intent = new Intent(this, SplashActivity.class);
//
//                intent.putExtra("target_type", target_type);
//                intent.putExtra("target_reg_no",target_reg_no);
//
//                contentIntent = PendingIntent.getActivity(this, s, intent, 0);
//            } else {
//                Intent intent = new Intent(this, MainActivity.class);
//                contentIntent = PendingIntent.getActivity(this, s, intent, 0);
//            }
//
//            // 오레오 버전
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                CharSequence name = getString(R.string.channel_name);
//                String description = getString(R.string.channel_description);
//                int importance = NotificationManager.IMPORTANCE_DEFAULT;
//                NotificationChannel channel = new NotificationChannel(getPackageName(), name, importance);
//                channel.setDescription(description);
//                mNotificationManager.createNotificationChannel(channel);
//            }
//
//            NotificationCompat.Builder mBuilder;
//
//            if (img_url != null && !img_url.equals("")) {
//                mBuilder = new NotificationCompat.Builder(this, getPackageName())
//                        .setSmallIcon(R.drawable.push_icon)
//                        .setTicker(title)
//                        .setAutoCancel(true)
//                        .setSound(notification);
//
//                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
//                style.bigPicture(imgBitmap);
//                mBuilder.setStyle(style);
//            } else {
//                mBuilder = new NotificationCompat.Builder(this, getPackageName())
//                        .setSmallIcon(R.drawable.push_icon)
//                        .setTicker(title)
//                        .setAutoCancel(true)
//                        .setSound(notification);
//            }
//
//            mBuilder.setContentIntent(contentIntent);
//            mNotificationManager.notify(s, mBuilder.build());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        count++;
////        ShortcutBadger.applyCount(this, count);
//    }
//}
