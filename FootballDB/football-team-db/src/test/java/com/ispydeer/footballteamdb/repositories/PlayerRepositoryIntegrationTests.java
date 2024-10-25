package com.ispydeer.footballteamdb.repositories;

import com.ispydeer.footballteamdb.domain.entities.Club;
import com.ispydeer.footballteamdb.domain.entities.Player;
import com.ispydeer.footballteamdb.utilities.TestDataCreator;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PlayerRepositoryIntegrationTests {

    @Autowired
    private PlayerRepository underTest;

    @Test
    public void testThatPlayerCanBeCreatedAndRecalled(){
        Player player = TestDataCreator.createPlayerEntityLM();
        underTest.save(player);

        Optional<Player> result = underTest.findById(player.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(player);

    }

    @Test
    public void testThatMultiplePlayersCanBeRecalled(){
        Player playerLM = TestDataCreator.createPlayerEntityLM();
        underTest.save(playerLM);
        Player playerCR = TestDataCreator.createPlayerEntityCR();
        underTest.save(playerCR);

        List<Player> result = new ArrayList<>();
        for (Player player : underTest.findAll()) {
            result.add(player);
        }

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(playerLM, playerCR);
    }

    @Test
    public void testThatClubCanBeUpdated(){
        Player playerLM = TestDataCreator.createPlayerEntityLM();
        underTest.save(playerLM);

        playerLM.setFirstName("Updated");
        underTest.save(playerLM);

        Optional<Player> result = underTest.findById(playerLM.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(playerLM);
    }

    @Test
    public void testThatClubCanBeDeleted(){
        Player playerLM = TestDataCreator.createPlayerEntityLM();
        underTest.save(playerLM);
        underTest.deleteById(playerLM.getId());

        Optional<Player> result = underTest.findById(playerLM.getId());
        assertThat(result).isEmpty();
    }

}
