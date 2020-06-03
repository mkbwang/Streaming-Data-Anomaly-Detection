package datapipeline.data;

public class JobBaseModel {
    protected int _ID;
    protected String _dest_topic;

    public JobBaseModel(int ID, String dest_topic) {
        this._ID = ID;
        this._dest_topic = dest_topic;
    }

    public JobBaseModel(){}

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getDest_topic() {
        return _dest_topic;
    }

    public void setDest_topic(String dest_topic) {
        this._dest_topic = dest_topic;
    }
}
