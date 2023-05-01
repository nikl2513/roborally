/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.dal;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
class Repository implements IRepository {

    private static final String GAME_GAMEID = "gameID";

    private static final String GAME_NAME = "name";

    private static final String GAME_CURRENTPLAYER = "currentPlayer";

    private static final String GAME_PHASE = "phase";

    private static final String GAME_STEP = "step";

    private static final String PLAYER_PLAYERID = "playerID";

    private static final String PLAYER_NAME = "name";

    private static final String PLAYER_COLOUR = "colour";

    private static final String PLAYER_GAMEID = "gameID";

    private static final String PLAYER_POSITION_X = "positionX";

    private static final String PLAYER_POSITION_Y = "positionY";

	private static final String PLAYER_CHECKPOINTVALU= "checkpointvalue";

	private static final String PLAYER_HP = "hp";

	private static final String PLAYER_HEADING = "heading";
	private static final String FIELD_GAMEID = "gameID";
	private static final String FIELD_PLAYERID = "playerID";
	private static final String FIELD_TYPE = "type";
	private static final int FIELD_TYPE_REGISTER = 0;
	private static final int FIELD_TYPE_HAND = 1;
	private static final String FIELD_POS = "position";
	private static final String FIELD_VISIBLE = "visible";
	private static final String FIELD_COMMAND = "command";

    private Connector connector;

    Repository(Connector connector) {
        this.connector = connector;
    }

    /**
     * @param game is the board that the game has been played on.
     * we need to use that to implement the information from the board to the databaseschema.
     * @param k is the board that has been chosen to play on.
     * this method creates the game in the database, with the board, amount of players and the cards the players has.
     * this method is only used to create the game in the database.
     * It then calls "createPlayersInDB" and "createCardfieldsInDB" to create the rest of the game in the database.
     * @return true if the games has been created in the database
     * @author s224549
     */
	@Override
	public boolean createGameInDB(Board game, int k) {

        if (game.getGameId() == null) {

            Connection connection = connector.getConnection();
            try {
                connection.setAutoCommit(false);

				PreparedStatement ps = getInsertGameStatementRGK();

				ps.setString(1, "Date: " + new Date()); // instead of name
				ps.setNull(2, Types.TINYINT); // game.getPlayerNumber(game.getCurrentPlayer())); is inserted after players!
				ps.setInt(3, game.getPhase().ordinal());
				ps.setInt(4, game.getStep());
				ps.setInt(5,k);

                // If you have a foreign key constraint for current players,
                // the check would need to be temporarily disabled, since
                // MySQL does not have a per transaction validation, but
                // validates on a per row basis.
                Statement statement = connection.createStatement();
                statement.execute("SET foreign_key_checks = 0");

                int affectedRows = ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (affectedRows == 1 && generatedKeys.next()) {
                    game.setGameId(generatedKeys.getInt(1));
                }
                generatedKeys.close();

                // Enable foreign key constraint check again:
                statement.execute("SET foreign_key_checks = 1");
                statement.close();

                createPlayersInDB(game);
				//TOODO this method needs to be implemented first
                //System.out.println("hej med dig");
				createCardFieldsInDB(game);
                //System.out.println("hej med mig");
                // since current player is a foreign key, it can oly be
                // inserted after the players are created, since MySQL does
                // not have a per transaction validation, but validates on
                // a per row basis.
                ps = getSelectGameStatementU();
                ps.setInt(1, game.getGameId());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
                    rs.updateRow();
                } else {

                }
                rs.close();

                connection.commit();
                connection.setAutoCommit(true);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Some DB error");

                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            System.err.println("Game cannot be created in DB, since it has a game id already!");
        }
        return false;
    }

