package datapipeline;

public interface KafkaConstants {
    public static String KAFKA_BROKERS = "localhost:9092";

    public static Integer MESSAGE_COUNT = 1000;

    public static String CLIENT_ID = "client1";

    public static String TEST_TOPIC = "test";

    public static String GROUP_ID_CONFIG="consumerGroup1";

    public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;

    public static String OFFSET_RESET_LATEST="latest";

    public static String OFFSET_RESET_EARLIER="earliest";

    public static Integer MAX_POLL_RECORDS=1;

    public static String STREAMER_PREFIX = "anomaly-detection-application-";

    public static String CONTROL_INFO_PREFIX = STREAMER_PREFIX + "parameter-";

    public static String CONTROL_FLOW_TOPIC_PREFIX = STREAMER_PREFIX + "control-";

    public static String STREAMER_OUTPUT_TOPIC_PREFIX = "anomaly_";

    public static String CONTROL_MESSAGE_PREFIX = STREAMER_PREFIX + "control-message-";
}
