package com.example.server;

import com.example.server.dao.PregenUrlDao;
import com.example.server.model.PregenUrl;
import com.example.server.service.PregenService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ServerApplication {

    public static void main(String[] args) throws IOException {
        // pre generate urls
//        PregenService ps = new PregenService();
//        ps.bulkInsertPregen(0,100);
//        PregenUrlDao pd = PregenUrlDao.make();
//        pd.readTable();
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
