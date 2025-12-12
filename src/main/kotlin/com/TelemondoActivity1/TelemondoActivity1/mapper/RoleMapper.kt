package com.TelemondoActivity1.TelemondoActivity1.mapper

import com.TelemondoActivity1.TelemondoActivity1.controller.RoleController
import com.TelemondoActivity1.TelemondoActivity1.controller.UserController
import com.TelemondoActivity1.TelemondoActivity1.model.Role
import com.TelemondoActivity1.TelemondoActivity1.model.User
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface RoleMapper {
    fun toEntity(dto: RoleController.RoleCreateDTO): Role

    fun updateEntityFromDto(
        dto: RoleController.RoleUpdateDTO,
        @MappingTarget entity: Role
    )
}