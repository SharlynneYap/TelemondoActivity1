package com.TelemondoActivity1.TelemondoActivity1.mapper

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController
import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface ClassmateMapper {
    fun toEntity(dto: ClassmateController.ClassmateCreateDTO): Classmate

    fun updateEntityFromDto(
        dto: ClassmateController.ClassmateUpdateDTO,
        @MappingTarget entity: Classmate
    )

    // Response dto
    fun toResponseDto(entity: Classmate): ClassmateController.ClassmateResponseDTO
    fun toResponseDtoList(entities: List<Classmate>): List<ClassmateController.ClassmateResponseDTO>
}