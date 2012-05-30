package mybatis.locking.configured

class ConfiguredBeanGateway {

  def static optimisticLocking = {
    useOptimisticLocking: true
    classes: []
  }

}
