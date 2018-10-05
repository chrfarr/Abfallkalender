package de.farr_net.abfallkalender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.farr_net.abfallkalender.SettingsActivity.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textViewResult;
    private TextView txtNextCollection;
    private TextView txtType;
    private TextView txtStreetDistrict;
    private ListView listView;
    private ImageView imgType;
    private ImageView otherImgType;
    private String display = "";
    private String displayStreetDistrict ="";
    private ProgressDialog loading;
    boolean doubleBackToExitPressedOnce = false;
    private static final String TAG = "MainActivity";
    private int startIndex = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_home);

        textViewResult = (TextView) findViewById(R.id.textViewResult);
        txtNextCollection = (TextView) findViewById(R.id.txtNextCollection);
        txtType = (TextView) findViewById(R.id.txtType);
        imgType = (ImageView) findViewById(R.id.imgType);
        txtStreetDistrict = (TextView) findViewById(R.id.txtStreetDistrict);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CardView cv1 = (CardView) findViewById(R.id.card_view1);
        CardView cv2 = (CardView) findViewById(R.id.card_view2);

        boolean connectedToInternet = new Connectivity().isConnected(this);
        if (!connectedToInternet){
            cv1.setVisibility(View.GONE);
            cv2.setVisibility(View.GONE);
            Snackbar.make(findViewById(android.R.id.content), "Bitte stelle eine Internetverbindung her!", Snackbar.LENGTH_LONG).show();
        }else {
            cv1.setVisibility(View.VISIBLE);
            cv2.setVisibility(View.VISIBLE);
            getData();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                return;
            }
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Zum Beenden nochmal drücken", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
         //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {


        //initializing the activity object which is selected
        switch (itemId) {
             case R.id.nav_all_dates:
                Intent all_dates = new Intent(this, AllDatesActivity.class);
                all_dates.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(all_dates);
                break;
            case R.id.nav_sondermuell:
                Intent sondermuell = new Intent(this, SonderMuellActivity.class);
                sondermuell.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(sondermuell);
                break;
            case R.id.nav_bauhof:
                Intent bauhof = new Intent(this, BauhofActivity.class);
                bauhof.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(bauhof);
                break;
            case R.id.nav_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
                startActivity(i);
                break;
            case R.id.nav_imprint:
                Intent imprint = new Intent(this, ImprintActivity.class);
                imprint.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(imprint);
                break;
            case R.id.nav_donate:
                Intent donate = new Intent(this, DonateActivity.class);
                donate.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(donate);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

     private void getData() {

         // read preferences from another activity
         String PREF_FILE_NAME = "de.farr_net.abfallkalender_preferences";
         SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

         Boolean storedPreferenceReminder = preferences.getBoolean("reminder_switch",true);
         String storedPreferenceStreetValue = preferences.getString("street_list","Keine Strasse#0");
         String storedPreferenceStreet = storedPreferenceStreetValue.split("#")[0];
         String storedPreferenceDistrict = storedPreferenceStreetValue.split("#")[1];

         CardView cv1 = (CardView) findViewById(R.id.card_view1);
         CardView cv2 = (CardView) findViewById(R.id.card_view2);
         cv1.setVisibility(View.VISIBLE);
         cv2.setVisibility(View.VISIBLE);

         if(storedPreferenceStreet.equalsIgnoreCase("Keine Strasse")) {

             cv1.setVisibility(View.GONE);
             cv2.setVisibility(View.GONE);

             AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
             alertDialog.setTitle("Hinweis");
             alertDialog.setCancelable(false);
             alertDialog.setMessage("Du hast bisher noch keine Strasse ausgewählt. Diese App funktioniert nur sinnvoll, wenn Du eine Strasse gewählt hast.\n");
             alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "zu den Einstellungen",
                     new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                             i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                             i.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
                             startActivity(i);
                         }
                     });
             alertDialog.show();

         }else {
             displayStreetDistrict= storedPreferenceStreet+", Bezirk: "+storedPreferenceDistrict;
         }

         String url = ConfigNextCollection.DATA_URL.trim() + storedPreferenceDistrict;

         StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
         },
            new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Log.i(TAG, "Error: " + error.getMessage().toString());
            }
            });

         RequestQueue requestQueue = Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
     }

     private void showJSON(final String response){

        String dspFirst = "";
        String dspType = "";
        display = "";

        CustomAdapterNext adapter;
        final ArrayList<DataModelNext> dataModels = new ArrayList<>();


         try {
             JSONObject jsonObject = new JSONObject(response);
             JSONArray details = jsonObject.getJSONArray(ConfigNextCollection.JSON_ARRAY);
             JSONObject DataFirst = details.getJSONObject(0);
             dspFirst = DataFirst.getString(ConfigNextCollection.KEY_DATE);

             DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.GERMANY);
             String dateString = df.format(java.util.Calendar.getInstance().getTime());

             Date reference = null;
             Date today = null;

             try {
                 reference = df.parse(dspFirst);
                 today = df.parse(dateString);
                 // 86400000 = convert millis to days
                 dspFirst = String.valueOf(Math.round((reference.getTime() - today.getTime()) / (86400000.0)));

             } catch (ParseException e) {
                 e.printStackTrace();
             }
             
             switch(dspFirst){
                 case "1":
                     dspFirst = " morgen!";
                     break;
                 case "2":
                     dspFirst = " übermorgen!";
                     break;
                 default:
                     dspFirst = " in " + dspFirst + " Tagen!";
             }

             switch(DataFirst.getString(ConfigNextCollection.KEY_TYPE)){
                case "gruen":
                    dspType =  "Gelber \"Sack\"";
                    imgType.setImageResource(R.mipmap.gruen);
                    break;
                case "bio":
                    dspType =  "Biotonne";
                    imgType.setImageResource(R.mipmap.bio);
                    break;
                case "rest":
                    dspType =  "Hausmüll";
                    imgType.setImageResource(R.mipmap.rest);
                    break;
                case "papier":
                    dspType =  "Altpapier";
                    imgType.setImageResource(R.mipmap.papier);
                    break;
                default:
                    Log.i(TAG, "Error: Ops! Something went wrong ;-(");
            }

            // check if other collection on the same date
             if (details.getJSONObject(0).getString(ConfigNextCollection.KEY_DATE).equals(details.getJSONObject(1).getString(ConfigNextCollection.KEY_DATE)))
             {
                 switch(details.getJSONObject(1).getString(ConfigNextCollection.KEY_TYPE)){
                     case "gruen":
                         dspType = dspType + ", Gelber \"Sack\"";
                         break;
                     case "bio":
                         dspType = dspType + ", Biotonne";
                         break;
                     case "rest":
                         dspType = dspType +  ", Hausmüll";
                         break;
                     case "papier":
                         dspType =  dspType + ", Altpapier";
                         break;
                     default:
                         Log.i(TAG, "Error: Ops! Something went wrong ;-(");
                 }
                 startIndex = 2;
             }
             dspType = dspType + "\nam "+ DataFirst.getString(ConfigNextCollection.KEY_DATE);

            for(int i=startIndex; i<details.length()-1; i++)
            {
                JSONObject Data = details.getJSONObject(i);

                dataModels.add(new DataModelNext(
                        Data.getString(ConfigNextCollection.KEY_DAYOFWEEK),
                        Data.getString(ConfigNextCollection.KEY_DATE),
                        Data.getString(ConfigNextCollection.KEY_TYPE)));

            }

            listView = (ListView) findViewById(R.id.otherDateList);
            adapter= new CustomAdapterNext(dataModels,getApplicationContext());
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

         txtNextCollection.setText(dspFirst);
         txtType.setText(dspType);
         txtStreetDistrict.setText(displayStreetDistrict);
    }


}
