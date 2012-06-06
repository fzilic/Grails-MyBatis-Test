package mybatis.locking

class OptimisticBean {
  def static useOptimisticLocking = true
  Long id
  Long version
  String textValue

}
