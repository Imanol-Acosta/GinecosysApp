package ucne.edu.ginecosys.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ucne.edu.ginecosys.data.local.dao.AppointmentDao
import ucne.edu.ginecosys.data.local.dao.PatientDao
import ucne.edu.ginecosys.data.local.entities.AppointmentEntity
import ucne.edu.ginecosys.data.local.entities.PatientEntity

@Database(
    entities = [PatientEntity::class, AppointmentEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun appointmentDao(): AppointmentDao
}
