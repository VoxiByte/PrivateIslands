package it.voxibyte.privateislands.island;

import it.voxibyte.privateislands.database.MysqlDatabase;
import it.voxibyte.privateislands.exception.PersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandRepository {
    private final MysqlDatabase mysqlDatabase;

    public IslandRepository(final MysqlDatabase mysqlDatabase) {
        this.mysqlDatabase = mysqlDatabase;
        createTable();
    }

    public boolean saveIsland(Island island) {
        try(Connection connection = mysqlDatabase.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into islands(owner, world) values (?, ?)");
            preparedStatement.setString(1, island.getOwnerUid().toString());
            preparedStatement.setString(2, island.getWorldUid().toString());

            int result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result == 1;
        } catch (SQLException exception) {
            throw new PersistenceException("something went wrong saving island " + island.getOwnerUid().toString(), exception);
        }
    }

    public boolean deleteIsland(Island island) {
        try(Connection connection = mysqlDatabase.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from islands where owner = ?");
            preparedStatement.setString(1, island.getOwnerUid().toString());

            int result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result == 1;
        } catch (SQLException exception) {
            throw new PersistenceException("something went wrong updating island " + island.getOwnerUid().toString(), exception);
        }
    }

    public List<Island> loadIslands() {
        List<Island> loadedIslands = new ArrayList<>();
        try(Connection connection = mysqlDatabase.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from islands");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UUID ownerUid = UUID.fromString(resultSet.getString(1));
                UUID worldUid = UUID.fromString(resultSet.getString(2));
                Island island = new Island(ownerUid, worldUid);

                loadedIslands.add(island);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException exception) {
            throw new PersistenceException("something went wrong retrieving islands from database", exception);
        }

        return loadedIslands;
    }

    private void createTable() {
        try(Connection connection = mysqlDatabase.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists islands(owner VARCHAR(255), world VARCHAR(255))");

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException exception) {
            throw new PersistenceException("something went wrong creating islands table", exception);
        }
    }
}
