package com.github.streaming.kafka

import java.util.HashMap

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.log4j.{Level, Logger}


object TopAirlineCancellation {
  val logger = Logger.getLogger(getClass.getName)
  
  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("Usage: TopAirlineCancellation <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }

    
    val Array(zkQuorum, group, topics, numThreads) = args
    val sparkConf = new SparkConf().setAppName("TopAirlineCancellation").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(10))
    ssc.checkpoint("checkpoint")

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1))
      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(10), 2)
    //wordCounts.print()
    wordCounts.saveAsTextFiles("/home/cloudera/Downloads/stream_output/total_cancellations",".csv")
    
    ssc.start()
    ssc.awaitTermination()
  }

}