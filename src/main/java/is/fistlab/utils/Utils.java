package is.fistlab.utils;

import java.util.Objects;

public class Utils {

    public static boolean isEmptyOrNull(final String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

}
