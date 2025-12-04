package com.example.chatbotapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotapp.network.ApiClient
import com.example.chatbotapp.network.CourseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CoursesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val courses: List<MSUCourse> = emptyList()
)

class CoursesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesUiState())
    val uiState: StateFlow<CoursesUiState> = _uiState

    private fun CourseDto.toMSUCourse(): MSUCourse =
        MSUCourse(
            courseCode = code,
            courseName = name,
            credits = credits,
            prerequisites = prerequisites
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() },
            offered = offered
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() },
            category = category ?: "Core"
        )

    fun loadCourses(category: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = ApiClient.courseApi.getCourses(category)
                if (response.isSuccessful) {
                    val body = response.body().orEmpty()
                    val mapped = body.map { it.toMSUCourse() }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        courses = mapped,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load courses: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error: ${e.localizedMessage}"
                )
            }
        }
    }
}
