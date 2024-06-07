package com.maku.figmaparser.model.finali

import com.squareup.moshi.JsonClass

//data class FigmaFile(
//    val document: Node
//)

@JsonClass(generateAdapter = true)
data class FigmaResponse(
    val document: Document
)

data class FigmaFile(
    val name: String,
    val role: String,
    val lastModified: String,
    val editorType: String,
    val thumbnailUrl: String,
    val version: String,
    val document: Node,
    val components: Map<String, Component>,
    val componentSets: Map<String, ComponentSet>,
    val schemaVersion: Int,
    val styles: Map<String, Style>,
    val mainFileKey: String?,
)

data class Branch(
    val key: String,
    val name: String,
    val thumbnail_url: String,
    val last_modified: String,
    val link_access: String
)

data class Node(
    val id: String,
    val name: String,
    val visible: Boolean = true,
    val type: String,
    val rotation: Double? = null,
    val pluginData: Any? = null,
    val sharedPluginData: Any? = null,
    val componentPropertyReferences: Map<String, String>? = null,
    val boundVariables: Map<String, VariableAlias>? = null,
    val explicitVariableModes: Map<String, String>? = null,

    // Geometry-related properties (if applicable)
    val x: Double?, // X-coordinate of the top-left corner
    val y: Double?, // Y-coordinate of the top-left corner
    val width: Double?, // Width of the node
    val height: Double?, // Height of the node

    // Style-related properties
    val styles: Map<String, String>?, // Map of style properties and their values (e.g., "fills", "strokes")
    val fills: List<Paint>?, // List of fill objects (color, gradient, etc.)
    val strokes: List<Paint>?, // List of stroke objects (color, width, etc.)
    val strokeWeight: Double?, // Width of the stroke
    val cornerRadius: Double?, // Corner radius (if applicable)

    // Text-related properties (if applicable)
    val characters: String?, // The text content of the node
    val style: TypeStyle?, // The text style object

    // Children (if applicable)
    val children: List<Node>? // List of child nodes
)

enum class NodeType {
    DOCUMENT,
    CANVAS,
    FRAME,
    GROUP,
    SECTION,
    VECTOR,
    BOOLEAN_OPERATION,
    STAR,
    LINE,
    ELLIPSE,
    REGULAR_POLYGON,
    RECTANGLE,
    TABLE,
    TABLE_CELL,
    TEXT,
    SLICE,
    COMPONENT,
    COMPONENT_SET,
    INSTANCE,
    STICKY,
    SHAPE_WITH_TEXT,
    CONNECTOR,
    WASHI_TAPE
}

class Document(
    id: String,
    name: String,
    visible: Boolean = true,
    rotation: Double? = null,
    pluginData: Any? = null,
    sharedPluginData: Any? = null,
    componentPropertyReferences: Map<String, String>? = null,
    boundVariables: Map<String, VariableAlias>? = null,
    explicitVariableModes: Map<String, String>? = null,
    val canvases: List<Canvas> = listOf()
)


class Canvas(
    id: String,
    name: String,
    visible: Boolean = true,
    rotation: Double? = null,
    pluginData: Any? = null,
    sharedPluginData: Any? = null,
    componentPropertyReferences: Map<String, String>? = null,
    boundVariables: Map<String, Any>? = null,
    explicitVariableModes: Map<String, String>? = null,
    val children: List<Node> = listOf(),
    val backgroundColor: Color? = null,
    val prototypeStartNodeID: String? = null,
    val flowStartingPoints: List<FlowStartingPoint> = listOf(),
    val prototypeDevice: PrototypeDevice? = null,
    val exportSettings: List<ExportSetting> = listOf(),
    val measurements: List<Measurement> = listOf()
)

