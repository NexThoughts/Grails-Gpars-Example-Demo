package gpars

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
}
