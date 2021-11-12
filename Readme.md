# Camel SpringBoot 

This project shows how to create REST services with SpringBoot and Camel REST DSL.

## SpringBoot REST service

- Get all contacts - `http://localhost:8080/spring/contacts`
- Get a specific contact - `http://localhost:8080/spring/contacts/2`
- Create a new contact - `http://localhost:8080/spring/contacts`
`
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
