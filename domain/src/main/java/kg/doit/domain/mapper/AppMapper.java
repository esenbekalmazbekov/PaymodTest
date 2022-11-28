package kg.doit.domain.mapper;

import kg.doit.domain.dto.AppDto;
import kg.doit.domain.model.App;
import kg.doit.domain.model.AppStatus;

public class AppMapper extends BaseMapper<App, AppDto> {
    private final IAppStatusFind statusFind;
    public AppMapper(IAppStatusFind statusFind) {
        this.statusFind = statusFind;
    }

    @Override
    public App mapToDomain(AppDto appDto) {
        return new App(
            appDto.getLink(),
            appDto.getVersion(),
            appDto.getType(),
            appDto.getLogo50Link(),
            appDto.getLogo200Link(),
            appDto.getTitle(),
            appDto.getDescription(),
            statusFind.findStatus(appDto)
        );
    }

    public interface IAppStatusFind {
        AppStatus findStatus(AppDto dto);
    }
}
