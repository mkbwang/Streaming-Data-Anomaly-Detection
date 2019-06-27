package datapipeline.api;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import datapipeline.data.JobResponse;
import datapipeline.streamer.MatrixThresholdLamda;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobController {
    private final AtomicInteger counter = new AtomicInteger();
    private final ObjectMapper mapper = new ObjectMapper();


    @RequestMapping(value = "api_v1/job", method = RequestMethod.POST)
    public JobResponse StartJobController(@RequestBody JsonNode request) {
        System.out.print(request.toString());
        System.out.print(request.get("threshold").asInt());

        //Thread streamer = new Thread(new MatrixThresholdLamda(counter.incrementAndGet()));
        //streamer.start();
        return new JobResponse(0, counter.get());
    }
}
