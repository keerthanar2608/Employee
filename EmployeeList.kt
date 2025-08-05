class EmployeeList : ArrayList<Employee>() {

    fun addEmployee(emp: Employee): Boolean {
        if (!emp.isValid()) return false

        if (this.any { it.firstName == emp.firstName && it.lastName == emp.lastName }) {
            return false
        }

        this.add(emp)
        return true
    }

    fun deleteEmployeeById(id: String): Boolean {
        return this.removeIf { it.id == id }
    }

    fun getEmployeeById(id: String): Employee? {
        return this.find { it.id == id }
    }

    fun getAllEmployees(): List<Employee> {
        return this.toList()
    }

     fun updateEmployee(updatedEmp: Employee): Boolean {
         if (!updatedEmp.isValid()) return false
         val index = this.indexOfFirst { it.id == updatedEmp.id }
         if (index != -1) {
             this[index] = updatedEmp
             return true
         }
         return false
     }


}
