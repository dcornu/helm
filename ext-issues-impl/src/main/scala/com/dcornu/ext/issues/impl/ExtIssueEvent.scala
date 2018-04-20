package com.dcornu.ext.issues.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventShards, AggregateEventTag}
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import play.api.libs.json.{Format, Json}


case class IssueAdded(extIssue: ExtIssue) extends ExtIssueEvent

object ExtIssueEvent {
  val NumShards = 1
  // second param is optional, defaults to the class name
  val Tag = AggregateEventTag.sharded[ExtIssueEvent](NumShards)


}

sealed trait ExtIssueEvent extends AggregateEvent[ExtIssueEvent] {
  override def aggregateTag: AggregateEventShards[ExtIssueEvent] = ExtIssueEvent.Tag
}



case object IssueAdded {
  implicit val format: Format[IssueAdded] = Json.format
}
