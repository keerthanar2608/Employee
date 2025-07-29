# Employee
<pre>
  DATA CLASSES

1. DataEmployee
Stores details about an employee.

id: Int

FirstName: String

LastName: String

Role: String

ReportingTo: Int? (nullable, optional reporting manager ID)

2. DataAttendance
Stores daily attendance per employee.

EmployeeId: Int

CheckInDate: LocalDate

CheckInTime: LocalTime

CheckOutTime: LocalTime? (nullable)

FUNCTIONS

1. validateEmployee(employeeId: Int): DataEmployee?
Checks whether an employee with the given ID exists in the EmployeeList.

2. hasAlreadyCheckedIn(employeeId: Int, date: LocalDate): Boolean
Checks whether the given employee has already checked in on the specified date.

3. recordCheckIn(employeeId: Int, checkInDateTime: LocalDateTime): DataAttendance
Records the check-in details of the employee and adds it to the attendance list.

4. recordCheckOut(employeeId: Int, checkOutDateTime: LocalDateTime): Double?
If valid check-in exists and employee has not already checked out:

Records check-out time

Returns total working hours as a Double

If no check-in found, returns null

If already checked out, returns -1.0
</pre>
