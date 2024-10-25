package com.ispydeer.footballteamdb.repositories;

import com.ispydeer.footballteamdb.domain.entities.Club;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends CrudRepository<Club, Long> {


}
