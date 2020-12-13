# Spring Filters

[![Build](https://github.com/michaelruocco/spring-filters/workflows/pipeline/badge.svg)](https://github.com/michaelruocco/spring-filters/actions)
[![codecov](https://codecov.io/gh/michaelruocco/spring-filters/branch/master/graph/badge.svg?token=oqKun1zNII)](https://codecov.io/gh/michaelruocco/spring-filters)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/84c95c60c3ff49fd8bba6c63a68efcd8)](https://www.codacy.com/gh/michaelruocco/spring-filters/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=michaelruocco/spring-filters&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/michaelruocco/spring-filters?branch=master)](https://bettercodehub.com/results/michaelruocco/spring-filters)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_spring-filters&metric=alert_status)](https://sonarcloud.io/dashboard?id=michaelruocco_spring-filters)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_spring-filters&metric=sqale_index)](https://sonarcloud.io/dashboard?id=michaelruocco_spring-filters)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_spring-filters&metric=coverage)](https://sonarcloud.io/dashboard?id=michaelruocco_spring-filters)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_spring-filters&metric=ncloc)](https://sonarcloud.io/dashboard?id=michaelruocco_spring-filters)
[![Download](https://api.bintray.com/packages/michaelruocco/maven/spring-filters/images/download.svg)](https://bintray.com/michaelruocco/maven/spring-filters/_latestVersion)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.michaelruocco/spring-filters.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.michaelruocco%22%20AND%20a:%22spring-filters%22)

## Overview

This library contains filters which can be useful when building restful web applications in spring boot. There
are filters that provide the following functionality:

*   Validating whether mandatory request headers have been provided

*   Validating whether request headers match a specified regex format

*   Adding common request parameters into the Mapped Diagnostic Context (MDC) including, request-method, request-uri, 
    request-duration and request-status

*   Adding specified request headers into the Mapped Diagnostic Context (MDC)

*   Clearing the Mapped Diagnostic Context (MDC) once a request has completed

*   Logging request body

*   Logging response body

*   Logging request body with transformations applied (e.g. masking specific json values within the payload)

*   Logging response body with transformations applied (e.g. masking specific json values within the payload)

*   Rewriting response body

The functionality for masking payloads uses this [JSON Masker](https://github.com/michaelruocco/json-masker) library.
The integration tests for this repository make use of a demo application which can be found in the testFixtures module
that gives examples of how each of these filters can be used. More detailed explanations with examples can be found
below.

### Examples

#### Header Validation

The header validation filter can be used to validate the headers on any incoming requests, you
need to pass it an instance of class implementing the HeaderValidator interface that performs the validation
logic, and an instance of the spring HandlerExceptionResolver class. This is so that you can add any handling
for the InvalidHeaderException thrown in the filter to the exception handler class in your application that
is marked with the @ControllerAdvice and @ExceptionHandler annotations so that you can handle filter exceptions
just like any other exceptions thrown from your application. The InvalidHeaderException message will contain a single
string of each of the error messages returned from the first failing validator separated by commas. In the example
below an error would be returned if a header named my-header is not provided or has an empty value

```java
Filter filter = new HeaderValidationFilter(new MandatoryHeaderValidator("my-header"), handlerExceptionResolver);
```

As well as validating mandatory headers, there is also a RegexHeaderValidator that checks the value of a header against
a provided regex. There is also a CompositeHeaderValidator that allows you to combine multiple validators. For example,
if you have a header that is mandatory, and that must contain a value matching a specific regex then you can use a
CompositeHeaderValidator to combine both a MandatoryHeaderValidator and RegexHeaderValidator to achieve this. The
contains a CorrelationIdHeaderValidator that is an example of this, it ensures a header named correlation-id is provided
and that the value is a valid UUID value. The constructor of the CorrelationIdHeaderValidator looks like this:

```java
public CorrelationIdHeaderValidator(String headerName) {
    super(
            new MandatoryHeaderValidator(headerName),
            new RegexHeaderValidator(headerName, Patterns.UUID)
    );
}
```

Upon receipt of a request that does not contain a correlation-id header, and a HeaderValidationFilter is configured
with the CorrelationIdHeaderValidator e.g:

```java
Filter filter = new HeaderValidationFilter(new CorrelationIdHeaderValidator(), handlerExceptionResolver);
```

Then the InvalidHeaderException that is throw would contain the following message "mandatory header correlation-id
not provided"

#### Header MDC Population

The HeaderMdcPopulatorFilter takes one or more header name strings and will look at each request to see if it contains
any of those headers it will populate them into the Mapped Diagnostic Context (MDC.)

#### Request MDC Population

The RequestMdcPopulatorFilter will populate 4 common values into the MDC, the first two upon receipt of the
request, are the request-method (e.g. POST, GET etc.) and the request-uri (e.g. /my-endpoint.) The second two upon
completion of the request, are the request-duration (in milliseconds) and the request-status (i.e. the http response
code e.g. 200 or 201 etc.)

#### Request and Response Body Logging

The RequestLoggingFilter and ResponseLoggingFilter can be used to log the incoming request body or outgoing response
body on each request. The default examples log the request and response bodies exactly as they are.

If you want to perform any modifications to your request or response bodies before logging you can configure
a TransformingRequestBodyExtractor or TransformingResponseBodyExtractor. For example, you might have some sensitive
values in your request or response body, but you still want to log the majority of the payload for debugging purposes.
If you combine the TransformingRequestBodyExtractor or TransformingResponseBodyExtractor with
[JSON Masker](https://github.com/michaelruocco/json-masker) then you could configure your request or response bodies
to mask specific fields within a JSON payload. TransformingRequestBodyExtractor and TransformingResponseBodyExtractor
takes a UnaryOperator<String> argument as the transformer, so of course you can write your own transformation logic too
if you wish. The integration tests contains working examples of masking request and response body filters e.g.

```java
JsonMasker masker = JsonMasker.builder()
        .mapper(mapper)
        .paths(JsonPathFactory.toJsonPaths("$.maskedRequest"))
        .build();
Filter filter = new RequestLoggingFilter(new TransformingRequestBodyExtractor(masker)));
```

#### Rewriting Response Body

The RewriteResponseBodyFilter can be used to change / modify the response body. The filter accepts a class
that implements the RewriteResponseBody interface, which extends the Function interface. The input argument is
a RewriteResponseBodyRequest which contains both the original request and original response. The reason for this
is that it gives the logic that you rewrite to perform the rewrite access to the request body, request method, request
parameters, and the response body and response status. Once you implement the RewriteResponseBody interface with
your logic to rewrite the response body you simple pass that to the filter and configure it into your application.
If you want to log the rewritten response body you can do that from your implementation class too. The demo application
used in the integration tests contains a working example to follow. This is created by doing the following:

```java
RewriteResponseBody rewriteFunction = new DemoRewriteResponseBody();
Filter filter = new RewriteResponseBodyFilter(rewriteFunction);
```

## Useful Commands

```gradle
// cleans build directories
// prints currentVersion
// checks dependency versions
// formats code
// builds code
// runs unit tests
// runs integration tests
./gradlew clean currentVersion dependencyUpdates spotlessApply build integrationTest
```