package ru.trendtech.services;

/**
 * Created by petr on 20.08.14.
 */


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.trendtech.repositories.mongo.RepositoryPackage;

@Configuration
@EnableMongoRepositories(basePackageClasses=RepositoryPackage.class)
//@ComponentScan(basePackageClasses=TemplatePackage.class)
public class SpringMongoConfig {

    @Value("${mongo.dbName}")
    private String dbName = "";

    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        // если вход с логином и паролем
        // UserCredentials userCredentials = new UserCredentials("petr", "secret");
        //SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(new MongoClient("stg0.taxisto.ru", 27017), "taxisto_stg"); // , userCredentials /taxisto_
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(new MongoClient("stg0.taxisto.ru", 27017), dbName);
           return simpleMongoDbFactory;
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
          return mongoTemplate;
    }

}