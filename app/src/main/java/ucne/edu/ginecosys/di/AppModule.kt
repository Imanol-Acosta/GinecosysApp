package ucne.edu.ginecosys.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ucne.edu.ginecosys.data.repository.ConsultationRepositoryImpl
import ucne.edu.ginecosys.data.repository.InsuranceRepositoryImpl
import ucne.edu.ginecosys.data.repository.PatientRepositoryImpl
import ucne.edu.ginecosys.domain.repository.ConsultationRepository
import ucne.edu.ginecosys.domain.repository.InsuranceRepository
import ucne.edu.ginecosys.domain.repository.PatientRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindPatientRepository(
        patientRepositoryImpl: PatientRepositoryImpl
    ): PatientRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: ucne.edu.ginecosys.data.repository.AuthRepositoryImpl
    ): ucne.edu.ginecosys.domain.repository.AuthRepository

    @Binds
    @Singleton
    abstract fun bindAppointmentRepository(
        appointmentRepositoryImpl: ucne.edu.ginecosys.data.repository.AppointmentRepositoryImpl
    ): ucne.edu.ginecosys.domain.repository.AppointmentRepository

    @Binds
    @Singleton
    abstract fun bindConsultationRepository(
        consultationRepositoryImpl: ConsultationRepositoryImpl
    ): ConsultationRepository

    @Binds
    @Singleton
    abstract fun bindInsuranceRepository(
        insuranceRepositoryImpl: InsuranceRepositoryImpl
    ): InsuranceRepository
}