class Frame(
    id: String,
    name: String,
    visible: Boolean = true,
    rotation: Float? = null,
    pluginData: Any? = null,
    sharedPluginData: Any? = null,
    componentPropertyReferences: Map<String, String>? = null,
    boundVariables: Map<String, Any>? = null,
    explicitVariableModes: Map<String, String>? = null,
    val children: List<Node> = listOf(),
    val locked: Boolean = false,
    val fills: List<Paint> = listOf(),
    val strokes: List<Paint> = listOf(),
    val strokeWeight: Float? = null,
    val strokeAlign: String? = null,
    val strokeDashes: List<Float> = listOf(),
    val cornerRadius: Float? = null,
    val rectangleCornerRadii: List<Float> = listOf(),
    val cornerSmoothing: Float? = null,
    val exportSettings: List<ExportSetting> = listOf(),
    val blendMode: BlendMode? = null,
    val preserveRatio: Boolean = false,
    val constraints: LayoutConstraint? = null,
    val layoutAlign: String? = null,
    val transitionNodeID: String? = null,
    val transitionDuration: Float? = null,
    val transitionEasing: EasingType? = null,
    val opacity: Float = 1.0f,
    val absoluteBoundingBox: Rectangle? = null,
    val absoluteRenderBounds: Rectangle? = null,
    val size: Vector? = null,
    val minWidth: Float? = null,
    val maxWidth: Float? = null,
    val minHeight: Float? = null,
    val maxHeight: Float? = null,
    val relativeTransform: Transform? = null,
    val clipsContent: Boolean = false,
    val layoutMode: String = "NONE",
    val layoutSizingHorizontal: String? = null,
    val layoutSizingVertical: String? = null,
    val layoutWrap: String = "NO_WRAP",
    val primaryAxisSizingMode: String = "AUTO",
    val counterAxisSizingMode: String = "AUTO",
    val primaryAxisAlignItems: String = "MIN",
    val counterAxisAlignItems: String = "MIN",
    val counterAxisAlignContent: String = "AUTO",
    val paddingLeft: Float = 0f,
    val paddingRight: Float = 0f,
    val paddingTop: Float = 0f,
    val paddingBottom: Float = 0f,
    val horizontalPadding: Float = 0f,
    val verticalPadding: Float = 0f,
    val itemSpacing: Float = 0f,
    val counterAxisSpacing: Float = 0f,
    val layoutPositioning: String = "AUTO",
    val itemReverseZIndex: Boolean = false,
    val strokesIncludedInLayout: Boolean = false,
    val layoutGrids: List<LayoutGrid> = listOf(),
    val overflowDirection: String = "NONE",
    val effects: List<Effect> = listOf(),
    val isMask: Boolean = false,
    val isMaskOutline: Boolean = false,
    val maskType: String? = null,
    val styles: Map<StyleType, String>? = null,
    val annotations: List<Annotation> = listOf()
)

data class Color(
    val r: Double,
    val g: Double,
    val b: Double,
    val a: Double
)

data class ExportSetting(
    val suffix: String,
    val format: String,
    val constraint: Constraint
)

data class Constraint(
    val type: String,
    val value: Double
)

data class Rectangle(
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double
)

data class ArcData(
    val startingAngle: Double,
    val endingAngle: Double,
    val innerRadius: Double
)

enum class BlendMode {
    PASS_THROUGH,
    NORMAL,
    DARKEN,
    MULTIPLY,
    LINEAR_BURN,
    COLOR_BURN,
    LIGHTEN,
    SCREEN,
    LINEAR_DODGE,
    COLOR_DODGE,
    OVERLAY,
    SOFT_LIGHT,
    HARD_LIGHT,
    DIFFERENCE,
    EXCLUSION,
    HUE,
    SATURATION,
    COLOR,
    LUMINOSITY
}

enum class MaskType {
    ALPHA,
    VECTOR,
    LUMINANCE
}

enum class EasingType {
    EASE_IN,
    EASE_OUT,
    EASE_IN_AND_OUT,
    LINEAR,
    GENTLE_SPRING
}

data class FlowStartingPoint(
    val nodeId: String,
    val name: String
)

data class LayoutConstraint(
    val vertical: String,
    val horizontal: String
)

data class LayoutGrid(
    val pattern: String,
    val sectionSize: Double,
    val visible: Boolean,
    val color: Color,
    val alignment: String? = null,
    val gutterSize: Double? = null,
    val offset: Double? = null,
    val count: Int? = null
)

