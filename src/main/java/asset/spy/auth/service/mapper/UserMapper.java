package asset.spy.auth.service.mapper;

import asset.spy.auth.service.dto.response.UserInfoResponseDto;
import asset.spy.auth.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "login", source = "account.login")
    @Mapping(target = "role", source = "account.role")
    UserInfoResponseDto toDto(User user);
}
