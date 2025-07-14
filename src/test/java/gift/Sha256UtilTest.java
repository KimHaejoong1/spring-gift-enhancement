package gift;

import gift.exception.UnsupportedShaAlgorithmException;
import gift.util.Sha256Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class Sha256UtilTest {
    @Test
    @DisplayName("SHA-256 예외 테스트")
    void throw_unsupportedShaAlgorithmException() {
        Sha256Util sha256Util = new Sha256Util();

        Exception exception = Assertions.assertThrows(UnsupportedShaAlgorithmException.class, () -> {
            sha256Util.encryptWithAlgorithm("test", "test-256");
        });
    }
}
