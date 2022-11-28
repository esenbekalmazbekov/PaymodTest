package kg.doit.domain.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppDto implements Serializable {
    @SerializedName("link")
    private String link;
    @SerializedName("version")
    private String version;
    @SerializedName("type")
    private String type;
    @SerializedName("logo50Link")
    private String logo50Link;
    @SerializedName("logo200Link")
    private String logo200Link;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;

    public String getLink() {
        return link;
    }

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public String getLogo200Link() {
        return logo200Link;
    }

    public String getLogo50Link() {
        return logo50Link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
