package org.kmontano.minidex.dto.response;

import lombok.Data;
import org.kmontano.minidex.domain.trainer.Trainer;

@Data
public class TrainerProfileDTO {
    private String name;
    private String username;
    private int coins;
    private int xp;
    private int level;
    private int caughtPokemons;
    private int wins;
    private int loses;

    public TrainerProfileDTO(Trainer t, int caughtPokemons) {
        this.name = t.getName();
        this.username = t.getUsername();
        this.coins = t.getCoins();
        this.xp = t.getXp();
        this.level = t.getLevel();
        this.caughtPokemons = caughtPokemons;
        this.wins = t.getWins();
        this.loses = t.getLoses();
    }
}
