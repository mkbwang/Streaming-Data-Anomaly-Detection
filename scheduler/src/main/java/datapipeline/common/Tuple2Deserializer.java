package datapipeline.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.Charset;
import java.util.Map;

public class Tuple2Deserializer<T1, T2> implements Deserializer<Tuple2<T1, T2>> {
    private static final Charset CHARSET;
    private Gson gson = new GsonBuilder().create();

    static {
        CHARSET = Charset.forName("UTF-8");
    }

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public Tuple2<T1, T2> deserialize(String s, byte[] bytes) {
        try {
            // Transform the bytes to String
            String tuple = new String(bytes, CHARSET);
            // Return the Person object created from the String 'person'
            return (Tuple2<T1, T2>) gson.fromJson(tuple, Tuple2.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading bytes! Yanlış", e);
        }
    }

    @Override
    public void close() {

    }
}
