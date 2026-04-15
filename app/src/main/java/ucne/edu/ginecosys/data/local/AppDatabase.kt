package ucne.edu.ginecosys.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ucne.edu.ginecosys.data.local.dao.AppointmentDao
import ucne.edu.ginecosys.data.local.dao.ConsultationDao
import ucne.edu.ginecosys.data.local.dao.InsuranceDao
import ucne.edu.ginecosys.data.local.dao.PatientDao
import ucne.edu.ginecosys.data.local.entities.AppointmentEntity
import ucne.edu.ginecosys.data.local.entities.ConsultationEntity
import ucne.edu.ginecosys.data.local.entities.InsuranceEntity
import ucne.edu.ginecosys.data.local.entities.PatientEntity

@Database(
    entities = [
        PatientEntity::class,
        AppointmentEntity::class,
        ConsultationEntity::class,
        InsuranceEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun consultationDao(): ConsultationDao
    abstract fun insuranceDao(): InsuranceDao
}
