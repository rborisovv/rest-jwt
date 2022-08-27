package bg.softuni.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

import static bg.softuni.jwt.common.FileConstant.USER_FOLDER;

@SpringBootApplication
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }
}