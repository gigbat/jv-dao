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
import java.util.Iterator;
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
            throw new DataProcessingException("Can't create the data - " + car, exception);
        }
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        Optional<Car> optionalCar;
        String query = "select cars.manufacturer_id as c_mid, cars.id as c_id, cars.model"
                + " as c_md, cars.manufacturer_id as c_mn, drivers.name as d_n, "
                + " drivers.license_number as d_ln, manufacturers.name as m_n, "
                + " manufacturers.country as m_c, drivers.id as d_id from cars "
                + " inner join cars_drivers on cars_drivers.car_id = cars.id"
                + " inner join drivers on drivers.id = cars_drivers.driver_id"
                + " inner join manufacturers on cars.manufacturer_id = manufacturers.id "
                + " where cars.id = ? and cars.deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
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
        String query = "SELECT id from cars where deleted = false";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Optional<Car> optionalCar = get(resultSet.getObject("id", Long.class));
                optionalCar.ifPresent(carList::add);
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get all data from DB", exception);
        }
        return carList;
    }

    @Override
    public Car update(Car car) {
        String queryForUpdateInCars = "update cars set model = ? where id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateStmtCars = connection
                            .prepareStatement(queryForUpdateInCars)) {
            updateStmtCars.setString(1, car.getModel());
            updateStmtCars.setLong(2, car.getId());

            deleteDataFromCarsDrivers(connection, car);
            updateStmtCars.executeUpdate();
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't update the data - "
                    + car + "in table of cars or cars_drivers", exception);
        }
        createCarsDriversDB(car);
        return car;
    }

    @Override
    public boolean delete(Long id) {
        String queryForCars = "UPDATE cars SET deleted = true where id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statementForCars = connection.prepareStatement(queryForCars)) {
            statementForCars.setLong(1, id);
            return statementForCars.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't delete the data by id: " + id, exception);
        }
    }

    public List<Car> getAllByDriver(Long driverId) {
        List<Car> carList = new ArrayList<>();
        String query = "select cars.id as c_id, cars.model "
                + " as c_m, drivers.id as d_id, drivers.name d_n,"
                + " drivers.license_number as d_ln, manufacturers.id "
                + " as m_id, manufacturers.name as m_n,"
                + " manufacturers.country as m_c from cars"
                + " inner join cars_drivers on cars.id = cars_drivers.car_id"
                + " inner join drivers on drivers.id = cars_drivers.driver_id"
                + " inner join manufacturers on manufacturers.id = cars.manufacturer_id"
                + " where drivers.id = ?";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                carList.add(getCarsFromDB(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't find drivers by id: " + driverId, exception);
        }
        return carList;
    }

    private Car getCarFromDB(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        Manufacturer manufacturer = new Manufacturer();
        resultSet.beforeFirst();
        if (resultSet.next()) {
            manufacturer.setId(resultSet.getObject("c_mid", Long.class));
            manufacturer.setName(resultSet.getObject("m_n", String.class));
            manufacturer.setCountry(resultSet.getObject("m_c", String.class));
            car.setId(resultSet.getObject("c_id", Long.class));
            car.setModel(resultSet.getObject("c_md", String.class));
            car.setManufacturer(manufacturer);
        }
        while (resultSet.next()) {
            Driver driver = new Driver();
            driver.setId(resultSet.getObject("d_id", Long.class));
            driver.setName(resultSet.getObject("d_n", String.class));
            driver.setLicenceNumber(resultSet.getObject("d_ln", String.class));
            car.setDrivers(driver);
        }
        return car;
    }

    private void createCarsDriversDB(Car car) {
        String queryForCarsDrivers = "INSERT INTO cars_drivers (driver_id, car_id) values (?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatementOfCarsDrivers = connection
                        .prepareStatement(queryForCarsDrivers)) {
            Iterator<Driver> iterator = car.getDrivers().iterator();
            while (iterator.hasNext()) {
                preparedStatementOfCarsDrivers.setLong(1, iterator.next().getId());
                preparedStatementOfCarsDrivers.setLong(2, car.getId());
                preparedStatementOfCarsDrivers.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't create data "
                    + car + " into cars_drivers table", exception);
        }
    }

    private void deleteDataFromCarsDrivers(Connection connection, Car car) {
        String queryForDeleteInCarsDrivers = "delete from cars_drivers where driver_id = ?;";
        try (PreparedStatement deleteStmtCarsDrivers = connection
                .prepareStatement(queryForDeleteInCarsDrivers)) {
            deleteStmtCarsDrivers.setLong(1, car.getId());
            deleteStmtCarsDrivers.executeUpdate();
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't delete data of " + car, exception);
        }
    }

    private Car getCarsFromDB(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        long prevId = 0;
        long id = resultSet.getObject("id", Long.class);
        Driver driver = new Driver();
        Manufacturer manufacturer = new Manufacturer();
        if (id == prevId) {
            driver.setId(resultSet.getObject("d_id", Long.class));
            driver.setName(resultSet.getObject("d_n", String.class));
            driver.setLicenceNumber(resultSet.getObject("d_ln", String.class));
            car.setDrivers(driver);
        } else {
            driver.setId(resultSet.getObject("d_id", Long.class));
            driver.setName(resultSet.getObject("d_n", String.class));
            driver.setLicenceNumber(resultSet.getObject("d_ln", String.class));
            car.setDrivers(driver);

            manufacturer.setId(resultSet.getObject("m_id", Long.class));
            manufacturer.setName(resultSet.getObject("m_n", String.class));
            manufacturer.setCountry(resultSet.getObject("m_c", String.class));
            car.setManufacturer(manufacturer);

            car.setId(resultSet.getObject("c_id", Long.class));
            car.setModel(resultSet.getObject("c_m", String.class));
        }
        return car;
    }
}
