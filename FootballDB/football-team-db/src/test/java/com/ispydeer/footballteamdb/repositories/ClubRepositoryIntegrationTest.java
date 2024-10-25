package com.ispydeer.footballteamdb.repositories;

import com.ispydeer.footballteamdb.domain.entities.Club;
import com.ispydeer.footballteamdb.utilities.TestDataCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClubRepositoryIntegrationTest {

    @Autowired
    private ClubRepository underTest;

    @Test
    public void testThatClubCanBeCreatedAndRecalled() {
        Club clubBarca = TestDataCreator.createClubEntityBarca();
        underTest.save(clubBarca);

        Optional<Club> result = underTest.findById(clubBarca.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(clubBarca);
    }

    @Test
    public void testThatMultipleClubsCanBeRecalled() {
        Club clubBarca = TestDataCreator.createClubEntityBarca();
        underTest.save(clubBarca);
        Club clubRealMadrid = TestDataCreator.createClubEntityRealMadrid();
        underTest.save(clubRealMadrid);

        List<Club> result = new ArrayList<>();
        for (Club club : underTest.findAll()) {
            result.add(club);
        }

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(clubBarca, clubRealMadrid);
    }

    @Test
    public void testThatClubCanBeUpdated() {
        Club clubBarca = TestDataCreator.createClubEntityBarca();
        underTest.save(clubBarca);

        clubBarca.setName("Updated");
        underTest.save(clubBarca);

        Optional<Club> result = underTest.findById(clubBarca.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(clubBarca);
    }

    @Test
    public void testThatClubCanBeDeleted() {
        Club clubBarca = TestDataCreator.createClubEntityBarca();
        underTest.save(clubBarca);
        underTest.deleteById(clubBarca.getId());

        Optional<Club> result = underTest.findById(clubBarca.getId());
        assertThat(result).isEmpty();
    }

}
