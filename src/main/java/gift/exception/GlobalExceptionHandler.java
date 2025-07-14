package gift.exception;

import gift.dto.itemDto.ItemCreateDto;
import gift.dto.itemDto.ItemUpdateDto;
import gift.exception.itemException.ItemNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationError(MethodArgumentNotValidException e, Model model) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            System.out.println("fieldError = " + fieldError);
        }

        String errorMessage = "오류 발생";
        if (!fieldErrors.isEmpty()) {
            errorMessage = fieldErrors.get(0).getDefaultMessage();
        }
        model.addAttribute("errorMessage", errorMessage);

        Object rejectedDTO = e.getBindingResult().getTarget();

        if (rejectedDTO instanceof ItemCreateDto itemDTO) {
            model.addAttribute("itemDTO", itemDTO);
            return "admin/createForm";

        } else if (rejectedDTO instanceof ItemUpdateDto itemDTO) {
            model.addAttribute("itemDTO", itemDTO);
            return "admin/editForm";

        } else {
            model.addAttribute("itemDTO", new ItemCreateDto("", 0, "", false));
            return "admin/createForm";
        }
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public String handleSearchItemError(ItemNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "admin/list";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherErrors(Exception e, Model model) {
        model.addAttribute("errorMessage", "오류 발생");
        model.addAttribute("itemDTO", new ItemCreateDto("", 0, "", false));
        return "admin/createForm";
    }
}
