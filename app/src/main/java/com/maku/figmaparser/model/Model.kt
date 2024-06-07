package com.maku.figmaparser.model

fun mapComponentsToComponentSets(components: Map<String, Component>): Map<String, List<Component>> {
    return components.values.groupBy { it.componentSetId }
}

fun getComponentSets(figmaDocument: FigmaDocument): Map<String, ComponentSet> {
    return figmaDocument.componentSets
}

fun getComponents(figmaDocument: FigmaDocument): Map<String, Component> {
    return figmaDocument.components
}

data class FigmaDocument(
    val document: Map<String, Any>,
    val components: Map<String, Component>,
    val componentSets: Map<String, ComponentSet>,
    val schemaVersion: Int,
    val styles: Map<String, Any>,
    val name: String,
    val lastModified: String,
    val thumbnailUrl: String,
    val version: String,
    val role: String,
    val editorType: String,
    val linkAccess: String
)

data class Component(
    val key: String,
    val name: String,
    val description: String,
    val remote: Boolean,
    val componentSetId: String,
    val documentationLinks: List<String>
)

data class ComponentSet(
    val key: String,
    val name: String,
    val description: String,
    val documentationLinks: List<String>
)
