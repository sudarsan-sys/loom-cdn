# Loom CDN - Event-Driven Content Delivery Network

A high-performance, distributed CDN backend built with Spring Boot, Redis, and MinIO. Designed to handle file uploads, stream content, track metadata, and broadcast real-time events for scalable background processing.

---

## Key Features

### Event-Driven Architecture
Uses Redis Pub/Sub to decouple file uploads from post-processing tasks (like thumbnail generation or virus scanning).

### S3-Compatible Storage
Integrated with MinIO for scalable object storage (can be swapped with AWS S3 easily).

### Metadata Tracking
Automatically stores file metadata (size, type, upload time) in an H2 Database using JPA.

### Fully Dockerized
Entire stack (App + Redis + MinIO) spins up with a single docker-compose command.

### Load Tested
Verified to handle concurrent user uploads using custom PowerShell load scripts.

---

## Tech Stack

**Core Framework:** Java 21, Spring Boot 3.4.2  
**Storage:** MinIO (S3 Compatible Object Storage)  
**Database:** H2 In-Memory Database (JPA/Hibernate)  
**Messaging/Cache:** Redis  
**Build Tool:** Maven  
**Containerization:** Docker & Docker Compose  

---

## Getting Started

### Prerequisites

- Docker & Docker Compose  
- Java 21 JDK (Optional, if running locally without Docker)  
- Maven (Optional)  

---

## Option 1: Run with Docker (Recommended)

This will start the entire system (Backend, Database, Redis, MinIO) in isolated containers.

### Clone the Repository

```bash
git clone https://github.com/yourusername/loom-cdn-infra.git
cd loom-cdn-infra
