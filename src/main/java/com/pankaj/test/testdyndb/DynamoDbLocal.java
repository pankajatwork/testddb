package com.pankaj.test.testdyndb;

/*
 * Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;

/**
 * This class demonstrates how to use DynamoDB Local as a test fixture.
 * @author Alexander Patrikalakis
 */
public class DynamoDbLocal {
    /**
     * You can use mvn to run DynamoDBLocalFixture, e.g.
     * <p>
     * $ mvn clean package
     * <p>
     * $ mvn exec:java -Dexec.mainClass="com.amazonaws.services.dynamodbv2.DynamoDBLocalFixture" \
     * -Dexec.classpathScope="test" \
     * -Dsqlite4java.library.path=target/dependencies
     * <p>
     * It's recommended to run "aws configure" one time before you run DynamoDBLocalFixture
     *
     * @param args - no args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //AwsDynamoDbHelper.initSqLite();
        System.setProperty("sqlite4java.library.path", "C:\\Users\\panka\\.m2\\repository\\com\\almworks\\sqlite4java\\sqlite4java-win32-x64\\1.0.392");

        AmazonDynamoDB dynamodb = null;
//        try {
//            // Create an in-memory and in-process instance of DynamoDB Local that skips HTTP
//            System.out.println("Starting ddb");
//            dynamodb = DynamoDBEmbedded.create().amazonDynamoDB();
//            // use the DynamoDB API with DynamoDBEmbedded
//            listTables(dynamodb.listTables(), "DynamoDB Embedded");
//            System.out.println("completed ddb");
//        } finally {
//            // Shutdown the thread pools in DynamoDB Local / Embedded
//            if(dynamodb != null) {
//                dynamodb.shutdown();
//            }
//        }

        // Create an in-memory and in-process instance of DynamoDB Local that runs over HTTP
        final String[] localArgs = { //"-inMemory",
                "-dbPath", "."};
        DynamoDBProxyServer server = null;
        try {
            System.out.println("Starting inmemory db");
            server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            System.out.println("starting server : " + server);
            server.start();
            System.out.println("server started");

            dynamodb = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                    // we can use any region here
                    new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                    .build();

            // use the DynamoDB API over HTTP
            System.out.println("listing tables");
            listTables(dynamodb.listTables(), "DynamoDB Local over HTTP");
            System.out.println("completed inmemory db");
            Thread.sleep(300000);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            // Stop the DynamoDB Local endpoint
//            if(server != null) {
//                server.stop();
//            }
        }
    }

    public static void listTables(ListTablesResult result, String method) {
        System.out.println("found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
        for(String table : result.getTableNames()) {
            System.out.println(table);
        }
    }
}