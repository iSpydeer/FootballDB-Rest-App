package com.ispydeer.footballteamdb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ispydeer.footballteamdb.domain.dto.ClubDto;
import com.ispydeer.footballteamdb.domain.entities.Club;
import com.ispydeer.footballteamdb.services.ClubService;
import com.ispydeer.footballteamdb.utilities.TestDataCreator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ClubControllerIntegrationTests {

    @Autowired
    private ClubService clubService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());


    @Test
    public void testThatCreateClubSuccessfullyReturnsHttpStatus201AndSavedClub() throws Exception {
        ClubDto clubDto = TestDataCreator.createClubDtoBarca();

        String json = objectMapper.writeValueAsString(clubDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/clubs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(clubDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortName").value(clubDto.getShortName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.foundingDate").value(clubDto.getFoundingDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalTrophies").value(clubDto.getTotalTrophies()));
    }

    @Test
    public void testThatGetClubSuccessfullyReturnsHttpStatus200AndFoundClub() throws Exception {
        ClubDto clubDto = TestDataCreator.createClubDtoBarca();
        long id = clubService.createClub(clubDto).getId();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/clubs/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(clubDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortName").value(clubDto.getShortName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.foundingDate").value(clubDto.getFoundingDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalTrophies").value(clubDto.getTotalTrophies()));
    }

    @Test
    public void testThatGetClubUnsuccessfullyReturnsHttpStatus404() throws Exception {
        ClubDto clubDto = TestDataCreator.createClubDtoBarca();
        clubService.createClub(clubDto);

        final int ID_OF_NOT_EXISTING_CLUB = 99;
        mockMvc.perform(
                MockMvcRequestBuilders.get("/clubs/" + ID_OF_NOT_EXISTING_CLUB)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatListsClubsReturnsHttpStatus200AndListOfClubs() throws Exception {
        ClubDto clubDtoBAR = TestDataCreator.createClubDtoBarca();
        clubService.createClub(clubDtoBAR);

        ClubDto clubDtoRM = TestDataCreator.createClubDtoBarca();
        clubService.createClub(clubDtoRM);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/clubs")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name",
                        Matchers.containsInAnyOrder(clubDtoBAR.getName(), clubDtoRM.getName())));
    }

    @Test
    public void testThatFullUpdateClubReturnsHttpStatus200WhenClubAlreadyExistsAndUpdatedClub() throws Exception {
        ClubDto clubDtoBAR = TestDataCreator.createClubDtoBarca();
        long id = clubService.createClub(clubDtoBAR).getId();

        ClubDto clubDtoRM = TestDataCreator.createClubDtoRealMadrid();
        String json = objectMapper.writeValueAsString(clubDtoRM);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/clubs/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(clubDtoRM.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortName").value(clubDtoRM.getShortName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.foundingDate").value(clubDtoRM.getFoundingDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalTrophies").value(clubDtoRM.getTotalTrophies()));

    }

    @Test
    public void testThatFullUpdateClubReturnsHttpStatus404WhenClubDoesntExist() throws Exception {
        final int ID_OF_NOT_EXISTING_CLUB = 99;

        ClubDto clubDtoBAR = TestDataCreator.createClubDtoBarca();
        clubService.createClub(clubDtoBAR);

        ClubDto clubDtoRM = TestDataCreator.createClubDtoRealMadrid();
        String json = objectMapper.writeValueAsString(clubDtoRM);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/clubs/" + ID_OF_NOT_EXISTING_CLUB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatPartialUpdateClubReturnsHttpStatus200WhenClubAlreadyExistsAndUpdatedClub() throws Exception {
        ClubDto clubDto = TestDataCreator.createClubDtoBarca();
        long id = clubService.createClub(clubDto).getId();

        clubDto.setName("Updated");
        String json = objectMapper.writeValueAsString(clubDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/clubs/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(clubDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortName").value(clubDto.getShortName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.foundingDate").value(clubDto.getFoundingDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalTrophies").value(clubDto.getTotalTrophies()));

    }

    @Test
    public void testThatPartialUpdateClubReturnsHttpStatus404WhenClubDoesntExist() throws Exception {
        final int ID_OF_NOT_EXISTING_CLUB = 99;

        ClubDto clubDto = TestDataCreator.createClubDtoBarca();
        clubService.createClub(clubDto);

        clubDto.setName("Updated");
        String json = objectMapper.writeValueAsString(clubDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/clubs/" + ID_OF_NOT_EXISTING_CLUB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatDeleteClubReturnsStatus204ForExistingClub() throws Exception {
        ClubDto clubDto = TestDataCreator.createClubDtoBarca();
        long id = clubService.createClub(clubDto).getId();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/clubs/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteClubReturnsStatus204ForNonExistingClub() throws Exception {
        final int ID_OF_NOT_EXISTING_CLUB = 99;

        ClubDto clubDto = TestDataCreator.createClubDtoBarca();
        clubService.createClub(clubDto);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/clubs/" + ID_OF_NOT_EXISTING_CLUB)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