data class Effect(
    val type: String,
    val visible: Boolean,
    val radius: Double,
    val color: Color? = null,
    val blendMode: BlendMode? = null,
    val offset: Vector? = null,
    val spread: Double = 0.0,
    val showShadowBehindNode: Boolean? = null
)

data class Hyperlink(
    val type: String,
    val url: String? = null,
    val nodeID: String? = null
)

data class DocumentationLink(
    val uri: String
)

data class Paint(
    val type: String,
    val visible: Boolean = true,
    val opacity: Double = 1.0,
    val color: Color? = null,
    val blendMode: BlendMode? = null,
    val gradientHandlePositions: List<Vector>? = null,
    val gradientStops: List<ColorStop>? = null,
    val scaleMode: String? = null,
    val imageTransform: Transform? = null,
    val scalingFactor: Double? = null,
    val rotation: Double? = null,
    val imageRef: String? = null,
    val filters: ImageFilters? = null,
    val gifRef: String? = null
)

data class Path(
    val path: String,
    val windingRule: String,
    val overrideID: Double? = null
)

data class Vector(
    val x: Double,
    val y: Double
)

data class Size(
    val width: Double,
    val height: Double
)

data class Transform(
    val matrix: List<List<Double>>
)

data class ImageFilters(
    val exposure: Double = 0.0,
    val contrast: Double = 0.0,
    val saturation: Double = 0.0,
    val temperature: Double = 0.0,
    val tint: Double = 0.0,
    val highlights: Double = 0.0,
    val shadows: Double = 0.0
)

data class ColorStop(
    val position: Double,
    val color: Color,
    val boundVariables: Map<String, VariableAlias>? = null
)

data class PaintOverride(
    val fills: List<Paint>,
    val inheritFillStyleId: String? = null
)

data class TypeStyle(
    val fontFamily: String,
    val fontPostScriptName: String,
    val paragraphSpacing: Double = 0.0,
    val paragraphIndent: Double = 0.0,
    val listSpacing: Double = 0.0,
    val italic: Boolean,
    val fontWeight: Double,
    val fontSize: Double,
    val textCase: String = "ORIGINAL",
    val textDecoration: String = "NONE",
    val textAutoResize: String = "NONE",
    val textTruncation: String = "DISABLED",
    val maxLines: Int? = null,
    val textAlignHorizontal: String,
    val textAlignVertical: String,
    val letterSpacing: Double,
    val fills: List<Paint>,
    val hyperlink: Hyperlink? = null,
    val opentypeFlags: Map<String, Double> = emptyMap(),
    val lineHeightPx: Double,
    val lineHeightPercent: Double = 100.0,
    val lineHeightPercentFontSize: Double? = null,
    val lineHeightUnit: String
)

data class Component(
    val key: String,
    val name: String,
    val description: String,
    val componentSetId: String? = null,
    val documentationLinks: List<DocumentationLink>,
    val remote: Boolean
)

data class ComponentSet(
    val key: String,
    val name: String,
    val description: String,
    val documentationLinks: List<DocumentationLink>,
    val remote: Boolean
)

data class Style(
    val key: String,
    val name: String,
    val description: String,
    val remote: Boolean,
    val styleType: StyleType
)

enum class StyleType {
    FILL,
    TEXT,
    EFFECT,
    GRID
}

enum class ShapeType {
    SQUARE,
    ELLIPSE,
    ROUNDED_RECTANGLE,
    DIAMOND,
    TRIANGLE_DOWN,
    PARALLELOGRAM_RIGHT,
    PARALLELOGRAM_LEFT,
    ENG_DATABASE,
    ENG_QUEUE,
    ENG_FILE,
    ENG_FOLDER,
    TRAPEZOID,
    PREDEFINED_PROCESS,
    SHIELD,
    DOCUMENT_SINGLE,
    DOCUMENT_MULTIPLE,
    MANUAL_INPUT,
    HEXAGON,
    CHEVRON,
    PENTAGON,
    OCTAGON,
    STAR,
    PLUS,
    ARROW_LEFT,
    ARROW_RIGHT,
    SUMMING_JUNCTION,
    OR,
    SPEECH_BUBBLE,
    INTERNAL_STORAGE
}

