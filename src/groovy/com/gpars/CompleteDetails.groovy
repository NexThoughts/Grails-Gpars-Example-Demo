package com.gpars


class CompleteDetails {
    String name
    Long age
    String locality
    String postalCode


    CompleteDetails(){

    }
    CompleteDetails(Person person,Address address){
        this.name=person.name
        this.age=person.age
        this.locality=address.locality
        this.postalCode=address.postalCode
    }

}
