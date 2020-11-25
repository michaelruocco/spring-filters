package uk.co.mruoc.spring.app;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.spring.filter.logging.request.RequestBodyExtractor;
import uk.co.mruoc.spring.filter.logging.request.SimpleRequestBodyExtractor;
import uk.co.mruoc.spring.filter.rewrite.RewriteResponseBody;
import uk.co.mruoc.spring.filter.rewrite.RewriteResponseBodyRequest;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
public class DemoRewriteResponseBody implements RewriteResponseBody {

    private final RequestBodyExtractor requestBodyExtractor;

    public DemoRewriteResponseBody() {
        this(new SimpleRequestBodyExtractor());
    }

    @Override
    public String apply(RewriteResponseBodyRequest rewriteRequest) {
        String requestBody = toRequestBody(rewriteRequest);
        boolean rewrite = JsonPath.parse(requestBody).read("$.rewrite");

        String responseBody = rewriteRequest.getResponseBody();
        if (!rewrite) {
            return responseBody;
        }
        String rewrittenBody = responseBody.replace("originalValue", "rewrittenValue");
        log.info("response-rewritten-as:{}", rewrittenBody);
        return rewrittenBody;
    }

    private String toRequestBody(RewriteResponseBodyRequest rewriteRequest) {
        HttpServletRequest request = rewriteRequest.getRequest();
        return requestBodyExtractor.extractBody(request);
    }

}
