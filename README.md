# SearchSphere

SearchSphere is a search engine built using **Java**, **Spring Boot**, and **MongoDB**. The project is being developed to explore the core concepts behind modern search engines, including web crawling, indexing, and keyword-based search.

## Features

### Implemented

* Breadth First Search (BFS) web crawler
* URL queue management using `ArrayDeque`
* Duplicate URL detection using a `HashSet`
* Configurable crawl depth and page limits
* HTML parsing with Jsoup
* Extraction of:

  * Page title
  * Page content
  * Hyperlinks
* URL normalization

  * Removes fragments (`#`)
  * Removes trailing slashes
* Domain and subdomain filtering
* Storage of crawled pages in MongoDB
* REST endpoint to trigger crawling

### Planned

* Inverted index
* Keyword search API
* Search result ranking
* Docker containerization
* GitHub Actions CI pipeline
* Continuous Deployment (CD)

## Tech Stack

* Java
* Spring Boot
* MongoDB
* Spring Data MongoDB
* Jsoup
* Maven
* Git & GitHub

## Project Structure

```text
src
├── controller
├── crawler
├── model
├── repository
└── service
```

## How It Works

1. A seed URL is provided through the crawl endpoint.
2. The crawler performs a **Breadth First Search (BFS)** traversal.
3. Each page is downloaded using Jsoup.
4. The page title and text content are extracted.
5. The page is stored in MongoDB.
6. Hyperlinks are extracted and normalized.
7. New links from the same domain (including subdomains) are added to the queue.
8. Crawling continues until the configured depth or page limit is reached.

## Running the Project

### Prerequisites

* Java 21+
* Maven
* MongoDB

### Clone the Repository

```bash
git clone https://github.com/fatimafarhan2/SearchSphere.git
cd SearchSphere
```

### Start MongoDB

Ensure MongoDB is running locally on:

```
localhost:27017
```

### Run the Application

```bash
./mvnw spring-boot:run
```

or on Windows:

```bash
mvnw.cmd spring-boot:run
```

The application starts on:

```
http://localhost:8081
```

## API

### Start Crawling

```
GET /crawl
```

Example:

```
http://localhost:8081/crawl
```

The crawler downloads pages, stores them in MongoDB, and follows links according to the configured crawl depth and page limit.

## Current Status

🚧 This project is under active development.

Completed:

* Web crawler
* MongoDB integration
* BFS traversal
* Link extraction

In Progress:

* Search indexing
* Query processing
* Ranking algorithms

## Author

**Fatima Farhan**
