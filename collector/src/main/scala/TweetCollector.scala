import org.slf4j.LoggerFactory
import twitter4j._

/**
 * Author: chris
 * Created: 5/19/14
 */
object TweetCollector extends App {

  val logger = LoggerFactory.getLogger("TWEETS")

  val namePattern = "([a-zA-Z]+) ([a-zA-Z]+)".r

  // Note: Requires all 4 Twitter auth properties to be set, e.g. passed as system properties or using twitter4j.properties
  val twitterStream = TwitterStreamFactory.getSingleton

  twitterStream.addListener(new StatusListener {

    def log(userId: Long, country: String, firstName: String, secondName: String) =
      logger.info(s"userId:$userId\tcountry:$country\tfirstName:$firstName\tsecondName:$secondName")

    def onStatus(status: Status): Unit = {
      /*
      If it's a geo-tagged tweet with a country field
      and the name matches the standard "firstname secondname" format, we're interested.
      Log it in LTSV format.
       */
      if (status.getPlace != null && status.getPlace.getCountry != null &&
        status.getUser != null && status.getUser.getName != null) {
        status.getUser.getName match {
          case namePattern(firstName, secondName) =>
            log(status.getUser.getId, status.getPlace.getCountry, firstName.toLowerCase, secondName.toLowerCase)
        }
      }
      status.getPlace.getCountry

    }

    def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}
    def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}
    def onException(ex: Exception): Unit = {}
    def onStallWarning(warning: StallWarning): Unit = {}
  })

  twitterStream.sample() // This will block forever

}
