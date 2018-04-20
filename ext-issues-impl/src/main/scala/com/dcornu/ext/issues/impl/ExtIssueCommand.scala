package com.dcornu.ext.issues.impl

import com.dcornu.utils.JsonFormats._
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.{Format, Json}


/**
  * This interface defines all the commands that the ExtIssue entity supports.
  */
sealed trait ExtIssueCommand

case object ExtIssueCommand {
  import play.api.libs.json._
  import JsonSerializer.emptySingletonFormat

  implicit val format = Json.format[ExtIssue]

  val serializers = Vector(
    // JsonSerializer(emptySingletonFormat(GetExtIssue)),
    JsonSerializer(emptySingletonFormat(GetIssues))
  )
}

case class AddIssue(extIssue: ExtIssue) extends ExtIssueCommand with PersistentEntity.ReplyType[ExtIssue]

case object GetIssues extends ExtIssueCommand with ReplyType[Option[Seq[ExtIssue]]]

case object GetExtIssue extends ExtIssueCommand with ReplyType[ExtIssue] {
  implicit val format: Format[GetExtIssue.type] = singletonFormat(GetExtIssue)
}

case object AddIssue {
  implicit val format: Format[AddIssue] = Json.format
}

