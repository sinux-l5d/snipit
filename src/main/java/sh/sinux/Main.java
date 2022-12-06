package sh.sinux;

import sh.sinux.config.Config;

public class Main {
    public static void main(String[] args) {
        Config config = Config.create();
        System.out.println(config.getStoragePath());
        System.out.println(config.getStorageType());
    }
}