package com.example

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.time.{Second, Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

class PostgresServiceSpec extends FlatSpec with Matchers with DockerPostgresService with LazyLogging {

  override val dockerName = Some(this.getClass.getSimpleName)
  override val dbName = "abc"
  implicit val credentials = getCredentials()


  implicit val pc = PatienceConfig(Span(20, Seconds), Span(1, Second))

  "postgres node" should "be ready with log line checker" in {
    isContainerReady(postgresContainer).futureValue shouldBe true
  }
}