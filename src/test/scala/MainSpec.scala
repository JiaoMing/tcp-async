import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class MainSpec extends FlatSpec with ShouldMatchers {
  "Main" should "have tests" in {
    true should be === true
  }
}
