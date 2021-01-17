package internet.shop.dao.impl;

import internet.shop.dao.DriverDao;
import internet.shop.exception.DataProcessingException;
import internet.shop.lib.Dao;
import internet.shop.model.Driver;
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
public class DriverDaoImpl implements DriverDao {
    @Override
    public Driver create(Driver driver) {
        String query = "INSERT INTO drivers (driver_name, driver_license_number) "
                + "values (?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, driver.getName());
            preparedStatement.setString(2, driver.getLicenceNumber());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                driver.setId(generatedKeys.getObject(1, Long.class));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't create the data - " + driver, exception);
        }
        return driver;
    }

    @Override
    public Optional<Driver> get(Long id) {
        Optional<Driver> optionalDriver = Optional.empty();
        String query = "SELECT * FROM drivers where driver_id = ? and driver_deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            optionalDriver = Optional.of(setDataIntoDriver(resultSet));
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get the data by id: " + id, exception);
        }
        return optionalDriver;
    }

    @Override
    public List<Driver> getAll() {
        List<Driver> driverList = new ArrayList<>();
        String query = "SELECT * FROM drivers where driver_deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            driverList.add(setDataIntoDriver(resultSet));
            while (resultSet.next()) {
                driverList.add(setDataIntoDriver(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get all data from DB", exception);
        }
        return driverList;
    }

    @Override
    public Driver update(Driver driver) {
        String query = "UPDATE drivers SET "
                + "driver_name = ?, driver_license_number = ? "
                + "where driver_deleted = false and driver_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, driver.getName());
            preparedStatement.setString(2, driver.getLicenceNumber());
            preparedStatement.setLong(3, driver.getId());

            if (preparedStatement.executeUpdate() > 0) {
                return driver;
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't update the data - " + driver, exception);
        }
        throw new RuntimeException("No driver " + driver + " to update in DB");
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE drivers SET driver_deleted = true "
                + "where driver_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't delete the data by id: " + id, exception);
        }
    }

    private Driver setDataIntoDriver(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        if (resultSet.next()) {
            driver.setId(resultSet.getObject(1, Long.class));
            driver.setName(resultSet.getObject(2, String.class));
            driver.setLicenceNumber(resultSet.getObject(3, String.class));
        }
        return driver;
    }
}
