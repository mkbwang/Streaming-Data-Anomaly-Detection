package datapipeline;

import com.google.gson.Gson;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.sql.Time;
import java.sql.Timestamp;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MockProducer {
    public static void main(String[] args) throws InterruptedException {
        //Producer<Long, String> prod = ProducerFactory.createProducer();


        /*Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test_con");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("test"));

        while (true) {
            //prod.send(new ProducerRecord<Long, String>(KafkaConstants.TEST_TOPIC, (long) 0, "Test2"));
            ConsumerRecords<Long, String> records = consumer.poll(1000);
            for (ConsumerRecord<Long, String> record : records)
                System.out.printf("offset = %d, key = %d, value = %s%n", record.offset(), record.key(), record.value());
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        }*/

        /*KafkaManager km = KafkaManager.getInstance();
        ListTopicsResult listResult = KafkaManager.listTopics(true);
        try {
            for (String name : listResult.names().get()) {
                //if (name.startsWith("anomaly-detection-streamer-application-test-" + 0)) {
                    System.out.println(name);
                    KafkaManager.deleteTopic(name);
                //}
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }*/


        int [] matrix = new int[5];

        for (int i = 0 ; i < 5; i++) {
            matrix[i] = 30;
        }

        Gson gs = new Gson();
        String value = gs.toJson(matrix);
        int index = 0 ;
        while (index < 100) {
            Properties props = new Properties();

            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            Producer<String, String> prod = new KafkaProducer<String, String>(props);

            //while (true) {
            //prod.send(new ProducerRecord<Long, String>(KafkaConstants.TEST_TOPIC, (long) 0, "Test2"));

            /*int[][] matrix = new int[5][5];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    prod.send(new ProducerRecord<>("test", 0, timestamp.getTime(), Integer.toString(i * matrix[i].length + j), Integer.toString(i + j)));
                }
            }*/
            //Gson gs = new Gson();
            //String value = gs.toJson(matrix);
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //prod.send(new ProducerRecord<>("Boolean_test_0", 0, timestamp.getTime(), "0", "1"));
            //prod.send(new ProducerRecord<>("Boolean_test_1", 0, timestamp.getTime(), "0", "0"));
            //Thread.sleep(3000);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            prod.send(new ProducerRecord<>("test", 0, timestamp.getTime(), "key", value));
            prod.send(new ProducerRecord<>("test", 0, timestamp.getTime(), "key2", value));
            //prod.send(new ProducerRecord<>(KafkaConstants.CONTROL_FLOW_TOPIC_PREFIX + 0, 0, timestamp.getTime(), KafkaConstants.CONTROL_MESSAGE_PREFIX + 0, "102"));
            prod.close();
            Thread.sleep(50);
            index++;
       }

        // Thread.sleep(50);

    }
}
