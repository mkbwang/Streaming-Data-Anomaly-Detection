package datapipeline;

import org.apache.kafka.clients.admin.*;
import scala.reflect.internal.Trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaManager {

    public static KafkaManager instance;

    private AdminClient admin;

    public KafkaManager() {
        Properties prop = new Properties();

        prop.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
        prop.put(AdminClientConfig.CLIENT_ID_CONFIG, "anomaly_detection_admin");

        admin = AdminClient.create(prop);
    };

    public static KafkaManager getInstance()
    {
        if (instance == null) {

            // double lock checking for thread safe
            synchronized (KafkaManager.class) {
                if (instance == null) {
                    instance = new KafkaManager();
                }
            }
        }
        return instance;
    }


    public static CreateTopicsResult createTopic(String topic, int numPartitions, short replicationFactor) {
        NewTopic newTopic = new NewTopic(topic, numPartitions, replicationFactor);
        List<NewTopic> topicList = new ArrayList<NewTopic>();
        topicList.add(newTopic);
        CreateTopicsResult result = instance.admin.createTopics(topicList);

        while(!result.all().isDone()) {
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {

            }
        }
        return result;
    }

    public static DeleteTopicsResult deleteTopic(String topic) {
        List<String> topicList = new ArrayList<>();
        topicList.add(topic);
        DeleteTopicsResult result = instance.admin.deleteTopics(topicList);

        while(!result.all().isDone()) {
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {

            }
        }

        return result;
    }

    public static ListTopicsResult listTopics(boolean isInternal)
    {
        ListTopicsOptions op = new ListTopicsOptions();
        op.timeoutMs(1000);
        op.listInternal(isInternal);
        return instance.admin.listTopics(op);
    }

    public static void deleteTopicWithPrefix(String prefix) {

        ListTopicsResult listResult = KafkaManager.listTopics(true);
        try {
            for (String name : listResult.names().get()) {
                if (name.startsWith(prefix)) {
                    System.out.println(name);
                    KafkaManager.deleteTopic(name);
                }
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
    }
}
