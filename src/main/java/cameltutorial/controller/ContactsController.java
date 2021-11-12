package cameltutorial.controller;

import cameltutorial.model.Contact;
import cameltutorial.service.ContactNotFoundException;
import cameltutorial.service.ContactService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/spring")
public class ContactsController {

    private static Logger LOGGER = LoggerFactory.getLogger(ContactsController.class);

    private ContactService contactService;

    @GetMapping("/contacts")
    public Collection<Contact> getContacts() {
        LOGGER.info("getContacts called...");
        return this.contactService.getContacts();
    }

    @GetMapping("/contacts/{id}")
    public Contact getContact(@PathVariable Long id) {
        LOGGER.info("getContact with id " + id + " called...");
        try {
            return contactService.getContact(id);
        } catch (ContactNotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/contacts")
    public Contact createContact(@RequestBody Contact contact) {
        LOGGER.info("createContact called...");
        Contact createdContact = contactService.createContact(contact);
        return createdContact;
    }
}
