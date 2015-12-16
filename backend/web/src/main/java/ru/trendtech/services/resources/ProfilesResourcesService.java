package ru.trendtech.services.resources;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import net.iharder.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.DriverCarPhotosInfo;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.nio.file.StandardOpenOption.*;

@Service
public class ProfilesResourcesService {
    private static final ContentInfoUtil CONTENT_INFO_UTIL = new ContentInfoUtil();

    private static final String DRIVER_PHOTO_PREFIX = "driver";

    private static final String DRIVER_PHOTO_BY_VERSION_APP_PREFIX = "driver_ver";

    private static final String CLIENT_PHOTO_PREFIX = "client";

    private static final String AUTO_PHOTO_BY_VERSION_APP_PREFIX = "auto_ver";

    private static final String AUTO_PHOTO_PREFIX = "auto";

    private static final String EVENT_PHOTO_PREFIX = "event";

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilesResourcesService.class);

    @Value("${profiles.path}")
    private String profilesFoldersPath;

    @Value("${profiles.url}")
    private String profilesUrlPath;

    private Path rootPath;

    public ProfilesResourcesService() {
    }

    private static String[] resolveExtensions(byte[] decode) throws IOException {
        ContentInfo match = CONTENT_INFO_UTIL.findMatch(new ByteArrayInputStream(decode));
        return match.getFileExtensions();
    }

    private static String buildClientPhotoFileName(long clientId, String ext) {
        return CLIENT_PHOTO_PREFIX + "_" + clientId + "." + ext;
    }

    private static String buildClientPhotoFileNameUpdate(long clientId, String ext) {
        return CLIENT_PHOTO_PREFIX + "_" + clientId + "_" + generateNumber() + "." + ext;
    }

    private static String buildAutoPhotoFileName(long driverId, int photoIdx, String ext, boolean clientVersionAppSupport) {
        if(clientVersionAppSupport){
            return AUTO_PHOTO_BY_VERSION_APP_PREFIX + "_" + driverId + "_" + photoIdx + "." + ext;
        }else{
            return AUTO_PHOTO_PREFIX + "_" + driverId + "_" + photoIdx + "." + ext;
        }
    }

    private static String buildEventPhotoFileName(long partnerId, int photoIdx, String ext) {
        return EVENT_PHOTO_PREFIX + "_" + partnerId + "_" + photoIdx + "." + ext;
    }

    private static String buildDriverPhotoFileName(long driverId, String ext, boolean clientVersionAppSupport) {
        if(clientVersionAppSupport){
            return DRIVER_PHOTO_BY_VERSION_APP_PREFIX + "_" + driverId + "." + ext;
        }else{
            return DRIVER_PHOTO_PREFIX + "_" + driverId + "." + ext;
        }
    }


    private static String buildServicePhotoFileName(String ext, String name) {
        return name+"." + ext;
    }


    private static int generateNumber(){
        int result= 10000;
         Random rand = new Random();
          result = rand.nextInt(result+1);
           return result;
    }



    @PostConstruct
    private void init() {
        rootPath = Paths.get(profilesFoldersPath);
        if (!rootPath.toFile().exists()) {
            try {
                Files.createDirectories(rootPath);
            } catch (IOException e) {
                LOGGER.error("problem on service initialization", e);
            }
        }
        if (!Files.isDirectory(rootPath)) {
            throw new RuntimeException("wrong path provided: " + rootPath.toString());
        }
    }

    public String saveClientPicture(String picure, long clientId, boolean isUpdate) {
        String result = null;
        try {
            byte[] decode = Base64.decode(picure);
            String[] extensions = resolveExtensions(decode);
            result = saveClientPhoto(clientId, decode, extensions[0], isUpdate);
        } catch (IOException e) {
            LOGGER.error("problem saving client picture", e);
        }
        return result;
    }





    public String saveDriverPicture(String picure, long driverId, boolean clientVersionAppSupport) {
        String result = null;
        try {
            byte[] decode = Base64.decode(picure);
            String[] extensions = resolveExtensions(decode);
            result = saveDriverPhoto(driverId, decode, extensions[0], clientVersionAppSupport);
        } catch (IOException e) {
            LOGGER.error("problem saving driver picture", e);
              e.printStackTrace();
        }
        return result;
    }


    public String saveServicePicture(String picture, String name) {
        String result = null;
        try {
            byte[] decode = Base64.decode(picture);
            String[] extensions = resolveExtensions(decode);
            result = saveServicePhoto(decode, extensions[0], name);
        } catch (IOException e) {
            LOGGER.error("problem saving client picture", e);
              e.printStackTrace();
        }
        return result;
    }



    private String saveServicePhoto(byte[] image, String ext, String name) {
        Path path = Paths.get(rootPath.toString(), buildServicePhotoFileName(ext, name));
        try (OutputStream out = getOutputStream(path)) {
            out.write(image, 0, image.length);
        } catch (IOException x) {
            LOGGER.error("problem saving driver photo", x);
        }
        return profilesUrlPath + path.getFileName().toString();
    }



    public List<String> saveEventPictures(List<String> pictures, long partnerId) {
        ArrayList<String> result = new ArrayList<>();
        try {
            for (int i = 0; i < pictures.size(); i++) {
                String picture = pictures.get(i);
                if (picture != null) {
                    byte[] decode = Base64.decode(picture);
                    String[] extensions = resolveExtensions(decode);
                    String url = saveEventPhotos(partnerId, i + 1, decode, extensions[0]);
                    result.add(url);
                }
            }
        } catch (IOException e) {
            LOGGER.error("problem saving auto pictures", e);
            e.printStackTrace();
        }
        return result;
    }



    public List<DriverCarPhotosInfo> saveAutoPicturesByVersionApp(List<DriverCarPhotosInfo> driverCarPhotosInfos, long driverId){
        try {
            if(CollectionUtils.isEmpty(driverCarPhotosInfos)){
                return null;
            }
            for (int i = 0; i < driverCarPhotosInfos.size(); i++) {
                DriverCarPhotosInfo picture = driverCarPhotosInfos.get(i);
                if (picture != null) {
                    byte[] decode = Base64.decode(picture.getPhotoUrl()); // base64, после там будет урл
                    String[] extensions = resolveExtensions(decode);
                    String url = saveAutoPhotos(driverId, i + 1, decode, extensions[0], true);
                    picture.setPhotoUrl(url);
                }
            }
        } catch (IOException e) {
            LOGGER.error("problem saving auto pictures", e);
            e.printStackTrace();
        }
        return driverCarPhotosInfos;
    }


    public List<String> saveAutoPictures(List<String> pictures, long driverId) {
        ArrayList<String> result = new ArrayList<>();
        try {
            for (int i = 0; i < pictures.size(); i++) {
                String picture = pictures.get(i);
                if (picture != null) {
                    byte[] decode = Base64.decode(picture);
                    String[] extensions = resolveExtensions(decode);
                    String url = saveAutoPhotos(driverId, i + 1, decode, extensions[0], false);
                    result.add(url);
                }
            }
        } catch (IOException e) {
            LOGGER.error("problem saving auto pictures", e);
               e.printStackTrace();
        }
        return result;
    }




    public String savePolygon(String coordinates, long regionId) {
        String result = null;
        try {
            byte[] decode = Base64.decode(coordinates);
            LOGGER.info("decode  ="+decode);
            LOGGER.info("regionId  ="+regionId);
            String[] extensions = resolveExtensions(decode);
            result = savePolygonCoordinates(regionId, decode, extensions[0]);
        } catch (IOException e) {
            LOGGER.error("problem saving driver picture", e);
            e.printStackTrace();
        }
        return result;
    }



    private String savePolygonCoordinates(long regionId, byte[] image, String ext) {
        String coordFileName = "region_"+regionId+"."+ext;
        LOGGER.info("file name is: "+coordFileName);
        Path path = Paths.get(rootPath.toString(), coordFileName);
        try {
            Files.deleteIfExists(path);
            try (OutputStream out = getOutputStream(path)) {
                out.write(image, 0, image.length);
            }
        } catch (IOException x) {
            LOGGER.error("problem saving driver photo", x);
        }
        return profilesUrlPath + path.getFileName().toString();
    }



    private String saveDriverPhoto(long driverId, byte[] image, String ext, boolean clientVersionAppSupport) {
        String driverPhotoFileName = buildDriverPhotoFileName(driverId, ext, clientVersionAppSupport);
        Path path = Paths.get(rootPath.toString(), driverPhotoFileName); // buildDriverPhotoFileName(driverId, ext)
        try {
        Files.deleteIfExists(path);
        try (OutputStream out = getOutputStream(path)) {
            out.write(image, 0, image.length);
        }
        } catch (IOException x) {
            LOGGER.error("problem saving driver photo", x);
        }
        return profilesUrlPath + path.getFileName().toString();
    }



    private String saveClientPhoto(long clientId, byte[] image, String ext, boolean isUpdate) {
        String clientPhotoFileName =  isUpdate ? buildClientPhotoFileNameUpdate(clientId, ext) : buildClientPhotoFileName(clientId, ext);

        Path path = Paths.get(rootPath.toString(), clientPhotoFileName); // buildClientPhotoFileName(clientId, ext)
        try {
            Files.deleteIfExists(path);
            try (OutputStream out = getOutputStream(path)) {
                out.write(image, 0, image.length);
            }
        } catch (IOException e) {
            LOGGER.error("problem saving auto photos", e);
        }
        return profilesUrlPath + path.getFileName().toString();
    }

    private BufferedOutputStream getOutputStream(Path path) throws IOException {
        return new BufferedOutputStream(Files.newOutputStream(path, CREATE, WRITE, APPEND));
    }

    private String saveAutoPhotos(long driverId, int photoIdx, byte[] image, String ext, boolean clientVersionAppSupport) {
        Path path = Paths.get(rootPath.toString(), buildAutoPhotoFileName(driverId, photoIdx, ext, clientVersionAppSupport));
        try {
            Files.deleteIfExists(path);
            try (OutputStream out = getOutputStream(path)) {
                out.write(image, 0, image.length);
            }
        } catch (IOException e) {
            LOGGER.error("problem saving auto photos", e);
        }
        return profilesUrlPath + path.getFileName().toString();
    }



    private String saveEventPhotos(long partnerId, int photoIdx, byte[] image, String ext) {
        Path path = Paths.get(rootPath.toString(), buildEventPhotoFileName(partnerId, photoIdx, ext));
        try {
            Files.deleteIfExists(path);
            try (OutputStream out = getOutputStream(path)) {
                out.write(image, 0, image.length);
            }
        } catch (IOException e) {
            LOGGER.error("problem saving auto photos", e);
        }
        return profilesUrlPath + path.getFileName().toString();
    }



    public boolean deleteClientPhotos(String fileName) {
        LOGGER.info("fileName: "+fileName);
        boolean result = false;
        if(StringUtils.isEmpty(fileName)){
            return result;
        }
        Path path = Paths.get(rootPath.toString(), fileName);
        try {
            Files.deleteIfExists(path);
              result = true;
        } catch (IOException e) {
            LOGGER.error("problem delete client photo", e);
        }
        return result;
    }



}