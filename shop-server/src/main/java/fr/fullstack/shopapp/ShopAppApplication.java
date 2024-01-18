package fr.fullstack.shopapp;

import fr.fullstack.shopapp.service.Indexer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopAppApplication.class, args);
    }

    @Bean
    public ApplicationRunner buildIndex(Indexer indexer) {
        return args -> {
            indexer.index("fr.fullstack.shopapp.model.Shop");
        };
    }

}
