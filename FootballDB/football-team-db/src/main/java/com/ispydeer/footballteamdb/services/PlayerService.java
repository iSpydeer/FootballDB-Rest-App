package com.ispydeer.footballteamdb.services;

import com.ispydeer.footballteamdb.domain.dto.PlayerDto;
import com.ispydeer.footballteamdb.domain.entities.Club;
import com.ispydeer.footballteamdb.domain.entities.Player;
import com.ispydeer.footballteamdb.repositories.PlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling football player operations.
 */
@Service
public class PlayerService {
    private PlayerRepository playerRepository;
    private ModelMapper modelMapper;

    public PlayerService(PlayerRepository playerRepository, ModelMapper modelMapper) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Checks if a player exists by their ID.
     *
     * @param id the ID of the player
     * @return true if the player exists, false otherwise
     */
    public boolean existsPlayerById(long id) {
        return !playerRepository.existsById(id);
    }

    /**
     * Creates a new player.
     *
     * @param playerDto the data transfer object containing player details
     * @return the created PlayerDto
     */
    public PlayerDto createPlayer(PlayerDto playerDto) {
        Player player = playerRepository.save(modelMapper.map(playerDto, Player.class));
        return modelMapper.map(player, PlayerDto.class);
    }

    /**
     * Retrieves a player by their ID.
     *
     * @param playerId the ID of the player to retrieve
     * @return an Optional containing the PlayerDto if found, or empty if not
     */
    public Optional<PlayerDto> retrievePlayerById(long playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        return player.map(value -> modelMapper.map(value, PlayerDto.class));
    }

    /**
     * Retrieves all players.
     *
     * @return a list of PlayerDto objects representing all players
     */
    public List<PlayerDto> retrieveAllPlayers() {
        List<PlayerDto> playerList = new ArrayList<>();
        for (Player value : playerRepository.findAll()) {
            playerList.add(modelMapper.map(value, PlayerDto.class));
        }
        return playerList;
    }

    /**
     * Partially updates a player's information by their ID.
     *
     * @param playerDto the data transfer object containing partial updates for the player
     * @param playerId  the ID of the player to update
     * @return the updated PlayerDto
     * @throws RuntimeException if the player does not exist
     */
    public PlayerDto partialUpdatePlayer(PlayerDto playerDto, long playerId) {
        return playerRepository.findById(playerId).map(existingPlayer -> {
            Optional.ofNullable(playerDto.getFirstName()).ifPresent(existingPlayer::setFirstName);
            Optional.ofNullable(playerDto.getLastName()).ifPresent(existingPlayer::setLastName);
            Optional.ofNullable(playerDto.getBirthDate()).ifPresent(existingPlayer::setBirthDate);
            Optional.ofNullable(playerDto.getPosition()).ifPresent(existingPlayer::setPosition);
            Optional.ofNullable(playerDto.getClub()).ifPresent(clubDto -> existingPlayer.setClub(modelMapper.map(clubDto, Club.class)));
            return modelMapper.map(playerRepository.save(existingPlayer), PlayerDto.class);
        }).orElseThrow(RuntimeException::new);
    }

    /**
     * Deletes a player by their ID.
     *
     * @param playerId the ID of the player to delete
     */
    public void deletePlayerById(long playerId) {
        playerRepository.deleteById(playerId);
    }
}
