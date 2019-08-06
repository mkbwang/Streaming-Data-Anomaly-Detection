package datapipeline.api;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import datapipeline.KafkaConstants;
import datapipeline.data.AnomalyDetectionJob;
import datapipeline.data.JobBaseModel;
import datapipeline.data.JobResponse;
import datapipeline.streamer.MatrixThresholdLamda;
import datapipeline.streamer.RunningMeanVarLamda;
import datapipeline.streamer.WindowedKeyAmountLamda;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobController {
    private final AtomicInteger counter = new AtomicInteger();
    private final ObjectMapper mapper = new ObjectMapper();


    @RequestMapping(value = "api_v1/job", method = RequestMethod.POST)
    public JobResponse StartJobController(@RequestBody JsonNode request) {
        System.out.println(request.toString());

        String type = request.get("type").asText();
        int threshold = request.get("threshold").asInt();

        if(type.equals("update")) {
            int ID = request.get("ID").asInt();

            if (ID >= counter.get()) {
                return new JobResponse(1, ID);
            }

            Properties props = new Properties();

            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            Producer<String, String> prod = new KafkaProducer<String, String>(props);

            prod.send(new ProducerRecord<>(KafkaConstants.CONTROL_FLOW_TOPIC_PREFIX + ID , KafkaConstants.CONTROL_MESSAGE_PREFIX + ID, Integer.toString(threshold)));
            prod.close();

            return new JobResponse(0, ID);
        }
        String topic = request.get("topic").asText();
        int ID = counter.getAndAdd(1);

        if (type.equals("value")) {
            int window = request.get("window").asInt();
            Thread streamer = new Thread(new MatrixThresholdLamda(ID, threshold, topic, window));
            streamer.start();
        } else if (type.equals("mean")) {
            int warmUp = request.get("warmup").asInt();
            int window = request.get("window").asInt();
            Thread streamer = new Thread(new RunningMeanVarLamda(ID, threshold, topic, warmUp, window));
            streamer.start();
        } else if (type.equals("windowedKeyCount")) {
            int window = request.get("window").asInt();
            AnomalyDetectionJob job = new AnomalyDetectionJob(
                    new JobBaseModel(ID, KafkaConstants.STREAMER_OUTPUT_TOPIC_PREFIX + ID)
                    ,window
                    ,topic
                    ,threshold
                    );
            Thread streamer = new Thread(new WindowedKeyAmountLamda(job));
            streamer.start();
        } else {
            counter.decrementAndGet();
            return new JobResponse(1, 0);
        }


        return new JobResponse(0, ID);
    }
}
