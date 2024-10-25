package com.ispydeer.footballteamdb.controllers;

import com.ispydeer.footballteamdb.domain.dto.ClubDto;
import com.ispydeer.footballteamdb.services.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing football clubs.
 */
@RestController
public class ClubController {

    private ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    /**
     * Creates a new football club.
     *
     * @param clubDto the data transfer object containing club details
     * @return a ResponseEntity containing the created ClubDto and HTTP status 201 (CREATED)
     */
    @PostMapping(path = "/clubs")
    public ResponseEntity<ClubDto> createClub(@RequestBody ClubDto clubDto) {
        return new ResponseEntity<>(clubService.createClub(clubDto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a football club by its ID.
     *
     * @param id the ID of the club to retrieve
     * @return a ResponseEntity containing the ClubDto and HTTP status 200 (OK) if found, or HTTP status 404 (NOT FOUND) if not
     */
    @GetMapping(path = "/clubs/{id}")
    public ResponseEntity<ClubDto> retrieveClub(@PathVariable Long id) {
        Optional<ClubDto> clubDtoOptional = clubService.retrieveClubById(id);
        return clubDtoOptional.map(clubDto -> new ResponseEntity<>(clubDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all football clubs.
     *
     * @return a ResponseEntity containing a list of ClubDto objects and HTTP status 200 (OK)
     */
    @GetMapping(path = "/clubs")
    public ResponseEntity<List<ClubDto>> retrieveAllClubs() {
        return new ResponseEntity<>(clubService.retrieveAllClubs(), HttpStatus.OK);
    }

    /**
     * Fully updates a football club by its ID.
     *
     * @param id      the ID of the club to update
     * @param clubDto the data transfer object containing updated club details
     * @return a ResponseEntity containing the updated ClubDto and HTTP status 200 (OK), or HTTP status 404 (NOT FOUND) if the club does not exist
     */
    @PutMapping(path = "/clubs/{id}")
    public ResponseEntity<ClubDto> fullUpdateClub(@PathVariable Long id, @RequestBody ClubDto clubDto) {
        if (clubService.existsClubById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clubDto.setId(id);
        return new ResponseEntity<>(clubService.createClub(clubDto), HttpStatus.OK);
    }

    /**
     * Partially updates a football club by its ID.
     *
     * @param id      the ID of the club to update
     * @param clubDto the data transfer object containing partial club updates
     * @return a ResponseEntity containing the updated ClubDto and HTTP status 200 (OK), or HTTP status 404 (NOT FOUND) if the club does not exist
     */
    @PatchMapping(path = "/clubs/{id}")
    public ResponseEntity<ClubDto> partialUpdateClub(@PathVariable Long id, @RequestBody ClubDto clubDto) {
        if (clubService.existsClubById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clubDto.setId(id);
        return new ResponseEntity<>(clubService.partialUpdateClub(clubDto, id), HttpStatus.OK);
    }

    /**
     * Deletes a football club by its ID.
     *
     * @param id the ID of the club to delete
     * @return a ResponseEntity with HTTP status 204 (NO CONTENT) upon successful deletion
     */
    @DeleteMapping(path = "/clubs/{id}")
    public ResponseEntity<ClubDto> deleteClub(@PathVariable Long id) {
        clubService.deleteClubById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
