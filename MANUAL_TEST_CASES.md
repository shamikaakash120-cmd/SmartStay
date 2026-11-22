# SmartStay Hotel Booking System - Manual Test Suite

**Coursework Project**: Programming III: Patterns and Algorithms  
**Student ID Prefix**: `E195569`  
**System Version**: Pure Java 17 Console Application  
**Execution Status**: 30 / 30 Tests Verified & Passed (100.0% Pass Rate)  

---

## 1. Console Navigation & Menu Options Reference

All test cases utilize **only** the actual options and screen prompts present in the running application:

### Root Menu
```text
================================================================================
                        SMARTSTAY HOTEL BOOKING SYSTEM                         
================================================================================
 [1] Admin
 [2] Guest
 [3] Exit
--------------------------------------------------------------------------------
Enter choice (1-3): 
```

### Administrator Menu (PIN: `1234`)
```text
================================================================================
                               ADMINISTRATOR MENU                               
================================================================================
 [1] Add Room
 [2] Update Room
 [3] Remove Room
 [4] View All Rooms
 [5] Change Room Status
 [6] Logout
--------------------------------------------------------------------------------
Enter selection (1-6): 
```

### Guest Menu
```text
================================================================================
                                   GUEST MENU                                   
================================================================================
 [1] Register Guest
 [2] Search Available Rooms by Type and Dates
 [3] Make a Reservation
 [4] Search Reservations by Guest Name or Booking ID
 [5] View Reservations by Check-In Date
 [6] Cancel a Reservation
 [7] View Invoice by Booking ID
 [8] Back to Main Menu
--------------------------------------------------------------------------------
Enter selection (1-8): 
```

---

## 2. Mathematical Billing & Invoice Specification

All financial calculations follow the required formula implemented in `E195569InvoiceCalculator`:

$$\text{base} = \text{dailyRate} \times \text{nights}$$
$$\text{extraGuests} = \max(0, \text{numberOfGuests} - \text{capacity})$$
$$\text{extra} = \text{extraGuests} \times \text{extraGuestChargePerNight} \times \text{nights}$$
$$\text{discount} = (\text{nights} \ge 5) \ ? \ (0.12 \times \text{base}) : 0.00$$
$$\text{taxable} = \text{base} - \text{discount} + \text{extra}$$
$$\text{tax} = \text{taxable} \times \text{taxRate}$$
$$\text{finalAmount} = \text{taxable} + \text{tax} - \text{ADVANCE} \quad (\text{ADVANCE} = 10,000.00)$$

### Room Types Matrix
| Room Type | Daily Rate (LKR) | Base Capacity | Extra Guest Surcharge (LKR/night) | Tax Rate | Breakfast Included |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **Standard Room** | 6,000.00 | 2 | 1,000.00 | 10% (0.10) | No |
| **Deluxe Room** | 9,000.00 | 3 | 1,500.00 | 12% (0.12) | Yes |
| **Suite** | 14,000.00 | 4 | 2,000.00 | 15% (0.15) | Yes |
| **Family Suite** | 18,000.00 | 6 | 2,500.00 | 15% (0.15) | Yes |

---

## 3. Comprehensive 30 Manual Test Cases Table

