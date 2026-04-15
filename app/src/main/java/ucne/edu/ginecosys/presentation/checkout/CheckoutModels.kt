package ucne.edu.ginecosys.presentation.checkout

sealed interface CheckoutEvent {
    data class LoadAppointment(val appointmentId: String) : CheckoutEvent
    data class UpdateAmount(val value: String) : CheckoutEvent
    data class UpdateMethod(val value: String) : CheckoutEvent
    data class UpdateNotes(val value: String) : CheckoutEvent
    data object ProcessPayment : CheckoutEvent
    data object UserMessageShown : CheckoutEvent
}

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val appointmentId: String = "",
    val patientName: String = "",
    val appointmentType: String = "",
    val appointmentDate: String = "",
    val amount: String = "",
    val method: String = "Efectivo",
    val notes: String = "",
    val userMessage: String? = null,
    val paymentCompleted: Boolean = false
)
