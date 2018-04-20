package com.dcornu.helmstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.dcornu.helmstream.api.HelmStreamService
import com.dcornu.helm.api.HelmService
import com.softwaremill.macwire._

class HelmStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new HelmStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new HelmStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[HelmStreamService])
}

abstract class HelmStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[HelmStreamService](wire[HelmStreamServiceImpl])

  // Bind the HelmService client
  lazy val helmService = serviceClient.implement[HelmService]
}
