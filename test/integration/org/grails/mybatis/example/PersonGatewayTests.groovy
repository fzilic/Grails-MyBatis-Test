package org.grails.mybatis.example

import groovy.sql.Sql

class PersonGatewayTests extends GroovyTestCase {

  /**
   * Dependency injection is used to test Gateway artefacts
   */
  def dataSource
  def personGateway

  protected void setUp() throws Exception {
    def sql = new Sql(dataSource)
    sql.execute("""CREATE TABLE PERSON (
    ID INTEGER NOT NULL,
    VERSION INTEGER NOT NULL,
    FIRST_NAME VARCHAR(30),
    LAST_NAME VARCHAR(30),
    AGE INTEGER,
    PRIMARY KEY (ID)
    )""")
  }

  protected void tearDown() throws Exception {
    def sql = new Sql(dataSource)
    sql.execute("""DROP TABLE IF EXISTS PERSON""")
  }

  void testDependencies() {
    assert dataSource
    assert personGateway
  }

  void testPersonInsert() {
    Person data = new Person(firstName: 'John', lastName: 'Doe', age: 50)

    personGateway.insertPerson(data);

    def databaseData = new Sql(dataSource).firstRow("SELECT * FROM PERSON")

    assert databaseData["ID"] == 1
    assert databaseData["VERSION"] == 1
    assert databaseData["FIRST_NAME"] == 'John'
    assert databaseData["LAST_NAME"] == 'Doe'
    assert databaseData["AGE"] == 50

    assert data.id == 1
    assert data.version == 1
  }

  void testLoadPersonById() {
    Person data = new Person(firstName: 'John', lastName: 'Doe', age: 50)

    personGateway.insertPerson(data);

    Person fromDb = personGateway.loadPersonById(data.id)

    assert fromDb
    assert fromDb.id == data.id
    assert fromDb.version == data.version
    assert fromDb.firstName == data.firstName
  }

  void testPersonUpdate() {
    Person data = new Person(firstName: 'John', lastName: 'Doe', age: 50)

    personGateway.insertPerson(data);

    assert data.version == 1
    assert data.age == 50

    Person fromDb = personGateway.loadPersonById(data.id)

    assert fromDb.id == 1
    assert fromDb.version == 1
    assert fromDb.age == 50

    data.age = 33

    personGateway.updatePerson(data)

    assert data.version == 2
    assert data.age == 33

    fromDb = personGateway.loadPersonById(data.id)

    assert fromDb.version == 2
    assert fromDb.age == 33

    new Sql(dataSource).execute("UPDATE PERSON SET VERSION = 7, AGE = 29 WHERE ID = 1")

    shouldFail {
      data.firstName = 'Mark'
      personGateway.updatePerson(data)
    }

  }

  void testLoadAllPersonList() {
    personGateway.insertPerson(new Person(firstName: 'John', lastName: 'Doe', age: 20))
    personGateway.insertPerson(new Person(firstName: 'Mark', lastName: 'Miles', age: 23))

    def results = personGateway.loadAllPersonList()

    assert results
    assert results.size() == 2
    assert results[0] instanceof Person
    assert results[0].firstName == 'John'
    assert results[1].firstName == 'Mark'
  }

}
