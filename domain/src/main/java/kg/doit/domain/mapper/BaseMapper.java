package kg.doit.domain.mapper;

public abstract class BaseMapper<Domain, Dto> {
    public abstract Domain mapToDomain (Dto dto);
}
