package hello;

import java.util.*;
import com.clickhouse.client.*;
import java.util.concurrent.ExecutionException;

public class HelloDB {
	public static void main(String[] args) {
            ClickHouseProtocol preferredProtocol = ClickHouseProtocol.HTTP;
            ClickHouseFormat preferredFormat = ClickHouseFormat.RowBinaryWithNamesAndTypes;
            String host = "127.0.0.1";
            String user = "default";
            String password = "password";
            ClickHouseCredentials credentials = ClickHouseCredentials
                .fromUserAndPassword(user, password);

            int port = 8123;
            String database = "default";

            ClickHouseNode server = ClickHouseNode.builder()
                .host(host)
                .port(preferredProtocol, port)
                .database(database)
                .credentials(credentials)
                .build();

            try {
                ClickHouseClient client = ClickHouseClient.newInstance(preferredProtocol);
                ClickHouseResponse resp = client.connect(server)
                    .format(preferredFormat)
                    .query("select * from numbers(:limit)")
                    .params(10).execute().get();
                for (ClickHouseRecord record : resp.records()) {
                    int num = record.getValue(0).asInteger();
                    String str = record.getValue(0).asString();
                    System.out.println(str);
                }
            } catch (InterruptedException e) {
                 e.printStackTrace();
	        } catch (ExecutionException e) {
                e.printStackTrace();
            }
	}
}
