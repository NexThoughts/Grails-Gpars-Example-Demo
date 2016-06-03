package com.gpars

import grails.transaction.Transactional

@Transactional
class PersonService {

    Person createPerson(String name, Long age, Integer addressCount = 5) {
        Person person = new Person()
        person.name = name
        person.age = age
        if (person.save(flush: true)) {
            println "******** Person -> ${person.name} : ${person.id}  **********"
            addressCount.times { Integer index ->
                assignAddress(person, index)
            }
        }
        return person
    }

    Address assignAddress(Person person, Integer index) {
        Address address = new Address()
        address.locality = "${person.name} Locality"
        address.postalCode = 201301
        address.person = person
        person.addToAddresses(address)
        address.save(flush: true)
        println "******########## Address ${index} for Person : ${person.id} **********"
        return address
    }

    List<Person> populatePersonList(Integer size) {
        List<Person> personList = []
        Thread.sleep(500)
        size.times {
            Person person = createPerson("Mr ${it}", it * 5, 5)
            personList.add(person)
        }
        return personList
    }

}
