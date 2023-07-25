package com.example.brewbuddy.domain.model

abstract class PostMetadata (
    id: String = "",
    bannerUrl: String = "",
    author: Author = Author(),
    title: String = ""
)