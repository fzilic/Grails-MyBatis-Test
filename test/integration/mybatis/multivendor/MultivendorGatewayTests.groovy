package mybatis.multivendor

import groovy.sql.Sql

/**
 * To test for multivendor support configure dataSource_alternate for alternate vendor.
 * Also add correct mapping for databaseIdProvider in mybatis plugin configuration (DataSources.groovy).
 *
 * Modify mapper file multivendor.xml to include query generation for that vendor
 * @author fzilic
 *
 */
class MultivendorGatewayTests extends GroovyTestCase {

  def dataSource_alternate
  def multivendorGateway

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    def sql = new Sql(dataSource_alternate)

    sql.execute("""CREATE TABLE TEST_TABLE (COL1 VARCHAR(50) NOT NULL, COL2 VARCHAR(50) NOT NULL)""")

    5.times {
      sql.execute("""INSERT INTO TEST_TABLE (COL1, COL2) VALUES (\'VALUE${it}\', \'VALUE${it+1}\')""".toString())
    }
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();

    def sql = new Sql(dataSource_alternate)

    sql.execute("""DROP TABLE TEST_TABLE""")
  }

  void testDependencies() {
    assert dataSource_alternate
    assert multivendorGateway
  }

  void testSelect() {
    def results = multivendorGateway.testSelectList()

    assert results
    assert results.size() == 5

    5.times {
      assert results[it] == "VALUE${it} VALUE${it+1}"
    }
  }
}
