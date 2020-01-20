import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.{Future, Promise}
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

object CountDownLatchSample extends App {
  val random = Random
  val maxWaitMillis = 1000
  val counter = new AtomicInteger(0)
  val promises = for (i <- 1 to 3) yield Promise[Int]
  val futures = for (i <- 1 to 8)
    yield
      Future[Int] {
        val waitMilliSec = random.nextInt(maxWaitMillis)
        Thread.sleep(waitMilliSec)
        waitMilliSec
      }

  futures.foreach { f =>
    f.foreach {
      case waitMilliSec =>
        val index = counter.getAndIncrement
        if (index < promises.length) {
          promises(index).success(waitMilliSec)
        }
    }
  }
  promises.foreach {
    p => p.future.foreach {
      case waitMilliSec => println(waitMilliSec)
    }
  }

  Thread.sleep(5000)
}
