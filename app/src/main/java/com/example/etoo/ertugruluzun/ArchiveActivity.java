package com.example.etoo.ertugruluzun;

import android.arch.persistence.room.Room;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ArchiveActivity extends AppCompatActivity {
    private static final String DATABASE_NAME = "earthquakes";
    public RecyclerViewAdapter adapter;
    TextView emptyText;
    ImageView emptyImage;
    private RecyclerView archiveRecylerList;
    private AppDatabase db;
    private List<Earthquake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_archive);

        Toolbar toolbar = findViewById(R.id.archiveToolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Archive");
        archiveRecylerList = findViewById(R.id.archiveList);

        emptyText = findViewById(R.id.emptyArchiveText);
        emptyImage = findViewById(R.id.emptyArchiveImage);

        db = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME).
                allowMainThreadQueries().build();

        earthquakes = db.earthquakeDao().getAllEarthquakes();

        checkIsArchiveEmpty(earthquakes);
        adapter = new RecyclerViewAdapter(getApplicationContext(), earthquakes);
        archiveRecylerList.setHasFixedSize(true);
        archiveRecylerList.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(archiveRecylerList.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        archiveRecylerList.addItemDecoration(dividerItemDecoration);
        archiveRecylerList.setItemAnimator(new DefaultItemAnimator());
        archiveRecylerList.setLayoutManager(layoutManager);
        archiveRecylerList.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                final Earthquake e = adapter.earthquakeList.get(position);
                Toast.makeText(ArchiveActivity.this, e.getPlace() + " depremi silindi", Toast.LENGTH_SHORT).show();
                adapter.earthquakeList.remove(e);
                adapter.notifyDataSetChanged();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                db.earthquakeDao().deleteSingleEarthquake(e);
                                adapter.earthquakeList.remove(e);
                                checkIsArchiveEmpty(adapter.earthquakeList);
                            }
                        });

                    }
                }).start();
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(archiveRecylerList);

    }

    private void checkIsArchiveEmpty(List<Earthquake> e) {
        if (e.size() < 1) {
            emptyText.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/editor_menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_archive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option

            case R.id.deleteAllArchive:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                db.earthquakeDao().deleteAllArchive();
                                adapter.earthquakeList.clear();
                                checkIsArchiveEmpty(adapter.earthquakeList);
                                Toast.makeText(getApplicationContext(), "Tüm arşiv temizlendi", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).start();
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}
