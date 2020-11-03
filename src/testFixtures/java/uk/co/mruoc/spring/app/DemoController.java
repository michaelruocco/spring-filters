package uk.co.mruoc.spring.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class DemoController {

    @GetMapping("/endpoint1")
    public String get() {
        log.info("log message from get hello controller");
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/endpoint2/{id}")
    public String get(@PathVariable("id") UUID id) {
        log.info("log message from get by id hello controller {}", id);
        return String.format("Greetings from Spring Boot! %s", id);
    }

    @PostMapping("/endpoint3")
    public String post(@RequestBody String body) {
        log.info("log message from post hello controller {}", body);
        return String.format("{\"maskedResponse\":%s}", body);
    }

}
