package com.example.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.SearchItem
import com.typesafe.scalalogging.StrictLogging

import scala.collection.JavaConversions


class DockerImageLookup(dockerClient: DockerClient) extends StrictLogging {


  def search(imageName: String) = {


    val searchItems: List[SearchItem] = JavaConversions.asScalaBuffer(dockerClient.searchImagesCmd(imageName).exec()).toList

    searchItems.foreach(item => logger.info(s"Image $item"))
  }


}

object DockerImageLookup {

  def apply(dockerClient: DockerClient) = new DockerImageLookup(dockerClient)

}