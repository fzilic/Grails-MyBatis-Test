package org.grails.mybatis.example

class Person {
  def static useOptimisticLocking = true

  Integer id
  Integer version
  String firstName
  String lastName
  Integer age
}
