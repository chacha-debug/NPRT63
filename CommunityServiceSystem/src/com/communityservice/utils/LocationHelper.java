package com.communityservice.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Optional approximate city lookup, invoked only when the user presses Use My Location. */
public final class LocationHelper {
    private LocationHelper() {}

    public static String findApproximateCity() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new java.net.URI("https://ipapi.co/json/").toURL().openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestProperty("User-Agent", "CommunityServicePlus/1.0");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) json.append(line);
            String city = value(json.toString(), "city");
            String region = value(json.toString(), "region");
            if (city.isEmpty()) throw new IllegalStateException("Location unavailable");
            return region.isEmpty() ? city : city + ", " + region;
        } finally { connection.disconnect(); }
    }

    private static String value(String json, String key) {
        Matcher m = Pattern.compile("\\\"" + key + "\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"").matcher(json);
        return m.find() ? m.group(1) : "";
    }
}