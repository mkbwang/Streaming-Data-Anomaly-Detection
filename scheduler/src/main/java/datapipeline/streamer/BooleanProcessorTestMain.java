package datapipeline.streamer;

import datapipeline.KafkaConstants;
import datapipeline.processors.BooleanLogicProcessor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.Locale;
import java.util.Properties;

public class BooleanProcessorTestMain {
    public static void main(String[] argv) {

        final Properties streamsConfiguration = new Properties();
        final Serde<String> stringSerde = Serdes.String();

        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
        // against which the application is run.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "Boolean-application-" + 0);
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "Boolean-streamer-" + 0);

        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);

        // Specify default (de)serializers for record keys and for record values.
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName()); // Set the commit interval to 500ms so that any changes are flushed frequently. The low latency


        final StreamsBuilder builder = new StreamsBuilder();


        final KStream<String, String> view_1 = builder.stream("Boolean_test_0");
        final KStream<String, String> view_2 = builder.stream("Boolean_test_1");



        final KStream<String, String> merged = view_1.merge(view_2);

        StoreBuilder<KeyValueStore<String, String>> booleanLogicStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("BooleanLogic_1"),
                stringSerde,
                stringSerde
        );

        builder.addStateStore(booleanLogicStore);
        KStream<String, String> outputStream = merged.transform(new TransformerSupplier<String, String, KeyValue<String, String>>() {
            public Transformer get() {
                return new BooleanLogicProcessor(2);
            }
        }, "BooleanLogic_1");

        outputStream.to("result_boolean_0");

        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);


        streams.cleanUp();
        streams.start();


        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


        while (true) {
            try {
                Thread.sleep(10000000);
            } catch (Exception e) {

            }
        }
    }
}
