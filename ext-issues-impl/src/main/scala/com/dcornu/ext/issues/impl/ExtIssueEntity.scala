package com.dcornu.ext.issues.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq


class ExtIssueEntity extends PersistentEntity {

  override type Command = ExtIssueCommand
  override type Event = ExtIssueEvent
  override type State = ExtIssueState

  override def initialState: ExtIssueState = ExtIssueState.empty

  override def behavior: Behavior = {
    case state if state.isEmpty  => initial
    case state if !state.isEmpty  => issueAdded
  }

  private val initial: Actions = {
    Actions()
      // Command handlers are invoked for incoming messages (commands).
      // A command handler must "return" the events to be persisted (if any).
      .onCommand[AddIssue, ExtIssue] {
      case (AddIssue(content), ctx, state) =>
        if (content.key == null || content.key.equals("")) {
          ctx.invalidCommand("Key must be defined")
          ctx.done
        } else {
          ctx.thenPersist(IssueAdded(content)) { _ =>
            // After persist is done additional side effects can be performed
            ctx.reply(content)
          }
        }
    }
      // Event handlers are used both when persisting new events and when replaying
      // events.
      .onEvent {
      case (IssueAdded(content), state) =>
        ExtIssueState(Some(content), published = false)
    }
  }

  private val issueAdded: Actions = {
    Actions()
      .onCommand[AddIssue, ExtIssue] {
      case (AddIssue(body), ctx, state) =>
        ctx.thenPersist(IssueAdded(body))(_ => ctx.reply(body))
    }
      .onEvent {
        case (IssueAdded(body), state) =>
          state.withBody(body)
      }
      .onReadOnlyCommand[GetExtIssue.type, ExtIssue] {
      case (GetExtIssue, ctx, state) =>
        println(state)
        ctx.reply(state.content.get)
    }
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

