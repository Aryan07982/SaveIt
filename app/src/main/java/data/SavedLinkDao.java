package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedLinkDao {

    @Insert
    void insertRecord(SavedLink links);

    @Query("SELECT * FROM savedlink ORDER BY createdAt DESC")
    List<SavedLink> getAll();

}
