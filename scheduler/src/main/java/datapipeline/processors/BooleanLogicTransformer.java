package datapipeline.processors;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BooleanLogicTransformer implements Transformer<String, String, KeyValue<String, String>> {

    private ProcessorContext context;
    private KeyValueStore<String, String> kvStore;
    private int ruleNum;

    public BooleanLogicTransformer(int ruleNum) {
        this.ruleNum = ruleNum;
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        kvStore = (KeyValueStore) context.getStateStore("BooleanLogic_1");

        // schedule a punctuate() method every second based on event-time
        this.context.schedule(Duration.ofSeconds(1), PunctuationType.WALL_CLOCK_TIME, (timestamp) -> {
            KeyValueIterator<String, String> iter = this.kvStore.all();
            List<String> keyToDelete = new ArrayList<>();

            while (iter.hasNext()) {
                KeyValue<String, String> entry = iter.next();
                String[] list = entry.value.split(";");

                if (list.length >= this.ruleNum) {
                    Boolean runningBool;
                    if (Integer.parseInt(list[0]) == 1) {
                        runningBool = true;
                    } else {
                        runningBool = false;
                    }


                    for (int i = 1; i < this.ruleNum; i++) {
                        if (Integer.parseInt(list[i]) == 1) {
                            runningBool &= true;
                        } else {
                            runningBool &= false;
                        }
                    }
                    if(runningBool) {
                        context.forward(entry.key, entry.key);
                    }
                    keyToDelete.add(entry.key);
                }
            }
            iter.close();

            for (String v: keyToDelete
                 ) {
                kvStore.delete(v);
            }

            // commit the current processing progress
            context.commit();
        });
    }

    @Override
    public KeyValue<String, String> transform(String key, String value) {

        if (kvStore.get(key) == null) {
            kvStore.put(key, value);
        } else {
            String v = kvStore.get(key);
            kvStore.put(key, v + ";" + value);
        }

        return null;
    }



    @Override
    public void close() {
        // Nothing to do
    }
}
