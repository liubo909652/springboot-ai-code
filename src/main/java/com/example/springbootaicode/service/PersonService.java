package com.example.springbootaicode.service;

import com.example.springbootaicode.entity.Person;
import com.example.springbootaicode.mapper.PersonMapper;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonMapper personMapper;

    public PersonService(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    public Person getPersonById(Long id) {
        return personMapper.selectById(id);
    }
}
