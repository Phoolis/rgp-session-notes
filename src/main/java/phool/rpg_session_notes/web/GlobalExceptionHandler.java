package phool.rpg_session_notes.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

}
