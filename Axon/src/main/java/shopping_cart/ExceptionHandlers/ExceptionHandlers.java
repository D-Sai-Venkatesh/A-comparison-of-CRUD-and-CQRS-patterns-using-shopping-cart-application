package shopping_cart.ExceptionHandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import shopping_cart.coreapi.CheckoutCartException;
import shopping_cart.coreapi.ItemAlreadyAddedException;
import shopping_cart.coreapi.ItemNotFoundException;
import javax.security.auth.login.AccountNotFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ItemNotFoundException.class)
  public ResponseEntity<Object> ItemNotFound(ItemNotFoundException ex) {

    return buildResponseEntity(
        new ResponseError(UNAUTHORIZED, ex.getMessage())
    );
  }

//  @ExceptionHandler(ItemAlreadyAddedException.class)
//  public ResponseEntity<Object> ItemAlreadyAdded(ItemAlreadyAddedException ex) {
//    System.out.println("======================= Handeling Item Already Added +++++++++++");
//    return buildResponseEntity(
//        new ResponseError(UNAUTHORIZED, ex.getMessage())
//    );
//  }

  @ExceptionHandler(CheckoutCartException.class)
  public ResponseEntity<Object> CartAlreadyCheckOut(CheckoutCartException ex) {

    return buildResponseEntity(
        new ResponseError(UNAUTHORIZED, ex.getMessage())
    );
  }



  private ResponseEntity<Object> buildResponseEntity(ResponseError responseError) {
    return new ResponseEntity<>(responseError, responseError.getStatus());
  }

}
