### \# Smart Travel Platform

## Name: Kethaka Wanasinghe
## Index No: ICT/21/939

-----

### 1\. Project Overview

The Smart Travel Platform is a distributed microservices system designed to simulate an end-to-end travel booking flow. The architecture is built using **Spring Boot microservices** and employs a centralized **Orchestration Pattern** to manage the transaction lifecycle, from user validation to payment and final notification.

**Architectural Compliance:**

  * **NO deprecated technologies** (exclusively uses WebClient and Feign Client).
  * **Inter-service communication** via **WebClient** and **Feign Client**.
  * **In-memory H2 databases** for isolated data persistence.
  * **Individual services** running on separate ports.

-----

### 2\. Microservices Architecture

The system is composed of six independent services, running on separate ports:

| Service | Purpose | Port | Data Persistence |
| :--- | :--- | :--- | :--- |
| **Booking Service** | **Orchestrator** - Manages transaction state and flow. | 8084 | H2 |
| **User Service** | Validates user existence and retrieves information. | 8081 | None |
| **Flight Service** | Handles flight availability and pricing. | 8082 | H2 |
| **Hotel Service** | Handles hotel availability and pricing. | 8083 | H2 |
| **Payment Service** | Simulates payment processing and provides transaction feedback. | 8085 | None |
| **Notification Service** | Sends final confirmation messages (simulated). | 8086 | None |

-----

### 3\. The Booking Orchestration Flow

The core workflow is initiated by a user request to the Booking Service, which coordinates the transaction across all components.

#### Booking Flow Steps

1.  User sends a booking request to **Booking Service**.
2.  Booking Service:
      * Validates user **(WebClient → User Service)**.
      * Checks flight availability **(Feign Client → Flight Service)**.
      * Checks hotel availability **(Feign Client → Hotel Service)**.
      * Calculates total cost and saves booking as `PENDING`.
      * Calls Payment Service **(WebClient)** to initiate payment.
3.  Payment Service:
      * Processes payment.
      * Calls **Booking Service** (`PUT /status` endpoint) to update status to `CONFIRMED` (Status Callback).
      * Calls Notification Service **(WebClient)** to dispatch confirmation.
4.  Notification Service logs/prints the final confirmation message.

#### Communication Rules Summary

| Requirement | Implemented |
| :--- | :---: |
| WebClient used for status updates | done |
| Feign Client used for inventory checks | done |
| Notification triggered | done |

-----

### 4\. Technical Challenges and Resolutions

This project required specialized solutions to overcome environmental and compiler conflicts:

| Challenge | Cause | Resolution |
| :--- | :--- | :--- |
| **Lombok Compiler Failure** | IDE/JDK incompatibility prevented required code generation. | Replaced all DTOs and Entities with **complete manual Java code** (full constructors, getters, and setters). |
| **Persistent 400 Bad Request** | JSON deserialization failed on `Long` and `LocalDate` fields due to dependency conflicts. | Applied explicit JSON mapping using **`@JsonProperty`** and **`@JsonFormat`** annotations to enforce correct data binding. |

-----

### 5\. Main Service Endpoints

#### Booking Service Endpoints (Port 8084)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/bookings` | **Trigger:** Creates a new booking and starts the full flow. |
| `PUT` | `/api/v1/bookings/{id}/status` | **Callback:** Endpoint for Payment Service to update booking status. |

**Example Booking Request Payload:**

```json
{
  "userId": 1,
  "flightId": 200,
  "hotelId": 55,
  "travelDate": "2025-10-10"
}
```

#### Supporting Endpoints

| Service | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| Payment (8085) | `POST` | `/api/v1/payments` | Process payment request. |
| Notification (8086) | `POST` | `/api/v1/notifications` | Trigger notification message. |

-----

### 6\. Conclusion

This project successfully demonstrates a highly functional microservices architecture. The strategic use of both **Feign Client** and **WebClient** ensures efficient and robust communication, meeting all assignment rules while providing full traceability of the booking transaction.
