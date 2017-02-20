package techgravy.nextstop.utils;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

/**
 * Created by aditlal on 17/11/16.
 */

public class CommonUtils {

    public static final String ADDRESS_GEO_CODE = "AddressGeoCode";   // cant be saved to strings , context prob
    public static final String CURRENT_LOCAL_ADDRESS = "My Current loction address";  // cant be saved to strings , context prob
    public static final String NO_ADDRESS = "No Address returned!";  // cant be saved to strings , context prob
    public static final String CANNOT_FIND_ADDRESS = "Canont get Address!";  // cant be saved to strings , context prob
    public static DecimalFormat df = new DecimalFormat("#");
    public static final String TIME_FORMAT = "yyyyMMddHHmmss"; // cant be saved to strings , context prob
    // Ex : 20161001214120
    private static DateTimeFormatter formatter = DateTimeFormat.forPattern(TIME_FORMAT);
    private final static AtomicInteger c = new AtomicInteger(0);

    public static DateTime getFormattedDateTime(String dateString) {
        return formatter.parseDateTime(dateString);
    }

    public static int getDuration(int secs) {
        int totalTime = secs;
        int hours = totalTime / 3600;
        int minutes = (totalTime % 3600) / 60;
        return minutes;
    }

    public static String getTripSlotTime(DateTime start, DateTime end) {
        return "";
    }

    public static String formatDate(DateTime modelDateTime, DateTime today, DateTime yesterday) {
        if (modelDateTime.toLocalDate().equals(today.toLocalDate())) {
            return "Today\n" + modelDateTime.toString("MM/dd"); // cant be saved to strings , context prob
        } else if (modelDateTime.toLocalDate().equals(yesterday.toLocalDate())) {
            return "Yest \n" + modelDateTime.toString("MM/dd"); // cant be saved to strings , context prob
        } else {
            return modelDateTime.toString("MM/dd"); // cant be saved to strings , context prob
        }
    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Timber.tag(CommonUtils.class.getSimpleName()).i("**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Timber.tag(ADDRESS_GEO_CODE).w(CURRENT_LOCAL_ADDRESS, "" + strReturnedAddress.toString());
            } else {
                Timber.tag(ADDRESS_GEO_CODE).w(CURRENT_LOCAL_ADDRESS, NO_ADDRESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Timber.tag(ADDRESS_GEO_CODE).w(CURRENT_LOCAL_ADDRESS, CANNOT_FIND_ADDRESS);
        }
        return strAdd;
    }

    public static int getID() {
        return c.incrementAndGet();
    }
}
