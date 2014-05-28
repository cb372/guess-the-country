import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object SimpleApp extends App {
  case class Tweet(userId: Long, country: String, firstName: String, secondName: String)
  val regex = """\{"userId":"(\d+)","country":"([^"]+)","firstName":"([^"]+)","secondName":"([^"]+)"\}""".r

  def writeToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

  val sc = new SparkContext("local", "TweetMunger", "/Users/chris/code/spark",
    List("target/scala-2.10/tweetmunger_2.10-1.0.jar"))

  val logs = sc.textFile("s3n://guess-the-country/logs/*.gz").cache
  val tweets = logs.map(_.split('\t')(2)).collect {
    case regex(uid, country, first, second) => Tweet(uid.toLong, country, first, second)
  }
  // Note: cannot use tweets.distinct because case class equality is broken in Spark?!
  val distinctUsers = tweets.groupBy(_.userId).map { case (uid, ts) => ts.head }
  val tsv = distinctUsers.map { 
    case Tweet(uid, country, first, second) => s"$first\t$second\t$country"
  }
  tsv.coalesce(1).saveAsTextFile("people.tsv")

}
