 
![Logo](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-Hf_uKiJFQxIgHbHpaGUTByWMhldFc1zIeg&s)


# JavaEE POS System

## Introduction
This project is designed to maintain a proper layered architecture for a web application using Jakarta EE, MySQL, and AJAX. The goal is to ensure a clean separation of concerns, enhancing maintainability, scalability, and testability.



## <img src="https://user-images.githubusercontent.com/74038190/216122041-518ac897-8d92-4c6b-9b3f-ca01dcaf38ee.png" style="width: 50px; height: 50px;" alt=""> Tech Stack 

- **Backend Framework:** Jakarta EE

- **Database:** MySQL

- **Client-side Communication:** AJAX

- **Database Configuration:** JNDI

- **Logging:** slf4j,logback

## <img src="https://user-images.githubusercontent.com/74038190/216122028-c05b52fb-983e-4ee8-8811-6f30cd9ea5d5.png" style="width: 50px; height: 50px;" alt="">Architecture Overview

The application follows a layered architecture comprising the following layers:

**1.** **Presentation Layer:** Manages the user interface and client-side interactions.

**2.** **Business Logic Layer:** Contains the core functionality and business rules.

**3.** **Data Access Layer:** Handles direct interactions with the database.


## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Keyboard.png" style="width: 50px; height: 50px;" alt="">Setup and Configuration

### Prerequisites

- **JDK 21**
- **Jakarta EE compatible application server (e.g., Tomcat,WildFly, GlassFish)**
- **MySQL server**
- **Maven**

### Clone the repository:

```bash
  git clone https://github.com/sasmithx/JavaEE-POS.git
```  

## <img src="https://user-images.githubusercontent.com/74038190/216122003-1c7d9078-357a-47f5-81c7-1c4f2552e143.png" style="width: 50px; height: 50px;" alt=""> Database Configuration

**1.Create a MySQL database.**

**2.Configure JNDI for database connectivity:**
- Define a JNDI resource in your application server's configuration.

- Example configuration for Tomcat:

<img src="https://github.com/sasmithx/JavaEE-POS/blob/main/Screenshot/DBConfig.png" width="600px" height="auto">

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/High%20Voltage.png" style="width: 50px; height: 50px;" alt="">Logging

- **DEBUG:** Fine-grained information for debugging.
- **INFO:** General information about application progress.
- **WARN:** Potentially harmful situations.
- **ERROR:** Error events that might still allow the application to continue running.

 ## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Robot.png" style="width: 50px; height: 50px;" alt="">API Documentation

To view this project API Documentation

```bash
  https://documenter.getpostman.com/view/35385442/2sA3s1nX8W
```
Refer to the [Postman API Documentation](https://documenter.getpostman.com/view/35385442/2sA3s1nX8W) for detailed API endpoints and usage instructions.

## <img src="https://user-images.githubusercontent.com/74038190/216122069-5b8169d7-1d8e-4a13-b245-a8e4176c99f8.png" style="width: 50px; height: 50px;" alt="">License
This project is licensed under the MIT License - see the [MIT License](LICENSE) file for details. 

<br>

<p align="center">
  &copy; 2024 Sasmith Manawadu
</p>
