package beertech.becks.externalbank.security;

import beertech.becks.externalbank.tos.request.PaymentSlipRequestTO;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.xml.bind.DatatypeConverter;

public class DigitalSign {

  private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCHHsiNllMhd29/6dFmkIuP2RpmUjlBsd/D438AEs8NCkSOoNmAm/2V7i3MfH6seQ/7VldtQyQXrvV7NkRvRfOKIsfLIKMzJyL3WAtWMvEB5WppJCEZICjPDUqsMdWNnVR4iEY6regVDQh9/DESmzZUeVsp+Gh5pFG8WAS8DhCphEskAVdJupMcQZQeK7u42hnwXS6TVmbux1Fvvaw4eP6wHqXbBqfD0KOXGUMmJlQkYq5G+xdfPHEvGAzWnXIeHkLE5uKOwTYLcJ2p2skR//SMiKXMnkkuAbUEI/Qz4e8ZNsz/px3UOiQaqDNkaCuOSWzKPsgVxIPJxZdf7KC3gndpAgMBAAECggEAYmLf1Xjn/y5gWdt/RhdqB8wllOu6FUPXk2NLTlpjuTMIyHIG7can10wevQ7Jjfx4zqIH9t80oKocxSK2oe9lbef1YsrZCp4sr73EQ3fEVYUe/n/4ObtY+6rJ/Kgsv3kX0MwplwCaRPnAfTvs34d0//19jAn8yNYFTgcpK2VybHt9RliQrM90a08Qav5NOjBz9tBke2r1VOAHenl6DJGFKHNJzUbCZWOrHSKNsPQa4n0G+cywAmXcEnPSe6k1jrCLRkVCEwIe07pPri65BM3J54PkFKjtLBBaNGfp8x5Drvt6mhn15Ar9xBcMFH0pdshhgahsHkHJYCp5yi/PcBocAQKBgQDbhWJFrS5vunG1OqKfb04jOXcJrlhOJOQVa+usQrm0h4w3aBsAMUg1PTfvaEV3hpxqqec5QCSt0GrhZgTOJMT5oVacGNl1A9TzoreNC2tEZoYDiqd3ESHNypTYX8xlbGZ9CxA5Evazy9KS1CCEvJR1Q0llGgvo1KeG+2wC0yHWkQKBgQCdkuqmois5mSgAkVA+iE3JcODDBfSxqPEHxX/rEeGzKnU8XJnFSqhF1Ud0Vv8j/q568J4M/eLwxjxbd3xwAIaube+XkoIo24azz3CePmwYc4e+PJKZhXoHCzKAqsv8BVAnU3RYbVAh50r2HwPliu1FwQPRlmfAad+879hqkFZvWQKBgFIciG/Loa0FLnc9oYrFlZKzcgVbA4jnX7FrltHue4w9j4NCG6dsx7eyWHPiqulUnrl/KBBQH+gsJ258PJvnQQ6m/MCSwvR0JHmlicbmWI9TUhmNCasN63kQp7Bv1QKzE3r+YmkW4NTMDs4BLtC3wUhDjPaWUZIbyTZCYVqFIwkhAoGBAJqtOQHRdrTXjtjfy/FWQ2VWFIvcMNKkj9WdHXBW4ULKjsJt89Qi8QN71ORy8YkX25Z60+rU3gyTy3PN2qznF03qRKJ5AqOwuo2PN3FevSVTYxBuzUk+KQb5Ct4aypndbUztMuwKgfqF3KO01lJhCYDLc0AtcDMEusfweHRCs1YhAoGAYs18vmi5nnhFU/0Mfegl8DZgELsE/eU6oVVQngnBtd5ooy53Jjw2enhBq+gOyDftqDXXonpg8d1bO3d2UWdQ4kWsU7sfpMLL4V4WSmE8hXQFOtoznP2cwD2nQf4YsUtwwuFc0ccFlwKMfoTnrBXdvinB+Yr8Z8e9o7DWQ0JPoaA=";

  public static PrivateKey getPrivateKey() {
    byte[] encoded = Base64.getDecoder().decode(PRIVATE_KEY);

    KeyFactory keyFactory = null;
    try {
      keyFactory = KeyFactory.getInstance("RSA");

      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
      return (PrivateKey) keyFactory.generatePrivate(keySpec);

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] sign(byte[] document, PrivateKey privateKey){

    try {
      Signature signature = Signature.getInstance("SHA256withRSA");
      signature.initSign(privateKey);
      signature.update(document);
      return  signature.sign();
    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      e.printStackTrace();
    }
    return null;
  }
}
