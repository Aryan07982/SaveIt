package data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SavedLink.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedLinkDao savedLinkDao();

    private static AppDatabase db;
    public static AppDatabase getDatabase(final Context context){
        if(db==null){
            db = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "room_db").build();
        }
        return db;
    }
}
