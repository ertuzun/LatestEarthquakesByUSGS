package com.example.etoo.ertugruluzun;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String DATABASE_NAME = "earthquakes";
    public RecyclerView recyclerList;
    ProgressBar progressBar;
    TextView tv;
    private RecyclerViewAdapter rvAdapter;
    public List<Earthquake> earthquakesList;
    private AppDatabase db;
    private EditText minMag;
    private ImageView emptyListImage;
    private TextView emptyListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Son Depremler");
        db = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME).
                allowMainThreadQueries().build();

        earthquakesList = new ArrayList<>();

        recyclerList = findViewById(R.id.recyclerList);
        tv = findViewById(R.id.progressText);
        progressBar = findViewById(R.id.progressCircle);

        minMag = findViewById(R.id.minMagEditText);
        emptyListImage = findViewById(R.id.emptyListImage);
        emptyListText = findViewById(R.id.emptyListText);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                final Earthquake e = rvAdapter.earthquakeList.get(position);
                Toast.makeText(MainActivity.this, e.getPlace() + " depremi arşivlendi", Toast.LENGTH_SHORT).show();
                rvAdapter.earthquakeList.remove(e);
                rvAdapter.notifyDataSetChanged();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.earthquakeDao().insertAll(e);
                    }
                }).start();
            }
        }).attachToRecyclerView(recyclerList);


        checkEmptyList(earthquakesList);


    }

    public void retrieveEarthquakes(View view) {
        String queryText = minMag.getText().toString();
        earthquakesList.clear();
        new FetchEarthquake(rvAdapter, earthquakesList).execute(queryText);
        progressBar.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        checkEmptyList(earthquakesList);
    }


    private void checkEmptyList(List list) {
        if (list.size() < 1) {
            emptyListText.setVisibility(View.VISIBLE);
            emptyListImage.setVisibility(View.VISIBLE);
        } else {
            emptyListText.setVisibility(View.GONE);
            emptyListImage.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/editor_menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_archive:
                // Pop up confirmation dialog for deletion
                Intent i = new Intent(MainActivity.this, ArchiveActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    /*
          This asynctask class is from yeditepeasync project gsahinpi's github and I have add few lines of code also
          https://github.com/gsahinpi/yeditepemobile18fall/blob/master/yeditepeasync.zip
       */
    public class FetchEarthquake extends AsyncTask<String, Void, String> {
        RecyclerViewAdapter adapter;
        List<Earthquake> earthquakes;


        private FetchEarthquake(RecyclerViewAdapter adapter, List<Earthquake> earthquakes) {
            this.adapter = adapter;
            this.earthquakes = earthquakes;
        }


        @Override
        protected String doInBackground(String... strings) {
            return NetworkUtils.getEarthquakes(strings[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            earthquakes = new ArrayList<>();
            try {
                JSONObject baseJsonResponse = new JSONObject(s);
                JSONArray results = baseJsonResponse.getJSONArray("features");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject currentEarthquake = results.getJSONObject(i);
                    JSONObject properties = currentEarthquake.getJSONObject("properties");
                    double magnitude = properties.getDouble("mag");
                    String place = properties.getString("place");
                    long time = properties.getLong("time");
                    String url = properties.getString("url");

                    Earthquake earthquake = new Earthquake(place, time, magnitude, url);

                    earthquakes.add(earthquake);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rvAdapter = new RecyclerViewAdapter(getApplicationContext(), earthquakes);
            recyclerList.setHasFixedSize(true);
            recyclerList.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerList.getContext(),
                    ((LinearLayoutManager) layoutManager).getOrientation());
            recyclerList.addItemDecoration(dividerItemDecoration);
            recyclerList.setItemAnimator(new DefaultItemAnimator());
            recyclerList.setLayoutManager(layoutManager);
            recyclerList.setAdapter(rvAdapter);

            tv.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            checkEmptyList(earthquakes);

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


}
