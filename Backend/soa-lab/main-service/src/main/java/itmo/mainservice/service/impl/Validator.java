package itmo.mainservice.service.impl;

import itmo.library.*;
import itmo.mainservice.exception.BadPageableException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Validator {
    public static final Logger logger = LoggerFactory.getLogger(Validator.class);

    public static int validatePageable(String page) throws BadPageableException, NumberFormatException {
        logger.info("Validating pageable " + page);
        if (page == null) throw new BadPageableException("Page and pagesize must be present!, ");
        int res = Integer.parseInt(page);
        if (res <= 0) throw new BadPageableException("Page and pagesize must be a positive number, ");
        return res;
    }

    public static void validateNewFlatRequest(FlatCreateDTO dto) throws ValidationException {
        logger.info("Validating new flat request");
        StringBuilder stringBuilder = new StringBuilder();
        validateName(dto.getName(), stringBuilder);
        validateCoordinates(dto, stringBuilder);
        if (dto.getArea() == null || dto.getArea() <= 0 || dto.getArea() > 527)
            stringBuilder.append("Area must be more than 0 and less than 527! , ");
        if (dto.getNumberOfRooms() == null || dto.getNumberOfRooms() <= 0)
            stringBuilder.append("Number of room must be more than 0, ");
        if (!Furnish.isValid(dto.getFurnish())) stringBuilder.append("Furnish value is wrong!, ");
        if (!View.isValid(dto.getView())) stringBuilder.append("View value is wrong!, ");
        if (!Transport.isValid(dto.getTransport())) stringBuilder.append("Transport value is wrong!, ");
        validateHouse(dto.getHouse(), stringBuilder);
        if (dto.getPrice() == null || dto.getPrice() <= 0) stringBuilder.append("Price must be a positive number, ");
        if (dto.getHasBalcony() == null) stringBuilder.append("Has Balcony boolean can't be null!, ");
        if (!validateBoolean(dto.getHasBalcony()))
            stringBuilder.append("Has Balcony boolean must be either true or false, ");


        if(!stringBuilder.isEmpty()) throw new ValidationException(stringBuilder.toString());
    }

    private static void validateCoordinates(FlatCreateDTO dto, StringBuilder stringBuilder) {
        if (dto.getCoordinates() == null) stringBuilder.append("Coordinates must not be null, ");
        else{
            if(dto.getCoordinates().getX() == null) stringBuilder.append("Coordinates X must not be null, ");
            else if (dto.getCoordinates().getX() > 548) stringBuilder.append("Coordinate X must not be more than 548, ");
            if(dto.getCoordinates().getY() == null) stringBuilder.append("Coordinates Y must not be null, ");
        }
    }

    private static boolean validateBoolean(String hasBalcony) {
        return hasBalcony.equalsIgnoreCase("true") || hasBalcony.equalsIgnoreCase("false");
    }

    public static void validateHouse(House house, StringBuilder stringBuilder) throws ValidationException {
        validateName(house.getName(), stringBuilder);
        if (house.getYear() == null || house.getYear() <= 0 || house.getYear() > 634)
            stringBuilder.append("Year of the house more than 0 and less than 634, ");
        if (house.getNumberOfFloors() == null || house.getNumberOfFloors() <= 0)
            stringBuilder.append("Number of floors must be a positive integer, ");
    }

    public static void validateHouse(House house) throws ValidationException {
        StringBuilder stringBuilder = new StringBuilder();
        validateName(house.getName(), stringBuilder);
        if (house.getYear() == null || house.getYear() <= 0 || house.getYear() > 634)
            stringBuilder.append("Year of the house more than 0 and less than 634, ");
        if (house.getNumberOfFloors() == null || house.getNumberOfFloors() <= 0)
            stringBuilder.append("Number of floors must be a positive integer, ");
    }

    private static void validateName(String name, StringBuilder stringBuilder) throws ValidationException {
        if (name == null || name.isEmpty() || name.isBlank())
            stringBuilder.append("New element's name cannot be empty, ");
        else if (name.length() > 255) stringBuilder.append("Element name can't have more than 255 letters, ");
    }



    public static void validateFilterList(List<Filter> filterList, Class<?> clazz) {
        logger.info("Validating filter list");
        StringBuilder stringBuilder = new StringBuilder();
        for(Filter filter : filterList){
            if (!isFieldPresent(clazz, filter.getFieldName())) stringBuilder.append(clazz.getSimpleName()).append(" does not have field ").append(filter.getFieldName()).append(", ");
            else if (filter.getNestedName() != null && !filter.getNestedName().isEmpty()) {
                try{
                    Class<?> nestedClass = clazz.getDeclaredField(filter.getFieldName()).getType();
                    logger.info("Nested class --> {}", nestedClass.getName());
                    if (!isFieldPresent(nestedClass, filter.getNestedName())) stringBuilder.append(clazz.getSimpleName()).append(" nested class can't be ").append(filter.getFieldName()).append(".").append(filter.getNestedName()).append(", ");
                } catch (NoSuchFieldException e){
                    stringBuilder.append(clazz.getSimpleName()).append(" nested class couldn't find field ").append(filter.getFieldName()).append(", ");
                }
            }
            if (clazz.getSimpleName().equalsIgnoreCase("Flat")){
                switch (filter.getFieldName()){
                    case "furnish":
                        if (!Furnish.isValid(filter.getFieldValue())) stringBuilder.append("Flat furnish can't be ").append(filter.getFieldValue()).append(", ");
                        break;
                    case "view":
                        if (!View.isValid(filter.getFieldValue())) stringBuilder.append("Flat view can't be ").append(filter.getFieldValue()).append(", ");
                        break;
                    case "transport":
                        if (!Transport.isValid(filter.getFieldValue())) stringBuilder.append("Flat transport can't be ").append(filter.getFieldValue()).append(", ");
                        break;
                    case "balcony":
                        if (!filter.getFieldValue().equalsIgnoreCase("true") && !filter.getFieldValue().equalsIgnoreCase("false")) stringBuilder.append("Balcony can't be ").append(filter.getFieldValue());
                        break;
                }
            }
        }
        if (!stringBuilder.isEmpty()) throw new ValidationException(stringBuilder.toString());
    }

    public static void validateSortList(List<Sort> sortList, Class<?> clazz) {
        logger.info("Validating sort list");
        StringBuilder stringBuilder = new StringBuilder();
        for (Sort sort : sortList){
            if (!isFieldPresent(clazz, sort.getFieldName())) stringBuilder.append(clazz.getSimpleName()).append(" field can't be ").append(sort.getFieldName()).append(", ");
            else if (sort.getNestedName() != null && !sort.getNestedName().isEmpty()){
                try{
                    Class<?> nestedClass = clazz.getDeclaredField(sort.getFieldName()).getType();
                    logger.info("Nested class --> {}", nestedClass.getName());
                    if (!isFieldPresent(nestedClass, sort.getNestedName())) stringBuilder.append(clazz.getSimpleName()).append(" nested class can't be ").append(sort.getFieldName()).append(".").append(sort.getNestedName()).append(", ");
                } catch (NoSuchFieldException e){
                    stringBuilder.append(clazz.getSimpleName()).append(" nested class couldn't find field ").append(sort.getFieldName()).append(", ");
                }
            }
        }
        if (!stringBuilder.isEmpty()) throw new ValidationException(stringBuilder.toString());
    }


    private static boolean isFieldPresent(Class<?> clazz, String field){
        return Arrays.stream(clazz.getDeclaredFields())
                .map(e -> e.getName().toLowerCase())
                .anyMatch(e -> e.equalsIgnoreCase(field));
    }
}