package cameltutorial;

import cameltutorial.model.Contact;
import cameltutorial.service.ContactNotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
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
                .dataFormatProperty("prettyPrint", "true")
                .bindingMode(RestBindingMode.json);

        onException(ContactNotFoundException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "${exception.message}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setBody(simple("${exception.message}\n"));

        rest("/contacts")
                .get()
                    .to("bean:contactService?method=getContacts")
                .get("{id}")
                    .to("bean:contactService?method=getContact(${header.id})")
                .post().type(Contact.class)
                    .to("bean:contactService?method=createContact");
    }
}
