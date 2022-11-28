package kg.doit.domain.model;

import java.io.Serializable;
import java.util.Objects;

public class App implements Serializable {
    private final String link;
    private final String version;
    private final String type;
    private final String logo50Link;
    private final String logo200Link;
    private final String title;
    private final String description;
    private final AppStatus status;

    public App(
        String link,
        String version,
        String type,
        String logo50Link,
        String logo200Link,
        String title,
        String description,
        AppStatus status
    ) {
        this.link = link;
        this.version = version;
        this.type = type;
        this.logo50Link = logo50Link;
        this.logo200Link = logo200Link;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public String getLogo50Link() {
        return logo50Link;
    }

    public String getLogo200Link() {
        return logo200Link;
    }

    public AppStatus getStatus() { return status; }

    public String localFile() {
        return type + ".apk";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof App)) return false;
        App that = (App) o;
        return Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }
}