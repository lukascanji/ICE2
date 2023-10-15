package ca.gbc.productservice.controller;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.gbc.productservice.service.ProductServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

     private final ProductServiceImpl productService;

     @PostMapping
     @ResponseStatus(HttpStatus.CREATED)
     public void createProduct(@RequestBody ProductRequest productRequest){
         productService.createProduct(productRequest);
     }

     @GetMapping
     @ResponseStatus(HttpStatus.OK)
     public List<ProductResponse> getAllProducts(){
         return productService.getAllProducts();
     }

     //http://localhost:8080/api/product/1
//     @PutMapping({"/productId"})
     @PutMapping("/{productId}")

     public ResponseEntity<?> updateProduct(@PathVariable("productId") String productId,
                                            @RequestBody ProductRequest productRequest) {
         String updatedProductId = productService.updateProduct(productId, productRequest);

         HttpHeaders headers = new HttpHeaders();
         headers.add("Location", "/api/product/" + updatedProductId);

         return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
     }


     //http://localhost:8080/api/product/5f9b1b3b1d9b7c1b4c3b3b1a -> 5f9b1b3b1d9b7c1b4c3b3b1a
     @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId){
         productService.deleteProduct(productId);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }


}
