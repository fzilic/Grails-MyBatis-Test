package mybatis.locking.configured

class ConfiguredBean {
  def static idProperty = 'identity'
  def static versionProperty = 'ver'
  def static versionQuery = 'queryForVersion'

  Long identity
  Long ver
  String textValue
}
