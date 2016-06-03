package gpars

import grails.transaction.Transactional

@Transactional
class ParallelDataService {

    List<Integer> populateList(Double size) {
        println "####### Since the key is new, so i am creating the Data ##########"
        List<Integer> populatedList = []
        if (size > 0) {
            size.times {
                populatedList << it
            }
        }
        return populatedList
    }
}