data class ConnectorEndpoint(
    val endpointNodeId: String,
    val position: Vector? = null,
    val magnet: ConnectorMagnet? = null
)

enum class ConnectorMagnet {
    AUTO,
    TOP,
    BOTTOM,
    LEFT,
    RIGHT
}

enum class ConnectorLineType {
    ELBOWED,
    STRAIGHT
}

data class ConnectorTextBackground(
    val cornerRadius: CornerRadius,
    val fills: List<Paint>
)

data class CornerRadius(
    val radius: Double? = null,
    val radii: List<Double>? = null
)

data class ComponentPropertyDefinition(
    val type: ComponentPropertyType,
    val defaultValue: Any?,
    val variantOptions: List<String>? = null,
    val preferredValues: List<InstanceSwapPreferredValue>? = null
)

data class ComponentProperty(
    val type: ComponentPropertyType,
    val value: Any?,
    val preferredValues: List<InstanceSwapPreferredValue>? = null,
    val boundVariables: Map<String, VariableAlias>? = null
)

enum class ComponentPropertyType {
    BOOLEAN,
    INSTANCE_SWAP,
    TEXT,
    VARIANT
}

data class InstanceSwapPreferredValue(
    val type: String, // 'COMPONENT' | 'COMPONENT_SET'
    val key: String
)

data class PrototypeDevice(
    val type: String, // 'NONE' | 'PRESET' | 'CUSTOM' | 'PRESENTATION'
    val size: Size? = null,
    val presetIdentifier: String? = null,
    val rotation: String? = null // 'NONE' | 'CCW_90'
)

data class Annotation(
    val label: String,
    val properties: List<AnnotationProperty>
)

data class AnnotationProperty(
    val type: String // 'width' | 'height' | 'maxWidth' | 'minWidth' | 'maxHeight' | 'minHeight' | 'fills' | 'strokes' | 'effects' | 'strokeWeight' | 'cornerRadius' | 'textStyleId' | 'textAlignHorizontal' | 'fontFamily' | 'fontStyle' | 'fontSize' | 'fontWeight' | 'lineHeight' | 'letterSpacing' | 'itemSpacing' | 'padding' | 'layoutMode' | 'alignItems' | 'opacity' | 'mainComponent'
)

@JsonClass(generateAdapter = true)
data class Measurement(
    val id: String,
    val start: MeasurementStartEnd,
    val end: MeasurementStartEnd,
    val offset: MeasurementOffset
)

@JsonClass(generateAdapter = true)
data class MeasurementStartEnd(
    val nodeId: String,
    val side: Side
)

enum class Side {
    TOP, RIGHT, BOTTOM, LEFT
}

sealed class MeasurementOffset {
    @JsonClass(generateAdapter = true)
    data class Inner(
        val type: String = "INNER",
        val relative: Double
    ) : MeasurementOffset()

    @JsonClass(generateAdapter = true)
    data class Outer(
        val type: String = "OUTER",
        val fixed: Double
    ) : MeasurementOffset()
}

//data class Measurement(
//    val id: String?,  // Optional
//    val start: MeasurementStartEnd?, // Optional
//    val end: MeasurementStartEnd?, // Optional
//    val offset: MeasurementOffset? // Optional
//)
//
//data class MeasurementStartEnd(
//    val nodeId: String?, // Optional
//    val side: String? // Optional: 'TOP' | 'RIGHT' | 'BOTTOM' | 'LEFT'
//)
//
//sealed class MeasurementOffset {
//    data class MeasurementOffsetInner(
//        val type: String? = "INNER", // Optional (but it might not be ideal to make this nullable)
//        val relative: Double? // Optional
//    ) : MeasurementOffset()
//
//    data class MeasurementOffsetOuter(
//        val type: String? = "OUTER", // Optional (but it might not be ideal to make this nullable)
//        val fixed: Double? // Optional
//    ) : MeasurementOffset()
//}

data class StrokeWeights(
    val top: Double,
    val right: Double,
    val bottom: Double,
    val left: Double
)

data class Overrides(
    val id: String,
    val overriddenFields: List<String>
)

data class VariableAlias(
    val type: String = "VARIABLE_ALIAS",
    val id: String
)

