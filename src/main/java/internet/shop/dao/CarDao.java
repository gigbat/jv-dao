package internet.shop.dao;

import internet.shop.model.Car;
import java.util.List;

public interface CarDao extends GenericDao<Car, Long> {
    List<Car> getAllByDriver(Long driverId);
}
