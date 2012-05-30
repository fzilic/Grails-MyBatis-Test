package mybatis.locking


import mybatis.locking.OptimisticBean


import grails.test.GrailsUnitTestCase
import groovy.sql.Sql

class OptimisticLockingTests extends GrailsUnitTestCase {

  def dataSource
  def optimisticLockingGateway

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

  void testDependencies() {
    assert dataSource
    assert optimisticLockingGateway
  }


  void testInsert() {
    def beans = [
      new OptimisticBean(textValue: 'Some value'),
      new OptimisticBean(textValue: 'Other value')
    ]

    beans.each { optimisticLockingGateway.insertOptimisticBean(it) }

    assert optimisticLockingGateway.countOptimisticBeans() == 2
  }

  void testUpdate() {
    def bean = new OptimisticBean(textValue: 'Some value')

    optimisticLockingGateway.insertOptimisticBean(bean)

    assert bean.id == 1
    assert bean.version
    assert bean.version == 1

    bean.textValue = 'Other value'

    optimisticLockingGateway.updateOptimisticBean(bean)

    assert bean.version == 2
    def updatedBean = optimisticLockingGateway.loadOptimisticBeanById(1)
    assert updatedBean.version == 2
    assert updatedBean.textValue == 'Other value'
  }

  void testUpdateWithMissingRecord() {
    def bean = new OptimisticBean(textValue: 'Some value')

    optimisticLockingGateway.insertOptimisticBean(bean)

    def sql = new Sql(dataSource)

    sql.execute("""DELETE FROM OPTIMISTIC_TABLE""")

    shouldFail { optimisticLockingGateway.updateOptimisticBean(bean) }
  }

  void testUpdateWithWrongVersion() {
    def bean = new OptimisticBean(textValue: 'Some value')

    optimisticLockingGateway.insertOptimisticBean(bean)

    def sql = new Sql(dataSource)

    sql.execute("""UPDATE OPTIMISTIC_TABLE SET OPT_VERSION = 5""")

    shouldFail { optimisticLockingGateway.updateOptimisticBean(bean) }
  }

  void testDelete() {
    def bean = new OptimisticBean(textValue: 'Some value')

    optimisticLockingGateway.insertOptimisticBean(bean)

    optimisticLockingGateway.deleteOptimisticBean(bean)

    assert !optimisticLockingGateway.countOptimisticBeans()
  }

  void testDeleteWithMissingRecord() {
    def bean = new OptimisticBean(textValue: 'Some value')

    optimisticLockingGateway.insertOptimisticBean(bean)

    def sql = new Sql(dataSource)

    sql.execute("""DELETE FROM OPTIMISTIC_TABLE""")

    shouldFail { optimisticLockingGateway.deleteOptimisticBean(bean) }
  }

  void testDeleteWithWrongVersion() {
    def bean = new OptimisticBean(textValue: 'Some value')

    optimisticLockingGateway.insertOptimisticBean(bean)

    def sql = new Sql(dataSource)

    sql.execute("""UPDATE OPTIMISTIC_TABLE SET OPT_VERSION = 15""")

    shouldFail { optimisticLockingGateway.deleteOptimisticBean(bean) }
  }
}
