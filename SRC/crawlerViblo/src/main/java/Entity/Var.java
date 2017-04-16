package Entity;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangminh on 21/10/2016.
 */
public class Var {
    public static List<Category> categoryArrayList=new ArrayList<>();
    public static Client client;
    public static Client subClient;
    public static int eventForCategories=0;

}
