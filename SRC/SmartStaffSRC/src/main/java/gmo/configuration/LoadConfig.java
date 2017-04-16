package gmo.configuration;

/**
 * Created by Quang Minh on 3/22/2016.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


@Component
public class LoadConfig {
    @EventListener
    public void init(ContextRefreshedEvent event) {
        try {

            URL url = Resources.getResource("application.properties");
            Properties prop = new Properties();
            prop.load(url.openStream());
            AppConfig.setRootDirectory(prop.getProperty("root.directory"));
            AppConfig.setDocPageUrl(prop.getProperty("docs.page.url"));
            AppConfig.setNewPageUrl(prop.getProperty("news.page.url"));
            AppConfig.setStaffInfoServer(prop.getProperty("staff-info.server"));
            /*AppConfig.client= TransportClient.builder().build()
                   .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));*/
            AppConfig.client = new TransportClient()
                    .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
