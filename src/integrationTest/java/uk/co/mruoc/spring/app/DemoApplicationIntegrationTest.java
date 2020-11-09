package uk.co.mruoc.spring.app;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import uk.co.mruoc.file.line.LineLoader;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    private String baseUrl;

    private static final File LOG_FILE = new File("./logs/test.log");

    @BeforeAll
    @AfterAll
    public static void deleteTestLog() {
        FileUtils.deleteQuietly(LOG_FILE);
    }

    @BeforeEach
    public void setUp() {
        baseUrl = buildBaseUrl();
    }

    @Test
    public void shouldRequestMethodRequestUriRequestDurationAndRequestStatusWithRequestAndResponseLog() {
        String url = String.format("%s/endpoint1", baseUrl);

        template.getForEntity(url, String.class);

        String lastExpectedLog = "[GET:/endpoint1::15:200:] INFO  GET /endpoint1 took 15ms to return status 200";
        waitForLog(lastExpectedLog);

        List<String> lines = loadLogLines();
        assertContainsLineStartingWith(lines, "[GET:/endpoint1::::] INFO  received-request::headers:{connection=[keep-alive], host=[localhost:");
        assertContainsLineEndingWith(lines, "], accept=[text/plain, application/json, application/*+json, */*]}");
        assertThat(lines).contains(
                "[GET:/endpoint1::::] INFO  log message from /endpoint controller",
                "[GET:/endpoint1::::] INFO  returned-response:Greetings from /endpoint1:headers:{}",
                lastExpectedLog
        );
    }

    @Test
    public void shouldLogTransformedRequestUriAndHeaderInMdc() {
        String id = "dbdb1bd4-d621-4507-b687-6868225c719f";
        String url = String.format("%s/endpoint2/%s", baseUrl, id);
        HttpHeaders headers = new HttpHeaders();
        headers.put("example-header", Collections.singletonList("example-value"));
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        template.exchange(url, HttpMethod.GET, entity, String.class);

        String expectedLog = "[::/endpoint2/{id}:::example-value] INFO  log message from /endpoint2 controller with id dbdb1bd4-d621-4507-b687-6868225c719f";
        waitForLog(expectedLog);

        List<String> lines = loadLogLines();
        assertThat(lines).contains(expectedLog);
    }

    @Test
    public void shouldMaskRequestAndResponsePayload() {
        String url = String.format("%s/endpoint3", baseUrl);
        HttpEntity<String> entity = new HttpEntity<>("{\"maskedRequest\":\"value-to-mask\"}");

        template.exchange(url, HttpMethod.POST, entity, String.class);

        String expectedLog = "[:::::] INFO  returned-response:{\"maskedResponse\":{\"maskedRequest\":\"*************\"}}:headers:{}";
        waitForLog(expectedLog);

        List<String> lines = loadLogLines();
        assertContainsLineStartingWith(lines, "[:::::] INFO  received-request:{\"maskedRequest\":\"*************\"}:headers:{content-length=[33], connection=[keep-alive], host=[localhost:");
        assertContainsLineEndingWith(lines, "], content-type=[text/plain;charset=UTF-8], accept=[text/plain, application/json, application/*+json, */*]}");
        assertThat(lines).contains(expectedLog);
    }

    @Test
    public void shouldReturnAnErrorIfMandatoryHeaderNotProvided() {
        String url = String.format("%s/header-endpoint", baseUrl);

        ResponseEntity<String> response = template.getForEntity(url, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("mandatory header correlation-id not provided");
    }

    @Test
    public void shouldReturnSuccessfulResultIfMandatoryHeaderProvided() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Correlation-ID", Collections.singletonList(UUID.randomUUID().toString()));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = String.format("%s/header-endpoint", baseUrl);

        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Greetings from /header-endpoint");
    }

    private void waitForLog(String expectedLog) {
        await().atMost(Duration.ofSeconds(1))
                .pollInterval(Duration.ofMillis(50))
                .until(() -> loadLogLines().contains(expectedLog));
    }

    private String buildBaseUrl() {
        return String.format("http://localhost:%d/", port);
    }

    private List<String> loadLogLines() {
        return LineLoader.loadLinesFromFileSystem(LOG_FILE.getAbsolutePath())
                .stream().map(String::trim)
                .collect(Collectors.toList());
    }

    private static void assertContainsLineStartingWith(Collection<String> lines, String prefix) {
        assertThat(lines.stream().anyMatch(line -> line.startsWith(prefix))).isTrue();
    }

    private static void assertContainsLineEndingWith(Collection<String> lines, String suffix) {
        assertThat(lines.stream().anyMatch(line -> line.endsWith(suffix))).isTrue();
    }

}
