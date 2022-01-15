package com.example.server.dao;

// [START database]
import com.example.server.model.PregenUrl;
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
import com.google.cloud.bigtable.data.v2.models.*;
import com.google.common.collect.Lists;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PregenUrlDao {

    private String tableId;
    private final BigtableDataClient dataClient;
    private final BigtableTableAdminClient adminClient;
    private static PregenUrlDao ONLY;

    public static PregenUrlDao make() throws IOException {
        if (ONLY == null) {
            ONLY = new PregenUrlDao("shortify_pregen");
        }
        return ONLY;
    }

    private PregenUrlDao(String tableId) throws IOException {
//        String serviceAccountPath = "/home/mc143/url-shortener-shortify/server/service-account.json";
        String serviceAccountPath = "./service-account.json";
        String projectId = "rice-comp-539-fall-2021";
        String instanceId = "comp-539-fall-2021";
        this.tableId = tableId;

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountPath))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        // [START bigtable_hw_connect]
        BigtableDataSettings settings =
                BigtableDataSettings.newBuilder().setProjectId(projectId)
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

    public void insertValue(PregenUrl value) {
        // [START bigtable_hw_insert_value]
        String columnFamily1 = "Essential";
        String columnQualifier1 = "shortUrl";
        String columnQualifier2 = "timeStamp";
        try {
            RowMutation rowMutation =
                    RowMutation.create(tableId, value.getShortUrl())
                            .setCell(columnFamily1, columnQualifier1, value.getShortUrl())
                            .setCell(columnFamily1, columnQualifier2, value.getTimeStamp());
            dataClient.mutateRow(rowMutation);
        } catch (NotFoundException e) {
            System.err.println("Failed to write value to table: " + e.getMessage());
            System.err.println("Failed to write key: " + value.getShortUrl());
        }
        // [END bigtable_hw_insert_value]
    }

    public void bulkInsertValue(List<PregenUrl> values) {
        // [START bigtable_hw_insert_value]
        String columnFamily1 = "Essential";
        String columnQualifier1 = "shortUrl";
        String columnQualifier2 = "timeStamp";
        try {
            BulkMutation bulkMutation = BulkMutation.create(tableId);
            for (PregenUrl value : values) {
                RowMutationEntry rowMutation =
                        RowMutationEntry.create(value.getShortUrl())
                                .setCell(columnFamily1, columnQualifier1, value.getShortUrl())
                                .setCell(columnFamily1, columnQualifier2, value.getTimeStamp());
                bulkMutation.add(rowMutation);
            }
            dataClient.bulkMutateRows(bulkMutation);
        } catch (NotFoundException e) {
            System.err.println("Failed to bulk write value to table: " + e.getMessage());
        }
        // [END bigtable_hw_insert_value]
    }

    public PregenUrl getAndDeleteKey() {
        // [START bigtable_hw_get_and_delete_by_key]
        try {
//            if (left < right && checkKey(String.valueOf(left))) {
//                PregenUrl.PregenUrlBuilder result = PregenUrl.builder();
//                List<RowCell> listOfCells = dataClient.readRow(tableId, String.valueOf(left)).getCells();
//                for (RowCell cell : listOfCells) {
//                    // String family = cell.getFamily();
//                    String column = cell.getQualifier().toStringUtf8();
//                    String value = cell.getValue().toStringUtf8();
//                    switch (column) {
//                        case "index":
//                            result.index(value);
//                            break;
//                        case "shortUrl":
//                            result.shortUrl(value);
//                            break;
//                        case "timeStamp":
//                            result.timeStamp(value);
//                            break;
//                    }
//                }
//                dataClient.mutateRowAsync(RowMutation.create(tableId, String.valueOf(left)).deleteRow());
//                left++;
//                return result.build();
                Query query = Query.create(tableId);
                ServerStream<Row> stream = dataClient.readRows(query);
                int count = 0;
                for (Row row : stream) {
                    if (++count > 1) {
                        stream.cancel();
                        break;
                    }
                    PregenUrl.PregenUrlBuilder result = PregenUrl.builder();
                    List<RowCell> listOfCells = row.getCells();
                    for (RowCell cell : listOfCells) {
                        // String family = cell.getFamily();
                        String column = cell.getQualifier().toStringUtf8();
                        String value = cell.getValue().toStringUtf8();
                        switch (column) {
                            case "shortUrl":
                                result.shortUrl(value);
                                break;
                            case "timeStamp":
                                result.timeStamp(value);
                                break;
                        }
                    }
//                    System.out.println("Deleting row: " + row.getKey());
                    dataClient.mutateRow(RowMutation.create(tableId, row.getKey()).deleteRow());
                    return result.build();
                }
//                else {
//                System.out.println("Not enough key");
//            }
        } catch (NotFoundException e) {
            System.err.println("Failed to get and delete value from table: " + e.getMessage());
        }
        return null;
        // [END bigtable_hw_get_and_delete_by_key]
    }

    public Set<String> getAndDeleteKey(int q) {
        // [START bigtable_hw_get_and_delete_by_key]
        Set<String> pregenUrlSet = new HashSet<>();
        while (pregenUrlSet.size() < q) {
            pregenUrlSet.add(getAndDeleteKey().getShortUrl());
        }
        return pregenUrlSet;
        // [END bigtable_hw_get_and_delete_by_key]
    }

    public void deleteKey(String key) {
        // [START bigtable_hw_get_and_delete_by_key]
        try {
//            if (left < right && checkKey(String.valueOf(left))) {
//                PregenUrl.PregenUrlBuilder result = PregenUrl.builder();
//                List<RowCell> listOfCells = dataClient.readRow(tableId, String.valueOf(left)).getCells();
//                for (RowCell cell : listOfCells) {
//                    // String family = cell.getFamily();
//                    String column = cell.getQualifier().toStringUtf8();
//                    String value = cell.getValue().toStringUtf8();
//                    switch (column) {
//                        case "index":
//                            result.index(value);
//                            break;
//                        case "shortUrl":
//                            result.shortUrl(value);
//                            break;
//                        case "timeStamp":
//                            result.timeStamp(value);
//                            break;
//                    }
//                }
//                dataClient.mutateRowAsync(RowMutation.create(tableId, String.valueOf(left)).deleteRow());
//                left++;
//                return result.build();
            Query query = Query.create(tableId);
            ServerStream<Row> stream = dataClient.readRows(query);
            for (Row row : stream) {
                List<RowCell> listOfCells = row.getCells();
                for (RowCell cell : listOfCells) {
                    // String family = cell.getFamily();
                    String column = cell.getQualifier().toStringUtf8();

                    if(column == "shortUrl") {
                        String value = cell.getValue().toStringUtf8();
                        if(value.equals(key)) {
                            System.out.println("Deleting Key from DB");
                            dataClient.mutateRow(RowMutation.create(tableId, row.getKey()).deleteRow());
                        }
                    }
                }
//                    System.out.println("Deleting row: " + row.getKey());
            }
//                else {
//                System.out.println("Not enough key");
//            }
        } catch (NotFoundException e) {
            System.err.println("Failed to get and delete value from table: " + e.getMessage());
        }
        // [END bigtable_hw_get_and_delete_by_key]
    }

    public List<User> readTable() {
        // [START bigtable_hw_read_table]
        try {
            System.out.println("\nReading the entire table");
            Query query = Query.create(tableId);
            ServerStream<Row> rowStream = dataClient.readRows(query);
            List<User> tableRows = new ArrayList<>();
            int count = 0;
            for (Row r : rowStream) {
                System.out.println("Row Key: " + r.getKey().toStringUtf8());
                User.UserBuilder userBuilder = User.builder();
//                for (RowCell cell : r.getCells()) {
//                    String column = cell.getQualifier().toStringUtf8();
//                    String value = cell.getValue().toStringUtf8();
//                    System.out.printf(
//                            "Family: %s    Qualifier: %s    Value: %s%n",
//                            cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
//                }
                User user =  userBuilder.build();
                tableRows.add(user);
                count++;
            }
            System.out.println("Total rows: " + count);
            return tableRows;
        } catch (NotFoundException e) {
            System.err.println("Failed to read a non-existent table: " + e.getMessage());
            return null;
        }
    }

}
// [END database]
