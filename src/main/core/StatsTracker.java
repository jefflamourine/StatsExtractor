package main.core;

public class StatsTracker {

    enum State {
        WAITING, TRACKING, OUTPUTTING
    }

    static State state = State.WAITING;

    static GameIdentity game;

    static OutputData outputData;

    static GameState currentState, previousState;

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
            System.out.println("Valid verified-game start command");
            game = new GameIdentity(args[1], args[2], args[3]);
            if (StatsUploader.verifyGame(game)) {
                System.out.println("Tracking: " + game.redTeamName + " vs. " + game.blueTeamName + " " + game.date.toString());
                initializeGame();
                state = State.TRACKING;
            }
        } else if (args[0].equals("start") && args.length == 3) {
            System.out.println("Valid non-verified-game start command w/ team names");
            game = new GameIdentity(args[1], args[2], null);
            initializeGame();
        } else if (args[0].equals("start") && args.length == 1) {
            System.out.println("Valid non-verified-game start command w/o team names");
            game = new GameIdentity("Red", "Blue", null);
            initializeGame();
        }
    }

    public static void initializeGame() {
        currentState = new GameState();
        previousState = new GameState(currentState);
        outputData = new OutputData();

    }

    public static void main(String... args) {
        StatsExtractor.init();
        StatsUploader.init();

        MemoryReadTimer timer = new MemoryReadTimer();

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
                    OutputHandler.outputToFiles(game, outputData);
                    OutputHandler.upload(game, outputData);
                    break;
                }
            }
        }
    }
}