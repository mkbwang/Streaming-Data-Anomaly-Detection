package datapipeline;

public class JobScheduler {

    public static JobScheduler instance;

    private JobScheduler() {};

    public static JobScheduler getInstance()
    {
        if (instance == null) {

            // double lock checking for thread safe
            synchronized (JobScheduler.class) {
                if (instance == null) {
                    instance = new JobScheduler();
                }
            }
        }
        return instance;
    }


    public static void start() {

    }
}
