package framework.toolkit;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AdbCommandsRunner {
    String adbDevicesCommand = "adb devices";
    String adbReboot = "adb -s %s reboot";

    public void runCommand(String adbCommand) {
        try {
            Process process = Runtime.getRuntime().exec(adbCommand);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
              log.info(line); // Wyświetlanie wyników komendy ADB
            }

            process.waitFor(); // Oczekiwanie na zakończenie procesu
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAdbDeviceList() {
        List<String> deviceList = new ArrayList<>();

        try {
            Process process = Runtime.getRuntime().exec(adbDevicesCommand);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && !line.startsWith("List of devices attached")) {
                    deviceList.add(line.trim());
                }
            }

            process.waitFor();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return deviceList;
    }

    public void rebootPhoneByUdid(String udid) {
        String command = String.format(adbReboot, udid);
        try {
            Process process = Runtime.getRuntime().exec(udid);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
               log.info(line); // Wyświetlanie wyników komendy ADB
            }

            process.waitFor(); // Oczekiwanie na zakończenie procesu
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

