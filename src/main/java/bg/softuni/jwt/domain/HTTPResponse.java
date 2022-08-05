package bg.softuni.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class HTTPResponse implements Serializable {
    private Integer httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}