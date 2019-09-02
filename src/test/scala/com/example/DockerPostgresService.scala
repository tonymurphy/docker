package com.example

import java.io.{PrintWriter, StringWriter}
import java.sql.DriverManager

import com.typesafe.scalalogging.LazyLogging
import com.whisk.docker.impl.dockerjava.DockerKitDockerJava
import com.whisk.docker.scalatest.DockerTestKit
import com.whisk.docker.{DockerCommandExecutor, DockerContainer, DockerContainerState, DockerReadyChecker}
import org.scalatest.Suite

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

trait DockerPostgresService extends DockerKitDockerJava with DockerTestKit with LiquibaseOps {
  this: Suite =>

  def dockerName: Option[String] = None

  def dbName: String = "mydb"

  val internalPort = 44445
  val externalPort = 5432
  val driver = "org.postgresql.Driver"
  val dockerImage = "dock.es.ecg.tools/hub.docker.com/postgres:9.6.9"

  val user = "user"
  val password = "safepassword"
  lazy val dbUrl = s"jdbc:postgresql://localhost:$internalPort/$dbName?autoReconnect=true&useSSL=false"


  lazy val postgresContainer = DockerContainer(dockerImage, dockerName)
    .withPorts((externalPort, Some(internalPort)))
    //    .withPortMapping()
    .withEnv(s"POSTGRES_USER=$user", s"POSTGRES_PASSWORD=$password", s"POSTGRES_DB=$dbName")
    .withReadyChecker(
      new PostgresReadyChecker(dbName, user, password, Some(internalPort)).looped(3, 5.second)
    )

  // adds our container to the DockerKit's list
  abstract override def dockerContainers: List[DockerContainer] =
    postgresContainer :: super.dockerContainers

  def getCredentials(): Credentials = {
    Credentials(dbUrl, user, password)
  }
}

class PostgresReadyChecker(dbName: String, user: String, password: String, port: Option[Int] = None)
  extends DockerReadyChecker with LazyLogging {

  override def apply(container: DockerContainerState)(implicit docker: DockerCommandExecutor,
                                                      ec: ExecutionContext) =
    container
      .getPorts()
      .map(ports =>
        Try {
          Class.forName("org.postgresql.Driver")

          val url = s"jdbc:postgresql://${docker.host}:${port.getOrElse(ports.values.head)}/$dbName?autoReconnect=true&useSSL=false"
          logger.debug(s"Attempt to connect to $url with user: $user, password: $password")
          Option(DriverManager.getConnection(url, user, password)).map(_.close).isDefined
        } match {
          case Success(result) => result
          case Failure(e) =>
            val sw = new StringWriter
            val lines = e.printStackTrace(new PrintWriter(sw)).toString.split(System.lineSeparator()).take(4).mkString(System.lineSeparator())

            logger.warn(s"Failed to get connection to postgres $lines")
            false
        })
}
