package com.dubhad.qaproject.util;

import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.resource.Configuration;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Class, that provides utility methods for file-based tasks
 */
@Log4j2
public class FileUtil {
    private static final String FILE_NAME_EXTENSION_DELIMITER = ".";
    private static final String CONTENT_DISPOSITION_DELIMITER = ";";
    private static final String PROPERTIES_DELIMITER = "=";
    private static final String CONTENT_FILENAME = "filename";

    /**
     * Delete specified avatar from specified folder
     * @param basePath path to folder with avatars
     * @param username username to be converted into file name
     * @param extension extension to be added to filename
     * @return true, if file was deleted, false otherwise
     */
    public boolean deleteAvatar(String basePath, String username, String extension){
        File file = new File(basePath + Configuration.AVATARS_FOLDER + File.separator + username + extension);
        boolean result = file.delete();
        log.debug("Deleting " + username + extension + " avatar: " + result);
        return result;
    }

    /**
     * Save picture parts to folder inside specified
     * @param parts parts to be saved
     * @param basePath path to parent folder of avatars folder
     * @param username username to be converted into file name
     * @return extension, fetched from parts
     */
    public String saveUploadedAvatar(Collection<Part> parts, String basePath, String username){
        return saveUploadedParts(parts, basePath + Configuration.AVATARS_FOLDER, username);
    }

    /**
     * Saves picture parts to specified location
     * @param parts parts to be saved
     * @param path path to folder
     * @param name file name, excluding extension
     * @return extension, fetched from parts
     */
    public String saveUploadedParts(Collection<Part> parts, String path, String name){
        log.debug(path);
        File uploadDir = new File(path);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String extension = null;
        try {
            for (Part part : parts) {
                extension = getFileExtension(part);
                if(extension == null){
                    continue;
                }
                part.write(path + File.separator + name + extension);
            }
        }
        catch (IOException e){
            log.error(e);
            extension = null;
        }
        log.debug(path + name + extension);
        return extension;
    }

    /**
     * Gets file extension from part
     * @param part part to get extension from
     * @return extension of passed part
     */
    private String getFileExtension(Part part) {
        String filename = null;
        for (String content : part.getHeader(RequestParamAttr.CONTENT_DISPOSITION).split(CONTENT_DISPOSITION_DELIMITER)) {
            if (content.trim().startsWith(CONTENT_FILENAME)) {
                filename = content.substring(content.indexOf(PROPERTIES_DELIMITER) + 2, content.length() - 1);
            }
        }
        return filename == null ? null :filename.substring(filename.lastIndexOf(FILE_NAME_EXTENSION_DELIMITER));
    }
}
