package asset.spy.auth.service.mapper;

import asset.spy.auth.service.dto.request.RegisterRequestDto;
import asset.spy.auth.service.model.Account;
import asset.spy.auth.service.model.Role;
import asset.spy.auth.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, Role.class})
public interface RegisterMapper {

    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "account", ignore = true)
    User toUser(RegisterRequestDto registerRequestDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "role", expression = "java(Role.getDefaultRole())")
    Account toAccount(RegisterRequestDto registerRequestDto);

}
