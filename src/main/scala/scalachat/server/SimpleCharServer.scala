package scalachat.server

import actors.Actor._
import actors.OutputChannel
import actors.remote.RemoteActor
import actors.remote.RemoteActor._

import collection.mutable.HashMap

import scalachat.common._
import scalachat.common.ChatUtil._

/**
 * @author Mario Fusco
 */
object SimpleCharServer extends Application {
  val sessions = new HashMap[String, OutputChannel[ChatEvent]]

  actor {
    RemoteActor.classLoader = getClass().getClassLoader()
    alive(chatServicePort)
    register('Server, self)

    loop {
      react {
        case LoginEvent(username) => {
          sessions.put(username, sender)
          sendMessageFrom(username, "I just logged in")
          println(username + " just logged in")
        }
        case LogoutEvent(username) => {
          sessions.remove(username).get ! LogoutEvent(username)
          sendMessageFrom(username, "I just logged out")
          println(username + " just logged out")
        }
        case MessageEvent(username, message) => {
          sendMessageFrom(username, message)
          println(username + " sent " + message);
        }
      }
    }
  }

  def sendMessageFrom(username: String, message: String) =
    sessions.filterKeys(_ != username).values.foreach(_ ! MessageEvent(username, message))
}