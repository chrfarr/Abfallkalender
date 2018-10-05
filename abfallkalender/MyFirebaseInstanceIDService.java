package de.farr_net.abfallkalender;

/**
 * Created by cfarr on 12.12.16.
 */

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String REGISTER_URL = "http://apps.farr-net.de/abfall/registerFCM.php";
    public static final String KEY_GCM_REGID = "gcm_regid";
    public static final String KEY_BEZIRK = "bezirk";


    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        // register new user
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //store new user into the database
        final String gcm_regid = token;
        final String bezirk = "0";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MyFirebaseInstanceIDService.this, response, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Erfolgreich registriert: " + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MyFirebaseInstanceIDService.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Fehler bei der Registrierung: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_GCM_REGID, gcm_regid);
                params.put(KEY_BEZIRK, bezirk);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        // write FCM ID to shared preferences
        String PREF_FILE_NAME = "de.farr_net.abfallkalender_preferences";
        SharedPreferences sharedPref = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("gcm_regid", gcm_regid);
        editor.commit();

    }
}