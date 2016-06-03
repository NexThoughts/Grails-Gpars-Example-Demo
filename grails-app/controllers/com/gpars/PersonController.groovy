package com.gpars

import groovyx.gpars.GParsPool
import groovyx.gpars.MessagingRunnable

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

    def actorExample = {
        final MyStatelessActor actor = new MyStatelessActor();
        actor.start();
        actor.send("Hello Vishal");
        actor.sendAndWait(10);

        actor.sendAndContinue(10.0, new MessagingRunnable<String>() {
            @Override
            protected void doRun(final String s) {
                println("******* Received a reply " + s);
            }
        });

        render "Success!!"
    }
}
