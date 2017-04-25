package com.chuidiang.examples

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.transform.ToString

import java.sql.ResultSet
import java.sql.ResultSetMetaData

/**
 * Created by JAVIER on 25/03/2017.
 */
class DataBase {
    static void main(String [] args) {
        def url = 'jdbc:hsqldb:mem:yourDB'
        def user = 'sa'
        def password = ''
        def driver = 'org.hsqldb.jdbcDriver'
        def sql = Sql.newInstance(url, user, password, driver)

        sql.execute('''CREATE TABLE Author (
                id          INTEGER GENERATED BY DEFAULT AS IDENTITY,
                firstname   VARCHAR(64),
                lastname    VARCHAR(64)
        );''')

        def persons = [new Person(name:'Pedro',surname:'Gomez'),
                       new Person(name:'Juan',surname:'Lopez'),
                       new Person(name:'Antonio',surname:'Garcia')
        ]

        persons.each { person ->

            def ids  = sql.executeInsert("""
                insert into Author
                values (null,?,?)
                """,
                [person.name, person.surname])

            ids.each {println it}

            ids  = sql.executeInsert("""
                insert into Author
                values (null, $person.name, $person.surname)
                """)

            ids.each {println it}

            def personMap = [name:person.name, surname:person.surname]

            ids  = sql.executeInsert("""
                insert into Author
                values (null, :name, :surname)
                """, personMap)

            ids.each {println it}

            ids  = sql.executeInsert("""
                insert into Author
                values (null, :name, :surname)
                """, person)

            ids.each {println it}

        }

        sql.eachRow("select * from Author") { row ->
            println "${row.id} ${row.firstname} ${row.lastname}"
        }

        println '--------------'

        // Pagination
        sql.eachRow("select * from Author", 2, 4) { row ->
            println "${row.id} ${row.firstname} ${row.lastname}"
        }

        def metaClosure = { ResultSetMetaData meta ->
            (1..meta.columnCount).each { index ->
                print meta.getColumnLabel(index).padRight(20)
            }
            println()
        }

        sql.eachRow("select * from Author", metaClosure) { row ->
            println "${row.id.toString().padRight(20)}${row.firstname.padRight(20)}${row.lastname.padRight(20)}"
        }

        GroovyRowResult row = sql.firstRow("select * from Author")
        println new PersonFromBd(row)
        println "$row.ID $row.FIRSTNAME $row.LASTNAME"

        sql.close()
    }
}

@ToString
class Person {
    String name
    String surname
}

@ToString
class PersonFromBd {
    Integer ID
    String FIRSTNAME
    String LASTNAME
}
