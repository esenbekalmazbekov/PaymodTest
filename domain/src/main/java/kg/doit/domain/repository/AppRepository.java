package kg.doit.domain.repository;

import java.util.List;

import io.reactivex.Single;
import kg.doit.domain.dto.AppDto;

public interface AppRepository {
    Single<List<AppDto>> fetchApps();

    Single<AppDto> fetchApp(String type);
}