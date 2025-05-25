package com.example.medisyncfrontend.Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:9090";

    /* ---------- GENERIC POST (JSON body) ---------- */
    public static String post(String endpoint, String jsonBody) {
        return sendWithBody("POST", endpoint, jsonBody);
    }

    /* ---------- GENERIC PUT (JSON body)  ---------- */
    public static String put(String endpoint, String jsonBody) {
        return sendWithBody("PUT", endpoint, jsonBody);
    }

    /* ---------- GENERIC DELETE (no body) ---------- */
    public static int delete(String endpoint) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            return conn.getResponseCode();                 // 200-299 = ok
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /* ---------- LOGIN helper that builds JSON from a Map ---------- */
    public static boolean postLogin(String endpoint, Map<String, String> body) {
        String json = mapToJson(body);
        int code = sendWithBody("POST", endpoint, json, false /*returnBody*/);
        return code == 200;
    }

    /* ---------- GET ---------- */
    public static String get(String endpoint) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ====================================================================== */
    /* =============  INTERNAL SHARED IMPLEMENTATION  ======================= */
    /* ====================================================================== */

    private static String sendWithBody(String method, String endpoint,
                                       String jsonBody) {
        int code = sendWithBody(method, endpoint, jsonBody, true);
        return (code >= 200 && code < 300) ? lastResponseBuffer.get() : null;
    }

    // thread-local buffer so GET/POST helpers can return body *or* just code
    private static final ThreadLocal<String> lastResponseBuffer = new ThreadLocal<>();

    private static int sendWithBody(String method, String endpoint,
                                    String jsonBody, boolean captureBody) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (captureBody && responseCode >= 200 && responseCode < 300) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                    lastResponseBuffer.set(sb.toString());
                }
            }
            return responseCode;

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    /* ---------- tiny helper to build JSON from Map ---------- */
    private static String mapToJson(Map<String, String> map) {
        StringBuilder json = new StringBuilder("{");
        map.forEach((k, v) -> json.append("\"").append(k).append("\":")
                .append("\"").append(v).append("\","));
        if (json.charAt(json.length() - 1) == ',') json.deleteCharAt(json.length() - 1);
        json.append("}");
        return json.toString();
    }
}
