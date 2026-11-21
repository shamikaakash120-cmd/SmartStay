# SmartStay Hotel Booking System

**Coursework Project**: Programming III: Patterns and Algorithms  
**Student ID Prefix**: `E195569`  
**Platform**: Java 17 (Console Application, Pure Java, No External Frameworks or Databases)  

---

## 1. Project Overview

The **SmartStay Hotel Booking System** is an in-memory console application designed to manage hotel room inventories, customer registrations, reservations, and financial billing. The architecture is engineered according to strict Object-Oriented Programming (OOP) principles, clean coding standards, robust input validation, and algorithmic efficiency.

---

## 2. Package Architecture (19 Files Total)

All application source code resides under the `src/` directory with `E195569Main.java` in the default root package:

```text
SmartStay/
├── bin/                                         # Compiled Java bytecode (.class)
├── scratch/                                     # Verification test scripts (outside src)
│   ├── InvoiceCheck.java                        # Scratch test verifying Deluxe 5-night invoice
│   └── SystemTest.java                          # Automated coursework test suite
├── src/
│   ├── E195569Main.java                         # Application entry point (default package)
│   ├── model/                                   # Domain entities, value objects, and enums (10 files)
│   │   ├── E195569Person.java                   # Abstract base class (name, contact, email)
│   │   ├── E195569Guest.java                    # Extends Person (+ nicOrPassport)
│   │   ├── E195569Admin.java                    # Extends Person (+ adminId, pin, login())
│   │   ├── E195569Room.java                     # Room entity with formatted row output
│   │   ├── E195569Booking.java                  # Reservation entity with overlap detection
│   │   ├── E195569Invoice.java                  # Formatted invoice with aligned colons
│   │   ├── E195569RoomType.java                 # Enum: STANDARD, DELUXE, SUITE, FAMILY_SUITE
│   │   ├── E195569BedType.java                  # Enum: SINGLE, DOUBLE, QUEEN, KING
│   │   ├── E195569RoomStatus.java               # Enum: AVAILABLE, RESERVED, UNDER_MAINTENANCE
│   │   └── E195569BookingStatus.java            # Enum: CONFIRMED, CANCELLED
│   ├── service/                                 # Business logic and in-memory repositories (4 files)
│   │   ├── E195569RoomService.java              # HashMap room storage, update, sort, search
│   │   ├── E195569GuestService.java             # HashMap guest directory with regex validation
│   │   ├── E195569BookingService.java           # ArrayList booking store, first-fit allocation, search
│   │   └── E195569InvoiceCalculator.java        # Exact invoice calculation engine
│   ├── exception/                               # Custom checked domain exception (1 file)
│   │   └── E195569BookingException.java         # Base checked exception for all business rules
│   ├── util/                                    # Reusable console readers and seeders (2 files)
│   │   ├── E195569ConsoleReader.java            # Safe console input readers (scanner helpers)
│   │   └── E195569DataSeeder.java               # Seeds demo rooms, guests, and admin
│   └── ui/                                      # User interface presentation layer (1 file)
│       └── E195569ConsoleMenu.java              # Interactive console interface (Admin & Guest workflows)
└── README.md                                    # Coursework technical documentation
```

---

## 3. Business Rules Implementation

### Room Specifications Matrix
| Room Type | Daily Rate (LKR) | Base Capacity | Extra Guest / Night (LKR) | Tax Rate | Breakfast Included |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **STANDARD** | 6,000.00 | 2 | 1,000.00 | 10% | No |
| **DELUXE** | 9,000.00 | 3 | 1,500.00 | 12% | Yes |
| **SUITE** | 14,000.00 | 4 | 2,000.00 | 15% | Yes |
| **FAMILY_SUITE** | 18,000.00 | 6 | 2,500.00 | 15% | Yes |

### Bed Types & Operational Statuses
* **Bed Types**: `SINGLE`, `DOUBLE`, `QUEEN`, `KING`.
* **Room Statuses**: `AVAILABLE`, `RESERVED`, `UNDER_MAINTENANCE`.
* **Booking Statuses**: `CONFIRMED`, `CANCELLED`.

### Reservation & Policy Constraints
1. **Lead Time Constraint**: Check-in must be at least **2 days** after today.
2. **Cancellation Cutoff**: Cancellations or modifications within **24 hours** prior to check-in (at 00:00 midnight) are strictly blocked.
3. **Advance Deposit**: A mandatory refundable advance deposit of **LKR 10,000.00** is credited at reservation.
4. **Long-Stay Discount**: Stays of **5 or more nights** receive a **12% discount** on the base room charge (before tax).

---

## 4. Mathematical Billing & Invoice Formula

