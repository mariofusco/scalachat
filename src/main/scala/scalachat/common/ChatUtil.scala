package scalachat.common

import util.Random

/**
 * @author Mario Fusco
 */
object ChatUtil {

  val chatServiceHost = "127.0.0.1"
  val chatServicePort = 9999

  def getFreePort = Random.nextInt(10000) + 10000
}