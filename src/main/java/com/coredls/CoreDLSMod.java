package com.coredls;

import com.coredls.ui.CoreDLSLoadingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class CoreDLSMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> client.execute(() -> client.setScreen(new CoreDLSLoadingScreen())));
    }
}
