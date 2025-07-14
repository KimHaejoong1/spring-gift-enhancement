package gift.exception.handler;

import gift.exception.dto.ErrorResponseDto;
import gift.exception.AuthenticationException;
import gift.exception.AuthorizationException;
import gift.exception.LoginFailedException;
import gift.exception.ProductNotFoundException;
import gift.exception.WishlistException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FrontExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(
            ProductNotFoundException ex,
            Model model,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        model.addAttribute("errorInfo", error);

        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(
            MethodArgumentNotValidException ex,
            Model model,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage(),
                request.getRequestURI()
        );
        model.addAttribute("errorInfo", error);

        return "error";
    }

    @ExceptionHandler(LoginFailedException.class)
    public String handleLoginFailedException(
            LoginFailedException ex,
            Model model,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        model.addAttribute("errorInfo", error);

        return "error";
    }

    // 인증
    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(
            AuthenticationException ex,
            Model model,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        model.addAttribute("errorInfo", error);

        return "error";
    }

    // 인가
    @ExceptionHandler(AuthorizationException.class)
    public String handleAuthorizationException(
            AuthorizationException ex,
            Model model,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        model.addAttribute("errorInfo", error);

        return "error";
    }

    @ExceptionHandler(WishlistException.class)
    public String handleWishlistException(
            WishlistException ex,
            Model model,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        model.addAttribute("errorInfo", error);

        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(
            Exception ex,
            Model model,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                request.getRequestURI()
        );
        model.addAttribute("errorInfo", error);

        return "error";
    }
}