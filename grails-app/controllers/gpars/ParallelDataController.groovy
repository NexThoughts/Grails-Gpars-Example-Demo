package gpars

import static groovyx.gpars.GParsPool.withPool

class ParallelDataController {
    def parallelDataService

    def index() {
        Long startTimeList = System.currentTimeMillis()

        List<Integer> list = parallelDataService.populateList(1000000)

        List<Integer> list2 = []
        Long startTime = System.currentTimeMillis()
        Double totalTimeTakenForData = (startTime - startTimeList) / 1000
        render "*** Total Time taken for List Creation : ${totalTimeTakenForData} Seconds"

//        list2 = list.collect { it * 2 }

        withPool(2) {
            list2 = list.collectParallel { it * 2 }
        }
        Long endTime = System.currentTimeMillis()

        Double totalTimeTaken = (endTime - startTime) / 1000
        println "*********** TOTAL TIME :- ${totalTimeTaken} Seconds "
        render "*** Total Time taken : ${totalTimeTaken} Seconds"
    }
}
