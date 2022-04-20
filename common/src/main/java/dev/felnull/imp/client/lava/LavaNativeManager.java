package dev.felnull.imp.client.lava;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.felnull.fnjl.util.FNDataUtil;
import dev.felnull.fnjl.util.FNURLUtil;
import dev.felnull.imp.IamMusicPlayer;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

public class LavaNativeManager {
    private static final Logger LOGGER = LogManager.getLogger(LavaNativeManager.class);
    private static final Gson GSON = new Gson();
    private static final LavaNativeManager INSTANCE = new LavaNativeManager();
    private static final String nativesVersion = "1";

    public static LavaNativeManager getInstance() {
        return INSTANCE;
    }

    public boolean load(String osAndArch, String name) {
        var npF = LavaPlayerLoader.getNaiveLibraryFolder().resolve(osAndArch).toFile();
        if (!checked(npF)) {
            try {
                LOGGER.info("LavaPlayer natives download start");
                downloadNatives(osAndArch);
                LOGGER.info("LavaPlayer natives download successful");
            } catch (Exception e) {
                LOGGER.error("LavaPlayer natives download failed", e);
                return false;
            }
        }
        LOGGER.info("LavaPlayer native(" + name + ") check successful");

        return npF.toPath().resolve(name).toFile().exists();
    }

    private void downloadNatives(String osAndArch) throws Exception {
        var npF = LavaPlayerLoader.getNaiveLibraryFolder().resolve(osAndArch).toFile();
        if (!npF.exists() && !npF.mkdirs())
            throw new IllegalStateException("Failed to create the folder of the native library");

        JsonObject jo;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FNURLUtil.getStream(new URL(IamMusicPlayer.CONFIG.lavaPlayerNativesURL))))) {
            jo = GSON.fromJson(reader, JsonObject.class);
        }

        if (!jo.has(nativesVersion) || !jo.get(nativesVersion).isJsonObject())
            throw new IllegalStateException("Could not find version of native library to support");

        var joo = jo.getAsJsonObject(nativesVersion);

        if (!joo.has(osAndArch) || !joo.get(osAndArch).isJsonObject())
            throw new IllegalStateException("Unsupported OS or architecture");

        var jooo = joo.getAsJsonObject(osAndArch);

        if (!jooo.has("hash"))
            throw new IllegalStateException("The hash value was not found");

        if (!jooo.has("url"))
            throw new IllegalStateException("Native library URL not found");

        var ho = new JsonObject();
        ho.add("hash", jooo.get("hash"));

        Files.writeString(npF.toPath().resolve("hash.json"), GSON.toJson(ho));

        FNDataUtil.readZipStreamed(new BufferedInputStream(FNURLUtil.getStream(new URL(jooo.get("url").getAsString()))), (zipEntry, inputStream) -> {
            var fl = npF.toPath().resolve(zipEntry.getName()).toFile();
            try (InputStream is = inputStream; OutputStream os = new FileOutputStream(fl)) {
                FNDataUtil.bufInputToOutput(is, os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        if (!checked(npF))
            throw new IllegalStateException("Consistency check failed");
    }

    private boolean checked(File file) {
        var fs = file.listFiles();
        if (fs == null) return false;
        var fls = Lists.newArrayList(fs);
        var hf = fls.stream().filter(n -> n.getName().equals("hash.json")).findAny();
        if (hf.isEmpty()) return false;
        JsonObject jo;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(hf.get())))) {
            jo = GSON.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            return false;
        }
        if (!jo.has("hash") || fls.size() <= 1)
            return false;
        fls.remove(hf.get());

        if (fls.size() == 1 && !jo.get("hash").isJsonObject()) {
            try {
                var ch = jo.get("hash").getAsString();
                var th = new String(Hex.encodeHex(FNDataUtil.createMD5Hash(Files.readAllBytes(fls.get(0).toPath()))));
                return th.equals(ch);
            } catch (Exception ignored) {
            }
        } else if (jo.get("hash").isJsonObject() && jo.get("hash").getAsJsonObject().size() == fls.size()) {
            var hjo = jo.get("hash").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : hjo.entrySet()) {
                var lf = fls.stream().filter(n -> n.getName().equals(entry.getKey())).findAny();
                if (lf.isEmpty()) return false;
                try {
                    var ch = entry.getValue().getAsString();
                    var th = new String(Hex.encodeHex(FNDataUtil.createMD5Hash(Files.readAllBytes(lf.get().toPath()))));
                    if (!th.equals(ch)) return false;
                } catch (Exception ignored) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}