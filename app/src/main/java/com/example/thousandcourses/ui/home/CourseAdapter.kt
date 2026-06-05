package com.example.thousandcourses.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thousandcourses.R
import com.example.thousandcourses.databinding.ItemCourseBinding

class CourseAdapter(
    private val onFavoriteClick: (courseId: String) -> Unit,
    private val onDetailsClick: (courseId: String) -> Unit,
) : ListAdapter<CourseUi, CourseAdapter.CourseViewHolder>(
    CourseDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CourseViewHolder(
        private val binding: ItemCourseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: CourseUi) {
            binding.apply {
                val imageRes = getImageForCourse(course.id)
                val cleanPrice = course.price.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                val cleanRate = course.rate.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                ivCourseImage.setImageResource(imageRes)

                tvTitle.text = course.title
                tvDescription.text = course.text
                tvPrice.text = itemView.context.getString(R.string.price_with_ruble, cleanPrice)
                tvRating.text = itemView.context.getString(R.string.rating_with_star, cleanRate)
                tvStartDate.text = itemView.context.getString(R.string.start_date, course.startDate)

                ivFavorite.setImageResource(
                    if (course.isFavorite) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_border
                )

                ivFavorite.setOnClickListener {
                    onFavoriteClick(course.id)
                }

                btnDetails.setOnClickListener {
                    onDetailsClick(course.id)
                }
            }
        }
        private fun getImageForCourse(courseId: String): Int {
            val courseImages = mapOf(
                "100" to R.drawable.course_default_1,
                "101" to R.drawable.course_default_2,
                "102" to R.drawable.course_default_3,
                "103" to R.drawable.course_default_4,
                "104" to R.drawable.course_default_5,
            )

            val fallbackImages = listOf(
                R.drawable.course_default_1,
                R.drawable.course_default_2,
                R.drawable.course_default_3,
                R.drawable.course_default_4,
                R.drawable.course_default_5,
                R.drawable.course_default_6,
            )

            return courseImages[courseId] ?: fallbackImages.random()
        }
    }
}
