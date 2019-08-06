package datapipeline.streamer;

import com.google.gson.Gson;
import datapipeline.KafkaConstants;
import datapipeline.KafkaManager;
import datapipeline.common.Tuple2;
import datapipeline.common.Tuple2Serde;
import datapipeline.processors.OutlierTransformer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.WindowStore;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class RunningMeanVarLamda implements Runnable{

    private int _threshold;
    private String _source_topic;
    private int _ID;
    private int _warmUp;
    private int _window;

    public RunningMeanVarLamda(int ID, int threshold, String topic, int warmUp, int window) {
        _threshold = threshold;
        _source_topic = topic;
        _ID = ID;
        _warmUp = warmUp;
        _window = window;
    }

    @Override
    public void run() {
        final Properties streamsConfiguration = new Properties();
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Double> doubleSerde = Serdes.Double();

        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
        // against which the application is run.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, KafkaConstants.STREAMER_PREFIX + _ID);
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, KafkaConstants.STREAMER_PREFIX + _ID);

        KafkaManager.deleteTopicWithPrefix(KafkaConstants.STREAMER_PREFIX + _ID);
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);

        // Specify default (de)serializers for record keys and for record values.
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName()); // Set the commit interval to 500ms so that any changes are flushed frequently. The low latency


        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

        final StreamsBuilder builder = new StreamsBuilder();


        //final KStream<String, String> view_1 = builder.stream("Boolean_test_0");
        final KStream<String, String> view = builder.stream(_source_topic);


        KStream<String, Integer> mappedStream = view
                .flatMap((k, v) -> {
                    List<KeyValue<String, Integer>> result = new LinkedList<>();

                    Gson gson = new Gson();
                    int[] value = gson.fromJson(v, int[].class);

                    for (int i = 0; i < value.length; i++) {
                            result.add(new KeyValue<>(Integer.toString(i), value[i]));
                            // KafkaManager.createTopic(job_id, this._ID,(short)1);
                    }
                    return result;
                });

        KGroupedStream<String, Integer> groupedStream = mappedStream.groupByKey(Grouped.with(stringSerde, Serdes.Integer()));
        TimeWindowedKStream <String, Integer> windowedStream = groupedStream.windowedBy(TimeWindows.of(Duration.ofMillis(_window)).grace(Duration.ofMillis((long)(_window * 0.1))));
        KTable<Windowed<String>, Tuple2<Double, Double>> aggregatedStream = windowedStream
                .aggregate(
                        () -> new Tuple2<>(0.0, 0.0),
                        (aggKey, newValue, aggregate)  -> {
                            aggregate.value1 ++;
                            try{
                                aggregate.value2 += (double) newValue;
                            } catch(Exception e) {

                            }
                            return aggregate;
                        },
                        Materialized.<String, Tuple2<Double, Double>, WindowStore<Bytes, byte[]>>as("timed-window").withValueSerde(new Tuple2Serde<>())
                )
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));

        KStream<String, String> anomalousData = aggregatedStream.mapValues(value -> Double.toString(value.value2 / value.value1))
                //.filter((key, value) -> Double.parseDouble(value) > (double)_threshold)
                .toStream()
                .map((k, v) -> KeyValue.pair(k.key(), v));


        //final KStream<String, String> merged = view.merge(view_2);

        StoreBuilder<KeyValueStore<String, Double>> booleanLogicStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(KafkaConstants.CONTROL_INFO_PREFIX + _ID),
                stringSerde,
                doubleSerde
        );

        KeyValueStore kvStore =  booleanLogicStore.build();
        builder.addStateStore(booleanLogicStore);
        /*KStream<String, String> outputStream = merged.transform(new TransformerSupplier<String, String, KeyValue<String, String>>() {
            public Transformer get() {
                return new BooleanLogicTransformer(2);
            }
        }, "BooleanLogic_1");*/

        KStream<String, String> outputStream = anomalousData.transform( () -> {
                    return new OutlierTransformer<>(_threshold, _ID, _warmUp);
                }
                , KafkaConstants.CONTROL_INFO_PREFIX + _ID);
        outputStream.to(KafkaConstants.STREAMER_OUTPUT_TOPIC_PREFIX + _ID);

        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);


        streams.cleanUp();
        streams.start();


        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
