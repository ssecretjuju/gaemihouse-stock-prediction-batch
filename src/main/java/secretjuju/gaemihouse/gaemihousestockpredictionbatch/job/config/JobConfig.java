package secretjuju.gaemihouse.gaemihousestockpredictionbatch.job.config;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import secretjuju.gaemihouse.gaemihousestockpredictionbatch.chatting_log.model.StockPrediction;
import secretjuju.gaemihouse.gaemihousestockpredictionbatch.chatting_log.service.StockPredictionService;

import java.util.List;

@Configuration
public class JobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final StockPredictionService stockPredictionService;

    public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,  StockPredictionService stockPredictionService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.stockPredictionService = stockPredictionService;
    }

    private final String BATCH_NAME = "CHATTING_LOG_SERVING_JOB";
    private List<StockPrediction> stockPredictions;

    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get(BATCH_NAME)
                .start(firstStep())
                .next(secondStep())
                .build();
    }

    @Bean
    public Step firstStep() {

        return stepBuilderFactory.get(BATCH_NAME + "secondStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("1. AI 모델에 POST 요청");
                    String url = "http://localhost:8080/test";

                    try {
                        HttpClient client = HttpClientBuilder.create().build();
                        HttpGet getRequest = new HttpGet(url);

                        HttpResponse response = client.execute(getRequest);

                        if (response.getStatusLine().getStatusCode() == 200) {
                            ResponseHandler<String> handler = new BasicResponseHandler();
                            String body = handler.handleResponse(response);

//                            stockPredictions = body 데이터 가공하기

                        } else {
                            System.out.println("응답 코드 : " + response.getStatusLine().getStatusCode());
                        }

                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean
    public Step secondStep() {
        return stepBuilderFactory.get(BATCH_NAME + "thirdStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("2. 응답 데이터 Oracle DB에 저장");
                    stockPredictionService.save(stockPredictions);
                    return RepeatStatus.FINISHED;
                })
                .build();

    }
}
