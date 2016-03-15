package main.core;

public class StatsTracker {

    enum State {
        WAITING, TRACKING, OUTPUTTING
    }

    // Initial state is waiting
    static State state = State.WAITING;

    static GameIdentity game;
    static OutputData outputData;

    // Track the state of the game in the previous tick and the current tick
    static HQMGameState currentState, previousState;

    /**
     * If chat string starts with command prefix,
     * Convert into an argv/argc style array, splitting on spaces.
     * @return [command, arg1, arg2, ... ]
     */
    static String[] detectCommands(String message) {
        String[] args = {};
        if (message.length() >= 4 && message.substring(0, 4).equals("!se ")) {
            args = message.substring(4).split(" ");
            System.out.println("Command detected: " + args[0]);
        }
        return args;
    }

    /**
     * Identifies the game (should verify it) and starts tracking it.
     * @param args An array with a command and set of args
     */
    static void handleCommands(String[] args) {
        String command;
        int argc = args.length;
        if (argc == 0) {
            return;
        } else {
            command = args[0];
        }
        if (command.equals("start")) {
            if (argc == 4) {
                // Start command like: '!se start BOS PHI MMDDYY' can be verified
                System.out.println("Valid verified-game start command");
                game = new GameIdentity(args[1], args[2], args[3]);
                System.out.println("Tracking: " + game.redTeamName + " vs. " + game.blueTeamName + " " + game.date.toString());
            } else if (argc == 3) {
                // Start command like: '!se start BOS PHI' can't be verified
                System.out.println("Valid non-verified-game start command w/ team names");
                game = new GameIdentity(args[1], args[2], null);
                System.out.println("Tracking: " + game.redTeamName + " vs. " + game.blueTeamName);
            } else if (argc == 1) {
                // Start command like: '!se start' can't be verified
                System.out.println("Valid non-verified-game start command w/o team names");
                game = new GameIdentity("Red", "Blue", null);
                System.out.println("Tracking unspecified game");
            } else {
                return;
            }

            initializeGame();
            state = State.TRACKING;
        }
    }

    /**
     * Initializes/resets the game states and output data
     */
    public static void initializeGame() {
        currentState = new HQMGameState();
        previousState = new HQMGameState(currentState);
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

                previousState = new HQMGameState(currentState);

                if (currentState.endOfGame()) {
                    outputData.addGoalsToPerformances();
                    OutputHandler.outputToFiles(game, outputData);
                    OutputHandler.upload(game, outputData);
                    state = State.WAITING;
                }
            }
        }
    }
}