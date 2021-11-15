# Usage of Camel Rest DSL within a Spring Boot project

This project shows how to expose Rest endpoints by using the Camel Rest DSL. 
For a comparison the same Rest endpoints are also implemented with Spring Boot.

Benefits of Camel Rest DSL:
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