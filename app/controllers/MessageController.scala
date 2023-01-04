package controllers

import handler.MessageHandler
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import play.api.libs.json._

case class Message(messageType: String, content: String, replyTo: String)

@Singleton
class MessageController @Inject()(val controllerComponents: ControllerComponents, val messageHandler: MessageHandler) extends BaseController {
  implicit val messageJson = Json.format[Message]

  def handleMessage(message: Message): Unit = {
    messageHandler.handle(message)
  }

  def addMessage(): Action[AnyContent] = Action {
    implicit request =>
      val content = request.body
      val jsonObject = content.asJson
      val messageOpt: Option[Message] =
        jsonObject.flatMap(
          Json.fromJson[Message](_).asOpt
        )

      messageOpt match {
        case Some(newItem) =>
          handleMessage(newItem)
          Created(Json.toJson(newItem))
        case None =>
          BadRequest
      }
  }
}


