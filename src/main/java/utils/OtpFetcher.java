package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpFetcher {

//    private static final String OTP_REGEX = "\\b\\d{4,6}\\b";

    public static String fetchOtpFromDevice() {
        String otpCode = null;
        long latestTimestamp = 0;
        String latestSms = null;

        try {
            // ADB command to fetch SMS messages
            String[] adbCommand = {
                    "adb", "shell", "content", "query",
                    "--uri", "content://sms/inbox",
                    "--projection", "address,body,date"
            };

            Process process = new ProcessBuilder(adbCommand).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                long timestamp = extractTimestamp(line);

                // Keep only the latest SMS
                if (timestamp > latestTimestamp) {
                    latestTimestamp = timestamp;
                    latestSms = line;
                }
            }

            process.waitFor();

            if (latestSms != null) {
                System.out.println("Latest SMS: " + latestSms); // Debugging
                otpCode = extractOtp(latestSms);
            }

        } catch (Exception e) {
            System.err.println("Error fetching OTP via ADB: " + e.getMessage());
        }

        return otpCode;
    }

    // Extract timestamp from SMS string
    private static long extractTimestamp(String smsLine) {
        Pattern timestampPattern = Pattern.compile("date=(\\d+)");
        Matcher matcher = timestampPattern.matcher(smsLine);
        return matcher.find() ? Long.parseLong(matcher.group(1)) : 0;
    }

    // Extract OTP (4-6 digits) from SMS body
    private static String extractOtp(String smsLine) {
        Pattern otpPattern = Pattern.compile("\\b\\d{4,6}\\b");
        Matcher matcher = otpPattern.matcher(smsLine);
        return matcher.find() ? matcher.group(0) : null;
    }
}