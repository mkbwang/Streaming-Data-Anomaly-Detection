package datapipeline;

import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.common.KafkaFuture;
import scala.Int;

import java.util.Map;

public class KafkaManagerTest {

    public static void main(String argv[]) throws InterruptedException {
        KafkaManager.getInstance();
        //CreateTopicsResult result =  KafkaManager.createTopic("java_client_test", 1, (short) 1);
        DeleteTopicsResult result = KafkaManager.deleteTopic("java_client_test");

        for (Map.Entry<String, KafkaFuture<Void>> entry : result.values().entrySet()) {
            System.out.println(entry.getKey());

            while (!entry.getValue().isDone()) {
                Thread.sleep(1000);
                System.out.println("Currently creating the topic");
            }

        }


    }
}
