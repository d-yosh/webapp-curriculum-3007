import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Random, Success}

object CompositeFutureChallenge extends App {

  val random = new Random()
  val f1 = Future {
    random.nextInt(10)
  }
  val f2 = Future {
    random.nextInt(10)
  }
  val f3 = Future {
    random.nextInt(10)
  }
  val f4 = Future {
    random.nextInt(10)
  }

  val compositeFuture: Future[Int] = for {
    i1 <- f1
    i2 <- f2
    i3 <- f3
    i4 <- f4
  } yield i1 * i2 * i3 * i4

  compositeFuture onComplete {
    case Success(value) => println(value)
    case Failure(throwable) => throwable.printStackTrace()
  }
  Await.result(compositeFuture, Duration.Inf)
}
