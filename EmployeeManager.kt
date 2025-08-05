import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalDate

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


class EmployeeManager {
    private val employeeList = EmployeeList()
    private val attendanceList = AttendanceList()

    fun addEmployee() {
        println("Enter first name:")
        val firstName = readln().trim()

        println("Enter last name:")
        val lastName = readln().trim()

        println("Enter role ( MANAGER,DEVELOPER,HR):")
        val roleInput = readln().trim().uppercase()
        val role = try {
            Role.valueOf(roleInput)
        } catch (e: IllegalArgumentException) {
            println("Invalid role.")
            return
        }

        println("Enter department ( IT, SALES, MARKETING, FINANCE):")
        val deptInput = readln().trim().uppercase()
        val department = try {
            Department.valueOf(deptInput)
        } catch (e: IllegalArgumentException) {
            println("Invalid department.")
            return
        }

        println("Enter reporting to (or leave empty):")
        val reportingTo = readln().trim().takeIf { it.isNotEmpty() }

        val emp = Employee(firstName, lastName, role, department, reportingTo)

        val added = employeeList.addEmployee(emp)
        if (added) {
            println("Employee added successfully with ID: ${emp.id}")
        } else {
            println("Failed to add employee. Invalid data.")
        }
    }

    fun deleteEmployee() {
        println("Enter employee ID to delete:")
        val id = readln().trim()

        val deleted = employeeList.deleteEmployeeById(id)
        if (deleted) {
            println("Employee deleted successfully.")
        } else {
            println("Employee with ID $id not found.")
        }
    }

    fun listEmployees() {
        val all = employeeList.getAllEmployees()
        if (all.isEmpty()) {
            println("No employees found.")
        } else {
            println("Employee List:")
            all.forEach { println(it) }
        }
    }


    fun checkIn() {
        println("Enter employee ID for check-in:")
        val id = readLine()?.trim().orEmpty()

        val employee = employeeList.getEmployeeById(id)
        if (employee == null) {
            println("Employee with ID $id not found.")
            return
        }

        val existingAttendance = attendanceList.findAttendanceById(id)
        if (existingAttendance != null) {
            println("Employee has already checked in and not checked out yet.")
            return
        }

        println("Enter check-in date and time (yyyy-MM-dd HH:mm) or take current date and time:")
        val checkInInput = readLine()?.trim().orEmpty()

        val checkInTime = if (checkInInput.isEmpty()) {
            LocalDateTime.now()
        } else {
            try {
                val parsedCheckIn = LocalDateTime.parse(checkInInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                val currentTime = LocalDateTime.now()
                if (parsedCheckIn.isAfter(currentTime)) {
                    println("Check-in time cannot be in the future. Using current time.")
                    currentTime
                } else {
                    parsedCheckIn
                }
            } catch (e: Exception) {
                println("Invalid date/time format. Using current time.")
                LocalDateTime.now()
            }
        }

        val attendance = Attendance(id, checkInTime)

        if (attendanceList.addAttendance(attendance)) {
            println("Check-in recorded for employee $id at ${checkInTime.format(formatter)}")
        } else {
            println("Only one check-in allowed per day for employee $id.")
        }
    }

    fun checkOut() {
        println("Enter employee ID for check-out:")
        val id = readln().trim()

        val attendance = attendanceList.findAttendanceById(id)
        if (attendance == null) {
            println("No active check-in found for employee $id.")
            return
        }

        println("Enter check-out date and time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
        val checkOutInput = readln().trim()

        val checkOutTime = if (checkOutInput.isEmpty()) {
            LocalDateTime.now()
        } else {
            try {
                val parsedCheckOut = LocalDateTime.parse(checkOutInput, formatter)
                val currentTime = LocalDateTime.now()
                if (parsedCheckOut.isAfter(currentTime)) {
                    println("Check-out time cannot be in the future. Using current time.")
                    currentTime
                } else {
                    parsedCheckOut
                }
            } catch (e: Exception) {
                println("Invalid date/time format. Error: ${e.message}")
                LocalDateTime.now()
            }

        }

        if (checkOutTime.isBefore(attendance.checkIn)) {
            println("Check-out time cannot be before check-in time.")
            return
        }

        if (attendanceList.updateCheckOut(id, checkOutTime)) {


            val duration = Duration.between(attendance.checkIn, checkOutTime)
            val hours = duration.toHours().toInt()
            val minutes = (duration.toMinutes() % 60).toInt()
            attendance.workingHours = Pair(hours, minutes)

            println("Check-out recorded for employee $id at $checkOutTime")
            println("Working hours: $hours hrs $minutes mins")
        } else {
            println("Failed to record check-out for employee $id.")
        }
    }
    fun listWorkingHoursBetweenDates() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        print("Enter From Date (yyyy-MM-dd): ")
        val fromDateInput = readLine()
        print("Enter To Date (yyyy-MM-dd): ")
        val toDateInput = readLine()

        if (fromDateInput.isNullOrBlank() || toDateInput.isNullOrBlank()) {
            println("Invalid input. Dates cannot be blank.")
            return
        }

        val fromDate: LocalDate
        val toDate: LocalDate

        try {
            fromDate = LocalDate.parse(fromDateInput, formatter)
            toDate = LocalDate.parse(toDateInput, formatter)
        } catch (e: Exception) {
            println("Error: Invalid date format. Use yyyy-MM-dd.")
            return
        }

        if (employeeList.isEmpty()) {
            println("No employees found.")
            return
        }

        for (employee in employeeList) {
            val records = attendanceList.filter { record ->
                record.employeeId == employee.id &&
                        !record.checkIn.toLocalDate().isBefore(fromDate) &&
                        !record.checkIn.toLocalDate().isAfter(toDate) &&
                        record.checkOut != null
            }.sortedBy { it.checkIn }

            if (records.isEmpty()) {
                println("No attendance records for ${employee.firstName} ${employee.lastName} (${employee.id}) between $fromDate and $toDate.")
                continue
            }

            val totalMinutes = records.sumOf {
                Duration.between(it.checkIn, it.checkOut).toMinutes()
            }

            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60

            println("Employee: ${employee.firstName} ${employee.lastName} (${employee.id}) worked: ${hours}h ${minutes}m from $fromDate to $toDate.")
        }
    }


    fun listEmployeesCheckedIn() {

        val employeesNotCheckedOut = attendanceList.filter { it.checkOut == null }


        if (employeesNotCheckedOut.isEmpty()) {
            println("No employees have checked in but not checked out yet.")
            return
        }


        println("Employees who checked in but have not checked out yet:")
        employeesNotCheckedOut.forEach { attendance ->
            val employee = employeeList.getEmployeeById(attendance.employeeId)
            if (employee != null) {
                val checkInTime = attendance.checkIn.format(formatter)
                println("${employee.firstName} ${employee.lastName} (ID: ${employee.id}) checked in at $checkInTime")
            }
        }
    }


    fun deleteAttendance() {
        println("Enter employee ID to delete attendance record:")
        val id = readln().trim()

        val removed = attendanceList.deleteAttendance(id)
        if (removed) {
            println("Attendance record deleted for employee $id.")
        } else {
            println("No attendance records found for employee $id.")
        }
    }

    fun listAttendance() {
        val all = attendanceList.getAll()
        if (all.isEmpty()) {
            println("No attendance records found.")
        } else {
            all.forEach { println(it) }
        }
    }
}

