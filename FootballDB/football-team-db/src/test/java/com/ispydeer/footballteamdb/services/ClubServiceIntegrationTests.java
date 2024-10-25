package com.ispydeer.footballteamdb.services;

import com.ispydeer.footballteamdb.domain.dto.ClubDto;
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
public class ClubServiceIntegrationTests {

    @Autowired
    private ClubService underTest;

    @Test
    public void testThatClubIsSuccessfullySavedAndRecalledById() {
        ClubDto clubDtoBAR = TestDataCreator.createClubDtoBarca();
        long id = underTest.createClub(clubDtoBAR).getId();

        ClubDto result = underTest.retrieveClubById(id).orElse(null);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(clubDtoBAR.getName());
    }

    @Test
    public void testThatClubIsNotFoundById() {
        final int ID_OF_NOT_EXISTING_CLUB = 99;

        ClubDto clubDtoBAR = TestDataCreator.createClubDtoBarca();
        underTest.createClub(clubDtoBAR);

        ClubDto result = underTest.retrieveClubById(ID_OF_NOT_EXISTING_CLUB).orElse(null);

        assertThat(result).isNull();
    }

    @Test
    public void testThatClubsAreSuccessfullySavedAndRecalled() {
        ClubDto clubDtoBAR = TestDataCreator.createClubDtoBarca();
        underTest.createClub(clubDtoBAR);

        ClubDto clubDtoRM = TestDataCreator.createClubDtoRealMadrid();
        underTest.createClub(clubDtoRM);

        List<ClubDto> result = underTest.retrieveAllClubs();

        assertThat(result)
                .hasSize(2)
                .extracting(ClubDto::getName)
                .containsExactlyInAnyOrder(clubDtoBAR.getName(), clubDtoRM.getName());
    }

    @Test
    public void testThatClubIsUpdatedSuccessfully() {
        ClubDto clubDtoBAR = TestDataCreator.createClubDtoBarca();
        long id = underTest.createClub(clubDtoBAR).getId();

        clubDtoBAR.setName("Updated");
        ClubDto result = underTest.partialUpdateClub(clubDtoBAR, id);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(clubDtoBAR.getName());
    }

    @Test
    public void testThatClubIsDeletedSuccessfully() {
        ClubDto clubDtoBAR = TestDataCreator.createClubDtoBarca();
        long id = underTest.createClub(clubDtoBAR).getId();

        underTest.deleteClubById(id);
        List<ClubDto> result = underTest.retrieveAllClubs();

        assertThat(result).isEmpty();
    }
}
