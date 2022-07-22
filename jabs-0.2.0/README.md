# Just Another Blockchain Simulator #

![GitHub](https://img.shields.io/github/license/hyajam/jabs)
![Build Status](https://travis-ci.com/hyajam/jabs.svg?branch=objectifiedNetworkAndSimulator)
![Twitter URL](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Ftwitter.com%2Fhabibyajam)

![Alt-Text](https://raw.githubusercontent.com/hyajam/jabs/objectifiedNetworkAndSimulator/img/Jabs-logo.png)

JABS - Just Another Blockchain Simulator.

JABS is a blockchain network simulator aimed at researching consensus 
algorithms for performance and security. it is designed to easily handel
simulation of networks as large as normal public blockchain networks 
(~10000 nodes) in reasonable time.


## Installation ##
There are two suggested installation methods Introduced here.
Installation on a Debian-based linux like Ubuntu and
installation using Dockerfile.

### Installation on Ubuntu 20.04 and 22.04 ### 

First, you should install `git`,  `openjdk-17-jdk`, and `maven`.

```shell script
$ sudo apt install git openjdk-17-jdk
$ sudo apt install maven
```

After that, clone the git repository.
```shell script
$ git clone https://github.com/hyajam/jabs.git
```

Use maven `mvn` to make a jar file.
```shell script
$ cd jabs
$ mvn clean install
```

You might need to set `JAVA_HOME` to java home directory (this 
might vary depending on your linux distribution and jdk version):

```shell
$ JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/; mvn clean install
```

Then to run the compiled jar file.

```shell script
$ java -jar target/jabs.jar
```


### Installation using Dockerfile ###
This installation method should work on any system that has `docker` installed.

First clone the repository using git.
```shell script
$ git clone https://github.com/hyajam/jabs.git
```

Build the docker image.
```shell script
$ docker build -t jabs .
```

Run the docker container.
```shell script
$ docker run -v "$(pwd)/_output":/app/_output -t jabs
```

If you want to prevent generating root owned files in `_output` directory use the following command in case you have
`id` command in your shell.
```shell script
$ docker run --rm --user $(id -u):$(id -g) -v "$(pwd)/_output":/app/_output -t jabs
```

There is a chance that your user id and group number is 1000. Thus, the following might work well.
```shell script
$ docker run --rm --user 1000:1000 -v "$(pwd)/_output":/app/_output -t jabs
```

The `Dockerfile` is designed to support caching dependencies
therefore by editing the source code you do not need to download 
new dependencies unless your changes include altering to `pom.xml` file.


## Quick Start ##



## Usage ##
Currently, you can use JABS by editing Main.java file and creating new scenarios for your tests.


## Supported Consensus algorithms ##
Currently, the following consensus algorithms are implemented in JABS: 
 1. Nakamoto Consensus
 2. Ghost protocol
 3. PBFT
 4. Casper FFG

## Roadmap ##

- [x] Add new block mining process that can support adjustable difficulty.
- [x] Add multiple loggers and scenarios in main class.
- [ ] Add detailed comparison with other simulators in README.md.
- [ ] Add new network latency stats that has countries instead of continents.
- [ ] Add events for network churn.
- [ ] Exploring possible multithreading options.
- [ ] Add new consensus algorithms:
    - [x] IOTA
    - [ ] Honey Badger BFT
    - [ ] DPoS

## Other Blockchain Simulators ##
 1. **Bitcoin Simulator**: developed in C++ and based on NS3. Can simulate Bitcoin, Litecoin, Dogecoin and probably other blockchain networks based on Nakamoto Consensus. Although it is probably the most accurate of the blockchain simulators it is fairly slow. Does not simulate transactions. (https://github.com/arthurgervais/Bitcoin-Simulator)
 2. **BlockSim**: developed in Python. Supports simulating both Bitcoin and Ethereum. Accurate but slow. Simulates transactions and connection handshakes. (https://github.com/carlosfaria94/blocksim)
 3. **SimBlock**: developed in Java. In current version supports only Nakamoto Consensus. Plans on adding GHOST protocol. Fast. Does not simulate transactions. SimBlock partially inspired this project. (https://github.com/dsg-titech/simblock)


## Contributing

Any contributions you make to JABS project are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star. Thanks.

Steps for code contribution to the project:

 1. Fork the Project
 2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
 3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
 4. Push to the Branch (`git push origin feature/AmazingFeature`)
 5. Open a Pull Request


## License ##

Distributed under the MIT License. See `LICENSE` for more information.

## Contact ##

Habib Yajam - [@habibyajam](https://twitter.com/HabibYajam) - habib.yajam@gmail.com