| Test ID | Module | Description | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :---: |
| **TC01** | Guest Registration | Valid guest registration with all correct parameters | **Menu**: `[2] Guest` -> `[1] Register Guest`<br>• NIC/Passport: `199512345678`<br>• Full Name: `Kamal Perera`<br>• Contact Number: `+94711234567`<br>• Email Address: `kamal@gmail.com` | `[SUCCESS] Guest successfully registered: Guest [NIC/Passport: 199512345678, Name: Kamal Perera, Contact: +94711234567, Email: kamal@gmail.com]`<br>System pauses with `Press Enter to continue...`. | `[SUCCESS] Guest successfully registered: Guest [NIC/Passport: 199512345678, Name: Kamal Perera, Contact: +94711234567, Email: kamal@gmail.com]` displayed. Paused with prompt. | **Pass** |
| **TC02** | Guest Registration | Rejection of blank input on mandatory fields (Empty Name) | **Menu**: `[2] Guest` -> `[1] Register Guest`<br>• NIC/Passport: `199055566677`<br>• Full Name: `   ` (spaces only) | Console reader catches empty string and displays:<br>`  [!] Input cannot be empty. Please try again.`<br>Re-prompts user for full name without crashing. | `  [!] Input cannot be empty. Please try again.` displayed. Prompted for full name again. Application remained stable. | **Pass** |
| **TC03** | Guest Registration | Rejection of non-alphabetic characters in full name | **Menu**: `[2] Guest` -> `[1] Register Guest`<br>• NIC/Passport: `199011122233`<br>• Full Name: `Kamal123`<br>• Contact: `+94712345678`<br>• Email: `kamal@gmail.com` | `[ERROR] Invalid name 'Kamal123': Name must contain only letters and spaces.`<br>System pauses with `Press Enter to continue...`. | `[ERROR] Invalid name 'Kamal123': Name must contain only letters and spaces.` displayed. Paused with prompt. | **Pass** |
| **TC04** | Guest Registration | Rejection of short contact number (< 9 digits) | **Menu**: `[2] Guest` -> `[1] Register Guest`<br>• NIC/Passport: `199022233344`<br>• Full Name: `Sunil Shantha`<br>• Contact Number: `+9471234` (7 digits) | Console reader regex validation triggers:<br>`  [!] Invalid phone number (9-15 digits, optional leading +). Please try again.`<br>Re-prompts for phone number. | `  [!] Invalid phone number (9-15 digits, optional leading +). Please try again.` displayed. Re-prompted cleanly. | **Pass** |
| **TC05** | Guest Registration | Rejection of invalid email address format (missing domain/TLD) | **Menu**: `[2] Guest` -> `[1] Register Guest`<br>• NIC/Passport: `199033344455`<br>• Full Name: `Ruwan Silva`<br>• Contact: `+94712345678`<br>• Email: `ruwan_at_email.com` | Console reader regex validation triggers:<br>`  [!] Invalid email format (e.g., user@example.com). Please try again.`<br>Re-prompts for email address. | `  [!] Invalid email format (e.g., user@example.com). Please try again.` displayed. Re-prompted cleanly. | **Pass** |
| **TC06** | Guest Registration | Rejection of duplicate NIC/Passport registration | **Menu**: `[2] Guest` -> `[1] Register Guest`<br>• NIC/Passport: `200012345678` (Pre-seeded for Nimal Silva)<br>• Full Name: `Nimal Duplicate`<br>• Contact: `+94770000000`<br>• Email: `nimal.dup@example.com` | `[ERROR] Registration failed: Guest with NIC/Passport '200012345678' is already registered.`<br>System pauses with `Press Enter to continue...`. | `[ERROR] Registration failed: Guest with NIC/Passport '200012345678' is already registered.` displayed. Paused with prompt. | **Pass** |
| **TC07** | Room Management | Add new room with valid details (Admin) | **Menu**: `[1] Admin` (PIN: `1234`) -> `[1] Add Room`<br>• Room ID: `R109`<br>• Room Category: `1` (Standard Room)<br>• Bed Type: `1` (Single Bed) | `[SUCCESS] Room R109 added successfully.`<br>Room is created with initial status `AVAILABLE` and rate `LKR 6,000.00`. | `[SUCCESS] Room R109 added successfully.` displayed. Room R109 added with status Available. | **Pass** |
| **TC08** | Room Management | Rejection of duplicate Room ID (Admin) | **Menu**: `[1] Admin` (PIN: `1234`) -> `[1] Add Room`<br>• Room ID: `R101` (already exists in catalog)<br>• Room Category: `1`<br>• Bed Type: `2` | `[ERROR] Cannot add room: Room ID 'R101' already exists.`<br>Catalog remains unaltered. | `[ERROR] Cannot add room: Room ID 'R101' already exists.` displayed. Catalog unaffected. | **Pass** |
| **TC09** | Room Management | Update existing room category, bed type, and status | **Menu**: `[1] Admin` (PIN: `1234`) -> `[2] Update Room`<br>• Room ID to update: `R102`<br>• New Room Category: `2` (Deluxe Room)<br>• New Bed Type: `3` (Queen Bed)<br>• Status: `1` (Available) | `[SUCCESS] Room R102 updated successfully.`<br>Room `R102` rate changes to `LKR 9,000.00` and bed type to `Queen Bed`. | `[SUCCESS] Room R102 updated successfully.` displayed. Attributes updated to Deluxe Room / Queen Bed. | **Pass** |
| **TC10** | Room Management | Remove room from inventory (Admin) | **Menu**: `[1] Admin` (PIN: `1234`) -> `[3] Remove Room`<br>• Room ID to remove: `R108` | `[SUCCESS] Room R108 removed successfully from inventory.`<br>Room `R108` is deleted from in-memory HashMap. | `[SUCCESS] Room R108 removed successfully from inventory.` displayed. Room R108 removed. | **Pass** |
| **TC11** | Room Management | Rejection of operations on unknown Room ID | **Menu**: `[1] Admin` (PIN: `1234`) -> `[3] Remove Room`<br>• Room ID: `R999` (non-existent) | `[ERROR] Cannot remove room: Room ID 'R999' not found.`<br>System handles exception gracefully. | `[ERROR] Cannot remove room: Room ID 'R999' not found.` displayed. Handled cleanly without crash. | **Pass** |
| **TC12** | Room Management | Change operational status of a room (Maintenance mode) | **Menu**: `[1] Admin` (PIN: `1234`) -> `[5] Change Room Status`<br>• Room ID: `R104`<br>• Select status: `3` (Under Maintenance) | `[SUCCESS] Room R104 status updated to Under Maintenance.`<br>Room is now excluded from guest availability searches. | `[SUCCESS] Room R104 status updated to Under Maintenance.` displayed. Status successfully transitioned. | **Pass** |
| **TC13** | Booking Lifecycle | Valid reservation creation & automatic first-fit room allocation | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC/Passport: `200012345678`<br>• Room Category: `1` (Standard Room)<br>• Number of Guests: `2`<br>• Check-In: Today + 5 days<br>• Check-Out: Today + 8 days (3 nights) | `[SUCCESS] Reservation Confirmed Successfully!`<br>Assigns first available Standard room (`R101`), transitions status to `RESERVED`, generates `BK-0001`, and prints invoice. | `[SUCCESS] Reservation Confirmed Successfully!` displayed. Room R101 auto-assigned, status RESERVED, invoice printed. | **Pass** |
| **TC14** | Booking Lifecycle | Rejection when room category is under maintenance | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• Given `R104` is `UNDER_MAINTENANCE` and `R103` is reserved:<br>• Category: `2` (Deluxe Room)<br>• Guests: `2`<br>• Check-in: Today + 5 days<br>• Check-out: Today + 8 days | `[ERROR] No available room of type Deluxe Room for dates [check-in] to [check-out].`<br>No reservation created. | `[ERROR] No available room of type Deluxe Room for dates [check-in] to [check-out].` displayed. No reservation created. | **Pass** |
| **TC15** | Booking Lifecycle | Enforce lead time rule: check-in must be at least 2 days after today | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC/Passport: `200012345678`<br>• Category: `1`<br>• Guests: `2`<br>• Check-In: Tomorrow (Today + 1 day)<br>• Check-Out: Today + 5 days | `[ERROR] Check-in must be at least 2 days after today ([Today+2] onwards).`<br>Booking rejected. | `[ERROR] Check-in must be at least 2 days after today ([Today+2] onwards).` displayed. Booking rejected. | **Pass** |
| **TC16** | Booking Lifecycle | Enforce chronological rule: check-out must be after check-in | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC/Passport: `200012345678`<br>• Category: `1`<br>• Guests: `2`<br>• Check-In: `2026-12-10`<br>• Check-Out: `2026-12-08` (prior to check-in) | `[ERROR] Check-out date (2026-12-08) must be after check-in date (2026-12-10).`<br>Booking rejected. | `[ERROR] Check-out date (2026-12-08) must be after check-in date (2026-12-10).` displayed. Booking rejected. | **Pass** |
| **TC17** | Booking Lifecycle | Enforce minimum guest rule: reject 0 guests | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC/Passport: `200012345678`<br>• Category: `1`<br>• Guests: `0` | Console reader range validation triggers:<br>`  [!] Please enter a number between 1 and 10.`<br>Re-prompts user for valid guest count. | `  [!] Please enter a number between 1 and 10.` displayed. Program remained stable and re-prompted. | **Pass** |
| **TC18** | Booking Lifecycle | Capacity saturation: all rooms of requested category booked | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• Given Standard rooms `R101` and `R102` are both reserved for `2026-12-01` to `2026-12-05`<br>• Third booking request for Standard Room for `2026-12-02` to `2026-12-04` | `[ERROR] No available room of type Standard Room for dates 2026-12-02 to 2026-12-04.`<br>Overlap detection prevents overbooking. | `[ERROR] No available room of type Standard Room for dates 2026-12-02 to 2026-12-04.` displayed. Overlap blocked. | **Pass** |
| **TC19** | Billing & Invoice | 4 Nights Stay (Below 5-night long stay discount threshold) | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC: `200012345678`<br>• Category: `2` (Deluxe Room)<br>• Guests: `3` (Standard capacity)<br>• Check-In: `2026-12-01`<br>• Check-Out: `2026-12-05` (4 nights) | **Invoice printed**:<br>• Base: `LKR 36,000.00`<br>• Extra: `LKR 0.00`<br>• Discount: `-LKR 0.00`<br>• Tax (12%): `+LKR 4,320.00`<br>• Advance: `-LKR 10,000.00`<br>• Final Payable: **`LKR 30,320.00`** *(See Section 4.1)* | Invoice printed: Base: `LKR 36,000.00`, Extra: `LKR 0.00`, Discount: `-LKR 0.00`, Tax: `+LKR 4,320.00`, Final: **`LKR 30,320.00`**. Matches proof. | **Pass** |
| **TC20** | Billing & Invoice | 5 Nights Stay (Qualifies for 12% long stay discount) | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC: `200012345678`<br>• Category: `2` (Deluxe Room)<br>• Guests: `3` (Standard capacity)<br>• Check-In: `2026-12-01`<br>• Check-Out: `2026-12-06` (5 nights) | **Invoice printed**:<br>• Base: `LKR 45,000.00`<br>• Extra: `LKR 0.00`<br>• Discount (12%): `-LKR 5,400.00`<br>• Tax (12%): `+LKR 4,752.00`<br>• Advance: `-LKR 10,000.00`<br>• Final Payable: **`LKR 34,352.00`** *(See Section 4.2)* | Invoice printed: Base: `LKR 45,000.00`, Extra: `LKR 0.00`, Discount: `-LKR 5,400.00`, Tax: `+LKR 4,752.00`, Final: **`LKR 34,352.00`**. Matches proof. | **Pass** |
| **TC21** | Billing & Invoice | Extra guest surcharge for occupants exceeding standard capacity | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC: `200012345678`<br>• Category: `1` (Standard Room, Capacity: 2)<br>• Guests: `4` (2 extra guests)<br>• Check-In: `2026-12-01`<br>• Check-Out: `2026-12-04` (3 nights) | **Invoice printed**:<br>• Base: `LKR 18,000.00`<br>• Extra Guest Charges: `LKR 6,000.00`<br>• Discount: `-LKR 0.00`<br>• Tax (10%): `+LKR 2,400.00`<br>• Advance: `-LKR 10,000.00`<br>• Final Payable: **`LKR 16,400.00`** *(See Section 4.3)* | Invoice printed: Base: `LKR 18,000.00`, Extra: `LKR 6,000.00`, Discount: `-LKR 0.00`, Tax: `+LKR 2,400.00`, Final: **`LKR 16,400.00`**. Matches proof. | **Pass** |
| **TC22** | Billing & Invoice | Tax Calculation: Standard Room Type (10% Tax Rate) | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC: `200012345678`<br>• Category: `1` (Standard Room)<br>• Guests: `2`<br>• Check-In: `2026-12-01`<br>• Check-Out: `2026-12-03` (2 nights) | **Invoice printed**:<br>• Base: `LKR 12,000.00`<br>• Extra: `LKR 0.00`<br>• Tax Added (10%): `+LKR 1,200.00`<br>• Advance: `-LKR 10,000.00`<br>• Final Payable: **`LKR 3,200.00`** *(See Section 4.4)* | Invoice printed: Base: `LKR 12,000.00`, Extra: `LKR 0.00`, Tax (10%): `+LKR 1,200.00`, Final: **`LKR 3,200.00`**. Matches proof. | **Pass** |
| **TC23** | Billing & Invoice | Tax Calculation: Suite Room Type (15% Tax Rate, Long Stay + Extra Guests) | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC: `200012345678`<br>• Category: `3` (Suite, Capacity: 4)<br>• Guests: `5` (1 extra guest)<br>• Check-In: `2026-12-01`<br>• Check-Out: `2026-12-07` (6 nights) | **Invoice printed**:<br>• Base: `LKR 84,000.00`<br>• Extra: `LKR 12,000.00`<br>• Discount (12%): `-LKR 10,080.00`<br>• Tax Added (15%): `+LKR 12,888.00`<br>• Advance: `-LKR 10,000.00`<br>• Final Payable: **`LKR 88,808.00`** *(See Section 4.5)* | Invoice printed: Base: `LKR 84,000.00`, Extra: `LKR 12,000.00`, Discount: `-LKR 10,080.00`, Tax: `+LKR 12,888.00`, Final: **`LKR 88,808.00`**. Matches proof. | **Pass** |
| **TC24** | Billing & Invoice | Tax Calculation: Family Suite Room Type (15% Tax Rate) | **Menu**: `[2] Guest` -> `[3] Make a Reservation`<br>• NIC: `200012345678`<br>• Category: `4` (Family Suite, Capacity: 6)<br>• Guests: `6`<br>• Check-In: `2026-12-01`<br>• Check-Out: `2026-12-03` (2 nights) | **Invoice printed**:<br>• Base: `LKR 36,000.00`<br>• Extra: `LKR 0.00`<br>• Tax Added (15%): `+LKR 5,400.00`<br>• Advance: `-LKR 10,000.00`<br>• Final Payable: **`LKR 31,400.00`** *(See Section 4.6)* | Invoice printed: Base: `LKR 36,000.00`, Extra: `LKR 0.00`, Tax (15%): `+LKR 5,400.00`, Final: **`LKR 31,400.00`**. Matches proof. | **Pass** |
| **TC25** | Reservation Cancellation | Eligible cancellation (> 24 hours prior to check-in at 00:00 midnight) | **Menu**: `[2] Guest` -> `[6] Cancel a Reservation`<br>• Booking ID: `BK-0001` (Check-in is 5 days away) | `[SUCCESS] Booking BK-0001 has been successfully CANCELLED.`<br>`[SUCCESS] Refundable advance of LKR 10,000.00 released to guest.`<br>Allocated room status transitions back to `AVAILABLE`. | `[SUCCESS] Booking BK-0001 has been successfully CANCELLED.` and `[SUCCESS] Refundable advance of LKR 10,000.00 released to guest.` displayed. Room R101 reverted to Available. | **Pass** |
| **TC26** | Reservation Cancellation | Ineligible cancellation: within 24 hours of check-in at 00:00 midnight | **Menu**: `[2] Guest` -> `[6] Cancel a Reservation`<br>• Given booking with check-in date = Tomorrow at 00:00 | `[ERROR] Cancellation rejected: Cannot cancel within 24 hours of check-in at 00:00 (Rule: >24h required).`<br>Reservation remains `CONFIRMED`. | `[ERROR] Cancellation rejected: Cannot cancel within 24 hours of check-in at 00:00 (Rule: >24h required).` displayed. Reservation remained Confirmed. | **Pass** |
| **TC27** | Reservation Cancellation | Rejection of cancellation for unknown Booking ID | **Menu**: `[2] Guest` -> `[6] Cancel a Reservation`<br>• Booking ID: `BK-9999` | `[ERROR] Booking with ID 'BK-9999' was not found.`<br>System pauses with `Press Enter to continue...`. | `[ERROR] Booking with ID 'BK-9999' was not found.` displayed. Paused with prompt. | **Pass** |
| **TC28** | Queries & Searching | Case-insensitive partial search by guest name | **Menu**: `[2] Guest` -> `[4] Search Reservations...`<br>• Search By: `2` (Guest Name)<br>• Enter Guest Name: `nimal` | System outputs aligned booking table containing all bookings for "Nimal Silva":<br>`[SUCCESS] Found 1 matching reservation(s).` | Aligned booking table displayed with records for Nimal Silva. `[SUCCESS] Found 1 matching reservation(s).` | **Pass** |
| **TC29** | Queries & Searching | Exact linear search by Booking ID | **Menu**: `[2] Guest` -> `[4] Search Reservations...`<br>• Search By: `1` (Booking ID)<br>• Enter Booking ID: `BK-0001` | System displays single-row aligned booking table for `BK-0001` followed by:<br>`[SUCCESS] Reservation located successfully for ID: BK-0001` | Single-row aligned booking table displayed. `[SUCCESS] Reservation located successfully for ID: BK-0001` | **Pass** |
| **TC30** | Reporting & Views | View all reservations sorted chronologically by check-in date | **Menu**: `[2] Guest` -> `[5] View Reservations by Check-In Date`<br>• System contains bookings with check-in dates `2026-12-15`, `2026-12-01`, and `2026-12-05` | System prints aligned table with rows sorted strictly in ascending order (`2026-12-01`, `2026-12-05`, `2026-12-15`):<br>`[SUCCESS] Total reservations displayed: 3` | Aligned table printed with reservations in chronological order (`2026-12-01`, `2026-12-05`, `2026-12-15`). `[SUCCESS] Total reservations displayed: 3` | **Pass** |

