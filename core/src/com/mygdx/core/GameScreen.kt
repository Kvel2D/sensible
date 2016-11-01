package com.mygdx.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.mygdx.core.entity.EntityFactory
import com.mygdx.core.entity.systems.PlayerInputSystem
import com.mygdx.core.entity.systems.RenderSystem

class GameScreen : ScreenAdapter() {
    val fail: Texture = Main.assets.get(AssetPaths.FAIL)
    val win: Texture = Main.assets.get(AssetPaths.WIN)
    var batchColor = Color(1f, 1f, 1f, 0f)

    init {
        gameState = GAME_STATE.NORMAL

        EntityFactory.background()
        player = EntityFactory.player(200f, 360f)

        val three1 = 120f
        val three2 = 360f
        val three3 = 600f
        val four1 = 90f
        val four2 = 270f
        val four3 = 450f
        val four4 = 630f
        val waveNumber = 15
        var x = 1500f
        for (i in 0..waveNumber) {
            var k = MathUtils.random(2)
            val xOffset = MathUtils.random(200f, 300f)
            x += xOffset
            if (k == 0) {
                x -= xOffset / 2
                continue
            } else {
                k = MathUtils.random(2)
                if (k == 0) {
                    k = MathUtils.random(4)
                    when (k) {
                        0 -> {
                            EntityFactory.enemy(x, four1)
                            EntityFactory.enemy(x, four2)
                            EntityFactory.enemy(x, four3)
                            EntityFactory.enemy(x, four4)
                        }
                        1 -> {
                            EntityFactory.enemy(x, four2)
                            EntityFactory.enemy(x, four3)
                            EntityFactory.enemy(x, four4)
                        }
                        2 -> {
                            EntityFactory.enemy(x, four1)
                            EntityFactory.enemy(x, four3)
                            EntityFactory.enemy(x, four4)
                        }
                        3 -> {
                            EntityFactory.enemy(x, four1)
                            EntityFactory.enemy(x, four2)
                            EntityFactory.enemy(x, four4)
                        }
                        4 -> {
                            EntityFactory.enemy(x, four1)
                            EntityFactory.enemy(x, four2)
                            EntityFactory.enemy(x, four3)
                        }
                    }
                } else {
                    k = MathUtils.random(5)
                    when (k) {
                        0 -> {
                            EntityFactory.enemy(x, three1)
                            EntityFactory.enemy(x, three2)
                            EntityFactory.enemy(x, three3)
                        }
                        1 -> {
                            EntityFactory.enemy(x, three2)
                            EntityFactory.enemy(x, three3)
                        }
                        2 -> {
                            EntityFactory.enemy(x, three1)
                            EntityFactory.enemy(x, three3)
                        }
                        3 -> {
                            EntityFactory.enemy(x, three1)
                            EntityFactory.enemy(x, three2)
                        }
                        4 -> {
                            EntityFactory.enemy(x, three1)
                        }
                        5 -> {
                            EntityFactory.enemy(x, three3)
                        }
                    }
                }
            }
        }
    }

    override fun render(deltaTime: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
//            Main.engine.systems.forEach {
//                it.setProcessing(false)
//            }
//            Main.engine.getSystem(RenderSystem::class.java).setProcessing(true)
//        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        Main.engine.update(deltaTime)

        when (gameState) {
            GAME_STATE.FAIL -> {
                stateTimer += deltaTime
                if (stateTimer > 1f) {
                    batchColor.a += 0.02f
                    if (batchColor.a > 1f) {
                        batchColor.a = 1f
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        restartGame()
                    }
                }
                Main.batch.begin()
                Main.batch.color = batchColor
                Main.batch.draw(fail, 0f, 0f)
                Main.batch.color = Color.WHITE
                Main.batch.end()
            }
            GAME_STATE.WIN -> {
                stateTimer += deltaTime
                if (stateTimer > 1f) {
                    batchColor.a += 0.02f
                    if (batchColor.a > 1f) {
                        batchColor.a = 1f
                    }
                }
                Main.batch.begin()
                Main.batch.color = batchColor
                Main.batch.draw(win, 0f, 0f)
                Main.batch.color = Color.WHITE
                Main.batch.end()
            }
        }
    }

    fun restartGame() {
        Main.engine.removeAllEntities()
        Main.gameScreen = GameScreen()
        Main.game.screen = Main.gameScreen
        Main.engine.systems.forEach {
            it.setProcessing(true)
        }
        stateTimer = 0f
        eyesKilled = 0
        bossHits = 0
        Main.engine.getSystem(PlayerInputSystem::class.java).fullMovement = false
    }
}
