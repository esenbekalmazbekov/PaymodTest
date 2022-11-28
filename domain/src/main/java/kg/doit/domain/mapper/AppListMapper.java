package kg.doit.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import kg.doit.domain.dto.AppDto;
import kg.doit.domain.model.App;

public class AppListMapper extends BaseMapper<List<App>, List<AppDto>> {
    private final AppMapper itemMapper;

    public AppListMapper(AppMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public List<App> mapToDomain(List<AppDto> appDtos) {
        ArrayList<App> list = new ArrayList<>();
        for (AppDto dto : appDtos) {
            list.add(itemMapper.mapToDomain(dto));
        }
        return list;
    }
}
