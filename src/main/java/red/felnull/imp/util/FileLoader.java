package red.felnull.imp.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import red.felnull.imp.IamMusicPlayer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class FileLoader {
    public static void createFolder(Path path) {
        path.toFile().mkdirs();
    }

    public static byte[] fileBytesReader(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            IamMusicPlayer.LOGGER.error("Failed to Read the File : " + e.getLocalizedMessage());
            return null;
        }
    }

    public static void fileBytesWriter(byte[] bytedatas, Path path) {
        createFolder(path.getParent());
        try {
            Files.write(path, bytedatas);
        } catch (IOException e) {
            IamMusicPlayer.LOGGER.error("Failed to Write the File : " + e.getLocalizedMessage());
        }
    }

    public static void deleteFile(File file) {
        file.delete();
    }

    public static void fileNBTWriter(CompoundNBT nbt, Path path) {
        createFolder(path.getParent());
        try {
            FileOutputStream stream = new FileOutputStream(path.toFile());
            nbt.putString("Timestamp", StringHelper.getTimeStamp());
            CompressedStreamTools.writeCompressed(nbt, stream);
        } catch (FileNotFoundException e) {
            IamMusicPlayer.LOGGER.error("Failed to Write the NBT File : " + e.getLocalizedMessage());
        } catch (IOException e) {
            IamMusicPlayer.LOGGER.error("Failed to Write the NBT File : " + e.getLocalizedMessage());
        }
    }

    public static CompoundNBT fileNBTReader(Path path) {
        FileInputStream stream;

        if (!path.toFile().exists())
            return new CompoundNBT();

        try {
            stream = new FileInputStream(path.toFile());
            return CompressedStreamTools.readCompressed(stream);
        } catch (FileNotFoundException e) {
            IamMusicPlayer.LOGGER.error("Failed to Read the NBT File : " + e.getLocalizedMessage());
        } catch (IOException e) {
            IamMusicPlayer.LOGGER.error("Failed to Read the NBT File : " + e.getLocalizedMessage());
        }
        return null;
    }

    public static void txtWriter(Map<String, String> map, Path path) {
        createFolder(path.getParent());
        try {
            FileWriter fw = new FileWriter(path.toString(), false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            map.entrySet().forEach(maps -> {
                pw.println(maps.getKey() + ":" + maps.getValue());
            });

            pw.close();
        } catch (IOException e) {

        }
    }

    public static void txtReader(Map<String, String> map, Path path) {
        map.clear();
        try {
            FileReader re = new FileReader(path.toString());
            BufferedReader bre = new BufferedReader(re);
            String st;
            while ((st = bre.readLine()) != null) {
                try {
                    String[] fruit = st.split(":", 0);
                    map.put(fruit[0], fruit[1]);
                } catch (Exception e) {

                }
            }

        } catch (IOException e) {

        }

    }
}
