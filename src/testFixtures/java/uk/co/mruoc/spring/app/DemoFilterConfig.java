package uk.co.mruoc.spring.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import uk.co.mruoc.clock.FixedTimesClock;
import uk.co.mruoc.json.mask.JsonMasker;
import uk.co.mruoc.json.mask.JsonPathFactory;
import uk.co.mruoc.spring.filter.logging.mdc.ClearMdcFilter;
import uk.co.mruoc.spring.filter.logging.mdc.HeaderMdcPopulatorFilter;
import uk.co.mruoc.spring.filter.logging.mdc.RequestMdcPopulatorFilter;
import uk.co.mruoc.spring.filter.logging.request.RequestLoggingFilter;
import uk.co.mruoc.spring.filter.logging.request.TransformingRequestBodyExtractor;
import uk.co.mruoc.spring.filter.logging.response.ResponseLoggingFilter;
import uk.co.mruoc.spring.filter.logging.response.TransformingResponseBodyExtractor;
import uk.co.mruoc.spring.filter.logging.uritransform.TransformRequestUriMdcPopulatorFilter;
import uk.co.mruoc.spring.filter.logging.uritransform.UuidIdStringTransformer;

import java.time.Duration;
import java.time.Instant;

@Configuration
public class DemoFilterConfig {

    private static final String ENDPOINT1 = "/endpoint1";
    private static final String ENDPOINT2 = "/endpoint2/*";
    private static final String ENDPOINT3 = "/endpoint3";

    private static final Instant NOW = Instant.now();
    private static final Duration DURATION = Duration.ofMillis(15);

    private final FixedTimesClock clock = new FixedTimesClock(NOW, NOW.plus(DURATION));

    @Bean
    public FilterRegistrationBean<RequestMdcPopulatorFilter> requestMdcPopulator() {
        FilterRegistrationBean<RequestMdcPopulatorFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RequestMdcPopulatorFilter(clock));
        bean.setOrder(1);
        bean.addUrlPatterns(ENDPOINT1);
        bean.setName("requestMdcPopulator");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RequestLoggingFilter());
        bean.setOrder(2);
        bean.addUrlPatterns(ENDPOINT1);
        bean.setName("requestLoggingFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<ResponseLoggingFilter> responseLoggingFilter() {
        FilterRegistrationBean<ResponseLoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ResponseLoggingFilter());
        bean.setOrder(3);
        bean.addUrlPatterns(ENDPOINT1);
        bean.setName("responseLoggingFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<TransformRequestUriMdcPopulatorFilter> getUriTransformerFilter() {
        FilterRegistrationBean<TransformRequestUriMdcPopulatorFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new TransformRequestUriMdcPopulatorFilter(new UuidIdStringTransformer()));
        bean.setOrder(1);
        bean.addUrlPatterns(ENDPOINT2);
        bean.setName("uriTransformerFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<HeaderMdcPopulatorFilter> headerMdcPopulator() {
        FilterRegistrationBean<HeaderMdcPopulatorFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new HeaderMdcPopulatorFilter("example-header"));
        bean.setOrder(2);
        bean.addUrlPatterns(ENDPOINT2);
        bean.setName("headerMdcPopulator");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> maskingRequestLoggingFilter(ObjectMapper mapper) {
        FilterRegistrationBean<RequestLoggingFilter> bean = new FilterRegistrationBean<>();
        JsonMasker masker = JsonMasker.builder()
                .mapper(mapper)
                .paths(JsonPathFactory.toJsonPaths("$.maskedRequest"))
                .build();
        bean.setFilter(new RequestLoggingFilter(new TransformingRequestBodyExtractor(masker)));
        bean.setOrder(1);
        bean.addUrlPatterns(ENDPOINT3);
        bean.setName("maskingRequestLoggingFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<ResponseLoggingFilter> maskingResponseLoggingFilter(ObjectMapper mapper) {
        FilterRegistrationBean<ResponseLoggingFilter> bean = new FilterRegistrationBean<>();
        JsonMasker masker = JsonMasker.builder()
                .mapper(mapper)
                .paths(JsonPathFactory.toJsonPaths("$.maskedResponse"))
                .build();
        bean.setFilter(new ResponseLoggingFilter(new TransformingResponseBodyExtractor(masker)));
        bean.setOrder(2);
        bean.addUrlPatterns(ENDPOINT3);
        bean.setName("maskingResponseLoggingFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<ClearMdcFilter> clearMdcFilter() {
        FilterRegistrationBean<ClearMdcFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ClearMdcFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns("*");
        bean.setName("clearMdcFilter");
        return bean;
    }

}
