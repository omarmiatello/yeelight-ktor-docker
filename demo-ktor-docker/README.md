# yeelight-ktor-docker
Control your Xiaomi Yeelight lamp from Ktor (a Kotlin Web server).

## Building and running the Docker image

Build an application package:

```
./gradlew build
```

Build and tag an image:

```
docker build -t yeelight-web .
```

Start a container:

```
docker run -m512M --cpus 2 -it -p 8080:8080 --rm --name yee yeelight-web
```

Stop a container:

```
docker stop yee
```

With this command, we start Docker in a foreground mode. It will wait for the server to exit, it
will also respond to `Ctrl+C` to stop it. `-it` instructs Docker to allocate a terminal (*tty*) to pipe the stdout
and to respond to the interrupt key sequence.

Since our server is running in an isolated container now, we should tell Docker to expose a port so we can
actually access the server port. Parameter `-p 8080:8080` tells Docker to publish port 8080 from inside a container as a port 8080 on a local
machine. Thus, when you tell your browser to visit `localhost:8080` it will first reach out to Docker, and it will bridge
it into internal port `8080` for your application.

You can adjust memory with `-m512M` and number of exposed cpus with `--cpus 2`.

By default a container’s file system persists even after the container exits, so we supply `--rm` option to prevent
garbage piling up.

For more information about running a docker image please consult [docker run](https://docs.docker.com/engine/reference/run)
documentation.


## Start local web server or use Docker to:

Find devices
```
http://localhost:8080
```

Switch on/off devices (+ find devices if needed)
```
http://localhost:8080/toggle
```

Start "Police flow" (+ find devices if needed)
```
http://localhost:8080/police
```