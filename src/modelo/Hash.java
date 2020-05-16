package modelo;

/**
 * Hash.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class Hash {

    /**
     * Retorna un hash a partir de un tipo y un texto
     *
     * @param txt texto de entrada
     * @param hashType tipo de encriptacion
     * @return el texto encriptado
     */
    public static String getHash(String txt, String hashType) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retorna un hash MD5 a partir de un texto
     *
     * @param txt texto de entrada
     * @return el texto encriptado en MD5
     */
    public static String md5(String txt) {
        return Hash.getHash(txt, "MD5");
    }

    /**
     * Retorna un hash SHA1 a partir de un texto
     *
     * @param txt texto de entrada
     * @return el texto encriptado en SHA1
     */
    public static String sha1(String txt) {
        return Hash.getHash(txt, "SHA1");
    }

}
