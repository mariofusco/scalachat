package scalachat.client

import actors.Actor
import actors.remote.RemoteActor
import actors.remote.RemoteActor._
import actors.remote.Node

import scalachat.common._
import scalachat.common.ChatUtil._

/**
 * @author Mario Fusco
 */
class ChatClient(username: String) extends Actor {

  def lifecycle() {
    start
    chat
  }

  def act() {
    trapExit = true
    RemoteActor.classLoader = getClass().getClassLoader()
    alive(getFreePort)
    val server = select(Node(chatServiceHost, chatServicePort), 'Server)
    link(server)

    server ! LoginEvent(username)

    loop {
      receive {
        case MessageEvent(username, message) => println(username + ": " + message)
        case "bye" => { server ! LogoutEvent(username); exit }
        case message: String => server ! MessageEvent(username, message)
      }
    }
  }

  def chat(): Unit = {
    var finished = false
    while (!finished) {
      var message = Console.readLine
      finished = message.trim == "bye"
      this ! message
    }
  }
}

object ChatClient extends Application {
  print("Username: ")
  val username = Console.readLine
  new ChatClient(username).lifecycle
}