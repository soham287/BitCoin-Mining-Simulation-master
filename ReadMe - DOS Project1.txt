Distributed Operating Systems Project #1

Name:                    UFID:        Email:                           GatorLink
Rahul Nyayapathi      - 03819264  -  nrahul1993@gmail.com    	      nrahul1993
Soham Talukdar        - 38975097  -  soham.talukdar@gmail.com         stalukdar

UFID used :stalukdar
Scala Project 1.zip folder contains two scala files 'Local' and 'HelloRemote'
'Local' is your master 
'HelloRemote' is your sub master
The server has it's workers which run the sha function on the server
There is a master class in client acts as a submaster which runs the workers on its machine
This way both server and client are mining bitcoins
Server sends the UFID to the client so that it can assign its subworkers to mine bitcoins.
At the end of the execution the client sends the bitcoins to the server
Server prints all bitcoins
The steps to execute them are as follows:
  1) Run these in sbt
  2) Change the server / client IPs in the application.config files and scala files accordingly
  3)Change the IP of the client you want to connect to in the SERVER
  4) Run the Server and pass the necessary argument of the leading zeroes to the variable leadingZeroes in server and client. WE HAVE KEPT THE DEFAULT VALUE OF LEADINGZEROES AS 3
  5) Immediately after, run the Client program and pass the necessary argument which is the IP address of the server.

The following result was tested on Windows OS, one i5 processor of 4 cores and another processor of 2 cores.
Time was measured in the unit of seconds.

1)We determined results for work units of 1000, 10000 and 100000. Here, work unit is the number of inputs for which Bitcoins were mined.

2) On running the code with k=4, managed to mine 34 Bitcoins in a duration of 10 seconds. The result file (output_4.doc) has been included in the project zip.

3) With k=5, there were 3 Bitcoins with a duration of 10 seconds.

4) The coins with the most 0s we managed to find were: 


5) We were able to run the code on 2 machines.

References:

http://doc.akka.io/docs/akka/2.2-M2/scala/remoting.html
http://doc.akka.io/docs/akka/2.0/intro/getting-started-first-scala.html
http://www.scala-lang.org/documentation/