package fr.nereide.editor;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class EditorBundle extends DynamicBundle {

    private static final EditorBundle ourInstance = new EditorBundle();

    @NonNls
    public static final String BUNDLE = "messages.EditorBundle";

    private EditorBundle() {
        super(BUNDLE);
    }

    public static @Nls
    String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object... params) {
        return ourInstance.getMessage(key, params);
    }
}
