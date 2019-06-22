package datapipeline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import datapipeline.common.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Vector;

public class FakeProducer implements Runnable {

    public void run() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> prod = new KafkaProducer<String, String>(props);

        //while (true) {
            //prod.send(new ProducerRecord<Long, String>(KafkaConstants.TEST_TOPIC, (long) 0, "Test2"));
            int[][] matrix = new int[5][5];


            for (int i = 0; i < matrix.length; i++)
            {
                for (int j = 0; j < matrix[i].length; j++)
                {
                    matrix[i][j] = i + j;
                }
            }
            Gson gs = new Gson();
            String value = gs.toJson(matrix);
            prod.send(new ProducerRecord<String, String>("test9", "key", value));

            /*try {
                Thread.sleep(10000);
            } catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }*/
       // }
    }
}
