package com.justory.backend.mapper;


import com.justory.backend.api.external.UsersDTO;
import com.justory.backend.api.external.UsersFeaturesDTO;
import com.justory.backend.api.internal.UsersFeatures;
import com.justory.backend.api.internal.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UsersDTO toDTO(Users users) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setId(users.getId())
                .setEmail(users.getEmail())
                .setName(users.getName())
                .setRole(users.getRole());

        if (users.getUserFeaturesID() != null) {
            UsersFeatures userFeatures = users.getUserFeaturesID();
            UsersFeaturesDTO userFeaturesDTO = new UsersFeaturesDTO();
            userFeaturesDTO.setId(userFeatures.getId())
                    .setPhone(userFeatures.getPhone());
            usersDTO.setUserFeaturesID(userFeaturesDTO);
        }

        return usersDTO;
    }

    public UsersFeaturesDTO toDTO(UsersFeatures userFeatures) {
        UsersFeaturesDTO userFeaturesDTO = new UsersFeaturesDTO();
        userFeaturesDTO.setId(userFeatures.getId())
                .setPhone(userFeatures.getPhone());

        return userFeaturesDTO;
    }
}