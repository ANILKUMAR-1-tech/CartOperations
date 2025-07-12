# 🛒 CartOperations & Order Management System

A full-stack e-commerce backend built with **Spring Boot 3**, **Spring Security (JWT)**, and **PostgreSQL**. This system allows users to register, manage carts, and place orders with secure authentication and role-based access control.

---

## 🚀 Features

- 🔐 **JWT-based Authentication**  
- 👤 **User Registration/Login**  
- 🛒 **Cart Management**
  - Add/Remove/View Items
- 📦 **Order Placement**
  - Place Orders from Cart
  - View Past Orders
- 📋 **Product Management**
- 🧑‍⚖️ **Role-based Access (User/Admin)**
- 📄 **RESTful APIs**

---

## 🧰 Tech Stack

| Layer          | Technology                 |
|----------------|-----------------------------|
| Backend        | Spring Boot 3, Spring MVC   |
| Security       | Spring Security 6, JWT      |
| Database       | PostgreSQL                  |
| ORM            | Hibernate (Spring Data JPA) |
| Build Tool     | Maven                       |
| IDE            | IntelliJ / STS              |
| Version Control| Git & GitHub                |

---

## 📂 Project Structure

CartOperations
├── controller
│ ├── AuthController
│ ├── CartController
│ ├── OrderController
│ └── ProductController
├── dto
├── enums
├── exception
├── model
│ ├── User
│ ├── Roles
│ ├── Product
│ ├── Cart
│ ├── CartItem
│ ├── Order
│ └── OrderItem
├── repository
├── request
├── response
├── security
│ ├── config
│ ├── jwt
│ └── user
├── service
└── Application.java


---

## 🔐 Authentication Endpoints

| Method | Endpoint               | Description          |
|--------|------------------------|----------------------|
| POST   | `/api/auth/register`   | Register new user    |
| POST   | `/api/auth/login`      | Login and get token  |

**JWT Token Usage:**

---

## 🛒 Cart API Endpoints

| Method | Endpoint                      | Description            |
|--------|-------------------------------|------------------------|
| GET    | `/api/cart/`                  | View current cart      |
| POST   | `/api/cart/add/{productId}`   | Add item to cart       |
| DELETE | `/api/cart/remove/{productId}`| Remove item from cart  |
| POST   | `/api/cart/clear`             | Clear the cart         |

---

## 📦 Order API Endpoints

| Method | Endpoint            | Description            |
|--------|---------------------|------------------------|
| POST   | `/api/order/place`  | Place order from cart  |
| GET    | `/api/order/user`   | View user orders       |
| GET    | `/api/order/all`    | (Admin) View all orders|

---

## 📦 Product API (Admin Only)

| Method | Endpoint              | Description         |
|--------|-----------------------|---------------------|
| POST   | `/api/product/add`    | Add new product     |
| PUT    | `/api/product/update` | Update product      |
| DELETE | `/api/product/delete/{id}` | Delete product |

---

## ⚙️ Configuration

### 📄 `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cart_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
