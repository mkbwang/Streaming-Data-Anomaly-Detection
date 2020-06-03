package datapipeline.processors;

import datapipeline.KafkaConstants;
import kafka.Kafka;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.WindowStore;


public class OutlierTransformer<K, V> implements Transformer<K, V, KeyValue<String, String>> {

    private ProcessorContext content;

    // private WindowStore<E, Long> windowStore;

    private KeyValueStore<String, Double> kvStore;

    private int _threshold;
    private int _ID;
    private int _warmup;
    private double _alpha = 0.25;

    public OutlierTransformer(int threshold, int ID, int warmup) {
        _threshold = threshold;
        _ID = ID;
        _warmup = warmup;
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
    public KeyValue<String, String> transform(K key, V value)
    {
        Double num, prev_mean = 0.0 , prev_var = 0.0 , mean = 0.0, var;

        double casted_value = Double.valueOf(value.toString());

        if (kvStore.get(key + "_mean") != null) {
            num = kvStore.get(key + "_num") + 1;
            prev_mean = kvStore.get(key + "_mean");
            prev_var = kvStore.get(key + "_variance");

            //mean = prev_mean + (casted_value - prev_mean) / num;
            //var = prev_var + (casted_value - mean) * ( casted_value - prev_mean);

            // Other option here: Exponentially weighted running average

            double theta = casted_value - prev_mean;
            mean = prev_mean + _alpha * theta;
            var = (1 - _alpha) * (prev_var + _alpha * theta * theta);

        } else {
            num = (double) 1;
            mean = (double) casted_value;
            var = (double) 0;
        }

        if (num > (double)_warmup) {
            if (casted_value - prev_mean > _threshold * Math.sqrt(prev_var)) {

                return KeyValue.pair(key.toString(), Double.toString(Math.abs(casted_value - prev_mean)));
            }
        }

        kvStore.put(key + "_num", num);
        kvStore.put(key + "_mean", mean);
        kvStore.put(key + "_variance", var);

        return null;
    }


    @Override
    public void close() {

    }
}
