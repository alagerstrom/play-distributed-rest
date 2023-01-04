package handler

import controllers.Message

import javax.inject.{Inject, Singleton}

@Singleton
class MessageHandler @Inject()(sender: MessageSender, pingService: TickService) extends Listener {
  val NO_URL = "no url yet"
  val NO_NAME = "no name yet"
  private var url: String = NO_URL
  private var count = 0
  private var workers: List[String] = List()
  private var name = NO_NAME
  pingService.start(this)


  def connectToMaster(masterUrl: String): Unit = {
    if (url == NO_URL) {
      printWithName("I have no url")
      return
    }
    printWithName("Connecting to master...")
    sender.send(masterUrl, Message("connection", url, url))
  }

  def handleConnection(workerUrl: String): Unit = {
    printWithName(s"Adding worker $workerUrl")
    workers = workers :+ workerUrl
    printWithName(workers.toString())
  }

  def setUrl(url: String): Unit = {
    printWithName(s"Setting my url to $url")
    this.url = url
  }

  def startJob(content: String): Unit = {
    // count number of letters in content
  }

  def handle(message: Message): Unit = {
    printWithName(s"Handling message: $message")
    count += 1
    printWithName(s"Handled ${count} messages so far")
    message.messageType match {
      case "connect_to_master" => connectToMaster(message.content)
      case "connection" => handleConnection(message.content)
      case "set_url" => setUrl(message.content)
      case "tick" => printWithName(s"Received tick from ${message.content}")
      case "set_name" => this.name = message.content
      case "start_job" => startJob(message.content)
      case _ => printWithName(s"Received unknown message: $message")
    }
  }

  private def printWithName(s: String): Unit = {
    println(s"($name): $s")
  }

  override def tick(): Unit = {
    sendToAllWorkers(Message("tick", url, url))
  }

  def sendToAllWorkers(message: Message): Unit = {
    for (worker <- workers) {
      sender.send(worker, message)
    }
  }
}
