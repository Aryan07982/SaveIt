package data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SavedLink {
    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "createdAt")
    public long createdAt;

    public SavedLink(String link, long createdAt) {
        this.id = id;
        this.link = link;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
