package mybatis.locking.configured

class ConfiguredBean {
  def static idProperty = 'identity'
  def static versionProperty = 'ver'
  def static versionQuery = 'queryForVersion'
  def static useOptimisticLocking = true

  Long identity
  Long ver
  String textValue
}
