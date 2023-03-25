package soccerapp.webapi.exceptions;

public class ApiRequestLimitReachedException extends RuntimeException {

    public ApiRequestLimitReachedException(String message) {
        super(message);
    }
}
