package at.aau.busfahrer.data.networking;

/**
 * Used for callbacks.
 */
public interface Callback<T> {

    void callback(T argument);

}
