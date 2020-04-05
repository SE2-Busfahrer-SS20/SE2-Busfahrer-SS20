package at.aau.busfahrer.data.networking.kryonet;

public interface KryoNetComponent {

    /**
     * Register a class for serialization.
     *
     * @param c
     */
    void registerClass(Class c);

}
