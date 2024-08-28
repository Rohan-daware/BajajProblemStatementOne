

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class PRNbajaj {

	public static void main(String[] args) {
		if (args.length < 2) {
            System.out.println(" <PRN Number> <Path to JSON file>");
            return;
        }

        String prnNumber = args[0].toLowerCase().replaceAll("\\s+", "");
        String jsonFilePath = args[1];

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

            String destinationValue = findDestinationValue(rootNode);
            if (destinationValue == null) {
                System.out.println("key destination not found");
                return;
            }

            String randomString = generateRandomString(8);
            String concatenatedValue = prnNumber + destinationValue + randomString;
            String hashValue = generateMD5Hash(concatenatedValue);

            // Output the result
            System.out.println(hashValue + ";" + randomString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String findDestinationValue(JsonNode node) {
        if (node.has("destination")) {
            return node.get("destination").asText();
        }
        for (JsonNode child : node) {
            if (child.isObject()) {
                String value = findDestinationValue(child);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "GFTDFVGVVVVVVVGhdbhvfbhdfbfhf354555555555555554343454566666555";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private static String generateMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
		

}
