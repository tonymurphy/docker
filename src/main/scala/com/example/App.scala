package com.example

import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.api.model.{Bind, PortBinding}
import com.github.dockerjava.core.{DefaultDockerClientConfig, DockerClientBuilder}
import com.example.docker.{ContainerService, DockerClientInfo, DockerImageLookup}
import com.typesafe.scalalogging.StrictLogging

/**
 * @author ${user.name}
 */
object App extends StrictLogging {
  
  def main(args : Array[String]) {

    val dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
//      .withDockerHost("unix:///var/run/docker.sock")
//      .withDockerHost("unix:///var/run/docker.sock")
      .build()

    implicit val dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build()

//    DockerClientInfo.info()

//    val dockerImageLookup = DockerImageLookup(dockerClient)
//
//    dockerImageLookup.search("postgres")

    ContainerService.createPostgresContainer()
  }

}
