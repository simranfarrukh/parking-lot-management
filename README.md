# 🚗 Parking Lot Management System

## 📌 Overview
This is a Spring Boot-based Parking Lot Management System that allows:
- Allocating parking spots to vehicles
- Managing a waiting queue when parking is full
- Deallocating spots in a **round-robin** manner
- Resetting the parking database

## 🛠 Technologies Used
- Java (Spring Boot)
- Spring Data JPA (Database operations)
- H2 or MySQL (Database)
- Lombok (Reduces boilerplate code)
- REST API (Endpoints for frontend communication)

## 📂 Project Structure
```json
parking_lot/
├── controller/      # API endpoints
├── model/           # Entities: Vehicle, ParkingSpot
├── repository/      # Database interactions
├── service/         # Business logic handling
├── resources/       # Application configuration
```

## 📖 API Endpoints

### 1️⃣ Allocate Parking
**URL:** `POST /parking/allocate`

**Request Body:**
```json
{
  "licensePlate": "ABC123"
}
```
**Response:** Allocates a spot or adds to the waiting queue.

### 2️⃣ Get Available Parking Spots
**URL:** `GET /parking/available-spots`
**Response:** List of empty parking spots.

### 3️⃣ Get Vehicles in Waiting Queue
**URL:** `GET /parking/waiting-queue`
**Response:** List of waiting vehicles.

### 4️⃣ Deallocate and Assign Next Vehicle
**URL:** `DELETE /parking/deallocate`
**Response:** Frees a spot and assigns the next waiting vehicle using a round-robin approach.

### 5️⃣ Reset Database
**URL:** `DELETE /parking/reset`
**Response:** Clears all spots and vehicles, resetting the system.

## 🔥 Round-Robin Deallocation Logic
When spots are full and deallocation occurs:
- The system alternates freeing parking spots.
- If the queue is empty, spots are freed up one-by-one.

## 🚀 How to Run Locally
1. Clone the repo:
   `git clone https://github.com/simranfarrukh.git`
   `cd parking_lot`

2. Build and run the project:
   `mvn spring-boot:run`

3. Access endpoints via Postman or a frontend app.

## 🤝 Contributing
Feel free to create issues or submit pull requests! 🚀
