package be.sgerard.kafka;

/**
 * Created by jeremy on 5/3/16.
 */
class VersionUtil {

    public static String getVersion() {
        try {
            return VersionUtil.class.getPackage().getImplementationVersion(); // The JAR plugin defines the version in META-INF file
        } catch (Exception e) {
            throw new IllegalStateException("Error while retrieving the current version.", e);
        }
    }
}
