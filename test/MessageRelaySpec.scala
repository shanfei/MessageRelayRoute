import org.specs2.mutable.Specification
import models.Input

import scala.collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: shanfei
 * Date: 10/13/13
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
class MessageRelaySpec extends Specification{

  " MessageRelay " should {

    " return Output contain small route for 1 recipient " in {
         import models.OutputT
         val input = new Input("This is a message", List("1234567890").to[ListBuffer])
         var output:OutputT = models.dispatcher.dispatch(Some(input))
         output.message must be equalTo(Some("This is a message"))
         output.routes(0).recipients.size must be equalTo 1
    }

    " return Output contain medium route for 5 recipients " in {
      import models.OutputT
      val input = new Input("This is a message", List("1234567890","123456789","1234567890","1234567890","1234567890").to[ListBuffer])
      var output:OutputT = models.dispatcher.dispatch(Some(input))
      output.message must be equalTo(Some("This is a message"))
      output.routes(0).recipients.size must be equalTo 5
      output.routes(0).ip must be equalTo "10.0.2.1"
    }

    " return Output contain medium route for 5 recipients and small route for 1 recipient" in {
      import models.OutputT
      val input = new Input("This is a message", List("1234567890","123456789","1234567890","1234567890","1234567890", "1234567890").to[ListBuffer])
      var output:OutputT = models.dispatcher.dispatch(Some(input))
      output.message must be equalTo(Some("This is a message"))
      output.routes(0).recipients.size must be equalTo 5
      output.routes(0).ip must be equalTo "10.0.2.1"
      output.routes(1).recipients.size must be equalTo 1
      output.routes(1).ip must be equalTo "10.0.1.1"
    }

    " return Output Error on error input " in {
      import models.OutputT
      val input = None
      val output:OutputT = models.dispatcher.dispatch(input)
      output.code must be equalTo 1
      output.message must be equalTo Some("request input is null")
    }







  }

}
