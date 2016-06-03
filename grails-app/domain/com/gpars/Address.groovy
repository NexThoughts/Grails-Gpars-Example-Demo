package com.gpars

class Address {

    String locality
    String postalCode

    static belongsTo = [person: Person]

    static constraints = {
    }
}