    /**
     * @param game is the board that is used for the game with all the information on the players and so on.
     * This method updates the game in the database.
     * That means it takes the already create game in the database and updates it.
     * So it updates both the game, the players, and the cards
     * this method only updates the game.
     * Then it calls 2 methods "updatePlayersInDB" and "updateCardfieldsInDB" to update the rest.
     * @return true if the game has been updated
     * @author s224549
     */
    @Override
    public boolean updateGameInDB(Board game) {
        assert game.getGameId() != null;

        Connection connection = connector.getConnection();
        try {
            connection.setAutoCommit(false);

            PreparedStatement ps = getSelectGameStatementU();
            ps.setInt(1, game.getGameId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
                rs.updateInt(GAME_PHASE, game.getPhase().ordinal());
                rs.updateInt(GAME_STEP, game.getStep());
                rs.updateRow();
            } else {
            }
            rs.close();

            updatePlayersInDB(game);
			//TOODO this method needs to be implemented first
			updateCardFieldsInDB(game);

            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Some DB error");

            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return false;
    }

    /**
     * @param id is the game id the player chose to play again.
     * this method loads the game that has been chosen by the player. that means it returns a board.
     * @return a board if the game do exist.
     * @author s224549
     */
    @Override
    public Board loadGameFromDB(int id) {
        Board game;
        try {
            PreparedStatement ps = getSelectGameStatementU();
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            int playerNo = -1;
            if (rs.next()) {

				game = LoadBoard.loadBoard(rs.getInt("boardname"));
				if (game == null) {
					return null;
				}
				playerNo = rs.getInt(GAME_CURRENTPLAYER);
				game.setPhase(Phase.values()[rs.getInt(GAME_PHASE)]);
				game.setStep(rs.getInt(GAME_STEP));
			} else {
				return null;
			}
			rs.close();

            game.setGameId(id);
            loadPlayersFromDB(game);

            if (playerNo >= 0 && playerNo < game.getPlayersNumber()) {
                game.setCurrentPlayer(game.getPlayer(playerNo));
            } else {
                return null;
            }

			//TOODO this method needs to be implemented first
			loadCardFieldsFromDB(game);


            return game;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Some DB error");
        }
        return null;
    }

    /**
     * this method is used to show the games that is in the database so the player can choose between them.
     * @return the result that is list of all the games.
     * @author s224549
     */
    @Override
    public List<GameInDB> getGames() {
        List<GameInDB> result = new ArrayList<>();
        try {
            PreparedStatement ps = getSelectGameIdsStatement();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(GAME_GAMEID);
                String name = rs.getString(GAME_NAME);
                result.add(new GameInDB(id, name));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param game is the board that is used for the game with all the information on the players and so on.
     * @throws SQLException if something doesn't work.
     * creates the players in the database with the information from the board.
     * So it makes a for loop for all the players. it adds the playerid, playercolour, player position, playerheading,
     * playercheckpointvalue and playerhp. playerhp is how much life a player has.
     * @author s224549
     */
    private void createPlayersInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectPlayersStatementU();
        ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		for (int i = 0; i < game.getPlayersNumber(); i++) {
			Player player = game.getPlayer(i);
			rs.moveToInsertRow();
			rs.updateInt(PLAYER_GAMEID, game.getGameId());
			rs.updateInt(PLAYER_PLAYERID, i);
			rs.updateString(PLAYER_NAME, player.getName());
			rs.updateString(PLAYER_COLOUR, player.getColor());
			rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
			rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
			rs.updateInt(PLAYER_CHECKPOINTVALU,player.getCheckpointValue());
			rs.updateInt(PLAYER_HP,player.getHp());
			rs.insertRow();
		}

        rs.close();
    }

    /**
     * @param game is the board that is used for the game with all the information on the players and so on.
     * @throws SQLException if something doesn't work.
     * creates the cards for each player in the database. That means, both the cards on the hand and the programming cards.
     * but in the start there is no programming cards, so that doesn't really do that much form the start.
     * @author s224549
     */
    private void createCardFieldsInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectCardFieldStatementU();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();

        for (int i = 0; i < game.getPlayersNumber(); i++) {
            Player player = game.getPlayer(i);
            for (int j = 0; j < 5; j++) {
                rs.moveToInsertRow();
                rs.updateInt(FIELD_GAMEID, game.getGameId());
                rs.updateInt(FIELD_PLAYERID, i);
                rs.updateInt(FIELD_TYPE, 0);
                rs.updateInt(FIELD_POS, j);
                rs.updateBoolean(FIELD_VISIBLE, player.getProgramField(j).isVisible());
                CommandCard cards = player.getProgramField(j).getCard();
                if (cards != null) {
                    Command card1 = player.getProgramField(j).getCard().command;
                    Integer card2 = card1.ordinal();
                    if (card2 != null) {
                        rs.updateInt(FIELD_COMMAND, card1.ordinal());
                    } else {
                        rs.updateNull(FIELD_COMMAND);
                    }
                }
                rs.insertRow();
            }
            for (int j = 0; j < 8; j++) {
                rs.moveToInsertRow();
                rs.updateInt(FIELD_GAMEID, game.getGameId());
                rs.updateInt(FIELD_PLAYERID, i);
                rs.updateInt(FIELD_TYPE, 1);
                rs.updateInt(FIELD_POS, j);
                rs.updateBoolean(FIELD_VISIBLE, player.getCardField(j).isVisible());
                CommandCard cards = player.getCardField(j).getCard();
                if (cards != null) {
                    Command card3 = player.getCardField(j).getCard().command;
                    Integer card4 = card3.ordinal();
                    if (card4 != null) {
                        rs.updateInt(FIELD_COMMAND, card3.ordinal());
                    } else {
                        rs.updateNull(FIELD_COMMAND);
                    }
                }
                rs.insertRow();
            }
        }
    }

    /**
     * @param game is the board that is used for the game with all the information on the players and so on.
     * @throws SQLException if something doesn't work.
     * this method is used to load all the players on the game that the players chose to use again.
     * this method is never used bu itself, but is only used in the "LoadGameFromDB".
     * @author s224549
     */
    private void loadPlayersFromDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectPlayersASCStatement();
        ps.setInt(1, game.getGameId());

        ResultSet rs = ps.executeQuery();
        int i = 0;
        while (rs.next()) {
            int playerId = rs.getInt(PLAYER_PLAYERID);
            if (i++ == playerId) {
                String name = rs.getString(PLAYER_NAME);
                String colour = rs.getString(PLAYER_COLOUR);
                Player player = new Player(game, colour, name);
                game.addPlayer(player);

				int x = rs.getInt(PLAYER_POSITION_X);
				int y = rs.getInt(PLAYER_POSITION_Y);
				player.setSpace(game.getSpace(x, y));
				int heading = rs.getInt(PLAYER_HEADING);
				player.setHeading(Heading.values()[heading]);
				int chekpointvaule = rs.getInt(PLAYER_CHECKPOINTVALU);
				player.setCheckpointValue(chekpointvaule);
				int hp = rs.getInt(PLAYER_HP);
				player.setHp(hp);
            } else {
                System.err.println("Game in DB does not have a player with id " + i + "!");
            }
        }
        rs.close();
    }

