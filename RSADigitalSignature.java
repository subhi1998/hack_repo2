import java.security.*;
import java.security.spec.PSSParameterSpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSADigitalSignature {

    public static void main(String[] args) {
        try {
            // Generate RSA key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // Message to be signed
            String message = "Hello, digital signatures!";

            // Sign the message
            byte[] signature = signMessage(message, privateKey);
            System.out.println("Signature: " + Base64.getEncoder().encodeToString(signature));

            // Verify the signature
            boolean isVerified = verifySignature(message, signature, publicKey);
            if (isVerified) {
                System.out.println("RSA Signature verified!");
            } else {
                System.out.println("RSA Signature verification failed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to sign a message
    public static byte[] signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA/PSS");
        signature.setParameter(new PSSParameterSpec("SHA-256"));
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        return signature.sign();
    }

    // Method to verify a signature
    public static boolean verifySignature(String message, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA/PSS");
        signature.setParameter(new PSSParameterSpec("SHA-256"));
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        return signature.verify(signatureBytes);
    }
}