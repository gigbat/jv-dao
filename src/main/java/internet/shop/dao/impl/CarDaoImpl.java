package internet.shop.dao.impl;

import internet.shop.dao.CarDao;
import internet.shop.dao.DriverDao;
import internet.shop.dao.ManufacturerDao;
import internet.shop.exception.DataProcessingException;
import internet.shop.lib.Dao;
import internet.shop.model.Car;
import internet.shop.model.Driver;
import internet.shop.model.Manufacturer;
import internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Dao
public class CarDaoImpl implements CarDao {
    @Override
    public Car create(Car car) {
        String queryForCar = "INSERT INTO cars (manufacturer_id, car_model) values (?, ?);";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatementOfCar = connection.prepareStatement(queryForCar,
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatementOfCar.setLong(1, car.getManufacturer().getId());
            preparedStatementOfCar.setString(2, car.getModel());
            preparedStatementOfCar.executeUpdate();

            ResultSet generatedKeys = preparedStatementOfCar.getGeneratedKeys();
            if (generatedKeys.next()) {
                car.setId(generatedKeys.getObject(1, Long.class));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't create the data - " + car, exception);
        }
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        Optional<Car> optionalCar;
        String query = "select c.car_id as c_cid, c.manufacturer_id as c_mid, c.car_model as c_cm,"
                + " d.driver_id as d_did, d.driver_name as d_dn, d.driver_license_number as d_dln"
                + " from cars c"
                + " inner join cars_drivers cd on c.car_id = cd.car_id"
                + " inner join drivers d on d.driver_id = cd.driver_id"
                + " where c.car_id = ? and c.car_deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            optionalCar = Optional.of(getCarFromDB(resultSet));
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get the data by id: " + id, exception);
        }
        return optionalCar;
    }

    @Override
    public List<Car> getAll() {
        List<Car> carList = new ArrayList<>();
        String query = "SELECT car_id from cars where car_deleted = false";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Car> optionalCar = get(resultSet.getObject("car_id", Long.class));
                optionalCar.ifPresent(carList::add);
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get all data from DB", exception);
        }
        return carList;
    }

    @Override
    public Car update(Car car) {
        String queryForDeleteInCarsDrivers = "delete from cars_drivers where car_id = ?;";
        String queryForUpdateInCars = "update cars set car_deleted = true where car_id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteStmtCarsDrivers = connection
                        .prepareStatement(queryForDeleteInCarsDrivers);
                PreparedStatement updateStmtCars = connection
                        .prepareStatement(queryForUpdateInCars)) {
            deleteStmtCarsDrivers.setLong(1, car.getId());
            updateStmtCars.setLong(1, car.getId());
            deleteStmtCarsDrivers.executeUpdate();
            updateStmtCars.executeUpdate();
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't update the data - "
                    + car + "in table of cars or cars_drivers", exception);
        }
        create(car);
        createCarsDriversDB(car);
        return car;
    }

    @Override
    public boolean delete(Long id) {
        String queryForCars = "UPDATE cars SET car_deleted = true where car_id = ?;";
        String queryForCarsDrivers = "delete from cars_drivers where car_id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statementForCars = connection.prepareStatement(queryForCars);
                PreparedStatement statementForCarsDrivers = connection
                        .prepareStatement(queryForCarsDrivers)) {
            statementForCars.setLong(1, id);
            statementForCarsDrivers.setLong(1, id);
            return statementForCars.executeUpdate() > 0
                    && statementForCarsDrivers.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't delete the data by id: " + id, exception);
        }
    }

    public List<Car> getAllByDriver(Long driverId) {
        List<Car> carList = new ArrayList<>();
        String query = "select distinct(c.car_id) from cars_drivers cd"
                + " inner join cars c on cd.car_id = c.car_id "
                + " where cd.driver_id = ? and c.car_deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Car> optionalCar = get(resultSet.getObject(1, Long.class));
                optionalCar.ifPresent(carList::add);
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't find drivers by id: " + driverId, exception);
        }
        return carList;
    }

    private Car getCarFromDB(ResultSet resultSet) throws SQLException {
        ManufacturerDao manufacturerDao = new ManufacturerDaoImpl();
        DriverDao driverDao = new DriverDaoImpl();
        Car car = new Car();

        if (resultSet.next()) {
            Optional<Manufacturer> optionalManufacturer = manufacturerDao
                    .get(resultSet.getObject(2, Long.class));
            optionalManufacturer.ifPresent(car::setManufacturer);
            car.setId(resultSet.getObject(1, Long.class));
            car.setModel(resultSet.getObject(3, String.class));
        }
        while (resultSet.next()) {
            Optional<Driver> optionalDriver = driverDao.get(resultSet.getObject(4, Long.class));
            optionalDriver.ifPresent(car::setDrivers);
        }
        return car;
    }

    private void createCarsDriversDB(Car car) {
        String queryForCarsDrivers = "INSERT INTO cars_drivers (driver_id, car_id) values (?, ?);";
        Iterator<Driver> iterator = car.getDrivers().iterator();
        while (iterator.hasNext()) {
            try (Connection connection = ConnectionUtil.getConnection();
                    PreparedStatement preparedStatementOfCarsDrivers = connection
                            .prepareStatement(queryForCarsDrivers)) {
                preparedStatementOfCarsDrivers.setLong(1, iterator.next().getId());
                preparedStatementOfCarsDrivers.setLong(2, car.getId());
                preparedStatementOfCarsDrivers.executeUpdate();
            } catch (SQLException exception) {
                throw new DataProcessingException("Can't create data "
                        + car + " into cars_drivers table", exception);
            }
        }
    }
}
