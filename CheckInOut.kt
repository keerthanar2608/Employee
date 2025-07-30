import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.Duration


data class DataEmployee(
    val id: Int,
    val FirstName: String,
    val LastName: String,
    val Role: String,
    val ReportingTo: Int? = null
)


data class DataAttendance(
    val EmployeeId: Int,
    val CheckInDate: LocalDate,
    val CheckInTime: LocalTime,
    var CheckOutTime: LocalTime? = null
)


val EmployeeList = mutableListOf<DataEmployee>()
val AttendanceList = mutableListOf<DataAttendance>()


fun validateEmployee(employeeId: Int): DataEmployee? {

    return EmployeeList.find { it.id == employeeId }
}

fun hasAlreadyCheckedIn(employeeId: Int, date: LocalDate): Boolean {

    return AttendanceList.any { it.EmployeeId == employeeId && it.CheckInDate == date }
}


fun recordCheckIn(employeeId: Int, checkInDateTime: LocalDateTime): DataAttendance {

    val checkInDate = checkInDateTime.toLocalDate()
    val checkInTime = checkInDateTime.toLocalTime()
    val attendanceEntry = DataAttendance(employeeId, checkInDate, checkInTime)
    AttendanceList.add(attendanceEntry)
    return attendanceEntry
}


fun recordCheckOut(employeeId: Int, checkOutDateTime: LocalDateTime): Double? {

    val date = checkOutDateTime.toLocalDate()
    val time = checkOutDateTime.toLocalTime()

    val attendance = AttendanceList.find { it.EmployeeId == employeeId && it.CheckInDate == date }
        ?: return null

    if (attendance.CheckOutTime != null) return -1.0

    attendance.CheckOutTime = time

    val duration = Duration.between(attendance.CheckInTime, time)
    return duration.toMinutes().toDouble() / 60.0
}


fun main() {


    EmployeeList.add(DataEmployee(1, "Anu", "M", "Manager"))
    EmployeeList.add(DataEmployee(2, "Sowmi", "S", "HR", 1))
    EmployeeList.add(DataEmployee(3, "Keerthana", "Ravikumar", "TeamLeader", 1))
    EmployeeList.add(DataEmployee(4, "Hashini", "Ravikumar", "Developer", 1))
    EmployeeList.add(DataEmployee(5, "Maha", "E", "Developer", 1))
    EmployeeList.add(DataEmployee(6, "Keerthu", "R", "Developer", 1))
    EmployeeList.add(DataEmployee(7, "Oviya", "B", "Tester", 1))
    EmployeeList.add(DataEmployee(8, "Harini", "S", "Developer", 1))
    EmployeeList.add(DataEmployee(9, "Saru", "R", "Developer", 1))
    EmployeeList.add(DataEmployee(10, "Priya", "R", "Developer", 1))

    val today = LocalDate.now()

    while (true) {
        print("\nEnter command (checkin/checkout), Employee ID and optional DateTime (yyyy-MM-ddTHH:mm), separated by space: ")
        val input = readln()

        if (input == "-1") {
            println("Exiting.")
            break
        }

        val parts = input.trim().split(" ")

        if (parts.size < 2) {
            println("Please enter both command and Employee ID.")
            continue
        }

        val command = parts[0].lowercase()
        val employeeId = parts[1].toIntOrNull()
        if (employeeId == null) {
            println("Invalid Employee ID.")
            continue
        }

        val dateTimeInput = if (parts.size == 3) {
            parts[2]
        } else {
            null
        }
        val dateTime = try {
            if (dateTimeInput == null) {
                LocalDateTime.now()
            }
            else{
                LocalDateTime.parse(dateTimeInput)
            }
        } catch (e: Exception) {
            println("Invalid date-time format. Use yyyy-MM-ddTHH:mm")
            continue
        }

        if (dateTime.toLocalDate() != today) {
            println("Only today's date ($today) is allowed.")
            continue
        }

        val employee = validateEmployee(employeeId)
        if (employee == null) {
            println("Employee ID $employeeId is not valid.")
            continue
        }

        when (command) {
            "checkin" -> {
                if (hasAlreadyCheckedIn(employeeId, dateTime.toLocalDate())) {
                    println("Employee ID ${employee.id}: ${employee.FirstName} ${employee.LastName} has already checked in on ${dateTime.toLocalDate()}.")
                    continue
                }
                val attendance = recordCheckIn(employeeId, dateTime)
                val formattedTime = attendance.CheckInTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                val reportingMessage = employee.ReportingTo?.let { "(Reports to: $it)" } ?: "null"
                println("Employee ID ${employee.id}: ${employee.FirstName} ${employee.LastName} checked in on ${attendance.CheckInDate} at $formattedTime. $reportingMessage")
            }

            "checkout" -> {

                val hoursWorked = recordCheckOut(employeeId, dateTime)

                when {
                    hoursWorked == null -> println("Employee ID ${employee.id} has not checked in today. Cannot checkout.")
                    hoursWorked == -1.0 -> println("Employee ID ${employee.id} has already checked out today.")

                    else -> {
                        val formattedTime = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        println("Employee ID ${employee.id}: ${employee.FirstName} ${employee.LastName} checked out on ${dateTime.toLocalDate()} at $formattedTime.")
                        println("Total working hours: %.2f".format(hoursWorked))
                    }
                }
            }

            else -> {
                println("Invalid command. Use 'checkin' or 'checkout'.")
            }
        }
    }
}