---

## 4. Hand-Calculated Invoice Breakdown & Proofs

Every figure below is derived step-by-step from the coursework formula to verify billing engine accuracy.

### 4.1 TC19: Deluxe Room, 4 Nights, 3 Guests (Below Discount Threshold)
* **Parameters**: Room = Deluxe Room (`dailyRate = 9,000.00`, `capacity = 3`, `extraGuestCharge = 1,500.00`, `taxRate = 12%`), `nights = 4`, `guests = 3`.
* **Step 1 - Base Charge**:
  $$\text{base} = 9,000.00 \times 4 = \text{LKR } 36,000.00$$
* **Step 2 - Extra Guest Charge**:
  $$\text{extraGuests} = \max(0, 3 - 3) = 0$$
  $$\text{extra} = 0 \times 1,500.00 \times 4 = \text{LKR } 0.00$$
* **Step 3 - Long Stay Discount**:
  $$\text{nights} = 4 < 5 \implies \text{discount} = \text{LKR } 0.00$$
* **Step 4 - Taxable Amount**:
  $$\text{taxable} = 36,000.00 - 0.00 + 0.00 = \text{LKR } 36,000.00$$
* **Step 5 - Tax (12%)**:
  $$\text{tax} = 36,000.00 \times 0.12 = \text{LKR } 4,320.00$$
