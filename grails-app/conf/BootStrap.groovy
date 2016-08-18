import com.gpars.Person
import groovyx.gpars.GParsPool

class BootStrap {

    def personService


    def init = { servletContext ->
        GParsPool.withPool(15) {
            (1..5000).eachParallel {
                Person person = personService.createPerson("Mr Person ${it}", it * 5, 5)
            }
        }
    }
    def destroy = {
    }
}
