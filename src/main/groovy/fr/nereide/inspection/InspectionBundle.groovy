package fr.nereide.inspection

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.PropertyKey

/**
 * Message bundle for inspections
 */
class InspectionBundle extends DynamicBundle {

    private static final InspectionBundle OUR_INSTANCE = new InspectionBundle()

    @NonNls
    public static final String BUNDLE = 'messages.InspectionBundle'

    private InspectionBundle() {
        super(BUNDLE)
    }

    static @Nls
    String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object... params) {
        return OUR_INSTANCE.getMessage(key, params)
    }

}
