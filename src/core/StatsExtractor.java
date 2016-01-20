package core;
import java.util.ArrayList;

import sys.MemoryExtractor;

public class StatsExtractor {

    private static long TIME = 0x7D349A8L;
    private static long PERIOD = 0x7D349B0L;
    private static long RED_SCORE = 0x7D33D98L;
    private static long BLUE_SCORE = 0x7D33D9CL;
    private static long PLAYER_STRUCT = 0x04A5B860L;
    private static int PLAYER_POSITION_OFFSET = 4;
    private static int PLAYER_TEAM_OFFSET = 8;
    private static int PLAYER_NAME_OFFSET = 20;
    private static int PLAYER_GOALS_OFFSET = 136;
    private static int PLAYER_ASSISTS_OFFSET = 140;
    private static int PLAYER_FULL_OFFSET = 152;

    private static long LAST_CHAT = 0x044968C0;
    private static long CHAT_STRUCT = 0x07126363L;
    private static int CHAT_MESSAGE_OFFSET = 148;

    public static void init() {
        MemoryExtractor.init();
    }

    public static int getTime() {
        return MemoryExtractor.readMemory(TIME, 4).getInt(0);
    }

    public static int getPeriod() {
        return MemoryExtractor.readMemory(PERIOD, 4).getInt(0);
    }

    public static int getRedScore() {
        return MemoryExtractor.readMemory(RED_SCORE, 4).getInt(0);
    }

    public static int getBlueScore() {
        return MemoryExtractor.readMemory(BLUE_SCORE, 4).getInt(0);
    }

    public static String getName(long fromMemLoc) {
        return MemoryExtractor.readMemory(fromMemLoc + PLAYER_NAME_OFFSET, 24).getString(0);
    }

    public static int getPos(long fromMemLoc) {
        return MemoryExtractor.readMemory(fromMemLoc + PLAYER_POSITION_OFFSET, 4).getInt(0);
    }

    public static int getGoals(long fromMemLoc) {
        return MemoryExtractor.readMemory(fromMemLoc + PLAYER_GOALS_OFFSET, 4).getInt(0);
    }

    public static int getAssists(long fromMemLoc) {
        return MemoryExtractor.readMemory(fromMemLoc + PLAYER_ASSISTS_OFFSET, 4).getInt(0);
    }

    public static int getTeam(long fromMemLoc) {
        return MemoryExtractor.readMemory(fromMemLoc + PLAYER_TEAM_OFFSET, 4).getInt(0);
    }

    public static ArrayList<GamePlayerStruct> getPlayers() {
        return getPlayers(PLAYER_STRUCT, new ArrayList<GamePlayerStruct>());
    }

    public static ArrayList<GamePlayerStruct> getPlayers(long fromMemLoc, ArrayList<GamePlayerStruct> players) {
        String name = getName(fromMemLoc);

        if (name.isEmpty()) {
            return players;
        }

        int team = getTeam(fromMemLoc);
        int goals = getGoals(fromMemLoc);
        int assists = getAssists(fromMemLoc);
        int pos = getPos(fromMemLoc);

        players.add(new GamePlayerStruct(team, name, goals, assists, pos));
        return getPlayers(fromMemLoc + PLAYER_FULL_OFFSET, players);
    }

    public static ArrayList<String> getChatMessages() {
        ArrayList<String> chatMessages = new ArrayList<String>();

        for (int i = 0; i < 8; i++) {
            String message = MemoryExtractor.readMemory(CHAT_STRUCT + i * CHAT_MESSAGE_OFFSET, CHAT_MESSAGE_OFFSET).getString(0);
            chatMessages.add(message);
        }

        return chatMessages;
    }

    public static String getLastChatMessage() {
        return trimName(MemoryExtractor.readMemory(LAST_CHAT, CHAT_MESSAGE_OFFSET).getString(0));
    }

    private static String trimName(String message) {
        String result = "";

        if (message.contains(":") && message.length() > message.indexOf(":") + 2) {
            result = message.substring(message.indexOf(":") + 2);
        }

        return result;
    }
}
