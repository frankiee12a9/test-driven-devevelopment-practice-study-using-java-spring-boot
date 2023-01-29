## Test-driven devevelopment practice study using java spring boot
As the project name describes, this project is created using Java-based [`Spring boot`](https://spring.io/projects/spring-boot) for [`Test-driven Development`]() practice study and for FUN.
Project development is separated into 2 types, one is [`MVC`](https://github.com/kkk12a9/Test-Driven-devevelopment-practice-study-using-java-spring-boot/tree/master/spring-boot-mvc/tdd)
with [`Thymeleaf`](https://www.thymeleaf.org/) as template engine, and the other one is [`REST-API`](https://github.com/kkk12a9/Test-Driven-devevelopment-practice-study-using-java-spring-boot/tree/master/spring-boot-rest-api/movie-api/src).

## Testing Overview
### Unit testing
* Testing an individual unit of code for correctness
* Provide fixed inputs
* Expect predictable output

### Unit testing Pros 
1. `Early detection of errors`: Unit tests can help developers identify and fix bugs early in the development process, before they make their way into the final product.
2. `Improved code quality`: By breaking code down into smaller, testable units, developers can ensure that each component is working as intended and that the overall codebase is more robust.
3. `Easier maintenance`: Unit tests provide a safety net for future changes, making it easier to refactor and update code without introducing new bugs.

### Unit testing Cons
1. `Additional development time`: Writing unit tests can add to the development time, which may not be feasible for all projects or teams.
2. `Limited scope`: Unit tests only test small, individual components of the code and may not catch integration or system-level issues.
3. `False sense of security`: If unit tests are not written or maintained properly, they may give developers a false sense of security that the code is working correctly when it is not.
4. `Test maintenance`: As the codebase changes, the unit test must be updated as well, this can be a time-consuming task and must be taken into account.

### Integration testing
* Test multiple components together as part of a test plan
* Determine if software units work together as expected
* Identify any negative side effects due to integration
* Can test using mocks / stubs
* Can also test using live integrations (database, file system)

## Unit Testing Frameworks 
* __Junit__
  * Supports creating test cases
  * Automation of the test cases with pass / fail
  * Utilities for test setup, teardown and assertions, etc..
* __Mockito__
  * Create mocks and stubs
  * Minimize dependencies on external components, etc...

## Prerequisites
* Java 17
* Springboot 2x
* Maven 3x

