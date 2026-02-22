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

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "createdAt")
    public long createdAt;

    public SavedLink(String link, String title, long createdAt) {
        this.link = link;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
