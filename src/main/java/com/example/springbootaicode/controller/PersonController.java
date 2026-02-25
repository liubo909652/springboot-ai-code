package com.example.springbootaicode.controller;

import com.example.springbootaicode.entity.Person;
import com.example.springbootaicode.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public Person getPersonInfo(@PathVariable Long id) {
        return personService.getPersonById(id);
    }
}
