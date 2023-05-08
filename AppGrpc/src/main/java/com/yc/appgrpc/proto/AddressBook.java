package com.yc.appgrpc.proto;

import java.util.ArrayList;
import java.util.List;


public class AddressBook {
    List<Person> persons;

    public AddressBook() {
        this.persons = new ArrayList<>();
    }

    public void addPersons(Person person) {
         persons.add(person);
    }

    public List<Person> getPersons( ) {
        return persons;
    }
}
