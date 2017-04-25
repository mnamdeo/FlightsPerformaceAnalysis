package com.github.streaming.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import java.util.HashMap
import scala.io.Source
object KafkaFlightDataProducer {
  // Produces some random words between 1 and 100.
  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("Usage: KafkaWordCountProducer <metadataBrokerList> <topic> " +
        "<messagesPerSec> <wordsPerMessage>")
      System.exit(1)
    }

    val Array(brokers, topic, messagesPerSec, wordsPerMessage) = args
    
    // Zookeeper connection properties
    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    
    // Send some messages
     val bufferedSource = Source.fromFile("/home/cloudera/Downloads/On_Time_On_Time_Performance_2017_1/On_Time_On_Time_Performance_2017_1.csv")
    val idToCancellation = bufferedSource.getLines().drop(1).map(line => {
  val lines = line.split(',')
  (lines(7), lines(47))
})
     for (line <- idToCancellation) {
       if(line._2=="1.00"){
      val cols = line._1+""
       
        // do whatever you want with the columns here
        val message = new ProducerRecord[String, String](topic, null, cols)
        producer.send(message)
    }}
    
          
  }

}