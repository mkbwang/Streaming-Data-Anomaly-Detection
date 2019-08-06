package datapipeline.streamer;

import datapipeline.KafkaConstants;
import datapipeline.KafkaManager;
import datapipeline.processors.ControlledFilterTransformer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

public class ControlledFilterWrapper<K, V> {


    public KStream<K, V> getControlledStream(KStream<K, V> stream, StreamsBuilder builder, String source, ControlledFilterTransformer<K, V> controller, int ID) {

        KafkaManager.createTopic(source, 1 ,(short)1);

        final KStream<K, V> controlledFlow = builder.stream(source);

        final KStream<K, V> mergedFlow = stream.merge(controlledFlow);


        StoreBuilder<KeyValueStore<String, Double>> booleanLogicStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(KafkaConstants.CONTROL_INFO_PREFIX + ID),
                Serdes.String(),
                Serdes.Double()
        );


        builder.addStateStore(booleanLogicStore);

        KStream<K, V> outputStream = mergedFlow.transform( () -> {
                    return controller;
                }
                , KafkaConstants.CONTROL_INFO_PREFIX + ID);

        return outputStream;
    }
}
