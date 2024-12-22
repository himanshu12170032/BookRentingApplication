package com.example.bookrent.Converter;

import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;


public class Converter {
    private static final ModelMapper modelMapper = new ModelMapper();
    public static <E, D> D convertToDto(E entity, Class<D> dtoClass) {
        try {
            return modelMapper.map(entity,dtoClass);
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
            return modelMapper.map(dto, entityClass);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting DTO to entity", e);
        }
    }
}