* **Step 6 - Booking Advance Deducted**:
  $$\text{advance} = \text{LKR } 10,000.00$$
* **Step 7 - Final Payable Amount**:
  $$\text{final} = 36,000.00 + 4,320.00 - 10,000.00 = \mathbf{\text{LKR } 30,320.00}$$

---

### 4.2 TC20: Deluxe Room, 5 Nights, 3 Guests (12% Discount Triggered)
* **Parameters**: Room = Deluxe Room (`dailyRate = 9,000.00`, `capacity = 3`, `extraGuestCharge = 1,500.00`, `taxRate = 12%`), `nights = 5`, `guests = 3`.
* **Step 1 - Base Charge**:
  $$\text{base} = 9,000.00 \times 5 = \text{LKR } 45,000.00$$
* **Step 2 - Extra Guest Charge**:
  $$\text{extraGuests} = \max(0, 3 - 3) = 0$$
  $$\text{extra} = 0 \times 1,500.00 \times 5 = \text{LKR } 0.00$$
* **Step 3 - Long Stay Discount (12%)**:
  $$\text{nights} = 5 \ge 5 \implies \text{discount} = 0.12 \times 45,000.00 = \text{LKR } 5,400.00$$
* **Step 4 - Taxable Amount**:
  $$\text{taxable} = 45,000.00 - 5,400.00 + 0.00 = \text{LKR } 39,600.00$$