Implemented in `E195569InvoiceCalculator`:

$$\text{base} = \text{dailyRate} \times \text{nights}$$

$$\text{extraGuests} = \max(0, \text{numberOfGuests} - \text{capacity})$$

$$\text{extra} = \text{extraGuests} \times \text{extraGuestChargePerNight} \times \text{nights}$$

$$\text{discount} = (\text{nights} \ge 5) \ ? \ (0.12 \times \text{base}) : 0$$

$$\text{taxable} = \text{base} - \text{discount} + \text{extra}$$

$$\text{tax} = \text{taxable} \times \text{taxRate}$$

$$\text{final} = \text{taxable} + \text{tax} - \text{ADVANCE}$$

### Worked Example: Deluxe Room, 5 Nights, 4 Guests
* **Room**: Deluxe (`dailyRate = 9,000.00`, `capacity = 3`, `extraRate = 1,500.00`, `taxRate = 12%`)
* **Nights**: $5$
* **Guests**: $4$
* **Calculations**:
  * $\text{base} = 9,000 \times 5 = \text{LKR } 45,000.00$
  * $\text{extra} = (4 - 3) \times 1,500 \times 5 = \text{LKR } 7,500.00$
  * $\text{discount} = 0.12 \times 45,000 = \text{LKR } 5,400.00$
  * $\text{taxable} = 45,000 - 5,400 + 7,500 = \text{LKR } 47,100.00$
  * $\text{tax} = 47,100 \times 0.12 = \text{LKR } 5,652.00$
  * $\text{advance} = \text{LKR } 10,000.00$
  * **Final Payable**: $47,100 + 5,652 - 10,000 = \mathbf{\text{LKR } 42,752.00}$

---

## 5. OOP Concepts & Design Patterns

### 1. Encapsulation & Validation
* All attributes in classes are `private` and accessed via appropriate getters.
* Entity constructors reject `null`, empty, or invalid values by throwing `IllegalArgumentException` or `E195569BookingException` with descriptive messages.

### 2. Inheritance
* `E195569Person` serves as an abstract base class holding common contact and identity fields (`fullName`, `contactNumber`, `email`).
* `E195569Guest` and `E195569Admin` inherit from `E195569Person`, reusing personal contact handling.

### 3. Polymorphism (Dynamic Dispatch)
* `E195569Person` defines the abstract method `public abstract String getDetails()`.
* Both `E195569Guest` and `E195569Admin` override `getDetails()` with role-specific formatted outputs.

---

## 6. Data Structures & Algorithms

| Operation | Data Structure / Algorithm | Complexity | Purpose |
| :--- | :--- | :---: | :--- |
| **Room Lookup** | `HashMap<String, E195569Room>` | $O(1)$ average | Instant room retrieval by unique `roomId`. |
| **Guest Retrieval** | `HashMap<String, E195569Guest>` | $O(1)$ average | Instant customer lookup by `nicOrPassport`. |
| **Booking Sequence** | `ArrayList<E195569Booking>` | $O(1)$ amortized append | Sequential in-memory storage of reservations. |
| **Catalog Ordering** | TimSort via `Comparator.comparing` | $O(n \log n)$ | Sorts room catalog by `roomId` for tabular display. |
| **Chronological Sort** | TimSort via `Comparator.comparing` | $O(n \log n)$ | Sorts bookings by check-in date (`viewByCheckInDate`). |
| **Interval Overlap** | Date Range Overlap Check | $O(1)$ per booking | Detects conflicts: $in < otherOut \land out > otherIn$. |
| **First-Fit Allocation** | Linear Search with Predicate Composition | $O(n)$ | Finds first available room of requested type without date overlap. |
| **Booking Search (ID)** | Linear Search | $O(n)$ | Searches booking by exact ID. |
| **Guest Name Search** | Linear Search (Partial Match) | $O(n)$ | Searches bookings matching guest name case-insensitively. |

---

## 7. Compilation & Execution Guide

### Requirements
* Java Development Kit (JDK) 17 or higher.

### 1. Compile the Application
```powershell
javac -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)
```

### 2. Launch the Application
```powershell
# Option A: Run directly from compiled bytecode
java -cp bin E195569Main

# Option B: Run via packaged executable JAR
java -jar SmartStay.jar
```

### 3. Run Automated Coursework Verification Suite
```powershell
javac -cp bin -d scratch scratch/SystemTest.java
java -cp "scratch;bin" SystemTest
```

### 4. Run Invoice Verification Script
```powershell
javac -cp bin -d scratch scratch/InvoiceCheck.java
java -cp "scratch;bin" InvoiceCheck
```
