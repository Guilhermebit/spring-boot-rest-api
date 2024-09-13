package com.personal.project.api.entity.user.enums.converters;

import com.personal.project.api.entity.user.enums.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true) // will be applied by JPA whenever necessary
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole userRole) {
        if(userRole == null)
           return null;

        return userRole.getRole();
    }

    @Override
    public UserRole convertToEntityAttribute(String value) {
        if(value == null)
           return null;

        return Stream.of(UserRole.values())
                .filter(ur -> ur.getRole().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
