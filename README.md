##  Features

- `feat: validateEmployee` – Checks whether an employee exists in the system
- `feat: recordCheckIn` – Logs employee check-in with date and time
- `feat: recordCheckOut` – Logs check-out and calculates working hours



# Data class
 `DataEmployee`
Represents the details of an employee in the system.

id: Int – Unique employee ID

FirstName: String – Employee’s first name

LastName: String – Employee’s last name

Role: String – Job title or position

ReportingTo: Int? – (Optional) ID of the manager they report to; can be null if none

 `DataAttendance`
Represents daily attendance information for one employee.

EmployeeId: Int – ID of the employee this record belongs to

CheckInDate: LocalDate – Date of check-in

CheckInTime: LocalTime – Time of check-in

CheckOutTime: LocalTime? – (Optional) Time of check-out; null if not checked out yet

# FUNCTIONS:
`validateEmployee(employeeId: Int): DataEmployee?`
Checks if an employee with the given employeeId exists in the EmployeeList.

 Returns the corresponding DataEmployee if found

 Returns null if the employee doesn’t exist

` hasAlreadyCheckedIn(employeeId: Int, date: LocalDate): Boolean`
Checks if the employee with the given employeeId has already checked in on the specified date.

Returns true if already checked in

Returns false if not

` recordCheckIn(employeeId: Int, checkInDateTime: LocalDateTime): DataAttendance`
Records the employee’s check-in time and creates a new DataAttendance entry.

Stores both the date and time of check-in

Returns the created DataAttendance object

` recordCheckOut(employeeId: Int, checkOutDateTime: LocalDateTime): Double?`
Handles employee check-out logic. Returns total working hours if valid.

If employee checked in and hasn’t checked out yet:

Updates the record with check-out time

Returns total working hours as a Double

If no check-in is found:

Returns null

If already checked out:

Returns -1.0
