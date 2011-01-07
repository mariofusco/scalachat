package scalachat.common

/**
 * @author Mario Fusco
 */
sealed trait ChatEvent
case class LoginEvent(username: String) extends ChatEvent
case class LogoutEvent(username: String) extends ChatEvent
case class MessageEvent(username: String, message: String) extends ChatEvent