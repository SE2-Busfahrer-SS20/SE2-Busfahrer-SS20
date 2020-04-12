package at.aau.common.networking;

/**
 * Used for callbacks.
 */
public interface Callback<T> {

    void callback(T argument);

}
