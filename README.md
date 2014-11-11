beagle
======

your own private serach engine

## Requirements

1.Tomcat

2.maven

## Installation

1. Clone the repository: `git clone https://github.com/valeraradu/beagle.git`
2. Build the application: `mvn package -DskipTests` (test fails either), if you planning to use port 80 no changes in project are needed otherwise change line `edu/uci/ics/crawler4j/examples/basic/BasicCrawler.java:69` to use correct port
3. Deploy war file from /target
4. View in browser at `http://localhost:8080/beagle-0.3.0-SNAPSHOT`
5. open `http://localhost:8080/beagle-0.3.0-SNAPSHOT/crawl.jsp` to start crawling 
6. open `http://localhost:8080/beagle-0.3.0-SNAPSHOT/index.jsp` to search indexed pages
 

please send your feedback at valeraradu@hotmail.com
