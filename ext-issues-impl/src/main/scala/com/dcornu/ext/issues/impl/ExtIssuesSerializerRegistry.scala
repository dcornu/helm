package com.dcornu.ext.issues.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

object ExtIssuesSerializerRegistry extends JsonSerializerRegistry{
  override def serializers = List(
    JsonSerializer[ExtIssue],

    JsonSerializer[AddIssue],
    JsonSerializer[GetExtIssue.type],
    // JsonSerializer[GetIssues.type],

    JsonSerializer[IssueAdded],
  )
}
