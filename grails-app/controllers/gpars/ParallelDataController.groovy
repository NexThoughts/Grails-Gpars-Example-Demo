package gpars

import static groovyx.gpars.GParsPool.withPool

class ParallelDataController {
def parallelDataService
    def index() {
        List<Integer> list=parallelDataService.populateList(100000)
        List<Integer> list2=[]
        def startTime=System.nanoTime()
        withPool(4){
            list2=list.collectParallel{it*2}
        }
        def endTime=System.nanoTime()
        def millis=10**(-6)
        render "Total Time taken : ${(endTime - startTime)*millis}"
    }
}
