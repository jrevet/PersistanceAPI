/*
===============================================================
   _____                       ______
  / ___/____ _____ ___  ____ _/ ____/___ _____ ___  ___  _____
  \__ \/ __ `/ __ `__ \/ __ `/ / __/ __ `/ __ `__ \/ _ \/ ___/
 ___/ / /_/ / / / / / / /_/ / /_/ / /_/ / / / / / /  __(__  )
/____/\__,_/_/ /_/ /_/\__,_/\____/\__,_/_/ /_/ /_/\___/____/

===============================================================
  Persistance API
  Copyright (c) for SamaGames, all right reserved
  By MisterSatch, January 2016
===============================================================
*/

package net.samagames.persistanceapi.datamanager.aggregationmanager.statistics;

import net.samagames.persistanceapi.beans.players.PlayerBean;
import net.samagames.persistanceapi.beans.statistics.DimensionsStatisticsBean;
import net.samagames.persistanceapi.beans.statistics.LeaderboardBean;
import net.samagames.persistanceapi.utils.Transcoder;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class DimensionsStatisticsManager
{
    // Defines
    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultset = null;

    // Get dimensions player statistics
    public DimensionsStatisticsBean getDimensionsStatistics(PlayerBean player, DataSource dataSource) throws Exception
    {
        DimensionsStatisticsBean dimensionsStats = null;

        try
        {
            // Set connection
            connection = dataSource.getConnection();

            // Query construction
            String sql = "select HEX(uuid) as uuid, deaths, kills, played_games, wins, creation_date, update_date, played_time from dimensions_stats where uuid = UNHEX(?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, Transcoder.encode(player.getUuid().toString()));

            // Execute the query
            resultset = statement.executeQuery();

            // Manage the result in a bean
            if(resultset.next())
            {
                // There's a result
                String playerUuid = Transcoder.decode(resultset.getString("uuid"));
                UUID uuid = UUID.fromString(playerUuid);
                int deaths = resultset.getInt("deaths");
                int kills = resultset.getInt("kills");
                int playedGames = resultset.getInt("played_games");
                int wins = resultset.getInt("wins");
                Timestamp creationDate = resultset.getTimestamp("creation_date");
                Timestamp updateDate = resultset.getTimestamp("update_date");
                long playedTime = resultset.getLong("played_time");

                dimensionsStats = new DimensionsStatisticsBean(uuid, deaths, kills, playedGames, wins, creationDate, updateDate, playedTime);
            }
            else
            {
                // If there no dimensions stats int the database create empty one
                this.close();
                this.createEmptyDimensionsStatistics(player, dataSource);
                this.close();

                DimensionsStatisticsBean newDimensionsStats = this.getDimensionsStatistics(player,dataSource);
                this.close();

                return newDimensionsStats;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw exception;
        }
        finally
        {
            // Close the query environment in order to prevent leaks
            this.close();
        }

        return dimensionsStats;
    }

    // Create an empty dimensions statistics
    private void createEmptyDimensionsStatistics(PlayerBean player, DataSource dataSource) throws Exception
    {
        try
        {
            // Create an empty bean
            DimensionsStatisticsBean dimensionStats = new DimensionsStatisticsBean(player.getUuid(), 0, 0, 0, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0);

            // Set connection
            connection = dataSource.getConnection();

            // Query construction for create
            String sql = "insert into dimensions_stats (uuid, deaths, kills, played_games, wins, creation_date, update_date, played_time)";
            sql += " values (UNHEX(?), ?, ?, ?, ?, now(), now(), ?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, Transcoder.encode(player.getUuid().toString()));
            statement.setInt(2, dimensionStats.getDeaths());
            statement.setInt(3, dimensionStats.getKills());
            statement.setInt(4, dimensionStats.getPlayedGames());
            statement.setInt(5, dimensionStats.getWins());
            statement.setLong(6, dimensionStats.getPlayedTime());

            // Execute the query
            statement.executeUpdate();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            throw exception;
        }
        finally
        {
            // Close the query environment in order to prevent leaks
            this.close();
        }

    }


    // Update dimensions player statistics
    public void updateDimensionsStatistics(PlayerBean player, DimensionsStatisticsBean dimensionsStats, DataSource dataSource) throws Exception
    {
        try
        {
            // Check if a record exists
            if (this.getDimensionsStatistics(player, dataSource) == null)
            {
                // Create an empty dimensions statistics
                this.createEmptyDimensionsStatistics(player, dataSource);
            }
            else
            {
                // Set connection
                connection = dataSource.getConnection();

                // Query construction for update
                String sql = "update dimensions_stats set deaths = ?, kills = ?, played_games = ?, wins = ?, update_date = now(), played_time = ? where uuid = UNHEX(?)";

                statement = connection.prepareStatement(sql);
                statement.setInt(1, dimensionsStats.getDeaths());
                statement.setInt(2, dimensionsStats.getKills());
                statement.setInt(3, dimensionsStats.getPlayedGames());
                statement.setInt(4, dimensionsStats.getWins());
                statement.setLong(5, dimensionsStats.getPlayedTime());
                statement.setString(6, Transcoder.encode(player.getUuid().toString()));

                // Execute the query
                statement.executeUpdate();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw exception;
        }
        finally
        {
            // Close the query environment in order to prevent leaks
            this.close();
        }
    }

    // Get the board for this game
    public List<LeaderboardBean> getLeaderBoard(String category, DataSource dataSource) throws Exception
    {
        List<LeaderboardBean> leaderBoard = new ArrayList<>();
        try
        {
            // Set connection
            connection = dataSource.getConnection();

            // Query construction
            String sql = String.format("select p.name as name, d.%1$s as score from players as p, dimensions_stats as d where p.uuid = d.uuid order by d.%2$s desc limit 3", category);

            statement = connection.prepareStatement(sql);

            // Execute the query
            resultset = statement.executeQuery();

            // Manage the result in a bean
            while(resultset.next())
            {
                LeaderboardBean bean = new LeaderboardBean(resultset.getString("name"), resultset.getInt("score"));
                leaderBoard.add(bean);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw exception;
        }
        finally
        {
            // Close the query environment in order to prevent leaks
            this.close();
        }
        return leaderBoard;
    }

    // Close all connection
    public void close() throws Exception
    {
        // Close the query environment in order to prevent leaks
        try
        {
            if (resultset != null)
            {
                // Close the resulset
                resultset.close();
            }
            if (statement != null)
            {
                // Close the statement
                statement.close();
            }
            if (connection != null)
            {
                // Close the connection
                connection.close();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw exception;
        }
    }
}
