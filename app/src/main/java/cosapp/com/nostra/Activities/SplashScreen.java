package cosapp.com.nostra.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.JSON.JSONParser;
import cosapp.com.nostra.JSON.JSONReaderTask;
import cosapp.com.nostra.Place.BikeStation;
import cosapp.com.nostra.Place.ParkingMachine;
import cosapp.com.nostra.Place.TicketMachine;
import cosapp.com.nostra.Place.TicketPoint;
import cosapp.com.nostra.R;
import cosapp.com.nostra.Utils;
import cosapp.com.nostra.Websites;
import cosapp.com.nostra.XMLParser;
import xyz.hanks.library.SmallBang;

public class SplashScreen extends AppCompatActivity {
    private ImageView imageView;
    private DataManager dataManager;
    private SmallBang smallBang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        smallBang = SmallBang.attach2Window(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smallBang.bang(view);

            }
        });

        dataManager = new DataManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isFirstRun() && !Utils.isNetworkAvailable(this)) {
             //TODO IMPLEMENT DIALOG WINDOW
        }

        if (isFirstRun()) {
            addObjectsToDatabase();
        }
        modifyFirstRunVariableInSharedPreferences();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        };
        handler.postDelayed(runnable, 3000);

    }

    private boolean isFirstRun() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = sharedPreferences.getBoolean("is_first_run", true);
        Log.d("is first run?", "" + isFirstRun);
        return isFirstRun;

    }

    private void modifyFirstRunVariableInSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = sharedPreferences.getBoolean("is_first_run", true);

        if (isFirstRun) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean("is_first_run", false);
            editor.commit();
        }
    }

    private void addObjectsToDatabase() {
        addTicketMachines();
        addParkingMachines();
        addTicketPoints();
        addBikeStations();
    }

    private void addTicketMachines() {
            JSONReaderTask task = new JSONReaderTask(Websites.TICKET_MACHINES);
            task.execute();

            String result = null;
            try {
                result = task.get();
            } catch (InterruptedException e) {
                Log.e("loadData", "InterruptedException");
            } catch (ExecutionException e) {
                Log.e("loadData", "InterruptedException");
            }

            ArrayList<TicketMachine> ticketMachines = null;

            if (result != null) {
                try {
                    ticketMachines = JSONParser.parseTicketMachines(result);
                } catch (JSONException e) {
                    Log.e("loadData", "JSONException");
                }
            }

            if (ticketMachines != null) {
                for (TicketMachine tm : ticketMachines) {
                    dataManager.addTicketMachineToDatabase(tm);
                }
            }
        }

    private void addParkingMachines() {
        JSONReaderTask task = new JSONReaderTask(Websites.PARKING_MACHINES);
        task.execute();

        String result = null;
        try {
            result = task.get();
        } catch (InterruptedException e) {
            Log.e("loadData", "InterruptedException");
        } catch (ExecutionException e) {
            Log.e("loadData", "InterruptedException");
        }

        ArrayList<ParkingMachine> parkingMachines = null;

        if (result != null) {
            try {
                parkingMachines = JSONParser.parseParkingMachines(result);
            } catch (JSONException e) {
                Log.e("loadData", "JSONException");
            }
        }

        if (parkingMachines != null) {
            for (ParkingMachine pm : parkingMachines) {
                dataManager.addParkingMachineToTheDatabase(pm);
            }
        }

    }

    private void addTicketPoints() {
        JSONReaderTask task = new JSONReaderTask(Websites.TICKETS_SALE_POINTS);
        task.execute();

        String result = null;
        try {
            result = task.get();
        } catch (InterruptedException e) {
            Log.e("loadData", "InterruptedException");
        } catch (ExecutionException e) {
            Log.e("loadData", "InterruptedException");
        }

        ArrayList<TicketPoint> ticketPoints = null;

        if (result != null) {
            try {
                ticketPoints = JSONParser.parseTicketPoints(result);
            } catch (JSONException e) {
                Log.e("loadData", "JSONException");
            }
        }

        if (ticketPoints != null) {
            for (TicketPoint tp : ticketPoints) {
                dataManager.addTicketPointToDatabase(tp);
            }
        }
    }

    private void addBikeStations() {
        XMLParser xmlParser = new XMLParser(Websites.BIKE_STATIONS);

        ArrayList<BikeStation> bikeStations = xmlParser.parseNextBike();

        for (BikeStation bs : bikeStations) {
            dataManager.addBikeStationToDatabase(bs);
        }
        dataManager.close();

        addTimeOfLastUpdate(xmlParser);
    }

    private void addTimeOfLastUpdate(XMLParser xmlParser) {
        String lastUpdateTime = xmlParser.readUpdateTime();
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString("lastUpdateTime", lastUpdateTime);
        edit.commit();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
