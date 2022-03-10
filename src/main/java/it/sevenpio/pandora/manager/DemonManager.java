package it.sevenpio.pandora.manager;

import it.sevenpio.pandora.objects.Demon;

import java.util.concurrent.ConcurrentHashMap;

public class DemonManager {

    private ConcurrentHashMap<Integer, Demon> demons;
    private ConcurrentHashMap<Integer, Integer> defeatedDemons;

    public DemonManager() {
        demons = new ConcurrentHashMap<>();
        defeatedDemons = new ConcurrentHashMap<>();
    }

    public Demon getDemonByName(Integer id) { return demons.get(id); }
    public void add(Demon demon) { demons.put(demon.getId(), demon); }

    public int getDefeatTurnByDemon(Demon demon) { return defeatedDemons.get(demon.getId()); }
    public void addDefeat(Demon demon, int turn) { defeatedDemons.put(demon.getId(), turn); }

    public ConcurrentHashMap<Integer, Demon> getDemons() { return demons; }
    public ConcurrentHashMap<Integer, Integer> getDefeatedDemons() { return defeatedDemons; }

    public void clearMap() { demons.clear(); }
    public void clearDefeated() {
        defeatedDemons.clear();
    }
}
