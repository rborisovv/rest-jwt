package bg.softuni.jwt.domain;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class HTTPResponse {
    private Integer httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}