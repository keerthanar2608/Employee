import java.time.LocalDate

fun main() {
    val employeeManager = EmployeeManager()


    while (true) {
        println(
            """
            |1. Add New Employee
            |2. Delete Employee
            |3. List All Employees
            |4. Check In Attendance
            |5. Check Out Attendance
            |6. Delete Attendance Record
            |7. List Attendance Records
            |8. View working hours between dates
            |9. List CheckedIn Employees
            |0. Exit
            |Enter your choice:
            """.trimMargin()
        )

        when (readln().trim()) {
            "1" -> employeeManager.addEmployee()
            "2" -> employeeManager.deleteEmployee()
            "3" -> employeeManager.listEmployees()
            "4" -> employeeManager.checkIn()
            "5" -> employeeManager.checkOut()
            "6" -> employeeManager.deleteAttendance()
            "7" -> employeeManager.listAttendance()
            "8" -> employeeManager.listWorkingHoursBetweenDates()
            "9" -> employeeManager.listEmployeesCheckedIn()
            "0" -> {
                println("Exiting...")
                return
            }
            else -> println("Invalid choice. Please try again.")
        }
    }
}
