WordLift for Stanbol
====================

Please note that this README is still work in progress. Help us improve it by sending your feed on the [GitHub issues page](https://github.com/insideout10/wordlift-stanbol/issues).

## About

**WordLift for Stanbol** are a number of components and engines that extend some features of [Apache Stanbol](http://incubator.apache.org/stanbol).

These features work together with the [WordLift](http://wordlift.it) plug-in for WordPress in order to bring Semantic Web lifting to WordPress-based web sites.

## Installation and Configuration

### Requirements

* **Apache Stanbol** is required to use these components. You can install Apache Stanbol by following the instructions at [Stanbol Getting Started Guide](http://incubator.apache.org/stanbol/docs/trunk/tutorial.html) or by [contacting us for support](http://wordlift.insideout.io/#about-us).
* **Freeling** is a requirement for the Freeling engine. In order to install and configure Freeling refer to the [installation guide](http://nlp.lsi.upc.edu/freeling/index.php?option=com_content&task=view&id=15&Itemid=44) and to the [support forums](http://nlp.lsi.upc.edu/freeling/index.php?option=com_simpleboard&Itemid=65&func=showcat&catid=5).
* general installation tasks make use of [Maven](http://maven.apache.org/what-is-maven.html) and of [Apache Felix](http://felix.apache.org/site/index.html), therefore some minimal knowledge and understanding of these software is required.

### Installation

Following are the instructions to download and install the WordLift components for Stanbol.

#### Get the source code

Get the source code from Git:

```sh
git clone https://github.com/insideout10/wordlift-stanbol.git
```

#### Edit the pom.xml

The sources need to find the Stanbol source tree on your computer. Change the *relativePath* in the [pom.xml](https://github.com/insideout10/wordlift-stanbol/blob/master/pom.xml) to point to the *stanbol/parent* folder.

#### Run Stanbol

Stanbol must be running before you start the installation of components. Ensure Stanbol is available at *http://localhost:8080/*.

#### Start installation

Start the build and installation of components by running the following command:

```sh
mvn clean install -DskipTests -PinstallBundle -Dsling.url=http://localhost:8080/system/console -o -e
```

#### Configuration

One installation is complete, open the Apache Felix components page to configure and start the single components at http://localhost:8080/system/console/components.

## RoadMap

Improve the unit tests.

## License