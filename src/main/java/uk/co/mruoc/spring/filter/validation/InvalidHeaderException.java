package uk.co.mruoc.spring.filter.validation;

import lombok.Getter;

@Getter
public class InvalidHeaderException extends RuntimeException {

    public InvalidHeaderException(String message) {
        super(message);
    }

}
