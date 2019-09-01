package com.example.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.InfoCmd
import com.github.dockerjava.api.model.Info
import com.typesafe.scalalogging.LazyLogging

object DockerClientInfo extends LazyLogging {

  def info()(implicit dockerClient: DockerClient): Info = {
    val infoCmd: InfoCmd = dockerClient.infoCmd()
    val info: Info = infoCmd.exec()

    import com.fasterxml.jackson.databind.ObjectMapper
    val mapper = new ObjectMapper
    val jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(info)
    logger.info(s"Info => \n$jsonString")
    info
  }

}
