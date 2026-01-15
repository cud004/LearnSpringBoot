package duc.demo.exception;


import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(Exception ex, WebRequest request)
    {
        System.out.println("===============> handleValidationException");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());


        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf('[');
            int end = message.lastIndexOf(']');
            message = message.substring(start + 1, end - 1);
            errorResponse.setError("Payload invalid");
        }
        else if (ex instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(": ") + 2);
            errorResponse.setError("PathVariable invalid");

        }
        else if (ex instanceof MethodArgumentTypeMismatchException) {
            errorResponse.setMessage("Failed to convert value of type " );

        }

        errorResponse.setMessage(message);
        return  errorResponse;
    }


//    @ExceptionHandler({})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleInternalServerErrorExceptions(Exception ex, WebRequest request)
//    {
//        System.out.println("===============> handleInternalServerErrorExceptions");
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setTimestamp(new Date());
//        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
//        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//
//        if (ex instanceof MethodArgumentTypeMismatchException) {
//            errorResponse.setMessage("Failed to convert value of type " );
//        }
//        return  errorResponse;
//    }






}
