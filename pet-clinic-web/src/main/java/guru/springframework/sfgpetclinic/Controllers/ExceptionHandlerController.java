package guru.springframework.sfgpetclinic.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.webjars.NotFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundError(Exception exception, Model model){
        log.error("handling NotFoundError");
        log.error(exception.getMessage());

        model.addAttribute("exception", exception);

        return "404error";
    }
}
