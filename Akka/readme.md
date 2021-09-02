# Akka Impimentation of shopping Cart
This implementation has been borrowed from https://developer.lightbend.com/docs/akka-platform-guide/microservices-tutorial/index.html.
We have added yaml scripts to deploy this shopping cart on kubernetes

## Create Docker Images
You can directly jump into deployment step if you do not need to modify the application. If you create new images please change the docker image names in the kubernetes scripts accordingly.  

### Create a table initialized Postgres image
docker build -t vazravasu/shopping-cart-akka-db ./docker_images/postgres
### Create image for shopping cart 
mvn clean package docker:build

docker tag shopping-cart-service:latest <new name>

## Deploy

### Create a namespace using following command
kubectl create namespace shopping-1

### Change default kubectl namespace
kubectl config set-context --current --namespace=shopping-1
  
kubectl apply -f ./kubernetes/db.yml

kubectl apply -f ./kubernetes/zookeeper.yml

kubectl apply -f ./kubernetes/kafka.yml

kubectl apply -f ./kubernetes/my-service.yml

## Expose
kubectl expose deployment shopping --type=LoadBalancer --name=my-service  
  
## Intercting with the application 
For this we have to install grpcurl from https://github.com/fullstorydev/grpcurl.
plase replase the ip address of the external endpoint of the loadbalancer 

grpcurl -d '{"cartId":"cart1", "items":[{"itemId":"socks", "quantity":3}, {"itemId":"t-shirt", "quantity":2}]}' -plaintext <ip-address> shoppingorder.ShoppingOrderService.Order

grpcurl -d '{"cartId":"cart1", "itemId":"scissors", "quantity":1}' -plaintext <ip-address> shoppingcart.ShoppingCartService.AddItem

grpcurl -d '{"cartId":"cart1"}' -plaintext <ip-address> shoppingcart.ShoppingCartService.Checkout
