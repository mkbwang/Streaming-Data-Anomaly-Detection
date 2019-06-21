package datapipeline.api;

import java.util.concurrent.atomic.AtomicInteger;

import datapipeline.common.JobResponse;
import datapipeline.streamer.MatrixThresholdLamda;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    private final AtomicInteger counter = new AtomicInteger();


    @RequestMapping(value = "api_v1/job", method = RequestMethod.POST)
    public JobResponse StartJobController() {
        Thread streamer = new Thread(new MatrixThresholdLamda());
        streamer.start();
        return new JobResponse(0, counter.incrementAndGet());
    }
}
