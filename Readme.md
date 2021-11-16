# Usage of Camel Rest DSL within a Spring Boot project

This project shows how to expose Rest endpoints by using the Camel Rest DSL. 
For a comparison the same Rest endpoints are also implemented with Spring Boot.

## Overview of Camel Rest DSL

The Rest endpoints are defined using Camel DSL.
In the background Camel creates regular Camel routes.

### Rest configuration

- Component - The component is needed to handle the task of hosting the service.
  In this example camel-servlet is used since the application is running in a servlet container.
- BindingMode is necesary to bind e.g. json to Pojo classes
  In the background a RestBindProcessor is injected into each Camel route as the first processor.

With using Spring Boot this configuration could have also been done in the application.properties. e.g.
camel.rest.component=servlet

### Rest endpoints

The rest endpoints are described using the REST methods get, post, ...
In the to-method they are forwarded to a class where the logic is implemented.

### Mapping
The getContacts method returns a list of contacts.
The mapping to json is done automatically from the RestBindProcessor.
For the post method we need to specify the Type Contact.class, so that the Processor knows in which class to map the incoming json.

### Exceptionhandling
The Exceptionhandling is done with a declarative approach. 
Exceptions occuring in the service implementation are catched and transformed into http response codes. 
More onXYExceptions can be implemented. 
And also onException for any other not caught Exception

### Documentation
The service can also be documented using Swagger.
This can be done with the method .apicontextPath("api-doc") in the rest configuration.
Each method can then be documented with the method .description("Method description").

## Benefits of Camel Rest DSL
- Can be used independently from the underlying container. e.g. also usable with Quarkus.
- Simple DSL to describe the REST endpoints
- Clear errorhandling

# Overview of the REST endpoints

## SpringBoot REST service

- Get all contacts - `http://localhost:8080/spring/contacts`
- Get a specific contact - `http://localhost:8080/spring/contacts/2`
- Create a new contact - `http://localhost:8080/spring/contacts`

## Camel REST DSL service

- Get all contacts - `http://localhost:8080/camel/contacts`
- Get a specific contact - `http://localhost:8080/camel/contacts/2`
- Create a new contact - `http://localhost:8080/camel/contacts`


Message body for creating a contact:

`{
"firstName": "David",
"lastName": "Muster",
"postalCode": "3037",
"city": "Bern"
}`

# Documentation

- `https://camel.apache.org/manual/rest-dsl.html`