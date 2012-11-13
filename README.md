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

## Installation

### Folder structure

```
 + opt/
    + freeling/
       + etc/
          - freeling (symbolic link to share/freeling)
    + wordlift/
       + etc/
    + stanbol/
       + bin/
          + rXXXXXXX/
          - stable (symbolic link to rXXXXXXX)
       + etc/
          - stanbol.conf
```

### Requirements

#### Maven 3

```sh
export MAVEN_OPTS="-Xmx512M -XX:MaxPermSize=128M"
```

In case of errors during the compilation try to raise the values, e.g.
```sh
export MAVEN_OPTS="-Xmx1024M -XX:MaxPermSize=512M"
```

### Install Stanbol

For reference: http://stanbol.apache.org/docs/trunk/tutorial.html

We suggest to create the following folder where to keep Apache Stanbol files:
```sh
 /opt/stanbol/bin
```

Then checkout Apache Stanbol in that folder:
```sh
svn co http://svn.apache.org/repos/asf/stanbol/trunk /opt/stanbol/bin/stanbol
```

Once the checkout is complete, run the installation *with tests*:
```sh
mvn clean install
```

or *without tests*:
```sh
mvn -DskipTests clean install
```

In case of memory errors during the installation, try to change the **maven** options as described in the requirements paragraph above.

### Run Stanbol

Create a *symbolic link* **stable** to Apache Stanbol (this will allow you in the future to update and test your installation before switching to a new release):
```sh
 ln -s /opt/stanbol/bin/stanbol /opt/stanbol/bin/stable
```

Create a *run* folder for Apache Stanbol:
```sh
 /opt/stanbol/var/run/1
```

Then launch Apache Stanbol from the *run* folder; replace {version} with the current version of Apache Stanbol:
```sh
cd /opt/stanbol/var/run/1
java -Xmx1g \
 -jar ../../../bin/stable/launchers/stable/target/org.apache.stanbol.launchers.stable-{version}.jar
```

For example, to run *0.10.0-SNAPSHOT*:
```sh
java -Xmx1g \
 -jar ../../../bin/stable/launchers/stable/target/org.apache.stanbol.launchers.stable-0.10.0-SNAPSHOT.jar
```

Check that Apache Stanbol is working by opening the following URL with your browser (replace *localhost* with the server name):
```sh
http://localhost:8080
```

### Install Freeling

Reference:
http://nlp.lsi.upc.edu/freeling/index.php?option=com_content&task=view&id=25&Itemid=62

#### Install on Ubuntu 12.04

Download **Freeling 3.0** from `http://devel.cpl.upc.edu/freeling/downloads/21`:
```sh
curl -o freeling-3.0.tar.gz http://devel.cpl.upc.edu/freeling/downloads/21
```

Then extract the archive and follow the instructions at this address to compile and install Freeling:
`http://nlp.lsi.upc.edu/freeling/doc/userman/userman.pdf`

#### Install on Mac OS X

use Brew

chmod 755 install-sh

Brew

icu4c 49.1.2
brew install icu4c

brew install --HEAD boost --with-icu

brew install https://raw.github.com/mxcl/homebrew/0d8d92bfcd00f42d6af777ba8bf548cbd5502638/Library/Formula/swig.rb

brew install freeling

#### Freeling Java APIs

##### Install Java APIs

JDK 6.0 is required.

Overwrite the `freeling-HEAD/APIs/commong/freeling.i` file with the one provided here:
`https://raw.github.com/ziodave/freeling-brew-formula/master/APIs/common/freeling.i`

freeling-HEAD/APIs/java

Fix the `freeling-HEAD/APIs/java/Makefile` file by setting the correct parameters:
```
FREELINGDIR=/usr/local
SWIGDIR=/usr/share/swig2.0
JAVADIR=/usr/lib/jvm/jdk1.6.0_32
```

java -> $(JAVADIR)/bin/java
jar -> $(JAVADIR)/bin/jar


#### Load the Java APIs in the Maven repository

```sh
mvn install:install-file \
    -Dfile=freeling.jar \
    -DgroupId=edu.upc.freeling \
    -DartifactId=edu.upc.freeling \
    -Dversion=3.0 \
    -Dpackaging=jar
```

## Configuration

### Freeling Configuration

Create the following folders:
```sh
 /opt/freeling
  - etc
```

In the *etc* folder, create a *symbolic link* to `/usr/local/share/freeling` (or where the share/freeling folder is located, e.g. `/usr/local/Cellar/freeling/3.0/share/freeling` on a Mac OS X install using *brew*).

### Stanbol Configuration

Most of the configuration can be done by opening the following url with a Web browser when Apache Stanbol is running: `http://{server}/system/console/components`.

#### Freebase Entity Recognition engine

Configure "InsideOut10 for Stanbol: Freebase Entity Recognition engine", by giving a name to its instance, e.g. "freebase-entityrecognition", then start it.

#### Freeling Language Identifier engine

Edit (or create) the file:
`stanbol/config/io/insideout/wordlift/org/apache/stanbol/enhancer/engines/freeling/impl/LanguageIdentifierImpl.config`

from the run folder `/opt/stanbol/var/run/1/`.

