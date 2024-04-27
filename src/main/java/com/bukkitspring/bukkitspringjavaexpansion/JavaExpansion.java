package com.bukkitspring.bukkitspringjavaexpansion;

import com.bukkitspring.bukkitspringjavaexpansion.api.annotations.BindJavaPlugin;
import com.ning.spring.api.SpringApi;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public final class JavaExpansion extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        registerListener();

    }

    private void registerListener() {
        SpringApi.getApplicationContext().getBeansOfType(Listener.class).values().forEach(listener -> {
            getServer().getPluginManager().registerEvents(listener, this);
        });
    }

    private void registerListenerB() {
        SpringApi.getApplicationContext().getBeansOfType(Listener.class).values().forEach(listener -> {
            // 获取注解
            BindJavaPlugin[] annotationsByType = listener.getClass().getAnnotationsByType(BindJavaPlugin.class);
            if (annotationsByType.length == 0) {
                getServer().getPluginManager().registerEvents(listener, this);
                return;
            }
            BindJavaPlugin bindJavaPlugin = annotationsByType[0];
            Class<?> value = bindJavaPlugin.value();
            if (value.isAssignableFrom(JavaPlugin.class)) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(value.getSimpleName());
                getServer().getPluginManager().registerEvents(listener, plugin);
            }else {
                getServer().getPluginManager().registerEvents(listener, this);
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
