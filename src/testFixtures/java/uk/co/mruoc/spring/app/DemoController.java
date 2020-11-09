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
        log.info("log message from /endpoint controller");
        return "Greetings from /endpoint1";
    }

    @GetMapping("/endpoint2/{id}")
    public String get(@PathVariable("id") UUID id) {
        log.info("log message from /endpoint2 controller with id {}", id);
        return String.format("Greetings from /endpoint2 with id %s", id);
    }

    @PostMapping("/endpoint3")
    public String post(@RequestBody String body) {
        log.info("log message from /endpoint3 controller {}", body);
        return String.format("{\"maskedResponse\":%s}", body);
    }

    @GetMapping("/header-endpoint")
    public String getWithHeader() {
        return "Greetings from /header-endpoint";
    }

}
