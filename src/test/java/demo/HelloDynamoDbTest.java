package demo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HelloDynamoDbTest {

    private DynamoDBProxyServer server;
    private AmazonDynamoDB amazonDynamoDB;

    static {
        System.setProperty("sqlite4java.library.path", "native-libs");
    }

    @BeforeEach
    public void setup() throws Exception {
        final String port = getAvailablePort();
        this.server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-inMemory", "-port", port});
        server.start();

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
            "http://localhost:" + port, Regions.US_EAST_1.getName());
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build();
    }

    @AfterEach
    public void teardown() throws Exception {
        if (server == null) return;
        server.stop();
    }

    private String getAvailablePort() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(0);
        return String.valueOf(serverSocket.getLocalPort());
    }

    @Test
    public void test() throws InterruptedException {
        HelloDynamoDb hello = new HelloDynamoDb(this.amazonDynamoDB);
        hello.createTable();
        hello.insertItem(User.build(111, "aaa", "aaa@test.com", 100));
        hello.insertItem(User.build(222, "bbb", "bbb@test.com", 200));
        List<User> users = hello.getItems();
        System.out.println("### users: " + users);
        assertThat(users).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(asList(
            User.build(111, "aaa", "aaa@test.com", 100),
            User.build(222, "bbb", "bbb@test.com", 200)
        ));
    }

}