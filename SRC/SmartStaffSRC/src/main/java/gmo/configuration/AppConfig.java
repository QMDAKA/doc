package gmo.configuration;

import org.elasticsearch.client.Client;

/**
 * Created by Quang Minh on 6/9/2016.
 */
public class AppConfig {
    private static String newPageUrl;
    private static String docPageUrl;
    private static String newDirectory;
    private static String docDirectory;
    private static String rootDirectory;
    private static String staffInfoServer;


    public static Client client;
    public static int eventForCategories = 0;

    public static String getRootDirectory() {
        return rootDirectory;
    }

    public static void setRootDirectory(String rootDirectory) {
        AppConfig.rootDirectory = rootDirectory;
    }

    public static String getNewDirectory() {
        return newDirectory;
    }

    public static void setNewDirectory(String newDirectory) {
        AppConfig.newDirectory = newDirectory;
    }

    public static String getDocDirectory() {
        return docDirectory;
    }

    public static void setDocDirectory(String docDirectory) {
        AppConfig.docDirectory = docDirectory;
    }

    public static String getNewPageUrl() {
        return newPageUrl;
    }

    public static void setNewPageUrl(String newPageUrl) {
        AppConfig.newPageUrl = newPageUrl;
    }

    public static String getDocPageUrl() {
        return docPageUrl;
    }

    public static void setDocPageUrl(String docPageUrl) {
        AppConfig.docPageUrl = docPageUrl;
    }

    public static String getStaffInfoServer() {
        return staffInfoServer;
    }

    public static void setStaffInfoServer(String staffInfoServer) {
        AppConfig.staffInfoServer = staffInfoServer;
    }
}
