package techgravy.nextstop.ui.push;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import org.json.JSONObject;

import java.util.Map;

import techgravy.nextstop.ui.home.HomeActivity;
import techgravy.nextstop.R;
import techgravy.nextstop.data.SharedPrefManager;
import techgravy.nextstop.utils.CommonUtils;
import timber.log.Timber;

/**
 * Created by aditlal on 29/11/16.
 */

public class FCMReceiverService extends FirebaseMessagingService {
    public static final String TAG = "PUSH";
    SharedPrefManager preferencesManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        preferencesManager = SharedPrefManager.getInstance(this);
        try {
            Timber.tag(TAG).d("From: " + remoteMessage.getFrom());
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Timber.tag(TAG).d("JSON_OBJECT =" + object.toString());
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setTicker(object.getString("title"))
                            .setContentTitle(object.getString("title"));
            Intent notificationIntent = new Intent(this, HomeActivity.class);
            PendingIntent intent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            builder.setContentTitle(object.getString("title"))
                    .setContentText(object.getString("alert"))
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(intent);
            builder.setColor(ContextCompat.getColor(this, R.color.accent));


            final int mNotificationId = CommonUtils.getID();
            final NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            // hide the notification after its selected
            final Notification notification = builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            mNotifyMgr.notify(mNotificationId, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
