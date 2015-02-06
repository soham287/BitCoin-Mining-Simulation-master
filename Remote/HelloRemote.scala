package remote

import akka.actor.{ Actor, ActorRef, Props, ActorSystem }
import akka.actor.actorRef2Scala
import akka.dispatch.ExecutionContexts.global
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration.DurationInt
import scala.io.Source.fromFile
import java.security.MessageDigest
import akka.actor.{ Address, AddressFromURIString }
import akka.routing.RoundRobinRouter

case class HelpMining(k: Integer,gatorid:String,string: String)
case class NumberOfTries()


object HelloRemote extends App  {
  val system = ActorSystem("HelloRemoteSystem")
  val remoteActor = system.actorOf(Props[RemoteActor], name = "RemoteActor")
}

class GetBitcoins extends Actor {
  var remote1 = context.actorFor("akka://LocalSystem@127.0.0.1:5151/user/RemoteActor1")
  var tries=0
  def receive = {
    case HelpMining(k,gatorid,string) => {
      
       
      //LOGIC TO MINE BITCOINS FROM STRING RECEIVED. - IMPLEMENTED
        val stringarr= string.split(" ")
        val sha = MessageDigest.getInstance("SHA-256")
        var qwe = ""
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

         
       // hexString.toString() is the hashed value.
       var a=0
        for (c <- 0 to k-48-1)
        if (hexString.toString().charAt(c)=='0')
           a = a+1
           if (a==k-48)
           { 
        qwe += "Bitcoin : " + stringwithseed + "     "+ hexString.toString()+"\n"
         
      
           }
           }
        //println(qwe)
        remote1 ! qwe
           }
    
  }
} 

class RemoteActor extends Actor {
var tries = 0
  
  def receive = {
    case msg: String => {
       println("RemoteActor received message")
       val processes = Runtime.getRuntime().availableProcessors();
        val actorcount=(3*processes)/2
        val actor = context.actorOf(Props[GetBitcoins].withRouter(RoundRobinRouter(actorcount)))
       
      val k = msg.charAt(0)
     
      val gatorid="dnair"
        val tobesent=msg substring(1,msg.length-1)
     sender ! (actor ! HelpMining(k,gatorid,tobesent)) 
}
      
       case NumberOfTries() =>
    {
      tries=tries+1
      if(tries==1000)
      {
        context.system.shutdown()
      }
    }
  }
}


