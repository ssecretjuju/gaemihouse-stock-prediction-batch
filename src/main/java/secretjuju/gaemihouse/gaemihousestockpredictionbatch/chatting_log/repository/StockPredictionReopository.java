package secretjuju.gaemihouse.gaemihousestockpredictionbatch.chatting_log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import secretjuju.gaemihouse.gaemihousestockpredictionbatch.chatting_log.model.StockPrediction;

public interface StockPredictionReopository extends JpaRepository<StockPrediction, Integer> {
}
