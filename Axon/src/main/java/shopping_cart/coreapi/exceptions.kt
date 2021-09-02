package shopping_cart.coreapi

class ItemNotFoundException(message: String) : Exception(message)
class ItemAlreadyAddedException(message: String) : Exception(message)
class CheckoutCartException(message: String) : Exception(message)
