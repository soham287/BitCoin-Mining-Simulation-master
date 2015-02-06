package sha256
import akka.actor.{ Actor, ActorRef, Props, ActorSystem }
import akka.actor.actorRef2Scala
import akka.dispatch.ExecutionContexts.global
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration.DurationInt
import scala.io.Source.fromFile
import java.security.MessageDigest
import akka.routing.RoundRobinRouter

case class HelpMining(k: Integer,gatorid:String,string: String)
case class ConsolidateCoins(tries: Integer,bitcoin: String, hexvalue: String)
case class NumberOfTries()

object SHA256 extends App {
 
  import akka.util.Timeout
  import scala.concurrent.duration._
  import akka.pattern.ask
  import akka.dispatch.ExecutionContexts._
  implicit val ec = global
 
  override def main(args: Array[String]) {
  implicit val system = ActorSystem("LocalSystem")
   println("\n\nEnter 'k' - the required number of leading zeroes : ")
    val k= readInt
	println ("\nConstructing the search space.......\n\n")
    val actor = system.actorOf(Props(new MineBitcoins("dnair",k)))
    val act2 = system.actorOf(Props[RemoteActor1], name = "RemoteActor1")
  implicit val timeout = Timeout(25 seconds)
    val future = actor ? StartMining()
                                                   

}
}

class RemoteActor1 extends Actor {
  def receive = {
    case msg: String => 
        println("\n Bitcoins from client \n\n"+msg)
        
  }
}
case class StartMining()
class MineBitcoins(gatorid: String,k: Integer) extends Actor {
 
  private var running = false
  private var fileSender: Option[ActorRef] = None
  private var tries = 0
 var remote = context.actorFor("akka://HelloRemoteSystem@127.0.0.1:5150/user/RemoteActor")
 
  
  // LOGIC TO GET STRING PREFIXED WITH GATORID, APPEND SOME STRINGS, DIVIDE AND ASSIGN IT TO WORKERS
  
  
  def receive = {
    
    case msg: String => 
        println(s": '$msg'")
        
    case StartMining() => {
   
        val processes = Runtime.getRuntime().availableProcessors();
        val actorcount=(3*processes)/2
        val actor = context.actorOf(Props[GetBitcoins].withRouter(RoundRobinRouter(actorcount)))
        var tobesent=k+""
        for(i <- 1 to 100000)
        tobesent+=scala.util.Random.alphanumeric.take(5).mkString+" "
		println("Search space created.....Now mining Bitcoins...!!\n\n")
       val toremote= tobesent substring(0,tobesent.length/2)
        tobesent =  tobesent substring(tobesent.length/2+1,tobesent.length - 1)
        remote ! toremote+""
        actor ! HelpMining(k,gatorid,tobesent)
  
      }
          
    //  }
    
    case NumberOfTries() =>
    {
      tries=tries+1
      if(tries==100000)
      {
        context.system.shutdown()
      }
    }
    
    case ConsolidateCoins(tries,bitcoin,hexvalue/*BITCOINS RECEIVED FROM WORKERS*/) => {
      println("Bitcoin : " + bitcoin + "     "+ hexvalue)
   
      
    }
    
    case _ => //println("Message not recognized!")
  }
  
  
  
}

 
class GetBitcoins extends Actor {
  var tries=0
  def receive = {
    case HelpMining(k,gatorid,string) => {
       
      //LOGIC TO MINE BITCOINS FROM STRING RECEIVED. - IMPLEMENTED
        val stringarr= string.split(" ")
        val sha = MessageDigest.getInstance("SHA-256")
        for(i<-0 to stringarr.length-1)
        {
         val stringwithseed=gatorid+stringarr(i)
        sha.update(stringwithseed.getBytes("UTF-8"))  
        val digest = sha.digest();
    
        val hexString = new StringBuffer();

        for ( j <- 0 to digest.length-1)   
        {
            val hex = Integer.toHexString(0xff & digest(j)); 
            if(hex.length() == 1) hexString.append('0'); 
            hexString.append(hex);
        }
         sender ! NumberOfTries()
         
       // hexString.toString() is the hashed value.
       var a=0
        for (c <- 0 to k-1)
        if (hexString.toString().charAt(c)=='0')
           a = a+1
           if (a==k)
           { 
       
         sender ! ConsolidateCoins(tries,stringwithseed,hexString.toString()/*BITCOIN AND MAYBE HASH VALUE AS WELL*/)
           }
           }
           }
    case _ => //println("Error: message not recognized")
  }
} 


 


