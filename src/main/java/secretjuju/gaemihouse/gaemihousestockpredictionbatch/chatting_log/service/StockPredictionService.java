package secretjuju.gaemihouse.gaemihousestockpredictionbatch.chatting_log.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secretjuju.gaemihouse.gaemihousestockpredictionbatch.chatting_log.model.StockPrediction;
import secretjuju.gaemihouse.gaemihousestockpredictionbatch.chatting_log.repository.StockPredictionReopository;

import java.util.List;

@Service
public class StockPredictionService {

    private final StockPredictionReopository stockPredictionReopository;

    public StockPredictionService(StockPredictionReopository stockPredictionReopository) {
        this.stockPredictionReopository = stockPredictionReopository;
    }

    @Transactional
    public void save(List<StockPrediction> stockPredictions) {
        stockPredictionReopository.saveAll(stockPredictions);
    }

}
