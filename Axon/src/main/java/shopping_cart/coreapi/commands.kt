package shopping_cart.coreapi

import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

class CreateShoppingCartCommand(
        @RoutingKey
        val shoppingCartId: UUID
)

data class AddItemCommand(
        @TargetAggregateIdentifier
        val shoppingCartId: UUID,
        val productId: String,
        val quantity: Int
)

data class AdjustItemQuantityCommand(
        @TargetAggregateIdentifier
        val shoppingCartId: UUID,
        val productId: String,
        val newQuantity: Int
)


data class RemoveItemCommand(
        @TargetAggregateIdentifier
        val shoppingCartId: UUID,
        val productId: String
)

data class CheckoutCommand(
        @TargetAggregateIdentifier
        val shoppingCartId: UUID
)

data class GetCartCommand(
        @TargetAggregateIdentifier
        val shoppingCartId: UUID
)
