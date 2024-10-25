package com.ispydeer.footballteamdb.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubDto {
    private Long id;
    private String name;
    private String shortName;
    private LocalDate foundingDate;
    private Integer totalTrophies;
}
