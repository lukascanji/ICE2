//Added Sept 26th Lab
package ca.gbc.productservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public class AbstractContainerBaseTest {
   // static final MongoDBContainer Mongo_DB_CONTAINER;
    static final MongoDBContainer Mongo_DB_CONTAINER;

 static {
     Mongo_DB_CONTAINER = new MongoDBContainer("mongo:latest");
     Mongo_DB_CONTAINER.start();
 }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add("spring.data.mongodb.uri",
            Mongo_DB_CONTAINER::getReplicaSetUrl);
    }
}

