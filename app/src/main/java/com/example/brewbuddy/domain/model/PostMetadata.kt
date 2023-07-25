package com.example.brewbuddy.domain.model

abstract class PostMetadata {
    abstract val id: String
    abstract val bannerUrl: String
    abstract val author: Author
    abstract val title: String
}