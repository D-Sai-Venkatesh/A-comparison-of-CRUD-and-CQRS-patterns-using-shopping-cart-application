# Akka Impimentation of shopping Cart

## Build docker image 
If you want to modify the code then plase create a docker image with the folowing command and update image name accordingly in kubernetes script. Otherwise just run kubernetes script it will automatically download images from my decker repository.
```
mvn compile jib:dockerBuild -Dimage=new_image_name
```
## Deploy
Install kustomize from https://kubectl.docs.kubernetes.io/installation/kustomize/binaries/
```
kubectl create namespace axon
```
```
kubectl config set-context --current --namespace=axon
```
```
kustomize build .k8s/base/deploy | kubectl apply -f -
```

Resources for this project will be added here soon.
