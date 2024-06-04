package com.hk416.fallingdowntino.scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hk416.fallingdowntino.R;
import com.hk416.fallingdowntino.object.MainCamera;
import com.hk416.fallingdowntino.object.Player;
import com.hk416.fallingdowntino.object.parachute.Parachute;
import com.hk416.fallingdowntino.object.tino.Tino;
import com.hk416.framework.audio.Sound;
import com.hk416.framework.object.GameObject;
import com.hk416.framework.object.SpriteAnimeObject;
import com.hk416.framework.render.DrawPipeline;
import com.hk416.framework.render.GameCamera;
import com.hk416.framework.scene.GameScene;
import com.hk416.framework.scene.SceneManager;
import com.hk416.framework.transform.Projection;
import com.hk416.framework.transform.Vector;
import com.hk416.framework.transform.Viewport;

public class PrepareGameScene extends GameScene {
    public enum Tags { Player };

    private static final String TAG = PrepareGameScene.class.getSimpleName();

    public static final float DURATION = 3.0f;

    private final Projection srcProjection = new Projection();
    private final Projection dstProjection = new Projection();
    private final Projection currProjection = new Projection();
    private final Vector srcPosition = new Vector();
    private final Vector dstPosition = new Vector();

    private float timer = 0.0f;

    public PrepareGameScene() {
        super(Tags.values().length);
        init();
    }

    private void init() {
        srcPosition.x = Player.X_POS;
        srcPosition.y = Player.Y_POS;
        srcPosition.z = 1.0f;

        dstPosition.x = MainCamera.X_POS;
        dstPosition.y = MainCamera.Y_POS;
        dstPosition.z = 1.0f;

        srcProjection.top = 0.5f * Projection.DEF_TOP;
        srcProjection.left = 0.5f * Projection.DEF_LEFT;
        srcProjection.bottom = 0.5f * Projection.DEF_BOTTOM;
        srcProjection.right = 0.5f * Projection.DEF_RIGHT;
        srcProjection.zNear = 0.0f;
        srcProjection.zFar = 100.0f;

        dstProjection.top = Projection.DEF_TOP;
        dstProjection.left = Projection.DEF_LEFT;
        dstProjection.bottom = Projection.DEF_BOTTOM;
        dstProjection.right = Projection.DEF_RIGHT;
        dstProjection.zNear = 0.0f;
        dstProjection.zFar = 100.0f;

        currProjection.top = srcProjection.top;
        currProjection.left = srcProjection.left;
        currProjection.bottom = srcProjection.bottom;
        currProjection.right = srcProjection.right;
        currProjection.zNear = 0.0f;
        currProjection.zFar = 100.0f;
    }

    @Override
    public void onEnter() {
        Log.d(TAG, "::onEnter >> 장면에 진입함");

        // 메인 카메라 생성
        GameCamera mainCamera = new GameCamera();
        mainCamera.setProjection(currProjection);
        mainCamera.setPosition(srcPosition.x, srcPosition.y);
        mainCamera.generateCameraTransform();

        // 메인 카메라 설정
        DrawPipeline.getInstance().setMainCamera(mainCamera);
        DrawPipeline.getInstance().setRatio(9.0f, 16.0f);

        // 임시 플레이어 낙하산 생성
        final int[] PARACHUTE_BITMAP_RES_IDS = new int[] {
                R.mipmap.parachute_default_0, R.mipmap.parachute_default_1,
                R.mipmap.parachute_default_2, R.mipmap.parachute_default_1
        };
        GameObject parachuteSprite = new SpriteAnimeObject(
                PARACHUTE_BITMAP_RES_IDS,
                Parachute.WIDTH,
                Parachute.HEIGHT,
                0.8f,
                true,
                false
        );
        parachuteSprite.setPosition(0.05f, 1.0f);

        // 임시 플레이어 생성
        final int[] TINO_BITMAP_RES_IDS = new int[] {
                R.mipmap.tino_default_0, R.mipmap.tino_default_1,
                R.mipmap.tino_default_2, R.mipmap.tino_default_1
        };
        GameObject tinoSprite = new SpriteAnimeObject(
                TINO_BITMAP_RES_IDS,
                Tino.WIDTH,
                Tino.HEIGHT,
                0.8f,
                true,
                false
        );

        // 임시 플레이어와 낙하산 결합후 장면에 추가.
        tinoSprite.setChild(parachuteSprite);
        tinoSprite.setPosition(Player.X_POS, Player.Y_POS);
        insertObject(Tags.Player, tinoSprite);

        Sound.preloadEffect(R.raw.effect0);
        Sound.preloadEffect(R.raw.effect1);
        Sound.preloadEffect(R.raw.effect2);
        Sound.playMusic(R.raw.game);
    }

    @Override
    public void onUpdate(float elapsedTimeSec, long frameRate) {
        timer = Math.min(timer + elapsedTimeSec, DURATION);

        // 카메라 Projection 갱신
        float delta = timer / DURATION;
        currProjection.top = srcProjection.top + (dstProjection.top - srcProjection.top) * delta;
        currProjection.left = srcProjection.left + (dstProjection.left - srcProjection.left) * delta;
        currProjection.bottom = srcProjection.bottom + (dstProjection.bottom - srcProjection.bottom) * delta;
        currProjection.right = srcProjection.right + (dstProjection.right - srcProjection.right) * delta;

        // 카메라 위치 갱신
        float x = srcPosition.x + (dstPosition.x - srcPosition.x) * delta;
        float y = srcPosition.y + (dstPosition.y - srcPosition.y) * delta;

        // 설정된 메인 카메라를 가져옵니다.
        GameCamera mainCamera = DrawPipeline.getInstance().getMainCamera();
        if (mainCamera == null) {
            throw new NullPointerException("설정된 메인 카메라가 없습니다!");
        }

        // 메인 카메라를 갱신합니다.
        mainCamera.setPosition(x, y);
        mainCamera.generateCameraTransform();

        if (timer >= DURATION) {
            SceneManager.getInstance().cmdChangeScene(new InGameScene());
        }
        super.onUpdate(elapsedTimeSec, frameRate);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        Viewport viewport = DrawPipeline.getInstance().getViewport();
        canvas.save();
        canvas.clipRect(viewport.left, viewport.top, viewport.right, viewport.bottom);
        canvas.drawColor(Color.parseColor("#FBEAFF"));
        super.onDraw(canvas);
        canvas.restore();
    }
}
