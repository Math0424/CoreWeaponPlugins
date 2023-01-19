package me.Math0424.Withered.Files;

import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileManager {

    public static void copyFiles(File source, File destination) {
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.lock", "playerData.yml"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!destination.exists())
                        destination.mkdirs();
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(destination, file);
                        copyFiles(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(destination);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    WitheredUtil.debug("Copied file " + source.getName() + " to " + destination.getName());
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Error while copying files. thats not good...");
        }
    }

    public static void copyInternalFiles(String jarSourceLocation) {
        copyInternalFiles(jarSourceLocation, false);
    }

    public static void copyInternalFiles(String jarSourceLocation, boolean replace) {
        try {
            String resourcePath = new File(Withered.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            JarFile jf = new JarFile(resourcePath);
            Enumeration<JarEntry> entries = jf.entries();
            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();
                if (je.getName().startsWith(jarSourceLocation) && !je.getName().equals(jarSourceLocation + "/")) {
                    File outFile = new File(Withered.getPlugin().getDataFolder(), je.getName());
                    if (replace) {
                        deleteFile(outFile);
                    }
                    if (!outFile.exists()) {
                        if (outFile.isDirectory()) {
                            WitheredUtil.debug("Loaded directory " + je.getName());
                            copyInternalFiles(jarSourceLocation);
                        } else {
                            Withered.getPlugin().saveResource(je.getName(), false);
                            WitheredUtil.debug("Loaded file " + je.getName());
                        }
                    }
                }
            }
            jf.close();
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Error while copying internal files. thats not good...");
        }
    }

    public static boolean deleteFile(File toDelete) {
        if (toDelete != null) {
            if (toDelete.exists()) {
                if (toDelete.listFiles() != null) {
                    File[] files = toDelete.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isDirectory()) {
                            deleteFile(files[i]);
                        } else {
                            if (files[i].delete()) {
                                WitheredUtil.debug("Deleted file " + files[i].getName());
                            } else {
                                WitheredUtil.debug("Couldnt delete file " + files[i].getName());
                            }
                        }
                    }
                }
                return toDelete.delete();
            }
        }
        return false;
    }

    public static FileConfiguration loadConfiguration(String fileName, String dataFolder) {
        File file = getFileInDataFolder(fileName, dataFolder);
        WitheredUtil.debug("Loading file " + dataFolder + fileName);

        if (!file.exists()) {
            WitheredUtil.debug("Created new file " + dataFolder + fileName);
            Withered.getPlugin().saveResource(dataFolder + fileName, false);
        }
        FileConfiguration fileToReplace = new YamlConfiguration();
        try {
            fileToReplace.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return fileToReplace;
    }

    public static void loadMassConfigurations(String dataFolder) {
        File file = new File(Withered.getPlugin().getDataFolder(), dataFolder);
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    loadMassConfigurations(dataFolder + f.getName());
                } else {
                    FileConfiguration tempConfig = new YamlConfiguration();
                    try {
                        tempConfig.load(f);
                    } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    public static void unzip(File zipFilePath, File destDir) {
        try {
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDir + "/" + entry.getName();
                WitheredUtil.debug("Unzipped file " + entry.getName());
                if (!entry.isDirectory()) {
                    extractZipData(zipIn, filePath);
                } else {
                    new File(filePath).mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to unzip file.");
        }
    }

    private static void extractZipData(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public static File getFileInDataFolder(String fileName, String dataFolder) {
        return new File(Withered.getPlugin().getDataFolder(), dataFolder + fileName);
    }


}
