package shopping_cart.gui;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping_cart.api.*;
import shopping_cart.query.PopularityView;
import shopping_cart.query.ShoppingCartView;
import shopping_cart.service.PopularityDTO;
import shopping_cart.service.ShoppingCart;
import shopping_cart.service.ShoppingCartDTO;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestMapping("/shoppingCart")
@RestController
class ShoppingCartController {


    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);


    @Autowired
    ShoppingCart shoppingCart;

    @PostMapping("/create")
    public ResponseEntity<Object> createShoppingCart() {
        UUID temp = UUID.randomUUID();
        logger.info("[REST-Controller] received request to create Shopping Cart {}", temp.toString());
        return new ResponseEntity<>(shoppingCart.CreateShoppingCart(new CreateShoppingCartMsg(temp)), HttpStatus.OK);
    }

    @PostMapping("/{shoppingCartId}/addItem/{productId}/quantity/{quantity}")
    public ResponseEntity<Object> addItem(@PathVariable("shoppingCartId") String shoppingCartId,
                                                      @PathVariable("productId") String productId,
                                                      @PathVariable("quantity") Integer quantity) {
        logger.info("[REST-Controller] received request to add Item {} shopping cart {} with quantity {}",
            productId, shoppingCartId, quantity);
        if(quantity == 54) {
            int i = 100/0;
        }
        try {
            shoppingCart.AddItem(new AddItemMsg(UUID.fromString(shoppingCartId), productId, quantity));
        }
        catch (CartNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
        catch (ItemAlreadyAddedException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Item added successfully", HttpStatus.OK);
    }

    @PostMapping("/{shoppingCartId}/adjustItemQuantity/{productId}/quantity/{quantity}")
    public ResponseEntity<Object> adjustItemQuantity(@PathVariable("shoppingCartId") String shoppingCartId,
                              @PathVariable("productId") String productId,
                              @PathVariable("quantity") Integer quantity) {
        logger.info("[REST-Controller] received request to modify Item {} in shopping cart {} to quantity {}",
            productId, shoppingCartId, quantity);
        if(quantity == 54) {
            int i = 100/0;
        }
        try {
            shoppingCart.AdjustItemQuantity(new AdjustItemQuantityMsg(UUID.fromString(shoppingCartId), productId, quantity));
        }
        catch (CartNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
        catch (ItemNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Item adjusted successfully", HttpStatus.OK);
    }

    @PostMapping("/{shoppingCartId}/removeItem/{productId}")
    public ResponseEntity<Object> removeItem(@PathVariable("shoppingCartId") String shoppingCartId,
                                @PathVariable("productId") String productId) {
        logger.info("[REST-Controller] received request to remove Item {} from shopping cart {}",
            productId, shoppingCartId);

        try {
            shoppingCart.RemoveItem(new RemoveItemMsg(UUID.fromString(shoppingCartId), productId));
        }
        catch (CartNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
        catch (ItemNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Item removed successfully", HttpStatus.OK);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findPopularity(@PathVariable("itemId") String itemId) {
//        logger.info("[REST-Controller] received request to find popularity of Item {} ",
//            itemId);
        try{
            PopularityDTO temp = shoppingCart.GetPopularity(new GetPopularityMsg(itemId));
            return new ResponseEntity<>(temp, HttpStatus.OK);
        }
        catch (ItemNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/{shoppingCartId}/getcart")
    public ResponseEntity<Object> getCart(@PathVariable("shoppingCartId") String shoppingCartId) {
        logger.info("[REST-Controller] received request to get cart {}",
            shoppingCartId);

        try{
            ShoppingCartView temp = shoppingCart.GetCart(new GetCartMsg(UUID.fromString(shoppingCartId)));
            return new ResponseEntity<>(temp, HttpStatus.OK);
        }
        catch(CartNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }


    }
}