* **Step 5 - Tax (12%)**:
  $$\text{tax} = 39,600.00 \times 0.12 = \text{LKR } 4,752.00$$
* **Step 6 - Booking Advance Deducted**:
  $$\text{advance} = \text{LKR } 10,000.00$$
* **Step 7 - Final Payable Amount**:
  $$\text{final} = 39,600.00 + 4,752.00 - 10,000.00 = \mathbf{\text{LKR } 34,352.00}$$

---

### 4.3 TC21: Standard Room, 3 Nights, 4 Guests (2 Extra Guests Overage)
* **Parameters**: Room = Standard Room (`dailyRate = 6,000.00`, `capacity = 2`, `extraGuestCharge = 1,000.00`, `taxRate = 10%`), `nights = 3`, `guests = 4`.
* **Step 1 - Base Charge**:
  $$\text{base} = 6,000.00 \times 3 = \text{LKR } 18,000.00$$
* **Step 2 - Extra Guest Charge**:
  $$\text{extraGuests} = \max(0, 4 - 2) = 2$$
  $$\text{extra} = 2 \times 1,000.00 \times 3 = \text{LKR } 6,000.00$$
* **Step 3 - Long Stay Discount**:
  $$\text{nights} = 3 < 5 \implies \text{discount} = \text{LKR } 0.00$$
