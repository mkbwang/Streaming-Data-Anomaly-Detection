package datapipeline.data;

public class ValueWithID<T1, T2> {
    private T1 _value;
    private T2 _id;

    ValueWithID(T1 value, T2 id) {
        _value = value;
        _id = id;
    }
    

    public T1 get_value() {
        return _value;
    }

    public T2 get_id() {
        return _id;
    }

    public void set_id(T2 _id) {
        this._id = _id;
    }

    public void set_value(T1 _value) {
        this._value = _value;
    }

    @Override
    public String toString() {
        return _id.toString() + ";" + _value.toString();
    }
}
