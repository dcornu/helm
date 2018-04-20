package com.dcornu.ext.issues.impl

import akka.NotUsed
import com.dcornu.ext.issues.api
import com.dcornu.ext.issues.api.ExtIssuesService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

class ExtIssuesServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends ExtIssuesService {

  persistentEntityRegistry.register(new ExtIssueEntity)

  override def addIssue = ServiceCall { request =>
    val ref = persistentEntityRegistry.refFor[ExtIssueEntity](request.id)
    val serviceObject = ExtIssue(request.id, request.description, request.key, request.summary)
    ref.ask(AddIssue(serviceObject)).map { _ => convertIssue(serviceObject) }
  }

  override def getIssue(id: String): ServiceCall[NotUsed, api.ExtIssue] = { request =>
    val ref = persistentEntityRegistry.refFor[ExtIssueEntity](id)
    ref.ask(GetExtIssue).map { retour => convertIssue(retour) }

  }

  private def convertIssue(item: ExtIssue): api.ExtIssue = {
    api.ExtIssue(item.id, item.description, item.key, item.summary)
  }

}
