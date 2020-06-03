package datapipeline;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {

    public static void main(String[] args) throws InterruptedException {
        //Producer<Long, String> prod = ProducerFactory.createProducer();


        KafkaManager km = KafkaManager.getInstance();
        KafkaManager.deleteTopicWithPrefix("anomaly_0");
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "100");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //props.setProperty("auto.offset.reset", "latest");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);


        consumer.subscribe(Arrays.asList("anomaly_0"));

        Long lastTimestamp = (long)0;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long start_time = timestamp.getTime();
        int index = 0;
        double overall = 0f;
        while (true) {
            //prod.send(new ProducerRecord<Long, String>(KafkaConstants.TEST_TOPIC, (long) 0, "Test2"));
            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
            Long tmp = (long)0;
            for (ConsumerRecord<String, String> record : records) {
                tmp = new Timestamp(System.currentTimeMillis()).getTime();
                // timestamp = new Timestamp(System.currentTimeMillis());
                // long interval = tmp - lastTimestamp;
                long interval = tmp - (long) Double.parseDouble(record.value());
                //System.out.printf("offset = %d, key = %s, value = %s, interval=%d\n", record.offset(), record.key(), record.value(), interval);
                overall += interval;
                index += 1;
                //lastTimestamp = tmp;
                //System.out.println(index);

                /*if (index % 3000000 == 0) {
                    long curr_time = new Timestamp(System.currentTimeMillis()).getTime();
                    //System.out.println(index);
                    double throughput = (double) index * 48f / (1024f * 1024f * ((curr_time - start_time) / 1000f));
                    System.out.printf("Bandwidth: > 40 Gib/s, Message size: %d Bytes, Number of Message: %d, Throughput: %f MB/s\n, Delay: %f", 48, index, throughput);
                }*/
            }
            System.out.printf("Average delay: %f\n", overall / (double)index);


            //System.out.printf("average_time = %f\n", (double)overall / (double)index);
            //index = 0;
            //overall = 0;
            //long curr_time = new Timestamp(System.currentTimeMillis()).getTime();
            //System.out.printf("Throughput:" , (double)index * 100f / (1024f * 1024f * ((curr_time - start_time) / 1000)));

        }
        /*Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> prod = new KafkaProducer<String, String>(props);

        //while (true) {
        //prod.send(new ProducerRecord<Long, String>(KafkaConstants.TEST_TOPIC, (long) 0, "Test2"));
        int[][] matrix = new int[5][5];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = i + j;
            }
        }
        Gson gs = new Gson();
        String value = gs.toJson(matrix);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        prod.send(new ProducerRecord<>("test", 0, timestamp.getTime(), KafkaConstants.CLIENT_ID + "@" + timestamp.getTime(), value));
        //Thread.sleep(3000);
        prod.close();*/

    }
}
