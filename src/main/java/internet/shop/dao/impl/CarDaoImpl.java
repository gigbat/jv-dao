package internet.shop.dao.impl;

import internet.shop.dao.CarDao;
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
import java.util.List;
import java.util.Optional;

@Dao
public class CarDaoImpl implements CarDao {
    @Override
    public Car create(Car car) {
        String queryForCar = "INSERT INTO cars (manufacturer_id, model) values (?, ?);";

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
            throw new DataProcessingException("Can't create the data - "
                    + car + " into cars", exception);
        }
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        String queryCreateCarAndManufacturer = "select c.id as car_id, c.model "
                + "as car_model, m.id as manufacturer_id, m.name as manufacturer_name, "
                + "m.country as manufacturer_country from cars c "
                + "inner join manufacturers m on m.id = c.manufacturer_id "
                + "where c.deleted = false and m.deleted = false and c.id = ?;";

        Car car = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement stmtCarManufacturer = connection
                         .prepareStatement(queryCreateCarAndManufacturer)) {
            stmtCarManufacturer.setLong(1, id);
            ResultSet resultSetCar = stmtCarManufacturer.executeQuery();
            if (resultSetCar.next()) {
                car = createCar(resultSetCar);
                Manufacturer manufacturer = createManufacturer(resultSetCar);
                car.setManufacturer(manufacturer);
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get the data by id: " + id, exception);
        }
        if (car != null) {
            List<Driver> drivers = getDriversByCarId(car.getId());
            for (Driver driver: drivers) {
                car.setDrivers(driver);
            }
        }
        return Optional.ofNullable(car);
    }

    private List<Driver> getDriversByCarId(Long id) {
        String queryCreateDriver = "select d.id as driver_id, "
                + "d.name as driver_name, d.license_number as driver_ln from drivers d "
                + "inner join cars_drivers cd on cd.driver_id = d.id "
                + "where cd.car_id = ? and d.deleted = false;";

        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement stSelectDrivers = connection
                        .prepareStatement(queryCreateDriver)) {
            stSelectDrivers.setLong(1, id);
            ResultSet resultSet = stSelectDrivers.executeQuery();
            while (resultSet.next()) {
                Driver driver = createDriver(resultSet);
                drivers.add(driver);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get drivers by id: " + id, e);
        }
        return drivers;
    }

    @Override
    public List<Car> getAll() {
        String query = "SELECT c.id, c.model, c.manufacturer_id, m.name, m.country "
                + "FROM cars c "
                + "INNER JOIN manufacturers m ON m.id=c.manufacturer_id "
                + "WHERE c.deleted=FALSE";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return getAllCarsFromResultSet(resultSet);
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get all data from DBs", exception);
        }
    }

    @Override
    public Car update(Car car) {
        String query = "UPDATE cars c set c.model = ? where c.id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(query)) {
            preparedStatement.setString(1, car.getModel());
            preparedStatement.setLong(2, car.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't update the data - "
                    + car + "in table of cars or cars_drivers", exception);
        }
        removeCarsDrivers(car);
        insertCarsDrivers(car);
        return car;
    }

    @Override
    public boolean delete(Long id) {
        String queryForCars = "UPDATE cars set model = ?, manufacturer_id = ?"
                + " where id = ? and deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statementForCars = connection.prepareStatement(queryForCars)) {
            statementForCars.setLong(1, id);
            return statementForCars.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't delete the data by id: "
                    + id + " into cars", exception);
        }
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String query = "SELECT c.id, c.model, c.manufacturer_id, m.name, m.country "
                + "FROM cars c "
                + "INNER JOIN cars_drivers cd ON cd.car_id = c.id "
                + "INNER JOIN manufacturers m ON m.id = c.manufacturer_id "
                + "INNER JOIN drivers d ON d.id = cd.driver_id "
                + "WHERE cd.driver_id = ? AND c.deleted = FALSE AND d.deleted = FALSE";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getAllCarsFromResultSet(resultSet);
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't find drivers by id: " + driverId, exception);
        }
    }

    private List<Car> getAllCarsFromResultSet(ResultSet resultSet) throws SQLException {
        List<Car> carList = new ArrayList<>();
        while (resultSet.next()) {
            Car car = new Car();
            Manufacturer manufacturer = new Manufacturer();

            car.setId(resultSet.getObject("id", Long.class));
            manufacturer.setId(resultSet.getObject("id", Long.class));

            car.setModel(resultSet.getObject("model", String.class));
            manufacturer.setName(resultSet.getObject("name", String.class));
            manufacturer.setCountry(resultSet.getObject("country", String.class));
            car.setManufacturer(manufacturer);
            List<Driver> drivers = getDriversByCarId(car.getId());
            for (Driver driver: drivers) {
                car.setDrivers(driver);
            }
            carList.add(car);
        }
        return carList;
    }

    private void removeCarsDrivers(Car car) {
        String queryRemove = "delete from cars_drivers cd where cd.car_id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement removeStmt = connection
                        .prepareStatement(queryRemove)) {
            removeStmt.setLong(1, car.getId());
            removeStmt.executeUpdate();
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't remove data of "
                    + car + " into cars_drivers", exception);
        }
    }

    private void insertCarsDrivers(Car car) {
        String queryInsert = "INSERT INTO cars_drivers (driver_id, car_id) VALUES (?, ?);";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement insertStmt = connection
                        .prepareStatement(queryInsert)) {
            for (Driver driver : car.getDrivers()) {
                insertStmt.setLong(1, driver.getId());
                insertStmt.setLong(2, car.getId());
                if (insertStmt.executeUpdate() <= 0) {
                    throw new RuntimeException("You have error when you put "
                            + "values into cars_drivers");
                }
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't insert data of "
                    + car + " into cars_drivers", exception);
        }
    }

    private Car createCar(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setId(resultSet.getObject("car_id", Long.class));
        car.setModel(resultSet.getObject("car_model", String.class));
        return car;
    }

    private Manufacturer createManufacturer(ResultSet resultSet) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getObject("manufacturer_id", Long.class));
        manufacturer.setName(resultSet.getObject("manufacturer_name", String.class));
        manufacturer.setCountry(resultSet.getObject("manufacturer_country", String.class));
        return manufacturer;
    }

    private Driver createDriver(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getObject("driver_id", Long.class));
        driver.setName(resultSet.getObject("driver_name", String.class));
        driver.setLicenceNumber(resultSet.getObject("driver_ln", String.class));
        return driver;
    }
}
