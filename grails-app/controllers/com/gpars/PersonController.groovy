package com.gpars

import groovyx.gpars.GParsPool

class PersonController {

    def personService

    def index() {


    }


    def create() {
        Long startTime = System.currentTimeMillis()
        (1..300).each {
            Person person = personService.createPerson("Mr ${it}", it * 5, 5)
        }
        Long endTime = System.currentTimeMillis()
        Double totalTimeTaken = (endTime - startTime) / 1000
        render "Success : Time taken ${totalTimeTaken} Seconds"
    }


    def createWithPool() {
        Long startTime = System.currentTimeMillis()
        GParsPool.withPool(15) {
            (1..300).eachParallel {
                Person person = personService.createPerson("Mr ${it}", it * 5, 5)
            }
        }
        Long endTime = System.currentTimeMillis()
        Double totalTimeTaken = (endTime - startTime) / 1000
        render "Success with GPars: Time taken ${totalTimeTaken} Seconds"
    }
}
