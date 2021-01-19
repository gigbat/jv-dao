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
        String query = "INSERT INTO drivers (name, license_number) "
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
            throw new DataProcessingException("Can't create the data - "
                    + driver + " into drivers", exception);
        }
        return driver;
    }

    @Override
    public Optional<Driver> get(Long id) {
        Optional<Driver> optionalDriver = Optional.empty();
        String query = "SELECT * FROM drivers where id = ? and deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                optionalDriver = Optional.of(createDriver(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get the data by id: "
                    + id + " into drivers", exception);
        }
        return optionalDriver;
    }

    @Override
    public List<Driver> getAll() {
        List<Driver> driverList = new ArrayList<>();
        String query = "SELECT * FROM drivers where deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                driverList.add(createDriver(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get all data from drivers DB", exception);
        }
        return driverList;
    }

    @Override
    public Driver update(Driver driver) {
        String query = "UPDATE drivers SET"
                + " name = ?, license_number = ?"
                + " where deleted = false and id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, driver.getName());
            preparedStatement.setString(2, driver.getLicenceNumber());
            preparedStatement.setLong(3, driver.getId());

            if (preparedStatement.executeUpdate() > 0) {
                return driver;
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't update the data - "
                    + driver + " into drivers", exception);
        }
        throw new RuntimeException("No driver " + driver + " to update in drivers DB");
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE drivers SET deleted = true "
                + "where id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't delete the data by id: "
                    + id + " into drivers", exception);
        }
    }

    private Driver createDriver(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getObject(1, Long.class));
        driver.setName(resultSet.getObject(2, String.class));
        driver.setLicenceNumber(resultSet.getObject(3, String.class));
        return driver;
    }
}
