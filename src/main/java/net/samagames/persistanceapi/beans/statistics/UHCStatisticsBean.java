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

package net.samagames.persistanceapi.beans.statistics;

import java.beans.ConstructorProperties;
import java.sql.Timestamp;
import java.util.UUID;

public class UHCStatisticsBean
{
    /* Database Structure

    Table: uhc_stats
    +---------------+------------+------+-----+---------------------+-------+
    | Field         | Type       | Null | Key | Default             | Extra |
    +---------------+------------+------+-----+---------------------+-------+
    | uuid          | binary(16) | NO   | PRI | NULL                |       |
    | damages       | int(11)    | NO   |     | NULL                |       |
    | deaths        | int(11)    | NO   |     | NULL                |       |
    | kills         | int(11)    | NO   |     | NULL                |       |
    | max_damages   | int(11)    | NO   |     | NULL                |       |
    | played_games  | int(11)    | NO   |     | NULL                |       |
    | wins          | int(11)    | NO   |     | NULL                |       |
    | creation_date | timestamp  | NO   |     | 0000-00-00 00:00:00 |       |
    | update_date   | timestamp  | NO   |     | 0000-00-00 00:00:00 |       |
    | played_time   | bigint(20) | NO   |     | NULL                |       |
    +---------------+------------+------+-----+---------------------+-------+
    */

    // Defines
    private UUID uuid;
    private int damages;
    private int deaths;
    private int kills;
    private int maxDamages;
    private int playedGames;
    private int wins;
    private Timestamp creationDate;
    private Timestamp updateDate;
    private long playedTime;

    // Constructor
    @ConstructorProperties({"uuid", "damages", "deaths", "kills", "maxDamages", "playedGames", "wins", "creationDate", "updateDate", "playedTime"})
    public UHCStatisticsBean(UUID uuid, int damages, int deaths, int kills, int maxDamages, int playedGames, int wins, Timestamp creationDate, Timestamp updateDate, long playedTime)
    {
        this.uuid = uuid;
        this.damages = damages;
        this.deaths = deaths;
        this.kills = kills;
        this.maxDamages = maxDamages;
        this.playedGames = playedGames;
        this.wins = wins;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.playedTime = playedTime;
    }

    // Getters
    public int getDamages()
    {
        return this.damages;
    }
    public int getDeaths()
    {
        return this.deaths;
    }
    public int getKills()
    {
        return this.kills;
    }
    public int getMaxDamages()
    {
        return this.maxDamages;
    }
    public int getPlayedGames()
    {
        return this.playedGames;
    }
    public int getWins()
    {
        return this.wins;
    }
    public Timestamp getCreationDate()
    {
        return this.creationDate;
    }
    public Timestamp getUpdateDate()
    {
        return this.updateDate;
    }
    public long getPlayedTime() { return playedTime; }

    // Setters
    public void setDamages(int damages)
    {
        this.damages = damages;
    }
    public void setDeaths(int deaths)
    {
        this.deaths = deaths;
    }
    public void setKills(int kills)
    {
        this.kills = kills;
    }
    public void setMaxDamages(int maxDamages)
    {
        this.maxDamages = maxDamages;
    }
    public void setPlayedGames(int playedGames)
    {
        this.playedGames = playedGames;
    }
    public void setWins(int wins)
    {
        this.wins = wins;
    }
    public void setCreationDate(Timestamp creationDate)
    {
        this.creationDate = creationDate;
    }
    public void setUpdateDate(Timestamp updateDate)
    {
        this.updateDate = updateDate;
    }
    public void setPlayedTime(long playedTime) { this.playedTime = playedTime; }
}