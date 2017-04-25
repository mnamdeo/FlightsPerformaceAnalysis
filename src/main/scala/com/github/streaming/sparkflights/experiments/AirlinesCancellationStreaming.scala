package com.github.streaming.sparkflights.experiments

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

class AirlinesCancellationStreaming(args: Array[String]) {
   
    val sparkConf = new SparkConf().setAppName("Flights").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(10))
    
    val linesDStream = ssc.textFileStream(args(0))
     val pmap = linesDStream.map ( line => {
       val lines = line.split (",") 
       (lines(7), lines(47))  
     })
     
     val filteredRDD = pmap.filter(t => t._2 == "1.00")
     val cancelledcount = filteredRDD.map(x=> (x._1,1)).reduceByKey(_ + _)
    
    cancelledcount.print
    
     ssc.start()
     ssc.awaitTermination()

}

object FlightsMainStream {

    //
    def main(args: Array[String]) {
 
    if (args.length < 1) {
      System.err.println("Usage: AirlinesCancellation <directory>")
      System.exit(1)
    }
 
      val f = new AirlinesCancellationStreaming(args)

    }

}