package kg.doit.domain.model;

import java.io.Serializable;

public enum AppStatus implements Serializable {
    ACTUAL,
    NEED_TO_UPDATE,
    DOWNLOADED,
    NOT_DOWNLOADED,
}
