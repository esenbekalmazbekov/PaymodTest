package kg.doit.data.network;

import java.util.List;

import io.reactivex.Single;
import kg.doit.domain.dto.AppDto;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AppApi {

    @GET("all/")
    Single<List<AppDto>> fetchApps();

    @GET("{type}/")
    Single<AppDto> fetchAppDetail(
        @Path("type") String type
    );
}