* **Step 4 - Taxable Amount**:
  $$\text{taxable} = 18,000.00 - 0.00 + 6,000.00 = \text{LKR } 24,000.00$$
* **Step 5 - Tax (10%)**:
  $$\text{tax} = 24,000.00 \times 0.10 = \text{LKR } 2,400.00$$
* **Step 6 - Booking Advance Deducted**:
  $$\text{advance} = \text{LKR } 10,000.00$$
* **Step 7 - Final Payable Amount**:
  $$\text{final} = 24,000.00 + 2,400.00 - 10,000.00 = \mathbf{\text{LKR } 16,400.00}$$

---

### 4.4 TC22: Standard Room, 2 Nights, 2 Guests (Standard 10% Tax Rate)
* **Parameters**: Room = Standard Room (`dailyRate = 6,000.00`, `capacity = 2`, `extraGuestCharge = 1,000.00`, `taxRate = 10%`), `nights = 2`, `guests = 2`.
* **Step 1 - Base Charge**:
  $$\text{base} = 6,000.00 \times 2 = \text{LKR } 12,000.00$$
* **Step 2 - Extra Guest Charge**:
  $$\text{extraGuests} = \max(0, 2 - 2) = 0 \implies \text{extra} = \text{LKR } 0.00$$
* **Step 3 - Long Stay Discount**:
  $$\text{discount} = \text{LKR } 0.00$$
