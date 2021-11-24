package cameltutorial;

import cameltutorial.model.Contact;
import cameltutorial.service.ContactService;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spi.Registry;
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Tests the Camel Rest endpoints.
 * The call to the ContactService is intercepted using AdviceRouteBuilder. Within this advice the call is directed to a MockEndpoint.
 * With this approach only the route itself is tested.
 */
public class ContactApiRouteTest extends CamelTestSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactApiRouteTest.class);

    @BeforeEach
    public void mockEndpoints() throws Exception {
        AdviceWithRouteBuilder getContactsRoute = new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:getContacts");

                interceptSendToEndpoint("bean:contactService?method=getContacts")
                        .skipSendToOriginalEndpoint()
                        .to("mock:getContacts");
            }
        };
        context.adviceWith(getRouteDefinition("rest:get:/contacts?"), getContactsRoute);

        AdviceWithRouteBuilder getContactRoute = new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:getContact");

                interceptSendToEndpoint("bean:contactService?method=getContact(${header.id})")
                        .skipSendToOriginalEndpoint()
                        .to("mock:getContact");
            }
        };
        context.adviceWith(getRouteDefinition("rest:get:/contacts:{id}?"), getContactRoute);

        AdviceWithRouteBuilder createContactRoute = new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:createContact");

                interceptSendToEndpoint("bean:contactService?method=createContact")
                        .skipSendToOriginalEndpoint()
                        .to("mock:createContact");
            }
        };
        context.adviceWith(getRouteDefinition("rest:post:/contacts?"), createContactRoute);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new ContactApiRoute();
    }

    @Override
    protected Registry createCamelRegistry() throws Exception {
        Registry registry = new DefaultRegistry();
        registry.bind("contactService", new ContactService());
        return registry;
    }

    @Test
    public void testGetContacts() throws Exception {
        MockEndpoint mockGetContacts = getMockEndpoint("mock:getContacts");

        context.start();
        mockGetContacts.expectedMessageCount(1);

        template.sendBodyAndHeader("direct:getContacts", "", "", "");

        // Verify mock
        mockGetContacts.assertIsSatisfied();
        context.stop();
    }

    @Test
    public void testGetContact() throws Exception {
        MockEndpoint mockGetContact = getMockEndpoint("mock:getContact");

        context.start();
        mockGetContact.expectedMessageCount(1);
        mockGetContact.expectedHeaderReceived("id", 1);

        template.sendBodyAndHeader("direct:getContact", "", "id", "1");

        // Verify mock
        mockGetContact.assertIsSatisfied();
        context.stop();
    }

    @Test
    public void testCreateContact() throws Exception {
        MockEndpoint mockCreateContact = getMockEndpoint("mock:createContact");

        context.start();
        mockCreateContact.expectedMessageCount(1);
        Contact expectedContact = new Contact();
        expectedContact.setFirstName("David");
        expectedContact.setLastName("Muster");
        expectedContact.setPostalCode("3037");
        expectedContact.setCity("Bern");
        mockCreateContact.expectedBodiesReceived(Collections.singletonList(expectedContact));

        String newContactJson = "{\n" +
                "  \"firstName\": \"David\",\n" +
                "  \"lastName\": \"Muster\",\n" +
                "  \"postalCode\": \"3037\",\n" +
                "  \"city\": \"Bern\"\n" +
                "}";
        template.sendBody("direct:createContact", newContactJson);

        // Verify mock
        mockCreateContact.assertIsSatisfied();
        context.stop();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    private RouteDefinition getRouteDefinition(String restDefinition) {
        for (RouteDefinition routeDefinition : context.getRouteDefinitions()) {
            if (routeDefinition.getInput().getUri().contains(restDefinition)) {
                return routeDefinition;
            }
        }

        return null;
    }
}
