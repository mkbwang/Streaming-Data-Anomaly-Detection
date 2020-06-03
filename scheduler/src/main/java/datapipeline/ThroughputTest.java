package datapipeline;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import sun.jvm.hotspot.debugger.cdbg.Sym;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.sql.Timestamp;
import java.util.Properties;

public class ThroughputTest {

    public static void main(String[] args) throws InterruptedException {

        KafkaManager km = KafkaManager.getInstance();
        KafkaManager.deleteTopicWithPrefix("throughput");

        while (true) {
            Properties props = new Properties();

            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            Producer<String, String> prod = new KafkaProducer<String, String>(props);
            long[] matrix = new long[1024];

            Gson gs = new Gson();
            int index = 0;

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < 1024; i++) {
                matrix[i] = timestamp.getTime();
            }
            //System.out.println(timestamp.getTime());
            String value = gs.toJson(matrix);

            System.out.println(value.length());

            while (index <= 96) {
                prod.send(new ProducerRecord<>("throughput", 0, timestamp.getTime(), Integer.toString(index), value));
                index++;
            }

            timestamp = new Timestamp(System.currentTimeMillis());
            //System.out.println(timestamp.getTime());
            prod.close();
            Thread.sleep(1000);
        }


        /*KafkaManager km = KafkaManager.getInstance();
        KafkaManager.deleteTopicWithPrefix("throughput");
        int[] matrix = new int[12];

        for (int i = 0; i < 12; i++) {
            matrix[i] = 10;
            //matrix[i] = 30;
        }

        Gson gs = new Gson();

        String value = gs.toJson(matrix);

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        Producer<String, String> prod = new KafkaProducer<String, String>(props);

        int index = 0;

        while (true) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            prod.send(new ProducerRecord<>("throughput", 0, timestamp.getTime(), Integer.toString(index), value));
            index ++;
        }*/



    }
}
