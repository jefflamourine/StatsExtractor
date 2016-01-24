package main.core;

public class StatsTracker {

    enum State {
        WAITING, TRACKING, OUTPUTTING
    }

    static State state = State.WAITING;

    static GameIdentity game;

    static OutputData outputData;

    static GameState currentState, previousState;

    //-- StatsUploader: has state after verifyGame() that uploads to that game --//
    static StatsUploader uploader = new StatsUploader();

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
            game = new GameIdentity(args[1], args[2], args[3]);
            if (uploader.verifyGame(game)) {
                System.out.println("Tracking: " + game.redTeamName + " vs. " + game.blueTeamName + " " + game.date.toString());
                state = State.TRACKING;
            }
        }
    }

    public static void main(String... args) {
        StatsExtractor.init();

        MemoryReadTimer timer = new MemoryReadTimer();

        currentState =  new GameState();
        previousState = new GameState(currentState);

        while (true) {
            if (state == State.WAITING) {
                handleCommands(detectCommands(StatsExtractor.getLastChatMessage()));
            } else if (timer.timeForNextRead()) {

                currentState.update();

                if (previousState.time != currentState.time) {
                    outputData.incrementTimeOnIce(currentState.players);
                }

                // If score has changed 
                if (currentState.redScore != previousState.redScore) {
                    outputData.addGoal(0, currentState.time, currentState.period, currentState.players, previousState.players);
                }

                if (currentState.blueScore != previousState.blueScore) {
                    outputData.addGoal(1, currentState.time, currentState.period, currentState.players, previousState.players);
                }

                previousState = new GameState(currentState);

                if (currentState.endOfGame()) {
                    outputData.addGoalsToPerformances();
                    outputData.outputToFile();
                    uploader.upload(outputData.goals());
                    break;
                }
            }
        }
    }
}