package com.coredls.ui;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CoreDLSLoadingScreen extends Screen {
    private static final int BAR_WIDTH = 620;
    private static final int BAR_HEIGHT = 22;
    private static final int BLOCK_SIZE = 40;
    private final Random random = new Random();
    private final List<Snowflake> snowflakes = new ArrayList<>();
    private float progress;
    private long startTime = System.currentTimeMillis();
    private boolean finished;
    private long finishTime;

    public CoreDLSLoadingScreen() {
        super(Text.literal("CoreDLS"));
    }

    @Override
    protected void init() {
        super.init();
        snowflakes.clear();
        for (int i = 0; i < 70; i++) {
            snowflakes.add(new Snowflake(random.nextFloat() * width, random.nextFloat() * height,
                    random.nextFloat() * 2.0F + 1.2F, random.nextFloat() * 0.8F + 0.4F,
                    random.nextInt(2) == 0 ? 1 : 2));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, width, height, 0xFF020611, 0xFF102E5D);
        renderSnowflakes(context, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("CoreDLS 5.0"), width / 2, height / 2 - 110, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(getGreeting()), width / 2, height / 2 - 78, 0xFFB7D7FF);
        renderLoader(context);
        if (finished) renderCollapseBlocks(context);
    }

    private void renderSnowflakes(DrawContext context, float delta) {
        for (Snowflake flake : snowflakes) {
            flake.x += flake.speedX * delta;
            flake.y += flake.speedY * delta;
            if (flake.x > width + 4) flake.x = -4;
            if (flake.y > height + 4) flake.y = -4;
            context.fill((int) flake.x, (int) flake.y, (int) flake.x + flake.size, (int) flake.y + flake.size, 0xCCFFFFFF);
        }
    }

    private String getGreeting() {
        int hour = LocalTime.now().getHour();
        String nickname = MinecraftClient.getInstance().getSession().getUsername();
        if (hour >= 5 && hour < 12) return "доброе утро, " + nickname;
        if (hour >= 12 && hour < 18) return "добрый день, " + nickname;
        if (hour >= 18 && hour < 23) return "бодрый вечер, " + nickname;
        return "не спите? " + nickname;
    }

    private void renderLoader(DrawContext context) {
        int x = (width - BAR_WIDTH) / 2;
        int y = height / 2 + 12;
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0xFF0D1C32);
        context.fill(x, y, x + BAR_WIDTH, y + 2, 0xFF72B8FF);
        context.fill(x, y + BAR_HEIGHT - 2, x + BAR_WIDTH, y + BAR_HEIGHT, 0xFF72B8FF);
        context.fill(x, y, x + 2, y + BAR_HEIGHT, 0xFF72B8FF);
        context.fill(x + BAR_WIDTH - 2, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0xFF72B8FF);
        int fillWidth = (int) ((BAR_WIDTH - 8) * progress);
        if (fillWidth > 0) context.fill(x + 4, y + 4, x + 4 + fillWidth, y + BAR_HEIGHT - 4, 0xFF64D1FF);
    }

    private void renderCollapseBlocks(DrawContext context) {
        int cx = width / 2;
        int cy = height / 2;
        int radius = Math.min(220, (int) ((System.currentTimeMillis() - finishTime) / 18L));
        for (int gy = -6; gy <= 6; gy++) {
            for (int gx = -6; gx <= 6; gx++) {
                int distance = Math.abs(gx) + Math.abs(gy);
                if (radius > distance * BLOCK_SIZE) {
                    int x = cx + gx * BLOCK_SIZE - BLOCK_SIZE / 2;
                    int y = cy + gy * BLOCK_SIZE - BLOCK_SIZE / 2;
                    context.fill(x, y, x + BLOCK_SIZE, y + BLOCK_SIZE, 0x99000000);
                }
            }
        }
    }

    @Override
    public void tick() {
        if (!finished) {
            progress = Math.min(1.0F, (System.currentTimeMillis() - startTime) / 4200.0F);
            if (progress >= 1.0F) {
                finished = true;
                finishTime = System.currentTimeMillis();
            }
        } else if (System.currentTimeMillis() - finishTime > 2200L) {
            close();
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static class Snowflake {
        float x, y, speedX, speedY;
        int size;
        Snowflake(float x, float y, float speedX, float speedY, int size) {
            this.x = x; this.y = y; this.speedX = speedX; this.speedY = speedY; this.size = size;
        }
    }
}
