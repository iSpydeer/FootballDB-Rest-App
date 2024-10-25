package com.ispydeer.footballteamdb.controllers;

import com.ispydeer.footballteamdb.domain.dto.PlayerDto;
import com.ispydeer.footballteamdb.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing football players.
 */
@RestController
public class PlayerController {

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Creates a new football player.
     *
     * @param playerDto the data transfer object containing player details
     * @return a ResponseEntity containing the created PlayerDto and HTTP status 201 (CREATED)
     */
    @PostMapping(path = "/players")
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        return new ResponseEntity<>(playerService.createPlayer(playerDto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a football player by their ID.
     *
     * @param id the ID of the player to retrieve
     * @return a ResponseEntity containing the PlayerDto and HTTP status 200 if found, or HTTP status 404 (NOT FOUND) if not
     */
    @GetMapping(path = "/players/{id}")
    public ResponseEntity<PlayerDto> retrievePlayer(@PathVariable Long id) {
        Optional<PlayerDto> playerDtoOptional = playerService.retrievePlayerById(id);
        return playerDtoOptional.map(playerDto -> new ResponseEntity<>(playerDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all football players.
     *
     * @return a ResponseEntity containing a list of PlayerDto objects and HTTP status 200 (OK)
     */
    @GetMapping(path = "/players")
    public ResponseEntity<List<PlayerDto>> retrieveAllPlayers() {
        return new ResponseEntity<>(playerService.retrieveAllPlayers(), HttpStatus.OK);
    }

    /**
     * Fully updates a football player by their ID.
     *
     * @param id        the ID of the player to update
     * @param playerDto the data transfer object containing updated player details
     * @return a ResponseEntity containing the updated PlayerDto and HTTP status 200 (OK), or HTTP status 404 (NOT FOUND) if the player does not exist
     */
    @PutMapping(path = "/players/{id}")
    public ResponseEntity<PlayerDto> fullUpdatePlayer(@PathVariable Long id, @RequestBody PlayerDto playerDto) {
        if (playerService.existsPlayerById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerDto.setId(id);
        return new ResponseEntity<>(playerService.createPlayer(playerDto), HttpStatus.OK);
    }

    /**
     * Partially updates a football player by their ID.
     *
     * @param id        the ID of the player to update
     * @param playerDto the data transfer object containing partial player updates
     * @return a ResponseEntity containing the updated PlayerDto and HTTP status 200 (OK), or HTTP status 404 (NOT FOUND) if the player does not exist
     */
    @PatchMapping(path = "/players/{id}")
    public ResponseEntity<PlayerDto> partialUpdatePlayer(@PathVariable Long id, @RequestBody PlayerDto playerDto) {
        if (playerService.existsPlayerById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerDto.setId(id);
        return new ResponseEntity<>(playerService.partialUpdatePlayer(playerDto, id), HttpStatus.OK);
    }

    /**
     * Deletes a football player by their ID.
     *
     * @param id the ID of the player to delete
     * @return a ResponseEntity with HTTP status 204 (NO CONTENT) upon successful deletion
     */
    @DeleteMapping(path = "/players/{id}")
    public ResponseEntity<PlayerDto> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
