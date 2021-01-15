package internet.shop.dao.impl;

import internet.shop.dao.ManufacturerDao;
import internet.shop.exception.DataProcessingException;
import internet.shop.lib.Dao;
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
public class ManufacturerDaoImpl implements ManufacturerDao {
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        String query = "INSERT INTO manufacturers (manufacturer_name, "
                + "manufacturer_country) values (?, ?);";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, manufacturer.getName());
            preparedStatement.setString(2, manufacturer.getCountry());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                manufacturer.setId(generatedKeys.getObject(1, Long.class));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't create the data - " + manufacturer, exception);
        }
        return manufacturer;
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        Optional<Manufacturer> optionalManufacturer = Optional.empty();
        String query = "SELECT * FROM manufacturers where "
                + "manufacturer_id = ? and manufacturer_deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                optionalManufacturer = Optional.of(setDataIntoManufacturer(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get the data by id: " + id, exception);
        }
        return optionalManufacturer;
    }

    @Override
    public List<Manufacturer> getAll() {
        List<Manufacturer> manufacturerList = new ArrayList<>();
        String query = "SELECT * FROM manufacturers where manufacturer_deleted = false;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                manufacturerList.add(setDataIntoManufacturer(resultSet));
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't get all data from DB", exception);
        }
        return manufacturerList;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        String query = "UPDATE manufacturers SET manufacturer_name = ?,"
                + " manufacturer_country = ? where manufacturer_id = ? "
                + "and manufacturer_deleted = false";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, manufacturer.getName());
            preparedStatement.setString(2, manufacturer.getCountry());
            preparedStatement.setLong(3, manufacturer.getId());

            if (preparedStatement.executeUpdate() > 0) {
                return manufacturer;
            }
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't update the data - " + manufacturer, exception);
        }
        throw new RuntimeException("No manufacturer " + manufacturer + " to update in DB");
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE manufacturers SET "
                + "manufacturer_deleted = true where manufacturer_id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new DataProcessingException("Can't delete the data by id: " + id, exception);
        }
    }

    private Manufacturer setDataIntoManufacturer(ResultSet resultSet) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getLong("manufacturer_id"));
        manufacturer.setName(resultSet.getString("manufacturer_name"));
        manufacturer.setCountry(resultSet.getString("manufacturer_country"));
        return manufacturer;
    }
}
