package com.ispydeer.footballteamdb.services;

import com.ispydeer.footballteamdb.domain.dto.ClubDto;
import com.ispydeer.footballteamdb.domain.entities.Club;
import com.ispydeer.footballteamdb.repositories.ClubRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling football club operations.
 */
@Service
public class ClubService {

    private ClubRepository clubRepository;
    private ModelMapper modelMapper;

    public ClubService(ClubRepository clubRepository, ModelMapper modelMapper) {
        this.clubRepository = clubRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Checks if a club exists by its ID.
     *
     * @param id the ID of the club
     * @return true if the club exists, false otherwise
     */
    public boolean existsClubById(long id) {
        return !clubRepository.existsById(id);
    }

    /**
     * Creates a new club.
     *
     * @param clubDto the data transfer object containing club details
     * @return the created ClubDto
     */
    public ClubDto createClub(ClubDto clubDto) {
        Club club = clubRepository.save(modelMapper.map(clubDto, Club.class));
        return modelMapper.map(club, ClubDto.class);
    }

    /**
     * Retrieves a club by its ID.
     *
     * @param clubId the ID of the club to retrieve
     * @return an Optional containing the ClubDto if found, or empty if not
     */
    public Optional<ClubDto> retrieveClubById(long clubId) {
        Optional<Club> club = clubRepository.findById(clubId);
        return club.map(value -> modelMapper.map(value, ClubDto.class));
    }

    /**
     * Retrieves all clubs.
     *
     * @return a list of ClubDto objects representing all clubs
     */
    public List<ClubDto> retrieveAllClubs() {
        List<ClubDto> clubList = new ArrayList<>();
        for (Club value : clubRepository.findAll()) {
            clubList.add(modelMapper.map(value, ClubDto.class));
        }
        return clubList;
    }

    /**
     * Partially updates a club's information by its ID.
     *
     * @param clubDto the data transfer object containing partial updates for the club
     * @param clubId  the ID of the club to update
     * @return the updated ClubDto
     * @throws RuntimeException if the club does not exist
     */
    public ClubDto partialUpdateClub(ClubDto clubDto, long clubId) {
        return clubRepository.findById(clubId).map(existingClub -> {
            Optional.ofNullable(clubDto.getName()).ifPresent(existingClub::setName);
            Optional.ofNullable(clubDto.getShortName()).ifPresent(existingClub::setShortName);
            Optional.ofNullable(clubDto.getFoundingDate()).ifPresent(existingClub::setFoundingDate);
            Optional.ofNullable(clubDto.getTotalTrophies()).ifPresent(existingClub::setTotalTrophies);
            return modelMapper.map(clubRepository.save(existingClub), ClubDto.class);
        }).orElseThrow(RuntimeException::new);
    }

    /**
     * Deletes a club by its ID.
     *
     * @param clubId the ID of the club to delete
     */
    public void deleteClubById(long clubId) {
        clubRepository.deleteById(clubId);
    }
}
