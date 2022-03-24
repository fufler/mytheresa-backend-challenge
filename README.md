# Intro

This is a solution for [Mytheresa backend challenge](./CHALLENGE.md).

## Interfaces and implementations

Provided implementation contains two different interfaces: `DiscountService` and `ProductsService`. 
This approach allows  one to easily replace implementation of specific service. There are two implementations of the services:
* `DBBackedProductsServiceImpl` is JPA-backend implementation of `ProductsService` that relies on usage of RDBMS for
  storing products;
* `DiscountServiceImpl` is very simple implementation of `DiscountService` that provides a way of
  compile-time configuring of SKU's and categories to apply specific discounts for.

## Storage model

At the storage level two SQL tables are used: one for products, one for categories. This approach enables
one to rename categories easily. Categories are not exposed as interface (like products)
because there is no need for this. For «real world» application it may be reasonable
to expose categories and implement many-to-many relationship between categories and products.

## Separation of concerns

Implementation uses *Controller-Service-Repository* architecture. Each layer puts minimal possible restrictions on API.
For example `ProductsRepository` has no limitation for response size. Instead it provides a parameter that enables caller
to specify paging parameters. `ProdutsService` puts another restriction: it's possible to request for «one page» only, 
but page size may be specified explicitly if necessary. `ProductsController` is the one in charge of enforcing result set
size to never exceed 5 as required by task description.

## Tests

There are two types of tests provided:

1. Unit tests (`DiscountServiceImplTest`) are supposed to validate services implementation. These tests do not use Spring   
  for dependency injection but are written in implementation-agnostic way.
1. Integration tests (`ProductsIT`) are supposed to ensure REST API satisfies all requirements (e.g. response format, 
  max response size etc.). Few of them in some sense duplicate unit tests.

# How to build, test and run

> ### Notice
> Please note that the following scenario is tested only on Linux environment. 
> It heavily relies on Maven and Docker so it should work for other platforms (MacOS, Windows with WSL),
> but some minor tweaks may be required.

To run both unit and integration tests use 

```shell
mvn clean verify
```

To build project use

```shell
mvn -P docker -D maven.test.skip=true package
```

To run demo project use supplied script `run_demo.sh` that does the following:
* builds Docker image for backend;
* generates input data with size of 100k products;
* runs backend and PostgreSQL containers using `docker-compose`.

Backend API will be accessible at `http://127.0.0.1:8080/products/` with Swagger UI at `http://127.0.0.1:8080/swagger/swagger-ui.html`.
