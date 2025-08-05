import java.time.LocalDateTime


class AttendanceList : ArrayList<Attendance>() {

    fun addAttendance(attendance: Attendance): Boolean {
        val alreadyCheckedInToday = this.any {
            it.employeeId == attendance.employeeId &&
                    it.checkIn.toLocalDate() == attendance.checkIn.toLocalDate()
        }

        if (alreadyCheckedInToday) {
            return false
        }

        this.add(attendance)
        return true
    }


    fun updateCheckOut(employeeId: String, checkOutTime: LocalDateTime): Boolean {
        val record = this.find { it.employeeId == employeeId && it.checkOut == null }
        return if (record != null) {
            record.checkOut = checkOutTime

            true
        } else {
            false
        }
    }

    fun deleteAttendance(employeeId: String): Boolean {
        return this.removeIf { it.employeeId == employeeId }
    }

    fun getAll(): List<Attendance> = this.toList()

    fun findAttendanceById(employeeId: String): Attendance? {
        return this.find { it.employeeId == employeeId && it.checkOut == null }
    }

    override fun toString(): String {
        return if (this.isEmpty()) {
            "No attendance records found."
        } else {
            this.joinToString("\n") { it.toString() }
        }
    }
}