Set the following configuration parameters. Please ensure that the specified paths and files exist:
```
service.bundleLocation="inputstream:freeling-engine-1.0-SNAPSHOT.jar"
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.locale="default"service.pid="
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl.LanguageIdentifierImpl"
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.path="/opt/freeling/etc/languageIdentifierConfiguration.cfg"
```

#### Schema.org Refactorer engine

Configure "InsideOut10 for Stanbol: Schema.org refactorer engine", by giving a name to its instance, e.g. "schemaorg-refactorer", then start it.

##### Schema.org Refafactorer implementation component

Edit (or create) the file:
`stanbol/config/io/insideout/wordlift/org/apache/stanbol/enhancer/engines/schemaorg/impl/SchemaOrgRefactorerImpl.config`

from the run folder `/opt/stanbol/var/run/1/`.

Set the following configuration parameters. Please ensure that the specified paths and files exist:
```
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.mappings.dbpedia.path="/opt/freeling/etc/dbpedia-mappings.rdf"
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.rules.custom.path="/opt/freeling/etc/custom-rules.rules"
service.bundleLocation="inputstream:schemaorg-engine-1.0-SNAPSHOT.jar"
service.pid="io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.impl.SchemaOrgRefactorerImpl"
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.mappings.custom.path="/opt/freeling/etc/custom-mappings.rdf"
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.type.uri=["http://schema.org/Place","http://schema.org/GeoCoordinates","http://schema.org/Product","http://schema.org/Person","http://schema.org/Organization","http://schema.org/Event","http://schema.org/CreativeWork","http://schema.org/MedicalEntity"]
```

#### TextAnnotation NewModel engine

Configure "TextAnnotation Model Engine", by giving a name to its instance, e.g. "textannotations-newmodel", then start it.

#### Freeling PoS Tagging engine

Edit (or create) the file:
`stanbol/config/io/insideout/wordlift/org/apache/stanbol/enhancer/engines/freeling/impl/PartOfSpeechTaggingImpl.config`

from the run folder `/opt/stanbol/var/run/1/`.

```
service.bundleLocation="inputstream:freeling-engine-1.0-SNAPSHOT.jar"
service.pid="io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.impl.PartOfSpeechTaggingImpl"
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.languages=["en","es","it","pt","ru"]
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.file.suffix=".cfg"
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.share.path="/opt/freeling/etc/freeling"
io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.configuration.path="/opt/freeling/etc/freeling/config"
```

Configure "InsideOut10 for Stanbol: Freeling PoS Tagging Engine", by giving a name to its instance, e.g. "freeling-postagging".

#### PoS Tagging engine

Configure "PartOfSpeechTagging Engine"

ApiServiceImpl
start manually

### Install Apache Stanbol Engines (Freebase, Freeling, Schema.org, WordLift)

Checkout sources:
```sh
git clone https://github.com/insideout10/wordlift-stanbol.git
```

Compile and install; replace {server} with your server name:
```sh
mvn clean install -PinstallBundle -Dsling.url=http://{server}/system/console
```

### Configure the default chain

From the Apache Stanbol web site, reconfigure the default chain to use the following engines:
`freelingLanguageIdentifier, freeling-postagging, freebase-entityrecognition, textannotations-newmodel, schemaorg-refactorer`

### Configure the cache strategy for the DBpedia site

It is suggested to change the cache strategy for the DBpedia referenced site from *All* to *Used* or *None*.

### Stop unused engines

You can safely stop unused engines to reduce resource usage and start-up times.

## Execution

```sh
java -Xmx1g -jar ../../../bin/stable/launchers/stable/target/org.apache.stanbol.launchers.stable-0.10.0-SNAPSHOT.jar
```

## Sample

A sample language configuration file includes references to the following files:
```
TokenizerFile=$FREELINGSHARE/it/tokenizer.dat
SplitterFile=$FREELINGSHARE/it/splitter.dat
LocutionsFile=$FREELINGSHARE/it/locucions.dat 
QuantitiesFile=$FREELINGSHARE/common/quantities_default.dat
AffixFile=$FREELINGSHARE/it/afixos.dat
ProbabilityFile=$FREELINGSHARE/it/probabilitats.dat
NPDataFile=$FREELINGSHARE/it/np.dat
PunctuationFile=$FREELINGSHARE/common/punct.dat
DictionaryFile=$FREELINGSHARE/it/dicc.src
*** CorrectorFile=$FREELINGSHARE/it/corrector/corrector.dat
*** PhoneticsFile=$FREELINGSHARE/it/phonetics.dat
SenseConfigFile=$FREELINGSHARE/it/senses.dat
UKBConfigFile=$FREELINGSHARE/it/ukb.dat
TaggerHMMFile=$FREELINGSHARE/it/tagger.dat
TaggerRelaxFile=$FREELINGSHARE/it/constr_gram.dat
*** GrammarFile=$FREELINGSHARE/it/chunker/grammar-chunk.dat
*** DepTxalaFile=$FREELINGSHARE/it/dep/dependences.dat
*** CorefFile=$FREELINGSHARE/it/coref/coref.dat
```

Ensure that all the files are found, or the underlying Freeling framework may fail and terminate Apache Stanbol in an unexpected way.