package com.example.saveit;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.List;

import data.AppDatabase;
import data.SavedLink;
import data.SavedLinkDao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handleIntent(getIntent());
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
                    Toast.makeText(this, sharedText, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class bgThread extends Thread{
        public String sharedText;
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        public bgThread(String sharedText){
            this.sharedText = sharedText;
        }

        public void run(){
            super.run();
            SavedLinkDao savedLinkDao = db.savedLinkDao();
            savedLinkDao.insertRecord(new SavedLink(sharedText, System.currentTimeMillis()));
        }
    }
}
