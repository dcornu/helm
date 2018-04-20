package com.dcornu.ext.issues.impl

import play.api.libs.json.{Format, Json}


object ExtIssueState {
  val empty = ExtIssueState(None, published = false)

  implicit val postContentFormat = Json.format[ExtIssue]

  implicit val format: Format[ExtIssueState] = Json.format[ExtIssueState]
}

final case class ExtIssueState(content: Option[ExtIssue], published: Boolean) {
  def withBody(extIssue: ExtIssue): ExtIssueState = {
    content match {
      case Some(c) =>
        copy(content = Some(c.copy(key = extIssue.key)))
      case None =>
        throw new IllegalStateException("Can't set body without content")
    }
  }

  def isEmpty: Boolean = content.isEmpty
}

