package com.example.server.dao;

// [START database]
import com.example.server.model.Url;
import com.example.server.model.User;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.NotFoundException;
import com.google.api.gax.rpc.ServerStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.common.collect.Lists;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UrlDao {

    private String tableId;
    private final BigtableDataClient dataClient;
    private final BigtableTableAdminClient adminClient;
    private static UrlDao ONLY;

    public static UrlDao make() throws IOException {
        if (ONLY == null) {
            ONLY = new UrlDao("shortify_url");
        }
        return ONLY;
    }

    private UrlDao(String tableId) throws IOException {
//        String serviceAccountPath = "/home/mc143/url-shortener-shortify/server/service-account.json";
        String serviceAccountPath = "./service-account.json";
        String projectId = "rice-comp-539-fall-2021";
        String instanceId = "comp-539-fall-2021";
        this.tableId = tableId;

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountPath))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        // [START bigtable_hw_connect]
        BigtableDataSettings settings =
                BigtableDataSettings.newBuilder()
                        .setProjectId(projectId)
                        .setInstanceId(instanceId)
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

        dataClient = BigtableDataClient.create(settings);

        BigtableTableAdminSettings adminSettings =
                BigtableTableAdminSettings.newBuilder()
                        .setProjectId(projectId)
                        .setInstanceId(instanceId)
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

        // Creates a bigtable table admin client.
        adminClient = BigtableTableAdminClient.create(adminSettings);
    }

    public void changeTable(String newTableId) {
        this.tableId = newTableId;
    }

    public boolean deleteTable() throws IOException {
        if (adminClient.exists(tableId)) {
            adminClient.deleteTable(tableId);
            return true;
        }
        return false;
    }

    // basie table
    public void createTable() {
        // [START bigtable_hw_create_table]
        if (!adminClient.exists(tableId)) {
            System.out.println("Creating table: " + tableId);
            CreateTableRequest createTableRequest =
                    CreateTableRequest.of(tableId).addFamily("Essential").addFamily("Optional");
            adminClient.createTable(createTableRequest);
            System.out.printf("Table %s created successfully%n", tableId);
        } else {
            System.out.printf("Table %s already exists%n", tableId);
        }
        // [END bigtable_hw_create_table]
    }

    // custom table
    public void createTable(List<String> columnFamily) {
        // [START bigtable_hw_create_table]
        if (!adminClient.exists(tableId)) {
            System.out.println("Creating table: " + tableId);
            CreateTableRequest createTableRequest = CreateTableRequest.of(tableId);
            for (String columnFamilyName : columnFamily) {
                createTableRequest.addFamily(columnFamilyName);
            }
            adminClient.createTable(createTableRequest);
            System.out.printf("Table %s created successfully%n", tableId);
        } else {
            System.out.printf("Table %s already exists%n", tableId);
        }
        // [END bigtable_hw_create_table]
    }

    public boolean checkKey(String key) {
        // [START bigtable_hw_check_key]
        return dataClient.exists(tableId, key);
        // [END bigtable_hw_check_key]
    }

    public Url getValueByKey(String key) {
        try {
            if (checkKey(key)) {
                Url.UrlBuilder result = Url.builder();
                List<RowCell> listOfCells = dataClient.readRow(tableId, key).getCells();
                for (RowCell cell : listOfCells) {
                    // String family = cell.getFamily();
                    String column = cell.getQualifier().toStringUtf8();
                    String value = cell.getValue().toStringUtf8();
                    switch (column) {
                        case "shortUrl":
                            result.shortUrl(value);
                            break;
                        case "longUrl":
                            result.longUrl(value);
                            break;
                        case "userId":
                            result.userId(value);
                            break;
                        case "expireTime":
                            result.expireTime(value);
                            break;
                        case "ipAddress":
                            result.ipAddress(value);
                            break;
                        case "timeStamp":
                            result.timeStamp(value);
                            break;
                    }
                }
                return result.build();
            }
        } catch (NotFoundException e) {
            System.err.println("Failed to get value from key: " + e.getMessage());
            System.err.println("Failed to read key: " + key);
        }
        // [END bigtable_hw_get_value_by_key]
        return null;
    }

    public void insertValue(String key, Url value) {
        // [START bigtable_hw_insert_value]
        String columnFamily1 = "Essential";
        String columnFamily2 = "Optional";
        String columnQualifier1 = "shortUrl";
        String columnQualifier2 = "longUrl";
        String columnQualifier3 = "userId";
        String columnQualifier4 = "expireTime";
        String columnQualifier5 = "ipAddress";
        String columnQualifier6 = "timeStamp";
        try {
        RowMutation rowMutation =
                RowMutation.create(tableId, key)
                        .setCell(columnFamily1, columnQualifier1, value.getShortUrl())
                        .setCell(columnFamily1, columnQualifier2, value.getLongUrl())
                        .setCell(columnFamily2, columnQualifier3, value.getUserId())
                        .setCell(columnFamily2, columnQualifier4, value.getExpireTime())
                        .setCell(columnFamily2, columnQualifier5, value.getIpAddress())
                        .setCell(columnFamily2, columnQualifier6, value.getTimeStamp());;
        dataClient.mutateRowAsync(rowMutation);
        } catch (NotFoundException e) {
            System.err.println("Failed to write value to table: " + e.getMessage());
            System.err.println("Failed to write key: " + key);
        }
        // [END bigtable_hw_insert_value]
    }

