package datapipeline.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class Tuple2Serde<T1, T2> implements Serde<Tuple2<T1, T2>> {

    private Gson gson = new GsonBuilder().create();

    private Tuple2Serializer tuple2Serializer = new Tuple2Serializer();
    private Tuple2Deserializer tuple2Deserializer = new Tuple2Deserializer();


    @Override
    public void configure(Map<String, ?> map, boolean b) {


    }

    @Override
    public void close() {
        tuple2Serializer.close();
        tuple2Deserializer.close();

    }

    @Override
    public Serializer<Tuple2<T1, T2>> serializer() {
        return tuple2Serializer;
    }

    @Override
    public Deserializer<Tuple2<T1, T2>> deserializer() {
        return tuple2Deserializer;
    }
}
