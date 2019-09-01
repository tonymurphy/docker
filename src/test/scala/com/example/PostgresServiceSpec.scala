package com.example

import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.netty.NettyDockerCmdExecFactory
import com.typesafe.scalalogging.LazyLogging
import com.whisk.docker.DockerFactory
import com.whisk.docker.impl.dockerjava.{Docker, DockerJavaExecutorFactory}
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.time.{Second, Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

class PostgresServiceSpec
  extends FlatSpec
    with Matchers
    with DockerTestKit
    with DockerPostgresService
    with LazyLogging {

  logger.info("hello")

  override val StartContainersTimeout = 20.seconds

  override implicit val dockerFactory: DockerFactory = new DockerJavaExecutorFactory(
    new Docker(DefaultDockerClientConfig.createDefaultConfigBuilder().build(),
      factory = new NettyDockerCmdExecFactory()))

  implicit val pc = PatienceConfig(Span(20, Seconds), Span(1, Second))

  "postgres node" should "be ready with log line checker" in {
    isContainerReady(postgresContainer).futureValue shouldBe true
  }
}