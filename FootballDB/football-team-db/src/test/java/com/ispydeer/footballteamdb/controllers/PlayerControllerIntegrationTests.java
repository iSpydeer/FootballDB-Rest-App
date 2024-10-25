package com.ispydeer.footballteamdb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ispydeer.footballteamdb.domain.dto.PlayerDto;
import com.ispydeer.footballteamdb.domain.entities.Player;
import com.ispydeer.footballteamdb.services.PlayerService;
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
public class PlayerControllerIntegrationTests {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    public void testThatCreatePlayerSuccessfullyReturnsHttpStatus201AndSavedPlayer() throws Exception {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();

        String json = objectMapper.writeValueAsString(playerDtoLM);
        System.out.println(json);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/players")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(playerDtoLM.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(playerDtoLM.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(playerDtoLM.getPosition().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(playerDtoLM.getBirthDate().toString()));
    }

    @Test
    public void testThatGetPlayerSuccessfullyReturnsHttpStatus200AndFoundClub() throws Exception {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        Long id = playerService.createPlayer(playerDtoLM).getId();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/players/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(playerDtoLM.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(playerDtoLM.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(playerDtoLM.getPosition().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(playerDtoLM.getBirthDate().toString()));
    }

    @Test
    public void testThatGetPlayerUnsuccessfullyReturnsHttpStatus404() throws Exception {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        playerService.createPlayer(playerDtoLM);

        final int ID_OF_NOT_EXISTING_PLAYER = 99;
        mockMvc.perform(
                MockMvcRequestBuilders.get("/players/" + ID_OF_NOT_EXISTING_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatListsPlayersReturnsHttpStatus200AndListOfPlayers() throws Exception {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        playerService.createPlayer(playerDtoLM);

        PlayerDto playerDtoCR = TestDataCreator.createPlayerDtoCR();
        playerService.createPlayer(playerDtoCR);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/players")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].firstName",
                        Matchers.containsInAnyOrder(playerDtoLM.getFirstName(), playerDtoCR.getFirstName())));
    }

    @Test
    public void testThatFullUpdatePlayerReturnsHttpStatus200WhenPlayerAlreadyExistsAndUpdatedPlayer() throws Exception {
        PlayerDto playerDtoCR = TestDataCreator.createPlayerDtoCR();
        long id = playerService.createPlayer(playerDtoCR).getId();

        Player playerDtoLM = TestDataCreator.createPlayerEntityLM();
        String json = objectMapper.writeValueAsString(playerDtoLM);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/players/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(playerDtoLM.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(playerDtoLM.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(playerDtoLM.getPosition().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(playerDtoLM.getBirthDate().toString()));

    }

    @Test
    public void testThatFullUpdatePlayerReturnsHttpStatus404WhenPlayerDoesntExist() throws Exception {
        final int ID_OF_NOT_EXISTING_PLAYER = 99;

        PlayerDto playerDtoCR = TestDataCreator.createPlayerDtoCR();
        playerService.createPlayer(playerDtoCR);

        Player playerDtoLM = TestDataCreator.createPlayerEntityLM();
        String json = objectMapper.writeValueAsString(playerDtoLM);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/players/" + ID_OF_NOT_EXISTING_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatPartialUpdatePlayerReturnsHttpStatus200WhenPlayerAlreadyExistsAndUpdatedPlayer() throws Exception {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        Long id = playerService.createPlayer(playerDtoLM).getId();

        playerDtoLM.setFirstName("Updated");
        String json = objectMapper.writeValueAsString(playerDtoLM);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/players/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(playerDtoLM.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(playerDtoLM.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(playerDtoLM.getPosition().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(playerDtoLM.getBirthDate().toString()));

    }

    @Test
    public void testThatPartialUpdatePlayerReturnsHttpStatus404WhenPlayerDoesntExist() throws Exception {
        final int ID_OF_NOT_EXISTING_PLAYER = 99;

        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        playerService.createPlayer(playerDtoLM);

        playerDtoLM.setFirstName("Updated");
        String json = objectMapper.writeValueAsString(playerDtoLM);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/players/" + ID_OF_NOT_EXISTING_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatDeletePlayerReturnsStatus204ForExistingPlayer() throws Exception {
        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        Long id = playerService.createPlayer(playerDtoLM).getId();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/players/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeletePlayerReturnsStatus204ForNonExistingPlayer() throws Exception {
        final int ID_OF_NOT_EXISTING_PLAYER = 99;

        PlayerDto playerDtoLM = TestDataCreator.createPlayerDtoLM();
        playerService.createPlayer(playerDtoLM);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/players/" + ID_OF_NOT_EXISTING_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
