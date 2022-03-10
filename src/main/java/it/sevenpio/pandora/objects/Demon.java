package it.sevenpio.pandora.objects;

import java.util.HashMap;

public class Demon {

    private int id;
    private long lostStamina, recoveredStamina;
    private int turnsRecover, turnsEarningFragments;
    private HashMap<Integer, Integer> fragmentsPerTurn;

    public Demon(int id, long lostStamina, long recoveredStamina,
                 int turnsRecover, int turnsEarningFragments, HashMap<Integer, Integer> fragmentsPerTurn) {
        this.id = id;
        this.lostStamina = lostStamina;
        this.recoveredStamina = recoveredStamina;
        this.turnsRecover = turnsRecover;
        this.fragmentsPerTurn = fragmentsPerTurn;
        this.turnsEarningFragments = turnsEarningFragments;
    }

    public int getId() { return id; }
    public HashMap<Integer, Integer> getFragmentsPerTurn() {
        return fragmentsPerTurn;
    }

    public int getTurnsRecover() { return turnsRecover; }
    public long getLostStamina() { return lostStamina; }
    public long getRecoveredStamina() { return recoveredStamina; }
    public int getTurnsEarningFragments() { return turnsEarningFragments; }
}
