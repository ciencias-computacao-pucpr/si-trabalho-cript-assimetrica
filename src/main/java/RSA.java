import java.security.*;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class RSA {

    private Key key;
    private Cipher cipher;

    private RSA(String algorithm, boolean publica, int tamanhoChave) {
        try {
            if (algorithm.equals("RSA")) {
                KeyPairGenerator keyGen = null;

                keyGen = KeyPairGenerator.getInstance(algorithm);
                keyGen.initialize(tamanhoChave);
                KeyPair keyPair = keyGen.generateKeyPair();
                if (publica) key = keyPair.getPublic();
                else key = keyPair.getPrivate();
            }

            if (algorithm.equals("AES")) {
                String aesKey = UUID.randomUUID().toString().substring(0, 16);
                key = new SecretKeySpec(aesKey.getBytes(), "AES");
            }

            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private byte[] encrypt(String mensagem) {
        try {
            return cipher.doFinal(mensagem.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        String alg = args[0];
        int keySize = Integer.parseInt(args[1]);
        String chave = args[2];

        String mensagem = "RSA  é  um  algoritmo  que  deve  o  seu  nome  a  três professores do MIT:Ronald Rivest, Adi Shamir e Leonard Adleman";

        long tempoTotal = temporisar(() -> new RSA(alg, chave.equals("publica"), keySize).encrypt(mensagem));

        System.out.println(tempoTotal);
    }

    private static long temporisar(Runnable execucao) {
        Instant inicio = Instant.now();
        execucao.run();
        Instant fim = Instant.now();
        return Duration.between(inicio, fim).toMillis();
    }
}