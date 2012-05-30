package mybatis.locking

class OptimisticLockingGateway {

  def static optimisticLocking = {
    useOptimisticLocking: true
    classes: []
  }

}