//    public List<Url> readTable() {
//        // [START bigtable_hw_read_table]
//        try {
//            System.out.println("\nReading the entire table");
//            Query query = Query.create(tableId);
//            ServerStream<Row> rowStream = dataClient.readRows(query);
//            for (Row r : rowStream) {
//                System.out.println("Row Key: " + r.getKey().toStringUtf8());
//                for (RowCell cell : r.getCells()) {
//                    String column = cell.getQualifier().toStringUtf8();
//                    String value = cell.getValue().toStringUtf8();
//                    System.out.printf(
//                            "Family: %s    Qualifier: %s    Value: %s%n",
//                            cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
//                }
//            }
//        } catch (NotFoundException e) {
//            System.err.println("Failed to read a non-existent table: " + e.getMessage());
//        }
//        return null;
//    }

    public List<Url> readTable() {
        // [START bigtable_hw_read_table]
        try {
            System.out.println("\nReading the entire table");
            Query query = Query.create(tableId);
            ServerStream<Row> rowStream = dataClient.readRows(query);
            List<Url> tableRows = new ArrayList<>();
            for (Row r : rowStream) {
                System.out.println("Row Key: " + r.getKey().toStringUtf8());
//                User.UserBuilder userBuilder = User.builder();
                Url.UrlBuilder urlBuilder = Url.builder();
                for (RowCell cell : r.getCells()) {
                    String column = cell.getQualifier().toStringUtf8();
                    String value = cell.getValue().toStringUtf8();
                    switch (column) {
                        case "shortUrl":
                            urlBuilder.shortUrl(value);
                            break;
                        case "longUrl":
                            urlBuilder.longUrl(value);
                            break;
                        case "userId":
                            urlBuilder.userId(value);
                            break;
                        case "expireTime":
                            urlBuilder.expireTime(value);
                            break;
                        case "ipAddress":
                            urlBuilder.ipAddress(value);
                            break;
                        case "timeStamp":
                            urlBuilder.timeStamp(value);
                            break;
                    }
//                    System.out.printf(
//                            "Family: %s    Qualifier: %s    Value: %s%n",
//                            cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
                }

                Url url =  urlBuilder.build();
                tableRows.add(url);
            }
            return tableRows;
        } catch (NotFoundException e) {
            System.err.println("Failed to read a non-existent table: " + e.getMessage());
            return null;
        }
    }

    public List<Url> readTable(String userId) {
        // [START bigtable_hw_read_table]
        try {
            System.out.println("\nReading the entire table");
            Query query = Query.create(tableId);
            ServerStream<Row> rowStream = dataClient.readRows(query);
            List<Url> tableRows = new ArrayList<>();
            for (Row r : rowStream) {
                System.out.println("Row Key: " + r.getKey().toStringUtf8());
//                User.UserBuilder userBuilder = User.builder();
                Url.UrlBuilder urlBuilder = Url.builder();
                for (RowCell cell : r.getCells()) {
                    String column = cell.getQualifier().toStringUtf8();
                    String value = cell.getValue().toStringUtf8();
                    switch (column) {
                        case "shortUrl":
                            urlBuilder.shortUrl(value);
                            break;
                        case "longUrl":
                            urlBuilder.longUrl(value);
                            break;
                        case "userId":
                            urlBuilder.userId(value);
                            break;
                        case "expireTime":
                            urlBuilder.expireTime(value);
                            break;
                        case "ipAddress":
                            urlBuilder.ipAddress(value);
                            break;
                        case "timeStamp":
                            urlBuilder.timeStamp(value);
                            break;
                    }
//                    System.out.printf(
//                            "Family: %s    Qualifier: %s    Value: %s%n",
//                            cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
                }

                Url url =  urlBuilder.build();
                if (url.getUserId().equals(userId)){
                    tableRows.add(url);
                }
            }
            return tableRows;
        } catch (NotFoundException e) {
            System.err.println("Failed to read a non-existent table: " + e.getMessage());
            return null;
        }
    }

    public void deleteValue(String key) {
        // [START bigtable_hw_delete_value]
        try {
            if (checkKey(key)) {
                dataClient.mutateRow(RowMutation.create(tableId, key).deleteRow());
            } else {
                System.out.println("Key not found");
            }
        } catch (NotFoundException e) {
            System.err.println("Failed to delete value from table: " + e.getMessage());
            System.err.println("Failed to delete key: " + key);
        }
        // [END bigtable_hw_delete_value]
    }
}
// [END database]
