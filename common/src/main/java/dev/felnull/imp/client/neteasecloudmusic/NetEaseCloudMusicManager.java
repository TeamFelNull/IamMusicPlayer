package dev.felnull.imp.client.neteasecloudmusic;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.fnjl.util.FNURLUtil;
import dev.felnull.imp.IamMusicPlayer;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * https://github.com/Binaryify/NeteaseCloudMusicApi
 */
public class NetEaseCloudMusicManager {
    private static final NetEaseCloudMusicManager INSTANCE = new NetEaseCloudMusicManager();
    private static final String BUILD_IN_API_URL = "https://api.felnull.dev/netease-cloud-music-api/";
    private static final String API_VERSION = "1";
    private static final Gson GSON = new Gson();
    private final AtomicBoolean init = new AtomicBoolean();
    private String apiURL = null;

    public static NetEaseCloudMusicManager getInstance() {
        return INSTANCE;
    }

    public void reload() {
        init.set(false);
    }

    private void init() {
        synchronized (init) {
            if (!init.compareAndSet(false, true))
                return;

            String curl = IamMusicPlayer.CONFIG.neteaseCloudMusicApiURL;

            try {
                var ret = jsonCheck(curl);
                if (ret != null)
                    curl = ret;
            } catch (Exception ignored) {
            }

            try {
                FNURLUtil.getResponse(new URL(curl));
            } catch (IOException e) {
                curl = BUILD_IN_API_URL;
            }

            apiURL = curl;
        }
    }

    private String jsonCheck(String url) throws Exception {
        JsonObject jo;
        try (InputStream stream = FNURLUtil.getStream(new URL(url)); InputStream bufStream = new BufferedInputStream(stream); Reader reader = new InputStreamReader(bufStream)) {
            jo = GSON.fromJson(reader, JsonObject.class);
        }
        var jao = jo.getAsJsonObject("netease_cloud_music_api");
        var jaeo = jao.get(API_VERSION);
        if (jaeo == null || !jaeo.isJsonPrimitive() || !jaeo.getAsJsonPrimitive().isString())
            return null;
        return jaeo.getAsString();
    }

    public String getMp3Url(String songId) throws IOException {
        init();
        String urlStr = FNStringUtil.urlConcatenation(apiURL, "/song/url?id=" + songId);
        JsonObject jo;
        try (InputStream stream = FNURLUtil.getStream(new URL(urlStr)); InputStream bufStream = new BufferedInputStream(stream); Reader reader = new InputStreamReader(bufStream)) {
            jo = GSON.fromJson(reader, JsonObject.class);
        }
        var data = jo.getAsJsonArray("data");
        if (data.isEmpty())
            return null;
        var entry = data.get(0).getAsJsonObject();
        return entry.get("url").getAsString();
    }

    public JsonObject getSongJson(String songId) throws IOException {
        init();
        var urlStr = FNStringUtil.urlConcatenation(apiURL, "/song/detail?ids=" + songId);
        JsonObject jo;
        try (InputStream stream = FNURLUtil.getStream(new URL(urlStr)); InputStream bufStream = new BufferedInputStream(stream); Reader reader = new InputStreamReader(bufStream)) {
            jo = GSON.fromJson(reader, JsonObject.class);
        }
        var songs = jo.getAsJsonArray("songs");
        if (songs.isEmpty())
            throw new IllegalArgumentException("Song not found");
        return songs.get(0).getAsJsonObject();
    }

    public String getPictureURL(JsonObject songJson) {
        var ajo = songJson.getAsJsonObject("al");
        return ajo.get("picUrl").getAsString();
    }

    public Pair<String, List<String>> getNameAndArtist(JsonObject songJson) {
        var aljo = songJson.getAsJsonObject("al");
        var name = aljo.get("name").getAsString();

        List<String> artists = new ArrayList<>();
        var arjo = songJson.getAsJsonArray("ar");
        for (JsonElement jo : arjo) {
            artists.add(jo.getAsJsonObject().get("name").getAsString());
        }
        return Pair.of(name, artists);
    }

    public List<JsonObject> getSearchSongs(String text) throws IOException, URISyntaxException {
        init();

        text = URLEncoder.encode(text, StandardCharsets.UTF_8);
        text = new URI(text).toASCIIString();

        var urlStr = FNStringUtil.urlConcatenation(apiURL, "/cloudsearch?keywords=" + text);

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
