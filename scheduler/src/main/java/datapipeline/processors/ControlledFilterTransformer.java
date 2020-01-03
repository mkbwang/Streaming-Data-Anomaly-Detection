package datapipeline.processors;

import datapipeline.KafkaConstants;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class ControlledFilterTransformer<K, V> implements Transformer<K, V, KeyValue<K, V>> {

    private ProcessorContext content;

    // private WindowStore<E, Long> windowStore;

    private KeyValueStore<String, Double> kvStore;

    private double _threshold;
    private int _ID;
    private int _warmup;
    private double _alpha;

    public ControlledFilterTransformer(int ID, int threshold) {
        _ID = ID;
        _threshold = threshold;
    }

    /**
     * Key: event ID
     * Value: timestamp
     */
    @Override
    public void init(ProcessorContext context)
    {
        this.content = context;

        kvStore = (KeyValueStore) context.getStateStore(KafkaConstants.CONTROL_INFO_PREFIX + _ID);

    }


    @Override
    public KeyValue<K, V> transform(K key, V value)
    {
        if (kvStore.get("threshold") != null) {
            _threshold = kvStore.get("threshold");
        }

        if (key.toString().startsWith(KafkaConstants.CONTROL_MESSAGE_PREFIX + _ID)) {
            kvStore.put("threshold", Double.parseDouble(value.toString()));
            _threshold = kvStore.get("threshold");
            System.out.println(_threshold);
        } else {
            if (Double.parseDouble(value.toString()) > _threshold) {
                return new KeyValue<>(key, (V) (value.toString() + ':' + _threshold));
            }
        }

        return null;
    }


    @Override
    public void close() {
    }
}
