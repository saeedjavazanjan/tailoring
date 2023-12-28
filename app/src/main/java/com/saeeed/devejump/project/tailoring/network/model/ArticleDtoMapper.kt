package com.saeeed.devejump.project.tailoring.network.model

import com.saeeed.devejump.project.tailoring.domain.model.Article
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.util.DomainMapper

class ArticleDtoMapper:DomainMapper<ArticleDto,Article> {
    override fun mapToDomainModel(model: ArticleDto): Article {
        return Article(
            id=model.id,
            title = model.title,
            image = model.image,
            html = model.html
        )
    }
    override fun mapFromDomainModel(domainModel: Article): ArticleDto {
        return ArticleDto(
            id=domainModel.id,
            title = domainModel.title,
            image = domainModel.image,
            html = domainModel.html
        )
    }
    fun toDomainList(initial: List<ArticleDto>): List<Article>{
        return initial.map { mapToDomainModel(it) }
    }
}