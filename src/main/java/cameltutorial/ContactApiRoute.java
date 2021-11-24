package cameltutorial;

import cameltutorial.model.Contact;
import cameltutorial.service.ContactNotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

/**
 * Camel Route offering a Rest Api via Camel Rest DSL
 */
@Component
public class ContactApiRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .apiContextPath("api-doc")
                .apiComponent("swagger")
                .apiContextListing(true)
                .apiProperty("api.version", "1.0.0")
                .apiProperty("api.title", "Contact service");

        onException(ContactNotFoundException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "${exception.message}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setBody(simple("${exception.message}\n"));

        rest("/contacts")
                .get()
                    .description("Get a list of all Contacts")
                    .to("bean:contactService?method=getContacts")
                .get("{id}")
                    .description("Get a Contact by id")
                    .param().name("id").type(RestParamType.path).description("The id of the Contact to get").dataType("long").endParam()
                    .to("bean:contactService?method=getContact(${header.id})")
                .post().type(Contact.class)
                    .description("Create a new Contact")
                    .to("bean:contactService?method=createContact");
    }
}
