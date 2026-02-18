package com.example.saveit;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import data.AppDatabase;
import data.SavedLink;
import data.SavedLinkDao;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handleIntent(getIntent());
        new bgThread2().start();
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

                    new bgThread(sharedText).start();
                    Toast.makeText(this, "Link Saved", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class bgThread extends Thread{
        public String sharedText;
        public bgThread(String sharedText){
            this.sharedText = sharedText;
        }

        public void run(){
            super.run();
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            SavedLinkDao savedLinkDao = db.savedLinkDao();
            savedLinkDao.insertRecord(new SavedLink(sharedText, System.currentTimeMillis()));
            new bgThread2().start();
        }
    }

    class bgThread2 extends Thread{

        @Override
        public void run(){
            super.run();
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            SavedLinkDao savedLinkDao = db.savedLinkDao();
            List<SavedLink> linkTable = savedLinkDao.getAll();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinkAdapter adapter = new LinkAdapter(MainActivity.this, linkTable);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
    }
}


