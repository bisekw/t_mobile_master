package framework.config;

import framework.core.DriverType;

import java.util.Collection;

public class Settings {
    public static String CHROME_VERSION;
    public static String GRID_APPIUM_ANDROID = "http://127.0.0.1:4723/wd/hub"; //TBD
    public static String TESTED_APP_URL;
    public static boolean IS_REMOTE_RUN;
    public static DriverType DRIVER_TYPE;
    public static String GRID_URL ="http://selenium-hub:4444/wd/hub";
    public static String GRID_LOCAL = "http://localhost:4444/wd/hub";
    public static String GRID_REMOTE = "http://localhost:4444/wd/hub";
    public static String GRID_APPIUM_IOS = "http://192.168.1.19:4723/wd/hub"; //TBD
    public static String SCENARIO_NAME;
    public static String RUNNER_TYPE;
    public static String CUCUMBER_RUNNER_TAG;
    public static Collection<String> CUCUMBER_SCENARIO_TAGS;
    public static String RP_ATTRIBUTES;
    public static String RP_ENDPOINT;
    public static String RP_USERNAME;
    public static String RP_UUID;
    public static String RP_LAUNCH;
    public static String RP_PROJECT;

    public static String APPIUM_DEVICE_NAME;
    public static String APPIUM_PLATFORM_VERSION;
    public static String APPIUM_APP_BUNDLE_ID ="com.apple.calculator";
    public static String APPIUM_APP_ACTIVITY;
    public static String APPIUM_APP_PACKAGE;

    public static String EMAIL_ADDRES_GUERRILLA_MAIL;
    public static String SIT_TOKEN_GUERRILLA_MAIL;
    public static String ACTIVATION_LINK;

}
