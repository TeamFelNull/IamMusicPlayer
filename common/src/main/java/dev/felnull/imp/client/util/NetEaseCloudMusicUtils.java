package dev.felnull.imp.client.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.felnull.fnjl.util.FNURLUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/Binaryify/NeteaseCloudMusicApi
 */
public class NetEaseCloudMusicUtils {
    private static final Gson GSON = new Gson();
    private static final String API_URL = "https://www.morimori0317.net/netease-cloud-music-api";

    public static String getMp3Url(String songId) throws IOException {
        String urlStr = API_URL + "/song/url?id=" + songId;
        JsonObject jo;
        try (InputStream stream = FNURLUtil.getStream(new URL(urlStr)); InputStream bufStream = new BufferedInputStream(stream); Reader reader = new InputStreamReader(bufStream)) {
            jo = GSON.fromJson(reader, JsonObject.class);
        }
        var data = jo.getAsJsonArray("data");
        var entry = data.get(0).getAsJsonObject();
        return entry.get("url").getAsString();
    }

    public static JsonObject getSongJson(String songId) throws IOException {
        var urlStr = API_URL + "/song/detail?ids=" + songId;
        JsonObject jo;
        try (InputStream stream = FNURLUtil.getStream(new URL(urlStr)); InputStream bufStream = new BufferedInputStream(stream); Reader reader = new InputStreamReader(bufStream)) {
            jo = GSON.fromJson(reader, JsonObject.class);
        }
        var songs = jo.getAsJsonArray("songs");
        if (songs.isEmpty())
            throw new IllegalArgumentException("Song not found");
        return songs.get(0).getAsJsonObject();
    }

    public static String getPictureURL(JsonObject songJson) {
        var ajo = songJson.getAsJsonObject("al");
        return ajo.get("picUrl").getAsString();
    }

    public static Pair<String, List<String>> getNameAndArtist(JsonObject songJson) {
        var aljo = songJson.getAsJsonObject("al");
        var name = aljo.get("name").getAsString();

        List<String> artists = new ArrayList<>();
        var arjo = songJson.getAsJsonArray("ar");
        for (JsonElement jo : arjo) {
            artists.add(jo.getAsJsonObject().get("name").getAsString());
        }
        return Pair.of(name, artists);
    }

    public static List<JsonObject> getSearchSongs(String text) throws IOException, URISyntaxException {
        text = URLEncoder.encode(text, StandardCharsets.UTF_8);
        text = new URI(text).toASCIIString();

        var urlStr = API_URL + "/cloudsearch?keywords=" + text;

        JsonObject jo;
        try (InputStream stream = FNURLUtil.getStream(new URL(urlStr)); InputStream bufStream = new BufferedInputStream(stream); Reader reader = new InputStreamReader(bufStream)) {
            jo = GSON.fromJson(reader, JsonObject.class);
        }
        var rj = jo.getAsJsonObject("result");
        if (rj == null)
            return new ArrayList<>();

        var sj = rj.getAsJsonArray("songs");

        if (sj == null)
            return new ArrayList<>();

        List<JsonObject> ret = new ArrayList<>();

        for (JsonElement je : sj) {
            ret.add(je.getAsJsonObject());
        }

        return ret;
    }
}
