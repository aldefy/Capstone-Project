package techgravy.nextstop.utils;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by aditlal on 25/12/16.
 */

public class FirebaseJSONUtil {

    public static <T> T deserialize(DataSnapshot dataSnapshot, Class<T> tClass) throws IOException {
        // Use Firebase to convert to a Map<String,Object>
        GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
        };
        Map<String, Object> map = dataSnapshot.getValue(genericTypeIndicator);

        // Use Jackson to convert from a Map to an Office object
        return LoganSquare.parse(new JSONObject(map).toString(), tClass);
    }
}
