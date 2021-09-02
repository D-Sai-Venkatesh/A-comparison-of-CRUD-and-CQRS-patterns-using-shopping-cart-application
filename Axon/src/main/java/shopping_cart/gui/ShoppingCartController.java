package shopping_cart.gui;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shopping_cart.command.ShoppingCartDTO;
import shopping_cart.coreapi.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping_cart.query.PopularityView;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequestMapping("/shoppingCart")
@RestController
class ShoppingCartController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    public ShoppingCartController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        logger.info("[REST-Controller] Shopping Cart Controller has started");
    }

    @PostMapping("/create")
    public CompletableFuture<UUID> createShoppingCart() {
        UUID temp = UUID.randomUUID();
        logger.info("[REST-Controller] received request to create Shopping Cart {}", temp.toString());
        return commandGateway.send(new CreateShoppingCartCommand(temp));
    }

    @PostMapping("/{shoppingCartId}/addItem/{productId}/quantity/{quantity}")
    public CompletableFuture<ShoppingCartDTO> addItem(@PathVariable("shoppingCartId") String shoppingCartId,
                                             @PathVariable("productId") String productId,
                                             @PathVariable("quantity") Integer quantity) {
        logger.info("[REST-Controller] received request to add Item {} shopping cart {} with quantity {}",
            productId, shoppingCartId, quantity);
        if(quantity == 54) {
            int i = 100/0;
        }
        return commandGateway.send(new AddItemCommand(
                UUID.fromString(shoppingCartId), productId, quantity
        ));
    }

    @PostMapping("/{shoppingCartId}/adjustItemQuantity/{productId}/quantity/{quantity}")
    public CompletableFuture<ShoppingCartDTO> adjustItemQuantity(@PathVariable("shoppingCartId") String shoppingCartId,
                              @PathVariable("productId") String productId,
                              @PathVariable("quantity") Integer quantity) {
        logger.info("[REST-Controller] received request to modify Item {} in shopping cart {} to quantity {}",
            productId, shoppingCartId, quantity);
        if(quantity == 54) {
            int i = 100/0;
        }
        return commandGateway.send(new AdjustItemQuantityCommand(
            UUID.fromString(shoppingCartId), productId, quantity
        ));
    }

    @PostMapping("/{shoppingCartId}/removeItem/{productId}")
    public CompletableFuture<ShoppingCartDTO> removeItem(@PathVariable("shoppingCartId") String shoppingCartId,
                                @PathVariable("productId") String productId) {
        logger.info("[REST-Controller] received request to remove Item {} from shopping cart {}",
            productId, shoppingCartId);
        return commandGateway.send(new RemoveItemCommand(
                UUID.fromString(shoppingCartId), productId));
    }


    @GetMapping("/{itemId}")
    public CompletableFuture<PopularityView> findPopularity(@PathVariable("itemId") String itemId) {
        logger.info("[REST-Controller] received request to find popularity of Item {} ",
            itemId);
        return queryGateway.query(
                new ItemPopularityQuery(itemId),
                ResponseTypes.instanceOf(PopularityView.class)
        );
    }

    @GetMapping("/{shoppingCartId}/getcart")
    public CompletableFuture<ShoppingCartDTO> getCart(@PathVariable("shoppingCartId") String shoppingCartId) {
        logger.info("[REST-Controller] received request to get cart {}",
            shoppingCartId);
        return commandGateway.send(new GetCartCommand(UUID.fromString(shoppingCartId)));
    }
}
