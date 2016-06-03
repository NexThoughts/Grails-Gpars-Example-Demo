package gpars

import static groovyx.gpars.GParsPool.withPool

class ParallelDataController {
def parallelDataService
    def index() {
        List<Integer> list=parallelDataService.populateList(100000)
        List<Integer> list2=[]
        Long startTime=System.nanoTime()
        withPool(4){
            list2=list.collectParallel{it*2}
        }
        Long endTime=System.nanoTime()
        Double millis=10**(-6)
        Double totalTimeTaken=(endTime - startTime)*millis
        println "TOTAL TIME :- ${totalTimeTaken} ms "
        render "Total Time taken : ${totalTimeTaken} ms"
    }
}
