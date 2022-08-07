package bg.softuni.jwt.web;

import bg.softuni.jwt.exception.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandler {

}