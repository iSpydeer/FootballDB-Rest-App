package com.ispydeer.footballteamdb.utilities;

import com.ispydeer.footballteamdb.domain.datatypes.Position;
import com.ispydeer.footballteamdb.domain.dto.ClubDto;
import com.ispydeer.footballteamdb.domain.dto.PlayerDto;
import com.ispydeer.footballteamdb.domain.entities.Club;
import com.ispydeer.footballteamdb.domain.entities.Player;

import java.sql.Date;
import java.time.LocalDate;

public final class TestDataCreator {
    private TestDataCreator(){};

    public static Club createClubEntityBarca(){
        return  Club.builder()
                .name("FC Barcelona")
                .shortName("BAR")
                .foundingDate(LocalDate.of(1899,11,29 ))
                .totalTrophies(100)
                .build();
    }

    public static Club createClubEntityRealMadrid(){
        return  Club.builder()
                .name("Real Madrid")
                .shortName("RM")
                .foundingDate(LocalDate.of(1902,2,6 ))
                .totalTrophies(99)
                .build();
    }

    public static ClubDto createClubDtoBarca(){
        return  ClubDto.builder()
                .name("FC Barcelona")
                .shortName("BAR")
                .foundingDate(LocalDate.of(1899,11,29 ))
                .totalTrophies(100)
                .build();
    }

    public static ClubDto createClubDtoRealMadrid(){
        return  ClubDto.builder()
                .name("Real Madrid")
                .shortName("RM")
                .foundingDate(LocalDate.of(1902,2,6 ))
                .totalTrophies(99)
                .build();
    }

    public static Player createPlayerEntityLM(){
        return Player.builder()
                .firstName("Lionel")
                .lastName("Messi")
                .birthDate(LocalDate.of(1987,6,24))
                .position(Position.MIDFIELDER)
                .club(createClubEntityBarca())
                .build();
    }

    public static Player createPlayerEntityCR(){
        return Player.builder()
                .firstName("Cristiano")
                .lastName("Ronaldo")
                .birthDate(LocalDate.of(1985,2,5))
                .position(Position.STRIKER)
                .club(createClubEntityRealMadrid())
                .build();
    }

    public static PlayerDto createPlayerDtoLM(){
        return PlayerDto.builder()
                .firstName("Lionel")
                .lastName("Messi")
                .birthDate(LocalDate.of(1987,6,24))
                .position(Position.MIDFIELDER)
                .club(createClubDtoBarca())
                .build();
    }

    public static PlayerDto createPlayerDtoCR(){
        return PlayerDto.builder()
                .firstName("Cristiano")
                .lastName("Ronaldo")
                .birthDate(LocalDate.of(1985,2,5))
                .position(Position.STRIKER)
                .club(createClubDtoRealMadrid())
                .build();
    }

}
