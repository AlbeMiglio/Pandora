package it.sevenpio.pandora.algorithm;

import it.sevenpio.pandora.file.FileUtils;
import it.sevenpio.pandora.manager.DemonManager;
import it.sevenpio.pandora.objects.Demon;
import it.sevenpio.pandora.objects.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Algorithm {

    public void execute() {
        DemonManager demonManager = new DemonManager();
        Player player = null;

        Random rand = new Random();
        int startingStamina;
        int maxStamina;
        int turns = 0;
        int demonsN = Integer.MAX_VALUE;
        int tests = 5;  // Warning: increasing this value will require much more computational power

        List<String> files = Arrays.asList("01-the-cloud-abyss.txt",
                "02-iot-island-of-terror.txt", "03-etheryum.txt", "04-the-desert-of-autonomous-machines.txt",
                "05-androids-armageddon.txt");
        for (String fileName : files) {
            demonManager.clearMap();
            System.out.printf("Starting with file %s...\n", fileName);
            Instant init = Instant.now();
            InputStream inputStream = FileUtils.getFileFromResourceAsStream(fileName);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                boolean firstLine = true;
                boolean keepReading = true;
                int demonCounter = 0;
                String line = br.readLine();
                while (keepReading && line != null) {
                    if (firstLine) {
                        startingStamina = Math.min(Math.max(0, Integer.parseInt(line.split(" ")[0])), 100000);
                        maxStamina = Math.min(Integer.parseInt(line.split(" ")[1]), 100000);
                        turns = Math.min(Integer.parseInt(line.split(" ")[2]), 1000000);
                        demonsN = Math.min(Integer.parseInt(line.split(" ")[3]), 100000);
                        player = new Player(startingStamina, maxStamina, turns, demonsN);
                        firstLine = false;
                        continue;
                    }

                    String[] values = line.split(" ");
                    int len = values.length;
                    int consumingStaminaToFight = Integer.parseInt(values[0]);
                    int turnsToRecover = Math.min(Math.max(1, Integer.parseInt(values[1])), turns);
                    int recoveringStamina = Math.min(Integer.parseInt(values[2]), 100000);
                    int turnsEarningFragments = Math.min(Integer.parseInt(values[3]), 100000);
                    HashMap<Integer, Integer> map = new HashMap<>();
                    for (int j = 4; j < Math.min(turnsEarningFragments, len); j++) {
                        int currentTurn = j - 4;
                        int fragmentsForJTurn = Math.min(Integer.parseInt(values[j]), 10000);
                        map.put(currentTurn, fragmentsForJTurn);
                    }

                    demonManager.add(new Demon(demonCounter, consumingStaminaToFight,
                            recoveringStamina, turnsToRecover, turnsEarningFragments, map));
                    demonCounter++;
                    line = br.readLine();
                    if (demonCounter > demonsN)
                        keepReading = false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Instant endLoading = Instant.now();
            System.out.printf("Input loaded - Time elapsed: %d seconds\n",
                    endLoading.getEpochSecond() - init.getEpochSecond());
            System.out.println("Computing...");
            long maxFragments = 0;
            List<Map.Entry<Integer, Integer>> currentBestDemonsList = null;

            for (int k = 0; k < tests; k++) {
                Instant start = Instant.now();
                demonManager.clearDefeated();
                int currentTurn = 0;
                int currentStamina = player.getStamina();
                long currentFragments = 0;

                while (currentTurn < player.getAvailableTurns()) {
                    for (Integer id : demonManager.getDefeatedDemons().keySet()) {
                        Demon demon = demonManager.getDemonByName(id);

                        int defeatTurn = demonManager.getDefeatTurnByDemon(demon);
                        if (currentTurn - defeatTurn == demon.getTurnsRecover()) {
                            long recoveringStamina = demon.getRecoveredStamina();
                            currentStamina += recoveringStamina;
                        }
                    }

                    int finalCurrentStamina = currentStamina;
                    List<Demon> demons = demonManager.getDemons().values().stream()
                            .filter(demon -> !demonManager.getDefeatedDemons().containsKey(demon.getId())
                                    && demon.getLostStamina() <= finalCurrentStamina).collect(Collectors.toList());

                    if (demons.size() > 0) {
                        int randomDemon = rand.ints(0, demons.size()).findFirst().getAsInt();
                        Demon fightingDemon = demons.get(randomDemon);
                        currentStamina -= fightingDemon.getLostStamina();
                        demonManager.addDefeat(fightingDemon, currentTurn);
                    }

                    currentTurn++;
                }

                // fragments calculation
                for (Integer id : demonManager.getDefeatedDemons().keySet()) {
                    Demon demon = demonManager.getDemonByName(id);

                    int defeatTurn = demonManager.getDefeatTurnByDemon(demon);
                    for (int i = 0; i < Math.min(turns - defeatTurn, demon.getFragmentsPerTurn().size()); i++) {
                        currentFragments += demon.getFragmentsPerTurn().get(i);
                    }
                }

                List<Map.Entry<Integer, Integer>> demonsList = demonManager.getDefeatedDemons().entrySet().stream()
                        .sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
                if (currentFragments > maxFragments) {
                    maxFragments = currentFragments;
                    currentBestDemonsList = demonsList;
                }
                Instant end = Instant.now();
                System.out.printf("Current try: %d/%d (%d seconds)- Current max points: %d\n",
                        k + 1, tests, end.getEpochSecond() - start.getEpochSecond(), maxFragments);
            }

            FileUtils.writeOutputFile(currentBestDemonsList, fileName);
        }
    }
}
