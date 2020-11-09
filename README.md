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

The functionality for masking payloads uses this [JSON Masker](https://github.com/michaelruocco/json-masker) library.
The integration tests for this repository make use of a demo application which can be found in the testFixtures module
that gives examples of how each of these filters can be used. More detailed explanations with examples can be found
below.

### Examples



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