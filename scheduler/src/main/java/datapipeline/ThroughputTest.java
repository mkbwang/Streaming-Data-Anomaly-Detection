package datapipeline;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.sql.Timestamp;
import java.util.Properties;

public class ThroughputTest {

    public static void main(String[] args) throws InterruptedException {

        while (true) {
            Properties props = new Properties();

            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            Producer<String, String> prod = new KafkaProducer<String, String>(props);
            int[] matrix = new int[1024];

            Gson gs = new Gson();
            int index = 0;

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());


            System.out.println(timestamp.getTime());
            //long BASE = 1564976193667L;
            for (int i = 0; i < 1024; i++) {
                matrix[i] = (int) (timestamp.getTime() - 1564976963439L);
                //matrix[i] = 30;
            }
            System.out.println(timestamp.getTime() - 1564976963439L);
            String value = gs.toJson(matrix);


            while (index <= 96) {
                prod.send(new ProducerRecord<>("throughput", 0, timestamp.getTime(), Integer.toString(index), value));
                index++;
            }

            timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp.getTime() - 1564976963439L);
            prod.close();
            Thread.sleep(1000);
        }



    }
}
