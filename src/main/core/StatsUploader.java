package main.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class StatsUploader {

    private static final String SUBMIT_URL = "https://hockey-jefflamourine.rhcloud.com/try-submit-goals";
    private static final String VERIFY_URL = "https://hockey-jefflamourine.rhcloud.com/verify-game";

    private static URL submitURL, verifyURL;

    public static void init() {
        try {
            submitURL = new URL(SUBMIT_URL);
            verifyURL = new URL(VERIFY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static HttpsURLConnection createConnection(URL url) {
        HttpsURLConnection con = null;
        try {
            con = (HttpsURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("POST");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static boolean verifyGame(GameIdentity game) {
        HttpsURLConnection con = createConnection(verifyURL);
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("red", game.redTeamName);
        jsonPayload.put("blue", game.blueTeamName);
        jsonPayload.put("date", game.date.toString());

        try {
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(jsonPayload.toString());
            osw.flush();
            int httpResult = con.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),
                        "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                if (sb.toString().equals("1")) {
                    return true;
                }
            } else {
                System.out.println(con.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void upload(GameIdentity game, ArrayList<Goal> goals) {
        HttpsURLConnection con = createConnection(submitURL);

        JSONObject jsonPayload = convertToPayload(game, goals);

        try {
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(jsonPayload.toString());
            osw.flush();
            int httpResult = con.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),
                        "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                System.out.println(sb.toString());
            } else {
                System.out.println(con.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject convertToPayload(GameIdentity game, ArrayList<Goal> goals) {
        JSONObject json = new JSONObject();
        json.put("red", game.redTeamName);
        json.put("blue", game.blueTeamName);
        json.put("date", game.date.toString());
        JSONArray goalsJson = new JSONArray();
        for (Goal g : goals) {
            goalsJson.put(g.toJson());
        }
        json.put("goals", goalsJson);
        System.out.println(json.toString());
        return json;
    }
}
