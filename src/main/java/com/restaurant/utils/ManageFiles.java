package com.restaurant.utils;

import com.restaurant.entity.Icon;
import com.restaurant.entity.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class ManageFiles {

    private static final String IMAGE_PATH = "/img/";

    public static Photo createPhoto(String url) {
        Photo photo = new Photo();
        photo.setUrl(url.substring(url.lastIndexOf("/")+1));
        photo.setPath(url);
        return photo;
    }

    public static Icon createIcon(String url) {
        Icon icon = new Icon();
        icon.setUrl(url.substring(url.lastIndexOf("/")+1));
        icon.setPath(url);
        return icon;
    }

    private static String getHash(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = file.getInputStream()){
            DigestInputStream dis = new DigestInputStream(is, md);

            /* Read decorated stream (dis) to EOF as normal... */
        }
        byte[] digest = md.digest();
        return String.format("%032x", new BigInteger(1, digest));
    }

    public static String saveFile(MultipartFile inputFile, ResizeImage.Size size) throws IOException, NoSuchAlgorithmException {
        String hash = getHash(inputFile);
        System.out.println(hash);
        String path = IMAGE_PATH + hash.substring(0,2) + "/" + hash.substring(2,4) + "/" + hash.substring(4,6);
        File file = new File(path);
        file.mkdirs();
//        file.createNewFile();
//        System.out.println(file.getPath());
        String uuid = UUID.randomUUID().toString();
        Files.write(Paths.get(path + "/" + uuid), ResizeImage.resize(inputFile.getInputStream(), size, inputFile.getContentType()));
        return path + "/" + uuid;
    }

    public static void deleteFile(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            return;
        }
        Path path1 = Paths.get(path);
        if (!path1.toFile().exists()) return;
        Files.delete(path1);
    }
}
