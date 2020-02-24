package io.dev.home.salesorders;

import io.dev.home.salesorders.interfaces.commandline.ImportOrdersParametersBuilder;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SalesOrdersApplication {

    public static void main(String[] args) {
        ImportOrdersParametersBuilder.validation(args);
        String[] arguments = ImportOrdersParametersBuilder.createFromArgs(args[0]);
        new SpringApplicationBuilder(SalesOrdersApplication.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .headless(true)
                .run(arguments);
    }

}

