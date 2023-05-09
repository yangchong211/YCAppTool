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

    public static class Person {
        private String name;
        private int id;
        private String email;

        private List<PhoneNumber> phones;

        public Person() {
            phones = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }


        public void addPhones(PhoneNumber number) {
            phones.add(number);
        }

        public List<PhoneNumber> getPhones() {
            return phones;
        }
    }

    public static class PhoneNumber {
        enum PhoneType {
            MOBILE,
            HOME,
            WORK;
        }

        private String number;
        private PhoneType type;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public PhoneType getType() {
            return type;
        }

        public void setType(PhoneType type) {
            this.type = type;
        }
    }
}
