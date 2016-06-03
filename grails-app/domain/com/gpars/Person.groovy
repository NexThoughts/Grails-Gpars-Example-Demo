package com.gpars

class Person {

    String name
    Long age

    static hasMany = [addresses: Address]

    static constraints = {

    }
}
