package my.repository;

import my.model.JonahomeHtmlModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JonahomeHtmlRepository extends JpaRepository<JonahomeHtmlModel, Long> {

    JonahomeHtmlModel findTopByWeek(int week);
}
