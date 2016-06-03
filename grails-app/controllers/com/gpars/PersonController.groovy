package com.gpars

class PersonController {

    def personService

    def index() {


    }


    def create() {
        Long startTime = System.currentTimeMillis()
        (1..500).each {
            Person person = personService.createPerson("Mr ${it}", it * 5, 5)
        }
        Long endTime = System.currentTimeMillis()
        Double totalTimeTaken = (endTime - startTime) / 1000
        render "Success : Time taken ${totalTimeTaken} Seconds"
    }
}
