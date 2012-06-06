dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}

dataSource_alternate {
  pooled = true
  driverClassName = "org.h2.Driver"
  username = "sa"
  password = ""
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}

// ==================================
// myBatis plugin configuration
// ==================================
// configure datasource for mybatis plugin
mybatis.dataSourceNames = [
  'dataSource', 'dataSource_alternate'
]
mybatis.multivendor.enabled = true
mybatis.multivendor.mapping = [
    'Microsoft SQL Server': 'mssql',
    'H2': 'h2'
  ]

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:devDb;MVCC=TRUE"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE"
        }
//        dataSource_alternate {
//          dbCreate = "update"
//          driverClassName = "net.sourceforge.jtds.jdbc.Driver"
//          url = "jdbc:jtds:sqlserver://192.168.236.130:1433;databaseName=TEST_DB"
//          password = "password"
//      }
        dataSource_alternate {
          dbCreate = "update"
          url = "jdbc:h2:mem:alternateDb;MVCC=TRUE"
      }

    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
