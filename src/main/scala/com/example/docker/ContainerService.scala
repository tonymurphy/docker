package com.example.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.core.command.WaitContainerResultCallback

object ContainerService {


  def createPostgresContainer()(implicit dockerClient: DockerClient): WaitContainerResultCallback = {

    val container: CreateContainerResponse = dockerClient.createContainerCmd("dock.es.ecg.tools/hub.docker.com/postgres:9.6.9")
      .withCmd("touch", "/test")
      .exec()

    dockerClient.startContainerCmd(container.getId()).exec()
    dockerClient.stopContainerCmd(container.getId()).exec()
    dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback())
  }

}
