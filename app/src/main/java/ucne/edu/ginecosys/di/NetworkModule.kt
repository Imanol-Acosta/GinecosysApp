package ucne.edu.ginecosys.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ucne.edu.ginecosys.core.config.SupabaseConfig
import ucne.edu.ginecosys.core.network.SupabaseInterceptor
import ucne.edu.ginecosys.data.remote.AuthApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        supabaseInterceptor: SupabaseInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(supabaseInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SupabaseConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSupabasePatientApi(retrofit: Retrofit): ucne.edu.ginecosys.data.remote.SupabasePatientApi {
        return retrofit.create(ucne.edu.ginecosys.data.remote.SupabasePatientApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSupabaseAppointmentApi(retrofit: Retrofit): ucne.edu.ginecosys.data.remote.SupabaseAppointmentApi {
        return retrofit.create(ucne.edu.ginecosys.data.remote.SupabaseAppointmentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSupabaseClinicApi(retrofit: Retrofit): ucne.edu.ginecosys.data.remote.SupabaseClinicApi {
        return retrofit.create(ucne.edu.ginecosys.data.remote.SupabaseClinicApi::class.java)
    }
}
