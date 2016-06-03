package com.gpars

import groovy.time.TimeCategory
import groovyx.gpars.ParallelEnhancer
import groovyx.gpars.actor.Actors
import groovyx.gpars.agent.Agent
import groovyx.gpars.dataflow.DataflowVariable

import java.util.concurrent.TimeUnit

import static groovyx.gpars.GParsPool.withPool
import static groovyx.gpars.dataflow.Dataflow.task

class ParallelDataController {
    def parallelDataService
    def personService

    def index() {
        Long startTimeList = System.currentTimeMillis()

        Double integerRange = 1000000
        List<Integer> dataList = session["${integerRange}"] ?: parallelDataService.populateList(integerRange)
        session["${integerRange}"] = dataList

        List<Integer> resultList = []
        Long startTime = System.currentTimeMillis()
        Double totalTimeTakenForData = (startTime - startTimeList) / 1000
        println "*** Total Time taken for List Creation : ${totalTimeTakenForData} Seconds"

//        resultList = dataList.collect { it * 2 }

        withPool(2) {
            resultList = dataList.collectParallel { it * 2 }
        }
        Long endTime = System.currentTimeMillis()

        Double totalTimeTaken = (endTime - startTime) / 1000
        println "*********** TOTAL TIME :- ${totalTimeTaken} Seconds "
        render "*** Total Time taken : ${totalTimeTaken} Seconds"
    }

    def parallelEnhancer() {
        List<String> animals = ['dog', 'ant', 'cat', 'whale']
        ParallelEnhancer.enhanceInstance(animals)
        render(animals.everyParallel { it.contains("a") } ? 'All animals contain a' : 'Some animals can live without a')
    }


    def nakul() {
        println "DataFlow Example"
        final def personList1 = new DataflowVariable()
        final def personList2 = new DataflowVariable()
        final def combinedList = new DataflowVariable()

        task {
            Thread.sleep(500)
            combinedList << personList1.val + personList2.val
            println ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>The combined list contains ${combinedList.val.size()}"
        }

        task {
            List<Person> personList = []
            (1..15).each {
                Thread.sleep(500)
                Person person = personService.createPerson("Mr ${it}", it * 5, 5)
                personList.add(person)
            }
            println ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>PersonList 1 contains ${personList.size()} persons"
            personList1 << personList

        }

        task {
            List<Person> personList = []
            Thread.sleep(500)
            (1..5).each {

                Person person = personService.createPerson("Mr ${it}", it * 5, 5)
                personList.add(person)
            }
            println ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>PersonList 2 contains ${personList.size()} persons"
            personList2 << personList
        }

        render "Result: ${combinedList.val}"
    }

    def agents() {
        def final world = new Agent<World>(new World())
        final Thread zombies = Thread.start {
            while (!world.val.apocalypse()) {
                world << { it.eatBrains() }
                sleep 200
            }
        }
        final Thread survivors = Thread.start {
            while (!world.val.apocalypse()) {
                world << { it.shotgun() }
                sleep 200
            }
        }
        while (!world.instantVal.apocalypse()) {
            world.val.report()
            sleep 200
        }
        render "Check console for output !!"
    }


    def asyncProcessing() {
        Closure square = { it -> it * it }
        withPool() {
            def result = square.callAsync(1000)
            Thread.sleep(5000)
            println result.get()
        }
    }

    def sendAndWait() {
        def replyingActor = Actors.actor {
            loop {
                react { msg ->
                    println "Received: $msg";
                    reply "I've got $msg"
                }
            }
        }
        def reply1 = replyingActor.sendAndWait('Message 4')
        def reply2 = replyingActor.sendAndWait('Message 5', 10, TimeUnit.SECONDS)
        use(TimeCategory) {
            def reply3 = replyingActor.sendAndWait('Message 6', 10.seconds)
        }
        render "Success !! View on Terminal"
    }

    def sendAndPromise() {
        def replyingActor = Actors.actor {
            loop {
                react { msg ->
                    Address.withNewSession {
                        Address.list().every { it.postalCode == 201301 }
                    }
                    println "Received: $msg"
                    reply "I've got $msg"
                }
            }
        }
        100.times {
            Thread.sleep(10)
        }
        def promise = replyingActor.sendAndPromise("Hello to  the audience!!")
        render promise.get()
        println "Hi I won't wait for the Actor to Complete"

    }

}
