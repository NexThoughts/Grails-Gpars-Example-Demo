package com.gpars

class PersonController {

    def personService

    def index() {


    }


    def create() {
        (1..100).each {
            Person person = personService.createPerson("Mr Person ${it}", it * 5, 5)
        }
        render "Success"
    }
}
