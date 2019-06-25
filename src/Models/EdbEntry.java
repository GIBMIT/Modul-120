package Models;


import java.util.Objects;

public class EdbEntry {
    String id;
    String file;
    String description;
    String date;
    String author;
    String type;
    String platform;
    String port;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


    //Overwritten equals and hashcode so methods like equals and contains work
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdbEntry)) return false;
        EdbEntry entry = (EdbEntry) o;
        return Objects.equals(getId(), entry.getId()) &&
                Objects.equals(getFile(), entry.getFile()) &&
                Objects.equals(getDescription(), entry.getDescription()) &&
                Objects.equals(getDate(), entry.getDate()) &&
                Objects.equals(getAuthor(), entry.getAuthor()) &&
                Objects.equals(getType(), entry.getType()) &&
                Objects.equals(getPlatform(), entry.getPlatform()) &&
                Objects.equals(getPort(), entry.getPort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFile(), getDescription(), getDate(), getAuthor(), getType(), getPlatform(), getPort());
    }
}
