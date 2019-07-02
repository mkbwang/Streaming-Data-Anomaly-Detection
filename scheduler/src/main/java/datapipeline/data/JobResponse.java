package datapipeline.data;

public class JobResponse {

    private final int rvalue;
    private final int id;


    public JobResponse(int rvalue, int id) {
        this.rvalue = rvalue;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRvalue() {
        return rvalue;
    }
}
