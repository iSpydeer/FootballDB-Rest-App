package com.ispydeer.footballteamdb.domain.dto;

import com.ispydeer.footballteamdb.domain.datatypes.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Position position;
    private ClubDto club;

}
