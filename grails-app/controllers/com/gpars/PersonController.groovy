package com.gpars

import groovy.time.TimeCategory
import groovyx.gpars.GParsPool
import groovyx.gpars.MessagingRunnable
import groovyx.gpars.ParallelEnhancer
import groovyx.gpars.ReactorMessagingRunnable
import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.Actors

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


    def minParallelAction = {
        Long startTime = System.currentTimeMillis()
        List<Person> personList = Person.list()

        Long time1 = System.currentTimeMillis()

        println " ********* ${personList*.age.min()}"
        Long time2 = System.currentTimeMillis()

        println "*********** TOTAL TIME Taken without Parallelism :- ${(time2 - time1)} MilliSeconds "

//       ================= Using GPars ======================================
        ParallelEnhancer.enhanceInstance(personList)

        Long time3 = System.currentTimeMillis()
        println " ********* ${personList.minParallel { it.age }}"

        println "*********** TOTAL TIME Taken With minParallel :- ${(time3 - time2)} MilliSeconds "
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


    def anotherActor = {
        final Closure handler = new ReactorMessagingRunnable<Integer, Integer>() {
            @Override
            protected Integer doRun(final Integer integer) {
                Thread.sleep(4000)
                return integer * 2;
            }
        };
        final Actor actor = Actors.reactor(handler);
        use(TimeCategory) {
            println("Result: " + actor.sendAndWait(1, 5.seconds));
            println("Result: " + actor.sendAndWait(2, 2.seconds));
            println("Result: " + actor.sendAndWait(3, 4.seconds));

        }
        render "Success!!"
    }


    def anotherActor1 = {

        def passiveActor = Actors.actor {
            react { Person person ->
                Address.withNewSession {
                    println "***** Yay!  Received Person: $person.name";
                    Address address = new Address()
                    address.locality = "${person.name} Locality"
                    address.postalCode = 201301
                    address.person = person
                    person.addToAddresses(address)
                    address.save(flush: true)
                    println "******########## Address for Person : ${person.id} **********"
                }
            }
        }

        def activeActor = Actors.actor {
            react { name ->
                println "Received Name for Person: $name";

                Person.withNewSession {
                    Person person = new Person()
                    person.name = name
                    person.age = 1
                    person.save(flush: true)

                    passiveActor.send(person)

                }
            }
        }

        activeActor.send 'Vishal 1'
        render "Success!!"
    }
}
