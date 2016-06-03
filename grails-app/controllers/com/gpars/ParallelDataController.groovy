package com.gpars

import com.gpars.Person
import groovyx.gpars.ParallelEnhancer
import groovyx.gpars.agent.Agent

import static groovyx.gpars.GParsPool.withPool

class ParallelDataController {
    def parallelDataService

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
        List<Person> personList = Person.list()
        println "----personList------" + personList.size()
        Long time1 = System.currentTimeMillis()

        println " ********* ${personList.collect { it.name.contains('1') }.size()}"
        Long time2 = System.currentTimeMillis()

        println "*********** TOTAL TIME Taken without Parallelism :- ${(time2 - time1)} MilliSeconds "

//       ================= Using GPars ======================================
        ParallelEnhancer.enhanceInstance(personList)

        Long time3 = System.currentTimeMillis()
        println " ********* ${personList.collectParallel { it.name.contains('1') }.size()}"

        println "*********** TOTAL TIME Taken With everyParallel :- ${(time3 - time2)} MilliSeconds "
        render("Success")
    }


    def mapReduce() {

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
}