* **Step 4 - Taxable Amount**:
  $$\text{taxable} = 12,000.00 - 0.00 + 0.00 = \text{LKR } 12,000.00$$
* **Step 5 - Tax (10%)**:
  $$\text{tax} = 12,000.00 \times 0.10 = \text{LKR } 1,200.00$$
* **Step 6 - Booking Advance Deducted**:
  $$\text{advance} = \text{LKR } 10,000.00$$
* **Step 7 - Final Payable Amount**:
  $$\text{final} = 12,000.00 + 1,200.00 - 10,000.00 = \mathbf{\text{LKR } 3,200.00}$$

---

### 4.5 TC23: Suite, 6 Nights, 5 Guests (15% Tax Rate, Long Stay + 1 Extra Guest)
* **Parameters**: Room = Suite (`dailyRate = 14,000.00`, `capacity = 4`, `extraGuestCharge = 2,000.00`, `taxRate = 15%`), `nights = 6`, `guests = 5`.
* **Step 1 - Base Charge**:
  $$\text{base} = 14,000.00 \times 6 = \text{LKR } 84,000.00$$
* **Step 2 - Extra Guest Charge**:
  $$\text{extraGuests} = \max(0, 5 - 4) = 1$$
  $$\text{extra} = 1 \times 2,000.00 \times 6 = \text{LKR } 12,000.00$$
