package techgravy.nextstop.data;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import techgravy.nextstop.utils.logger.Logger;

import static techgravy.nextstop.data.DatabaseContract.CONTENT_URI;

/* Process DB actions on a background thread */
public class SearchUpdateService extends IntentService {
    private static final String TAG = SearchUpdateService.class.getSimpleName();
    //Intent actions
    public static final String ACTION_INSERT = TAG + ".INSERT";
    public static final String ACTION_UPDATE = TAG + ".UPDATE";
    public static final String ACTION_DELETE = TAG + ".DELETE";

    public static final String EXTRA_VALUES = TAG + ".ContentValues";
    public static final String RECEIVER_TAG = "receiver";
    private ResultReceiver resultReceiver;

    public static void insertNewResult(Context context, ContentValues values) {
        Intent intent = new Intent(context, SearchUpdateService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }


    public static void updateResult(Context context, Uri uri, SearchChangeReceiver searchChangeReceiver, ContentValues values) {
        Intent intent = new Intent(context, SearchChangeReceiver.class);
        intent.setAction(ACTION_UPDATE);
        intent.setData(uri);
        intent.putExtra(RECEIVER_TAG, searchChangeReceiver);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void updateResult(Context context, Uri uri, ContentValues values) {
        Intent intent = new Intent(context, SearchUpdateService.class);
        intent.setAction(ACTION_UPDATE);
        intent.setData(uri);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void deleteResult(Context context, Uri uri) {
        Intent intent = new Intent(context, SearchUpdateService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        context.startService(intent);
    }


    public SearchUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_INSERT.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsert(values);
        } else if (ACTION_UPDATE.equals(intent.getAction())) {
            resultReceiver = intent.getParcelableExtra(RECEIVER_TAG);
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performUpdate(intent.getData(), values);
        } else if (ACTION_DELETE.equals(intent.getAction())) {
            performDelete(intent.getData());
        }
    }

    private void performInsert(ContentValues values) {
        ContentResolver contentResolver = getContentResolver();
        if (contentResolver.insert(CONTENT_URI, values) != null) {
            Log.d(TAG, "Inserted new Result");
        } else {
            Log.w(TAG, "Error inserting new Result");
        }
    }

    private void performUpdate(Uri uri, ContentValues values) {
        int count = getContentResolver().update(uri, values, null, null);
        if (count == 1)
            if (resultReceiver != null) {
                Bundle bundle = new Bundle();
                bundle.putInt(RECEIVER_TAG, count);
                resultReceiver.send(0, bundle);
            }
        Log.d(TAG, "Updated " + count + " Result items");
    }

    private void performDelete(Uri uri) {
        int count = getContentResolver().delete(uri, null, null);
        Logger.d(TAG, "Deleted " + count + " Results");
    }

}
