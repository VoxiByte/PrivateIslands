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
    }

    public boolean saveIsland(Island island) {
        try(Connection connection = mysqlDatabase.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into islands(owner, world) values (?, ?)");
            preparedStatement.setString(1, island.getOwnerUid().toString());
            preparedStatement.setString(2, island.getWorldUid().toString());

            int result = preparedStatement.executeUpdate();
            return result == 1;
        } catch (SQLException exception) {
            throw new PersistenceException("something went wrong saving island " + island.getOwnerUid().toString(), exception);
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
        } catch (SQLException exception) {
            throw new PersistenceException("something went wrong retrieving islands from database", exception);
        }

        return loadedIslands;
    }
}
