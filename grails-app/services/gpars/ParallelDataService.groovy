package gpars

import grails.transaction.Transactional

@Transactional
class ParallelDataService {

    List<Integer> populateList(Integer size) {
        List<Integer> populatedList = []
        if (size > 0) {
            size.times {
                populatedList << it
            }
        }
        return populatedList
    }
}
