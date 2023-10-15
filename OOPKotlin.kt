abstract class Employee(
    var name: String,
    var surname: String,
    var baseSalary: Int,
    var experience: Int
) {
    open val countedSalary: Double
        get() {
            var salary = baseSalary.toDouble()
			
            if (experience > 5) {
                salary = salary * 1.2 + 500.0
            } else if (experience > 2) {
                salary += 200.0
            }

            return salary
        }
}

class Developer(
    name: String,
    surname: String,
    baseSalary: Int,
    experience: Int
) : Employee(name, surname, baseSalary, experience) {
}

class Designer(
    name: String,
    surname: String,
    baseSalary: Int,
    experience: Int,
    effCoef: Double
) : Employee(name, surname, baseSalary, experience) {
    var effCoef = checkCoef(effCoef)
        set(value) {
            field = checkCoef(value)
        }

    private fun checkCoef(coef: Double): Double {
        if (coef <= 1 && coef >= 0) {
            return coef
        } else if (coef > 1) {
            return 1.0
        } else {
            return 0.0
        }
    }

    override val countedSalary: Double
        get() {
            return super.countedSalary * effCoef
        }
}

class Manager(
    name: String,
    surname: String,
    baseSalary: Int,
    experience: Int,
    val team: MutableList<Employee> = mutableListOf()
) : Employee(name, surname, baseSalary, experience) {
    override val countedSalary: Double
        get() {
            var salary = super.countedSalary

            if (team.size > 5) {
                salary += 200.0
                if (team.size > 10) {
                    salary += 100.0
                }
            }

            val developerCount = team.count { it is Developer }
            if (developerCount > team.size / 2) {
                salary *= 1.1
            }
			
            val roundedNumber = String.format("%.2f", salary).toDouble()
            return roundedNumber
        }
}

class Department(
    val managers: MutableList<Manager> = mutableListOf()
) {
    fun giveSalary() {
        for (manager in managers) {
            manager.team.forEach { employee ->
                val salary = employee.countedSalary
                println("${employee.name} ${employee.surname} отримав $salary шекелів")
            }
        }
        
        for (manager in managers) {
            val salary = manager.countedSalary
            println("Менеджер ${manager.name} ${manager.surname} отримав $salary шекелів")
        }
    }
}

fun main() {
    // Створення департаменту, різних працівників, додавання до команд тощо
    val department = Department()
    val manager1 = Manager("Bill", "Bon", 100000, 6)
    val manager2 = Manager("Anna", "Marice", 150000, 9)
    val developer1 = Developer("Bob", "Jordan", 95000, 5)
    val developer2 = Developer("Evan", "Back", 125000, 8)
    val developer3 = Developer("Erica", "Bouch", 120000, 9)
    val designer1 = Designer("Backy", "Clark", 97000, 7, -0.95)
    val designer2 = Designer("Danny", "Black", 90000, 4, 0.99)
    val designer3 = Designer("Daria", "Marick", 90000, 5, 0.91)

    manager1.team.add(developer1)
    manager1.team.add(developer2)
    manager1.team.add(developer3)
    manager1.team.add(designer3)
    manager2.team.add(designer1)
    manager2.team.add(designer2)

    department.managers.add(manager1)
    department.managers.add(manager2)

    // Виплата зарплати
    department.giveSalary()
}