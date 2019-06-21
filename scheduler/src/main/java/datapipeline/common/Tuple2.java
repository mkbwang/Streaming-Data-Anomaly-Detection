package datapipeline.common;

public class Tuple2<T1, T2> {
    public T1 value1;
    public T2 value2;

    public Tuple2(T1 v1, T2 v2) {
        value1 = v1;
        value2 = v2;
    }
}
