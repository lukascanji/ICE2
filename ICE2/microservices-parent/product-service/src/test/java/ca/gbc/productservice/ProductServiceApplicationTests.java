//Added Sept 26th Lab

package ca.gbc.productservice;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultHandlersDsl;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


//import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
//import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;



import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    private ProductRequest getProductRequest()
    {
        return ProductRequest.builder()
                .name("Apple iPad 2023")
                .description("Apple iPad 2023")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

    private List<Product> getProductList() {

        List<Product> productList = new ArrayList<>();
        UUID uuid = UUID.randomUUID();

        Product product = Product.builder()
                .id(uuid.toString()) //was originally id
                .name("Apple iPad 2023")
                .description("Apple iPad 2023")
                .price(BigDecimal.valueOf(1200))
                .build();

        productList.add(product);
        return productList;

    }

    private String convertObjectToJson(List<ProductResponse> productList)
            throws JsonProcessingException {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(productList);
                //return objectMapper.writeValueAsString(object);
    }

    private List<ProductResponse> convertJsonStringToObject(String jsonString)
            throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(jsonString, new TypeReference<List<ProductResponse>>() {
                });
    }

    /*
    *POST
    * * @throws Excption
    *
     */

    @Test
    void createProduct() throws Exception {

        ProductRequest product = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType("application/json")
                .content(productRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertTrue(productRepository.findAll().size() > 0);

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Apple iPad 2023"));
        List<Product> products = mongoTemplate.find(query, Product.class);
        Assertions.assertTrue(products.size() > 0);


    }

    /*BDD - behaviour driven development
    //given - setup
    when - action
    then - verify
     */
//    @Test
//    void getAllProducts() throws Exception{
//
//        //GIVEN
//
//        productRepository.saveAll(getProductList());
//
//        //WHEN
//        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
//                .get("/api/product")
//                .accept(MediaType.APPLICATION_JSON));
//
//        //THEN
//        response.andExpect(MockMvcResultMatchers.status().isOk());
//        response.andDo(MockMvcResultHandlers.print());
//
//        MvcResult result = response.andReturn();
//        String jsonResponse = result.getResponse().getContentAsString();
//        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);
//
//        int actualSize = jsonNodes.size();
//        int expectedSize = getProductList().size();
//
//        Assertions.assertEquals(expectedSize, actualSize);
//
//    }

    @Test
    void getAllProducts() throws Exception {
        // Given: Clear existing products and add the expected product
        productRepository.deleteAll();
        Product expectedProduct = new Product("1", "Widget", "A useful widget", new BigDecimal("19.99"));
        productRepository.save(expectedProduct);

        // When: Retrieve all products
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/product")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then: Validate the response
        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Product> actualProducts = mapper.readValue(jsonResponse, new TypeReference<List<Product>>(){});
        System.out.println("Returned products: " + actualProducts);

        int expectedSize = 1;
        int actualSize = actualProducts.size();
        Assertions.assertEquals(expectedSize, actualSize);
    }



    @Test
    void updateProduct() throws Exception{

        //GIVEN

        Product savedProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Widget")
                .description("Widget Original Price")
                .price(BigDecimal.valueOf(100))
                .build();

        //Saved product with original price
        productRepository.save(savedProduct);

        //prepare updated [roduct and productResuest
        savedProduct.setPrice(BigDecimal.valueOf(200));
        String productRequestString = objectMapper.writeValueAsString(savedProduct);


        //WHEN
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/product/" + savedProduct.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString));



        //THEN
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedProduct.getId()));
        Product storedProduct = mongoTemplate.findOne(query, Product.class);

        assertEquals(savedProduct.getPrice(), storedProduct.getPrice());




    }

    @Test
    void deleteProduct() throws Exception{

        //GIVEN

        Product savedProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Java Microservices Programming")
                .description("Course Textbook - Java Microservices Programming")
                .price(BigDecimal.valueOf(200))
                .build();

        productRepository.save(savedProduct);

        //WHEN

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/product/" + savedProduct.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON));


        //THEN

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedProduct.getId()));
        Long productCount = mongoTemplate.count(query, Product.class);

        assertEquals(0, productCount);



    }


}
