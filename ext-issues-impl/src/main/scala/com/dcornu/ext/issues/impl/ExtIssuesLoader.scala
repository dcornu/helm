package com.dcornu.ext.issues.impl

import com.dcornu.ext.issues.api.ExtIssuesService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

class ExtIssuesLoader extends LagomApplicationLoader {

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ExtIssuesApplication(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext):LagomApplication =
    new ExtIssuesApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def describeService = Some(readDescriptor[ExtIssuesService])
}

abstract class ExtIssuesApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[ExtIssuesService](wire[ExtIssuesServiceImpl])

  // Bind the HelmService client
  // lazy val extIssuesService = serviceClient.implement[ExtIssuesService]

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = ExtIssuesSerializerRegistry

  // Register the Helm persistent entity
  persistentEntityRegistry.register(wire[ExtIssueEntity])
}
