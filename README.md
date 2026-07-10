![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.5.16-brightgreen)
![Build](https://img.shields.io/badge/Build-Maven-orange)
![Database](https://img.shields.io/badge/Database-PostgreSQL-blue)
# Customer Rewards System

A Spring Boot RESTful application that calculates customer reward points based on purchase transactions. This project demonstrates clean architecture, REST API development, Java 21 features, Spring Boot best practices, PostgreSQL integration, exception handling, validation, and unit testing using JUnit 5 and Mockito.

---

# Problem Statement

A retailer offers a rewards program to its customers.

Reward Rules:

- 2 points for every dollar spent over **$100**
- 1 point for every dollar spent between **$50 and $100**

Example:

Purchase Amount = **$120**

Reward Points

- $50-$100 = 50 points
- Above $100 = 20 × 2 = 40 points

Total Reward = **90 Points**

---

# Features

- Create Customer
- Get Customer Details
- Add Transaction
- Get Customer Transactions
- Calculate Rewards
- Dynamic Date Range
- Monthly Reward Summary
- Total Reward Calculation
- Global Exception Handling
- Input Validation
- Unit Testing

---

# Technology Stack

| Technology      | Version                |
|-----------------|------------------------|
| Java            | 21                     |
| Spring Boot     | 3.5.16                 |
| Spring Data JPA | Latest                 |
| PostgreSQL      | Neon Database          |
| Maven           | Build Tool             |
| ModelMapper     | DTO Mapping            |
| Lombok          | Boilerplate Reduction  |
| JUnit 5         | Unit Testing           |
| Mockito         | Mocking Framework      |

---

# Project Architecture

Controller

↓

Service

↓

Repository

↓

PostgreSQL Database

---

# Project Structure

src/main/java

controller

service

repository

entity

dto

exception

util

config

---

# Database Design

Customer

- customer_id
- customer_name
- email
- created_at

Transaction

- transaction_id
- amount
- transaction_date
- customer_id

Relationship

Customer (1)

↓

Transaction (Many)

---

# Reward Calculation Logic

Amount ≤ 50

Reward = 0

50 < Amount ≤ 100

Reward = Amount - 50

Amount > 100

Reward = 50 + ((Amount - 100) × 2)

---

# REST APIs

## Create Customer

POST

/api/v1/customers

---

## Get Customer

GET

/api/v1/customers/{customerId}

---

## Add Transaction

POST

/api/v1/customers/{customerId}/transactions

---

## Get Customer Transactions

GET

/api/v1/customers/{customerId}/transactions

---

## Calculate Rewards

GET

/api/v1/customers/{customerId}/rewards

Example

/api/v1/customers/1/rewards?fromDate=2026-01-01&toDate=2026-03-31

---

# Sample Reward Response

```json
{
  "customerId": 1,
  "customerName": "John Doe",
  "fromDate": "2026-01-01",
  "toDate": "2026-03-31",
  "transactions": [
    {
      "transactionId": 1,
      "amount": 120,
      "transactionDate": "2026-01-10",
      "rewardPoints": 90
    }
  ],
  "monthlyRewards": [
    {
      "month": "January",
      "rewardPoints": 90
    }
  ],
  "totalRewardPoints": 90
}
```

---

# Validation

Customer

- Name is mandatory
- Email is mandatory
- Email format validation

Transaction

- Amount is mandatory
- Amount must be positive
- Transaction date cannot be in the future

Reward

- From Date cannot be after To Date

---

# Exception Handling

- Customer Not Found
- Customer Already Exists
- Invalid Date Range
- Validation Exception
- Internal Server Error

---

# Unit Testing

JUnit 5

Mockito

MockMvc

Test Coverage includes

- Reward Calculator
- Customer Service
- Transaction Service
- Reward Service
- Customer Controller
- Transaction Controller
- Reward Controller

---

# API Screenshots

API execution screenshots are available in:

docs/screenshots

---

# How to Run

Clone Repository

Configure PostgreSQL Database

Update application.yml

Run

mvn spring-boot:run

Run Tests

mvn test

---

# Future Enhancements

- Swagger/OpenAPI Documentation
- Docker Support
- Spring Security with JWT
- Pagination
- Audit Logging
- Caching using Redis

---

# Author

Deepika Paryani
