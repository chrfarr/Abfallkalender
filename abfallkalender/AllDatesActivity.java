package de.farr_net.abfallkalender;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllDatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        boolean connectedToInternet = new Connectivity().isConnected(this);
        if (!connectedToInternet){
            Snackbar.make(findViewById(android.R.id.content), "Bitte stelle eine Internetverbindung her!", Snackbar.LENGTH_LONG).show();
        }else {
            setContentView(R.layout.activity_all_dates);
            getData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

    private void getData() {

        String url = ConfigAllCollection.DATA_URL.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AllDatesActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(final String response){

        CustomAdapterAll adapter;
        final ArrayList<DataModelAll> dataModels = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray details = jsonObject.getJSONArray(ConfigAllCollection.JSON_ARRAY);

            for(int i=0; i<details.length()-1; i++)
            {
                JSONObject Data = details.getJSONObject(i);

                dataModels.add(new DataModelAll(
                        Data.getString(ConfigAllCollection.KEY_DAYOFWEEK),
                        Data.getString(ConfigAllCollection.KEY_DATE),
                        Data.getString(ConfigAllCollection.KEY_TYPE_REST),
                        Data.getString(ConfigAllCollection.KEY_TYPE_PAPIER),
                        Data.getString(ConfigAllCollection.KEY_TYPE_GRUEN),
                        Data.getString(ConfigAllCollection.KEY_TYPE_BIO),
                        Data.getString(ConfigAllCollection.KEY_TYPE_SONSTIGES)));
            }

            ListView listView = (ListView) findViewById(R.id.AllDateList);
            adapter= new CustomAdapterAll(dataModels,getApplicationContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // read preferences from another activity
                    String PREF_FILE_NAME = "de.farr_net.abfallkalender_preferences";
                    SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
                    String storedPreferenceStreetValue = preferences.getString("street_list","Keine Strasse#0");
                    String storedPreferenceStreet = storedPreferenceStreetValue.split("#")[0];
                    String storedPreferenceDistrict = storedPreferenceStreetValue.split("#")[1];


                    DataModelAll dataModel = dataModels.get(position);

                    Snackbar.make(view, dataModel.getWochentag() + ", " + dataModel.getDatum() + "\n" + storedPreferenceStreet + ": " +
                                  getCollection(storedPreferenceDistrict, dataModel.getRest(), dataModel.getPapier(), dataModel.getGruen(), dataModel.getBio()).getText().toString(),
                                  Snackbar.LENGTH_LONG).show();

                }
             });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private TextView getCollection(String district, String rest, String papier, String gruen, String bio){
        String tmpSnackbar = "";
        if (rest.contains(district)) tmpSnackbar = tmpSnackbar + "Hausmüll, ";
        if (papier.contains(district)) tmpSnackbar = tmpSnackbar + "Altpapier, ";
        if (gruen.contains(district)) tmpSnackbar = tmpSnackbar + "Gelber \"Sack\", ";
        if (bio.contains(district)) tmpSnackbar = tmpSnackbar + "Biotonne";

        // if no collection return appropriate message
        if (tmpSnackbar.isEmpty()) tmpSnackbar = "Keine Leerung";

        // if string ends with "," remove it
        if (tmpSnackbar.endsWith(", ")) tmpSnackbar = tmpSnackbar.substring(0, tmpSnackbar.length()-2);

        TextView txtSnackbar = new TextView(this);
        txtSnackbar.setText(tmpSnackbar);
        txtSnackbar.setBackgroundColor(Color.parseColor("#bababa"));

        return txtSnackbar;
    }

}
