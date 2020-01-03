package datapipeline.data;

public class AnomalyDetectionJob extends JobBaseModel {

    private int _window_size;
    private String _source_topic;
    private int _threshold;

    public AnomalyDetectionJob(JobBaseModel base, int window_size, String source_topic, int threshold) {
        this._ID = base._ID;
        this._dest_topic = base._dest_topic;
        this._window_size = window_size;
        this._source_topic = source_topic;
        this._threshold = threshold;
    }


    public void set_window_size(int _window_size) {
        this._window_size = _window_size;
    }

    public void set_source_topic(String _source_topic) {
        this._source_topic = _source_topic;
    }

    public void set_threshold(int _threshold) {
        this._threshold = _threshold;
    }

    public String get_source_topic() {
        return _source_topic;
    }

    public int get_window_size() {
        return _window_size;
    }

    public int get_threshold() {
        return _threshold;
    }
}
