package com.dcornu.helmstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.dcornu.helmstream.api.HelmStreamService
import com.dcornu.helm.api.HelmService

import scala.concurrent.Future

/**
  * Implementation of the HelmStreamService.
  */
class HelmStreamServiceImpl(helmService: HelmService) extends HelmStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(helmService.hello(_).invoke()))
  }
}
