package main.core;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class StatsTracker {

    final static String newLine = System.getProperty("line.separator");

    //-- Looping/Iteration --//

    // Time of last memory read
    static long lastTickTime = 0;
    // Time of current memory read
    static long currentTickTime;
    // Is a game being tracked?
    static boolean tracking = false;

    //-- Output Data --//

    static String redTeamName = "";
    static String blueTeamName = "";
    static GameDate gameDate;
    // Map of players who have played, i.e. those that should be shown in output
    // to their performance in the game
    static HashMap<String, Performance> players = new HashMap<String, Performance>();
    // Lists of goals, to be output
    static ArrayList<Goal> redGoals = new ArrayList<Goal>();
    static ArrayList<Goal> blueGoals = new ArrayList<Goal>();

    //-- StatsUploader: has state after verifyGame() that uploads to that game --//
    static StatsUploader uploader = new StatsUploader();

    //-- Current memory data --//

    static int redScore = 0;
    static int blueScore = 0;
    static int time, period;
    static ArrayList<GamePlayerStruct> gamePlayers = new ArrayList<GamePlayerStruct>();

    //-- Previous memory data --//

    static int previousRedScore, previousBlueScore, previousTime;
    static ArrayList<GamePlayerStruct> previousGamePlayers = new ArrayList<GamePlayerStruct>();

    static void reset() {
        redScore = 0;
        blueScore = 0;
        redTeamName = "";
        blueTeamName = "";
        redGoals = new ArrayList<Goal>();
        blueGoals = new ArrayList<Goal>();
        players = new HashMap<String, Performance>();
        uploader = new StatsUploader();
        gamePlayers = new ArrayList<GamePlayerStruct>();
        previousGamePlayers = new ArrayList<GamePlayerStruct>();
    }

    /**
     * Turn a game time integer into a pretty readable time
     * @param time
     * @return Formatted time string
     */
    public static String formatTime(int time) {
        int totalSeconds = time / 100;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds - (minutes * 60);

        // Add a leading zero to seconds if < 10
        return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
    }

    static String[] detectCommands(String message) {
        String[] args = {};
        if (message.length() >= 4 && message.substring(0, 4).equals("!se ")) {
            args = message.substring(4).split(" ");
            System.out.println("Command detected: " + args[0]);
        }
        return args;
    }

    static void handleCommands(String[] args) {
        if (args.length == 0) {
            return;
        }
        if (args[0].equals("start") && args.length == 4) {
            System.out.println("Valid start command args");
            redTeamName = args[1];
            blueTeamName = args[2];
            String dateString = args[3];
            int month = Integer.parseInt(dateString.substring(0, 2));
            int day = Integer.parseInt(dateString.substring(2, 4));
            int year = Integer.parseInt(dateString.substring(4, 6));
            int game = Integer.parseInt(dateString.substring(6, 7));
            switch (game) {
            case 1:
                gameDate = new GameDate(year, month, day, 19, 30);
                break;
            case 2:
                gameDate = new GameDate(year, month, day, 19, 50);
                break;
            case 3:
                gameDate = new GameDate(year, month, day, 20, 10);
                break;
            }
            System.out.println(gameDate.toString());

            if (uploader.verifyGame(redTeamName, blueTeamName, gameDate)) {
                System.out.println("Tracking: " + redTeamName + " vs. " + blueTeamName + " " + gameDate.toString());
                tracking = true;
            }
        }
    }

    static boolean timeForNextTick() {
        currentTickTime = System.currentTimeMillis();
        return currentTickTime - lastTickTime >= 1000;
    }

    /**
     * Update static members containing data from memory
     */
    static void updateGameData() {
        time = StatsExtractor.getTime();
        period = StatsExtractor.getPeriod();
        redScore = StatsExtractor.getRedScore();
        blueScore = StatsExtractor.getBlueScore();
        gamePlayers = StatsExtractor.getPlayers();
    }

    static void incrementTimeOnIce() {
        if (previousTime != time) {
            for (GamePlayerStruct gps : gamePlayers) {
                if (gps.isPlaying()) {
                    String name = gps.name;
                    if (players.containsKey(name)) {
                        players.get(name).toi++;
                    } else {
                        players.put(name, new Performance(gps.team));
                    }
                }
            }
        }
    }

    static void updatePlusMinus(int teamScored, GamePlayerStruct player) {
        if (player.isPlaying()) {
            if (player.team == teamScored) {
                players.get(player.name).plusminus++;
            } else {
                players.get(player.name).plusminus--;
            }
        }
    }

    static void addGoal(int team, int time, int period) {
        String scorer = "", assister = "";

        // Find the name of the scorer and the assister, update +/-
        for (GamePlayerStruct currentPlayer : gamePlayers) {

            updatePlusMinus(team, currentPlayer);

            for (GamePlayerStruct previousPlayer : previousGamePlayers) {
                if (currentPlayer.name.equals(previousPlayer.name)) {

                    if (currentPlayer.goals != previousPlayer.goals) {
                        scorer = currentPlayer.name;
                    }

                    if (currentPlayer.assists != previousPlayer.assists) {
                        assister = currentPlayer.name;
                    }
                }
            }
        }

        if (team == 0) {
            redGoals.add(new Goal(scorer, assister, time, period, 0));
        } else {
            blueGoals.add(new Goal(scorer, assister, time, period, 1));
        }
    }

    static void updatePreviousGameData() {
        previousRedScore = redScore;
        previousBlueScore = blueScore;
        previousGamePlayers = gamePlayers;
        previousTime = time;
    }

    /**
     * Check for the end of the game
     * Period value increases at the start of intermission not the next period
     * it appears: time = 0 if game ends in regulation
     * 			   time = 1 if goal is scored in OT
     */
    static boolean endOfGame() {
        return period > 3 && redScore != blueScore;
    }

    static void addGoalsToPerformances(ArrayList<Goal> goals) {
        for (Goal g : goals) {
            if (!g.scorer.equals("")) {
                players.get(g.scorer).goals++;
            }
            if (!g.assister.equals("")) {
                players.get(g.assister).assists++;
            }
        }
    }

    static void writeGoalsToFile(File file, ArrayList<Goal> goals) throws IOException {
        for (Goal g : goals) {
            FileUtils.writeStringToFile(file, g.toString() + newLine, true);
        }
    }

    static void writePerformancesToFile(File file) throws IOException {
        for (Entry<String, Performance> e : players.entrySet()) {
            FileUtils.writeStringToFile(file, (e.getKey() + ": " + e.getValue().toString() + newLine), true);
        }
    }

    static void outputData() {
        Date now = new Date();

        File goalsFile = new File(now.getTime() + "-goals.txt"); 
        File performancesFile = new File(now.getTime() + "-performances.txt"); 

        String gameScore = "RED: " + redGoals.size() + " BLUE: " + blueGoals.size();

        try {
            FileUtils.writeStringToFile(performancesFile, gameScore + newLine, true);
            FileUtils.writeStringToFile(goalsFile, gameScore + newLine, true);

            writeGoalsToFile(goalsFile, redGoals);
            writeGoalsToFile(goalsFile, blueGoals);

            writePerformancesToFile(performancesFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        StatsExtractor.init();

        // Initial previous scores to score when tracker starts
        previousRedScore = StatsExtractor.getRedScore();
        previousBlueScore = StatsExtractor.getBlueScore();

        while (true) {
            if (!tracking) {
                handleCommands(detectCommands(StatsExtractor.getLastChatMessage()));
            } else if (timeForNextTick()) {
                lastTickTime = currentTickTime;

                updateGameData();

                incrementTimeOnIce();

                // If score has changed

                if (redScore != previousRedScore) {
                    addGoal(0, time, period);
                }

                if (blueScore != previousBlueScore) {
                    addGoal(1, time, period);
                }

                updatePreviousGameData();

                if (endOfGame()) {
                    addGoalsToPerformances(redGoals);
                    addGoalsToPerformances(blueGoals);
                    outputData();
                    ArrayList<Goal> toUpload = new ArrayList<Goal>();
                    toUpload.addAll(redGoals);
                    toUpload.addAll(blueGoals);
                    uploader.upload(toUpload);
                    reset();
                    tracking = false;
                }
            }
        }
    }
}