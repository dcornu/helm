package com.dcornu.ext.issues.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait ExtIssuesService extends Service {
  def getIssue(key: String): ServiceCall[NotUsed, ExtIssue]

  // def getIssues: ServiceCall[NotUsed, Seq[ExtIssue]]

  def addIssue: ServiceCall[ExtIssue, ExtIssue]

  override final def descriptor = {
    import Service._
    import com.lightbend.lagom.scaladsl.api.transport.Method

    named("ext-issues").withCalls(
      // restCall(Method.GET, "/api/ext/issues", getIssues),
      restCall(Method.GET, "/api/ext/issues/:key", getIssue _),
      pathCall("/api/ext/issues", addIssue)
    )
      .withAutoAcl(true)
  }
}

case class ExtIssue (
  id: String,
  description: Option[String],
  key: String,
  summary: String
)

case object ExtIssue {
  implicit val format: Format[ExtIssue] = Json.format[ExtIssue]
}
