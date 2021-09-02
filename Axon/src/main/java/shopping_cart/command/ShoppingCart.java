package shopping_cart.command;


import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import shopping_cart.ExceptionHandlers.ResponseError;
import shopping_cart.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Profile("command")
@Aggregate
class ShoppingCart {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCart.class);

    @AggregateIdentifier
    private UUID shoppingCartId;
    private Map<String, Integer> selectedProducts;
    private boolean confirmed;

    public ShoppingCart() {
        // Required by Axon
    }

    @CommandHandler
    public ShoppingCart(CreateShoppingCartCommand command) {
        logger.info("[Command] cart " + command.getShoppingCartId() + " created");
        AggregateLifecycle.apply(new ShoppingCartCreatedEvent(command.getShoppingCartId()));
    }

    @CommandHandler
    public ShoppingCartDTO handle(AddItemCommand command) throws ItemAlreadyAddedException{
        if(selectedProducts.containsKey(command.getProductId())) {
            logger.error("Item " + command.getProductId() + " is already added in the cart " + shoppingCartId.toString());
            throw new ItemAlreadyAddedException(
                "Item " + command.getProductId() + " is already added in the cart " + shoppingCartId.toString()
            );
        }
        if(command.getQuantity() == 51) {
            int i = 100/0;
        }
        logger.info("[Command] Item " + command.getProductId() + " is added in the cart " + shoppingCartId.toString());
        AggregateLifecycle.apply(new ItemAddedEvent(shoppingCartId, command.getProductId(), command.getQuantity()));
        if(command.getQuantity() == 55) {
            int i = 100/0;
        }
        return new ShoppingCartDTO(shoppingCartId, selectedProducts, confirmed);
    }

    @CommandHandler
    public ShoppingCartDTO handle(AdjustItemQuantityCommand command) throws ItemNotFoundException{
        if(!selectedProducts.containsKey(command.getProductId())) {
            logger.error("Item " + command.getProductId() + " is not present in the cart " + shoppingCartId.toString());
            throw new ItemNotFoundException(
                "Item " + command.getProductId() + " is not present in the cart " + shoppingCartId.toString()
            );
        }
        if(command.getNewQuantity() == 51) {
            int i = 100/0;
        }
        logger.info("[Command] Item " + command.getProductId() + " is adjusted in the cart " + shoppingCartId.toString());
        AggregateLifecycle.apply(new ItemQuantityAdjustedEvent(shoppingCartId, command.getProductId(), selectedProducts.get(command.getProductId()), command.getNewQuantity()));
        if(command.getNewQuantity() == 55) {
            int i = 100/0;
        }
        return new ShoppingCartDTO(shoppingCartId, selectedProducts, confirmed);
    }

    @CommandHandler
    public ShoppingCartDTO handle(RemoveItemCommand command) throws ItemNotFoundException {
        String productId = command.getProductId();
        int quantity = selectedProducts.get(productId);

        if (!selectedProducts.containsKey(productId)) {
            logger.error("Item "+ productId +" is not present in the cart" + shoppingCartId.toString());
            throw new ItemNotFoundException(
                    "Item "+ productId +" is not present in the cart" + shoppingCartId.toString()
            );
        }

        logger.info("[Command] Item "+ productId +" is removed from the cart" + shoppingCartId.toString());
        AggregateLifecycle.apply(new RemovedItemEvent(shoppingCartId, productId, quantity));
        return new ShoppingCartDTO(shoppingCartId, selectedProducts, confirmed);
    }

    @CommandHandler
    public ShoppingCartDTO handle(CheckoutCommand command) throws CheckoutCartException{
        if (confirmed) {
            logger.error("Cart " + command.getShoppingCartId() + " has already been checked out");
            throw new CheckoutCartException(
                "Cart " + command.getShoppingCartId() + " has already been checked out"
            );
        }

        logger.info("[Command] Cart " + command.getShoppingCartId() + " has been checked out");
        AggregateLifecycle.apply(new CheckedoutEvent(shoppingCartId));
        //--------------------
        return new ShoppingCartDTO(shoppingCartId, selectedProducts, confirmed);
    }

    @CommandHandler
    public ShoppingCartDTO on(GetCartCommand command) {
        return new ShoppingCartDTO(shoppingCartId, selectedProducts, confirmed);
    }

    @EventSourcingHandler
    public void on(ShoppingCartCreatedEvent event) {
        shoppingCartId = event.getShoppingCartId();
        selectedProducts = new HashMap<>();
        confirmed = false;
    }

    @EventSourcingHandler
    public void on(ItemAddedEvent event) {
        if(event.getQuantity() == 52) {
            int i = 100/0;
        }
        selectedProducts.merge(event.getProductId(), event.getQuantity(), Integer::sum);
    }

    @EventSourcingHandler
    public void on(ItemQuantityAdjustedEvent event) {
        // selectedProducts.merge(event.getProductId(), event.getNewQuantity(), Integer::sum);
        if(event.getNewQuantity() == 52) {
            int i = 100/0;
        }
        selectedProducts.computeIfPresent(
            event.getProductId(),
            (productId, quantity) -> quantity += (event.getNewQuantity() - event.getOldQuantity())
        );
    }

    @EventSourcingHandler
    public void on(RemovedItemEvent event) {
        selectedProducts.remove(event.getProductId());
    }

    @EventSourcingHandler
    public void on(CheckedoutEvent event) {
        confirmed = true;
    }

}
