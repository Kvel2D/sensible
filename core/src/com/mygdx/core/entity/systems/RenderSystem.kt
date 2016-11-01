package com.mygdx.core.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mygdx.core.entity.Mappers
import com.mygdx.core.entity.components.CircleShapeComponent
import com.mygdx.core.entity.components.TextureComponent
import com.mygdx.core.entity.components.TransformComponent
import java.util.*

class RenderSystem : SortedIteratingSystem {
    val camera: OrthographicCamera
    val batch: SpriteBatch
    val shapeRenderer = ShapeRenderer()
    var viewportWidth = 0f
    var viewportHeight = 0f

    constructor(batch: SpriteBatch) :
    super(Family.one(TextureComponent::class.java, CircleShapeComponent::class.java).all(TransformComponent::class.java).get(), ZComparator()) {
        this.batch = batch
        this.camera = initCamera()
    }

    constructor(priority: Int, batch: SpriteBatch) :
    super(Family.one(TextureComponent::class.java, CircleShapeComponent::class.java).all(TransformComponent::class.java).get(), ZComparator(), priority) {
        this.batch = batch
        this.camera = initCamera()
    }

    fun initCamera(): OrthographicCamera {
        val width = Gdx.graphics.width
        val height = Gdx.graphics.height
        val camera = OrthographicCamera(width.toFloat(), height.toFloat())
        camera.position.set(width / 2f, height / 2f, 0f)
        camera.update()

        return camera
    }

    override fun update(deltaTime: Float) {
        viewportWidth = camera.viewportWidth * camera.zoom
        viewportHeight = camera.viewportHeight * camera.zoom

        batch.projectionMatrix = camera.combined
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (Mappers.texture.has(entity)) {
            drawTexture(entity)
        } else if (Mappers.circleShape.has(entity)) {
            batch.end()
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            drawCircle(entity)
            shapeRenderer.end()
            batch.begin()
        }
    }

    fun drawTexture(entity: Entity) {
        val transformC = Mappers.transform.get(entity)
        val textureC = Mappers.texture.get(entity)

        val scale = transformC.scale
        val width = textureC.region.regionWidth.toFloat()
        val height = textureC.region.regionHeight.toFloat()
        val originX = 0.5f * width
        val originY = 0.5f * height

        // Frustum check
        val xMin = transformC.x + width * scale - originX
        val xMax = transformC.x - originX
        val yMin = transformC.y + height * scale - originY
        val yMax = transformC.y - originY
        if (xMin < camera.position.x - viewportWidth / 2
                || xMax > camera.position.x + viewportWidth / 2
                || yMin < camera.position.y - viewportHeight / 2
                || yMax > camera.position.y + viewportHeight / 2)
            return

        // Set batch color if entity has a color component
        val colorComponent = Mappers.color.get(entity)
        batch.color =
                if (colorComponent != null)
                    colorComponent.color
                else Color.WHITE

        batch.draw(textureC.region,
                Math.round(transformC.x - originX).toFloat(),
                Math.round(transformC.y - originY).toFloat(),
                originX,
                originY,
                width,
                height,
                scale,
                scale,
                transformC.angle)
    }

    fun drawCircle(entity: Entity) {
        val transformC = Mappers.transform.get(entity)
        val circleShapeC = Mappers.circleShape.get(entity)

        // Set batch color if entity has a color component
        val colorComponent = Mappers.color.get(entity)
        shapeRenderer.color =
                if (colorComponent != null)
                    colorComponent.color
                else Color.WHITE

        shapeRenderer.circle(transformC.x, transformC.y, circleShapeC.radius)
    }

    private class ZComparator : Comparator<Entity> {
        override fun compare(e1: Entity, e2: Entity): Int {
            return Integer.signum(Mappers.transform.get(e1).z - Mappers.transform.get(e2).z)
        }
    }
}