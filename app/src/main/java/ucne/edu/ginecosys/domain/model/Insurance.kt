package ucne.edu.ginecosys.domain.model

import java.util.UUID

data class Insurance(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val type: String = "",
    val phone: String = "",
    val isActive: Boolean = true
)
