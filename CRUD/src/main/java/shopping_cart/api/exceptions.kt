package shopping_cart.api

class ItemNotFoundException(message: String) : Exception(message)
class CartNotFoundException(message: String) : Exception(message)
class ItemAlreadyAddedException(message: String) : Exception(message)
class CheckoutCartException(message: String) : Exception(message)
