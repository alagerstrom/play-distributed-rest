package handler

import controllers.Message
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class MessageSender @Inject() (ws: WSClient, ec: ExecutionContext){
  def send(toUrl: String, message: Message): Unit = {
    ws.url(toUrl + "/message")
      .withHttpHeaders(("Content-Type", "application/json"))
      .post(Json.fo(
        "messageType" -> message.messageType,
        "content" -> message.content
      ))
      .map(response => {
//        println("Received response")
//        println("status: " + response.status)
//        println(response.body)
      })(executor = ec)
  }
}