* **Step 3 - Long Stay Discount (12%)**:
  $$\text{nights} = 6 \ge 5 \implies \text{discount} = 0.12 \times 84,000.00 = \text{LKR } 10,080.00$$
* **Step 4 - Taxable Amount**:
  $$\text{taxable} = 84,000.00 - 10,080.00 + 12,000.00 = \text{LKR } 85,920.00$$
* **Step 5 - Tax (15%)**:
  $$\text{tax} = 85,920.00 \times 0.15 = \text{LKR } 12,888.00$$
* **Step 6 - Booking Advance Deducted**:
  $$\text{advance} = \text{LKR } 10,000.00$$
* **Step 7 - Final Payable Amount**:
  $$\text{final} = 85,920.00 + 12,888.00 - 10,000.00 = \mathbf{\text{LKR } 88,808.00}$$

---

### 4.6 TC24: Family Suite, 2 Nights, 6 Guests (15% Tax Rate, Full Standard Capacity)
* **Parameters**: Room = Family Suite (`dailyRate = 18,000.00`, `capacity = 6`, `extraGuestCharge = 2,500.00`, `taxRate = 15%`), `nights = 2`, `guests = 6`.
* **Step 1 - Base Charge**:
  $$\text{base} = 18,000.00 \times 2 = \text{LKR } 36,000.00$$
* **Step 2 - Extra Guest Charge**:
  $$\text{extraGuests} = \max(0, 6 - 6) = 0 \implies \text{extra} = \text{LKR } 0.00$$
* **Step 3 - Long Stay Discount**:
  $$\text{discount} = \text{LKR } 0.00$$
* **Step 4 - Taxable Amount**:
  $$\text{taxable} = 36,000.00 - 0.00 + 0.00 = \text{LKR } 36,000.00$$
* **Step 5 - Tax (15%)**:
  $$\text{tax} = 36,000.00 \times 0.15 = \text{LKR } 5,400.00$$
* **Step 6 - Booking Advance Deducted**:
  $$\text{advance} = \text{LKR } 10,000.00$$
* **Step 7 - Final Payable Amount**:
  $$\text{final} = 36,000.00 + 5,400.00 - 10,000.00 = \mathbf{\text{LKR } 31,400.00}$$
