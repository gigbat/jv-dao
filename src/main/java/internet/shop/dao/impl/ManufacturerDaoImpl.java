package internet.shop.dao.impl;

import internet.shop.dao.ManufacturerDao;
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
            while (generatedKeys.next()) {
                manufacturer.setId(generatedKeys.getObject(1, Long.class));
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Can't connect to DB", exception);
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
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setId(resultSet.getLong("manufacturer_id"));
                manufacturer.setName(resultSet.getString("manufacturer_name"));
                manufacturer.setCountry(resultSet.getString("manufacturer_country"));
                optionalManufacturer = Optional.of(manufacturer);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Can't connect to DB", exception);
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
            if (resultSet.next()) {
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setId(resultSet.getLong("manufacturer_id"));
                manufacturer.setName(resultSet.getString("manufacturer_name"));
                manufacturer.setCountry(resultSet.getString("manufacturer_country"));
                manufacturerList.add(manufacturer);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Can't connect to DB", exception);
        }
        return manufacturerList;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        String query = "UPDATE manufacturers SET manufacturer_name = ?,"
                + " manufacturer_country = ? where manufacturer_id = ?";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, manufacturer.getName());
            preparedStatement.setString(2, manufacturer.getCountry());
            preparedStatement.setLong(3, manufacturer.getId());

            if (preparedStatement.executeUpdate() == 1) {
                return manufacturer;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Can't connect to DB", exception);
        }
        throw new RuntimeException("You can't update " + manufacturer + " in DB");
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE manufacturers SET "
                + "manufacturer_deleted = true where manufacturer_id = ?;";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException exception) {
            throw new RuntimeException("Can't connect to DB", exception);
        }
    }
}
