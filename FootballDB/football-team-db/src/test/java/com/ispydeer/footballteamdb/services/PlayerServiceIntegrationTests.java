package com.ispydeer.footballteamdb.services;

import com.ispydeer.footballteamdb.domain.dto.ClubDto;
import com.ispydeer.footballteamdb.domain.dto.PlayerDto;
import com.ispydeer.footballteamdb.utilities.TestDataCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class PlayerServiceIntegrationTests {

    @Autowired
    private PlayerService underTest;

    @Test
    public void testThatPlayerIsSuccessfullySavedAndRecalledById() {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        long id = underTest.createPlayer(playerDtoLM).getId();

        PlayerDto result = underTest.retrievePlayerById(id).orElse(null);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(playerDtoLM.getFirstName());
    }

    @Test
    public void testThatPlayerIsNotFoundById() {
        final int ID_OF_NOT_EXISTING_PLAYER = 99;

        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        underTest.createPlayer(playerDtoLM);

        PlayerDto result = underTest.retrievePlayerById(ID_OF_NOT_EXISTING_PLAYER).orElse(null);

        assertThat(result).isNull();
    }

    @Test
    public void testThatPlayersAreSuccessfullySavedAndRecalled() {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        underTest.createPlayer(playerDtoLM);

        PlayerDto playerDtoCR = TestDataCreator.createPlayerDtoCR();
        underTest.createPlayer(playerDtoCR);

        List<PlayerDto> result = underTest.retrieveAllPlayers();

        assertThat(result)
                .hasSize(2)
                .extracting(PlayerDto::getFirstName)
                .containsExactlyInAnyOrder(playerDtoLM.getFirstName(), playerDtoCR.getFirstName());
    }

    @Test
    public void testThatClubIsUpdatedSuccessfully() {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        long id = underTest.createPlayer(playerDtoLM).getId();

        playerDtoLM.setFirstName("Updated");
        PlayerDto result = underTest.partialUpdatePlayer(playerDtoLM, id);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(playerDtoLM.getFirstName());
    }

    @Test
    public void testThatClubIsDeletedSuccessfully() {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        long id = underTest.createPlayer(playerDtoLM).getId();

        underTest.deletePlayerById(id);
        List<PlayerDto> result = underTest.retrieveAllPlayers();

        assertThat(result).isEmpty();
    }
}
