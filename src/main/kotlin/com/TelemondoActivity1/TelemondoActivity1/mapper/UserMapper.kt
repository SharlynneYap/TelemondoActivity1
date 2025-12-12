package com.TelemondoActivity1.TelemondoActivity1.mapper

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController
import com.TelemondoActivity1.TelemondoActivity1.controller.UserController
import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import com.TelemondoActivity1.TelemondoActivity1.model.User
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toEntity(dto: UserController.UserCreateDTO): User

    fun updateEntityFromDto(
        dto: UserController.UserUpdateDTO,
        @MappingTarget entity: User
    )
}