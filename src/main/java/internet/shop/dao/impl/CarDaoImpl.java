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
            throw new DataProcessingException("Can't create the data - "
                    + car + " into cars", exception);
        }
        return car;
    }

    @Override
    public Optional<Car> get(Long id) {
        Optional<Car> optionalCar;
        String queryCreateCarAndManufacturer = "select cars.id "
                + "as c_id, cars.manufacturer_id as c_mid, "
                + "cars.model as c_m, manufacturers.id as m_id, manufacturers.name as m_n, "
                + "manufacturers.country as m_c from cars "
                + "inner join manufacturers on manufacturers.id = cars.manufacturer_id "
                + "where cars.deleted = false and manufacturers.deleted = false and cars.id = ?;";
        String queryCreateDriver = "select cars.id as c_id, drivers.id as d_id, "
                + "drivers.name as d_n, drivers.license_number as d_ln from cars "
                + "inner join cars_drivers on cars_drivers.car_id = cars.id "
                + "inner join drivers on drivers.id = cars_drivers.driver_id "
                + "where cars.id = ? and drivers.deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement stmtCarManufacturer = connection
                        .prepareStatement(queryCreateCarAndManufacturer);
                PreparedStatement stmtDriver = connection
                        .prepareStatement(queryCreateDriver)) {
            stmtCarManufacturer.setLong(1, id);
            stmtDriver.setLong(1, id);

            ResultSet resultSetCar = stmtCarManufacturer.executeQuery();
            ResultSet resultSetDriver = stmtDriver.executeQuery();
            Car car = new Car();
            if (resultSetCar.next()) {
                car = createCar(resultSetCar);
                Manufacturer manufacturer = createManufacturer(resultSetCar);
                car.setManufacturer(manufacturer);
            }
            while (resultSetDriver.next()) {
                Driver driver = creteDriver(resultSetDriver);
                car.setDrivers(driver);
            }
            optionalCar = Optional.of(car);
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
                carList.add(getDriversByIdCars(connection,
                        resultSet.getObject("id", Long.class)));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get all data from DBs", exception);
        }
        return carList;
    }

    @Override
    public Car update(Car car) {
        String query = "UPDATE cars set cars.model = ? where cars.id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection
                            .prepareStatement(query)) {
            preparedStatement.setString(1, car.getModel());
            preparedStatement.setLong(2, car.getId());

            removeCarsDrivers(connection, car);
            insertCarsDrivers(connection, car);
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
            throw new DataProcessingException("Can't delete the data by id: "
                    + id + " into cars", exception);
        }
    }

    public List<Car> getAllByDriver(Long driverId) {
        List<Car> carList = new ArrayList<>();
        String query = "SELECT id from drivers where id = ? and deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, driverId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                carList.addAll(getCarsByIdDrivers(connection,
                        resultSet.getObject("id", Long.class)));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't find drivers by id: " + driverId, exception);
        }
        return carList;
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

    private void removeCarsDrivers(Connection connection, Car car) {
        String queryRemove = "delete from cars_drivers where car_id = ?;";

        try (PreparedStatement removeStmt = connection
                .prepareStatement(queryRemove)) {
            removeStmt.setLong(1, car.getId());
            removeStmt.executeUpdate();
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't remove data of "
                    + car + " into cars_drivers", exception);
        }
    }

    private void insertCarsDrivers(Connection connection, Car car) {
        String queryInsert = "INSERT INTO cars_drivers (driver_id, car_id) VALUES (?, ?);";

        try (PreparedStatement insertStmt = connection
                     .prepareStatement(queryInsert)) {
            Iterator<Driver> iterator = car.getDrivers().iterator();
            while (iterator.hasNext()) {
                insertStmt.setLong(1, iterator.next().getId());
                insertStmt.setLong(2, car.getId());
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't insert data of "
                    + car + " into cars_drivers", exception);
        }
    }

    private Car createCar(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setId(resultSet.getObject("c_id", Long.class));
        car.setModel(resultSet.getObject("c_m", String.class));
        return car;
    }

    private Manufacturer createManufacturer(ResultSet resultSet) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getObject("m_id", Long.class));
        manufacturer.setName(resultSet.getObject("m_n", String.class));
        manufacturer.setCountry(resultSet.getObject("m_n", String.class));
        return manufacturer;
    }

    private Driver creteDriver(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getObject("d_id", Long.class));
        driver.setName(resultSet.getObject("d_n", String.class));
        driver.setLicenceNumber(resultSet.getObject("d_ln", String.class));
        return driver;
    }

    private Car getDriversByIdCars(Connection connection, Long id) {
        String query = "select cars.id as c_id, cars.manufacturer_id as c_mid, "
                + "cars.model as c_m, drivers.id as d_id, "
                + "drivers.name as d_n, drivers.license_number as d_ln, "
                + "manufacturers.id as m_id, manufacturers.name as m_n, "
                + "manufacturers.country as m_c from cars "
                + "inner join cars_drivers on cars_drivers.car_id = cars.id "
                + "inner join drivers on drivers.id = cars_drivers.driver_id "
                + "inner join manufacturers on manufacturers.id = cars.manufacturer_id "
                + "where cars.id = ?;";
        Car car = new Car();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Manufacturer manufacturer = createManufacturer(resultSet);
                Driver driver = creteDriver(resultSet);
                car = createCar(resultSet);

                car.setDrivers(driver);
                car.setManufacturer(manufacturer);
            }
            while (resultSet.next()) {
                Driver driver = creteDriver(resultSet);
                car.setDrivers(driver);
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get drivers by car", exception);
        }
        return car;
    }

    private List<Car> getCarsByIdDrivers(Connection connection, Long id) {
        String query = "select cars.id as c_id, cars.model "
                + "as c_m, drivers.id as d_id, drivers.name d_n, "
                + "drivers.license_number as d_ln, manufacturers.id "
                + "as m_id, manufacturers.name as m_n, "
                + "manufacturers.country as m_c from cars "
                + "inner join cars_drivers on cars.id = cars_drivers.car_id "
                + "inner join drivers on drivers.id = cars_drivers.driver_id "
                + "inner join manufacturers on manufacturers.id = cars.manufacturer_id "
                + "where drivers.id = ?;";

        List<Car> carList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car car = createCar(resultSet);
                Driver driver = creteDriver(resultSet);
                Manufacturer manufacturer = createManufacturer(resultSet);
                car.setManufacturer(manufacturer);
                car.setDrivers(driver);

                carList.add(car);
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get cars by driver", exception);
        }
        return carList;
    }
}
