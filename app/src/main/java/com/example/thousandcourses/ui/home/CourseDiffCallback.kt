package com.example.thousandcourses.ui.home

import androidx.recyclerview.widget.DiffUtil

class CourseDiffCallback : DiffUtil.ItemCallback<CourseUi>() {
    override fun areItemsTheSame(
        oldItem: CourseUi,
        newItem: CourseUi,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: CourseUi,
        newItem: CourseUi,
    ): Boolean = oldItem == newItem
}