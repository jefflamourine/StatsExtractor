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

    private String redTeamName, blueTeamName;
    GameDate date;
    private static URL submitURL, verifyURL;

    public StatsUploader() {
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

    public boolean verifyGame(String redTeamName, String blueTeamName, GameDate date) {
        HttpsURLConnection con = createConnection(verifyURL);
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("red", redTeamName);
        jsonPayload.put("blue", blueTeamName);
        jsonPayload.put("date", date.toString());

        try {
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(jsonPayload.toString());
            osw.flush();
            int httpResult = con.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                if (sb.toString().equals("1")) {
                    this.redTeamName = redTeamName;
                    this.blueTeamName = blueTeamName;
                    this.date = date;
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

    public void upload(ArrayList<Goal> goals) {
        HttpsURLConnection con = createConnection(submitURL);

        JSONObject jsonPayload = convertToPayload(goals);

        try {
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(jsonPayload.toString());
            osw.flush();
            int httpResult = con.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                System.out.println(sb.toString());
            } else {
                System.out.println(con.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject convertToPayload(ArrayList<Goal> goals) {
        JSONObject json = new JSONObject();
        json.put("red", redTeamName);
        json.put("blue", blueTeamName);
        json.put("date", date.toString());
        JSONArray goalsJson = new JSONArray();
        for (Goal g : goals) {
            goalsJson.put(g.toJson());
        }
        json.put("goals", goalsJson);
        System.out.println(json.toString());
        return json;
    }

    public static void main(String[] args) {
        StatsUploader su = new StatsUploader();
        System.out.println(su.verifyGame("ATL", "LAK", new GameDate(15, 5, 17, 19, 30)));
    }
}
