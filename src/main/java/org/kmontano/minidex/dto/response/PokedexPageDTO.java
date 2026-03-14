package org.kmontano.minidex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PokedexPageDTO {
    private List<PokemonDTO> pokemons;
    private int page;
    private int totalPages;
    private int totalElements;
}
