class Employee(
    val firstName: String,
    val lastName: String,
    val role: Role,
    val department: Department,
    val reportingTo: String? = null
) {
    val id: String = generateId()

    companion object {
        private var counter = 1

        private fun generateId(): String {
            return "EI" + counter++.toString().padStart(4, '0')
        }
    }

    fun isValid(): Boolean {
        if (firstName.isBlank() || lastName.isBlank()) return false
        if (reportingTo != null && reportingTo.isBlank()) return false
        return true
    }

    override fun toString(): String {
        return "Employee id=$id, FirstName=$firstName, LastName=$lastName, Role=$role, Department=$department, ReportingTo=${reportingTo ?: "N/A"}"
    }
}
