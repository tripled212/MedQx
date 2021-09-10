package com.example.medqx.Reminder;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.medqx.R;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import java.util.List;
import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    private static final String CHANNEL_ID="SAMPLE CHANNEL";

   @Override
    public void onReceive(Context context, Intent intent) {
//        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
//        mediaPlayer.start();
       Log.i("PARSER", "Trigger broadcast receiver. ");
       int notificationId = intent.getIntExtra("notificationId", 0);
       String msg = intent.getStringExtra("todo");

       try {
           // you must have android.permission.RECORD_AUDIO granted at this point
           Speech.getInstance().startListening(new SpeechDelegate() {
               @Override
               public void onStartOfSpeech() {
                   Log.i("speech", "speech recognition is now active");
               }

               @Override
               public void onSpeechRmsChanged(float value) {
                   Log.d("speech", "rms is now: " + value);
               }



               @Override
               public void onSpeechPartialResults(List<String> results) {
                   StringBuilder str = new StringBuilder();
                   for (String res : results) {
                       str.append(res).append(" ");
                   }

                   Log.i("speech", "partial result: " + str.toString().trim());
               }

               @Override
               public void onSpeechResult(String result) {
                   Log.i("speech", "result: " + result);
                   Speech.getInstance().say(msg);
               }


           });
       }  catch (SpeechRecognitionNotAvailable exc) {
           Log.e("speech", "Speech recognition is not available on this device!");
           // You can prompt the user if he wants to install Google App to have
           // speech recognition, and then you can simply call:
           //
           // SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
           //
           // to redirect the user to the Google App page on Play Store
       } catch (GoogleVoiceTypingDisabledException exc) {
           Log.e("speech", "Google voice typing must be enabled!");
       }

        Intent intent1 = new Intent(context, Med_take.class);
        intent1.putExtra("notificationId", notificationId);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent1,0);


        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 28){
            CharSequence channel_name = "My Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
            myNotificationManager.createNotificationChannel(channel);
       }

       Intent snoozeIntent = new Intent(context, SimpleAlarm.class);
       snoozeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId + 9876);

       NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Time To Take Your Medicine!!")
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
               .addAction(R.drawable.notification_snooze, "Take",
                       contentIntent)
               .setAutoCancel(true);


        myNotificationManager.notify(notificationId, builder.build());

    }

}
