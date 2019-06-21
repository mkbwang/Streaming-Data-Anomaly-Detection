package datapipeline.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class Tuple2Serializer<T1, T2> implements Serializer<Tuple2<T1, T2>> {
    private Gson gson = new GsonBuilder().create();

    @Override
    public void configure(Map config, boolean isKey) {
        // this is called right after construction
        // use it for initialisation
    }

    @Override
    public byte[] serialize(String s, Tuple2<T1, T2> t) {
        return gson.toJson(t).getBytes();
    }

    @Override
    public void close() {
        // this is called right before destruction
    }
}
