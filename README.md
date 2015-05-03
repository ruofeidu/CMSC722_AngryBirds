# Deliberately Planning and Acting for Angry Birds with Refinement Methods

Angry Birds has been a popular game throughout the world since 2009. The goal of the game is to destroy all the pigs and as many obstacles as possible using a limited number of birds. Since the game environment is subject to change tremendously after each shot, a deterministic planning model is very likely to fail. In this paper, we integrate deliberately planning and acting for Angry Birds with refinement methods. Specifically, we design a refinement acting engine (RAE) based on ARP-interleave with Sequential Refinement Planning Engine (SeRPE). In addition, we implement greedy algorithm, Depth First Forward Search (DFFS) and A\* algorithm to perform the actor's deliberation functions. Eventually, we evaluate our agent to solve the web version of Angry Birds in Chrome using the client-server platform provided by the IJCAI 2015 AI Birds Competition. In our experiments, we find out that our agent using SeRPE with A\* algorithm greatly outperforms the agent using greedy algorithm or forward search without SeRPE. In this way, we prove the significance of refinement methods for planning in practice. Please see the supplementary video [here] [0] for more results.

### Authors

[Ruofei Du], [Zebao Gao], [Zheng Xu]

### Version
1.0.0

### Tech

Our project uses a number of softwares to work properly:

* [Google Chrome] - To web the web version of Angry Birds
* [Angry Birds Plugins] - Chrome extension to communicate between server and client. Under /plugins/
* [Marked] - a super fast port of Markdown to JavaScript
* [JRE] - Java SE Runtime Environment 8
* [JSON Simple] - under /external/json-simple-1.1.1.jar/
* [Jama] - under /external/Jama-1.0.2/
* [Jar in Jar Loader] - under /external/jar-in-jar-loader.zip
* [Common Codec] - under /external/ commons-codec-1.7.jar
* [WebSocket] - under /external/ WebSocket.jar

### Run

The main class is under the package of ab.demo.MainEntry; a simple run can be:

```sh
java -jar project.jar -ai
```

All possible command

```sh
-g [level] // greedy algorithm
-d [level] // DFFS
-a [levle] // A*
-gi [level] // SeRPE with greedy algorithm
-di [level] // SeRPE with DFFS
-dir [level] // SeRPE with DFFS repeating one level
-air [level] // SeRPE with A*
```

License
----

GNU


**Free Software, Hell Yeah!**
[0]:https://youtu.be/u7XJ0g6d9po 
[Google Chrome]:http://daringfireball.net/
[Angry Birds Plugins]:http://twitter.com/thomasfuchs
[JRE]:http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
[JSON Simple]:https://code.google.com/p/json-simple/
[Jama]:http://math.nist.gov/javanumerics/jama/
[Jar in Jar Loader]:http://one-jar.sourceforge.net/version-0.95/
[Common Codec]:https://commons.apache.org/proper/commons-codec/
[WebSocket]:http://mvnrepository.com/artifact/org.java-websocket/Java-WebSocket/1.3.0
[Ruofei Du]: http://duruofei.com
[Zebao Gao]: https://www.cs.umd.edu/~gaozebao/
[Zheng Xu]: https://sites.google.com/site/xuzhustc/
