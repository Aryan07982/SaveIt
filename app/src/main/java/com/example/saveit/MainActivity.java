package com.example.saveit;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import data.AppDatabase;
import data.SavedLink;
import data.SavedLinkDao;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handleIntent(getIntent());
        new dbThreadDisplay().start();
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Overwrites the old intent with the new one
        handleIntent(intent);
    }

    public void handleIntent(Intent intent){
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {

                    String cleanUrl = extractUrl(sharedText);
                    new dbThreadInsert(cleanUrl).start();
                    Toast.makeText(this, "Link Saved", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class dbThreadInsert extends Thread{
        public String url;
        public dbThreadInsert(String sharedText){
            this.url = sharedText;
        }

        public void run(){
            super.run();
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            SavedLinkDao savedLinkDao = db.savedLinkDao();

            savedLinkDao.insertRecord(new SavedLink(url, System.currentTimeMillis()));
            new dbThreadDisplay().start();
        }
    }

    class dbThreadDisplay extends Thread{

        @Override
        public void run(){
            super.run();
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            SavedLinkDao savedLinkDao = db.savedLinkDao();
            List<SavedLink> linkTable = savedLinkDao.getAll();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new LinkAdapter(MainActivity.this, linkTable);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
    }

    class dbThreadDelete extends Thread{
        SavedLink linkToDelete;

        public dbThreadDelete(SavedLink linkToDelete){
            this.linkToDelete = linkToDelete;
        }

        @Override
        public void run(){
            super.run();
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            SavedLinkDao savedLinkDao = db.savedLinkDao();
            savedLinkDao.deleteRecord(linkToDelete);
            new dbThreadDisplay().start();
        }
    }

    private String extractUrl(String input) {
        if (input == null) return null;

        String[] words = input.split("\\s+");
        for (String word : words) {
            if (word.startsWith("http://") || word.startsWith("https://")) {
                return word;
            }
        }
        return input;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            SavedLink linkToDelete = adapter.getLinkAt(position);
            new dbThreadDelete(linkToDelete).start();
        }
    };
}


