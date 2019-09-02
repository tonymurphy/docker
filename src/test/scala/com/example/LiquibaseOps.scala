package com.example

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

trait LiquibaseOps {

  val CHANGELOG_FILE = "com/example/advert/importer/liquibase/changelog-master.xml"

  case class Credentials(dbUrl: String, username: String, password: String)

  def resetDBToSchema(implicit credentials: Credentials): HikariDataSource = {
    val datasource = h2DataSource(credentials)
    LiquibaseChangeLogLoader.loadWithDropAllFirst(CHANGELOG_FILE, datasource)
    datasource
  }

  def truncateDB(implicit credentials: Credentials): HikariDataSource = {
    val datasource = h2DataSource(credentials)
    LiquibaseChangeLogLoader.loadWithTruncateOnly(datasource)
    datasource
  }

  private def h2DataSource(credentials: Credentials): HikariDataSource = {
    val config = new HikariConfig()
    config.setJdbcUrl(credentials.dbUrl)
    config.setUsername(credentials.username)
    config.setPassword(credentials.password)
    config.setConnectionTimeout(2000)
    config.setIdleTimeout(10000)
    config.setMaximumPoolSize(1)
    new HikariDataSource(config)
  }
}