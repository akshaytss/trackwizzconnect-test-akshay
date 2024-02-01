package app.trackwizz.connect;

import app.trackwizz.connect.utils.KycMatchCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KycMatchApplication extends Throwable {


    public static void main(String[] args) {
        SpringApplication.run(KycMatchApplication.class, args);
        KycMatchCache.setAllKycMatchCache();
    }
}
