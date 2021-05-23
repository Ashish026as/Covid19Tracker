package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView confirmed,active,recovered,deceased;
    States state;
    RecyclerView recycle;
    StateAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    TextView lastUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        confirmed = findViewById(R.id.confirmedTv);
        active = findViewById(R.id.activeTv);
        recovered = findViewById(R.id.recoveredTv);
        deceased = findViewById(R.id.deceasedTv);
        recycle = findViewById(R.id.recycle);
        swipeRefresh = findViewById(R.id.swipeRefresh);
       lastUpdated = findViewById(R.id.lastUpdated);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchResults();
                swipeRefresh.setRefreshing(false);
            }
        });

        fetchResults();
    }


    private void fetchResults() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.covid19india.org/data.json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String result = response.body().string();

                Gson gson = new GsonBuilder().create();
                state = gson.fromJson(result,States.class);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bindCombinedData(state.statewise.get(0));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        bindStateWiseData(state.statewise.subList(1,state.statewise.size()));
                    }
                });

            }


        });
    }

    private void bindStateWiseData(List<StateWise> subList) {

        recycle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StateAdapter(new ArrayList<StateWise>(subList));
        recycle.setAdapter(adapter);

    }

    private void bindCombinedData(StateWise stateWise) throws ParseException {

      confirmed.setText(stateWise.confirmed + "\n" + "+" + stateWise.deltaconfirmed);
        active.setText(stateWise.active + "\n" + "+" + stateWise.deltaactive);
        recovered.setText(stateWise.recovered + "\n" + "+" + stateWise.deltarecovered);
        deceased.setText(stateWise.deaths + "\n" + "+" + stateWise.deltadeaths);
       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
       lastUpdated.setText("Last Updated \n"  + getTimeAgo(sdf.parse(stateWise.lastupdatedtime)));

    }


    public String getTimeAgo(Date past){
        Date now = new Date();
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        Long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());

        if(seconds<60){
            return "Few Seconds ago";
        }else if (minutes <60){
            return minutes+" minutes ago";
        }else if(hours<24){
            return hours+" hour "+(minutes%60)+"min ago";
        }else {
            return new SimpleDateFormat().format(past).toString();
        }
    }

}