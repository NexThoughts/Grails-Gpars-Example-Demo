package com.gpars

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
        List<String> animals = ['dog', 'ant', 'cat', 'whale']
        ParallelEnhancer.enhanceInstance(animals)
        render(animals.everyParallel { it.contains("a") } ? 'All animals contain a' : 'Some animals can live without a')
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
