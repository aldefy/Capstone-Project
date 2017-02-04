package techgravy.nextstop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.ImageView;

import com.beltaief.reactivefb.ReactiveFB;
import com.beltaief.reactivefb.SimpleFacebookConfiguration;
import com.beltaief.reactivefb.util.PermissionHelper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bumptech.glide.Glide;
import com.facebook.login.DefaultAudience;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import net.danlew.android.joda.JodaTimeAndroid;

import techgravy.nextstop.ui.home.model.PlaceLatLng;
import techgravy.nextstop.utils.PlacesLatLngConverter;
import techgravy.nextstop.utils.logger.LoggerTree;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by aditlal on 13/12/16.
 */

public class NSApplication extends MultiDexApplication {
    private NetComponent mNetComponent;
    public static NSApplication mNSApplication;

    public static NSApplication getInstance() {
        return mNSApplication;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNSApplication = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setupDependencies();
    }

    private void setupDependencies() {
        LoganSquare.registerTypeConverter(PlaceLatLng.class, new PlacesLatLngConverter());
        /*
            Joda Android Init
         */
        JodaTimeAndroid.init(getApplicationContext());

        /*
            Reactive Facebook init
         */
        // define list of permissions
        PermissionHelper[] permissions = new PermissionHelper[]{
                PermissionHelper.USER_ABOUT_ME,
                PermissionHelper.EMAIL,
                PermissionHelper.USER_FRIENDS,
                PermissionHelper.USER_BIRTHDAY,
                PermissionHelper.USER_TAGGED_PLACES,
                PermissionHelper.PUBLISH_ACTION};
        // add permission to a configuration
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(String.valueOf(R.string.facebook_app_id))
                .setPermissions(permissions)
                .setDefaultAudience(DefaultAudience.FRIENDS)
                .setAskForAllPermissionsAtOnce(false)
                .build();
        // init lib
        ReactiveFB.sdkInitialize(this);
        ReactiveFB.setConfiguration(configuration);

        /*
            Init NetModule
         */
        mNetComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .netModule(new NetModule(BuildConfig.GOOGLE_URL,BuildConfig.GOOGLE_API_KEY))
                .build();


         /*
          Logging init
         */
        if (BuildConfig.DEBUG) {
            Timber.plant(new LoggerTree());
            Timber.tag("NextStop");
        }

        /*
           Database Firebase
         */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
    }

    public NetComponent getmNetComponent() {
        return mNetComponent;
    }
}
