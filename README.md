 
![Logo](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-Hf_uKiJFQxIgHbHpaGUTByWMhldFc1zIeg&s)


# JavaEE POS System

## Introduction
This project is designed to maintain a proper layered architecture for a web application using Jakarta EE, MySQL, and AJAX. The goal is to ensure a clean separation of concerns, enhancing maintainability, scalability, and testability.

## Tech Stack

- **Backend Framework:** Jakarta EE

- **Database:** MySQL

- **Client-side Communication:** AJAX

- **Database Configuration:** JNDI

- **Logging:** slf4j,logback



## Architecture Overview

The application follows a layered architecture comprising the following layers:

**1.** **Presentation Layer:** Manages the user interface and client-side interactions.

**2.** **Business Logic Layer:** Contains the core functionality and business rules.

**3.** **Data Access Layer:** Handles direct interactions with the database.

## Setup and Configuration

### Prerequisites

- **JDK 21**
- **Jakarta EE compatible application server (e.g., Tomcat,WildFly, GlassFish)**
- **MySQL server**
- **Maven**

## Database Configuration

**1.Create a MySQL database.**

**2.Configure JNDI for database connectivity:**
- Define a JNDI resource in your application server's configuration.

- Example configuration for Tomcat:

<img src="https://github.com/sasmithx/JavaEE-POS/blob/main/Screenshot/xml.png" width="600px" height="auto">

## Logging

- **DEBUG:** Fine-grained information for debugging.
- **INFO:** General information about application progress.
- **WARN:** Potentially harmful situations.
- **ERROR:** Error events that might still allow the application to continue running.

 
## Architecture Overview

The application follows a layered architecture comprising the following layers:

**1.** **Presentation Layer:** Manages the user interface and client-side interactions.

**2.** **Business Logic Layer:** Contains the core functionality and business rules.

**3.** **Data Access Layer:** Handles direct interactions with the database.

## Setup and Configuration

### Prerequisites

- **JDK 21**
- **Jakarta EE compatible application server (e.g., Tomcat,WildFly, GlassFish)**
- **MySQL server**
- **Maven**

## Database Configuration

**1.Create a MySQL database.**

**2.Configure JNDI for database connectivity:**
- Define a JNDI resource in your application server's configuration.

- Example configuration for Tomcat:


## Logging

- **DEBUG:** Fine-grained information for debugging.
- **INFO:** General information about application progress.
- **WARN:** Potentially harmful situations.
- **ERROR:** Error events that might still allow the application to continue running.

 
