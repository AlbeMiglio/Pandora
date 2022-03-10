package it.sevenpio.pandora.objects;

public class Player {

    private int stamina, staminaMax, availableTurns, availableDemons;

    public Player(int stamina, int staminaMax, int availableTurns, int availableDemons) {
        this.stamina = stamina;
        this.staminaMax = staminaMax;
        this.availableTurns = availableTurns;
        this.availableDemons = availableDemons;
    }

    public int getAvailableTurns() { return availableTurns; }
    public int getStamina() { return stamina; }

}
