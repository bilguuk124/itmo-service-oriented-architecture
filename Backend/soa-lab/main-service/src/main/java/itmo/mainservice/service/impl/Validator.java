package itmo.mainservice.service.impl;

import itmo.library.*;
import itmo.mainservice.exception.BadPageableException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Validator {
    public static final Logger logger = LoggerFactory.getLogger(Validator.class);

    public static int validatePageable(String page) throws BadPageableException, NumberFormatException {
        logger.info("Validating pageable " + page);
        if (page == null) throw new BadPageableException("Page and pagesize must be present!");
        int res = Integer.parseInt(page);
        if (res <= 0) throw new BadPageableException("Page and pagesize must be a positive number");
        return res;
    }

    public static void validateNewFlatRequest(FlatCreateDTO dto) throws ValidationException {
        logger.info("Validating new flat request");
        validateName(dto.getName());
        validateCoordinates(dto);
        if (dto.getArea() == null|| dto.getArea() <= 0 || dto.getArea() > 527) throw new ValidationException("Area must be more than 0 and less than 527");
        if (dto.getNumberOfRooms() == null || dto.getNumberOfRooms() <= 0) throw new ValidationException("Number of room must be more than 0");
        if (!Furnish.isValid(dto.getFurnish())) throw new ValidationException("Furnish value is wrong!");
        if (!View.isValid(dto.getView())) throw new ValidationException("View value is wrong!");
        if (!Transport.isValid(dto.getTransport())) throw new ValidationException("Transport value is wrong!");
        validateHouse(dto.getHouse());
        if (dto.getPrice() <= 0) throw new ValidationException("Price must be a positive number");
        if (dto.getHasBalcony() == null ) throw new ValidationException("Has Balcony boolean can't be null!");
        if (!validateBoolean(dto.getHasBalcony())) throw new ValidationException("Has Balcony boolean must be either true or false");
    }

    private static boolean validateBoolean(String hasBalcony) {
        return hasBalcony.equalsIgnoreCase("true") || hasBalcony.equalsIgnoreCase("false");
    }

    public static void validateHouse(House house) throws ValidationException {
        validateName(house.getName());
        if (house.getYear() == null || house.getYear() <= 0 || house.getYear() > 634) throw new ValidationException("Year of the house more than 0 and less than 634");
        if (house.getNumberOfFloors() == null || house.getNumberOfFloors() <= 0) throw new ValidationException("Number of floors must be a positive integer");
    }

    private static void validateName(String name) throws ValidationException {
        if (name == null || name.isEmpty() || name.isBlank()) throw new ValidationException("New Flat's name cannot be empty");
        if (name.length() > 255) throw new ValidationException("Flat name can't have more than 255 letters");
    }

    private static void validateCoordinates(FlatCreateDTO dto) throws ValidationException {
        if (dto.getCoordinates() == null) throw new ValidationException("Coordinates can't be null");
        if (dto.getCoordinates().getX() > 548) throw new ValidationException("Coordinates X must be less than 548.");
        if (dto.getCoordinates().getY() == null) throw new ValidationException("Coordinates Y must be a number!");
    }
}