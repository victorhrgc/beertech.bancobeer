package beertech.becks.api.security.sign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DigitalSign {

  public static byte[] objectToJsonBytes(Object obj){
    try {
      ObjectMapper objectMapper =new ObjectMapper();
      return objectMapper.writeValueAsBytes(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static boolean verify(byte[] document,byte[] receivedSignature, PublicKey publicKey){
    try {
      Signature signature = Signature.getInstance("SHA256withRSA");
      signature.initVerify(publicKey);
      signature.update(document);
      return  signature.verify(receivedSignature);
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static PublicKey readPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] encoded = Base64.getDecoder().decode(key);

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    return keyFactory.generatePublic(keySpec);
  }
}
