package com.example.bookrent.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    public static <E, D> D convertToDto(E entity, Class<D> dtoClass) {
        try {
            return dtoClass.getConstructor(entity.getClass()).newInstance(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting entity to DTO", e);
        }
    }

    public static <E, D> List<D> convertToDtoList(List<E> entities, Class<D> dtoClass) {
        return entities.stream()
                .map(entity -> convertToDto(entity, dtoClass))
                .collect(Collectors.toList());
    }

    public static <E, D> E convertToEntity(D dto, Class<E> entityClass) {
        try {
            return entityClass.getConstructor(dto.getClass()).newInstance(dto);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting DTO to entity", e);
        }
    }
}