    /**
     * @param game is the board that is used for the game with all the information on the players and so on.
     * @throws SQLException if something doesn't work.
     * loads the cardfields from every player that is in the game the players has chosen to use again.
     * @author s224549
     */
    private void loadCardFieldsFromDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectCardFieldStatement();
        ps.setInt(1, game.getGameId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int playerID = rs.getInt(FIELD_PLAYERID);
            Player player = game.getPlayer(playerID);
            int type = rs.getInt(FIELD_TYPE);
            int pos = rs.getInt(FIELD_POS);
            CommandCardField field;
            if (type == FIELD_TYPE_REGISTER) {
                field = player.getProgramField(pos);
            } else if (type == FIELD_TYPE_HAND) {
                field = player.getCardField(pos);
            } else {
                field = null;
            }
            if (field != null) {
                field.setVisible(rs.getBoolean(FIELD_VISIBLE));
                Object c = rs.getObject(FIELD_COMMAND);
                if (c != null) {
                    Command card = Command.values()[rs.getInt(FIELD_COMMAND)];
                    field.setCard(new CommandCard(card));
                }
            }
        }
        rs.close();
    }

    /**
     * @param game is the board that is used for the game with all the information on the players and so on.
     * @throws SQLException if something doesn't work.
     * Updates the players in the database.
     * It is used for everytime the player press save game. then it updates the players.
     * @author s224549
     */
    private void updatePlayersInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectPlayersStatementU();
        ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int playerId = rs.getInt(PLAYER_PLAYERID);
			Player player = game.getPlayer(playerId);
			rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
			rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
			rs.updateInt(PLAYER_CHECKPOINTVALU, player.getCheckpointValue());
			rs.updateInt(PLAYER_HP,player.getHp());
			rs.updateRow();
		}
		rs.close();
    }

    /**
     * @param game is the board that is used for the game with all the information on the players and so on.
     * @throws SQLException if something doesn't work.
     * Updates the cardfields in the database for every player in the game that the player has chosen to use.
     * This method is only used when the player presses save game.
     * @author s224549
     */
    private void updateCardFieldsInDB(Board game) throws SQLException {
        PreparedStatement ps = getSelectCardFieldStatementU();
        ps.setInt(1, game.getGameId());
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int playerId = rs.getInt(FIELD_PLAYERID);

            Player player = game.getPlayer(playerId);

            int type = rs.getInt(FIELD_TYPE);
            int pos = rs.getInt(FIELD_POS);
            CommandCardField field;
            if (FIELD_TYPE_REGISTER == type) {

                field = player.getProgramField(pos);

                if (field != null && field.getCard() != null ) {

                    rs.updateInt(FIELD_VISIBLE, 1);
                    String cardname = field.getCard().getName();

                    if(cardname != null ) {

                        try {
                            Command card1 = Command.valueOf(player.getProgramField(pos).getCard().getName());

                            if (card1 != null) {
                                rs.updateInt(FIELD_COMMAND, card1.ordinal());

                            } else {
                                rs.updateNull(FIELD_COMMAND);

                            }
                        }catch (IllegalArgumentException e){

                        }

                    }else{
                        rs.updateNull(FIELD_COMMAND);
                    }
                }else {
                    rs.updateNull(FIELD_COMMAND);

                }
            } else if (FIELD_TYPE_HAND == type) {
                field = player.getCardField(pos);
                if (field != null && field.getCard() != null ) {
                    rs.updateInt(FIELD_VISIBLE, 0);
                    String cardname = field.getCard().getName();
                    if (cardname != null) {
                        try {
                            Command card1 = Command.valueOf(cardname);

                            if (card1 != null) {
                                rs.updateInt(FIELD_COMMAND, card1.ordinal());
                                rs.updateRow();
                            } else {
                                rs.updateNull(FIELD_COMMAND);
                            }


                        }catch (IllegalArgumentException e){

                        }

                    }else {
                        rs.updateNull(FIELD_COMMAND);
                    }
                }

            } else {
                rs.updateNull(FIELD_TYPE_HAND);
            }

            rs.next();
        }
        rs.close();
    }

	private static final String SQL_INSERT_GAME =
			"INSERT INTO Game(name, currentPlayer, phase, step, boardname) VALUES (?, ?, ?, ?, ?)";

    private PreparedStatement insert_game_stmt = null;

    private PreparedStatement getInsertGameStatementRGK() {
        if (insert_game_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                insert_game_stmt = connection.prepareStatement(
                        SQL_INSERT_GAME,
                        Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return insert_game_stmt;
    }

    private static final String SQL_SELECT_GAME =
            "SELECT * FROM Game WHERE gameID = ?";

    private PreparedStatement select_game_stmt = null;

    private PreparedStatement getSelectGameStatementU() {
        if (select_game_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_game_stmt = connection.prepareStatement(
                        SQL_SELECT_GAME,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_game_stmt;
    }

    private static final String SQL_SELECT_PLAYERS =
            "SELECT * FROM Player WHERE gameID = ?";

    private PreparedStatement select_players_stmt = null;

    private PreparedStatement getSelectPlayersStatementU() {
        if (select_players_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_players_stmt = connection.prepareStatement(
                        SQL_SELECT_PLAYERS,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_players_stmt;
    }

    private static final String SQL_SELECT_CARD_FIELDS = "SELECT * FROM Cardfield WHERE gameID = ?";
    private static final String SQL_SELECT_CARD_FIELDS2 = "AND playerID = ?";

    private PreparedStatement select_card_field_stmt = null;

    private PreparedStatement getSelectCardFieldStatement() {
        if (select_card_field_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_card_field_stmt = connection.prepareStatement(
                        SQL_SELECT_CARD_FIELDS,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_card_field_stmt;
    }

    private PreparedStatement select_card_field_stmt_u = null;

    private PreparedStatement getSelectCardFieldStatementU() {
        if (select_card_field_stmt_u == null) {
            Connection connection = connector.getConnection();
            try {
                select_card_field_stmt_u = connection.prepareStatement(
                        SQL_SELECT_CARD_FIELDS,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_card_field_stmt_u;
    }

    private static final String SQL_SELECT_PLAYERS_ASC =
            "SELECT * FROM Player WHERE gameID = ? ORDER BY playerID ASC";

    private static final String SQL_SELECT_CARD_FIELDS_ASC =
            "SELECT * FROM Cardfield WHERE gameID = ? ORDER BY playerID asc";
    private PreparedStatement select_players_asc_stmt = null;

    private PreparedStatement getSelectPlayersASCStatement() {
        if (select_players_asc_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                // This statement does not need to be updatable
                select_players_asc_stmt = connection.prepareStatement(
                        SQL_SELECT_PLAYERS_ASC);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_players_asc_stmt;
    }

    private PreparedStatement select_Card_field_asc_stmt = null;

	private PreparedStatement getSelectCardfieldASCStatement(){
		if(select_Card_field_asc_stmt == null){
			Connection connection = connector.getConnection();
			try {
				select_Card_field_asc_stmt = connection.prepareStatement(
						SQL_SELECT_CARD_FIELDS);
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
		return select_Card_field_asc_stmt;
	}
	private static final String SQL_SELECT_GAMES =
			"SELECT gameID, name FROM Game";

    private PreparedStatement select_games_stmt = null;

    private PreparedStatement getSelectGameIdsStatement() {
        if (select_games_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_games_stmt = connection.prepareStatement(
                        SQL_SELECT_GAMES);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_games_stmt;
    }


}
