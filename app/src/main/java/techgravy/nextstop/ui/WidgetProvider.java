package techgravy.nextstop.ui;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import techgravy.nextstop.R;
import techgravy.nextstop.ui.details.DetailsCityActivity;
import techgravy.nextstop.ui.home.model.AutoJson_Places;
import techgravy.nextstop.ui.home.model.Places;
import techgravy.nextstop.utils.FirebaseJSONUtil;
import techgravy.nextstop.utils.logger.Logger;
import timber.log.Timber;

public class WidgetProvider extends AppWidgetProvider {
    private DatabaseReference mDatabaseReference;
    private Random random;
    private final String TAG = "Widget";
    private Places places;
    private AppWidgetTarget mRemoteViewsTarget;

    private List<Places> shownList;

    public WidgetProvider() {
        super();
        shownList = new ArrayList<>();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = {R.xml.app_widget};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        random = new Random();

        if (intent != null)
            if (intent.getExtras() != null)
                if (intent.getExtras().getParcelable("place") != null)
                    places = intent.getExtras().getParcelable("place");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //  if (places == null) {
        Logger.t("PlacePlan").d("Place = random ");
        Query query = mDatabaseReference.child("places");
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    try {
                        List<Places> placesList = new ArrayList<Places>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Places places = FirebaseJSONUtil.deserialize(snapshot, AutoJson_Places.class);
                            placesList.add(places);
                        }

                        for (int i = 0; i < count; i++) {
                            int widgetId = appWidgetIds[i];
                            places = placesList.get(random.nextInt(placesList.size() - 1));
                            Logger.t("PlaceWidget").d("Place =" + places.place());
                            shownList.add(places);
                            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                                    R.layout.widget);
                            mRemoteViewsTarget = new AppWidgetTarget(context, remoteViews, R.id.placeImageView, appWidgetIds);

                            remoteViews.setTextViewText(R.id.placeNameTextView, places.place());
                            Glide.with(context).load(places.photos().get(0)).asBitmap().placeholder(new ColorDrawable(ContextCompat.getColor(context, R.color.widget_background))).override(200, 200).into(mRemoteViewsTarget);
                            Intent leftIntent = new Intent(context, WidgetProvider.class);
                            leftIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            leftIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                            if (shownList.size() > 1)
                                leftIntent.putExtra("place", shownList.get(shownList.size() - 1));
                            PendingIntent pendingLeftIntent = PendingIntent.getBroadcast(context,
                                    0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            remoteViews.setOnClickPendingIntent(R.id.leftNav, pendingLeftIntent);


                            Intent rightIntent = new Intent(context, WidgetProvider.class);
                            rightIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            rightIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                            PendingIntent pendingRightIntent = PendingIntent.getBroadcast(context,
                                    0, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            remoteViews.setOnClickPendingIntent(R.id.rightNav, pendingRightIntent);

                            Intent intent;
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                intent = new Intent(context, DetailsCityActivity.class);
                                intent.setData(Uri.withAppendedPath(Uri.parse("nextstop://widget/id/#togetituniqie" + widgetId), UUID.randomUUID().toString()));
                                Logger.t("PlaceWidget").d("Place to be loaded =" + places.place());
                                intent.putExtra(DetailsCityActivity.WIDGET_PLACE, places);
                            } else {
                                intent = new Intent(context, SplashActivity.class);
                                intent.setData(Uri.withAppendedPath(Uri.parse("nextstop://widget/id/#togetituniqie" + widgetId), UUID.randomUUID().toString()));
                            }

                            remoteViews.setOnClickPendingIntent(R.id.planTextView, PendingIntent.getActivity(context, 0, intent, 0));


                            appWidgetManager.updateAppWidget(widgetId, remoteViews);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.tag(TAG).e(databaseError.getMessage());
            }
        });

       /* } else {
            Logger.t("PlacePlan").d("Place = else ");
            if (shownList.size() > 1)
                shownList.remove(places);
            for (int i = 0; i < count; i++) {
                int widgetId = appWidgetIds[i];
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget);
                mRemoteViewsTarget = new AppWidgetTarget(context, remoteViews, R.id.placeImageView, appWidgetIds);

                remoteViews.setTextViewText(R.id.placeNameTextView, places.place());
                Glide.with(context).load(places.photos().get(0)).asBitmap().override(200, 200).into(mRemoteViewsTarget);
                Intent leftIntent = new Intent(context, WidgetProvider.class);
                leftIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                leftIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                if (shownList.size() > 1)
                    leftIntent.putExtra("place", shownList.get(shownList.size() - 1));
                PendingIntent pendingLeftIntent = PendingIntent.getBroadcast(context,
                        0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.leftNav, pendingLeftIntent);


                Intent rightIntent = new Intent(context, WidgetProvider.class);
                rightIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                rightIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                PendingIntent pendingRightIntent = PendingIntent.getBroadcast(context,
                        0, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.rightNav, pendingRightIntent);


                remoteViews.setOnClickPendingIntent(R.id.planTextView, getPendingSelfIntent(context, places));


                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
*/
    }


    protected PendingIntent getPendingSelfIntent(Context context, Places mPlaces) {
        /*
         Intent intent = new Intent(context, CreateOperationsActivity.class);
    intent.putExtra("someKey", true);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, Constants.RequestCodes.CREATE_OPERATIONS, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         */
        Intent intent = new Intent(context, DetailsCityActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //NSApplication.getInstance().setPlaceToLoad(mPlaces);
        Logger.t("PlaceWidget").d("Place to be loaded =" + mPlaces.place());
        intent.putExtra(DetailsCityActivity.WIDGET_PLACE, mPlaces);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}
