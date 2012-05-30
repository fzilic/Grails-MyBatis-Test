package mybatis.locking.configured

import grails.test.GrailsUnitTestCase
import groovy.sql.Sql

class ConfiguredBeanGatewayTests extends GrailsUnitTestCase {
  def dataSource
  def configuredBeanGateway

  @Override
  protected void setUp() {
    def sql = new Sql(dataSource)

    sql.execute("""DROP TABLE IF EXISTS OPTIMISTIC_TABLE""")
    sql.execute("""CREATE TABLE OPTIMISTIC_TABLE (
      OPT_ID BIGINT NOT NULL,
      OPT_VERSION BIGINT NOT NULL,
      OPT_VALUE VARCHAR(255) NULL,
      PRIMARY KEY (OPT_ID))""")
  }

  void testInsert() {
    def bean = new ConfiguredBean(textValue: 'Some value')

    configuredBeanGateway.insertBean(bean)

    assert configuredBeanGateway.countBeans() == 1
  }

  void testUpdate() {
    def bean = new ConfiguredBean(textValue: 'Some value')

    configuredBeanGateway.insertBean(bean)

    bean.textValue = 'Another value'

    configuredBeanGateway.updateBean(bean)

    assert bean.ver == 2

  }

  void testDelete() {
    def bean = new ConfiguredBean(textValue: 'Some value')

    configuredBeanGateway.insertBean(bean)

    configuredBeanGateway.deleteBean(bean)

    assert configuredBeanGateway.countBeans() == 0
  }

}
