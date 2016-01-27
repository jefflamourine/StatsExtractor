package main.core;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class OutputHandler {

    final static String newLine = System.getProperty("line.separator");

    public static void writeGoalsToFile(File file, OutputData data) throws IOException {
        for (Goal g : data.goals()) {
            FileUtils.writeStringToFile(file, g.toString() + newLine, true);
        }
    }

    public static void writePerformancesToFile(File file, OutputData data) throws IOException {
        for (Entry<String, Performance> e : data.players.entrySet()) {
            FileUtils.writeStringToFile(file,
                    (e.getKey() + ": " + e.getValue().toString() + newLine), true);
        }
    }

    public static void outputToFiles(GameIdentity game, OutputData data) {
        Date now = new Date();

        File goalsFile = new File(now.getTime() + "-goals.txt");
        File performancesFile = new File(now.getTime() + "-performances.txt");

        String gameScore = "RED - " + game.redTeamName + "  " + data.redGoals.size() + ":"
                + data.blueGoals.size() + "  " + " - BLUE";

        try {
            FileUtils.writeStringToFile(performancesFile, gameScore + newLine, true);
            FileUtils.writeStringToFile(goalsFile, gameScore + newLine, true);

            writeGoalsToFile(goalsFile, data);

            writePerformancesToFile(performancesFile, data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void upload(GameIdentity game, OutputData data) {
        StatsUploader.upload(game, data.goals());
    }

}
