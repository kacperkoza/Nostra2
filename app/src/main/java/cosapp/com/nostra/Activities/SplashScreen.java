package cosapp.com.nostra.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cosapp.com.nostra.DataManager;
import cosapp.com.nostra.JSON.JSONParser;
import cosapp.com.nostra.JSON.JSONReaderTask;
import cosapp.com.nostra.Place.ParkingMachine;
import cosapp.com.nostra.Place.TicketMachine;
import cosapp.com.nostra.R;
import cosapp.com.nostra.Websites;
import xyz.hanks.library.SmallBang;

public class SplashScreen extends Activity {
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
        if (isFirstRun()) {
            addObjectsToDatabase();
        }
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
        if (isFirstRun) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean("is_first_run", false);
            editor.commit();
        }
        return isFirstRun;
    }

    private void addObjectsToDatabase() {
        addTicketMachines();
        addParkingMachines();
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
                    ticketMachines = JSONParser.getTicketMachines(result);
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
                parkingMachines = JSONParser.parseTicketMachines(result);
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

    private void startMainActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}
